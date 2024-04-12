package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
      /*  System.out.println("Поехали!");
        InMemoryTaskManager manager = new InMemoryTaskManager();

        System.out.println("Проверяем Task, Epic, SubTask:");

        System.out.println("Создаем Task");
        Task task1 = new Task("Задача 1", "Выучить теорию 4 спринта");
        Task task2 = new Task("Задача 2", "Сдать тз4");
        manager.createTasks(task1);
        manager.createTasks(task2);

        System.out.println("Получение спика всех задач:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("Получение по идентификатору:");
        System.out.println(manager.getTasksById(1));
        System.out.println(manager.getTasksById(2));

        System.out.println("Обновление объекта:");
        Task newTask1 = new Task("Новая задача 1", "Купить молоко");
        newTask1.setId(1);
        Task newTask2 = new Task("Новая задача 2", "Купить хлеб");
        newTask2.setId(2);
        manager.updateTasks(newTask1);
        manager.updateTasks(newTask2);

        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("Удаление/Удаление по идентификатору:");
        manager.deleteTasksById(2);
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        manager.deleteAllTasks();
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("Создаем Epic и SubTask");
        Epic epic1 = new Epic("Epic 1", "Починить авто");
        manager.createEpics(epic1);
        int idEpic1 = epic1.getId();
        Subtask subtask1 = new Subtask("SubTask 1", "Поменять масло", Status.NEW, idEpic1);
        Subtask subtask2 = new Subtask("SubTask 2", "Починить дверь", Status.NEW, idEpic1);
        Subtask subtask3 = new Subtask("SubTask 3", "Поменять лампочки", Status.NEW, idEpic1);
        manager.createSubTasks(subtask1);
        manager.createSubTasks(subtask2);
        manager.createSubTasks(subtask3);

        Epic epic2 = new Epic("Epic 2", "Съездить на море");
        manager.createEpics(epic2);
        int idEpic2 = epic2.getId();
        Subtask subtask2_1 = new Subtask("SubTask 2_1", "Купить билеты", Status.NEW, idEpic2);
        Subtask subtask2_2 = new Subtask("SubTask 2_2", "Собрать чемодан", Status.NEW, idEpic2);
        Subtask subtask2_3 = new Subtask("SubTask 2_3", "Попросить друга следить за котом", Status.NEW, idEpic2);
        manager.createSubTasks(subtask2_1);
        manager.createSubTasks(subtask2_2);
        manager.createSubTasks(subtask2_3);

        System.out.println("Проверяем Epic и SubTask:");
        System.out.println("Печать всех Epic");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("Печать всех SubTask");
        for (Subtask subtask : manager.getAllSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("Печать всех сабтаск Epic 1");
        for (Subtask subtask : manager.getSubTaskFromEpic(3)) {
            System.out.println(subtask);
        }
        System.out.println("Печать всех сабтаск Epic 2");
        for (Subtask subtask : manager.getSubTaskFromEpic(7)) {
            System.out.println(subtask);
        }
        System.out.println("Получаем Epic и SubTask по id");
        System.out.println(manager.getEpicsById(7));
        System.out.println(manager.getSubTasksById(10));

        System.out.println("Обновляем Epic и SubTask");
        Subtask newSubtask1 = new Subtask("newSubtask 1", "Масло поменял", Status.DONE, idEpic1);
        Subtask newSubtask2 = new Subtask("newSubtask 2", "Дверь починил", Status.DONE, idEpic1);
        Subtask newSubtask3 = new Subtask("newSubtask 3", "Лампочки поменял", Status.DONE, idEpic1);
        newSubtask1.setId(4);
        newSubtask2.setId(5);
        newSubtask3.setId(6);
        manager.updateSubTasks(newSubtask1);
        manager.updateSubTasks(newSubtask2);
        manager.updateSubTasks(newSubtask3);
        for (Subtask subtask : manager.getSubTaskFromEpic(3)) {
            System.out.println(subtask);
        }
        System.out.println(manager.getEpicsById(3));
        Subtask subtask4 = new Subtask("Subtask 4","Помыть авто", Status.NEW, idEpic1);
        manager.createSubTasks(subtask4);
        System.out.println("Добавили новую зачаду");
        for (Subtask subtask : manager.getSubTaskFromEpic(3)) {
            System.out.println(subtask);
        }
        System.out.println(manager.getEpicsById(3));
        System.out.println("Обновили Epic 1");
        Epic newEpic1 = new Epic("newEpic 1", "Доделать дела с авто");
        newEpic1.setId(3);
        manager.updateEpics(newEpic1);
        System.out.println(manager.getEpicsById(3));

        System.out.println("Удаляем SubTask по id");
        manager.deleteSubTasksById(11);
        System.out.println(manager.getEpicsById(3));

        System.out.println("Удаляем Epic по id");
        manager.deleteEpicsById(7);
        System.out.println("Печать всех SubTask");
        for (Subtask subtask : manager.getAllSubTasks()) {
            System.out.println(subtask);
        }
        System.out.println("Удаляем все SubTask");
        manager.deleteAllSubTasks();
        for (Subtask subtask : manager.getAllSubTasks()) {
            System.out.println(subtask);
        }
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }
        System.out.println("Удаляем все Эпики");
        manager.deleteAllEpics();

*/
    }
}