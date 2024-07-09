package EventCalendarTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

class WrongDateException extends Exception
{
    public WrongDateException(String message) {
        super(message);
    }
}

class Nastan
{
    private String ime;
    private String lokacija;
    private Date vreme;

    public Nastan(String ime, String lokacija, Date vreme) {
        this.ime = ime;
        this.lokacija = lokacija;
        this.vreme = vreme;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    @Override
    public String toString() {
        //dd MMM, YYY HH:mm at [location], [name].
        //Thu Feb 14 11:00:00 UTC 2013
        DateFormat dateFormat=new SimpleDateFormat("dd MMM, YYY HH:mm");
        return String.format("%s at %s, %s",dateFormat.format(getVreme()),getLokacija(),getIme());
    }
}
class EventCalendar
{
    private Map<Integer, Set<Nastan>> mapaOdSetOdNastaniPoDen;
    private Map<Integer,Integer> mapaOdBrojNANastaniPoMesec;
    private int year;

    public EventCalendar(int year)
    {
        this.year=year;
        this.mapaOdSetOdNastaniPoDen=new TreeMap<>();
        this.mapaOdBrojNANastaniPoMesec=new TreeMap<>();
    }

    public int getDay(Date d)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public int getMOnth(Date d)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.MONTH);
    }

    public int getGodina(Date d)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.YEAR);
    }
    public void addEvent(String name, String location, Date date) throws WrongDateException {
        if(getGodina(date)!=getYear())
        {
            throw new WrongDateException(String.format("Wrong date: %s",date));
        }
        Nastan n=new Nastan(name,location,date);
        this.mapaOdSetOdNastaniPoDen.putIfAbsent(getDay(date),new TreeSet<>(Comparator.comparing(Nastan::getVreme).thenComparing(Nastan::getIme)));
        this.mapaOdSetOdNastaniPoDen.get(getDay(date)).add(n);
        this.mapaOdBrojNANastaniPoMesec.putIfAbsent(getMOnth(date)+1,0);
        this.mapaOdBrojNANastaniPoMesec.computeIfPresent(getMOnth(date)+1,(k,v)->++v);
        IntStream.range(1,13).forEach(broj->this.mapaOdBrojNANastaniPoMesec.putIfAbsent(broj,0));
    }

    public void listEvents(Date date)
    {
        if(this.mapaOdSetOdNastaniPoDen.get(getDay(date))==null)
        {
            System.out.println("No events on this day!");
        }
        this.mapaOdSetOdNastaniPoDen.values().stream().flatMap(set->set.stream())
                .filter(nastan -> getDay(nastan.getVreme())==getDay(date))
                .forEach(nastan -> System.out.println(nastan));
    }

    public void listByMonth()
    {
        this.mapaOdBrojNANastaniPoMesec.entrySet().stream()
                .forEach(entry-> System.out.println(String.format("%d : %d",entry.getKey(),entry.getValue())));
    }

    public EventCalendar(Map<Integer, Set<Nastan>> mapaOdSetOdNastaniPoDen, Map<Integer, Integer> mapaOdBrojNANastaniPoMesec) {
        this.mapaOdSetOdNastaniPoDen = mapaOdSetOdNastaniPoDen;
        this.mapaOdBrojNANastaniPoMesec = mapaOdBrojNANastaniPoMesec;
    }

    public Map<Integer, Set<Nastan>> getMapaOdSetOdNastaniPoDen() {
        return mapaOdSetOdNastaniPoDen;
    }

    public void setMapaOdSetOdNastaniPoDen(Map<Integer, Set<Nastan>> mapaOdSetOdNastaniPoDen) {
        this.mapaOdSetOdNastaniPoDen = mapaOdSetOdNastaniPoDen;
    }

    public Map<Integer, Integer> getMapaOdBrojNANastaniPoMesec() {
        return mapaOdBrojNANastaniPoMesec;
    }

    public void setMapaOdBrojNANastaniPoMesec(Map<Integer, Integer> mapaOdBrojNANastaniPoMesec) {
        this.mapaOdBrojNANastaniPoMesec = mapaOdBrojNANastaniPoMesec;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "EventCalendar{" +
                "mapaOdSetOdNastaniPoDen=" + mapaOdSetOdNastaniPoDen +
                ", mapaOdBrojNANastaniPoMesec=" + mapaOdBrojNANastaniPoMesec +
                '}';
    }
}

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde