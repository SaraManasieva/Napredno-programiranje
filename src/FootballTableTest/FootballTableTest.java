package FootballTableTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */

class FudbalskiTim
{
    private String imeNaTim;
    private int odigraniNatprevari;
    private int brojNaPobedi;
    private int brojNaaNereseniNatprevari;
    private int brojNaOsvoeniPoeni;
    private int golRazlika;

    private int brojNaDadeniGolovi;
    private int getBrojNaPrimeniGolovi;

    private int brojNaPorazi;

    public FudbalskiTim(String imeNaTim) {
        this.imeNaTim = imeNaTim;
        this.odigraniNatprevari = 0;
        this.brojNaPobedi = 0;
        this.brojNaaNereseniNatprevari = 0;
        this.brojNaOsvoeniPoeni = 0;
        this.golRazlika = 0;
        this.brojNaDadeniGolovi = 0;
        this.getBrojNaPrimeniGolovi = 0;
        this.brojNaPorazi=0;
    }

    public FudbalskiTim() {

    }

    public int getBrojNaPorazi() {
        return brojNaPorazi;
    }

    public void setBrojNaPorazi(int brojNaPorazi) {
        this.brojNaPorazi = brojNaPorazi;
    }

    public String getImeNaTim() {
        return imeNaTim;
    }

    public void setImeNaTim(String imeNaTim) {
        this.imeNaTim = imeNaTim;
    }

    public int getOdigraniNatprevari() {
        return odigraniNatprevari;
    }

    public void setOdigraniNatprevari(int odigraniNatprevari) {
        this.odigraniNatprevari = odigraniNatprevari;
    }

    public int getBrojNaPobedi() {
        return brojNaPobedi;
    }

    public void setBrojNaPobedi(int brojNaPobedi) {
        this.brojNaPobedi = brojNaPobedi;
    }

    public int getBrojNaaNereseniNatprevari() {
        return brojNaaNereseniNatprevari;
    }

    public void setBrojNaaNereseniNatprevari(int brojNaaNereseniNatprevari) {
        this.brojNaaNereseniNatprevari = brojNaaNereseniNatprevari;
    }

    public int getBrojNaOsvoeniPoeni() {
        return 3*getBrojNaPobedi()+getBrojNaaNereseniNatprevari();
    }

    public void setBrojNaOsvoeniPoeni(int brojNaOsvoeniPoeni) {
        this.brojNaOsvoeniPoeni = brojNaOsvoeniPoeni;
    }

    public int getBrojNaDadeniGolovi() {
        return brojNaDadeniGolovi;
    }

    public void setBrojNaDadeniGolovi(int brojNaDadeniGolovi) {
        this.brojNaDadeniGolovi = brojNaDadeniGolovi;
    }

    public int getGetBrojNaPrimeniGolovi() {
        return getBrojNaPrimeniGolovi;
    }

    public void setGetBrojNaPrimeniGolovi(int getBrojNaPrimeniGolovi) {
        this.getBrojNaPrimeniGolovi = getBrojNaPrimeniGolovi;
    }

    public int getGolRazlika() {
        //разлика од постигнатите голови и примените голови
        return getBrojNaDadeniGolovi()-getBrojNaPrimeniGolovi;
    }

    public void setGolRazlika(int golRazlika) {
        this.golRazlika = golRazlika;
    }

    //името (со 15 места порамнето во лево), бројот на одиграни натпревари, бројот на победи,
    // бројот на нерешени натпревари, бројот на освоени поени (сите броеви се печатат со 5 места порамнети во десно).
    // Бројот на освоени поени се пресметува како број_на_победи x 3 + број_на_нерешени x 1.
    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d",getImeNaTim(),getOdigraniNatprevari(),getBrojNaPobedi(),
                getBrojNaaNereseniNatprevari(),getBrojNaPorazi(),getBrojNaOsvoeniPoeni());
    }
}
class FootballTable
{

    //Bournemouth;Man Utd;1;3
    Map<String,FudbalskiTim> stringFudbalskiTimMap;

    public FootballTable(Map<String, FudbalskiTim> stringFudbalskiTimMap) {
        this.stringFudbalskiTimMap = stringFudbalskiTimMap;
    }

    public FootballTable() {
        this.stringFudbalskiTimMap=new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals)
    {
        FudbalskiTim domasenTim=stringFudbalskiTimMap.computeIfAbsent(homeTeam,v->new FudbalskiTim(homeTeam));
        FudbalskiTim gostinskiTim=stringFudbalskiTimMap.computeIfAbsent(awayTeam,v->new FudbalskiTim(awayTeam));

        //domasenTim.setBrojNaDadeniGolovi(homeGoals);
        if(homeGoals>awayGoals)
        {
            domasenTim.setOdigraniNatprevari(domasenTim.getOdigraniNatprevari()+1);
            gostinskiTim.setOdigraniNatprevari(gostinskiTim.getOdigraniNatprevari()+1);
            domasenTim.setBrojNaDadeniGolovi(domasenTim.getBrojNaDadeniGolovi()+homeGoals);
            gostinskiTim.setBrojNaDadeniGolovi(gostinskiTim.getBrojNaDadeniGolovi()+awayGoals);
            domasenTim.setBrojNaPobedi(domasenTim.getBrojNaPobedi()+1);
            gostinskiTim.setBrojNaPorazi(gostinskiTim.getBrojNaPorazi()+1);
            domasenTim.setGetBrojNaPrimeniGolovi(domasenTim.getGetBrojNaPrimeniGolovi()+awayGoals);
            gostinskiTim.setGetBrojNaPrimeniGolovi(gostinskiTim.getGetBrojNaPrimeniGolovi()+homeGoals);
            // return String.format("%-15s%5d%5d%5d%5d%5d",getImeNaTim(),getOdigraniNatprevari(),getBrojNaPobedi(),
            //                getBrojNaaNereseniNatprevari(),getBrojNaPorazi(),getBrojNaOsvoeniPoeni());
        }

        if(homeGoals==awayGoals)
        {
            domasenTim.setOdigraniNatprevari(domasenTim.getOdigraniNatprevari()+1);
            gostinskiTim.setOdigraniNatprevari(gostinskiTim.getOdigraniNatprevari()+1);
            domasenTim.setBrojNaDadeniGolovi(domasenTim.getBrojNaDadeniGolovi()+homeGoals);
            gostinskiTim.setBrojNaDadeniGolovi(gostinskiTim.getBrojNaDadeniGolovi()+awayGoals);
            domasenTim.setBrojNaaNereseniNatprevari(domasenTim.getBrojNaaNereseniNatprevari()+1);
            gostinskiTim.setBrojNaaNereseniNatprevari(gostinskiTim.getBrojNaaNereseniNatprevari()+1);
            domasenTim.setGetBrojNaPrimeniGolovi(domasenTim.getGetBrojNaPrimeniGolovi()+awayGoals);
            gostinskiTim.setGetBrojNaPrimeniGolovi(gostinskiTim.getGetBrojNaPrimeniGolovi()+homeGoals);
        }

        if(homeGoals<awayGoals)
        {
            domasenTim.setOdigraniNatprevari(domasenTim.getOdigraniNatprevari()+1);
            gostinskiTim.setOdigraniNatprevari(gostinskiTim.getOdigraniNatprevari()+1);
            domasenTim.setBrojNaDadeniGolovi(domasenTim.getBrojNaDadeniGolovi()+homeGoals);
            gostinskiTim.setBrojNaDadeniGolovi(gostinskiTim.getBrojNaDadeniGolovi()+awayGoals);
            domasenTim.setBrojNaPorazi(domasenTim.getBrojNaPorazi()+1);
            gostinskiTim.setBrojNaPobedi(gostinskiTim.getBrojNaPobedi()+1);
            domasenTim.setGetBrojNaPrimeniGolovi(domasenTim.getGetBrojNaPrimeniGolovi()+awayGoals);
            gostinskiTim.setGetBrojNaPrimeniGolovi(gostinskiTim.getGetBrojNaPrimeniGolovi()+homeGoals);
        }
    }

    public void printTable()
    {
        //Тимовите се подредени според бројот на освоени поени во опаѓачки редослед, ако имаат ист број на освоени поени според
        // гол разликата (разлика од постигнатите голови и примените голови) во опаѓачки редослед,
        // а ако имаат иста гол разлика, според името.
        StringBuilder sb=new StringBuilder();
        AtomicInteger atomicInteger=new AtomicInteger(1);
        this.stringFudbalskiTimMap.keySet().stream()
                .map(string->stringFudbalskiTimMap.get(string))
                .sorted(Comparator.comparing(FudbalskiTim::getBrojNaOsvoeniPoeni).thenComparing(FudbalskiTim::getGolRazlika).reversed()
                        .thenComparing(FudbalskiTim::getImeNaTim)).forEach(fudbalskiTim -> System.out.println(String.format("%2d. %s",atomicInteger.getAndIncrement(),fudbalskiTim)));
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

