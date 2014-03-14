package com.marriott.dsap.gateway.webservices;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

//Camel related imports
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

// Project related imports
import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;

import com.marriott.dsap.gateway.business.PropertyBusiness;
//Imports for Logger


/*
 * This is the start point of the application. For every service there will be one class of similar structure. 
 * Rest web service build in jersey 
 * 
 */
@Path("/properties")
public class PropertyService {
	private PropertyBusiness propertyBusiness;
	
	@javax.ws.rs.core.Context ServletContext context;
	@javax.ws.rs.core.Context HttpServletRequest httpRequest;

	
	static IMessageConveyor messageConveyor = new MessageConveyor(
			Locale.ENGLISH);
	
	// create the LogLoggerFactory
	static LocLoggerFactory locLoggerFactory = new LocLoggerFactory(
			messageConveyor);
	
	final static LocLogger errorLogger = locLoggerFactory
	.getLocLogger("ErrorLogger");

	public PropertyService () throws Exception
	{
		propertyBusiness = new PropertyBusiness ();
	}


	/**
	 * This method will get servlet context from jersey's controller
	 * This context will be used for session management purpose
	 * @param context
	 */
	@Context
	public void setServletContext(ServletContext context) {

		this.context = context;

	}

	@Context
	public void setHttpServletRequest(HttpServletRequest httpRequest) {

		this.httpRequest = httpRequest;

	}


	/**
	 * Start point for property by ID method for property service 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("{ID}")
	@Produces({MediaType.APPLICATION_JSON })
	public String getPropertybyId(@PathParam("ID") String id) throws Exception {

		String response=null;
		String messageId = httpRequest.getHeader ("MessageID");

		//Header Additions 
		HashMap <String,Object> hashCheck = new HashMap<String,Object>();

		hashCheck.put("SessionId",null);
		hashCheck.put("Local",httpRequest.getHeader("Local"));
		hashCheck.put("MessageID", "12345");
//		hashCheck.put("MessageID",httpRequest.getHeader("MessageID"));
		hashCheck.put(Exchange.HTTP_PATH, id);
		
		// Placeholder name until permanent is decided upon.
		hashCheck.put("key", id);

		response = propertyBusiness.execute (hashCheck);

		System.out.println (response);
		
		return response;

	}

}