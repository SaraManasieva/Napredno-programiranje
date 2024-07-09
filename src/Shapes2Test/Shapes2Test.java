package Shapes2Test;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class IrregularCanvasException extends Exception
{
    public IrregularCanvasException(String message) {
        super(message);
    }
}
enum TipNaForma
{
    Kvadrat,Krug
}

class Kvadrat extends Forma
{

    public Kvadrat( TipNaForma tipNaForma,int strana) {
        super(tipNaForma, strana);
    }

    public Kvadrat() {
    }

    @Override
    public double getPlostinaNaForma() {
        return 1.0*getDolzina()*getDolzina();
    }
}

class Krug extends Forma
{

    public Krug(TipNaForma tipNaForma,int dolzina) {
        super(tipNaForma, dolzina);
    }

    public Krug() {
    }

    @Override
    public double getPlostinaNaForma() {
        return Math.PI*getDolzina()*getDolzina();
    }
}
abstract class Forma
{
    private TipNaForma tip;
    private int dolzina;

    public Forma() {

    }

    public Forma(TipNaForma tip, int dolzina) {
        this.tip = tip;
        this.dolzina = dolzina;
    }

    abstract public double getPlostinaNaForma();

    public TipNaForma getTip() {
        return tip;
    }

    public void setTip(TipNaForma tip) {
        this.tip = tip;
    }

    public int getDolzina() {
        return dolzina;
    }

    public void setDolzina(int dolzina) {
        this.dolzina = dolzina;
    }


    @Override
    public String toString() {
        return "Forma{" +
                "tip=" + tip +
                ", dolzina=" + dolzina +
                '}';
    }
}
class Prozorec
{
    private String id;
    private List<Forma> formi;

    public Prozorec(String id, List<Forma> formi) {
        this.id = id;
        this.formi = formi;
    }

    public static Prozorec napraviProzorec(String linija) throws IrregularCanvasException {
        //0cc31e47 C 27 C 13 C 29 C 15 C 22
        String [] parts=linija.split("\\s+");
        String id=parts[0];
        List<Forma> lista=new ArrayList<>();
        Arrays.stream(parts).skip(1).forEach(string->{
            if(Character.isAlphabetic(string.charAt(0)))
            {
                if(string.charAt(0)=='C')
                {
                    lista.add(new Krug());
                    lista.get(lista.size()-1).setTip(TipNaForma.Krug);
                }

                if(string.charAt(0)=='S')
                {
                    lista.add(new Kvadrat());
                    lista.get(lista.size()-1).setTip(TipNaForma.Kvadrat);
                }
            }
            else
            {
                lista.get(lista.size()-1).setDolzina(Integer.parseInt(string));
            }
        });
        Prozorec p= new Prozorec(id,lista);
        if(p.max_area()>ShapesApplication.maxArea)
        {
            throw new IrregularCanvasException(String.format("Canvas %s has a shape with area larger than %.2f", p.getId(),ShapesApplication.maxArea));
        }
        return p;
    }

    public double sumaNaPlostiniNaSiteFormi()
    {
        return this.formi.stream().mapToDouble(forma->forma.getPlostinaNaForma()).sum();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Forma> getFormi() {
        return formi;
    }

    public void setFormi(List<Forma> formi) {
        this.formi = formi;
    }

    public int brojNaKvadrati()
    {
        return (int) formi.stream().filter(forma -> forma.getTip().equals(TipNaForma.Kvadrat)).count();
    }

    public int brojNaKrugovi()
    {
        return (int) formi.stream().filter(forma -> forma.getTip().equals(TipNaForma.Krug)).count();
    }

    public double min_area()
    {
        return this.formi.stream().mapToDouble(forma->forma.getPlostinaNaForma()).min().getAsDouble();
    }

    public double max_area()
    {
        return this.formi.stream().mapToDouble(forma->forma.getPlostinaNaForma()).max().getAsDouble();
    }

    public double average_area()
    {
        return this.formi.stream().mapToDouble(forma->forma.getPlostinaNaForma()).average().getAsDouble();
    }

    @Override
    public String toString() {
        //5e28f402 11 5 6 100.00 2642.08 1007.35
        return String.format("%s %d %d %d %.2f %.2f %.2f"
                ,getId(),getFormi().size(),brojNaKrugovi(),brojNaKvadrati(),min_area(),max_area(),average_area());
    }
}
class ShapesApplication
{
    private List<Prozorec> prozorci;

    public static double maxArea;

    void readCanvases (InputStream inputStream)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.prozorci=br.lines().map(linija-> {
            try {
                return Prozorec.napraviProzorec(linija);
            } catch (IrregularCanvasException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

    }

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
    }

    public ShapesApplication(List<Prozorec> prozorci) {
        this.prozorci = prozorci;
    }

    public List<Prozorec> getProzorci() {
        return prozorci;
    }

    public void setProzorci(List<Prozorec> prozorci) {
        this.prozorci = prozorci;
    }

    @Override
    public String toString() {
        return "ShapesApplication{" +
                "prozorci=" + prozorci +
                '}';
    }

    public void printCanvases(OutputStream out) {
        PrintWriter pw=new PrintWriter(out);
        //Прозорците да се сортирани во опаѓачки редослед според сумата на плоштините на геометриските слики во нив.
        this.prozorci.stream().sorted(Comparator.comparing(Prozorec::sumaNaPlostiniNaSiteFormi).reversed())
                        .forEach(prozorec -> pw.println(prozorec));
        pw.flush();
    }
}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}