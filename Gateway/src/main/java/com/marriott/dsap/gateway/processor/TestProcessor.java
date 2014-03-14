package com.marriott.dsap.gateway.processor;


import org.apache.camel.Processor;
import org.apache.camel.Exchange;


public class TestProcessor implements Processor {
	
	@Override
	public void process(Exchange exchange) throws Exception {
	
		System.out.println("headers map : "+exchange.getIn().getHeaders());
		System.out.println("Body Pankaj :"+exchange.getIn().getBody(String.class) );
		Exception e=exchange.getException();


		
	}
	
}
