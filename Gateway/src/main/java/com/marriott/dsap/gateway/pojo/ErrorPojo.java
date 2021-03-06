package com.marriott.dsap.gateway.pojo;
/**
Description for a ErrorPojo.
Another line of description.
Adding this from gitHub.
Adding another comment locally.
Another comment from gitHub


 */
public class ErrorPojo {
	
	private String errorCode;
	private String errorMessage;
	
	public ErrorPojo (String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "Error [errorCode=" + errorCode + ", errorMessage="
				+ errorMessage + "]";
	}

}
