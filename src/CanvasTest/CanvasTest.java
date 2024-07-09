package CanvasTest;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
interface Forma
{
     double getPlostina();
     double getPerimetar();
     void skaliraj(double koef);
     String getIdInterfejs();
}
class Krug implements Forma
{
    private String id;
    private double radius;

    public Krug(String id, double radius) {
        this.id = id;
        this.radius = radius;
    }

    @Override
    public double getPlostina() {
        return Math.PI * radius*radius;
    }

    @Override
    public double getPerimetar() {
        return 2*Math.PI*radius;
    }

    @Override
    public void skaliraj(double koef) {
        this.radius=radius*koef;
    }

    @Override
    public String getIdInterfejs() {
        return getId();
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f",getRadius(),getPlostina(),getPerimetar());
    }

}
class Kvadrat implements Forma
{
    private String id;
    private double strana;

    public Kvadrat(String id, double strana) {
        this.id = id;
        this.strana = strana;
    }

    @Override
    public double getPlostina() {
        return strana*strana;
    }

    @Override
    public double getPerimetar() {
        return 4*strana;
    }

    @Override
    public void skaliraj(double koef) {
        this.strana=strana*koef;
    }

    @Override
    public String getIdInterfejs() {
        return getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f",strana,getPlostina(),getPerimetar());
    }
}
class Pravoagolnik implements Forma
{
    private String id;
    private double dolzina;
    private double sirina;

    public Pravoagolnik(String id, double dolzina, double sirina) {
        this.id = id;
        this.dolzina = dolzina;
        this.sirina = sirina;
    }

    @Override
    public double getPlostina() {
        return dolzina*sirina;
    }

    @Override
    public double getPerimetar() {
        return 2*(dolzina+sirina);
    }

    @Override
    public void skaliraj(double koef) {
        this.dolzina=dolzina*koef;
        this.sirina=sirina*koef;
    }

    @Override
    public String getIdInterfejs() {
        return getId();
    }

    public double getDolzina() {
        return dolzina;
    }

    public void setDolzina(double dolzina) {
        this.dolzina = dolzina;
    }

    public double getSirina() {
        return sirina;
    }

    public void setSirina(double sirina) {
        this.sirina = sirina;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f",getDolzina(),getSirina(),getPlostina(),getPerimetar());
    }
}

class Korisnik
{
    private String id;
    private Set<Forma> setOdFormi;

    public Korisnik(String id, Set<Forma> setOdFormi) {
        this.id = id;
        this.setOdFormi = setOdFormi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Forma> getSetOdFormi() {
        return setOdFormi;
    }

    public void setSetOdFormi(Set<Forma> setOdFormi) {
        this.setOdFormi = setOdFormi;
    }
    public int getBrojNaFormi()
    {
        return getSetOdFormi().size();
    }
    public double getZbirNaSitePlostini()
    {
        return this.setOdFormi.stream().mapToDouble(f->f.getPlostina()).sum();
    }

    @Override
    public String toString() {
        //Shapes of user: 123456
        //Circle -> Radius: 7.33 Area: 168.58 Perimeter: 46.03
        //Rectangle: -> Sides: 18.63, 13.11 Area: 244.19 Perimeter: 63.47
        //Square: -> Side: 17.52 Area: 306.99 Perimeter: 70.08
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Shapes of user: %s\n",getId()));
        setOdFormi.stream().sorted(Comparator.comparing(Forma::getPerimetar))
                        .forEach(forma -> sb.append(forma).append("\n"));
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
}
class Klasa
{

    public static Forma napraviObjektOdForma(String linija) throws InvalidIDException {
        String[]parts=linija.split("\\s+");
        if(!(parts[1].length()==6&&proveri(parts[1])))
        {
                throw new InvalidIDException(String.format("ID %s is not valid",parts[1]));

        }
        int broj=Integer.parseInt(parts[0]);
        switch (broj)
        {
            case 1:
                return new Krug(parts[1],Double.parseDouble(parts[2]));
            case 2:
                return new Kvadrat(parts[1],Double.parseDouble(parts[2]));
            case 3:
                return new Pravoagolnik(parts[1],Double.parseDouble(parts[2]),Double.parseDouble(parts[3]));
            default: return null;
        }
    }

    private static boolean proveri(String string) {
        return string.chars().allMatch(c->Character.isLetterOrDigit(c));
    }

}

class Canvas
{
    private Set<Forma> setOdInterfForma;
    private Map<String,Korisnik>mapaOdKorisnikPoId;

    public Canvas() {
        this.setOdInterfForma =new TreeSet<>(Comparator.comparing(Forma::getPlostina));
        this.mapaOdKorisnikPoId=new TreeMap<>();
    }

    public void readShapes(InputStream is) throws InvalidDimensionException {
        //1 123456 4.8835
        Scanner scanner=new Scanner(is);
        while (scanner.hasNextLine())
        {
            try {
                String linija=scanner.nextLine();
                if(proveriJaLinijata(linija))
                {
                    throw new InvalidDimensionException("Dimension 0 is not allowed!");
                }
                Forma forma= Klasa.napraviObjektOdForma(linija);
                this.setOdInterfForma.add(forma);
                this.mapaOdKorisnikPoId.putIfAbsent(forma.getIdInterfejs(),
                        new Korisnik(forma.getIdInterfejs(),new TreeSet<>(Comparator.comparing(Forma::getPlostina))));
                this.mapaOdKorisnikPoId.get(forma.getIdInterfejs()).getSetOdFormi().add(forma);

            } catch (InvalidIDException e) {
                System.out.println(e.getMessage());
            }
        }
//        Map<String,Set<Forma>> mapaPom=
//                setOdInterfForma.stream().collect(Collectors.groupingBy(f->f.getIdInterfejs(),
//                        Collectors.toCollection(() -> new TreeSet<Forma>(Comparator.comparing(Forma::getPlostina)))));
//        mapaPom.entrySet().stream().forEach(entry->{
//            mapaOdKorisnikPoId.putIfAbsent(entry.getKey(),new Korisnik(entry.getKey(), new TreeSet<>(Comparator.comparing(Forma::getPlostina))));
//            mapaOdKorisnikPoId.computeIfPresent(entry.getKey(),(k,v)->{
//                v.getSetOdFormi().addAll(entry.getValue());
//                return v;
//            });
//        });
//        BufferedReader br=new BufferedReader(new InputStreamReader(is));
//
//            this.setOdInterfForma =br.lines()
//                    .map(linija-> {
//                                try {
//                                    return Klasa.napraviObjektOdForma(linija);
//                                } catch (InvalidIDException e) {
//                                    System.out.println(e.getMessage());
//                                    return null;
//                                }
//                            }
//                    ).filter(Objects::nonNull)
//                    .collect(Collectors.toCollection(() -> new TreeSet<Forma>(Comparator.comparing(Forma::getPlostina))));




    }

    private boolean proveriJaLinijata(String linija) {
        return Arrays.stream(linija.split("\\s+")).skip(2)
                .anyMatch(part->Double.parseDouble(part)==0);
    }

    void printAllShapes (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        this.setOdInterfForma.forEach(forma -> pw.println(forma));
        pw.flush();
    }

    void scaleShapes (String userID, double coef)
    {
        this.mapaOdKorisnikPoId
                .getOrDefault(userID,new Korisnik(userID,new TreeSet<>(Comparator.comparing(Forma::getPlostina)))).getSetOdFormi().stream()
                .forEach(forma->forma.skaliraj(coef));
    }


    void printByUserId (OutputStream os)
    {
        //Shapes of user: 123456
        //Circle -> Radius: 7.33 Area: 168.58 Perimeter: 46.03
        //Rectangle: -> Sides: 18.63, 13.11 Area: 244.19 Perimeter: 63.47
        //Square: -> Side: 17.52 Area: 306.99 Perimeter: 70.08
        //Shapes of user: nprogm
        //Rectangle: -> Sides: 6.80, 7.63 Area: 51.86 Perimeter: 28.85
        //Shapes of user: finki1
        //Circle -> Radius: 5.06 Area: 80.45 Perimeter: 31.79
        PrintWriter pw=new PrintWriter(os);
        this.mapaOdKorisnikPoId.values().stream()
                .sorted(Comparator.comparing(Korisnik::getBrojNaFormi).reversed()
                .thenComparing(Korisnik::getZbirNaSitePlostini))
                        .forEach(user->pw.println(user));
        pw.flush();
    }

    void statistics (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        DoubleSummaryStatistics dss=this.setOdInterfForma.stream().mapToDouble(forma-> forma.getPlostina()).summaryStatistics();
        //count: 5
        //sum: 852.06
        //min: 51.86
        //average: 170.41
        //max: 306.99
        pw.println(String.format("count: %d",dss.getCount()));
        pw.println(String.format("sum: %.2f",dss.getSum()));
        pw.println(String.format("min: %.2f",dss.getMin()));
        pw.println(String.format("average: %.2f",dss.getAverage()));
        pw.println(String.format("max: %.2f",dss.getMax()));
        pw.flush();
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