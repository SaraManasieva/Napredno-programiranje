package TasksManagerTest_zad21;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class DeadlineNotValidException extends Exception
{
    public DeadlineNotValidException(String message) {
        super(message);
    }
}
abstract class Task
{
    //[категорија][име_на_задача],[oпис],[рок_за_задачата],[приоритет]
    protected String category;
    protected String name;
    protected String description;

    public Task(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }
    abstract int getPrioritet();
    abstract long getVremenskoRAstojanie();

    public  static Task napraviTask(String linija) throws DeadlineNotValidException {
        String[]parts=linija.split(",");
        if(parts.length==3)
        {
            return new TaskSoTri(parts[0], parts[1], parts[2]);
        }
        else if(parts.length==4)
        {
            if(parts[3].contains("-"))//deadLine
            {
                TaskSoCetiriIDeadLine t=new TaskSoCetiriIDeadLine(parts[0],parts[1],parts[2],LocalDateTime.parse(parts[3]));
                if(t.getDeadLine().isBefore(LocalDateTime.parse("2020-06-02T23:59:59")))
                {
                    throw new DeadlineNotValidException(String.format("The deadline %s has already passed",
                            LocalDateTime.parse(parts[3]).toString()));
                }
                return t;
            }
            else//so prioritet
            {
                return new TaskSoCetiriIPrioritet(parts[0],parts[1],parts[2],Integer.parseInt(parts[3]));
            }
        }
        else if(parts.length==5)
        {
            TaskSoPet t=new TaskSoPet(parts[0],parts[1],parts[2],LocalDateTime.parse(parts[3]),Integer.parseInt(parts[4]));
            if(t.getDeadLine().isBefore(LocalDateTime.parse("2020-06-02T23:59:59")))
            {
                throw new DeadlineNotValidException(String.format("The deadline %s has already passed",
                        LocalDateTime.parse(parts[3]).toString()));
            }
            return t;
        }
        else
        {
            return null;
        }
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
class TaskSoTri extends Task
{

    public TaskSoTri(String category, String name, String description) {
        super(category, name, description);
    }

    @Override
    int getPrioritet() {
        return Integer.MAX_VALUE;
    }

    @Override
    long getVremenskoRAstojanie() {
        return Long.MAX_VALUE;
    }

    @Override
    public String toString() {
        return String.format("Task{name='%s', description='%s'}",
                name,description);
    }
}
class TaskSoCetiriIDeadLine extends Task
{
    private LocalDateTime deadLine;
    private LocalDateTime momentalnoVreme;

    public TaskSoCetiriIDeadLine(String category, String name, String description, LocalDateTime deadLine) {
        super(category, name, description);
        this.deadLine = deadLine;
        this.momentalnoVreme=LocalDateTime.now();
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    @Override
    int getPrioritet() {
        return Integer.MAX_VALUE;
    }

    @Override
    long getVremenskoRAstojanie() {
        return (Duration.between(momentalnoVreme,deadLine).getSeconds());
    }

    @Override
    public String toString() {
        return String.format("Task{name='%s', description='%s', deadline=%s}",
                name,description,deadLine);
    }
}
class TaskSoCetiriIPrioritet extends Task
{
    private int priority;

    public TaskSoCetiriIPrioritet(String category, String name, String description, int priority) {
        super(category, name, description);
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    int getPrioritet() {
        return priority;
    }

    @Override
    long getVremenskoRAstojanie() {
        return Long.MAX_VALUE;
    }

    @Override
    public String toString() {
        return String.format("Task{name='%s', description='%s', priority=%d}",
                name,description,priority);
    }
}
class TaskSoPet extends Task
{
    private LocalDateTime deadLine;
    private int priority;
    LocalDateTime momentalnoVreme;

    public TaskSoPet(String category, String name, String description, LocalDateTime deadLine, int priority) {
        super(category, name, description);
        this.deadLine = deadLine;
        this.priority = priority;
        this.momentalnoVreme=LocalDateTime.now();
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    int getPrioritet() {
        return priority;
    }

    @Override
    long getVremenskoRAstojanie() {
        return (Duration.between(momentalnoVreme,deadLine).toSeconds());
    }

    @Override
    public String toString() {
        return String.format("Task{name='%s', description='%s', deadline=%s, priority=%d}",
                name,description,deadLine,priority);
    }
}
class TaskManager
{
    private List<Task> listaOdTaskovi;

    public TaskManager() {
        this.listaOdTaskovi=new ArrayList<>();
    }

    void readTasks (InputStream inputStream)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.listaOdTaskovi=br.lines().map(linija-> {
                    try {
                        return Task.napraviTask(linija);
                    } catch (DeadlineNotValidException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    void printTasks(OutputStream os, boolean includePriority, boolean includeCategory)
    {
        PrintWriter pw=new PrintWriter(os);
        Map<String,List<Task>>mapa=listaOdTaskovi.stream()
                        .collect(Collectors.groupingBy(task->task.category,
                                TreeMap::new,
                                Collectors.toCollection(ArrayList::new)));
        Comparator<Task>comparator;
        if(includeCategory)
        {
            if(includePriority)
            {
                comparator=Comparator.comparing(Task::getPrioritet)
                        .thenComparing(Task::getVremenskoRAstojanie)
                        .thenComparing(Task::getDescription);
                StringBuilder sb=new StringBuilder();
                mapa.entrySet().stream()
                        .forEach(entry->{
                            sb.append(entry.getKey().toUpperCase()).append("\n");
                            entry.getValue().stream().sorted(comparator)
                                    .forEach(task->sb.append(task).append("\n"));
                        });
                sb.setLength(sb.length()-1);
                pw.println(sb.toString());
            }
            else
            {
                comparator=Comparator.comparing(Task::getVremenskoRAstojanie)
                        .thenComparing(Task::getDescription);
                StringBuilder sb=new StringBuilder();
                mapa.entrySet().stream()
                        .forEach(entry->{
                            sb.append(entry.getKey().toUpperCase()).append("\n");
                            entry.getValue().stream().sorted(comparator)
                                    .forEach(task->sb.append(task).append("\n"));
                        });
                sb.setLength(sb.length()-1);
                pw.println(sb.toString());
            }
        }
        else
        {
            if(includePriority)
            {
                comparator=Comparator.comparing(Task::getPrioritet)
                        .thenComparing(Task::getVremenskoRAstojanie)
                        .thenComparing(Task::getDescription);
                this.listaOdTaskovi.stream()
                        .sorted(comparator)
                        .forEach(task->pw.println(task));
            }
            else
            {
                comparator=Comparator.comparing(Task::getVremenskoRAstojanie)
                        .thenComparing(Task::getDescription);
                this.listaOdTaskovi
                        .stream()
                        .sorted(comparator)
                        .forEach(task->pw.println(task));
            }
        }
        pw.flush();
    }
}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
