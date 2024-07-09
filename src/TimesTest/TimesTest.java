package TimesTest;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class UnsupportedFormatException extends Exception
{
    public UnsupportedFormatException(String message) {
        super(message);
    }
}

class InvalidTimeException extends Exception
{
    public InvalidTimeException(String message) {
        super(message);
    }
}

class Vreme
{
    //11:15
    //18.46
    private int hour;
    private int min;

    public Vreme(int hour, int min) {
        this.hour = hour;
        this.min = min;
    }

    public Vreme(String delOdLinija) throws UnsupportedFormatException, InvalidTimeException {
        String[]parts=delOdLinija.split("\\.");
        if(parts.length==1)
        {
            parts=delOdLinija.split(":");
        }
        if(parts.length==1)
        {
            throw new UnsupportedFormatException(delOdLinija);
        }
        this.hour=Integer.parseInt(parts[0]);
        this.min=Integer.parseInt(parts[1]);
        if(hour<0||hour>23||min<0||min>59)
        {
            throw new InvalidTimeException(delOdLinija);
        }
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getAmPm()
    {
        String dodavka="AM";
        int h=getHour();
        if(h==0)
        {
            h=h+12;
        } else if (h==12) {
            dodavka="PM";
        } else if (h>12) {
            h=h-12;
            dodavka="PM";
        }
        return String.format("%2d:%02d %s",h,getMin(),dodavka);
    }

    @Override
    public String toString() {
        return String.format("%2d:%02d",getHour(),getMin());
    }
}

class TimeTable
{
    private List<Vreme> listaOdVreminja;

    public TimeTable() {
        this.listaOdVreminja=new ArrayList<>();
    }

    void readTimes(InputStream inputStream) throws UnsupportedFormatException,InvalidTimeException
    {
        //11:15 0.45 23:12 15:29 18.46
//        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
//        this.listaOdVreminja=br.lines().flatMap(line->Arrays.stream(line.split("\\s+")))
//                .map(del-> {
//                    try {
//                        return new Vreme(del);
//                    } catch (UnsupportedFormatException e) {
//                        return null;
//
//                    } catch (InvalidTimeException e) {
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull).collect(Collectors.toList());
        Scanner scanner=new Scanner(inputStream);
        while (scanner.hasNextLine())
        {
            String line=scanner.nextLine();
            String[]parts=line.split("\\s+");
            for(String p:parts)
            {
                Vreme vreme=new Vreme(p);
                this.listaOdVreminja.add(vreme);
            }
        }
    }

    void writeTimes(OutputStream outputStream, TimeFormat format)
    {
        PrintWriter pw=new PrintWriter(outputStream);
        this.listaOdVreminja.stream().sorted(Comparator.comparing(Vreme::getHour).thenComparing(Vreme::getMin))
                .forEach(vreme->{
                    if(format==TimeFormat.FORMAT_24)
                    {
                        pw.println(vreme);
                    }
                    else
                    {
                        pw.println(vreme.getAmPm());
                    }
                });
        pw.flush();
    }

    public List<Vreme> getListaOdVreminja() {
        return listaOdVreminja;
    }

    public void setListaOdVreminja(List<Vreme> listaOdVreminja) {
        this.listaOdVreminja = listaOdVreminja;
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "listaOdVreminja=" + listaOdVreminja +
                '}';
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}