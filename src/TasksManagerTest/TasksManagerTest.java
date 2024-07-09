package TasksManagerTest;

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

interface InterfejsZaZadaca
{
    int getBrojNaPartovi();
    boolean getDaliSodrziPriority();
    boolean getDaliSodrziDeadLine();
}

abstract class Task implements InterfejsZaZadaca
{
    protected String category;
    protected String name;
    protected String description;

    public Task(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    abstract int getPrioritet();
    abstract long getVremRast();

    public static Task kreirajTask(String linija) throws DeadlineNotValidException {
        String[]parts=linija.split(",");
        if(parts.length==3)
        {
            return new TaskSoTri(parts[0],parts[1],parts[2]);
        }
        else if(parts.length==5)
        {
            TaskSoPet t=new TaskSoPet(parts[0],parts[1],parts[2],LocalDateTime.parse(parts[3]),Integer.parseInt(parts[4]));
            //2020-06-01T23:59:59
            if(t.getDeadline().isBefore(LocalDateTime.parse("2020-06-02T23:59:59")))
            {
                throw new DeadlineNotValidException(String.format("The deadline %s has already passed",t.getDeadline()));
            }
            return t;
        }
        else if(parts.length==4)
        {
            if(parts[3].contains("-"))
            {
                TaskSoCetoriIDeadLine t=new TaskSoCetoriIDeadLine(parts[0],parts[1],parts[2],LocalDateTime.parse(parts[3]));
                if(t.getDeadline().isBefore(LocalDateTime.parse("2020-06-02T23:59:59")))
                {
                    throw new DeadlineNotValidException(String.format("The deadline %s has already passed",t.getDeadline()));
                }
                return t;
            }
            else
            {
                return new TaskSoCetoriIPriority(parts[0],parts[1],parts[2],Integer.parseInt(parts[3]));
            }
        }
        else
        {
            return null;
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}

class TaskSoTri extends Task
{

    public TaskSoTri(String category, String name, String description) {
        super(category, name, description);
    }

    @Override
    int getPrioritet() {
        return getPriority();
    }

    @Override
    long getVremRast() {
        return getVremenskoRast();
    }

    @Override
    public int getBrojNaPartovi() {
        return 3;
    }

    @Override
    public boolean getDaliSodrziPriority() {
        return false;
    }

    @Override
    public boolean getDaliSodrziDeadLine() {
        return false;
    }

    public int getPriority()
    {
        return 100;
    }

    public long getVremenskoRast()
    {
        return 10000l;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

class TaskSoCetoriIDeadLine extends Task
{
    private LocalDateTime deadline;
    private LocalDateTime momentalnoVreme;

    public TaskSoCetoriIDeadLine(String category, String name, String description,LocalDateTime deadline) {
        super(category, name, description);
        this.deadline=deadline;
        this.momentalnoVreme=LocalDateTime.now();
    }

    public long getVremenskoRast()
    {
        return Duration.between(getMomentalnoVreme(),getDeadline()).toMillis();
    }
    @Override
    public int getBrojNaPartovi() {
        return 4;
    }

    @Override
    public boolean getDaliSodrziPriority() {
        return false;
    }

    @Override
    public boolean getDaliSodrziDeadLine() {
        return true;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getMomentalnoVreme() {
        return momentalnoVreme;
    }

    public void setMomentalnoVreme(LocalDateTime momentalnoVreme) {
        this.momentalnoVreme = momentalnoVreme;
    }

    public int getPriority()
    {
        return 100;
    }


    @Override
    public String toString() {
        return String.format("Task{name='%s', description='%s', deadline=%s}",getName(),getDescription(),getDeadline());
    }

    @Override
    int getPrioritet() {
        return getPriority();
    }

    @Override
    long getVremRast() {
        return getVremenskoRast();
    }
}

class TaskSoCetoriIPriority extends Task
{
    private int priority;

    public TaskSoCetoriIPriority(String category, String name, String description,int priority) {
        super(category, name, description);
        this.priority=priority;
    }

    @Override
    public int getBrojNaPartovi() {
        return 4;
    }

    @Override
    public boolean getDaliSodrziPriority() {
        return true;
    }

    @Override
    public boolean getDaliSodrziDeadLine() {
        return false;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getVremenskoRast()
    {
        return 10000l;
    }
    @Override
    public String toString() {
        return String.format("Task{name='%s', description='%s', priority=%d}",getName(),getDescription(),getPriority());
    }

    @Override
    int getPrioritet() {
        return getPriority();
    }

    @Override
    long getVremRast() {
        return getVremenskoRast();
    }
}

class TaskSoPet extends Task
{
    private LocalDateTime deadline;
    private LocalDateTime momentalnoVreme;
    private int priority;

    public TaskSoPet(String category, String name, String description,LocalDateTime deadline,int priority) {
        super(category, name, description);
        this.deadline=deadline;
        this.momentalnoVreme=LocalDateTime.now();
        this.priority=priority;
    }

    public long getVremenskoRast()
    {
        return Duration.between(getMomentalnoVreme(),getDeadline()).toMillis();
    }
    @Override
    public int getBrojNaPartovi() {
        return 5;
    }

    @Override
    public boolean getDaliSodrziPriority() {
        return true;
    }

    @Override
    public boolean getDaliSodrziDeadLine() {
        return true;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getMomentalnoVreme() {
        return momentalnoVreme;
    }

    public void setMomentalnoVreme(LocalDateTime momentalnoVreme) {
        this.momentalnoVreme = momentalnoVreme;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format("Task{name='%s', description='%s', deadline=%s, priority=%d}",
                getName(),getDescription(),getDeadline(),getPriority());
    }

    @Override
    int getPrioritet() {
        return getPriority();
    }

    @Override
    long getVremRast() {
        return getVremenskoRast();
    }
}

class KomparatorZaIncludePriority
{
    public static Comparator<Task> napraviKomparator(boolean includePriority)
    {
        if(includePriority)
        {
            return Comparator.comparing(Task::getPrioritet).thenComparing(Task::getVremRast)
                    .thenComparing(Task::getDescription);
        }
        else
        {
            return Comparator.comparing(Task::getVremRast).thenComparing(Task::getDescription);
        }
    }
}
class TaskManager
{
    private List<Task> listaOdTaskovi;
    private Map<String,Set<Task>> mapaOdsetOdTaskoviPoKategorija;

    public TaskManager() {
        this.listaOdTaskovi=new ArrayList<>();
        this.mapaOdsetOdTaskoviPoKategorija=new TreeMap<>();
    }

    public void readTasks(InputStream is) {
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        this.listaOdTaskovi=br.lines().map(l-> {
            try {
                return Task.kreirajTask(l);
            } catch (DeadlineNotValidException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        //System.out.println(listaOdTaskovi);
    }

    void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        //[категорија][име_на_задача],[oпис],[рок_за_задачата],[приоритет]
        //School,NP,lab 1 po NP,2020-06-23T23:59:59.000,1
        //Task{name='NP', description='lab 1 po NP', deadline=2020-06-23T23:59:59, priority=1}
        PrintWriter pw=new PrintWriter(os);

        this.mapaOdsetOdTaskoviPoKategorija=listaOdTaskovi.stream()
                .collect(Collectors.groupingBy(task->task.getCategory(),
                        TreeMap::new,Collectors.toCollection(() -> new TreeSet<Task>(KomparatorZaIncludePriority.napraviKomparator(includePriority)))));

        if(includeCategory)
        {
            StringBuilder sb=new StringBuilder();
            this.mapaOdsetOdTaskoviPoKategorija.keySet().forEach(kluc->{
                sb.append(kluc.toUpperCase()).append("\n");
                mapaOdsetOdTaskoviPoKategorija.get(kluc).forEach(t->sb.append(t).append("\n"));
            });
            sb.setLength(sb.length()-1);
            pw.println(sb.toString());
        }
        else
        {
            this.listaOdTaskovi.stream().sorted(KomparatorZaIncludePriority.napraviKomparator(includePriority)).forEach(task->pw.println(task));
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
