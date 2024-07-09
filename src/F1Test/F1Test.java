package F1Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class NedozvolenoVremeException extends Exception
{

    public NedozvolenoVremeException(int i) {
        super(String.format("Ne postoi takov vozac so vreme %d",i));
    }

    public NedozvolenoVremeException(String string) {
     super(string);
    }
}

class NedozvolenoVremeException1 extends Exception
{
    public NedozvolenoVremeException1(String string) {
        super(string);
    }
}

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class Vozac
{
    private String ime;
    private List<String> listaOdLap;



    public Vozac(String ime, List<String> lap) {
        this.ime = ime;
        this.listaOdLap = lap;
    }



    public static Vozac kreirajVozac(String linija) {
        //Vetel 1:55:523 1:54:987 1:56:134.
        String[]parts=linija.split("\\s+");
        String name=parts[0];
        List<String> list= Arrays.stream(parts).skip(1).collect(Collectors.toList());
        Vozac v=new Vozac(name,list);
        if(parts[0].equals("Vettel"))
            return null;
        else
            return v;
            //throw new NedozvolenoVremeException1("message");

    }

    public int pretvoriStringVoMilisec(String string)
    {
        //1:55:523
        String[]parts=string.split(":");
        return Integer.parseInt(parts[0])*60*1000+Integer.parseInt(parts[1])*1000+Integer.parseInt(parts[2]);
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public List<String> getListaOdLap() {
        return listaOdLap;
    }

    public void setListaOdLap(List<String> listaOdLap) {
        this.listaOdLap = listaOdLap;
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s",getIme(),getNajdobroVreme());
    }

    public String getNajdobroVreme() {
        int minVreme= this.listaOdLap.stream().mapToInt(str->pretvoriStringVoMilisec(str))
                .min().getAsInt();
        return this.listaOdLap.stream().filter(str->pretvoriStringVoMilisec(str)==minVreme).collect(Collectors.toList()).get(0);
    }
}

class F1Race {
    private List<Vozac> vozaci;
    public F1Race() {
        this.vozaci=new ArrayList<>();
    }
    // vashiot kod ovde
    //Vetel 1:55:523 1:54:987 1:56:134.
    //Driver_name lap1 lap2 lap3
    void readResults(InputStream inputStream)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.vozaci=br.lines().map(linija-> Vozac.kreirajVozac(linija)).collect(Collectors.toList());
        System.out.println(vozaci);
    }

    void printSorted(OutputStream outputStream)
    {
        PrintWriter pw=new PrintWriter(outputStream);
        AtomicInteger atomicInteger=new AtomicInteger(1);
        //sorted(Comparator.comparing(Vozac::getNajdobroVreme)).
        System.out.println(vozaci);
        this.vozaci.stream().forEach(vozac ->
        {
            try {
                if(vozac.equals(null))
                {
                    throw new NullPointerException();
                }
                else
                {
                    pw.println(String.format("%d. %s",atomicInteger.getAndIncrement(),vozac));
                }
            }
            catch (NullPointerException e)
            {
                pw.println(String.format("%d. Objktot e null",atomicInteger.getAndIncrement()));
            }
//            if(vozac==null)
//            {
//                try {
//                    throw new NedozvolenoVremeException(String.format("%d. Objktot e null",atomicInteger.getAndIncrement()));
//                } catch (NedozvolenoVremeException e) {
//                    pw.println(e.getMessage());
//                }
//            }
//               else
//
//            pw.println(String.format("%d. %s",atomicInteger.getAndIncrement(),vozac));
        });
        pw.flush();
    }

}