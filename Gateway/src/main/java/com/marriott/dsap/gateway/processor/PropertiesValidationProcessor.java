package com.marriott.dsap.gateway.processor;

import java.util.Locale;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;

import com.marriott.dsap.gateway.exception.GatewayException;
import com.marriott.dsap.gateway.initialize.LoggingKeys;


public class PropertiesValidationProcessor implements Processor {

	static IMessageConveyor messageConveyor = new MessageConveyor(
			Locale.ENGLISH);
	// create the LogLoggerFactory
	static LocLoggerFactory locLoggerFactory = new LocLoggerFactory(
			messageConveyor);
	//Add Logger
	final static LocLogger debugLogger = locLoggerFactory.getLocLogger("DebugLogger"); 

	/* 
	 * This method will be used for validating input message
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(Exchange exchange) throws GatewayException {
		debugLogger.debug(LoggingKeys.Inside_Gateway_Message);
		//Code business validations in this class
	}
}

