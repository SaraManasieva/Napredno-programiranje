package Shapes1Test3;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//9:42
class Prozorec
{
//canvas_id size_1 size_2 size_3 …. size_n
    private String id;
    private List<Integer>listaOdStraniNaKvadr;

    public Prozorec(String linija) {
        String[]parts=linija.split("\\s+");
        this.id=parts[0];
        this.listaOdStraniNaKvadr= Arrays.stream(parts)
                .skip(1).map(str->Integer.parseInt(str))
                .collect(Collectors.toList());
    }

    public int getPerimetarNaSiteKvadrati()
    {
        return this.listaOdStraniNaKvadr.stream().mapToInt(strana->strana*4)
                .sum();
    }

    public List<Integer> getListaOdStraniNaKvadr() {
        return listaOdStraniNaKvadr;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",id,listaOdStraniNaKvadr.size(),
                getPerimetarNaSiteKvadrati());
    }
}

class ShapesApplication
{
    private List<Prozorec> listaOdProzorci;

    public ShapesApplication() {
        this.listaOdProzorci=new ArrayList<>();
    }

    int readCanvases (InputStream inputStream)
    {
        //canvas_id size_1 size_2 size_3 …. size_n
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.listaOdProzorci=br.lines().map(linija->new Prozorec(linija))
                .collect(Collectors.toList());
        return this.listaOdProzorci.stream().mapToInt(p->p.getListaOdStraniNaKvadr().size())
                .sum();
    }

    void printLargestCanvasTo (OutputStream outputStream)
    {
        PrintWriter pw=new PrintWriter(outputStream);
        pw.println(this.listaOdProzorci.stream().max(Comparator.comparing(Prozorec::getPerimetarNaSiteKvadrati))
                .orElse(null));
        pw.flush();
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