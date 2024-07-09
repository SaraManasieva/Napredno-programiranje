package TermFrequencyTest;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class TermFrequency
{
    private Map<String,Long> mapa;
    private Set<String> zboroviStoSeIgnoriraat;
    private List<String> siteZborovi;

    TermFrequency(InputStream inputStream, String[] stopWords)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.zboroviStoSeIgnoriraat=Arrays.stream(stopWords).collect(Collectors.toSet());
        this.siteZborovi=br.lines().flatMap(linija->zboroviVoEdnaLinija(linija).stream()).filter(zbor->!zboroviStoSeIgnoriraat.contains(zbor))
                .filter(zbor->!zbor.isEmpty()).collect(Collectors.toList());
        //System.out.println(siteZborovi);
        this.mapa=siteZborovi.stream().filter(zbor->!zboroviStoSeIgnoriraat.contains(zbor))
                .filter(zbor->!zbor.isEmpty()).collect(Collectors.groupingBy(zbor->zbor,Collectors.counting() ));
    }

    private List<String> zboroviVoEdnaLinija(String linija) {
        linija=linija.toLowerCase().replaceAll("[.,]","").trim();
        String[]parts=linija.split("\\s+");
        return Arrays.stream(parts).collect(Collectors.toList());
    }

    public int countTotal() {
        return this.siteZborovi.size();
    }


    public int countDistinct() {
        return this.mapa.keySet().size();
    }


    public List<String> mostOften(int k) {
        // враќа листа која ги содржи k-те зборови кои најчесто се појавуваат во текстот подредени според бројот на
        // појавување од најмногу до најмалку. Во случај на ист број на појавувања се подредуваат алфабетски.
        List<String> list=new ArrayList<>(mapa.keySet());
        list.sort(Comparator.naturalOrder());
        return list.stream().sorted(Comparator.comparing(zbor->mapa.get(zbor)).reversed()).limit(k).collect(Collectors.toList());
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in, stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
