package com.valeo.task.manager.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valeo.task.manager.interfaces.ITask;

@Service
public class FileManagerService {
	private ObjectMapper objectMapper = new ObjectMapper();
	private File tasksFile = new File("tasks.json");
	private File auditTrailFile = new File("audit-trail.json");
	
	public FileManagerService() {}

	public FileManagerService(ObjectMapper objectMapper, File tasksFile, File auditTrailFile) {
		this.objectMapper = objectMapper;
		this.tasksFile = tasksFile;
		this.auditTrailFile = auditTrailFile;
	}

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
