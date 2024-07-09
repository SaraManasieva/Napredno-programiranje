package WordVectorsTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Word vectors test 08:07?
 */
class KlasaZaVektor
{
    private List<Integer> vektor;
    final static KlasaZaVektor vektorOdnapredDefiniranAkoNema=new KlasaZaVektor(List.of(5,5,5,5,5));
    final static KlasaZaVektor pocetenVekto=new KlasaZaVektor(List.of(0,0,0,0,0));

    public KlasaZaVektor(List<Integer> vektor) {
        this.vektor = vektor;
    }

    public KlasaZaVektor soberiDvaVektora(KlasaZaVektor other)
    {
        return new KlasaZaVektor(IntStream.range(0,this.vektor.size()).
                map(brojce->this.vektor.get(brojce)+other.vektor.get(brojce)).boxed().collect(Collectors.toList()));
    }

    public List<Integer> getVektor() {
        return vektor;
    }

    public void setVektor(List<Integer> vektor) {
        this.vektor = vektor;
    }

    @Override
    public String toString() {
        return "KlasaZaVektor{" +
                "vektor=" + vektor +
                '}';
    }

    public Integer presmetajMax() {
        return this.vektor.stream().max(Comparator.naturalOrder()).orElse(0);
    }
}
class WordVectors
{
    private Map<String,KlasaZaVektor> mapaOdListaPoZbor;
    private List<KlasaZaVektor> listaOdVektori;


    public WordVectors(String[] words, List<List<Integer>> vectors)
    {
        //конструктор за иницијализација со зборови и нивната соодветна репрезентација во вектор од 5 цели броеви
        // (со вредност од 0-9). За секој стринг од низата words соодветствува една листа од 5 цели броеви
        // (негова векторска репрезентација).
        //sara -> List{1,3,4,5,4}
        //meri -> {1,7,5,4,3}
        this.mapaOdListaPoZbor=new HashMap<>();
        IntStream.range(0,words.length)
                .forEach(brojce->{
                    mapaOdListaPoZbor.put(words[brojce],new KlasaZaVektor(vectors.get(brojce)));
                });
        this.mapaOdListaPoZbor=mapaOdListaPoZbor;

    }

    public void readWords(List<String> words)
    {
        // се вчитува листа од зборови од некој текст за кој треба да се пресмета векторска репрезентација.
        this.listaOdVektori=IntStream.range(0,words.size())
                .mapToObj(i->mapaOdListaPoZbor.getOrDefault(words.get(i),KlasaZaVektor.vektorOdnapredDefiniranAkoNema))
                .collect(Collectors.toList());
    }



    public List<Integer> slidingWindow(int n)
    {
        //10 5
        //listSize n
        //10      5   ->6
        //1 2 3 4 5 6 7 8 9 10
        return IntStream.range(0,this.listaOdVektori.size()-n+1)
                .mapToObj(i->listaOdVektori.subList(i,n+i).stream().reduce(KlasaZaVektor.pocetenVekto,(v1,v2)->v1.soberiDvaVektora(v2)))
                .map(vektor->vektor.presmetajMax())
                .collect(Collectors.toList());
        //
    }
}
public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}



