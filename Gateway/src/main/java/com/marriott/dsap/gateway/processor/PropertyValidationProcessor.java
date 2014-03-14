package com.marriott.dsap.gateway.processor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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


public class PropertyValidationProcessor implements Processor {

	static IMessageConveyor messageConveyor = new MessageConveyor(
			Locale.ENGLISH);
	// create the LogLoggerFactory
	static LocLoggerFactory locLoggerFactory = new LocLoggerFactory(
			messageConveyor);
	//Add Logger
	final static LocLogger errorLogger = locLoggerFactory.getLocLogger("ErrorLogger"); 

	/* 
	 * This method will be used for validating input message
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(Exchange exchange) throws GatewayException {
		String body = exchange.getIn().getBody(String.class);

		//Message ID for Logging
		String messageID=exchange.getIn().getHeader("MessageID", String.class);

		String query = body;


		Map<String, String> reqparams = new HashMap<String, String>();
		String[] strParams = query.split("&");
		for (String params : strParams) {
			String[] param = params.split("=");
			String name = "";
			String value = "";
			if (param.length != 2) {
				name = param[0];
				value = "";
			} else {
				name = param[0];
				value = param[1];
			}
			reqparams.put(name, value);
		}
		String cityValue = reqparams.get("City");
		if (!cityValue.equalsIgnoreCase("null") && cityValue != null
				&& cityValue.length() != 0) {
			String regex = "[a-zA-Z]+";
			if (cityValue.matches(regex)) {

				exchange.getIn().setBody(body);
			} else {
				exchange.getIn().setBody(body);

				exchange.getIn().setHeader("error",
						"Error occured: Invalid city name");
				errorLogger.error(LoggingKeys.Invalid_City_Error,messageID);
				GatewayException gatewayException = new GatewayException();
				gatewayException.setErrorCode("C101M");
				throw  gatewayException;
			}
		} else {
			exchange.getIn().setBody(
					body + "Error occured: City may be null or blank");
			exchange.getIn().setHeader("error",
					"Error occured: City may be null or blank");

			errorLogger.error(LoggingKeys.City_Blank_Error,messageID);
			GatewayException gatewayException = new GatewayException();
			gatewayException.setErrorCode("E103M");
			throw  gatewayException;
		}	


	}



}
