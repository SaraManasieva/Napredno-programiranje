package StaduimTest_zad19;

import java.util.*;
import java.util.stream.IntStream;

class Sector
{
    private String kod;
    private int brNaMesta;
    private int[]nizaOdType;

    public Sector(String kod, int brNaMesta) {
        this.kod = kod;
        this.brNaMesta = brNaMesta;
        // 0 1 2 3
        this.nizaOdType=new int[brNaMesta];
        IntStream.range(0,brNaMesta)
                        .forEach(i->nizaOdType[i]=3);
    }

    public String getKod() {
        return kod;
    }

    public int getBrNaMesta() {
        return brNaMesta;
    }

    public boolean proveriDaliESlobodno(int seat) {
        return this.nizaOdType[seat-1]==3;
    }

    public boolean proveriDaliSeTrpat(int type) {
        if(type==1)
        {
            return !(Arrays.stream(nizaOdType).anyMatch(i->i==2));
        }
        else if(type==2)
        {
            return !(Arrays.stream(nizaOdType).anyMatch(i->i==1));
        }
        else
        {
            return true;
        }
    }

    public void setType(int seat,int type) {
        this.nizaOdType[seat-1]=type;
    }
    public int getBrNaSlobodniMesta()
    {
        return (int) Arrays.stream(nizaOdType).filter(i->i==3).count();
    }
    public int getKupeniBileti()
    {
        return getBrNaMesta()-getBrNaSlobodniMesta();
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%",getKod(),getBrNaSlobodniMesta(),
                getBrNaMesta(),(getKupeniBileti())*100.0/getBrNaMesta());
    }
}
class Stadium
{
    private String ime;
    private Map<String,Sector> mapaOdSectorPoKod;
    Stadium(String name)
    {
        this.ime=name;
        this.mapaOdSectorPoKod=new HashMap<>();
    }
    void createSectors(String[] sectorNames, int[] sizes)
    {
        IntStream.range(0,sectorNames.length)
                .mapToObj(i->new Sector(sectorNames[i],sizes[i]))
                .forEach(sector->mapaOdSectorPoKod.put(sector.getKod(),sector));
    }
    void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if(!mapaOdSectorPoKod.get(sectorName).proveriDaliESlobodno(seat))
        {
            throw new SeatTakenException();
        }
        if(!mapaOdSectorPoKod.get(sectorName).proveriDaliSeTrpat(type))
        {
            throw new SeatNotAllowedException();
        }
        mapaOdSectorPoKod.get(sectorName).setType(seat,type);
    }

    void showSectors()
    {
        this.mapaOdSectorPoKod.values().stream()
                .sorted(Comparator.comparing(Sector::getBrNaSlobodniMesta).reversed()
                        .thenComparing(Sector::getKod))
                .forEach(sector-> System.out.println(sector));
    }
}
class SeatTakenException extends Exception
{

}
class SeatNotAllowedException extends Exception
{

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
