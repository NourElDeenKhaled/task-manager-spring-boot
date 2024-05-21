package com.valeo.task.manager.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valeo.task.manager.enums.CategoryEnum;
import com.valeo.task.manager.enums.DeleteTypes;
import com.valeo.task.manager.enums.PriorityEnum;
import com.valeo.task.manager.enums.SearchTypesEnum;
import com.valeo.task.manager.enums.SortingTypesEnum;
import com.valeo.task.manager.enums.StatusTypesEnum;
import com.valeo.task.manager.interfaces.IComment;
import com.valeo.task.manager.interfaces.ITask;
import com.valeo.task.manager.models.Comment;
import com.valeo.task.manager.models.Task;

@ExtendWith(MockitoExtension.class)
public class TaskManagerServiceTests {

	@Mock
	FileManagerService fileManagerService;

	@Mock
	AuditTrailService auditTrailService;

	@InjectMocks
	TaskManagerService taskManagerService;

	ITask lowPriorityTask;
	ITask mediumPriorityTask;
	ITask highPriorityTask;
	List<ITask> mockTasks;

	@BeforeEach
	public void init() throws IOException {
		mockTasks = new ArrayList<ITask>();
		mediumPriorityTask = Task.builder()
				.id(1)
				.title("Task 1")
				.description("Task 1 Description")
				.dueDate("20-06-2024")
				.status(StatusTypesEnum.OPEN)
				.priority(PriorityEnum.MEDIUM)
				.category(CategoryEnum.PERSONAL)
				.build();
		highPriorityTask = Task.builder()
				.id(2)
				.title("Task 2")
				.description("Task 2 Description")
				.dueDate("15-09-2024")
				.status(StatusTypesEnum.IN_PROGRESS)
				.priority(PriorityEnum.HIGH)
				.category(CategoryEnum.SCHOOL)
				.build();
		
		lowPriorityTask = Task.builder()
				.id(3)
				.title("Go to work")
				.description("Task 3 Description")
				.dueDate("01-10-2024")
				.status(StatusTypesEnum.IN_PROGRESS)
				.priority(PriorityEnum.LOW)
				.category(CategoryEnum.WORK)
				.build();
		taskManagerService.init();
	}
	
	@Test
	public void TaskManagerService_GetTaskById_ReturnsTaskById() throws IOException {
		mockTasks.add(highPriorityTask);
		taskManagerService.addTask(lowPriorityTask);
		taskManagerService.addTask(mediumPriorityTask);
		taskManagerService.addTask(highPriorityTask);
		ITask task = taskManagerService.getTaskById(3);

		assertEquals(mockTasks.get(0), task);

	}

	@Test
	public void TaskManagerService_GetAllTasks_ReturnsAllTasks() throws IOException {
		mockTasks.add(mediumPriorityTask);
		mockTasks.add(highPriorityTask);
		taskManagerService.addTask(mediumPriorityTask);
		taskManagerService.addTask(highPriorityTask);
		List<ITask> tasks = taskManagerService.getAllTasks();

		assertEquals(tasks, mockTasks);

	}

	@Test
	public void TaskManagerService_AddTask_ReturnsAddedTask() throws IOException {
		ITask addedTask = taskManagerService.addTask(mediumPriorityTask);
		String addedHistory = "Creation: Task is created.";

		assertEquals(mediumPriorityTask, addedTask);
		assertEquals(1, addedTask.getId());
		assertEquals(mediumPriorityTask.getTitle(), addedTask.getTitle());
		assertEquals(addedHistory, addedTask.getHistory().get(0).getText());
	}

	@Test
	public void TaskManagerService_AddComment_ReturnsCommentAdded() throws IOException {
		ITask addedTask = taskManagerService.addTask(mediumPriorityTask);
		IComment addedComment = new Comment("Comment 1.");
		taskManagerService.addComment(addedTask.getId(), addedComment);

		assertEquals(addedComment, addedTask.getComments().get(0));
	}

	@Test
	public void TaskManagerService_DeleteTaskById_ReturnsTasksIsEmpty() throws IOException {
		taskManagerService.addTask(mediumPriorityTask);

		assertEquals(1, taskManagerService.getAllTasks().size());

		taskManagerService.deleteTask("1", DeleteTypes.ID);

		Assertions.assertThat(taskManagerService.getAllTasks().isEmpty());
		
		taskManagerService.addTask(mediumPriorityTask);

		assertEquals(1, taskManagerService.getAllTasks().size());

		taskManagerService.deleteTask(mediumPriorityTask.getTitle(), DeleteTypes.Title);

		Assertions.assertThat(taskManagerService.getAllTasks().isEmpty());
	}
	
	@Test
	public void TaskManagerService_EditTask_ReturnsEditedTask() throws IOException {
		ITask addedTask = taskManagerService.addTask(mediumPriorityTask);
		ITask editedTask = taskManagerService.editTask(addedTask.getId(), highPriorityTask);
		String addedHistory = "Edit: Task is edited.";

		assertEquals(1, editedTask.getId());
		assertEquals(highPriorityTask.getTitle(), editedTask.getTitle());
		assertEquals(addedHistory, editedTask.getHistory().get(1).getText());
	}
	
	@Test
	public void TaskManagerService_SearchTasks_ReturnsSearchedTasks() throws IOException {
		String searchText = "task";
		Set<SearchTypesEnum> searchTypes = Set.of(SearchTypesEnum.TITLE, SearchTypesEnum.STATUS);

		mockTasks.add(mediumPriorityTask);
		mockTasks.add(highPriorityTask);
		taskManagerService.addTask(highPriorityTask);
		taskManagerService.addTask(mediumPriorityTask);
		taskManagerService.addTask(lowPriorityTask);
		
		Set<ITask> searchedTasks = taskManagerService.searchTask(searchText, searchTypes);
		Set<ITask> expectedTasks = new HashSet<ITask>(mockTasks);

		assertEquals(expectedTasks, searchedTasks);
	}
	
	@Test
	public void TaskManagerService_FilterTasksByCategory_ReturnsFilteredTasks() throws IOException {
		Set<CategoryEnum> filters = Set.of(CategoryEnum.PERSONAL, CategoryEnum.SCHOOL);

		mockTasks.add(mediumPriorityTask);
		mockTasks.add(highPriorityTask);
		taskManagerService.addTask(highPriorityTask);
		taskManagerService.addTask(mediumPriorityTask);
		taskManagerService.addTask(lowPriorityTask);
		
		Set<ITask> filteredTasks = taskManagerService.filterByCategory(filters);
		Set<ITask> expectedTasks = new HashSet<ITask>(mockTasks);

		assertEquals(expectedTasks, filteredTasks);
	}
	
	@Test
	public void TaskManagerService_SortTasksByPriority_ReturnsSortedTasks() throws IOException {

		mockTasks.add(highPriorityTask);
		mockTasks.add(mediumPriorityTask);
		mockTasks.add(lowPriorityTask);
		
		taskManagerService.addTask(mediumPriorityTask);
		taskManagerService.addTask(highPriorityTask);
		taskManagerService.addTask(lowPriorityTask);
		
		List<ITask> sortedTasks = taskManagerService.displayTasksSortedByPriority(SortingTypesEnum.HIGH_LOW);

		assertEquals(mockTasks, sortedTasks);
		
		mockTasks.clear();
		mockTasks.add(lowPriorityTask);
		mockTasks.add(mediumPriorityTask);
		mockTasks.add(highPriorityTask);
		
		sortedTasks = taskManagerService.displayTasksSortedByPriority(SortingTypesEnum.LOW_HIGH);
		
		assertEquals(mockTasks, sortedTasks);
	}
	
	@Test
	public void TaskManagerService_FilterTasksBetweenDates_ReturnsTasksBetweenDates() throws IOException {
		mockTasks.add(mediumPriorityTask);
		
		taskManagerService.addTask(highPriorityTask);
		taskManagerService.addTask(mediumPriorityTask);
		taskManagerService.addTask(lowPriorityTask);
		
		String startDate = "10-06-2024";
		String endDate = "20-08-2024";
		List<ITask> filteredTasks = taskManagerService.filterBetweenDates(startDate, endDate);

		assertEquals(mockTasks, filteredTasks);
	}

}
