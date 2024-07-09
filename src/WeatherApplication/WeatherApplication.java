package WeatherApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface IVremenskaPrognoza
{
    void ispecatiPoraka(float t,float v,float p);
}

class CurrentConditionsDisplay implements IVremenskaPrognoza
{
    private float temperatura;
    private float vlaznost;
    private WeatherDispatcher weatherDispatcher;

    public CurrentConditionsDisplay(WeatherDispatcher wd) {
        this.weatherDispatcher=wd;
        this.weatherDispatcher.register(this);
    }

    @Override
    public void ispecatiPoraka(float t, float v, float p) {
        //Temperature: 1.0F
        //Humidity: 2.0%
        this.temperatura=t;
        this.vlaznost=v;
        System.out.println(String.format("Temperature: %.1fF\nHumidity: %.1f%%",t,v));
    }
}
class ForecastDisplay implements IVremenskaPrognoza
{

    private float pritisok=0;
    private WeatherDispatcher weatherDispatcher;

    public ForecastDisplay(WeatherDispatcher wd) {
        this.weatherDispatcher=wd;
        this.weatherDispatcher.register(this);
    }

    @Override
    public void ispecatiPoraka(float t, float v, float p) {
        if(p>this.pritisok)
        {
            System.out.println("Forecast: Improving\n");
        }
        else if(p==this.pritisok)
        {
            System.out.println("Forecast: Same\n");
        }
        else
        {
            System.out.println("Forecast: Cooler\n");
        }
        this.pritisok=p;
    }

}
class WeatherDispatcher
{
    private float temperature;
    private float humidity;
    private float pressure;

    private List<IVremenskaPrognoza>listaodIVremenskaPrognoza;

    public WeatherDispatcher() {
        this.listaodIVremenskaPrognoza=new ArrayList<>();
    }

    public void setMeasurements(float temperature, float humidity, float pressure)
    {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        listaodIVremenskaPrognoza.stream().forEach(iv->iv.ispecatiPoraka(temperature,humidity,pressure));
    }

    public List<IVremenskaPrognoza> getListaodIVremenskaPrognoza() {
        return listaodIVremenskaPrognoza;
    }

    public void register(IVremenskaPrognoza iVremenskaPrognoza) {
        this.listaodIVremenskaPrognoza.add(iVremenskaPrognoza);
    }

    public void remove(IVremenskaPrognoza i) {
        this.listaodIVremenskaPrognoza.remove(i);
    }
}

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}