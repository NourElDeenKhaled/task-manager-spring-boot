package com.valeo.task.manager.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.valeo.task.manager.enums.CategoryEnum;
import com.valeo.task.manager.enums.DeleteTypes;
import com.valeo.task.manager.enums.SearchTypesEnum;
import com.valeo.task.manager.enums.SortingTypesEnum;
import com.valeo.task.manager.interfaces.ITask;
import com.valeo.task.manager.models.Comment;
import com.valeo.task.manager.models.Task;
import com.valeo.task.manager.services.AuditTrailService;
import com.valeo.task.manager.services.TaskManagerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/tasks")
@Validated
public class TaskManagerController {
	
	private final TaskManagerService taskManager;
	private final AuditTrailService auditTrailService;
	
	@Autowired
	public TaskManagerController(TaskManagerService taskManager, AuditTrailService auditTrailService) {
		this.taskManager = taskManager;
		this.auditTrailService = auditTrailService;
	}
	
	@GetMapping("/getalltasks")
	public ResponseEntity<List<ITask>> getAllTasks() throws IOException {
		return ResponseEntity.ok(taskManager.getAllTasks());
	}
	
	@GetMapping("/gettask/{id}")
    public ResponseEntity<ITask> getTaskById(@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(taskManager.getTaskById(id));
    }
	
	@GetMapping("/admin/getallactions")
	public ResponseEntity<List<String>> getAllActions() {
		return ResponseEntity.ok(auditTrailService.getAllActions());
	}
	
	@GetMapping("/searchtasks")
	public ResponseEntity<Set<ITask>> searchTasks(@RequestParam String text, @RequestParam Set<SearchTypesEnum> searchTypes) throws IOException {
		return ResponseEntity.ok(taskManager.searchTask(text, searchTypes));
	}
	
	@GetMapping("/filterbetweendates")
	public ResponseEntity<List<ITask>> filterBetweenDates(
			@Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Due date must be in the format dd-MM-yyyy") @Valid @RequestParam String startDate, 
			@Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Due date must be in the format dd-MM-yyyy") @Valid @RequestParam String endDate) 
	{
		return ResponseEntity.ok(taskManager.filterBetweenDates(startDate, endDate));
	}
	
	@GetMapping("/filterbycategory")
	public ResponseEntity<Set<ITask>> filterByCategory(@RequestParam Set<CategoryEnum> categoryTypes) throws IOException {
		return ResponseEntity.ok(taskManager.filterByCategory(categoryTypes));
	}
	
	@GetMapping("/sortbypriority")
	public ResponseEntity<List<ITask>> displayTasksSortedByPriority(@RequestParam SortingTypesEnum sortingType) throws IOException {
		return ResponseEntity.ok(taskManager.displayTasksSortedByPriority(sortingType));
	}
	
	@PostMapping("/addtask")
	public ResponseEntity<ITask> addTask(@Valid @RequestBody Task task) throws IOException {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskManager.addTask(task));
	}
	
	@PutMapping("/edittask/{id}")
    public ResponseEntity<ITask> editTask(@PathVariable Integer id, @Valid @RequestBody Task task) throws IOException {
        return ResponseEntity.ok(taskManager.editTask(id, task));
    }
	
	@PostMapping("/addcomment/{id}")
	public ResponseEntity<String> addComment(@PathVariable Integer id, @RequestBody Comment comment) {
		return ResponseEntity.ok(taskManager.addComment(id, comment));
	}
	
	@DeleteMapping("/{text}/{deleteType}")
    public ResponseEntity<String> deleteTask(@PathVariable String text, @PathVariable DeleteTypes deleteType) {
        return ResponseEntity.ok(taskManager.deleteTask(text, deleteType));
    }
	
	
}
