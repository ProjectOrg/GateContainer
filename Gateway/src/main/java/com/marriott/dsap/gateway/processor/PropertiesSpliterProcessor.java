package com.marriott.dsap.gateway.processor;

import java.util.Locale;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import com.marriott.dsap.gateway.initialize.LoggingKeys;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;

public class PropertiesSpliterProcessor implements Processor{

	static IMessageConveyor messageConveyor = new MessageConveyor(
			Locale.ENGLISH);
	// create the LogLoggerFactory
	static LocLoggerFactory locLoggerFactory = new LocLoggerFactory(
			messageConveyor);
	//Add Logger
	final static LocLogger debugLogger = locLoggerFactory.getLocLogger("DebugLogger"); 

	/* This call will be used for manipulate the body to pass to the next webservice on route
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	public void process(Exchange ex) throws Exception{


		String body =ex.getIn().getBody(String.class);
		body="Property="+body;

		String messageID=ex.getIn().getHeader("MessageID", String.class);

		debugLogger.debug(LoggingKeys.Splitter_Body,messageID,body);

		ex.getIn().setHeader(Exchange.HTTP_QUERY, body);
	}

}
