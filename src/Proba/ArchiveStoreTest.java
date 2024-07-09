package Proba;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

class NonExistingItemException extends Exception
{
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist",id));
    }
}
class NepravilnoOtvoranje extends Exception
{
    public NepravilnoOtvoranje(String message) {
        super(message);
    }
}
abstract class Archive
{
    //id - цел број
    //dateArchived - датум на архивирање.
    protected int id;
    protected LocalDate dateArchived;

    public Archive(int id, LocalDate dateArchived) {
        this.id = id;
        this.dateArchived = dateArchived;
    }

    abstract String proveriDaliSeOtvara(int id,LocalDate dataNaOtvaranje) ;

    public Archive(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateArchived() {
        return dateArchived;
    }

    @Override
    public String toString() {
        return "Archive{" +
                "id=" + id +
                ", dateArchived=" + dateArchived +
                '}';
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }
}

class LockedArchive extends Archive
{
    private LocalDate dateToOpen;


    //LockedArchive(int id, LocalDate dateToOpen)

    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen=dateToOpen;
    }

    @Override
    String proveriDaliSeOtvara(int id, LocalDate dataNaOtvaranje) {
        if(dataNaOtvaranje.isBefore(this.dateToOpen))
        {
            return String.format("Item %d cannot be opened before %s\n",id,dateToOpen);
        }
        return String.format("Item %d opened at %s\n",id,dataNaOtvaranje);
    }


}
class SpecialArchive extends Archive
{
    private int maxOpen;
    private int counter;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen=maxOpen;
        this.counter=0;
    }

    @Override
    String proveriDaliSeOtvara(int id, LocalDate dataNaOtvaranje)  {
        if(counter>=maxOpen)
        {
            return String.format("Item %d cannot be opened more than %d times\n",id,maxOpen);
        }
        counter++;
        return String.format("Item %d opened at %s\n",id,dataNaOtvaranje);
    }


    //SpecialArchive(int id, int maxOpen)
}

class ArchiveStore
{
    private List<Archive> listaOdArhivi;
    StringBuilder sb;

    public ArchiveStore() {
        this.listaOdArhivi=new ArrayList<>();
        this.sb=new StringBuilder();
    }
    void archiveItem(Archive item, LocalDate date)
    {
        //За секоја акција на архивирање во текст треба да се додаде следната порака Item [id] archived at [date],
        item.setDateArchived(date);
        listaOdArhivi.add(item);
        sb.append(String.format("Item %d archived at %s\n",item.getId(),item.getDateArchived()));
    }
    void openItem(int id, LocalDate date) throws NonExistingItemException {
        //метод за отварање елемент од архивата со зададен id и одреден датум date.
        // Ако не постои елемент со даденото id треба да се фрли исклучок од тип NonExistingItemException
        // со порака Item with id [id] doesn't exist.
        //
        //При отварање ако се работи за LockedArhive и датумот на отварање е пред датумот кога може да се отвори,
        // да се додаде порака Item [id] cannot be opened before [date].
        //
        //Ако се работи за SpecialArhive и се обидиеме да ја отвориме повеќе пати од дозволениот број (maxOpen) да
        // се додаде порака Item [id] cannot be opened more than [maxOpen] times.
        Optional<Archive> archive=listaOdArhivi.stream().filter(a->a.getId()==id).findFirst();
        if(archive.isPresent())
        {
            sb.append(archive.get().proveriDaliSeOtvara(id,date));
        }
        else
        {
            throw new NonExistingItemException(id);
        }



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