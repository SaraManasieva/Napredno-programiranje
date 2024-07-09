package LabExercisesTest;

import java.util.*;
import java.util.stream.Collectors;
//9:28 //9:44
class Student
{

    private String indeks;
    private List<Integer> listaOdPoeniPoLab;

    public Student(String indeks, List<Integer> listaOdPoeniPoLab) {
        this.indeks = indeks;
        this.listaOdPoeniPoLab = listaOdPoeniPoLab;
    }

    public double getSumarniPoeni()
    {
        return this.listaOdPoeniPoLab.stream().mapToInt(p->p).sum()/10.0;
    }

    public int getGodinaNaStudiranje()
    {
        return 20-Integer.parseInt(getIndeks().substring(0,2));
    }
    public String getPotpis()
    {
        if(listaOdPoeniPoLab.size()>=8)
        {
            return "YES";
        }
        else
        {
            return "NO";
        }
    }

    public String getIndeks() {
        return indeks;
    }

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public List<Integer> getListaOdPoeniPoLab() {
        return listaOdPoeniPoLab;
    }

    public void setListaOdPoeniPoLab(List<Integer> listaOdPoeniPoLab) {
        this.listaOdPoeniPoLab = listaOdPoeniPoLab;
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f",getIndeks(),getPotpis(),getSumarniPoeni());
    }
}
class LabExercises
{
    private List<Student> listaOdStudenti;

    public LabExercises() {
        this.listaOdStudenti = new ArrayList<>();
    }

    public void addStudent (Student student)
    {
        this.listaOdStudenti.add(student);
    }

    public void printByAveragePoints (boolean ascending, int n)
    {
        if(ascending)
        {
            this.listaOdStudenti.stream().sorted(Comparator.comparing(Student::getSumarniPoeni).thenComparing(Student::getIndeks))
                    .limit(n).forEach(student -> System.out.println(student));
        }
        else
        {
            this.listaOdStudenti.stream().sorted(Comparator.comparing(Student::getSumarniPoeni).thenComparing(Student::getIndeks).reversed())
                    .limit(n).forEach(student -> System.out.println(student));
        }
    }

    public List<Student> failedStudents ()
    {
        return this.listaOdStudenti.stream().filter(student -> student.getPotpis().equals("NO"))
                .sorted(Comparator.comparing(Student::getIndeks).thenComparing(Student::getSumarniPoeni))
                .collect(Collectors.toList());
    }

    public Map<Integer,Double> getStatisticsByYear()
    {
        return this.listaOdStudenti.stream().filter(student -> student.getPotpis().equals("YES")).collect(Collectors.groupingBy(student -> student.getGodinaNaStudiranje(),
              TreeMap::new,  Collectors.averagingDouble(student->student.getSumarniPoeni())));
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}