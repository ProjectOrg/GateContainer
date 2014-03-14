package com.marriott.dsap.gateway.initialize;

import java.util.Locale;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;

import com.marriott.dsap.gateway.routes.PropertyIdRoute;
import com.marriott.dsap.resource.property.pojo.Property;
import com.marriott.reference.cache.coherence.routing.CoherencePropertyRouter;
import com.marriott.reference.coherence.cache.CacheOperations;
import com.marriott.reference.coherence.cache.CoherenceCacheOperations;

public class InitializeEnvironment {
	static IMessageConveyor messageConveyor = new MessageConveyor(
			Locale.ENGLISH);
	// create the LogLoggerFactory
	static LocLoggerFactory locLoggerFactory = new LocLoggerFactory(
			messageConveyor);
	// Debug Logger
	final static LocLogger debugLogger = locLoggerFactory
			.getLocLogger("DebugLogger");
	// Error Logger
	final static LocLogger errorLogger = locLoggerFactory
			.getLocLogger("ErrorLogger");
	// Info Logger
	final static LocLogger infoLogger = locLoggerFactory
			.getLocLogger("InfoLogger");

	// Initialize the cache between calls.
//	static CacheOperations<?, ?> cacheInstance = CoherenceCacheOperations.getInstance();
	

	public InitializeEnvironment ()
	{
		CoherenceCacheOperations.getInstance();	
	}
	
	public static ProducerTemplate getProducerTemplate(CamelContext context, boolean alreadyStarted) throws Exception {

		// For camel logging add below line
		if (!alreadyStarted)
		{
			context.addRoutes(new PropertyIdRoute());
			context.addRoutes (new CoherencePropertyRouter<String, Property> ());
		}
		
		ProducerTemplate producerTemplate = context.createProducerTemplate();

		return producerTemplate;
	}

	public static String getRouteValue(String routeKey) throws Exception {
		String routeString = "";
		try {
			PropertiesConfiguration config = new PropertiesConfiguration("/usr/projects/reference-application/Gateway/src/com/marriott/dsap/gateway/initialize/Routing.properties");
//					"C:\\Users\\puneet.x.kuma\\Gateway\\src\\com\\marriott\\dsap\\gateway\\initialize\\Routing.properties");
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
			routeString = config.getString(routeKey);
			infoLogger.info("Route properties :: routeKey : " + routeKey
					+ " :: routeString : " + routeString);
		} catch (ConfigurationException e) {

			errorLogger.error("Error while reading routing properties file : "
					+ e.getMessage());
			e.printStackTrace();
		}
		return routeString;
	}

	public static String getErrorValue(String errorKey) throws Exception {
		String errorString = "";
		try {
			PropertiesConfiguration config = new PropertiesConfiguration("/usr/projects/reference-application/Gateway/src/com/marriott/dsap/gateway/initialize/Error.properties");
//					"C:\\Users\\puneet.x.kuma\\Gateway\\src\\com\\marriott\\dsap\\gateway\\initialize\\Error.properties");
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
			errorString = config.getString(errorKey);
			infoLogger.info("Error properties :: errorKey : " + errorKey
					+ " :: errorString : " + errorString);
		} catch (ConfigurationException e) {

			errorLogger.error("Error while reading error properties file : "
					+ e.getMessage());
			e.printStackTrace();
		}
		return errorString;
	}
}