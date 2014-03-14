package com.marriott.dsap.gateway.constants;

import org.apache.camel.Exchange;

public class PredicateConstants {
	
	public static final String PROPERTY_ID_REGEX = "[a-zA-Z]{5}";
	public static final String MESSAGE_ID_REGEX = "[0-9]{5}";
	
	public static final String MESSAGE_ID = "MessageID";
	public static final String PROPERTY_ID = Exchange.HTTP_PATH;

}
