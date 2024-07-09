package Shapes2Test4;

//8:01

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class IrregularCanvasException extends Exception
{
    public IrregularCanvasException(String message) {
        super(message);
    }
}
class Prozorec
{
    private String id;
    private List<Forma> listaOdFormi;

    public Prozorec(String linija) throws IrregularCanvasException {
        //f37ff1d9 S 17 S 14 S 21 C 14 C 15 S 17 C 23 S 21 S 28 C 27 S 10 S 28 C 28 S 10
        this.listaOdFormi=new ArrayList<>();
        String[]parts=linija.split("\\s+");
        this.id=parts[0];
        Arrays.stream(parts).skip(1)
                .forEach(str->{
                    if(str.equals("C"))
                    {
                        this.listaOdFormi.add(new Krug());
                    } else if (str.equals("S")) {
                        this.listaOdFormi.add(new Kvadrat());
                    }
                    else
                    {
                        this.listaOdFormi.get(listaOdFormi.size()-1).setDolzina(Integer.parseInt(str));
                    }
                });
        if(najgolemaPlostinaNaForma()>ShapesApplication.maxArea)
        {
            throw new IrregularCanvasException(String.format("Canvas %s has a shape with area larger than %.2f",
                    id,ShapesApplication.maxArea));
        }

    }

    private double najgolemaPlostinaNaForma() {
        return this.listaOdFormi.stream().mapToDouble(f->f.getPlostina()).max().getAsDouble();
    }
    public double getSumaNaPlostiniNaSiteFormi()
    {
        return this.listaOdFormi.stream().mapToDouble(f->f.getPlostina())
                .sum();
    }

    public int getBrojNaKvadrati()
    {
        return (int) listaOdFormi.stream().filter(f->f.getType().equals(Type.KVADRAT))
                .count();
    }
    public int getBrojNaKrugovi()
    {
        return (int) listaOdFormi.stream().filter(f->f.getType().equals(Type.KRUG))
                .count();
    }

    @Override
    public String toString() {
        //5e28f402 11 5 6 100.00 2642.08 1007.35
        DoubleSummaryStatistics dss=this.listaOdFormi.stream().mapToDouble(f->f.getPlostina()).summaryStatistics();
        //ID total_shapes total_circles total_squares min_area max_area average_area.
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                id,listaOdFormi.size(),getBrojNaKrugovi(),getBrojNaKvadrati(),
                dss.getMin(),dss.getMax(),dss.getAverage()
        );
    }
}

enum Type
{
    KRUG,KVADRAT;
}
abstract class Forma
{
    abstract void setDolzina(int dolzina);
    abstract double getPlostina();
    abstract Type getType();
}
class Krug extends Forma
{
    private int radius;
    public Krug() {
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    void setDolzina(int dolzina) {
        this.radius=dolzina;
    }

    @Override
    double getPlostina() {
        return Math.PI*Math.pow(radius,2);
    }

    @Override
    Type getType() {
        return Type.KRUG;
    }
}
class Kvadrat extends Forma
{
    private int strana;
    public Kvadrat() {
    }

    public int getStrana() {
        return strana;
    }

    public void setStrana(int strana) {
        this.strana = strana;
    }

    @Override
    void setDolzina(int dolzina) {
        this.strana=dolzina;
    }

    @Override
    double getPlostina() {
        return Math.pow(strana,2);
    }

    @Override
    Type getType() {
        return Type.KVADRAT;
    }
}

class ShapesApplication
{
    private List<Prozorec>listaOdProzorci;
    public static double maxArea;
    ShapesApplication(double maxArea)
    {
        this.listaOdProzorci=new ArrayList<>();
        this.maxArea=maxArea;
    }

    void readCanvases (InputStream inputStream)
    {
        //f37ff1d9 S 17 S 14 S 21 C 14 C 15 S 17 C 23 S 21 S 28 C 27 S 10 S 28 C 28 S 10
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.listaOdProzorci=br.lines().map(linija-> {
                    try {
                        return new Prozorec(linija);
                    } catch (IrregularCanvasException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    void printCanvases (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        this.listaOdProzorci.stream().sorted(Comparator.comparing(Prozorec::getSumaNaPlostiniNaSiteFormi).reversed())
                        .forEach(p->pw.println(p));
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