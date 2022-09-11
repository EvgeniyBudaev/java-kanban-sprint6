import manager.Managers;
import manager.TaskManager;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());

        System.out.println("*** Test History ***");
        System.out.println("--- Create ---");
        taskManager.createTask(new Task("Описание-1", "Task-1", Status.NEW)); // id 1
        taskManager.createTask(new Task("Описание-2", "Task-2", Status.NEW)); // id 2
        taskManager.createEpic(new Epic("Описание-1", "Epic-1", Status.NEW)); // id 3
        taskManager.createEpic(new Epic("Описание-1", "Epic-2", Status.NEW)); // id 4
        taskManager.createSubtask(new Subtask("Описание-1", "Subtask-1", Status.NEW, 3)); // id 5
        taskManager.createSubtask(new Subtask("Описание-2", "Subtask-2", Status.NEW, 3)); // id 6
        taskManager.createSubtask(new Subtask("Описание-3", "Subtask-3", Status.NEW, 3)); // id 7

        System.out.println("--- Get By Id ---");
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getEpicById(3);
        taskManager.getEpicById(3);
        taskManager.getTaskById(1);
        taskManager.getEpicById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);

        System.out.println("--- Get History ---");
        List<Task> history = taskManager.getHistory();
        System.out.println(history);

        System.out.println("--- Remove from history ---");
        taskManager.remove(1);
        taskManager.deleteEpicById(3);

        List<Task> historyAfterRemove = taskManager.getHistory();
        System.out.println(historyAfterRemove);

    }
}
