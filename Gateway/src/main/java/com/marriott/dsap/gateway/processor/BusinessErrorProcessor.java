package com.marriott.dsap.gateway.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.processor.validation.PredicateValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marriott.dsap.gateway.constants.PredicateConstants;
import com.marriott.dsap.gateway.initialize.ApplicationConstant;

/**
 * This class will convert the errors reported by predicate validations to the 
 * error code so that corresponding Message can be obtain from Atomic service
 * After this class content look up service on resource layer will be invoked which will invoke Atomic service
 * @author p.ravindra.gupte
 *
 */
public class BusinessErrorProcessor implements Processor{

	//Add Loger
	final static Logger log = LoggerFactory.getLogger("logger"); 

	@Override
	public void process(Exchange exchange) throws Exception {

		String messageID=exchange.getIn().getHeader(ApplicationConstant.Header_MessageID, String.class);

		Throwable businessException;

		businessException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);

		PredicateValidationException predicateValidationException = (PredicateValidationException)businessException;

		Predicate predicate = predicateValidationException.getPredicate();

		String predicateToString = predicate.toString();

		String contentCode="";

		if(predicateToString.contains(PredicateConstants.PROPERTY_ID))

		{
			contentCode = ApplicationConstant.PropertyIDValiodationFailedCode;
		}

		else if(predicateToString.contains(PredicateConstants.MESSAGE_ID))

		{
			contentCode = ApplicationConstant.MessageIDValiodationFailedCode;
		}

		log.debug(ApplicationConstant.Header_MessageID+messageID+ApplicationConstant.Header_ContentCode+ contentCode);

		String URL=contentCode+"/"+messageID;
		exchange.getIn().setHeader(Exchange.HTTP_PATH,URL);

	}

}
