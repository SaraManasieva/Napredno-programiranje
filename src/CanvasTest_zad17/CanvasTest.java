package CanvasTest_zad17;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

abstract class Forma
{
    protected String id;

    public Forma(String id) throws InvalidIDException {
        if(!Canvas.getDaliIdEVoRed(id))
        {
            throw new InvalidIDException(String.format("ID %s is not valid",id));
        }
        this.id = id;
    }
    abstract void scale(double coef);
    abstract double getPlostina();
    abstract double getPerimetar();

    public String getId() {
        return id;
    }
}
class Krug extends Forma
{
    private double radius;

    public Krug(String id, double radius) throws InvalidIDException {
        super(id);
        this.radius = radius;
    }

    @Override
    void scale(double coef) {
        this.radius*=coef;
    }

    @Override
    double getPlostina() {
        return Math.PI*Math.pow(radius,2);
    }

    @Override
    double getPerimetar() {
        return 2*Math.PI*radius;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f",
                radius,getPlostina(),getPerimetar());
    }
}
class Kvadrat extends Forma
{
    private double strana;

    public Kvadrat(String id, double strana) throws InvalidIDException {
        super(id);
        this.strana = strana;
    }

    @Override
    void scale(double coef) {
        this.strana*=coef;
    }

    @Override
    double getPlostina() {
        return Math.pow(strana,2);
    }

    @Override
    double getPerimetar() {
        return 4*strana;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f",
                strana,getPlostina(),getPerimetar());
    }
}
class Pravoagolnik extends Forma
{
    private double dolzina;
    private double sirina;

    public Pravoagolnik(String id, double dolzina, double sirina) throws InvalidIDException {
        super(id);
        this.dolzina = dolzina;
        this.sirina = sirina;
    }

    @Override
    void scale(double coef) {
        this.dolzina*=coef;
        this.sirina*=coef;
    }

    @Override
    double getPlostina() {
        return dolzina*sirina;
    }

    @Override
    double getPerimetar() {
        return 2*(sirina+dolzina);
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f",
                dolzina,sirina,getPlostina(),getPerimetar());
    }
}
class User implements Comparable<User>
{
    private String id;
    private Set<Forma>formi;

    public User(String id, Set<Forma> formi) {
        this.id = id;
        this.formi = formi;
    }

    public int getBrojNaFormi()
    {
        return this.formi.size();
    }
    public double sumaNaPlostini()
    {
        return this.formi.stream().mapToDouble(f->f.getPlostina()).sum();
    }
    @Override
    public int compareTo(User o) {
        Comparator<User>comparator=Comparator.comparing(User::getBrojNaFormi).thenComparing(User::sumaNaPlostini);
        return comparator.compare(this,o);
    }

    public String getId() {
        return id;
    }

    public Set<Forma> getFormi() {
        return formi;
    }
}
class Canvas
{
    private Set<Forma>listaOdFormi;

    public Canvas() {
        this.listaOdFormi=new TreeSet<>(Comparator.comparing(Forma::getPlostina));
    }

    void readShapes (InputStream is) throws InvalidDimensionException, IOException {
        //1 123456 4.8835
        //2 123456 11.6807
        //3 123456 12.4201 8.7382
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String linija;
        while ((linija=br.readLine())!=null)
        {
            String[]parts=linija.split("\\s+");
            if(parts[0].equals("1"))//krug
            {
                try {
                    if(Double.parseDouble(parts[2])==0)
                    {
                        throw new InvalidDimensionException("Dimension 0 is not allowed!");
                    }
                    listaOdFormi.add(new Krug(parts[1],Double.parseDouble(parts[2])));
                } catch (InvalidIDException e) {
                    System.out.println(e.getMessage());
                }
            }
            else if(parts[0].equals("2"))//kvadrat
            {
                try {
                    if(Double.parseDouble(parts[2])==0)
                    {
                        throw new InvalidDimensionException("Dimension 0 is not allowed!");
                    }
                    listaOdFormi.add(new Kvadrat(parts[1],Double.parseDouble(parts[2])));
                } catch (InvalidIDException e) {
                    System.out.println(e.getMessage());
                }
            }
            else//pravoagolnik
            {
                try {
                    if(Double.parseDouble(parts[2])==0||Double.parseDouble(parts[3])==0)
                    {
                        throw new InvalidDimensionException("Dimension 0 is not allowed!");
                    }
                    listaOdFormi.add(new Pravoagolnik(parts[1],Double.parseDouble(parts[2]),Double.parseDouble(parts[3])));
                } catch (InvalidIDException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        }


        //this.listaOdFormi=this.listaOdFormi.stream().sorted(Comparator.comparing(Forma::getPlostina)).collect(Collectors.toList());;

    void scaleShapes (String userID, double coef)
    {
        this.listaOdFormi.stream()
                .filter(f->f.id.equals(userID))
                .forEach(f->f.scale(coef));
    }

    void printAllShapes (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        this.listaOdFormi.stream()
                        .forEach(f->pw.println(f));
        pw.flush();
    }

    void printByUserId (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        StringBuilder sb=new StringBuilder();
        Map<String,Set<Forma>>mapa=
                listaOdFormi.stream()
                                .collect(Collectors.groupingBy(f->f.id
                                ,Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Forma::getPerimetar)))));

        Comparator<Map.Entry<String,Set<Forma>>>entryComparator=
                Comparator.comparing(entry->entry.getValue().size());
        mapa.entrySet().stream()
                        .sorted(entryComparator.reversed()
                                .thenComparing(entry->entry.getValue()
                                        .stream().mapToDouble(f->f.getPlostina()).sum()))
                                .forEach(entry->{
                                    sb.append(String.format("Shapes of user: %s\n",entry.getKey()));
                                    entry.getValue().stream()
                                            .forEach(f->sb.append(f).append("\n"));
                                });
        sb.setLength(sb.length()-1);
        pw.println(sb.toString());
        pw.flush();
    }
    void statistics (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        DoubleSummaryStatistics dss=listaOdFormi.stream().mapToDouble(f->f.getPlostina()).summaryStatistics();
        //count: 5
        //sum: 852.06
        //min: 51.86
        //average: 170.41
        //max: 306.99
        pw.println(String.format("count: %d\nsum: %.2f\nmin: %.2f\naverage: %.2f\nmax: %.2f",
                dss.getCount(),dss.getSum(),dss.getMin(),dss.getAverage(),dss.getMax()));
        pw.flush();
    }

    public static boolean getDaliIdEVoRed(String id) {
        return id.length()==6&&getDaliNemaSpecZnaci(id);
    }

    public static boolean getDaliNemaSpecZnaci(String id) {
        return id.chars().allMatch(c->Character.isLetterOrDigit(c));
    }


}
class InvalidIDException extends Exception
{
    public InvalidIDException(String message) {
        super(message);
    }
}
class InvalidDimensionException extends Exception
{
    public InvalidDimensionException(String message) {
        super(message);
    }
}

public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try {
            canvas.readShapes(System.in);
        } catch (InvalidDimensionException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {

        }

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}