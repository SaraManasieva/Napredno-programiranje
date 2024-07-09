package DiscountsTest5;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class Proizvod
{
    //GAP 501:593  6135:7868  1668:2582  3369:4330  9702:15999  8252:13674
    private int cena;
    private int cenaNaPopust;

    public Proizvod(String str) {
        String[]parts=str.split(":");
        this.cenaNaPopust=Integer.parseInt(parts[0]);
        this.cena=Integer.parseInt(parts[1]);
    }

    public int getPopust()
    {
        return Math.abs(cena-cenaNaPopust);
    }

    public int getPopustVoProcenti()
    {
        return (int) (100*(getPopust()/(cena*1.0)));
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d",getPopustVoProcenti(),cenaNaPopust,cena);
    }
}
class Store
{

    private String ime;
    private List<Proizvod> listaOdProizvodi;
    public Store(String linija) {
        this.listaOdProizvodi=new ArrayList<>();
        //GAP 501:593  6135:7868  1668:2582  3369:4330  9702:15999  8252:13674
        String[]parts=linija.split("\\s+");
        this.ime=parts[0];
        this.listaOdProizvodi= Arrays.stream(parts)
                .skip(1).map(str->new Proizvod(str))
                .collect(Collectors.toList());
    }

    public double getProsecenPopust()
    {
        return this.listaOdProizvodi.stream().mapToInt(p->p.getPopustVoProcenti())
                .average().orElse(0.0);
    }
    public int getVkupenPopust()
    {
        return this.listaOdProizvodi.stream().mapToInt(p->p.getPopust())
                .sum();
    }

    public String getIme() {
        return ime;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        //Levis
        //Average discount: 35.8%
        //Total discount: 21137
        //48% 2579/4985
        //47% 9988/19165
        //36% 7121/11287
        //35% 1501/2316
        //32% 6385/9497
        //17% 6853/8314
        sb.append(ime).append("\n");
        sb.append(String.format("Average discount: %.1f%%\n",getProsecenPopust()));
        sb.append(String.format("Total discount: %d\n",getVkupenPopust()));
        this.listaOdProizvodi.stream().sorted(Comparator.comparing(Proizvod::getPopustVoProcenti)
                .thenComparing(Proizvod::getPopust).reversed())
                .forEach(p->sb.append(p).append("\n"));
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
}
class Discounts
{
    private List<Store>listaOdStore;

    public Discounts() {
        this.listaOdStore=new ArrayList<>();
    }

    public int readStores(InputStream inputStream)
    {
        //GAP 501:593  6135:7868  1668:2582  3369:4330  9702:15999  8252:13674
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.listaOdStore=br.lines().map(linija->new Store(linija))
                .collect(Collectors.toList());
        return this.listaOdStore.size();
    }

    public List<Store> byAverageDiscount()
    {
        return this.listaOdStore.stream().sorted(Comparator.comparing(Store::getProsecenPopust).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount()
    {
        return this.listaOdStore.stream().sorted(Comparator.comparing(Store::getVkupenPopust)
                .thenComparing(Store::getIme))
                .limit(3).collect(Collectors.toList());
    }
}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}