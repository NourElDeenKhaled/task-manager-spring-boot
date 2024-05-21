package com.valeo.task.manager.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valeo.task.manager.enums.CategoryEnum;
import com.valeo.task.manager.enums.PriorityEnum;
import com.valeo.task.manager.enums.StatusTypesEnum;
import com.valeo.task.manager.interfaces.ITask;
import com.valeo.task.manager.models.Task;

@ExtendWith(MockitoExtension.class)
public class FileManagerServiceTest {

	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private File tasksFile;
	
	@Mock
	private File auditTrailFile;

	@InjectMocks
	private FileManagerService fileManagerService;

    private List<ITask> mockTasks;
    private List<String> audits;

	@BeforeEach
	public void init() throws IOException {
		fileManagerService = new FileManagerService(objectMapper, tasksFile, auditTrailFile);
		audits = new ArrayList<>(Arrays.asList("User created task", "User updated task"));
        mockTasks = new ArrayList<>(Arrays.asList(
        		Task.builder()
				.id(1)
				.title("Task 1")
				.description("Task 1 Description")
				.dueDate("20-06-2024")
				.status(StatusTypesEnum.OPEN)
				.priority(PriorityEnum.MEDIUM)
				.category(CategoryEnum.PERSONAL)
				.build()
				,
        		Task.builder()
				.id(2)
				.title("Task 2")
				.description("Task 2 Description")
				.dueDate("15-09-2024")
				.status(StatusTypesEnum.IN_PROGRESS)
				.priority(PriorityEnum.HIGH)
				.category(CategoryEnum.SCHOOL)
				.build()));
	}
	
	@Test
	public void FileManagerService_SaveTasks_VerifySaveIsCalled() throws IOException {
		
		fileManagerService.saveTasks(mockTasks);
		
		verify(objectMapper, times(1)).writeValue(tasksFile, mockTasks);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void FileManagerService_LoadTasks_ReturnLoadedTasks() throws IOException {
		
		when(tasksFile.exists()).thenReturn(true);
        when(objectMapper.readValue(eq(tasksFile), any(TypeReference.class))).thenReturn(mockTasks);
        List<ITask> actualTasks = fileManagerService.loadTasks();
        
        assertEquals(mockTasks, actualTasks);
	}
	
	@Test
	public void FileManagerService_SaveAuditTrail_VerifySaveIsCalled() throws IOException {
		
		fileManagerService.saveAuditTrail(audits);
		
		verify(objectMapper, times(1)).writeValue(auditTrailFile, audits);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void FileManagerService_LoadAuditTrail_ReturnLoadedTasks() throws IOException {
		
		when(auditTrailFile.exists()).thenReturn(true);
        when(objectMapper.readValue(eq(auditTrailFile), any(TypeReference.class))).thenReturn(audits);
        List<String> actualAudits = fileManagerService.loadAuditTrail();
        
        assertEquals(audits, actualAudits);
	}

}
