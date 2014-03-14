package com.marriott.dsap.gateway.initialize;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogMessageConverter extends ClassicConverter {


	@Override
	public String convert(ILoggingEvent event) {
		String key = event.getFormattedMessage();

		ResourceBundle resourceBundle = ResourceBundle.getBundle(
				"com.marriott.dsap.gateway.initialize.LogMessages", Locale.getDefault());
		if (resourceBundle.containsKey(key)) {
			String logMessage = resourceBundle.getString(key);
			Object[] argumentsArray = event.getArgumentArray();
			if(logMessage.contains("{0}"))
			{
				return MessageFormat.format(logMessage, argumentsArray);
			}
			return logMessage;
		} else {
			return resourceBundle.getString("No_Logging_Key");
		}
	}

}