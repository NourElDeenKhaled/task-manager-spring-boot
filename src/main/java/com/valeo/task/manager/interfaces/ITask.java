package com.valeo.task.manager.interfaces;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.valeo.task.manager.enums.CategoryEnum;
import com.valeo.task.manager.enums.PriorityEnum;
import com.valeo.task.manager.enums.StatusTypesEnum;
import com.valeo.task.manager.models.Task;

import lombok.Data;

@JsonDeserialize(as = Task.class)
public interface ITask {
	Integer getId();
	void setId(Integer id);
	String getTitle();
	void setTitle(String title);
	String getDescription();
	void setDescription(String description);
	String getDueDate();
	void setDueDate(String dueDate);
	List<IComment> getComments();
	void setComments(List<IComment> history);
	List<IHistory> getHistory();
	void setHistory(List<IHistory> history);
	StatusTypesEnum getStatus();
	void setStatus(StatusTypesEnum status);
	PriorityEnum getPriority();
	void setPriority(PriorityEnum priority);
	CategoryEnum getCategory();
	void setCategory(CategoryEnum category);
	void addComment(IComment comment);
	void addHistory(IHistory history);
	void edit(String newTitle, String newDescription, String newDueDate, StatusTypesEnum newStatus, PriorityEnum newPriority, CategoryEnum newCategory);
}
