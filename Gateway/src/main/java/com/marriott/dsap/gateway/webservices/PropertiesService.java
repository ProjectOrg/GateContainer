package com.marriott.dsap.gateway.webservices;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;

import com.marriott.dsap.gateway.initialize.InitializeEnvironment;
import com.marriott.dsap.gateway.initialize.LoggingKeys;
import com.marriott.dsap.resource.webservices.CommonServices;


/*
 * This is the start point of the application. For every service there will be one class of similar structure. 
 * Rest web service build in jersey 
 * 
 */

@Path("/propertiesmultipleID")

public class PropertiesService extends CommonServices {

	@javax.ws.rs.core.Context ServletContext context;

	static IMessageConveyor messageConveyor = new MessageConveyor(
			Locale.ENGLISH);
	
	// create the LogLoggerFactory
	static LocLoggerFactory locLoggerFactory = new LocLoggerFactory(
			messageConveyor);

	final static LocLogger biLogger = locLoggerFactory.getLocLogger("BILogger");

	// Debug Logger
	final static LocLogger debugLogger = locLoggerFactory
			.getLocLogger("DebugLogger");
	// Error Logger
	final static LocLogger errorLogger = locLoggerFactory
			.getLocLogger("ErrorLogger");
	// Info Logger
	final static LocLogger infoLogger = locLoggerFactory
			.getLocLogger("InfoLogger");

	/**
	 * This method will get servlet context from jersey's controller
	 * This context will be used for session management purpose
	 * @param context
	 */
	@Context
	public void setServletContext(ServletContext context) {

		this.context = context;

	}

	public PropertiesService () throws Exception
	{
		super ("com/marriott/dsap/gateway/initialize/Routing.properties");
	}

	/**
	 * @param city
	 * @param property
	 * @param httpRequest
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	//@Produces({"application/json"})
	public String getProperties(@QueryParam("City") String city,@QueryParam("Property") String property,  @Context HttpServletRequest httpRequest) {

		//intialize a default response	
		String response=null;

		String req="City="+city+"&type="+property; 

		//Header Additions 

		Map <String,Object> hashCheck = new HashMap<String,Object>();

		// SessionID header is set to null and will be used once session management is integrated in this code
		hashCheck.put("SessionId",null);

		// Local header is used for retriving message in Local language this header is expected from client 
		//and will be used in future with multiple language support
		hashCheck.put("Local",httpRequest.getHeader("Local"));

		//Message ID Header is used for tracking messages end to end by inserting this in Log message for entire flow 
		hashCheck.put("MessageID",httpRequest.getHeader("MessageID"));

		Map headeMap=(Map)hashCheck;

		try{

			ProducerTemplate template = InitializeEnvironment.getProducerTemplate(camelContext, alreadyStarted);

			// for BI Feed Request logging 
			biLogger.debug(LoggingKeys.Request_Message, req);

			response=template.requestBodyAndHeaders("direct:GateWayProperty", req, headeMap, String.class);

			if (response.length()==0){
				String req1="";
				response = template.requestBody("direct:Retrival",req1, String.class);
			}
			
			biLogger.debug(LoggingKeys.Response_Message,httpRequest.getHeader("MessageID"),response);

		}catch(Exception e){
			
			errorLogger.error(LoggingKeys.Error_Message,httpRequest.getHeader("MessageID"),e.toString());
		}


		return response;

	}
	//Method ends here

}
