package com.valeo.task.manager.models;

import java.util.ArrayList;
import java.util.List;

import com.valeo.task.manager.enums.CategoryEnum;
import com.valeo.task.manager.enums.PriorityEnum;
import com.valeo.task.manager.enums.StatusTypesEnum;
import com.valeo.task.manager.interfaces.IComment;
import com.valeo.task.manager.interfaces.IHistory;
import com.valeo.task.manager.interfaces.ITask;

public class Task implements ITask {
	private Integer id;
	private String title;
	private String description;
	private String dueDate;
	private StatusTypesEnum status;
	private PriorityEnum priority;
	private CategoryEnum category;
	private List<IComment> comments;
	private List<IHistory> history;
	
	public Task() {
		this.comments = new ArrayList<IComment>();
		this.history = new ArrayList<IHistory>();
	}
	
	public Task(Integer id, String title, String description, String dueDate, StatusTypesEnum status, PriorityEnum priority, CategoryEnum category) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.status = status;
		this.priority = priority;
		this.category = category;
	}
	public List<IComment> getComments() {
		return comments;
	}
	public void setComments(List<IComment> comments) {
		this.comments = comments;
	}
	public List<IHistory> getHistory() {
		return history;
	}
	public void setHistory(List<IHistory> history) {
		this.history = history;
	}
	public CategoryEnum getCategory() {
		return category;
	}
	public void setCategory(CategoryEnum category) {
		this.category = category;
	}
	public PriorityEnum getPriority() {
		return priority;
	}
	public void setPriority(PriorityEnum priority) {
		this.priority = priority;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public StatusTypesEnum getStatus() {
		return status;
	}
	public void setStatus(StatusTypesEnum status) {
		this.status = status;
	}
	public void addComment(IComment comment) {
		this.comments.add(comment);
	}
	public void addHistory(IHistory history) {
		this.history.add(history);
	}
	public void edit(String newTitle, String newDescription, String newDueDate, StatusTypesEnum newStatus, PriorityEnum newPriority, CategoryEnum newCategory) {
		this.setTitle(newTitle);
		this.setDescription(newDescription);
		this.setDueDate(newDueDate);
		this.setStatus(newStatus);
		this.setPriority(newPriority);
		this.setCategory(newCategory);
	}
	@Override
	public String toString() {
		return 
				"========================="
				+ "\nTask details:"
				+ "\nID: " + this.id
				+ "\nTitle: " + this.title
				+ "\nDescription: " + this.description
				+ "\nDue Date: " + this.dueDate
				+ "\nStatus: " + this.status
				+ "\nPriority: " + this.priority
				+ "\nCategory: " + this.category
				+ "\n=========================";
	}
}
