/**
 * 
 */
package com.marriott.dsap.gateway.business;

import java.util.Locale;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;

import com.marriott.dsap.gateway.initialize.InitializeEnvironment;
import com.marriott.dsap.gateway.initialize.LoggingKeys;
import com.marriott.dsap.resource.webservices.CommonServices;

/**
 * @author Accenture
 *
 */
public class PropertyBusiness extends CommonServices
{
	private ProducerTemplate template;

	
	static IMessageConveyor messageConveyor = new MessageConveyor(
			Locale.ENGLISH);
	
	// create the LogLoggerFactory
	static LocLoggerFactory locLoggerFactory = new LocLoggerFactory(
			messageConveyor);
	
	final static LocLogger errorLogger = locLoggerFactory
	.getLocLogger("ErrorLogger");


	public PropertyBusiness () throws Exception
	{
		super ("com/marriott/dsap/resource/property/initialize/Routing.properties");

		template = InitializeEnvironment.getProducerTemplate(camelContext, alreadyStarted);
	}
	
	public String execute (Map<String, Object> headerMap) throws Exception
	{
		String response=null;
		String req="ID=" + headerMap.get (Exchange.HTTP_PATH); 

		try {
			response = template.requestBodyAndHeaders("direct:GateWayPropertybyID", req, headerMap, String.class);
		}catch(Exception e){
			errorLogger.error (LoggingKeys.Error_Message, headerMap.get ("MessageId"), e.toString());

			throw e;
		}
		
		return response;
	}
}
