
package com.example.time_management.task;

import com.example.time_management.category.Category;
import com.example.time_management.category.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(
            TaskRepository taskRepository,
            CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    // Them task
    public Task createTask(Task task) {
        task.setId(null);
        task.setCategory(findCategory(task.getCategory()));

        return taskRepository.save(task);
    }

    // Lay tat ca task
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Lay task theo ID
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Khong tim thay task co ID: " + id
                        ));
    }

    // Cap nhat task
    public Task updateTask(Long id, Task newTask) {
        Task task = getTaskById(id);

        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());
        task.setDate(newTask.getDate());
        task.setTime(newTask.getTime());
        task.setCategory(findCategory(newTask.getCategory()));

        return taskRepository.save(task);
    }

    // Xoa task
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    // Tim category theo ID
    private Category findCategory(Category inputCategory) {
        if (inputCategory == null) {
            return null;
        }

        Long categoryId = inputCategory.getId();

        if (categoryId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Category phai co ID hop le"
            );
        }

        return categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Khong tim thay category co ID: "
                                        + categoryId
                        ));
    }
}
