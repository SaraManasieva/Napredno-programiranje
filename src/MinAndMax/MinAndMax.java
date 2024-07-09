package MinAndMax;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class MinMax<T extends Comparable<T>>
{
    private T min;
    private T max;
    private List<T> listaOdT;

    public MinMax(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public MinMax() {
        listaOdT=new ArrayList<>();
    }
    //void update(T element)
    public void update(T element) {
        listaOdT.add(element);
        this.max=max();
        this.min=min();
    }


    public T min() {
        return listaOdT.stream().sorted().min(Comparator.naturalOrder()).get();
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T max() {
        return listaOdT.stream().max(Comparator.naturalOrder()).get();
    }

    public void setMax(T max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n",min,max,(int)listaOdT.stream().filter(t->!(t.equals(min)||t.equals(max))).count());
    }


}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}