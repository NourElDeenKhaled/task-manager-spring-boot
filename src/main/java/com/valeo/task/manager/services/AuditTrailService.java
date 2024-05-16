package com.valeo.task.manager.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.valeo.task.manager.interfaces.ITask;

@Service
public class AuditTrailService {

	List<String> audits;
	
	public AuditTrailService() {
		audits = new ArrayList<String>();
	}
	
	public void addAction(ITask task, String text) {
		this.audits.add("Action for Task '" + task.getTitle() +"' ID: " + task.getId() + ": " + text);
	}
	
	public void addAction(String text) {
		this.audits.add("Action: " + text);
	}
	
	public List<String> getAllActions() {
		return audits;
	}
	
	public void setAllActions(List<String> actions) {
		this.audits = actions;
	}
}
