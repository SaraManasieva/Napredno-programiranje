package NamesTest;

import java.util.*;
import java.util.stream.Collectors;

//10:27
class Names
{
    private Map<String,Integer> mapaNaBrojNaPojavuvanjaPoIme;

    public Names() {
        this.mapaNaBrojNaPojavuvanjaPoIme = new TreeMap<>(Comparator.naturalOrder());
    }

    public void addName(String name) {
        this.mapaNaBrojNaPojavuvanjaPoIme.putIfAbsent(name,0);
        this.mapaNaBrojNaPojavuvanjaPoIme.computeIfPresent(name,(k,v)->++v);

    }

    public void printN(int n) {
        this.mapaNaBrojNaPojavuvanjaPoIme.keySet()
                .stream().filter(ime->brojNaPojavuvanja(ime)>=n)
                .forEach(ime-> System.out.println(String.format("%s (%d) %d",ime,mapaNaBrojNaPojavuvanjaPoIme.get(ime),brojNaUnikatniVukviVoIme(ime))));

    }

    private int brojNaUnikatniVukviVoIme(String ime) {
        return(int) ime.toLowerCase().chars().distinct().count();
    }

    private int brojNaPojavuvanja(String ime) {
        return this.mapaNaBrojNaPojavuvanjaPoIme.get(ime);
    }

    public String findName(int len, int x) {
        List<String> listaOdIminja=mapaNaBrojNaPojavuvanjaPoIme.keySet().stream().filter(ime->ime.length()<len).collect(Collectors.toList());
        return listaOdIminja.get(x%listaOdIminja.size());
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde