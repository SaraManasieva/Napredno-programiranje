package Anagrams;

import com.sun.source.tree.Tree;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        // Vasiod kod ovde
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        List<String>listaOdZborovi=br.lines().collect(Collectors.toList());
        Map<String,List<String>> mapaOdSetOdAnagramiPoSortiranAnagram=
                listaOdZborovi.stream()
                        .collect(Collectors.groupingBy(zbor->getNapraviSortiranZbor(zbor),
                                LinkedHashMap::new,Collectors.toCollection(ArrayList::new)));
        mapaOdSetOdAnagramiPoSortiranAnagram.values().stream()
                .filter(set->set.size()>=5)
                .forEach(set-> System.out.println(napraviString(set)));
        //System.out.println(mapaOdSetOdAnagramiPoSortiranAnagram);
    }

    public static String napraviString (List<String> list)
    {
        return list.stream().collect(Collectors.joining(" "));
    }
    public static String getNapraviSortiranZbor(String zbor)
    {
        return zbor.chars().mapToObj(asci->(char)asci)
                .sorted().map(c->c.toString())
                .collect(Collectors.joining(""));
    }
}
