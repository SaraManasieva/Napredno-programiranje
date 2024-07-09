package RiskTester;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Napad
{
    //X1 X2 X3;Y1 Y2 Y3
    private List<Integer> igrac1;
    private List<Integer> igrac2;

    public Napad(List<Integer> igrac1, List<Integer> igrac2) {
        this.igrac1 = igrac1;
        this.igrac2 = igrac2;
    }

    public static Napad napraviNapad(String linija)
    {
        String[]parts=linija.split(";");
        String[]p1=parts[0].split("\\s+");
        String[]p2=parts[1].split("\\s+");
        List<Integer> l1=Arrays.stream(p1).map(str->Integer.parseInt(str)).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        List<Integer> l2=Arrays.stream(p2).map(str->Integer.parseInt(str)).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return new Napad(l1,l2);
    }

    public int getProveriDaliNapadotEUspesen()
    {
        int counter=(int)IntStream.range(0,igrac1.size())
                .filter(brojka->igrac1.get(brojka)>igrac2.get(brojka)).count();
        if(counter==3)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public List<Integer> getIgrac1() {
        return igrac1;
    }

    public void setIgrac1(List<Integer> igrac1) {
        this.igrac1 = igrac1;
    }

    public List<Integer> getIgrac2() {
        return igrac2;
    }

    public void setIgrac2(List<Integer> igrac2) {
        this.igrac2 = igrac2;
    }

    @Override
    public String toString() {
        return "Napad{" +
                "igrac1=" + igrac1 +
                ", igrac2=" + igrac2 +
                '}';
    }
}
class Risk
{
    int processAttacksData (InputStream is)
    {
        //X1 X2 X3;Y1 Y2 Y3
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        return br.lines().map(linija->Napad.napraviNapad(linija))
                .mapToInt(napad->napad.getProveriDaliNapadotEUspesen()).sum();
    }
}

public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}