package WeatherStationTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
//9:12

class Merenje
{
    private float temperature;
    private float wind;
    private float humidity;
    private float visibility;
    private Date date;

    public Merenje(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getWind() {
        return wind;
    }

    public void setWind(float wind) {
        this.wind = wind;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",getTemperature(),getWind(),
                getHumidity(),getVisibility(),getDate().toString().replace("UTC","GMT"));
    }
}
class WeatherStation
{
    private List<Merenje> listaODMerenja;
    private int brojNaDenovi;

    public WeatherStation(int brojNaDenovi) {
        this.brojNaDenovi = brojNaDenovi;
        this.listaODMerenja=new ArrayList<>();
    }
    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date)
    {
        Merenje m=new Merenje(temperature,wind,humidity,visibility,date);
        this.listaODMerenja.removeIf(merenje->Math.abs(m.getDate().getTime()-merenje.getDate().getTime())>getBrojNaDenovi()*24*60*60*1000);
        if(!listaODMerenja.stream().anyMatch(merenje->Math.abs(merenje.getDate().getTime()-m.getDate().getTime())<2.5*60*1000))
        {
            this.listaODMerenja.add(m);
        }
        //System.out.println(listaODMerenja);
    }
    public int total()
    {
        return this.listaODMerenja.size();
    }

    public void status(Date from, Date to)
    {
        List<Merenje>filtrirani=this.listaODMerenja.stream().filter(merenje->merenje.getDate().getTime()>=from.getTime()&&merenje.getDate().getTime()<=to.getTime())
                .collect(Collectors.toList());
        if(filtrirani.size()==0)
        {
            throw new RuntimeException();
        }
        filtrirani.stream().sorted(Comparator.comparing(Merenje::getDate))
                .forEach(m-> System.out.println(m));
        System.out.println(String.format("Average temperature: %.2f",filtrirani.stream().mapToDouble(mer->mer.getTemperature()).average().getAsDouble()));
    }

    public int getBrojNaDenovi() {
        return brojNaDenovi;
    }

    public void setBrojNaDenovi(int brojNaDenovi) {
        this.brojNaDenovi = brojNaDenovi;
    }
}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde