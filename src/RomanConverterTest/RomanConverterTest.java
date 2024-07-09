package RomanConverterTest;//9:02

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        // your solution here
        List<Integer> listOdBrojki=List.of(1000,900,500,400,100,90,50,40,10,9,5,4,1);
        List<String> rimskiZnaci=List.of("M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I");
        StringBuilder sb=new StringBuilder();
        AtomicInteger atomicInteger=new AtomicInteger(0);
        while (n>0)
        {
            //6673
            int dolzina=n/listOdBrojki.get(atomicInteger.get());
            //System.out.println(dolzina);
            IntStream.range(0,dolzina).forEach(brojka->sb.append(rimskiZnaci.get(atomicInteger.get())));
            n=n-(dolzina*listOdBrojki.get(atomicInteger.get()));
           atomicInteger.getAndIncrement();
        }
        return sb.toString();
    }

}
