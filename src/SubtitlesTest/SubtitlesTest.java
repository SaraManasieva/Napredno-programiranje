package SubtitlesTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Prevod
{
    private int redenBroj;
    private int pocetnoVreme;
    private int krajnoVreme;
    private String pocKrajVremeVoString;
    private String tekst;

    public Prevod(int redenBroj, int pocetnoVreme, int krajnoVreme, String tekst) {
        this.redenBroj = redenBroj;
        this.pocetnoVreme = pocetnoVreme;
        this.krajnoVreme = krajnoVreme;
        this.tekst = tekst;
    }
    //1*00:00:43,700 --> 00:00:47,973*Come on ladies, we're pushing pennies*around like a lot of old 'tards here.*


    public Prevod(String delOdLinija) {
        String[]parts=delOdLinija.split("&");
        this.redenBroj=Integer.parseInt(parts[0]);
        //00:00:43,700 --> 00:00:47,973
        this.pocKrajVremeVoString=parts[1];
        String[]zaVreme=parts[1].split(" --> ");
        String pocetok=zaVreme[0];//00:00:43,700
        String kraj=zaVreme[1];//00:00:47,973
        this.pocetnoVreme=pretvoriStrinVoInt(pocetok);
        this.krajnoVreme=pretvoriStrinVoInt(kraj);
        this.tekst=IntStream.range(2,parts.length)
                .mapToObj(broj->parts[broj])
                .collect(Collectors.joining("\n"));
    }

    private int pretvoriStrinVoInt(String string) {
        //00:00:43,700
        String[]parts=string.split(":");
        return Integer.parseInt(parts[0])*60*60*1000+Integer.parseInt(parts[1])*60*1000+
                Integer.parseInt(parts[2].split(",")[0])*1000+Integer.parseInt(parts[2].split(",")[1]);

    }


    public int getRedenBroj() {
        return redenBroj;
    }

    public void setRedenBroj(int redenBroj) {
        this.redenBroj = redenBroj;
    }

    public int getPocetnoVreme() {
        return pocetnoVreme;
    }

    public void setPocetnoVreme(int pocetnoVreme) {
        this.pocetnoVreme = pocetnoVreme;
    }

    public String getPocKrajVremeVoString() {
        return pocKrajVremeVoString;
    }

    public void setPocKrajVremeVoString(String pocKrajVremeVoString) {
        this.pocKrajVremeVoString = pocKrajVremeVoString;
    }

    public int getKrajnoVreme() {
        return krajnoVreme;
    }

    public void setKrajnoVreme(int krajnoVreme) {
        this.krajnoVreme = krajnoVreme;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    @Override
    public String toString() {
        return String.format("%d %s %d \n%s",getRedenBroj(),getPocKrajVremeVoString(),getTekst());
    }

    public String getNapraviString(int ms) {
        //00:00:43,922 --> 00:00:48,195
        int casoviPoc=(getPocetnoVreme()+ms)/(1000*60*60);
        int ostatokPoc=(getPocetnoVreme()+ms)%(1000*60*60);
        int min=ostatokPoc/(1000*60);
        int o2=ostatokPoc%(1000*60);
        int sec=o2/1000;
        int mili=o2%1000;
        ///
        int casoviPoc2=(getKrajnoVreme()+ms)/(1000*60*60);
        int ostatokPoc2=(getKrajnoVreme()+ms)%(1000*60*60);
        int min2=ostatokPoc2/(1000*60);
        int o22=ostatokPoc2%(1000*60);
        int sec2=o22/1000;
        int mili2=o22%1000;
        return String.format("%02d:%02d:%02d,%03d --> %02d:%02d:%02d,%03d",
                casoviPoc,min,sec,mili,casoviPoc2,min2,sec2,mili2);
    }
}
class Subtitles
{
    private List<Prevod> listaOdprevodi;

    public Subtitles() {
        this.listaOdprevodi=new ArrayList<>();
    }

    int loadSubtitles(InputStream inputStream)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        //17
        //00:01:53,468 --> 00:01:54,745
        //All right.
        //
        //18
        //00:01:55,540 --> 00:01:57,305
        //Johnny's got the head.
       String siteStringovi=br.lines().map(linija->{
           if(linija.isEmpty())
           {
               return ";";
           }
           else
           {
               return linija+"&";
           }
       }).collect(Collectors.joining());

        this.listaOdprevodi=Arrays.stream(siteStringovi.split(";")).map(p->new Prevod(p)).collect(Collectors.toList());

        return getListaOdprevodi().size();
    }

    void print()
    {
        this.getListaOdprevodi().forEach(p-> System.out.println(String.format("%d\n%s\n%s\n",p.getRedenBroj(),p.getPocKrajVremeVoString(),p.getTekst())));
    }

    void shift(int ms)
    {
        this.getListaOdprevodi().forEach(p-> p.setPocKrajVremeVoString(p.getNapraviString(ms)));
    }


    public List<Prevod> getListaOdprevodi() {
        return listaOdprevodi;
    }

    public void setListaOdprevodi(List<Prevod> listaOdprevodi) {
        this.listaOdprevodi = listaOdprevodi;
    }

    @Override
    public String toString() {
        return "Subtitles{" +
                "listaOdprevodi=" + listaOdprevodi +
                '}';
    }
}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде
