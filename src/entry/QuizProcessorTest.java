package entry;

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
class Kviz
{
    //C1, C2, C3, C4, C5, … ,Cn
    private List<String> listaOdStringovi;

    public Kviz(List<String> listaOdStringovi) {
        this.listaOdStringovi = listaOdStringovi;
    }
    public static Kviz napraviKviz(String delOdLinija)
    {
        String[]parts=delOdLinija.split(", ");
        List<String> list= Arrays.stream(parts)
                .collect(Collectors.toList());
        return new Kviz(list);
    }

    public List<String> getListaOdStringovi() {
        return listaOdStringovi;
    }

    public void setListaOdStringovi(List<String> listaOdStringovi) {
        this.listaOdStringovi = listaOdStringovi;
    }

    @Override
    public String toString() {
        return "Kviz{" +
                "listaOdStringovi=" + listaOdStringovi +
                '}';
    }
}
class Student
{
    private String indeks;
    private Kviz tocniOdgovori;
    private Kviz odgovoriNaStudent;

    public Student(String indeks, Kviz tocniOdgovori, Kviz odgovoriNaStudent) {
        this.indeks = indeks;
        this.tocniOdgovori = tocniOdgovori;
        this.odgovoriNaStudent = odgovoriNaStudent;
    }

    public static Student napraviStudent (String linija) throws Isklucok {
        //ID; C1, C2, C3, C4, C5, … ,Cn; A1, A2, A3, A4, A5, …,An.
        String[]parts=linija.split(";");
        String id=parts[0];
        Kviz k1=Kviz.napraviKviz(parts[1]);
        Kviz k2=Kviz.napraviKviz(parts[2]);
        Student s= new Student(id,k1,k2);
        if(s.tocniOdgovori.getListaOdStringovi().size()!=s.odgovoriNaStudent.getListaOdStringovi().size())
            throw new Isklucok("Ne e dozvoleno");
        return s;
    }

    //квиз со 6 прашања, има точни 3 прашања, а неточни 3 прашања, студентот ќе освои 3*_1 - 3*_0.25 = 2.25.
    public double getPoeni()
    {
        int brojNaTocniOdgovori=(int)IntStream.range(0,tocniOdgovori.getListaOdStringovi().size())
                .filter(i->tocniOdgovori.getListaOdStringovi().get(i).equals(odgovoriNaStudent.getListaOdStringovi().get(i))).count();
        int brojNaNetocniOdgovori=(int)IntStream.range(0,tocniOdgovori.getListaOdStringovi().size())
                .filter(i->!tocniOdgovori.getListaOdStringovi().get(i).equals(odgovoriNaStudent.getListaOdStringovi().get(i))).count();
    return brojNaTocniOdgovori*3-0.25*brojNaNetocniOdgovori;
    }

    public String getIndeks() {
        return indeks;
    }

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public Kviz getTocniOdgovori() {
        return tocniOdgovori;
    }

    public void setTocniOdgovori(Kviz tocniOdgovori) {
        this.tocniOdgovori = tocniOdgovori;
    }

    public Kviz getOdgovoriNaStudent() {
        return odgovoriNaStudent;
    }

    public void setOdgovoriNaStudent(Kviz odgovoriNaStudent) {
        this.odgovoriNaStudent = odgovoriNaStudent;
    }

    @Override
    public String toString() {
        return "Student{" +
                "indeks='" + indeks + '\'' +
                ", tocniOdgovori=" + tocniOdgovori +
                ", odgovoriNaStudent=" + odgovoriNaStudent +
                '}';
    }
}
class QuizProcessor {
    private Map<String,Double> mapa;

    public static Map<String, Double> processAnswers(InputStream is) {
        Map<String,Double> mapa=new TreeMap<>();
        //ID; C1, C2, C3, C4, C5, … ,Cn; A1, A2, A3, A4, A5, …,An.
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        br.lines().map(linija->
                {
                    try {
                        return Student.napraviStudent(linija);
                    } catch (Isklucok e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(student -> {
                    mapa.put(student.getIndeks(),student.getPoeni());

                });
        return mapa;
    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}