package com.marriott.dsap.gateway.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.processor.validation.PredicateValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marriott.dsap.gateway.constants.PredicateConstants;
import com.marriott.dsap.gateway.initialize.InitializeEnvironment;
import com.marriott.dsap.gateway.pojo.ErrorPojo;

public class ErrorProcessor implements Processor {

	//Add Loger
	final static Logger log = LoggerFactory.getLogger("logger"); 

	@Override
	public void process(Exchange exchange) throws Exception {
		Throwable businessException;

		businessException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);

		PredicateValidationException predicateValidationException = (PredicateValidationException)businessException;

		Predicate predicate = predicateValidationException.getPredicate();

		String predicateToString = predicate.toString();

		String errorCode="";

		if(predicateToString.contains(PredicateConstants.PROPERTY_ID))

		{
			errorCode = "PR102";
		}

		else if(predicateToString.contains(PredicateConstants.MESSAGE_ID))

		{
			errorCode = "PR101";
		}

		String errorMessage = InitializeEnvironment.getErrorValue(errorCode);
		
		ErrorPojo errorPojo = new ErrorPojo(errorCode,errorMessage);

		exchange.getIn().setBody(errorPojo);

	}
}
