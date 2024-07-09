package AirportsTest;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Flight
{
    private String kodNaAerodromFrom;
    private String kodNaAerodromTo;
    private int vremeVoMinPosle00;
    private int vremetraenjeNaLetot;

    public Flight(String kodNaAerodromFrom, String kodNaAerodromTo, int vremeVoMinPosle00, int vremetraenjeNaLetot) {
        this.kodNaAerodromFrom = kodNaAerodromFrom;
        this.kodNaAerodromTo = kodNaAerodromTo;
        this.vremeVoMinPosle00 = vremeVoMinPosle00;
        this.vremetraenjeNaLetot = vremetraenjeNaLetot;
    }

    public String getKodNaAerodromFrom() {
        return kodNaAerodromFrom;
    }

    public void setKodNaAerodromFrom(String kodNaAerodromFrom) {
        this.kodNaAerodromFrom = kodNaAerodromFrom;
    }

    public String getKodNaAerodromTo() {
        return kodNaAerodromTo;
    }

    public void setKodNaAerodromTo(String kodNaAerodromTo) {
        this.kodNaAerodromTo = kodNaAerodromTo;
    }

    public int getVremeVoMinPosle00() {
        return vremeVoMinPosle00;
    }

    public void setVremeVoMinPosle00(int vremeVoMinPosle00) {
        this.vremeVoMinPosle00 = vremeVoMinPosle00;
    }

    public int getVremetraenjeNaLetot() {
        return vremetraenjeNaLetot;
    }

    public void setVremetraenjeNaLetot(int vremetraenjeNaLetot) {
        this.vremetraenjeNaLetot = vremetraenjeNaLetot;
    }

    @Override
    public String toString() {
        return String.format("%s-%s %02d:%02d-%02d:%02d%s %dh%02dm",kodNaAerodromFrom,kodNaAerodromTo,
                vremeVoMinPosle00/60,vremeVoMinPosle00%60,
                ((vremeVoMinPosle00+vremetraenjeNaLetot)/60)%24,
                (vremeVoMinPosle00+vremetraenjeNaLetot)%60
                ,(vremeVoMinPosle00+vremetraenjeNaLetot)/(60*24)>0 ? " +1d":"",
                (vremetraenjeNaLetot/60)%24,vremetraenjeNaLetot%60
                );
    }

}

class Airport
{
 private String imeNaAerodrom;
 private String drzava;
 private String kod;
 private int brNaPatnici;
 private Set<Flight> setOdLetoviVoAerodrom;

    public Airport(String imeNaAerodrom, String drzava, String kod, int brNaPatnici) {
        this.imeNaAerodrom = imeNaAerodrom;
        this.drzava = drzava;
        this.kod = kod;
        this.brNaPatnici = brNaPatnici;
        this.setOdLetoviVoAerodrom = new TreeSet<>(Comparator.comparing(Flight::getKodNaAerodromFrom).thenComparing(Flight::getKodNaAerodromTo)
                .thenComparing(Flight::getVremeVoMinPosle00).thenComparing(Flight::getVremetraenjeNaLetot));
    }

    public String getImeNaAerodrom() {
        return imeNaAerodrom;
    }

    public void setImeNaAerodrom(String imeNaAerodrom) {
        this.imeNaAerodrom = imeNaAerodrom;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public int getBrNaPatnici() {
        return brNaPatnici;
    }

    public void setBrNaPatnici(int brNaPatnici) {
        this.brNaPatnici = brNaPatnici;
    }

    public Set<Flight> getSetOdLetoviVoAerodrom() {
        return setOdLetoviVoAerodrom;
    }

    public void setSetOdLetoviVoAerodrom(Set<Flight> setOdLetoviVoAerodrom) {
        this.setOdLetoviVoAerodrom = setOdLetoviVoAerodrom;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "imeNaAerodrom='" + imeNaAerodrom + '\'' +
                ", drzava='" + drzava + '\'' +
                ", kod='" + kod + '\'' +
                ", brNaPatnici=" + brNaPatnici +
                ", setOdLetoviVoAerodrom=" + setOdLetoviVoAerodrom +
                '}';
    }
}


class Airports
{
    private Map<String,Airport> mapaOdAerodromPoKod;

    public Airports() {
        this.mapaOdAerodromPoKod = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers)
    {
        Airport airport=new Airport(name,country,code,passengers);
        this.mapaOdAerodromPoKod.put(airport.getKod(),airport);
    }

    public void addFlights(String from, String to, int time, int duration)
    {
        Flight flight=new Flight(from,to,time,duration);
        this.mapaOdAerodromPoKod.get(from).getSetOdLetoviVoAerodrom().add(flight);
    }

    public void showFlightsFromAirport(String code)
    {
        //Tokyo International (HND)
        //Japan
        //66795178
        //1. HND-AMS 14:44-19:16 4h32m
        //2. HND-DFW 17:28-22:20 4h52m
        //3. HND-PEK 04:59-06:49 1h50m
        //4. HND-PVG 21:13-01:29 +1d 4h16m
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%s (%s)\n",mapaOdAerodromPoKod.get(code).getImeNaAerodrom(),code));
        sb.append(mapaOdAerodromPoKod.get(code).getDrzava()).append("\n");
        sb.append(String.format("%d\n",mapaOdAerodromPoKod.get(code).getBrNaPatnici()));
        AtomicInteger ai=new AtomicInteger(1);
        String str=mapaOdAerodromPoKod.get(code).getSetOdLetoviVoAerodrom().stream()
                .map(flight -> String.format("%-1d. %s",ai.getAndIncrement(),flight))
                .collect(Collectors.joining("\n"));
        sb.append(str);
        System.out.println(sb.toString());
    }

    public void showDirectFlightsFromTo(String from, String to)
    {
        List<Flight> listaPom=this.mapaOdAerodromPoKod.values().stream().flatMap(a->a.getSetOdLetoviVoAerodrom().stream())
                .filter(let->let.getKodNaAerodromFrom().equals(from)&&let.getKodNaAerodromTo().equals(to))
                .collect(Collectors.toList());
        if(listaPom.isEmpty())
        {
            System.out.println(String.format("No flights from %s to %s",from,to));
        }
        else
        {
            listaPom.stream().sorted(Comparator.comparing(Flight::getVremeVoMinPosle00))
                    .forEach(let-> System.out.println(let));
        }
    }

    public void showDirectFlightsTo(String to)
    {
        List<Flight> listaPom=this.mapaOdAerodromPoKod.values().stream().flatMap(a->a.getSetOdLetoviVoAerodrom().stream())
                .filter(let->let.getKodNaAerodromTo().equals(to))
                .collect(Collectors.toList());
        if(listaPom.isEmpty())
        {
            System.out.println(String.format("No flights to %s",to));
        }
        else
        {
            listaPom.stream().sorted(Comparator.comparing(Flight::getVremeVoMinPosle00).thenComparing(Flight::getKodNaAerodromFrom))
                    .forEach(let-> System.out.println(let));
        }
    }

    public Map<String, Airport> getMapaOdAerodromPoKod() {
        return mapaOdAerodromPoKod;
    }

    public void setMapaOdAerodromPoKod(Map<String, Airport> mapaOdAerodromPoKod) {
        this.mapaOdAerodromPoKod = mapaOdAerodromPoKod;
    }

    @Override
    public String toString() {
        return "Airports{" +
                "mapaOdAerodromPoKod=" + mapaOdAerodromPoKod +
                '}';
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

