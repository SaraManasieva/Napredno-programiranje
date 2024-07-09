package TextProcessorTest_zad22;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CosineSimilarityCalculator {
    public static double cosineSimilarity (Collection<Integer> c1, Collection<Integer> c2) {
        int [] array1;
        int [] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1=0, down2=0;

        for (int i=0;i<c1.size();i++) {
            up+=(array1[i] * array2[i]);
        }

        for (int i=0;i<c1.size();i++) {
            down1+=(array1[i]*array1[i]);
        }

        for (int i=0;i<c1.size();i++) {
            down2+=(array2[i]*array2[i]);
        }

        return up/(Math.sqrt(down1)*Math.sqrt(down2));
    }
}

class TextProcessor
{
    private Set<String>setOdSiteZborovi;
    private List<Map<String,Integer>>listaOdMapi;
    private Map<String,Integer>mapaOdBrojPoZbor;
    private List<String>listaOdLinii;

    public TextProcessor() {
        this.listaOdMapi=new ArrayList<>();
        this.setOdSiteZborovi=new TreeSet<>();
        this.mapaOdBrojPoZbor=new TreeMap<>();
        this.listaOdLinii=new ArrayList<>();
    }
    void readText (InputStream is)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        this.listaOdLinii=br.lines().collect(Collectors.toList());
        List<String>listaOdSiteLinii=listaOdLinii.stream().map(linija->linija.replaceAll("[^A-Za-z\\s+]","").toLowerCase())
                .collect(Collectors.toList());
        listaOdSiteLinii.stream().flatMap(linija->Arrays.stream(linija.split("\\s+")))
                .forEach(zbor->this.setOdSiteZborovi.add(zbor));
        listaOdSiteLinii.stream().forEach(linija->{
            String[]zborovi=linija.split("\\s+");
            Map<String,Integer>mapaPom=Arrays.stream(zborovi).collect(Collectors.groupingBy(zbor->zbor,
                    TreeMap::new
                    ,Collectors.summingInt(zbor->1)));
            Arrays.stream(zborovi).forEach(zbor->{
                mapaOdBrojPoZbor.putIfAbsent(zbor,0);
                mapaOdBrojPoZbor.computeIfPresent(zbor,(k,v)->++v);
            });
            listaOdMapi.add(mapaPom);
        });
        setOdSiteZborovi.stream().forEach(zbor->{
            listaOdMapi.forEach(mapa->mapa.putIfAbsent(zbor,0));
        });

    }
    void printTextsVectors (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        this.listaOdMapi.stream().forEach(mapa->pw.println(mapa.values()));
        pw.flush();
    }
    void printCorpus(OutputStream os, int n, boolean ascending)
    {
        PrintWriter pw=new PrintWriter(os);
        Comparator<Map.Entry<String,Integer>>comparator=Map.Entry.comparingByValue();
        this.mapaOdBrojPoZbor.entrySet().stream().sorted(ascending?comparator:comparator.reversed())
                .limit(n).forEach(entry->pw.println(String.format("%s : %d",entry.getKey(),entry.getValue())));
        pw.flush();
    }
    static double najgolemaSlicnost=0;
    static int iMax=0;
    static int jMax=0;

    public void mostSimilarTexts (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        IntStream.range(0,listaOdMapi.size())
                        .forEach(i->{
                            IntStream.range(0,listaOdMapi.size())
                                    .forEach(j->{
                                        if(i!=j)
                                        {
                                            double slicnost=CosineSimilarityCalculator.cosineSimilarity(listaOdMapi.get(i).values(),
                                                    listaOdMapi.get(j).values());
                                            if(slicnost>=najgolemaSlicnost)
                                            {
                                                najgolemaSlicnost=slicnost;
                                                iMax=i;
                                                jMax=j;
                                            }
                                        }
                                    });
                        });
        pw.println(listaOdLinii.get(jMax));
        pw.println(listaOdLinii.get(iMax));
        pw.println(String.format("%12.10f",najgolemaSlicnost));
        pw.flush();
    }
}
public class TextProcessorTest {

    public static void main(String[] args) {
        TextProcessor textProcessor = new TextProcessor();

        textProcessor.readText(System.in);

        System.out.println("===PRINT VECTORS===");
        textProcessor.printTextsVectors(System.out);

        System.out.println("PRINT FIRST 20 WORDS SORTED ASCENDING BY FREQUENCY ");
        textProcessor.printCorpus(System.out,  20, true);

        System.out.println("PRINT FIRST 20 WORDS SORTED DESCENDING BY FREQUENCY");
        textProcessor.printCorpus(System.out, 20, false);

        System.out.println("===MOST SIMILAR TEXTS===");
        textProcessor.mostSimilarTexts(System.out);
    }
}