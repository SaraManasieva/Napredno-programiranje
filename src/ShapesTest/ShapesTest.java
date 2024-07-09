package ShapesTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface Scalable
{
    void scale(float scaleFactor);
}

interface Stackable
{
    float weight();
}

class Krug extends Forma
{
    private float radius;

    public Krug(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius=this.radius*scaleFactor;
    }

    @Override
    public float weight() {
        return (float) Math.PI*radius*radius;
    }

    @Override
    public String toString() {
        //C: [id:5 места од лево] [color:10 места од десно] [weight:10.2 места од десно]
        return String.format("C: %-5s%-10s%10.2f",getId(),getColor(),weight());
    }

    @Override
    public int compareTo(Forma o) {
        Comparator<Forma> comparator=Comparator.comparing(Forma::weight).reversed().thenComparing(Forma::getId);
        return comparator.compare(this,o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Krug krug = (Krug) o;
        return Float.compare(krug.radius, radius) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), radius);
    }
}

class Pravoagolnik extends Forma
{
    private float width;
    private float height;

    public Pravoagolnik(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        this.height=this.height*scaleFactor;
        this.width=this.width*scaleFactor;
    }

    @Override
    public float weight() {
        return width*height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f",getId(),getColor(),weight());
    }

    @Override
    public int compareTo(Forma o) {
        Comparator<Forma> comparator=Comparator.comparing(Forma::weight).reversed().thenComparing(Forma::getId);
        return comparator.compare(this,o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pravoagolnik that = (Pravoagolnik) o;
        return Float.compare(that.width, width) == 0 && Float.compare(that.height, height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), width, height);
    }
}
abstract class Forma implements Scalable,Stackable,Comparable<Forma>
{
    private String id;
    private Color color;

    public Forma(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Forma{" +
                "id='" + id + '\'' +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Forma forma = (Forma) o;
        return id.equals(forma.id) && color == forma.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color);
    }
}
class Canvas
{
    private Set<Forma> formi;

    public Canvas(Set<Forma> formi) {
        this.formi = formi;
    }

    public Canvas() {
        this.formi=new TreeSet<Forma>();
    }

    void add(String id, Color color, float radius)
    {
        this.formi.add(new Krug(id,color,radius));
        //this.formi=this.formi.stream().sorted(Comparator.comparing(Forma::weight).reversed()).collect(Collectors.toList());
    }

    void add(String id, Color color, float width, float height)
    {
        this.formi.add(new Pravoagolnik(id,color,width,height));
        //this.formi=this.formi.stream().sorted(Comparator.comparing(Forma::weight).reversed()).collect(Collectors.toList());
    }

    void scale(String id, float scaleFactor)
    {
        Forma f=this.formi.stream().filter(forma->forma.getId().equals(id)).findFirst().get();
        this.formi.remove(f);
        f.scale(scaleFactor);
        this.formi.add(f);
        //this.formi=this.formi.stream().sorted(Comparator.comparing(Forma::weight).reversed()).collect(Collectors.toList());
    }

    public Set<Forma> getFormi() {
        return formi;
    }

    public void setFormi(Set<Forma> formi) {
        this.formi = formi;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        this.formi.forEach(f->sb.append(f).append("\n"));
        return sb.toString();
    }
}

enum Color
{
    RED,GREEN,BLUE
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}