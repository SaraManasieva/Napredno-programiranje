package StaduimTest;

import java.util.*;
import java.util.stream.IntStream;

class SeatNotAllowedException extends Exception
{

}
class SeatTakenException extends Exception
{

}

class Sector
{
    //кодот на секторот String
    //бројот на места за седење int
    //информации за зафатеност на местата за седење ?
    //Stadium во која се чуваат информации за:
    //името на стадионот String
    //и сите сектори во стадионот ?
    private Map<Integer,Integer> mapaNaTipNaSedistePoBrojNaSediste;
    private String kodNaSektor;
    private int brojNaMestaZaSedenje;
    private Set<Integer> setOdTipoviNaSedista;

    public Sector(String kodNaSektor, int brojNaMestaZaSedenje) {
        this.kodNaSektor = kodNaSektor;
        this.brojNaMestaZaSedenje = brojNaMestaZaSedenje;
        this.mapaNaTipNaSedistePoBrojNaSediste =new HashMap<>();
        this.setOdTipoviNaSedista =new TreeSet<>();
    }

    public String getKodNaSektor() {
        return kodNaSektor;
    }

    public void setKodNaSektor(String kodNaSektor) {
        this.kodNaSektor = kodNaSektor;
    }

    public int getBrojNaMestaZaSedenje() {
        return brojNaMestaZaSedenje;
    }

    public void setBrojNaMestaZaSedenje(int brojNaMestaZaSedenje) {
        this.brojNaMestaZaSedenje = brojNaMestaZaSedenje;
    }
    //информации за зафатеност на местата за седење ?
    public float getInformacijaZaMestataZaSedenjeNaSektor()
    {
        return (1.0f*this.mapaNaTipNaSedistePoBrojNaSediste.keySet().size()/getBrojNaMestaZaSedenje())*100;
    }
    public int getBrojNaSlobodniMesta()
    {
        return getBrojNaMestaZaSedenje()-getMapaNaTipNaSedistePoBrojNaSediste().size();
    }


    public Map<Integer, Integer> getMapaNaTipNaSedistePoBrojNaSediste() {
        return mapaNaTipNaSedistePoBrojNaSediste;
    }

    public void setMapaNaTipNaSedistePoBrojNaSediste(Map<Integer, Integer> mapaNaTipNaSedistePoBrojNaSediste) {
        this.mapaNaTipNaSedistePoBrojNaSediste = mapaNaTipNaSedistePoBrojNaSediste;
    }

    public Set<Integer> getSetOdTipoviNaSedista() {
        return setOdTipoviNaSedista;
    }

    public void setSetOdTipoviNaSedista(Set<Integer> setOdTipoviNaSedista) {
        this.setOdTipoviNaSedista = setOdTipoviNaSedista;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%",getKodNaSektor(),getBrojNaSlobodniMesta(),getBrojNaMestaZaSedenje(),getInformacijaZaMestataZaSedenjeNaSektor());
    }
}
class Stadium
{
    //името на стадионот String
    //и сите сектори во стадионот ?
    private String imeNaStadion;
    private Map<String,Sector> mapaOdSektoriPoimeNaSektor;

    public Stadium(String name) {
        this.imeNaStadion=name;
        this.mapaOdSektoriPoimeNaSektor=new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sectorSizes) {
        //void createSectors(String[] sectorNames, int[] sizes)
        //креирање на сектори со имиња String[] sectorNames и број на места int[] sizes (двете низи се со иста големина)
        IntStream.range(0,sectorSizes.length)
                .forEach(brojka->{
                    this.mapaOdSektoriPoimeNaSektor.put(sectorNames[brojka],new Sector(sectorNames[brojka],sectorSizes[brojka]));
                });
    }

    //void buyTicket(String sectorName, int seat, int type)
    public void buyTicket(String sectorName, int seat, int type) throws SeatNotAllowedException, SeatTakenException {
        Sector s=this.mapaOdSektoriPoimeNaSektor.get(sectorName);
        if(s.getMapaNaTipNaSedistePoBrojNaSediste().keySet().contains(seat))
        {
            throw new SeatTakenException();
        }
        if(type==1)
        {
            if(s.getSetOdTipoviNaSedista().contains(2))
            {
                throw new SeatNotAllowedException();
            }
        }
        if(type==2)
        {
            if(s.getSetOdTipoviNaSedista().contains(1))
            {
                throw new SeatNotAllowedException();
            }
        }
        s.getSetOdTipoviNaSedista().add(type);
        s.getMapaNaTipNaSedistePoBrojNaSediste().put(seat,type);
    }

    public void showSectors() {
        //ги печати сите сектори сортирани според бројот на слободни места во опаѓачки редослед
        // (ако повеќе сектори имаат ист број на слободни места, се подредуваат според името).
        this.mapaOdSektoriPoimeNaSektor.values().stream()
                .sorted(Comparator.comparing(Sector::getBrojNaSlobodniMesta).reversed().thenComparing(Sector::getKodNaSektor))
                .forEach(sector -> System.out.println(sector));
    }

    public String getImeNaStadion() {
        return imeNaStadion;
    }

    public void setImeNaStadion(String imeNaStadion) {
        this.imeNaStadion = imeNaStadion;
    }

    public Map<String, Sector> getMapaOdSektoriPoimeNaSektor() {
        return mapaOdSektoriPoimeNaSektor;
    }

    public void setMapaOdSektoriPoimeNaSektor(Map<String, Sector> mapaOdSektoriPoimeNaSektor) {
        this.mapaOdSektoriPoimeNaSektor = mapaOdSektoriPoimeNaSektor;
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
