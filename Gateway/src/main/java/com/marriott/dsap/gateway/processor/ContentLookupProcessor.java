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

public class ContentLookupProcessor implements Processor {

	static IMessageConveyor messageConveyor = new MessageConveyor(
			Locale.ENGLISH);
	// create the LogLoggerFactory
	static LocLoggerFactory locLoggerFactory = new LocLoggerFactory(
			messageConveyor);
	//Add Logger
	final static LocLogger debugLogger = locLoggerFactory.getLocLogger("DebugLogger"); 

	/* This class is provided for doing any modification with the error message so that content lookup 
	 * service can handle the error message
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(Exchange exchange) throws Exception {

		String messageID=exchange.getIn().getHeader("MessageID", String.class);
		String contentCode=exchange.getIn().getHeader("ContentCode", String.class);

		debugLogger.debug(LoggingKeys.Content_Message,messageID,contentCode);

		String URL="errorCode="+contentCode+"&messageId="+messageID;
		exchange.getIn().setHeader(Exchange.HTTP_QUERY,URL);
	}
}
