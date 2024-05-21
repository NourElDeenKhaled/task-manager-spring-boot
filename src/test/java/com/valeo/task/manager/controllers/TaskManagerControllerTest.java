package com.valeo.task.manager.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valeo.task.manager.enums.CategoryEnum;
import com.valeo.task.manager.enums.PriorityEnum;
import com.valeo.task.manager.enums.StatusTypesEnum;
import com.valeo.task.manager.interfaces.ITask;
import com.valeo.task.manager.models.Task;
import com.valeo.task.manager.services.AuditTrailService;
import com.valeo.task.manager.services.FileManagerService;
import com.valeo.task.manager.services.TaskManagerService;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.hamcrest.Matchers.hasSize;



@WebMvcTest(controllers = TaskManagerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TaskManagerControllerTest {
	
	@Autowired
	private MockMvc mockMVC;
	
	@MockBean
	private FileManagerService fileManagerService;
	
	@MockBean
	private AuditTrailService auditTrailService;
	
	@MockBean
	private TaskManagerService taskManagerService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	ITask mockTask1;
	ITask mockTask2;
	ITask mockTask3;
	List<ITask> mockTasks;

	@BeforeEach
	public void init() throws IOException {
		mockTasks = new ArrayList<ITask>();
		mockTask1 = Task.builder()
				.id(1)
				.title("Task 1")
				.description("Task 1 Description")
				.dueDate("20-06-2024")
				.status(StatusTypesEnum.OPEN)
				.priority(PriorityEnum.MEDIUM)
				.category(CategoryEnum.PERSONAL)
				.build();
		mockTask2 = Task.builder()
				.id(2)
				.title("Task 2")
				.description("Task 2 Description")
				.dueDate("15-09-2024")
				.status(StatusTypesEnum.IN_PROGRESS)
				.priority(PriorityEnum.HIGH)
				.category(CategoryEnum.SCHOOL)
				.build();
		
		mockTask3 = Task.builder()
				.id(3)
				.title("Go to work")
				.description("Task 3 Description")
				.dueDate("01-10-2024")
				.status(StatusTypesEnum.IN_PROGRESS)
				.priority(PriorityEnum.LOW)
				.category(CategoryEnum.WORK)
				.build();
	}
	
	@Test
	public void TaskManagerController_CreateTask_ReturnCreated() throws Exception {
		given(taskManagerService.addTask(ArgumentMatchers.any()))
			.willAnswer(invocation -> invocation.getArgument(0));
		
		ResultActions response = mockMVC.perform(post("/tasks/addtask")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockTask1)));
		
		response.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(mockTask1.getTitle())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(mockTask1.getId())));
	}
	
	@Test
	public void TaskManagerController_GetTaskById_ReturnTaskById() throws Exception {
		when(taskManagerService.getTaskById(mockTask1.getId())).thenReturn(mockTask1);
				
		ResultActions response = mockMVC.perform(get("/tasks/gettask/{id}", mockTask1.getId())
				.contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(mockTask1.getTitle())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(mockTask1.getId())));
	}
	
	@Test
	public void TaskManagerController_GetAllTasks_ReturnAllTasks() throws Exception {
		mockTasks.add(mockTask2);
		mockTasks.add(mockTask1);
		mockTasks.add(mockTask3);
		
		when(taskManagerService.getAllTasks()).thenReturn(mockTasks);
		
		
		ResultActions response = mockMVC.perform(get("/tasks/getalltasks")
				.contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(mockTasks.size())));
	}
	
	@Test
	public void TaskManagerController_EditTask_ReturnEditedTask() throws Exception {
		when(taskManagerService.getTaskById(mockTask1.getId())).thenReturn(mockTask1);
		
		given(taskManagerService.editTask(ArgumentMatchers.any(), ArgumentMatchers.any()))
		.willAnswer(invocation -> invocation.getArgument(1));
		
		ResultActions response = mockMVC.perform(put("/tasks/edittask/{id}", mockTask1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockTask2)));
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(mockTask2.getTitle())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dueDate", CoreMatchers.is(mockTask2.getDueDate())));
	}
	
	@Test
	public void TaskManagerController_DeleteTaskById_ReturnOkay() throws Exception {
		when(taskManagerService.deleteTask(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(null);
		
		ResultActions response = mockMVC.perform(delete("/tasks/{id}/ID", mockTask1.getId().toString())
				.contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(MockMvcResultMatchers.status().isOk());	
		
	}
	
}
