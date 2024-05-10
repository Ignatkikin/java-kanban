package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskEqualsTaskById() {
        Task task1 = new Task("Задача 1", "Выучить теорию 5 спринта");
        Task task2 = new Task("Задача 2", "Сдать тз5");
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2);
    }

}