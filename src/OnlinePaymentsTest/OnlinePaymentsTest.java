package OnlinePaymentsTest;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Stavka
{
    //STUDENT_IDX ITEM_NAME PRICE.
    private String indeks;
    private String imeStavka;
    private int cena;

    public Stavka(String indeks, String imeStavka, int cena) {
        this.indeks = indeks;
        this.imeStavka = imeStavka;
        this.cena = cena;
    }

    public static Stavka createStavka(String linija)
    {
        String[]parts=linija.split(";");
        return new Stavka(parts[0],parts[1],Integer.parseInt(parts[2]));
    }

    public String getIndeks() {
        return indeks;
    }

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public String getImeStavka() {
        return imeStavka;
    }

    public void setImeStavka(String imeStavka) {
        this.imeStavka = imeStavka;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    //Student: 151020 Net: 13050 Fee: 149 Total: 13199
    //Items:
    //1. Школарина за летен семестар 2022/2023 12300
    //2. Административно-материјални трошоци и осигурување 750
    //Student 151021 not found!
    //Student 151022 not found!
    //Student 151023 not found!
    //Student 151024 not found!
    @Override
    public String toString() {
        return String.format("%s %d",imeStavka,cena);
    }
}

class Student
{
    private String indeks;
    private List<Stavka> stavki;

    public Student(String indeks, List<Stavka> stavki) {
        this.indeks = indeks;
        this.stavki = stavki;
    }

    public String getIndeks() {
        return indeks;
    }

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public List<Stavka> getStavki() {
        return stavki;
    }

    public void setStavki(List<Stavka> stavki) {
        this.stavki = stavki;
    }
    public int neto()
    {
        return this.stavki.stream().mapToInt(stavka->stavka.getCena())
                .sum();
    }

    public int provizija()
    {
        int p=(int) Math.round(neto()*0.0114);
        if(p>300)
        {
            return 300;
        }
        if (p<3)
        {
            return 3;
        }
        return p;
    }

    public int vkupno()
    {
        return provizija()+neto();
    }

    //Student: 151020 Net: 13050 Fee: 149 Total: 13199
    //Items:
    //1. Школарина за летен семестар 2022/2023 12300
    //2. Административно-материјални трошоци и осигурување 750
    //Student 151021 not found!
    //Student 151022 not found!
    //Student 151023 not found!
    //Student 151024 not found!
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Student: %s Net: %d Fee: %d Total: %d\n",indeks,neto(),provizija(),vkupno()));
        sb.append(String.format("Items:\n"));
        AtomicInteger atomicInteger=new AtomicInteger(1);
        sb.append(this.stavki.stream().sorted(Comparator.comparing(Stavka::getCena).reversed())
                .map(stavka -> String.format("%d. %s",atomicInteger.getAndIncrement(),stavka))
                .collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
class OnlinePayments
{
    private Map<String,Student> mapa;
    public OnlinePayments() {
        this.mapa=new HashMap<>();
    }

    //void readItems (InputStream is)
    public void readItems(InputStream is) {
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        //151020;Административно-материјални трошоци и осигурување;750
        br.lines().map(linija->Stavka.createStavka(linija))
                .forEach(stavka -> {
                    mapa.putIfAbsent(stavka.getIndeks(),new Student(stavka.getIndeks(),new ArrayList<>()));
                    //mapa.get(stavka.getIndeks()).getStavki().add(stavka);
                    mapa.computeIfPresent(stavka.getIndeks(),(k,v)->{
                        v.getStavki().add(stavka);
                        return v;
                    });
                });
    }


    public void printStudentReport(String id, OutputStream out) {
        //Student: 151020 Net: 13050 Fee: 149 Total: 13199
        //Items:
        //1. Школарина за летен семестар 2022/2023 12300
        //2. Административно-материјални трошоци и осигурување 750
        //Student 151021 not found!
        //Student 151022 not found!
        //Student 151023 not found!
        //Student 151024 not found!
        PrintStream pw=new PrintStream(out);
        Student s=mapa.get(id);
        //!mapa.containsKey(id)
        if(s==null)
        {
            pw.println(String.format("Student %s not found!",id));
        }
        else
        {
           pw.println(s);
        }
        pw.flush();
    }
}

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}