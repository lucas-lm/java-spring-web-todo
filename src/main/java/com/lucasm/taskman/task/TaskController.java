package com.lucasm.taskman.task;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucasm.taskman.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity<?> create(@RequestBody TaskModel task, HttpServletRequest request) {
    var userId = request.getAttribute("userId");
    task.setUserId((UUID) userId);

    var isTimeOk = false;
    if (task.getStartAt() == null | task.getEndAt() == null) {
      isTimeOk = true;
    } else {
      isTimeOk = task.getStartAt().isBefore(task.getEndAt());
    }

    if (!isTimeOk) {
      return ResponseEntity.badRequest().body("Start timestamp should not be after End timestamp");
    }
    
    var createdTask = this.taskRepository.save(task);
    return ResponseEntity.ok().body(createdTask);
  }

  @GetMapping("/")
  public ResponseEntity<?> list(HttpServletRequest request) {
    var userId = request.getAttribute("userId");
    var tasks = this.taskRepository.findByUserId((UUID) userId);
    return ResponseEntity.ok().body(tasks);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@RequestBody TaskModel newTaskProps, HttpServletRequest request, @PathVariable UUID id) {
    var oldTask = this.taskRepository.findById(id).orElse(null);

    if (oldTask == null) {
      return ResponseEntity.badRequest().body("Task to update does not found.");
    }

    var userId = request.getAttribute("userId");

    if (!oldTask.getUserId().equals(userId)) {
      return ResponseEntity.badRequest().body("Operation not allowed for this user.");
    }

    Utils.copyNonNullProperties(newTaskProps, oldTask); // update oldTask with newTaskProps
    var newTask = this.taskRepository.save(oldTask); // oldTask here is already up-to-date
    return ResponseEntity.ok().body(newTask);
  }
}
