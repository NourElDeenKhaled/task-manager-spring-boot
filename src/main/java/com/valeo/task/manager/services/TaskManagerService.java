package com.valeo.task.manager.services;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.valeo.task.manager.enums.CategoryEnum;
import com.valeo.task.manager.enums.DeleteTypes;
import com.valeo.task.manager.enums.SearchTypesEnum;
import com.valeo.task.manager.enums.SortingTypesEnum;
import com.valeo.task.manager.exceptions.EmptyValueException;
import com.valeo.task.manager.exceptions.TaskNotFoundException;
import com.valeo.task.manager.interfaces.IComment;
import com.valeo.task.manager.interfaces.ITask;
import com.valeo.task.manager.models.History;

import jakarta.annotation.PostConstruct;

@Service
public class TaskManagerService {
	private Integer lastTaskId = 0;
	private List<ITask> tasks = new ArrayList<ITask>();
	
	private final AuditTrailService auditTrailService;
	private final FileManagerService fileManagerService;
	
	@Autowired
	public TaskManagerService(AuditTrailService auditTrailService, FileManagerService fileManagerService) {
		this.auditTrailService = auditTrailService;
		this.fileManagerService = fileManagerService;
	}
	
	@PostConstruct
	public void init() throws IOException {
		this.tasks = fileManagerService.loadTasks();
		auditTrailService.setAllActions(fileManagerService.loadAuditTrail());
		this.setLastTaskId(this.tasks.size() + 1);
	}
	
	public Integer getLastTaskId() {
		return lastTaskId;
	}
	
	public void setLastTaskId(Integer num) {
		this.lastTaskId = num;
	}
	
	public List<ITask> getAllTasks() throws IOException {
		auditTrailService.addAction("User retrieved all tasks data.");
		fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
		return tasks;
	}
	
	public ITask getTaskById(Integer id) throws IOException {
		try {
		ITask index = this.tasks.stream()
				.filter(t -> t.getId().equals(id))
				.findFirst()
				.get();
				auditTrailService.addAction(index, "User retrieved data of task (ID:" + index.getId() +").");
				fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
				return index;
		} catch (Exception e) {
			throw new TaskNotFoundException("Task with ID: '"+ id + "' is not found.");
		}
	}
	
	public ITask addTask(ITask task) throws IOException {
		task.setId(lastTaskId);
		task.addHistory(new History("Creation: Task is created."));
		this.tasks.add(task);
		auditTrailService.addAction(task, "User added task.");
		fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
		lastTaskId ++;
		fileManagerService.saveTasks(tasks);
		return task;
	}
	
	public String addComment(Integer taskID, IComment comment) {
		if(Objects.isNull(comment.getText()) || comment.getText().isBlank()) throw new EmptyValueException("Comment text can not be empty.");
		try {
			ITask task =
			this.tasks.stream()
				.filter(t -> t.getId().equals((taskID)))
				.findFirst()
				.get();
			task.addComment(comment);
			task.addHistory(new History("Comment: " + comment.getText() + " is added."));
			auditTrailService.addAction(task, "User added comment: '" + comment.getText() + "'.");
			fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
			fileManagerService.saveTasks(tasks);
			return ("Comment added");
		} catch (Exception e) {
			return ("Comment not added");
		}
	}
	
	public String deleteTask(String text, DeleteTypes deleteType) {
		try {
			ITask index;
			switch (deleteType) {
			case Title:
				index = this.tasks.stream()
				.filter(t -> t.getTitle().trim().equalsIgnoreCase(text))
				.findFirst()
				.get();
				auditTrailService.addAction(index, "User deleted task.");
				fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
				tasks.remove(index);
				fileManagerService.saveTasks(tasks);
				return ("Task is deleted successfully.");
			case ID:
				index = this.tasks.stream()
				.filter(t -> t.getId().equals(Integer.parseInt(text)))
				.findFirst()
				.get();
				auditTrailService.addAction(index, "User deleted task.");
				fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
				tasks.remove(index);
				fileManagerService.saveTasks(tasks);
				return ("Task is deleted successfully.");
			default:
				return ("Invalid delete type.");
			}
		} catch (Exception e) {
			throw new TaskNotFoundException("Task with " + deleteType.getText() + " '"+ text + "' is not found.");
//			return ("Invalid task.");
		}
	}
	
	public ITask editTask(Integer id, ITask newTask) throws IOException {
		ITask task = this.tasks.stream()
			.filter(t -> Objects.equals(t.getId(), id))
			.findFirst().orElseThrow(() -> new TaskNotFoundException("Task with ID: '" + id + "' is not found."));
		task.edit(newTask.getTitle(), newTask.getDescription(), newTask.getDueDate(), newTask.getStatus(), newTask.getPriority(), newTask.getCategory());
		task.addHistory(new History("Edit: Task is edited."));
		auditTrailService.addAction(task, "User edited task.");
		fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
		fileManagerService.saveTasks(tasks);
		return task;
	}
	
	public Set<ITask> searchTask(String text, Set<SearchTypesEnum> searchTypes) throws IOException {
		Set<ITask> results = new HashSet<ITask>();
		for (SearchTypesEnum st : searchTypes) {
			this.tasks.stream()
			.filter(SearchTypesEnum.getSearchTypePredicate(st, text))
			.forEach(e -> results.add(e));
		}
		auditTrailService.addAction("User searched for task with Text: '"+ text + "' and search types: " + searchTypes + ".");
		fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
		return results;
	}
	
	public Set<ITask> filterByCategory(Set<CategoryEnum> filters) throws IOException {
		Set<ITask> results = new HashSet<ITask>();
		for (CategoryEnum st : filters) {
			this.tasks.stream()
			.filter(CategoryEnum.getCategoryFilterPredicate(st))
			.forEach(e -> results.add(e));
		}
		auditTrailService.addAction("User filtered tasks with categories: "+ filters + ".");
		fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
		return results;
	}
	
	public List<ITask> displayTasksSortedByPriority(SortingTypesEnum sortingType) throws IOException {
		auditTrailService.addAction("User displayed tasks sorted by priority: " + sortingType.getText() + ".");
		fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
		switch (sortingType) {
		case LOW_HIGH:
			return this.tasks.stream()
			.sorted((a, b) -> a.getPriority().getId().compareTo(b.getPriority().getId()))
			.toList();
		case HIGH_LOW:
			return this.tasks.stream()
			.sorted((a, b) -> b.getPriority().getId().compareTo(a.getPriority().getId()))
			.toList();
		default:
			return new ArrayList<ITask>();
		}
	}
	
	public List<ITask> filterBetweenDates(String startDate, String endDate) {
		Date start = null;
		Date end = null;
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setLenient(false);
		try {
			start = sdf.parse(startDate);
			end = sdf.parse(endDate);
		}
		catch (ParseException e) {
			System.out.println("Parsing error");
		}
		if(start.after(end)) {
			Date temp = start;
			start = end;
			end = temp;
		}
		final Date finalStart = start;
		final Date finalEnd = end;
		return this.tasks.stream()
		.filter(
				e -> {
					try {
						Date dueDate = sdf.parse(e.getDueDate());
						auditTrailService.addAction("User filtered tasks between dates: " + startDate + " & " + endDate + ".");
						fileManagerService.saveAuditTrail(auditTrailService.getAllActions());
						return dueDate.after(finalStart) && dueDate.before(finalEnd);
					} catch (ParseException | IOException e1) {
						return false;
					}
				})
		.toList();
	}
}
