package com.valeo.task.manager.exceptions;

public class EmptyValueException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EmptyValueException(String message) {
		super(message);
	}
}
