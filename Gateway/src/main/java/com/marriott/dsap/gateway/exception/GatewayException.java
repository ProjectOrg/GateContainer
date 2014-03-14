package com.marriott.dsap.gateway.exception;

public class GatewayException extends Exception {

	String errorCode = null;

	/**
	 * Setting the error code in this method
	 * @param errorCode
	 */
	public void setErrorCode(String errorCode){
		this.errorCode=errorCode;
	}

	/**
	 * Getting the error code set for this exception in this method
	 * @return
	 */
	public String getErrorCode(){
		return this.errorCode;
	}

	/* 
	 * This method will return the error code as required
	 * @see java.lang.Throwable#toString()
	 */
	public String toString (){
		return this.errorCode;
	}


}
