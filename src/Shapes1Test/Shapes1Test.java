package Shapes1Test;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Prozorec
{
    // canvas_id size_1 size_2 size_3 …. size_n,
    private String id;
    private List<Integer> straniNaKvadrati;

    public Prozorec(String id, List<Integer> straniNaKvadrati) {
        this.id = id;
        this.straniNaKvadrati = straniNaKvadrati;
    }

    public static Prozorec napraviProzorec(String linija)
    {
        String[]parts=linija.split("\\s+");
        String kod=parts[0];
        List<Integer> lista= Arrays.stream(parts).skip(1)
                .map(string->Integer.parseInt(string)).collect(Collectors.toList());
        return new Prozorec(kod,lista);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getStraniNaKvadrati() {
        return straniNaKvadrati;
    }

    public void setStraniNaKvadrati(List<Integer> straniNaKvadrati) {
        this.straniNaKvadrati = straniNaKvadrati;
    }

    public static int presmetajPerimatar(int strana)
    {
        return strana*4;
    }

    public int getPresmetajZbirNaPerimetriNaSiteKvadrati()
    {
        return getStraniNaKvadrati().stream().mapToInt(str->presmetajPerimatar(str))
                .sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",getId(),
                getStraniNaKvadrati().size(),
                getPresmetajZbirNaPerimetriNaSiteKvadrati());
    }
}
class ShapesApplication
{
    private List<Prozorec> prozorci;

    public ShapesApplication(List<Prozorec> prozorci) {
        this.prozorci = prozorci;
    }

    public ShapesApplication() {
        this.prozorci=new ArrayList<>();
    }

    public int readCanvases(InputStream inputStream) {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        this.prozorci= br.lines().map(linija->Prozorec.napraviProzorec(linija)).collect(Collectors.toList());
//                .mapToInt(prozorec -> prozorec.getStraniNaKvadrati().size())
//                .sum();
        return this.prozorci.stream().mapToInt(p->p.getStraniNaKvadrati().size())
                .sum();
    }

    public void printLargestCanvasTo(OutputStream outputStream) {
        // метод којшто на излезен поток ќе го испечати прозорецот чии квадрати имаат најголем периметар.
        // Печатењето да се изврши во форматот canvas_id squares_count total_squares_perimeter.
        PrintWriter printWriter=new PrintWriter(outputStream);
        //this.prozorci.stream().forEach(p-> System.out.println(p));


//        if(this.prozorci.stream().max(Comparator.comparing(Prozorec::getPresmetajZbirNaPerimetriNaSiteKvadrati))
//                        .isPresent())
//        {
//            printWriter.println(this.prozorci.stream().max(Comparator.comparing(Prozorec::getPresmetajZbirNaPerimetriNaSiteKvadrati))
//                    .get());
//        }
        printWriter.println(this.prozorci.stream().max(Comparator.comparing(Prozorec::getPresmetajZbirNaPerimetriNaSiteKvadrati))
                .orElseThrow());
        printWriter.flush();
    }

    public List<Prozorec> getProzorci() {
        return prozorci;
    }

    public void setProzorci(List<Prozorec> prozorci) {
        this.prozorci = prozorci;
    }

    @Override
    public String toString() {
        return "ShapesApplication{" +
                "prozorci=" + prozorci +
                '}';
    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}