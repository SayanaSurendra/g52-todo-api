package se.lexicon.g52todoapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.g52todoapi.domain.dto.PersonDTOForm;
import se.lexicon.g52todoapi.domain.dto.PersonDTOView;
import se.lexicon.g52todoapi.domain.dto.TaskDTOForm;
import se.lexicon.g52todoapi.domain.dto.TaskDTOView;
import se.lexicon.g52todoapi.domain.entity.Person;
import se.lexicon.g52todoapi.domain.entity.Task;
import se.lexicon.g52todoapi.domain.entity.User;
import se.lexicon.g52todoapi.exception.DataNotFoundException;
import se.lexicon.g52todoapi.repository.PersonRepository;
import se.lexicon.g52todoapi.repository.TaskRepository;
import se.lexicon.g52todoapi.repository.UserRepository;
import se.lexicon.g52todoapi.service.TaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(PersonRepository personRepository, TaskRepository taskRepository, UserRepository userRepository, UserRepository userRepository1) {
        this.personRepository = personRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository1;
    }

    @Override
    public TaskDTOView create(TaskDTOForm taskDTOForm) {
        if (taskDTOForm.person() != null) {
            return addTaskToPerson(taskDTOForm.person().id(), taskDTOForm);
        } else {
            // Create a new Task entity using the DTO
            Task task = Task.builder()
                    .title(taskDTOForm.title())
                    .description(taskDTOForm.description())
                    .deadline(taskDTOForm.deadline())
                    .done(false)
                    .build();


            // Save the created entity to the database
            Task savedTask = taskRepository.save(task);


            // Convert the saved entity to a DTO

         /* PersonDTOView builtPersonView = PersonDTOView.builder()
                  .id(savedTask.getPerson().getId())
                  .name(savedTask.getPerson().getName())
                  .build();*/


            return TaskDTOView.builder()
                    .id(savedTask.getId())
                    .title(savedTask.getTitle())
                    .description(savedTask.getDescription())
                    .deadline(savedTask.getDeadline())
                    .done(savedTask.isDone())
                    //  .person(builtPersonView)
                    .build();

        }
    }

    @Override
    public TaskDTOView findById(Long taskId) {
        // Retrieve a Task entity by its ID
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new DataNotFoundException("Task not found"));

        // Convert the entity to a DTO
        return convertToTaskDTOView(task);
    }

    @Override
    public void update(TaskDTOForm taskDTOForm) {
        // Check if the Task exists
        Task existingTask = taskRepository.findById(taskDTOForm.id())
                .orElseThrow(() -> new DataNotFoundException("Task not found"));

        // Update the existing entity using the DTO
        existingTask.setTitle(taskDTOForm.title());
        existingTask.setDescription(taskDTOForm.description());
        existingTask.setDeadline(taskDTOForm.deadline());
        existingTask.setDone(taskDTOForm.done());

        if (taskDTOForm.person() != null && taskDTOForm.person().id() != null) {
            Person person = personRepository.findById(taskDTOForm.person().id())
                    .orElseThrow(() -> new DataNotFoundException("Person not found with id: " + taskDTOForm.person().id()));
            existingTask.setPerson(person);  // Assign the person to the task
        }else{
            existingTask.setPerson(null);
        }


        // Save the updated entity to the database
        taskRepository.save(existingTask);
    }

    @Override
    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("Task ID must not be null");
        if (!taskRepository.existsById(id)) throw new DataNotFoundException("Task not found with id: " + id);
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDTOView> findTasksByPersonId(Long personId) {
        // Retrieve tasks associated with a person by their ID
        List<Task> taskList = taskRepository.findByPerson_Id(personId);
        return taskList.stream()
                .map(this::convertToTaskDTOView)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTOView> findTasksBetweenStartAndEndDate(LocalDate start, LocalDate end) {
// Retrieve tasks with deadlines between the given dates
        List<Task> taskList = taskRepository.findByDeadlineBetween(start, end);
        return taskList.stream()
                .map(this::convertToTaskDTOView)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTOView> findAllUnassignedTodoItems() {
// Retrieve all unassigned tasks
        List<Task> taskList = taskRepository.findByPersonIsNull();
        return taskList.stream()
                .map(this::convertToTaskDTOView)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTOView> findAllUnfinishedAndOverdue() {
        // Retrieve all unfinished and overdue tasks
        List<Task> taskList = taskRepository.selectUnFinishedAndOverdueTasks();
        return taskList.stream()
                .map(this::convertToTaskDTOView)
                .collect(Collectors.toList());
    }


    @Override
    public TaskDTOView addTaskToPerson(Long personId, TaskDTOForm taskDTOForm) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new DataNotFoundException("Person not found with id: " + personId));

        Task task = Task.builder()
                .title(taskDTOForm.title())
                .description(taskDTOForm.description())
                .deadline(taskDTOForm.deadline())
                .done(taskDTOForm.done())
                .person(person)
                .build();

        Task savedTask = taskRepository.save(task);

        PersonDTOView personDTOView = PersonDTOView.builder().id(person.getId()).name(person.getName()).build();

        return new TaskDTOView(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getDeadline(),
                savedTask.isDone(),
                personDTOView
        );
    }

    private TaskDTOView convertToTaskDTOView(Task task) {


        /*return TaskDTOView.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .done(task.isDone())
                .build();*/


        PersonDTOView personDTOView=null;
        if (task.getPerson() != null) {
            Person person = task.getPerson();
             personDTOView = PersonDTOView.builder()
                    .id(person.getId())
                    .name(person.getName())
                    .build();


        }

        return TaskDTOView.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .done(task.isDone()).person(personDTOView)
                .build();


    }

}
