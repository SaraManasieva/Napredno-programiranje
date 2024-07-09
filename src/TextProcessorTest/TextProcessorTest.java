package TextProcessorTest;

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
    private Map<String,Integer> mapaZaBrojNaPojavuvanjaNaZbor;
    private List<Map<String,Integer>> listaOdMapi;
    private List<String> listaOdTekstoviVoEdnaLinija;
    public TextProcessor() {
        this.mapaZaBrojNaPojavuvanjaNaZbor =new TreeMap<>();
        this.listaOdMapi=new ArrayList<>();
        this.listaOdTekstoviVoEdnaLinija=new ArrayList<>();
    }
    void readText (InputStream is)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        this.listaOdTekstoviVoEdnaLinija=br.lines().collect(Collectors.toList());
        this.listaOdTekstoviVoEdnaLinija.stream().forEach(linija->{
            Map<String,Integer> mapaPomosna=new TreeMap<>();
            linija=linija.replaceAll("[^A-Za-z\\s+]","");
            linija=linija.toLowerCase();
            String[]parts=linija.split("\\s+");
            IntStream.range(0,parts.length)
                    .forEach(i->{
                        mapaPomosna.putIfAbsent(parts[i],0);
                        mapaPomosna.computeIfPresent(parts[i],(k,v)->{
                            v=v+1;
                            return v;
                        });
                        this.mapaZaBrojNaPojavuvanjaNaZbor.putIfAbsent(parts[i],0);
                        this.mapaZaBrojNaPojavuvanjaNaZbor.computeIfPresent(parts[i],(k,v)->{
                            v=v+1;
                            return v;
                        });
                    });
            this.listaOdMapi.add(mapaPomosna);
        });
        this.mapaZaBrojNaPojavuvanjaNaZbor.keySet().forEach(zbor->{
            this.listaOdMapi.forEach(mapa->{
                mapa.putIfAbsent(zbor,0);
            });
        });

    }

    void printTextsVectors (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        this.listaOdMapi.stream().forEach(mapa-> System.out.println(mapa.values()));
        pw.flush();
    }

    void printCorpus(OutputStream os, int n, boolean ascending)
    {
        PrintWriter pw=new PrintWriter(os);
        Comparator<Map.Entry<String,Integer>> comparator=Comparator.comparing(Map.Entry::getValue);
        this.mapaZaBrojNaPojavuvanjaNaZbor.entrySet().stream().sorted(ascending ? comparator :comparator.reversed())
                .limit(n).forEach(entry-> pw.println(String.format("%s : %d",entry.getKey(),entry.getValue())));
        pw.flush();
    }

    public void mostSimilarTexts (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        double najgolemaSlicnost=0;
        int iMax=0;
        int jMax=0;
        for(int i=0;i<listaOdMapi.size();i++)
        {
            for(int j=0;j<listaOdMapi.size();j++)
            {
                if(i!=j)
                {
                    double slicnost=CosineSimilarityCalculator.cosineSimilarity(listaOdMapi.get(i).values(),listaOdMapi.get(j).values());
                    if(slicnost>najgolemaSlicnost)
                    {
                        najgolemaSlicnost=slicnost;
                        iMax=i;
                        jMax=j;
                    }
                }
            }
        }
        //Napredno programiranje..
        //Napredno programiranje
        //1.0000000000
        pw.println(this.listaOdTekstoviVoEdnaLinija.get(iMax));
        pw.println(this.listaOdTekstoviVoEdnaLinija.get(jMax));
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