package manager;

import TaskType.TaskType;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final HistoryManager historyManager;

    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
        this.historyManager = historyManager;
    }

    public void loadFromFile(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            String line = bufferedReader.readLine();
            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();
                if (line.equals("")) {
                    break;
                }

                Task task = fromString(line);

                if (task instanceof Epic epic) {
                    addEpic(epic);
                } else if (task instanceof Subtask subtask) {
                    addSubtask(subtask);
                } else {
                    addTask(task);
                }
            }

            String lineWithHistory = bufferedReader.readLine();
            for (int id : historyFromString(lineWithHistory)) {
                addToHistory(id);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
    }

    public void save() {
        Path fileToSaveData = Path.of("data.csv");

        try {
            if (Files.exists(fileToSaveData)) {
                Files.delete(fileToSaveData);
            }
            Files.createFile(fileToSaveData);
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для записи данных");
        }

        try (FileWriter writer = new FileWriter(String.valueOf(fileToSaveData), StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getAllTasks()) {
                writer.write(toString(task) + "\n");
            }

            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic) + "\n");
                List<Subtask> subtasksByEpic = getAllSubtasksByEpicId(epic.getId());
                for (Subtask subtask : subtasksByEpic) {
                    writer.write(toString(subtask) + "\n");
                }
            }

            writer.write("\n");
            writer.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }
    }

    private String getParentEpicId(Task task) {
        if (task instanceof Subtask) {
            return Integer.toString(((Subtask) task).getEpicId());
        }
        return "";
    }

    private TaskType getType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        }
        return TaskType.TASK;
    }

    // Метод сохранения задачи в строку
    private String toString(Task task) {
        String[] toJoin = {Integer.toString(task.getId()), getType(task).toString(), task.getName(),
                task.getStatus().toString(), task.getDescription(), getParentEpicId(task)};
        return String.join(",", toJoin);
    }

    // Метод создания задачи из строки
    private Task fromString(String value) {
        String[] params = value.split(",");
        if (params[1].equals("EPIC")) {
            Epic epic = new Epic(params[4], params[2], Status.valueOf(params[3].toUpperCase()));
            epic.setId(Integer.parseInt(params[0]));
            epic.setStatus(Status.valueOf(params[3].toUpperCase()));
            return epic;
        } else if (params[1].equals("SUBTASK")) {
            Subtask subtask = new Subtask(params[4], params[2], Status.valueOf(params[3].toUpperCase()),
                    Integer.parseInt(params[5]));
            subtask.setId(Integer.parseInt(params[0]));
            return subtask;
        } else {
            Task task = new Task(params[4], params[2], Status.valueOf(params[3].toUpperCase()));
            task.setId(Integer.parseInt(params[0]));
            return task;
        }
    }

    @Override
    public int createTask(Task task) {
        int newTaskId = super.createTask(task);
        save();
        return newTaskId;
    }

    @Override
    public int createEpic(Epic epic) {
        int newEpicId = super.createEpic(epic);
        save();
        return newEpicId;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int newSubtaskId = super.createSubtask(subtask);
        save();
        return newSubtaskId;
    }

    public int addTask(Task task) {
        return super.createTask(task);
    }

    public int addEpic(Epic epic) {
        return super.createEpic(epic);
    }

    public int addSubtask(Subtask subtask) {
        return super.createSubtask(subtask);
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateStatusEpic(Epic epic) {
        super.updateStatusEpic(epic);
        save();
    }

    // Метод для сохранения истории в CSV
    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder str = new StringBuilder();

        for (Task task : history) {
            str.append(task.getId()).append(",");
        }

        if (str.length() != 0) {
            str.deleteCharAt(str.length() - 1);
        }

        return str.toString();
    }

    // Метод восстановления менеджера истории из CSV
    static List<Integer> historyFromString(String value) {
        List<Integer> toReturn = new ArrayList<>();
        if (value != null) {
            String[] id = value.split(",");

            for (String number : id) {
                toReturn.add(Integer.parseInt(number));
            }

            return toReturn;
        }
        return toReturn;
    }

    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager(Managers.getDefaultHistory());

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
//        Path path = Path.of("data.csv");
//        manager.loadFromFile(new File(String.valueOf(path)));
//        System.out.println("Задачи");
//        System.out.println(manager.getAllTasks());
//        System.out.println("Эпики");
//        System.out.println(manager.getAllEpics());
//        System.out.println("Подзадачи");
//        System.out.println(manager.getAllSubtasks());
//        System.out.println("История");
//        System.out.println(manager.getHistory());
    }
}
