package tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    void subTaskEqualsSubTaskById() {
        Epic epic1 = new Epic("Эпик 1", "Починить авто");
        epic1.setId(5);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.DONE, epic1.getId());
        subtask1.setId(7);
        subtask2.setId(7);
        Assertions.assertEquals(subtask1, subtask2);
    }
}