package com.marriott.dsap.gateway.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cache.CacheConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.validation.PredicateValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marriott.dsap.gateway.constants.PredicateConstants;
import com.marriott.dsap.gateway.exception.GatewayException;
import com.marriott.dsap.gateway.initialize.InitializeEnvironment;
import com.marriott.dsap.gateway.processor.ContentLookupProcessor;
import com.marriott.dsap.gateway.processor.ErrorProcessor;
import com.marriott.dsap.gateway.processor.PropertiesValidationProcessor;
import com.marriott.reference.cache.coherence.processors.CoherenceResultProcessor;

/**
 * Route for property service ID method
 * @author p.ravindra.gupte
 *
 */
public class PropertyIdRoute extends RouteBuilder {

	//Logger 
	final Logger log = LoggerFactory.getLogger("logger"); 

	/* Actual route class for property service implementation in gateway layer
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		
		
		//Predicate validatePropertyId = header(Exchange.HTTP_PATH).regex("[a-zA-Z]{5}");
		//Predicate validateMessageId = header(Exchange.HTTP_PATH).regex("[a-zA-Z]{5}");
		
		Predicate validateMessageId= header(PredicateConstants.MESSAGE_ID).regex(PredicateConstants.MESSAGE_ID_REGEX); 
		Predicate validatePropertyId= header(PredicateConstants.PROPERTY_ID).regex(PredicateConstants.PROPERTY_ID_REGEX);
	
		onException(PredicateValidationException.class).handled(true).process(new ErrorProcessor()).marshal().json(JsonLibrary.Jackson).to("mock:result"); 

		from("direct:GateWayPropertybyID").setHeader(Exchange.HTTP_METHOD, constant("GET"))
			.validate(validatePropertyId).validate(validateMessageId).to("seda:doComplexValidations");
		
		
		//Validation route for business validation is processor
		from("seda:doComplexValidations")
			.setHeader(Exchange.HTTP_METHOD, constant("GET"))
				.doTry()
					.process(new PropertiesValidationProcessor())
					.to("seda:processPropertybyID")
				.doCatch(GatewayException.class)
					.process(new ContentLookupProcessor())
					.to(InitializeEnvironment.getRouteValue("contentProperty"));

		//Main processing route 
		from("seda:processPropertybyID")
			.to ("direct:RetrieveProperty")
	     	.choice()
	     		// Evaluate whether the results from the cache was found in the Gateway cache.
	    		.when(header (CacheConstants.CACHE_ELEMENT_WAS_FOUND).isNull ())
	    			// Scenario is the element was not found in the Gateway cache.
	    			// Make call to atomic service.
	    			.setHeader(Exchange.HTTP_METHOD, constant("GET"))
					.to(InitializeEnvironment.getRouteValue("propertybyId")) 			
				// Convert from input stream to valid value.
				.process(new CoherenceResultProcessor ())
				// Store in cache when returned.
					.to ("direct:StoreProperty");
	}
}