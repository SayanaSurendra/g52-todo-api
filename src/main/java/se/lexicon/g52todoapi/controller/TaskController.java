package se.lexicon.g52todoapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.g52todoapi.domain.dto.TaskDTOForm;
import se.lexicon.g52todoapi.domain.dto.TaskDTOView;
import se.lexicon.g52todoapi.service.TaskService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    //Todo: Implement Controller
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping
    public ResponseEntity<TaskDTOView> doCreateTask(@RequestBody TaskDTOForm taskDTOForm) {
       TaskDTOView taskDTOView=taskService.create(taskDTOForm);
       return ResponseEntity.ok(taskDTOView);

   }


   @GetMapping("/{id}")
    public ResponseEntity<TaskDTOView> doFindById(@PathVariable Long id) {
        TaskDTOView taskDTOView=taskService.findById(id);
        return ResponseEntity.ok(taskDTOView);
   }

   @PutMapping
   public ResponseEntity<Void> doUpdate(@RequestBody TaskDTOForm taskDTOForm) {
        taskService.update(taskDTOForm);
        return ResponseEntity.noContent().build();
   }

   @DeleteMapping
   public ResponseEntity<Void> doDelete(@RequestParam Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
   }

   public

@GetMapping("/person/{personId}")
 ResponseEntity<List<TaskDTOView>> doFindTasksByPersonId(@PathVariable Long personId) {
    List<TaskDTOView> taskDTOViews=taskService.findTasksByPersonId(personId);
    return ResponseEntity.ok(taskDTOViews);
    }

    @GetMapping("/dates")
    ResponseEntity<List<TaskDTOView>> doFindTasksBetweenDates(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<TaskDTOView> taskDTOViews= taskService.findTasksBetweenStartAndEndDate(startDate, endDate);
        return ResponseEntity.ok(taskDTOViews);
    }


    @GetMapping
    ResponseEntity<List<TaskDTOView>>  doFindAllUnassignedTasks() {
        List<TaskDTOView> taskDTOViews= taskService.findAllUnassignedTodoItems();
        return ResponseEntity.ok(taskDTOViews);

    }

    @GetMapping("/tasksOverdue")
    ResponseEntity<List<TaskDTOView>>  dofindAllUnfinishedAndOverdue() {
        List<TaskDTOView> taskDTOViews= taskService.findAllUnfinishedAndOverdue();
        return ResponseEntity.ok(taskDTOViews);
    }
}
