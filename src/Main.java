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
    }
}
