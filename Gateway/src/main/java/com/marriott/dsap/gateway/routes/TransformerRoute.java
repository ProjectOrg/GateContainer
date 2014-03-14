package com.marriott.dsap.gateway.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;

public class TransformerRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		String getRoomTypeListInfo = "http://localhost:8090/Resource/rest/getRoomTypeList?id=nycmc";
		String propertyInfo = "http://localhost:8090/Resource/rest/PropertyInfo?id=nycmc";
		
		
		XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
		xmlJsonFormat.setEncoding("UTF-8");
		//xmlJsonFormat.setTrimSpaces(true);
		
		//from("direct:start12").setHeader(Exchange.HTTP_METHOD,constant("GET")).to(getRoomTypeListInfo ).marshal(xmlJsonFormat).to("mock:json");;
		
		from("direct:start12").setHeader(Exchange.HTTP_METHOD, constant("GET")).to(propertyInfo).marshal(xmlJsonFormat).to("mock:json");

	}

}
