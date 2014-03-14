package com.marriott.dsap.gateway.routes;

import java.util.Locale;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cache.CacheConstants;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

//Project related classes are imported below

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;

import com.marriott.dsap.gateway.exception.GatewayException;
import com.marriott.dsap.gateway.initialize.InitializeEnvironment;
import com.marriott.dsap.gateway.initialize.LoggingKeys;
import com.marriott.dsap.gateway.processor.ContentLookupProcessor;
import com.marriott.dsap.gateway.processor.PropertiesSpliterProcessor;
import com.marriott.dsap.gateway.processor.PropertyValidationProcessor;
import com.marriott.dsap.gateway.strategy.PropertySplitterStrategy;

public class PropertyRoute extends RouteBuilder {
	
	//Logger 
	static IMessageConveyor messageConveyor = new MessageConveyor(
			Locale.ENGLISH);
	// create the LogLoggerFactory
	static LocLoggerFactory locLoggerFactory = new LocLoggerFactory(
			messageConveyor);
	//Add Logger
	final static LocLogger infoLogger = locLoggerFactory.getLocLogger("InfoLogger");

	/* Actual route class for property service implementation in gateway layer
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {

		String cache="cacheRetrival1234";

		//Mandatory Element Chack can be done by below method also
		/*
	        Predicate SessionIDCheck= header("SessionId").isNotNull();
	        Predicate LocalIDCheck= header("Local").isNotNull();
	        Predicate MessageIDCheck= header("MessageId").isNotNull();
		 */

		AggregationStrategy propertySplitterStrategy= new PropertySplitterStrategy();


		// logging from java on regular logger
		infoLogger.info(LoggingKeys.Logging_Init);
		
		// below code will do validations and exception handling on route
		from("direct:GateWayProperty")
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.doTry().process(new PropertyValidationProcessor())
		.to("seda:result")
		.doCatch(GatewayException.class)
		.process(new ContentLookupProcessor())
		.to(InitializeEnvironment.getRouteValue("contentProperty"));

		// below code will split and properties in to one property each will do business logic processing, 
		//aggregate the response and add it to Cache 
		from("seda:result")
		.setHeader(Exchange.HTTP_METHOD,constant("GET")).setHeader(Exchange.HTTP_QUERY, body())
		.to(InitializeEnvironment.getRouteValue("search"))
		.split(body(String.class).tokenize("}")).process(new PropertiesSpliterProcessor())
		.to(InitializeEnvironment.getRouteValue("property"))
		.aggregate(propertySplitterStrategy).header(Exchange.HTTP_METHOD).ignoreInvalidCorrelationKeys().completionSize(4)
		.setHeader(CacheConstants.CACHE_OPERATION,constant(CacheConstants.CACHE_OPERATION_ADD)).setHeader(CacheConstants.CACHE_KEY, constant(cache)).to("cache://TestCache");
		//.to("seda:resultAgre");
		
		//below code will help caller to retrirve data back from cache
		from("direct:Retrival").setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_GET)).setHeader(CacheConstants.CACHE_KEY, constant(cache)).to("cache://TestCache");


	}

}

