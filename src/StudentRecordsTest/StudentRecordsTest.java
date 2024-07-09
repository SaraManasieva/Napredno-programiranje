package StudentRecordsTest;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
//10:02

/**
 * January 2016 Exam problem 1
 */
class Nasoka
{
    private String imeNaNasoka;
    private Set<Student> setOdStudentiVoNasoka;

    public Nasoka(String imeNaNasoka) {
        this.imeNaNasoka=imeNaNasoka;
        this.setOdStudentiVoNasoka=new TreeSet<>(Comparator.comparing(Student::getProsekNaStudent).reversed().thenComparing(Student
        ::getKod));
    }

    public static String pecatiZvezdi(int broj)
    {
        return IntStream.range(0,(int) (Math.ceil(broj/10.0)))
                .mapToObj(i->"*")
                .collect(Collectors.joining(""));
    }

    public String pecatiDistribucijaPoNasoka()
    {
        //KNI
        // 6 | ***********(103)
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%s\n", getImeNaNasoka()));
        Map<Integer,Integer> mapa=getSetOdStudentiVoNasoka().stream().flatMap(student -> student.getOceni().stream())
                .collect(Collectors.groupingBy(ocena->ocena,TreeMap::new,Collectors.summingInt(ocena->1)));
        mapa.keySet().stream().forEach((k->{
            String str=String.format("%2d | %s(%d)\n",k,Nasoka.pecatiZvezdi(mapa.get(k)),mapa.get(k));
            sb.append(str);
        }));
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public int getBrojNaOcenkiSporedVrednost(int ocena)
    {
        return (int) getSetOdStudentiVoNasoka().stream().flatMap(student -> student.getOceni().stream())
                .filter(o->o==ocena).count();
    }

    public int getBrojNaDestki()
    {
        return getBrojNaOcenkiSporedVrednost(10);
    }

    public int getBrojNaOceniVoNasoka()
    {
        return (int) getSetOdStudentiVoNasoka().stream().flatMap(student -> student.getOceni().stream())
                .count();
    }
    public void addStudent(Student s)
    {
        setOdStudentiVoNasoka.add(s);
    }

    public Nasoka(String imeNaNasoka, Set<Student> setOdStudentiVoNasoka) {
        this.imeNaNasoka = imeNaNasoka;
        this.setOdStudentiVoNasoka = setOdStudentiVoNasoka;
    }

    public String getImeNaNasoka() {
        return imeNaNasoka;
    }

    public void setImeNaNasoka(String imeNaNasoka) {
        this.imeNaNasoka = imeNaNasoka;
    }

    public Set<Student> getSetOdStudentiVoNasoka() {
        return setOdStudentiVoNasoka;
    }

    public void setSetOdStudentiVoNasoka(Set<Student> setOdStudentiVoNasoka) {
        this.setOdStudentiVoNasoka = setOdStudentiVoNasoka;
    }

    @Override
    public String toString() {
        return "Nasoka{" +
                "imeNaNasoka='" + imeNaNasoka + '\'' +
                ", setOdStudentiVoNasoka=" + setOdStudentiVoNasoka +
                '}';
    }
}
class Student
{
    private String kod;
    private String nasoka;
    private List<Integer> oceni;

    public Student(String kod, String nasoka, List<Integer> oceni) {
        this.kod = kod;
        this.nasoka = nasoka;
        this.oceni = oceni;
    }

    public Student(String linija) {
        //ioqmx7 MT 10 8 10 8 10 7 6 9 9 9 6 8 6 6 9 9 8
        String []parts=linija.split("\\s+");
        this.kod=parts[0];
        this.nasoka=parts[1];
        this.oceni=Arrays.stream(parts).skip(2)
                .filter(str->!str.contains("snip")).map(str->Integer.parseInt(str)).collect(Collectors.toList());
    }

    public double getProsekNaStudent()
    {
        return getOceni().stream().mapToDouble(o->o).average().getAsDouble();
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getNasoka() {
        return nasoka;
    }

    public void setNasoka(String nasoka) {
        this.nasoka = nasoka;
    }

    public List<Integer> getOceni() {
        return oceni;
    }

    public void setOceni(List<Integer> oceni) {
        this.oceni = oceni;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f",getKod(),getProsekNaStudent());
    }
}
class StudentRecords
{
    private List<Student> listaOdStudenti;
    private Map<String,Nasoka> mapaOdStudentiPoNasoka;

    public StudentRecords() {
        this.listaOdStudenti=new ArrayList<>();
        this.mapaOdStudentiPoNasoka=new TreeMap<>();
    }

    int readRecords(InputStream inputStream) {
        //ioqmx7 MT 10 8 10 8 10 7 6 9 9 9 6 8 6 6 9 9 8
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.listaOdStudenti=br.lines().map(line->new Student(line))
                .collect(Collectors.toList());
        this.listaOdStudenti.stream().forEach(student -> {
            this.mapaOdStudentiPoNasoka.putIfAbsent(student.getNasoka(),new Nasoka(student.getNasoka()));
            this.mapaOdStudentiPoNasoka.get(student.getNasoka()).addStudent(student);
        });
        return listaOdStudenti.size();
    }

    void writeTable(OutputStream outputStream) {
        //метод кој ги печати сите записите за сите студенти групирани по насока
        // (најпрво се печати името на насоката), а потоа се печатат сите записи за
        // студентите од таа насока сортирани според просекот во опаѓачки редослед
        // (ако имаат ист просек според кодот лексикографски) во формат kod prosek,
        // каде што просекот е децимален број заокружен на две децимали. Пример jeovz8 8.47.
        // Насоките се сортирани лексикографски. Комплексноста на методот да не надминува $O(N)$
        // во однос на бројот на записи.
        //IKI
        //ookrq3 8.86
        //rkmkc7 8.58
        //ywiuo5 8.56
        PrintWriter pw=new PrintWriter(outputStream);
        StringBuilder sb=new StringBuilder();
        mapaOdStudentiPoNasoka.keySet().stream().forEach(kluc->
        {
            sb.append(String.format("%s\n",kluc));
            String str=mapaOdStudentiPoNasoka.get(kluc).getSetOdStudentiVoNasoka()
                            .stream().map(student -> student.toString())
                            .collect(Collectors.joining("\n"));
            sb.append(str);
            sb.append("\n");
        });
        sb.setLength(sb.length()-1);
        pw.println(sb.toString());
        pw.flush();
    }

    void writeDistribution(OutputStream outputStream) {
        PrintWriter pw=new PrintWriter(outputStream);
        this.mapaOdStudentiPoNasoka.values().stream().sorted(Comparator.comparing(Nasoka::getBrojNaDestki).reversed())
                        .forEach(nasoka -> pw.println(nasoka.pecatiDistribucijaPoNasoka()));
        pw.flush();
    }
}
public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here