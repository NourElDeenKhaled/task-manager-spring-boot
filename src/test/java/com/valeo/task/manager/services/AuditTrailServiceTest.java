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
public class AuditTrailServiceTest {

	private AuditTrailService auditTrailService;

    private List<ITask> mockTasks;

	@BeforeEach
	public void init() throws IOException {
		auditTrailService = new AuditTrailService();
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
	public void AuditTrailService_AddActionWithTask_ReturnsAddedAction() throws IOException {
		
		String actionText = "Task Added";

        auditTrailService.addAction(mockTasks.get(0), actionText);

        List<String> actions = auditTrailService.getAllActions();
        assertEquals(1, actions.size());
        assertEquals("Action for Task 'Task 1' ID: 1: " + actionText, actions.get(0));
    
	}
	
	@Test
	public void AuditTrailService_AddActionWithoutTask_ReturnsAddedAction() throws IOException {
		
		String actionText = "User retreived data";

        auditTrailService.addAction(actionText);

        List<String> actions = auditTrailService.getAllActions();
        assertEquals(1, actions.size());
        assertEquals("Action: " + actionText, actions.get(0));
    
	}
	
	@Test
	public void AuditTrailService_AddMultipleActions_ReturnsAddedActions() throws IOException {
		
		String action1Text = "User retreived data";
		String action2Text = "Task Added";

        auditTrailService.addAction(action1Text);
        auditTrailService.addAction(mockTasks.get(0), action2Text);

        List<String> actions = auditTrailService.getAllActions();
        assertEquals(2, actions.size());
        assertEquals("Action: " + action1Text, actions.get(0));
        assertEquals("Action for Task 'Task 1' ID: 1: " + action2Text, actions.get(1));
    
	}
}
