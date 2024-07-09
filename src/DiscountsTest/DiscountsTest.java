package DiscountsTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Discounts
 */

class NedozvolenPecatenje extends Exception
{
    public NedozvolenPecatenje(String message) {
        super(message);
    }

    public NedozvolenPecatenje() {
    }
}

class NullIsklucok extends Exception
{

}

class Proizvod
{
    private int namalenaCena;
    private int redovnaCena;

    public Proizvod(int namalenaCena, int redovnaCena) {
        this.namalenaCena = namalenaCena;
        this.redovnaCena = redovnaCena;
    }

    public Proizvod(String delOdLinija) throws NedozvolenPecatenje {
        String[]parts=delOdLinija.split(":");
        this.namalenaCena=Integer.parseInt(parts[0]);
        this.redovnaCena=Integer.parseInt(parts[1]);
    }

    public int getPopust()
    {
        return redovnaCena-namalenaCena;
    }

    public int getPopustVoProcenti()
    {
        return (int) (100*((redovnaCena-namalenaCena)/(redovnaCena*1.0)));
    }
    public int getNamalenaCena() {
        return namalenaCena;
    }

    public void setNamalenaCena(int namalenaCena) {
        this.namalenaCena = namalenaCena;
    }

    public int getRedovnaCena() {
        return redovnaCena;
    }

    public void setRedovnaCena(int redovnaCena) {
        this.redovnaCena = redovnaCena;
    }

    @Override
    public String toString() {
        //[процент во две места]% [цена на попуст]/[цена]
        return String.format("%2d%% %d/%d",getPopustVoProcenti(),namalenaCena,redovnaCena);
    }
}
class Store
{
    private String imeNaProdavnica;
    private List<Proizvod> proizvodi;

    public Store(String imeNaProdavnica, List<Proizvod> proizvodi) {
        this.imeNaProdavnica = imeNaProdavnica;
        this.proizvodi = proizvodi;
    }
    public Store(String linija)
    {
        String []parts=linija.split("\\s+");
        this.imeNaProdavnica=parts[0];
        this.proizvodi=Arrays.stream(parts).skip(1).map(delOdLin-> {
            try {
                return new Proizvod(delOdLin);
            } catch (NedozvolenPecatenje e) {
                return null;
            }
        }).collect(Collectors.toList());

    }

    public void addProizvod(Proizvod p) throws NedozvolenPecatenje {

        this.proizvodi.add(p);
    }

    public double getProsecenPopust()
    {
       return this.proizvodi.stream().mapToInt(proizvod->proizvod.getPopustVoProcenti()).average().getAsDouble();
    }
    public int getVkupenPopust()
    {
        return this.proizvodi.stream().mapToInt(proizvod->proizvod.getPopust()).sum();
    }

    public String getImeNaProdavnica() {
        return imeNaProdavnica;
    }

    public void setImeNaProdavnica(String imeNaProdavnica) {
        this.imeNaProdavnica = imeNaProdavnica;
    }

    public List<Proizvod> getProizvodi() {
        return proizvodi;
    }

    public void setProizvodi(List<Proizvod> proizvodi) {
        this.proizvodi = proizvodi;
    }
    //[Store_name]
    //Average discount: [заокружена вредност со едно децимално место]%
    //Total discount: [вкупен апсолутен попуст]
    //[процент во две места]% [цена на попуст]/[цена]
    //...

    //Levis
    //Average discount: 35.8%
    //Total discount: 21137
    //48% 2579/4985
    //47% 9988/19165
    //36% 7121/11287
    //35% 1501/2316
    //32% 6385/9497
    //17% 6853/8314
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%s\n",imeNaProdavnica));
        sb.append(String.format("Average discount: %.1f%%\n",getProsecenPopust()));
        sb.append(String.format("Total discount: %d\n",getVkupenPopust()));
        AtomicInteger atomicInteger=new AtomicInteger(1);
        this.proizvodi=this.proizvodi.stream().sorted(Comparator.comparing(Proizvod::getPopustVoProcenti)
                .thenComparing(Proizvod::getNamalenaCena).thenComparing(Proizvod::getRedovnaCena).reversed()).collect(Collectors.toList());
        IntStream.range(0,proizvodi.size())
                .forEach(i->
                {
                    try {
                        if(proizvodi.get(i).getPopustVoProcenti()>47)
                            throw new NedozvolenPecatenje(String.format("Ne e dozvoleno procent pogolem od 47 odnosno %d",proizvodi.get(i).getPopustVoProcenti()));
                        else
                        {
                            sb.append(String.format("%d. %s\n",atomicInteger.getAndIncrement(),proizvodi.get(i)));
                        }
                    }
                    catch (NedozvolenPecatenje e)
                    {
                        sb.append(String.format("%d. %s\n",atomicInteger.getAndIncrement(),e.getMessage()));
                    }


                });
        sb.setLength(sb.length()-1);
//        String str=IntStream.range(0,proizvodi.size())
//                .mapToObj(i->proizvodi.get(i).toString())
//                .collect(Collectors.joining("\n") );
        //sb.append(str);
        return sb.toString();
    }
}
class Discounts
{
    private List<Store> listaOdStore;
    public int readStores(InputStream inputStream)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.listaOdStore=br.lines().map(str->new Store(str)).collect(Collectors.toList());
        return listaOdStore.size();
    }

    public List<Store> byAverageDiscount()
    {
        return this.listaOdStore.stream().sorted(Comparator.comparing(Store::getProsecenPopust).reversed()
                .thenComparing(Store::getImeNaProdavnica)).limit(3).collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount()
    {
        return this.listaOdStore.stream().sorted(Comparator.comparing(Store::getVkupenPopust).thenComparing(Store::getImeNaProdavnica))
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
//5:44