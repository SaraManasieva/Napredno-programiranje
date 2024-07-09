package ArchiveStoreTest30;

import java.time.LocalDate;
import java.util.*;
import java.util.Date;
import java.util.Scanner;

class NonExistingItemException extends Exception
{
    public NonExistingItemException(String message) {
        super(message);
    }
}
abstract class Archive
{
    protected int id;
    protected LocalDate dateArchived;
    abstract void arhiviraj(LocalDate d);
    abstract String open(LocalDate date);


}
class LockedArchive extends Archive
{
    private LocalDate dateToOpen;
    LockedArchive(int id, LocalDate dateToOpen)
    {
        this.id=id;
        this.dateToOpen=dateToOpen;
    }

    @Override
    void arhiviraj(LocalDate d) {
        this.dateArchived=d;
    }

    @Override
    String open(LocalDate date) {
        if(date.isBefore(dateToOpen))
        {
            return String.format("Item %d cannot be opened before %s",id,dateToOpen);
        }
        else
        {
            return String.format("Item %d opened at %s",id,date);
        }
    }


}
class SpecialArchive extends Archive
{
    private  int maxOpen;
    private int brojac;
    SpecialArchive(int id, int maxOpen)
    {
        this.id=id;
        this.maxOpen=maxOpen;
        this.brojac=0;
    }

    @Override
    void arhiviraj(LocalDate d) {
        this.dateArchived=d;
    }

    @Override
    String open(LocalDate date) {
        brojac++;
        if(brojac>maxOpen)
        {
            return String.format("Item %d cannot be opened more than %d times",id,maxOpen);
        }
        else
        {
            return String.format("Item %d opened at %s",id,date);
        }
    }


}
class ArchiveStore
{
    private List<Archive>listaOdArhivi;
    private StringBuilder sb;

    public ArchiveStore() {
        this.listaOdArhivi=new ArrayList<>();
        this.sb=new StringBuilder();
    }
    void archiveItem(Archive item, LocalDate date)
    {
        item.arhiviraj(date);
        this.listaOdArhivi.add(item);
        sb.append(String.format("Item %d archived at %s",item.id,date)).append("\n");
    }
    void openItem(int id, LocalDate date) throws NonExistingItemException {
        if(!this.listaOdArhivi.stream().anyMatch(a->a.id==id))
        {
            throw new NonExistingItemException(String.format("Item with id %d doesn't exist",id));
        }
        Archive archive=this.listaOdArhivi.stream().filter(a->a.id==id).findFirst().get();
        sb.append(archive.open(date)).append("\n");
    }

    String getLog()
    {
        return sb.toString();
    }
}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}
// вашиот код овде


