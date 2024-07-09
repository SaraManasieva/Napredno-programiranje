package DailyTemperatureTest;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * I partial exam 2016
 */

class TemperaturiPoDen
{
    private int brojNaDen;
    private List<Double> listaOdTemperaturi;
    private char daliECiliF;


    public TemperaturiPoDen(int brojNaDen, List<Double> listaOdTemperaturi) {
        this.brojNaDen = brojNaDen;
        this.listaOdTemperaturi = listaOdTemperaturi;
    }

    public Double pretvoriStringVoDouble(String part)
    {
        //24C
        return Double.parseDouble(part.substring(0,part.length()-1));
    }

    public TemperaturiPoDen(String linija) {
        //317 24C 29C 28C 29C
        String[]parts=linija.split("\\s+");
        this.brojNaDen=Integer.parseInt(parts[0]);
        this.listaOdTemperaturi= IntStream.range(1,parts.length)
                .mapToDouble(broj->{
                    if(parts[broj].charAt(parts[broj].length()-1)=='C')
                    {
                        this.daliECiliF='C';
                        return pretvoriStringVoDouble(parts[broj]);
                    }
                    else
                    {
                        this.daliECiliF='F';
                        return getPretvoriFVoC(pretvoriStringVoDouble(parts[broj]));
                    }

                })
                .boxed().collect(Collectors.toList());

    }

    public static double getPretvoriFVoC(double t) {
        // $\frac{(T - 32) * 5}{9}$
        return ((t-32)*5)/9;
    }

    public static double getPretvoriCVoF(double t) {
        // $\frac{T * 9}{5} + 32$
        return ((t*9)/5)+32;
    }



    public int getBrojNaDen() {
        return brojNaDen;
    }

    public void setBrojNaDen(int brojNaDen) {
        this.brojNaDen = brojNaDen;
    }

    public char getDaliECiliF() {
        return daliECiliF;
    }

    public void setDaliECiliF(char daliECiliF) {
        this.daliECiliF = daliECiliF;
    }

    public List<Double> getListaOdTemperaturi() {
        return listaOdTemperaturi;
    }

    public void setListaOdTemperaturi(List<Double> listaOdTemperaturi) {
        this.listaOdTemperaturi = listaOdTemperaturi;
    }

    public String vratiStringVoZavisostOdChar(char scale)
    {
        DoubleSummaryStatistics ds=getListaOdTemperaturi().stream().mapToDouble(d->d).summaryStatistics();
        if(scale=='C')
        {
            return String.format("%3d: Count: %3d Min:%7.2fC Max:%7.2fC Avg:%7.2fC",getBrojNaDen(),
                    ds.getCount(),ds.getMin(),ds.getMax(),ds.getAverage());
        }
        else
        {

            return String.format("%3d: Count: %3d Min:%7.2fF Max:%7.2fF Avg:%7.2fF",getBrojNaDen(),
                    ds.getCount(),getPretvoriCVoF(ds.getMin()),getPretvoriCVoF(ds.getMax()),getPretvoriCVoF(ds.getAverage()));
        }
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics ds=getListaOdTemperaturi().stream().mapToDouble(d->d).summaryStatistics();
        if(getDaliECiliF()=='C')
        {
            //Минималната, максималната и просечната температура се печатат со 6 места, од кои 2 децимални,
            // а по бројот се запишува во која скала е температурата (C/F).
            return String.format("%3d: Count:%3d Min:%6.2fC Max:%6.2fC Avg:%6.2fC",getBrojNaDen(),
                    ds.getCount(),ds.getMin(),ds.getMax(),ds.getAverage());
        }
        else
        {

            return String.format("%3d: Count:%3d Min:%6.2fF Max:%6.2fF Avg:%6.2fF",getBrojNaDen(),
                    getPretvoriCVoF(ds.getCount()),getPretvoriCVoF(ds.getMin()),getPretvoriCVoF(ds.getMax()),getPretvoriCVoF(ds.getAverage()));
        }

    }
}

class DailyTemperatures
{
    private List<TemperaturiPoDen> listaOdTempPoDen;

    public DailyTemperatures() {
        this.listaOdTempPoDen=new ArrayList<>();
    }

    void readTemperatures(InputStream inputStream)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.listaOdTempPoDen=br.lines().map(linija->new TemperaturiPoDen(linija))
                .collect(Collectors.toList());

    }

    void writeDailyStats(OutputStream outputStream, char scale)
    {
        PrintWriter pw=new PrintWriter(outputStream);
        this.listaOdTempPoDen.stream().sorted(Comparator.comparing(TemperaturiPoDen::getBrojNaDen)).forEach(td->pw.println(td.vratiStringVoZavisostOdChar(scale)));
        pw.flush();
    }

    public DailyTemperatures(List<TemperaturiPoDen> listaOdTempPoDen) {
        this.listaOdTempPoDen = listaOdTempPoDen;
    }

    public List<TemperaturiPoDen> getListaOdTempPoDen() {
        return listaOdTempPoDen;
    }

    @Override
    public String toString() {
        return "DailyTemperatures{" +
                "listaOdTempPoDen=" + listaOdTempPoDen +
                '}';
    }

    public void setListaOdTempPoDen(List<TemperaturiPoDen> listaOdTempPoDen) {
        this.listaOdTempPoDen = listaOdTempPoDen;
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde