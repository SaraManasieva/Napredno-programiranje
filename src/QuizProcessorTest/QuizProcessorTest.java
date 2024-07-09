package QuizProcessorTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Isklucok extends Exception
{
    public Isklucok(String message) {
        super(message);
    }
}

class Student
{
    private String id;
    private List<PrasanjeIOdgovor> listaOdKvizoviNaStudent;

    public Student(String id) {
        this.id = id;
        this.listaOdKvizoviNaStudent = new ArrayList<>();
    }

    public void dodadiPrasanjeIOdgvor(PrasanjeIOdgovor prasanjeIOdgovor)
    {
        this.listaOdKvizoviNaStudent.add(prasanjeIOdgovor);
    }

    public double getVkupnoPoeniNaStudent()
    {
        return this.listaOdKvizoviNaStudent.stream().mapToDouble(p->p.getPoeni())
                .sum();
    }

    public String getId() {
        return id;
    }
}

class PrasanjeIOdgovor
{
    private String id;
    private List<String>tocniOdgovorii;
    private List<String>odgovoriNaZstudent;

    public PrasanjeIOdgovor(String linija) throws Isklucok {
        //ID; C1, C2, C3, C4, C5, … ,Cn; A1, A2, A3, A4, A5, …,An.
       String[]parts=linija.split("; ");
       if(parts[1].split(", ").length!=parts[2].split(", ").length)
       {
           throw new Isklucok("A quiz must have same number of correct and selected answers");
       }
       this.id=parts[0];
       this.tocniOdgovorii= Arrays.stream(parts[1].split(", "))
               .collect(Collectors.toList());
       this.odgovoriNaZstudent=Arrays.stream(parts[2].split(", "))
               .collect(Collectors.toList());
    }

    public double getPoeni()
    {
        return IntStream.range(0,odgovoriNaZstudent.size())
                .mapToDouble(i->{
                    if(odgovoriNaZstudent.get(i).equals(tocniOdgovorii.get(i)))
                    {
                        return 1;
                    }
                    else
                    {
                        return -0.25;
                    }
                }).sum();
    }

    public String getId() {
        return id;
    }

    public List<String> getTocniOdgovorii() {
        return tocniOdgovorii;
    }

    public List<String> getOdgovoriNaZstudent() {
        return odgovoriNaZstudent;
    }
}
class QuizProcessor
{

    static Map<String, Double> processAnswers(InputStream is)
    {
        //ID; C1, C2, C3, C4, C5, … ,Cn; A1, A2, A3, A4, A5, …,An.
        Map<String,Student> mapaNaStudentPoIndeks=new TreeMap<String, Student>();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        br.lines().map(linija-> {
                    try {
                        return new PrasanjeIOdgovor(linija);
                    } catch (Isklucok e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(p->{
                   mapaNaStudentPoIndeks.putIfAbsent(p.getId(),new Student(p.getId()));
                   mapaNaStudentPoIndeks.get(p.getId()).dodadiPrasanjeIOdgvor(p);
                });
        Map<String, Double> mapaNaPoeniPoIndeks=new TreeMap<>();
        mapaNaStudentPoIndeks.values().stream()
                .forEach(s->{
                    mapaNaPoeniPoIndeks.put(s.getId(),s.getVkupnoPoeniNaStudent());
                });
        return mapaNaPoeniPoIndeks;
    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}