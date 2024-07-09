package RiskTester2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Igrac
{
    private List<Integer> listaOdTriBrojki;

    public Igrac(String polovinLinija) {
        this.listaOdTriBrojki = Arrays.stream(polovinLinija.split("\\s+"))
                .map(str->Integer.parseInt(str)).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    public List<Integer> getListaOdTriBrojki() {
        return listaOdTriBrojki;
    }
}

class Igra
{
    private Igrac igrac1;
    private Igrac igrac2;

    public Igra(String linija) {
        this.igrac1=new Igrac(linija.split(";")[0]);
        this.igrac2=new Igrac(linija.split(";")[1]);
    }

    public int counter()
    {
//        i=0;
//        return (int) igrac1.getListaOdTriBrojki().stream()
//                .filter(integer -> integer>igrac2.getListaOdTriBrojki().get(i++))
//                .count();
        return (int) IntStream.range(0,igrac1.getListaOdTriBrojki().size())
                .filter(i->igrac1.getListaOdTriBrojki().get(i)>igrac2.getListaOdTriBrojki().get(i))
                .count();
    }

    @Override
    public String toString() {
        return String.format("%d %d",counter(),3-counter());
    }
}
class Risk
{

    public void processAttacksData(InputStream is) {
        //5 3 4;2 4 1
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        br.lines().map(linija->new Igra(linija)).forEach(igra-> System.out.println(igra));
    }
}

public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}