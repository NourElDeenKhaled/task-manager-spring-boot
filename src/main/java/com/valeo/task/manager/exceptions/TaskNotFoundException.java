package com.valeo.task.manager.exceptions;

public class TaskNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public TaskNotFoundException(String message) {
		super(message);
	}
}
