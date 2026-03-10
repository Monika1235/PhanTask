package com.phantask.task.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import com.phantask.task.dto.AdminTaskDTO;
import com.phantask.task.dto.EmployeeTaskDTO;
import com.phantask.task.dto.TaskResponse;
import com.phantask.task.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskService;

	// ----------------- ADMIN endpoints -----------------
	// Admin or HR can create tasks
	@PostMapping("/admin/create")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<TaskResponse> createTask(@RequestBody AdminTaskDTO dto, Authentication auth) {
		try {
            auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !auth.isAuthenticated()) {
              throw new InsufficientAuthenticationException("Authentication required");
            }
            
        	boolean isAdmin = auth.getAuthorities()
        	        .stream()
        	        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        	if (!isAdmin) {
        	    throw new AccessDeniedException("Forbidden");
        	}
			String admin = auth.getName();
		    TaskResponse resp = taskService.createTask(dto, admin);
		    return ResponseEntity.ok(resp);
		}catch (AccessDeniedException ex) {
            throw ex;
        }catch (AuthenticationException ae) {
            throw ae;
        }		
	}

	@PutMapping("/admin/update/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody AdminTaskDTO dto) {
		TaskResponse resp = taskService.updateTask(id, dto);
		return ResponseEntity.ok(resp);
	}

	@DeleteMapping("/admin/delete/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> deleteTask(@PathVariable Long id) {
		boolean deleted = taskService.deleteTask(id);

		if (deleted) {
			return ResponseEntity.ok("Task deleted successfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
		}
	}

	@GetMapping("/admin/all")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<TaskResponse>> adminAll() {
		return ResponseEntity.ok(taskService.getAllTasksAdmin());
	}

	// ----------------- EMPLOYEE endpoints -----------------
	// Helper to extract roles (without ROLE_ prefix)
	private List<String> getRolesFromAuth(Authentication auth) {
		if (auth == null)
			return Collections.emptyList();
		return auth.getAuthorities().stream().map(a -> a.getAuthority().replace("ROLE_", ""))
				.collect(Collectors.toList());
	}

	// Get all tasks visible to logged-in user
	@GetMapping("/my")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<TaskResponse>> myTasks(Authentication auth) {
		String username = auth.getName();
		List<String> roles = getRolesFromAuth(auth);
		return ResponseEntity.ok(taskService.getAllTasksForUser(username, roles));
	}

	// Get pending tasks visible to logged-in user
	@GetMapping("/my/pending")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<TaskResponse>> myPending(Authentication auth) {
		String username = auth.getName();
		List<String> roles = getRolesFromAuth(auth);
		return ResponseEntity.ok(taskService.getPendingTasksForUser(username, roles));
	}

	// Get submitted tasks visible to logged-in user
	@GetMapping("/my/submitted")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<TaskResponse>> mySubmitted(Authentication auth) {
		try {
            auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
              throw new InsufficientAuthenticationException("Authentication required");
            } 
			String username = auth.getName();
		    List<String> roles = getRolesFromAuth(auth);
		    return ResponseEntity.ok(taskService.getSubmittedTasksForUser(username, roles));
		}catch (AuthenticationException ae) {
            throw ae;
        }		
	}

	// Submit task by logged in user (provide driveUrl)
	@PutMapping("/my/submit/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<TaskResponse> submitTask(@PathVariable Long id, @RequestBody EmployeeTaskDTO dto,
			Authentication auth) {
		
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
		String username = auth.getName();
		TaskResponse resp = taskService.submitTask(id, dto, username);
		return ResponseEntity.ok(resp);
	}
}
