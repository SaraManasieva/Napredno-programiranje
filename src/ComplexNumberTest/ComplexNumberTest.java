package ComplexNumberTest;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;//8:25
class ComplexNumber<T extends Number,U extends Number> implements Comparable<ComplexNumber<?, ?>> {
    private T realenDel;
    private U imaginarenDel;

    public ComplexNumber(T realenDel, U imaginarenDel) {
        this.realenDel = realenDel;
        this.imaginarenDel = imaginarenDel;
    }

    public double modul()
    {
        return Math.sqrt(Math.pow(getReal().doubleValue(),2)+Math.pow(getImaginary().doubleValue(),2));
    }

    public T getReal() {
        return realenDel;
    }

    public void setRealenDel(T realenDel) {
        this.realenDel = realenDel;
    }

    public U getImaginary() {
        return imaginarenDel;
    }

    public void setImaginarenDel(U imaginarenDel) {
        this.imaginarenDel = imaginarenDel;
    }

    @Override
    public String toString() {
        return String.format("%.2f%s%.2fi",getReal().doubleValue(), getImaginary().doubleValue()>=0?"+":"",getImaginary().doubleValue());
    }

    @Override
    public int compareTo(ComplexNumber<?, ?> o) {
        Comparator<ComplexNumber<?,?>> comparator=Comparator.comparing(ComplexNumber::modul);
        return comparator.compare(this,o);
    }


}

public class ComplexNumberTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test simple functions int
            int r = jin.nextInt();int i = jin.nextInt();
            ComplexNumber<Integer, Integer> c = new ComplexNumber<Integer, Integer>(r, i);
            System.out.println(c);
            System.out.println(c.getReal());
            System.out.println(c.getImaginary());
            System.out.println(c.modul());
        }
        if ( k == 1 ) { //test simple functions float
            float r = jin.nextFloat();
            float i = jin.nextFloat();
            ComplexNumber<Float, Float> c = new ComplexNumber<Float, Float>(r, i);
            System.out.println(c);
            System.out.println(c.getReal());
            System.out.println(c.getImaginary());
            System.out.println(c.modul());
        }
        if ( k == 2 ) { //compareTo int
            LinkedList<ComplexNumber<Integer,Integer>> complex = new LinkedList<ComplexNumber<Integer,Integer>>();
            while ( jin.hasNextInt() ) {
                int r = jin.nextInt(); int i = jin.nextInt();
                complex.add(new ComplexNumber<Integer, Integer>(r, i));
            }
            System.out.println(complex);
            Collections.sort(complex);
            System.out.println(complex);
        }
        if ( k == 3 ) { //compareTo double
            LinkedList<ComplexNumber<Double,Double>> complex = new LinkedList<ComplexNumber<Double,Double>>();
            while ( jin.hasNextDouble() ) {
                double r = jin.nextDouble(); double i = jin.nextDouble();
                complex.add(new ComplexNumber<Double, Double>(r, i));
            }
            System.out.println(complex);
            Collections.sort(complex);
            System.out.println(complex);
        }
        if ( k == 4 ) { //compareTo mixed
            LinkedList<ComplexNumber<Double,Integer>> complex = new LinkedList<ComplexNumber<Double,Integer>>();
            while ( jin.hasNextDouble() ) {
                double r = jin.nextDouble(); int i = jin.nextInt();
                complex.add(new ComplexNumber<Double, Integer>(r, i));
            }
            System.out.println(complex);
            Collections.sort(complex);
            System.out.println(complex);
        }
    }
}