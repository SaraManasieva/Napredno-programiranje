package FacultyTest;

//package mk.ukim.finki.vtor_kolokvium;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class OperationNotAllowedException extends Exception
{
    public OperationNotAllowedException(String message) {
        super(message);
    }
}
abstract class Student
{
    protected String indeks;
    protected Map<Integer,List<Integer>> mapaOdListaNaOcenkiPoSemestar;
    protected Set<String> setOdPolozeniPredmeti;

    public Student(String indeks, Map<Integer, List<Integer>> mapaOdListaNaOcenkiPoSemestar, Set<String> setOdPolozeniPredmeti) {
        this.indeks = indeks;
        this.mapaOdListaNaOcenkiPoSemestar = mapaOdListaNaOcenkiPoSemestar;
        this.setOdPolozeniPredmeti = setOdPolozeniPredmeti;
    }

    public Student(String indeks) {
        this.indeks = indeks;
        this.mapaOdListaNaOcenkiPoSemestar=new TreeMap<>();
        this.setOdPolozeniPredmeti=new TreeSet<>();
    }
    public double getProsekNaStudent()
    {
        return getMapaOdListaNaOcenkiPoSemestar().values().stream().flatMap(lista->lista.stream())
                .mapToInt(o->o).average().orElse(5.0);
    }

    double getProsekVoDadenSemestar(int brNASemestar)
    {
        return this.mapaOdListaNaOcenkiPoSemestar.get(brNASemestar).stream().mapToInt(o->o).average().orElse(5.0);
    }

    abstract boolean dodajOcenki(int brNaSemestar,String imeNaPredmet,int ocenka) throws OperationNotAllowedException;

    void validacijaPoBrojNaSemestar(int brNaSemestar) throws OperationNotAllowedException {
        if(!mapaOdListaNaOcenkiPoSemestar.containsKey(brNaSemestar))
        {
            throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s",brNaSemestar,getIndeks()));
        }
        if(mapaOdListaNaOcenkiPoSemestar.get(brNaSemestar).size()==3)
        {
            throw new OperationNotAllowedException(String.format("Student %s already has 3 grades in term %d",getIndeks(),brNaSemestar));
        }
    }

    public String getDetalenIzvestajNaStudent()
    {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Student: %s",getIndeks())).append("\n");
        mapaOdListaNaOcenkiPoSemestar.keySet().forEach(sem->sb.append(getIzvestajPoSemestar(sem)).append("\n"));
        sb.append(String.format("Average grade: %.2f",getProsekNaStudent())).append("\n");
        sb.append(String.format("Courses attended: %s",
                String.join(",",getSetOdPolozeniPredmeti())));
        return sb.toString();
    }

    private String getIzvestajPoSemestar(int brNaSem) {
        return String.format("Term %d\nCourses: %d\nAverage grade for term: %.2f",brNaSem,
                mapaOdListaNaOcenkiPoSemestar.get(brNaSem).size(),getProsekVoDadenSemestar(brNaSem));
    }

    public String getKratokIzvestajZaStudent()
    {
        return String.format("Student: %s Courses passed: %d Average grade: %.2f",
                getIndeks(),getBrojNaPolozeniIspiti(),getProsekNaStudent());
    }

    public int getBrojNaPolozeniIspiti()
    {
        return (int) mapaOdListaNaOcenkiPoSemestar.values().stream()
                .flatMap(lista->lista.stream())
                .mapToInt(o->o).count();
    }

    public String getPorakaAkoDiplomiral()
    {
        return String.format("Student with ID %s graduated with average grade %.2f",indeks,getProsekNaStudent());
    }

    public String getIndeks() {
        return indeks;
    }

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public Map<Integer, List<Integer>> getMapaOdListaNaOcenkiPoSemestar() {
        return mapaOdListaNaOcenkiPoSemestar;
    }

    public void setMapaOdListaNaOcenkiPoSemestar(Map<Integer, List<Integer>> mapaOdListaNaOcenkiPoSemestar) {
        this.mapaOdListaNaOcenkiPoSemestar = mapaOdListaNaOcenkiPoSemestar;
    }

    public Set<String> getSetOdPolozeniPredmeti() {
        return setOdPolozeniPredmeti;
    }

    public void setSetOdPolozeniPredmeti(Set<String> setOdPolozeniPredmeti) {
        this.setOdPolozeniPredmeti = setOdPolozeniPredmeti;
    }


}

class StudenNaTrigodisniStudii extends Student
{

    public StudenNaTrigodisniStudii(String indeks) {
        super(indeks);
        IntStream.range(1,7).forEach(i->mapaOdListaNaOcenkiPoSemestar.putIfAbsent(i,new ArrayList<>()));
    }

    @Override
    boolean dodajOcenki(int brNaSemestar, String imeNaPredmet, int ocenka) throws OperationNotAllowedException {
        validacijaPoBrojNaSemestar(brNaSemestar);
        mapaOdListaNaOcenkiPoSemestar.get(brNaSemestar).add(ocenka);
        setOdPolozeniPredmeti.add(imeNaPredmet);
        return getBrojNaPolozeniIspiti()==18;
    }

    @Override
    public String getPorakaAkoDiplomiral() {
        return super.getPorakaAkoDiplomiral()+" in 3 years.";
    }
}

class StudenNaCetirigodisniStudii extends Student
{

    public StudenNaCetirigodisniStudii(String indeks) {
        super(indeks);
        IntStream.range(1,9).forEach(i->mapaOdListaNaOcenkiPoSemestar.putIfAbsent(i,new ArrayList<>()));
    }

    @Override
    boolean dodajOcenki(int brNaSemestar, String imeNaPredmet, int ocenka) throws OperationNotAllowedException {
        validacijaPoBrojNaSemestar(brNaSemestar);
        mapaOdListaNaOcenkiPoSemestar.get(brNaSemestar).add(ocenka);
        setOdPolozeniPredmeti.add(imeNaPredmet);
        return getBrojNaPolozeniIspiti()==24;
    }

    @Override
    public String getPorakaAkoDiplomiral() {
        return super.getPorakaAkoDiplomiral()+" in 4 years.";
    }
}

class Predmet
{
    private String imeNaPredmet;
    IntSummaryStatistics iss;

    public Predmet(String imeNaPredmet) {
        this.imeNaPredmet = imeNaPredmet;
        this.iss = new IntSummaryStatistics();
    }

    void dodajOcenka(int ocena)
    {
        iss.accept(ocena);
    }

    public String getImeNaPredmet() {
        return imeNaPredmet;
    }

    public void setImeNaPredmet(String imeNaPredmet) {
        this.imeNaPredmet = imeNaPredmet;
    }

    public IntSummaryStatistics getIss() {
        return iss;
    }

    public void setIss(IntSummaryStatistics iss) {
        this.iss = iss;
    }

    public int getBrojNStudentiStoGoSlusatTojPredmet()
    {
        return (int)iss.getCount();
    }

    double getProsecnaOcenaPoPredmet(){
        return iss.getAverage();
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f",getImeNaPredmet(),(int) iss.getCount(),iss.getAverage());
    }
}

class Faculty {
    private Map<String,Student> mapaNAStudentiPoIndeks;
    private Map<String,Predmet> mapaOdPredmetiPoImeNAPredmet;
    private StringBuilder logs;


    public Faculty() {
        this.mapaNAStudentiPoIndeks=new HashMap<>();
        this.mapaOdPredmetiPoImeNAPredmet=new HashMap<>();
        this.logs=new StringBuilder();
    }

    void addStudent(String id, int yearsOfStudies) {
        this.mapaNAStudentiPoIndeks.put(id,yearsOfStudies==3?new StudenNaTrigodisniStudii(id):new StudenNaCetirigodisniStudii(id));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        Student student=mapaNAStudentiPoIndeks.get(studentId);
            boolean informacijaZaDiplomiranje=student.dodajOcenki(term,courseName,grade);
            this.mapaOdPredmetiPoImeNAPredmet.putIfAbsent(courseName,new Predmet(courseName));
            this.mapaOdPredmetiPoImeNAPredmet.get(courseName).dodajOcenka(grade);
            if(informacijaZaDiplomiranje)
            {
                logs.append(student.getPorakaAkoDiplomiral()).append("\n");
                this.mapaNAStudentiPoIndeks.remove(studentId);
            }

    }

    String getFacultyLogs() {
        logs.setLength(logs.length()-1);
        return logs.toString();
    }

    String getDetailedReportForStudent(String id) {
        return this.mapaNAStudentiPoIndeks.get(id).getDetalenIzvestajNaStudent();
    }

    void printFirstNStudents(int n) {
        this.mapaNAStudentiPoIndeks.values().stream().sorted(Comparator.comparing(Student::getBrojNaPolozeniIspiti).thenComparing(Student::getProsekNaStudent)
                .thenComparing(Student::getIndeks).reversed())
                .limit(n).forEach(s-> System.out.println(s.getKratokIzvestajZaStudent()));
    }

    void printCourses() {
        this.mapaOdPredmetiPoImeNAPredmet.values().stream().sorted(Comparator.comparing(Predmet::getBrojNStudentiStoGoSlusatTojPredmet)
                .thenComparing(Predmet::getProsecnaOcenaPoPredmet).thenComparing(Predmet::getImeNaPredmet)).forEach(p-> System.out.println(p));

    }

    public Map<String, Student> getMapaNAStudentiPoIndeks() {
        return mapaNAStudentiPoIndeks;
    }

    public void setMapaNAStudentiPoIndeks(Map<String, Student> mapaNAStudentiPoIndeks) {
        this.mapaNAStudentiPoIndeks = mapaNAStudentiPoIndeks;
    }

    public Map<String, Predmet> getMapaOdPredmetiPoImeNAPredmet() {
        return mapaOdPredmetiPoImeNAPredmet;
    }

    public void setMapaOdPredmetiPoImeNAPredmet(Map<String, Predmet> mapaOdPredmetiPoImeNAPredmet) {
        this.mapaOdPredmetiPoImeNAPredmet = mapaOdPredmetiPoImeNAPredmet;
    }

    public StringBuilder getLogs() {
        return logs;
    }

    public void setLogs(StringBuilder logs) {
        this.logs = logs;
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
