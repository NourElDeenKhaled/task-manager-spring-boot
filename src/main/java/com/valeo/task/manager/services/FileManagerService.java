package com.valeo.task.manager.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valeo.task.manager.interfaces.ITask;
import com.valeo.task.manager.models.Task;

@Service
public class FileManagerService {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final File tasksFile = new File("tasks.json");
	private final File auditTrailFile = new File("audit-trail.json");

	public FileManagerService() {}

	public void saveTasks(List<ITask> tasks) throws IOException {
		objectMapper.writeValue(tasksFile, tasks);
	}

	public List<ITask> loadTasks() throws IOException {
		if (!tasksFile.exists()) {
			return new ArrayList<ITask>();
		}

		return objectMapper.readValue(tasksFile, new TypeReference<List<ITask>>() {});
	}
	
	public void saveAuditTrail(List<String> audits) throws IOException {
		objectMapper.writeValue(auditTrailFile, audits);
	}

	public List<String> loadAuditTrail() throws IOException {
		if (!auditTrailFile.exists()) {
			return new ArrayList<String>();
		}

		return objectMapper.readValue(auditTrailFile, new TypeReference<List<String>>() {});
	}

}
