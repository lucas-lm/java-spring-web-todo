package com.lucasm.taskman.task;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity<?> create(@RequestBody TaskModel task, ServletRequest request) {
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
}
