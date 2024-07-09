package ShapesTest_zad18;

import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

enum Color {
    RED, GREEN, BLUE
}
interface Scalable
{
    void scale(float scaleFactor);
}

interface Stackable
{
    float weight();
}

abstract class Forma implements Scalable,Stackable
{
    String id;
    Color color;

    public Forma(String id, Color color) {
        this.id = id;
        this.color = color;
    }
}

class Krug extends Forma
{
    float radius;

    public Krug(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius*=scaleFactor;
    }

    @Override
    public float weight() {
        return (float)(Math.PI*Math.pow(radius,2));
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f",
                id,color.toString(),weight());
    }
}

class Pravoagolnik extends Forma
{

    float width;
    float height;

    public Pravoagolnik(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        this.width*=scaleFactor;
        this.height*=scaleFactor;
    }

    @Override
    public float weight() {
        return width*height;
    }
    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f",
                id,color.toString(),weight());
    }
}
class Canvas
{
    private Set<Forma> setOdFormi;

    public Canvas() {
        this.setOdFormi=new TreeSet<>(Comparator.comparing(Forma::weight).reversed());
    }

    void add(String id, Color color, float radius)
    {
        this.setOdFormi.add(new Krug(id,color,radius));
    }
    void add(String id, Color color, float width, float height)
    {
        this.setOdFormi.add(new Pravoagolnik(id,color,width,height));
    }
    void scale(String i, float scaleFactor)
    {
        this.setOdFormi.stream().filter(f->f.id.equals(i))
                .forEach(f->f.scale(scaleFactor));
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        this.setOdFormi.stream()
                .sorted(Comparator.comparing(Forma::weight).reversed())
                .forEach(f->sb.append(f).append("\n"));
        return sb.toString();
    }
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