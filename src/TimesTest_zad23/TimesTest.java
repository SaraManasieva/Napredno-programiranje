package TimesTest_zad23;

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
    private int cas;
    private int min;

    public Vreme(String delOdLinija) throws UnsupportedFormatException, InvalidTimeException {
        //11:15
        //0.45
        //0*23
        String[]parts=delOdLinija.split(":");
        if(parts.length==1)
        {
            parts=delOdLinija.split("\\.");
        }
        if(parts.length==1)
        {
            throw new UnsupportedFormatException(delOdLinija);
        }
        this.cas=Integer.parseInt(parts[0]);
        this.min=Integer.parseInt(parts[1]);
        if(cas<0||cas>23||min<0||min>59)
        {
            throw new InvalidTimeException(delOdLinija);
        }
    }

    public int getCas() {
        return cas;
    }

    public int getMin() {
        return min;
    }

    @Override
    public String toString() {
        return String.format("%2d:%02d",cas,min);
    }

    public String getAmPmString() {
        //за првиот час од денот (0:00 - 0:59), додадете 12 и направете го "AM"
        //од 1:00 до 11:59, само направето го "AM"
        //од 12:00 до 12:59, само направето го "PM"
        //од 13:00 до 23:59 одземете 12 и направете го "PM"
        if(cas==0)
        {
            cas+=12;
            return String.format("%2d:%02d AM",cas,min);
        }
        else if (cas>=1&&cas<=11)
        {
            return String.format("%2d:%02d AM",cas,min);
        }
        else if(cas==12)
        {
            return String.format("%2d:%02d PM",cas,min);
        }
        else if(cas>=13&&cas<=23)
        {
            cas-=12;
            return String.format("%2d:%02d PM",cas,min);
        }
        else
        {
            return "";
        }
    }
}
class TimeTable
{
    private List<Vreme>listaOdVreminja;

    public TimeTable() {
        this.listaOdVreminja=new ArrayList<>();
    }
    void readTimes(InputStream inputStream) throws InvalidTimeException, UnsupportedFormatException {
        //11:15 0.45 23:12 15:29 18.46
        Scanner scanner=new Scanner(inputStream);
        while (scanner.hasNextLine())
        {
            String linija=scanner.nextLine();
            String[]parts=linija.split("\\s+");
            for(String part:parts)
            {
                this.listaOdVreminja.add(new Vreme(part));
            }
        }
    }
    void writeTimes(OutputStream outputStream, TimeFormat format)
    {
        PrintWriter pw=new PrintWriter(outputStream);
        if(format.equals(TimeFormat.FORMAT_24))
        {
            this.listaOdVreminja.stream().sorted(Comparator.comparing(Vreme::getCas).thenComparing(Vreme::getMin))
                    .forEach(vreme->pw.println(vreme));
        }
        else
        {
            this.listaOdVreminja.stream().sorted(Comparator.comparing(Vreme::getCas).thenComparing(Vreme::getMin))
                    .forEach(vreme->pw.println(vreme.getAmPmString()));
        }
        pw.flush();
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