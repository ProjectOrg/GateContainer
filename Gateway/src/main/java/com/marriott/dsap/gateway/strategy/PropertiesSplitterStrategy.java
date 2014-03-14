package com.marriott.dsap.gateway.strategy;

import java.util.Locale;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;

import com.marriott.dsap.gateway.initialize.LoggingKeys;

public class PropertiesSplitterStrategy implements AggregationStrategy{
	
	static IMessageConveyor messageConveyor = new MessageConveyor(
			Locale.ENGLISH);
	// create the LogLoggerFactory
	static LocLoggerFactory locLoggerFactory = new LocLoggerFactory(
			messageConveyor);
	//Add Logger
	final static LocLogger debugLogger = locLoggerFactory.getLocLogger("DebugLogger");
	
	/* 
	 * This class will combine the output post splitting into final response
	 * Once splitter breaks the request in multiple values for each iteration this class will be invoked by camel with 
	 *  Old and new exchange. Manipulate body and added it back on old exchange so that it will be available for next iteration 
	 * @see org.apache.camel.processor.aggregate.AggregationStrategy#aggregate(org.apache.camel.Exchange, org.apache.camel.Exchange)
	 */
	@Override
	public Exchange aggregate(Exchange original, Exchange resource) {
		
		if (original==null){

			return resource;
		}

		String messageID=original.getIn().getHeader("MessageID", String.class);
		
		//Body modification done here to pass the final body 
		
		String originalBody=original.getIn().getBody(String.class); 
		String newBody=resource.getIn().getBody(String.class);
		String finalBody=originalBody+newBody;

		debugLogger.debug(LoggingKeys.Splitter_Msg_Body,messageID,originalBody);
		
		original.getIn().setBody(finalBody);
		

		original.getIn().setHeader(Exchange.HTTP_QUERY, finalBody);
		debugLogger.debug(LoggingKeys.Splitter_Msg_Final_Body,messageID,finalBody);

		return original;	
	}



}
