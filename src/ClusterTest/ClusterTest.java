package ClusterTest;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * January 2016 Exam problem 2
 */

interface Interfejs
{
    long getId();
    double getRastojanie(Interfejs other);
}

class Cluster<T extends Interfejs>
{
    Map<Long,T> mapaOdTPoId;

    public Cluster(Map<Long, T> mapaOdTPoId) {
        this.mapaOdTPoId = mapaOdTPoId;
    }

    public Cluster() {
        this.mapaOdTPoId=new HashMap<>();
    }

    void addItem(T element)
    {
        this.mapaOdTPoId.put(element.getId(),element);
    }

    void near(long id, int top)
    {
        T referentnoT=mapaOdTPoId.get(id);
        AtomicInteger a=new AtomicInteger(1);
        this.mapaOdTPoId.values().stream().sorted(Comparator.comparing(t->t.getRastojanie(referentnoT))).skip(1).limit(top)
                .forEach(t-> System.out.println(String.format("%d. %d -> %.3f",a.getAndIncrement(),t.getId(),t.getRastojanie(referentnoT))));
    }

    public Map<Long, T> getMapaOdTPoId() {
        return mapaOdTPoId;
    }

    public void setMapaOdTPoId(Map<Long, T> mapaOdTPoId) {
        this.mapaOdTPoId = mapaOdTPoId;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "mapaOdTPoId=" + mapaOdTPoId +
                '}';
    }
}

class Point2D implements Interfejs
{
    //id - long
    //x - float
    //y - float
    private long id;
    private float x;
    private float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point2D{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public double getRastojanie(Interfejs other) {
        // $\sqrt{{(x1 - x2)^2} + {(y1 - y2)^2}}$
        Point2D otherPoint=(Point2D) other;
        return Math.sqrt(Math.pow(this.x-otherPoint.x,2)+Math.pow(this.y-otherPoint.y,2));

    }
}

public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<Point2D>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here