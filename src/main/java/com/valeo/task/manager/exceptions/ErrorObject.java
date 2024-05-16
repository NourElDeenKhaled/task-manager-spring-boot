package com.valeo.task.manager.exceptions;

import java.util.Date;

public class ErrorObject {
	private Integer statusCode;
	private String message;
	private Date timestamp;
	
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public String getMessage() {
		return message;
	}
	public Date getTimestamp() {
		return timestamp;
	}
}
