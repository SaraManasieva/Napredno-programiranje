package TripleTest;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Triple<T extends Number>
{
    private T x;
    private T y;
    private T z;

    public Triple(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    double max()
    {
        List<T> listaZaObrabotka=List.of(x,y,z);
        return listaZaObrabotka.stream().map(t->t.doubleValue()).max(Comparator.naturalOrder()).get();
    }

    double avarage()
    {
        List<T> listaZaObrabotka=List.of(x,y,z);
        return listaZaObrabotka.stream().mapToDouble(t->t.doubleValue()).average().getAsDouble();
    }

    void sort()
    {
        List<T> listaZaObrabotka=List.of(x,y,z);
        listaZaObrabotka=listaZaObrabotka.stream().sorted(Comparator.comparing(T::doubleValue)).collect(Collectors.toList());
        this.x=listaZaObrabotka.get(0);
        this.y=listaZaObrabotka.get(1);
        this.z=listaZaObrabotka.get(2);
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f",x.doubleValue(),y.doubleValue(),z.doubleValue());
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple


