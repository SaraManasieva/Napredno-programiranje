package CourseTest;

//package mk.ukim.finki.midterm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface InterfejsZaPopolnuvanjeVoStudent
{
     void popolni(Student student,int poeni) throws Exception;
}
class Student
{
    private String indeks;
    private String imeNaStudent;
    private int poeniPrvKol;
    private int poeniVtorKol;
    private int poeniLab;

    public Student(String indeks, String imeNaStudent, int poeniPrvKol, int poeniVtorKol, int poeniLab) {
        this.indeks = indeks;
        this.imeNaStudent = imeNaStudent;
        this.poeniPrvKol = poeniPrvKol;
        this.poeniVtorKol = poeniVtorKol;
        this.poeniLab = poeniLab;
    }

    public Student(String indeks, String imeNaStudent) {
        this.indeks=indeks;
        this.imeNaStudent=imeNaStudent;
    }

    public String getIndeks() {
        return indeks;
    }

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public String getImeNaStudent() {
        return imeNaStudent;
    }

    public void setImeNaStudent(String imeNaStudent) {
        this.imeNaStudent = imeNaStudent;
    }

    public int getPoeniPrvKol() {
        return poeniPrvKol;
    }

    public void setPoeniPrvKol(int poeniPrvKol) {
        this.poeniPrvKol = poeniPrvKol;
    }

    public int getPoeniVtorKol() {
        return poeniVtorKol;
    }

    public void setPoeniVtorKol(int poeniVtorKol) {
        this.poeniVtorKol = poeniVtorKol;
    }

    public int getPoeniLab() {
        return poeniLab;
    }

    public void setPoeniLab(int poeniLab) {
        this.poeniLab = poeniLab;
    }
    public double getSumarniPoeni()
    {
        //midterm1 * 0.45 + midterm2 * 0.45 + labs.
        return getPoeniPrvKol()*0.45+getPoeniVtorKol()*0.45+getPoeniLab();
    }
    public int getOcena()
    {
        int ocena=(int) getSumarniPoeni()/10+1;
        if(ocena>10)
        {
            ocena=10;
        }
        if(ocena<5)
        {
            ocena=5;
        }
        return ocena;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",
                getIndeks(),getImeNaStudent(),getPoeniPrvKol(),getPoeniVtorKol(),getPoeniLab(),getSumarniPoeni(),getOcena());
    }
}

class AdvancedProgrammingCourse
{
    private Map<String,Student> mapaOdStudneti;
    private Map<String,InterfejsZaPopolnuvanjeVoStudent> mapaZaObjektiNaInterfejs;


    public AdvancedProgrammingCourse(Map<String, Student> mapaOdStudneti, Map<String, InterfejsZaPopolnuvanjeVoStudent> mapaZaObjektiNaInterfejs) {
        this.mapaOdStudneti = mapaOdStudneti;
        this.mapaZaObjektiNaInterfejs = mapaZaObjektiNaInterfejs;
    }


    public AdvancedProgrammingCourse() {
        this.mapaOdStudneti=new HashMap<>();
        this.mapaZaObjektiNaInterfejs=new HashMap<>();
        this.mapaZaObjektiNaInterfejs.put("midterm1",(student,poeni)->{
            if(poeni>100)
            {
                throw new Exception();
            }
            student.setPoeniPrvKol(poeni);
        });
        this.mapaZaObjektiNaInterfejs.put("midterm2",(student,poeni)->{
            if(poeni>100)
            {
                throw new Exception();
            }
            student.setPoeniVtorKol(poeni);
        });
        this.mapaZaObjektiNaInterfejs.put("labs",(student,poeni)->{
            if(poeni>100)
            {
                throw new Exception();
            }
            student.setPoeniLab(poeni);
        });

    }

    public void addStudent (Student s)
    {
        this.mapaOdStudneti.put(s.getIndeks(),s);
    }

    public void updateStudent (String idNumber, String activity, int points)
    {
        try {
            this.mapaZaObjektiNaInterfejs.get(activity).popolni(mapaOdStudneti.get(idNumber),points);
        } catch (Exception e) {

        }
    }

    public List<Student> getFirstNStudents (int n)
    {
        return this.mapaOdStudneti.values().stream().sorted(Comparator.comparing(Student::getSumarniPoeni).reversed().thenComparing(Student::getIndeks))
                .limit(n).collect(Collectors.toList());
    }

    public Map<Integer,Integer> getGradeDistribution()
    {
        Map<Integer,Integer> mapa=this.mapaOdStudneti.values().stream().map(student -> student.getOcena())
                .collect(Collectors.groupingBy(ocena->ocena, () -> new TreeMap<Integer, Integer>(),Collectors.summingInt(ocena->1)));
        IntStream.range(5,11).forEach(brojka->mapa.putIfAbsent(brojka,0));
        return mapa;
    }

    public void printStatistics()
    {
        DoubleSummaryStatistics ds=this.mapaOdStudneti.values().stream().filter(student -> student.getOcena()>=6)
                .mapToDouble(student->student.getSumarniPoeni()).summaryStatistics();
        //Count: 1 Min: 79.10 Average: 79.10 Max: 79.10
        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f",ds.getCount(),ds.getMin(),ds.getAverage(),ds.getMax()));
    }

    public Map<String, Student> getMapaOdStudneti() {
        return mapaOdStudneti;
    }

    public void setMapaOdStudneti(Map<String, Student> mapaOdStudneti) {
        this.mapaOdStudneti = mapaOdStudneti;
    }

    public Map<String, InterfejsZaPopolnuvanjeVoStudent> getMapaZaObjektiNaInterfejs() {
        return mapaZaObjektiNaInterfejs;
    }

    public void setMapaZaObjektiNaInterfejs(Map<String, InterfejsZaPopolnuvanjeVoStudent> mapaZaObjektiNaInterfejs) {
        this.mapaZaObjektiNaInterfejs = mapaZaObjektiNaInterfejs;
    }

    @Override
    public String toString() {
        return "AdvancedProgrammingCourse{" +
                "mapaOdStudneti=" + mapaOdStudneti +
                ", mapaZaObjektiNaInterfejs=" + mapaZaObjektiNaInterfejs +
                '}';
    }
}

public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
