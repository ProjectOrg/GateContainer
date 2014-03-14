package com.marriott.dsap.gateway.initialize;

	import ch.qos.cal10n.BaseName;
	import ch.qos.cal10n.LocaleData;
	import ch.qos.cal10n.Locale;
	 
	@LocaleData(defaultCharset = "UTF-8", value = {@Locale("en")})
	@BaseName("com.marriott.dsap.gateway.initialize.LogMessages")
	public enum LoggingKeys {
	    /* Define parameterized log messages of any type 
	     * (i.e. trace, debug, info, warn, error) in this file
	     */
	 
		Content_Message,
		Inside_Gateway_Message,
		Invalid_City_Error,
		City_Blank_Error,
		Logging_Init,
		Splitter_Msg_Body,
		Splitter_Msg_Final_Body,
		Request_Message,
		Response_Message,
		Error_Message,
		Splitter_Body;
	}

