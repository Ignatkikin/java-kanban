package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        /*


        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Задача1", "Описание1");
        manager.createTasks(task1);
        Epic epic1 = new Epic("Epic 1", "Починить авто");
        manager.createEpics(epic1);
        int idEpic1 = epic1.getId();
        Subtask subtask1 = new Subtask("SubTask 1", "Поменять масло", Status.NEW, idEpic1);
        manager.createSubTasks(subtask1);
        Task task2 = new Task("обновленная задача", "Описание2",
                LocalDateTime.of(2020, 5, 5, 5, 0), Duration.ofHours(1));
        task2.setId(1);
        Subtask subtask2 = new Subtask("SubTask 1", "Поменять масло", Status.NEW, idEpic1);
        subtask2.setId(3);
        manager.updateTasks(task2);
        manager.updateSubTasks(subtask2);

        List<Task> listik = manager.getPrioritizedTasks();

        for (Task list : listik) {
            System.out.println(list);
        }







            Task task1 = new Task("Задача 1", "Выучить теорию 6 спринта");
            Task task2 = new Task("Задача 2", "Сдать тз6");
            manager.createTasks(task1);
            manager.createTasks(task2);

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
            Subtask subtask2_3 = new Subtask("SubTask 2_3", "Попросить друга следить за котом",
                    Status.NEW, idEpic2);
            manager.createSubTasks(subtask2_1);
            manager.createSubTasks(subtask2_2);
            manager.createSubTasks(subtask2_3);


        Task updateTask = new Task("Обновленная задача", "Обновленное описание",
                LocalDateTime.of(2023,10, 10, 8, 0), Duration.ofHours(1));
        updateTask.setId(1);
        Epic updateEpic = new Epic("Обновленый Эпик", "Подзадача Эпика");
        updateEpic.setId(3);
        Subtask updateSubtask = new Subtask("Обновленная подзадача", "Подзадача эпика", Status.DONE,
                updateEpic.getId());
        updateSubtask.setId(4);



        manager.updateTasks(updateTask);
        manager.updateEpics(updateEpic);
        System.out.println(updateSubtask);
        List<Task> list = manager.getPrioritizedTasks();
        for (Task listic : list) {
            System.out.println(listic);
        }
        manager.updateSubTasks(updateSubtask);




        System.out.println("Проверяем Task, Epic, SubTask:");
        System.out.println("Создаем Task");
        Task task1 = new Task("Задача 1", "Описание 1",
                LocalDateTime.of(2024,5, 5, 5,0), Duration.ofHours(1));
        manager.createTasks(task1);
        System.out.println("Получение спика всех задач:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("печатаем приоритеты:");

        List<Task> taskList = manager.getPrioritizedTasks();

        for (Task list : taskList) {
            System.out.println(list);
        }

        System.out.println("Обновление объекта:");

        Task newTask1 = new Task("Новая задача 1", "Купить молоко");
        newTask1.setId(1);
        manager.updateTasks(newTask1);

        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("печатаем приоритеты:");

        List<Task> taskList1 = manager.getPrioritizedTasks();

        for (Task list : taskList1) {
            System.out.println(list);
        }


        System.out.println("Обновление объекта22:");

        Task task22 = new Task("Задача 1", "Описание 1",
                LocalDateTime.of(2023,5, 5, 5,0), Duration.ofHours(3));
        task22.setId(1);
        manager.updateTasks(task22);

        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("печатаем приоритеты:");

        List<Task> taskList22 = manager.getPrioritizedTasks();

        for (Task list : taskList22) {
            System.out.println(list);
        }

        Epic epic1 = new Epic("Epic1", "Починить машину");
        manager.createEpics(epic1);
        int idEpic = epic1.getId();
        Subtask subtask1 = new Subtask("Subtask1", "поменять масло", Status.NEW,
                LocalDateTime.of(2022,7, 7, 7,0), Duration.ofHours(3), idEpic);
        manager.createSubTasks(subtask1);

        List<Task> taskList23 = manager.getPrioritizedTasks();

        System.out.println("печатаем приоритеты, после добавления сабтаски:");

        for (Task list : taskList23) {
            System.out.println(list);
        }

        System.out.println(epic1.getStartTime() + "\n" + epic1.getDuration() + "\n" + epic1.getEndTime());

        Subtask subtask2 = new Subtask("Subtask2", "починить подвеску", Status.DONE,
                LocalDateTime.of(2021, 7, 4, 7,40), Duration.ofHours(12), idEpic);
        manager.createSubTasks(subtask2);

        System.out.println(epic1.getStartTime() + "\n" + epic1.getDuration() + "\n" + epic1.getEndTime());

        Subtask subtask3 = new Subtask("Subtask3", "Починить стекло", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 5, 5, 4,0), Duration.ofHours(1), idEpic);
        manager.createSubTasks(subtask3);

        System.out.println(epic1.getStartTime() + "\n" + epic1.getDuration() + "\n" + epic1.getEndTime());

        List<Task> taskList24 = manager.getPrioritizedTasks();

        System.out.println("печатаем приоритеты, после добавления сабтаски:");

        for (Task list : taskList24) {
            System.out.println(list);
        }

        manager.deleteSubTasksById(subtask3.getId());

        List<Task> taskList25 = manager.getPrioritizedTasks();

        System.out.println("печатаем приоритеты, после удаления сабтаски:");

        for (Task list : taskList25) {
            System.out.println(list);
        }

        System.out.println(epic1.getStartTime() + "\n" + epic1.getDuration() + "\n" + epic1.getEndTime());

        Epic epic2 = new Epic("Epic2", "сгонять на море");

        manager.createEpics(epic2);
        int idepic2 = epic2.getId();

        Subtask subtask10 = new Subtask("Subtask11", "Купить билеты", Status.NEW,
                LocalDateTime.of(2026, 6,6,6,6), Duration.ofHours(10), idepic2);
        manager.createSubTasks(subtask10);

        System.out.println("Смотрим на второй эпик с сабтаской");
        System.out.println(epic2.getStartTime() + "\n" + epic2.getDuration() + "\n" + epic2.getEndTime());

        Subtask subtask20 = new Subtask("Subtask20", "Вылетаем", Status.NEW,
                LocalDateTime.of(2026, 6,8,6,6), Duration.ofHours(10), idepic2);
        manager.createSubTasks(subtask20);

        System.out.println("Смотрим на второй эпик с сабтаской еще раз");

        System.out.println(epic2.getStartTime() + "\n" + epic2.getDuration() + "\n" + epic2.getEndTime());

        List<Task> taskList26 = manager.getPrioritizedTasks();

        System.out.println("печатаем приоритеты");

        for (Task list : taskList26) {
            System.out.println(list);
        }

        Subtask subtask99 = new Subtask("обновленная подзадача", "починить крышу", Status.NEW,
                LocalDateTime.of(2022, 07, 07, 0, 0), Duration.ofHours(7), idEpic);

        subtask99.setId(4);

        manager.updateSubTasks(subtask99);

        List<Task> taskList27 = manager.getPrioritizedTasks();

        System.out.println("печатаем приоритеты");

        for (Task list : taskList27) {
            System.out.println(list);
        }

        System.out.println(epic1.getStartTime() + "\n" + epic1.getDuration() + "\n" + epic1.getEndTime());

        System.out.println(epic2.getStartTime() + "\n" + epic2.getDuration() + "\n" + epic2.getEndTime());


         */
























/*
        System.out.println("Поехали!");
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
