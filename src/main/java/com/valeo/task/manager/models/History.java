package com.valeo.task.manager.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.valeo.task.manager.interfaces.IHistory;

public class History implements IHistory {
	private String date;
	private String text;
	
	public History() {
		this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}

	public History(String text) {
		this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
