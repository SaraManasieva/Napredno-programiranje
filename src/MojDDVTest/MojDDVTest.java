package MojDDVTest;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception
{
    public AmountNotAllowedException(int broj) {
        super(String.format("Receipt with amount %d is not allowed to be scanned",broj));
    }
}
enum TipNaDanok
{
    A,B,V
}
class Item
{
    //item_price1 item_tax_type1
    private int cena;
    private TipNaDanok tipNaDanok;

    public Item(int cena, TipNaDanok tipNaDanok) {
        this.cena = cena;
        this.tipNaDanok = tipNaDanok;
    }

    public Item(int cena) {
        this.cena = cena;
    }

    public Item(String delOdLinija) {
        String[]parts=delOdLinija.split("\\s+");
        this.cena=Integer.parseInt(parts[0]);
        this.tipNaDanok=TipNaDanok.valueOf(parts[1]);
    }

    public double iznosNaDanok()
    {
        //422895 399 V 2380 A 2380 B 2380 V 874 B 874 V 2417 V
        //88.665
        if(getTipNaDanok().equals(TipNaDanok.A))//591.1
        {
            return getCena()*0.18*0.15;//428.4
        }
        else if(getTipNaDanok().equals(TipNaDanok.B))
        {
            return getCena()*0.05*0.15;//162.7
        }
        else
        {
            return 0.0*getCena();
        }

    }
    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public TipNaDanok getTipNaDanok() {
        return tipNaDanok;
    }

    public void setTipNaDanok(TipNaDanok tipNaDanok) {
        this.tipNaDanok = tipNaDanok;
    }

    @Override
    public String toString() {
        return "Item{" +
                "cena=" + cena +
                ", tipNaDanok=" + tipNaDanok +
                '}';
    }
}
class Fiskalna
{
    ////ID item_price1 item_tax_type1 item_price2 item_tax_type2 … item_price-n item_tax_type-n
    // 12334 1789 А 1238 B 1222 V 111 V
    private String id;
    private List<Item> items;

    public Fiskalna(String id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public Fiskalna(String linija) throws AmountNotAllowedException {
        String[]parts=linija.split("\\s+");
        this.id=parts[0];
        List<Item> lista=new ArrayList<>();
        Arrays.stream(parts).skip(1)
                .forEach(str->{
                    if(Character.isDigit(str.charAt(0)))
                    {
                        Item item=new Item(Integer.parseInt(str));
                        lista.add(item);
                    }
                    else
                    {
                        lista.get(lista.size()-1).setTipNaDanok(TipNaDanok.valueOf(str));

                    }
                });
        this.items=new ArrayList<Item>(lista);
        if(presmetajVkupenIznos()>30000)
        {
            throw  new AmountNotAllowedException(presmetajVkupenIznos());
        }
    }

    public int presmetajVkupenIznos()
    {
        return this.items.stream().mapToInt(item->item.getCena()).sum();
    }
    //“ID SUM_OF_AMOUNTS TAX_RETURN”
    public double presmetajPovratokNaDDV()
    {
        return this.items.stream().mapToDouble(item->item.iznosNaDanok()).sum();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f",getId(),presmetajVkupenIznos(),presmetajPovratokNaDDV());
    }
}
class MojDDV
{
    private List<Fiskalna> fiskalni;

    public void readRecords(InputStream in) {

        //ID item_price1 item_tax_type1 item_price2 item_tax_type2 … item_price-n item_tax_type-n
        //12334 1789 А 1238 B 1222 V 111 V
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        this.fiskalni=br.lines().map(linija-> {
            try {
                return new Fiskalna(linija);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());


    }

    public void printTaxReturns(OutputStream os) {
        PrintWriter pw=new PrintWriter(os);
        //“ID SUM_OF_AMOUNTS TAX_RETURN”
        this.fiskalni.forEach(fiskalna -> pw.println(fiskalna));
        pw.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}