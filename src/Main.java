import manager.FileBackedTasksManager;
import manager.Managers;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        // Спринт 6
        Path path = Path.of("data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager manager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);

        Task firstTask = new Task("Разработать лифт до луны", "Космолифт", Status.NEW);
        manager.createTask(firstTask);
        Task secondTask = new Task("Познакомиться", "Tinder", Status.NEW);
        manager.createTask(secondTask);

        Epic firstEpic = new Epic("Посадить дерево", "Дерево", Status.NEW);
        manager.createEpic(firstEpic);

        Subtask firstSubtask = new Subtask("Купить семена", "Семена", Status.NEW, firstEpic.getId());
        manager.createSubtask(firstSubtask);

        manager.getTaskById(firstTask.getId());
        manager.getTaskById(secondTask.getId());
        System.out.println();

//        System.out.println("--- Считывание из файла ---");
//        Path path2 = Path.of("data.csv");
//        File file2 = new File(String.valueOf(path));
//        manager.loadFromFile(new File(file2);
//        System.out.println("Задачи");
//        System.out.println(manager.getAllTasks());
//        System.out.println("Эпики");
//        System.out.println(manager.getAllEpics());
//        System.out.println("Подзадачи");
//        System.out.println(manager.getAllSubtasks());
//        System.out.println("История");
//        System.out.println(manager.getHistory());

        // Спринт 5
//        TaskManager taskManager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());
//
//        System.out.println("*** Test History ***");
//        System.out.println("--- Create ---");
//        taskManager.createTask(new Task("Описание-1", "Task-1", Status.NEW)); // id 1
//        taskManager.createTask(new Task("Описание-2", "Task-2", Status.NEW)); // id 2
//        taskManager.createEpic(new Epic("Описание-1", "Epic-1", Status.NEW)); // id 3
//        taskManager.createEpic(new Epic("Описание-1", "Epic-2", Status.NEW)); // id 4
//        taskManager.createSubtask(new Subtask("Описание-1", "Subtask-1", Status.NEW, 3)); // id 5
//        taskManager.createSubtask(new Subtask("Описание-2", "Subtask-2", Status.NEW, 3)); // id 6
//        taskManager.createSubtask(new Subtask("Описание-3", "Subtask-3", Status.NEW, 3)); // id 7
//
//        System.out.println("--- Get By Id ---");
//        taskManager.getTaskById(1);
//        taskManager.getEpicById(3);
//        taskManager.getEpicById(3);
//        taskManager.getEpicById(3);
//        taskManager.getTaskById(1);
//        taskManager.getEpicById(4);
//        taskManager.getSubtaskById(5);
//        taskManager.getSubtaskById(5);
//        taskManager.getSubtaskById(6);
//
//        System.out.println("--- Get History ---");
//        List<Task> history = taskManager.getHistory();
//        System.out.println(history);
//
//        System.out.println("--- Remove from history ---");
//        taskManager.remove(1);
//        taskManager.deleteEpicById(3);
//
//        List<Task> historyAfterRemove = taskManager.getHistory();
//        System.out.println(historyAfterRemove);
    }
}
