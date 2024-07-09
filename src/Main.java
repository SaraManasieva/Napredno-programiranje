

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {
    public static void main(String[] args) {
     List<Integer>lista=new ArrayList<>(List.of(1,2,5,6,0));
        System.out.println(lista);
        lista.removeIf(i->i<1);
        System.out.println(lista);

    }

}