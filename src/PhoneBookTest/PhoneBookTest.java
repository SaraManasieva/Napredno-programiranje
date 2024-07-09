package PhoneBookTest;

import java.util.*;
import java.util.stream.Collectors;

class DuplicateNumberException extends Exception
{
    public DuplicateNumberException(String message) {
        super(message);
    }
}

class Contact
{
    private String ime;
    private String broj;

    public Contact(String ime, String broj) {
        this.ime = ime;
        this.broj = broj;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    @Override
    public String toString() {
        return String.format("%s %s",getIme(),getBroj());
    }
}
class PhoneBook
{
    private Set<Contact> setOdKontakti;

    public PhoneBook() {
        this.setOdKontakti = new TreeSet<>(Comparator.comparing(Contact::getIme).thenComparing(Contact::getBroj));
    }

    void addContact(String name, String number) throws DuplicateNumberException {
        if(this.setOdKontakti.stream().filter(k->k.getBroj().equals(number)).count()>0)
        {
            throw new DuplicateNumberException(String.format("Duplicate number: %d",number));
        }
        this.setOdKontakti.add(new Contact(name,number));
    }

    void contactsByNumber(String number)
    {
        List<Contact>kontakti=this.setOdKontakti.stream().filter(k->k.getBroj().contains(number))
                .collect(Collectors.toList());
        if(kontakti.isEmpty())
        {
            System.out.println("NOT FOUND");
        }
        else
        {
            kontakti.forEach(k-> System.out.println(k));
        }
    }

    void contactsByName(String name)
    {
        List<Contact>kontakti=this.setOdKontakti.stream().filter(k->k.getIme().contains(name))
                .collect(Collectors.toList());
        if(kontakti.isEmpty())
        {
            System.out.println("NOT FOUND");
        }
        else
        {
            kontakti.forEach(k-> System.out.println(k));
        }
    }

    public Set<Contact> getSetOdKontakti() {
        return setOdKontakti;
    }

    public void setSetOdKontakti(Set<Contact> setOdKontakti) {
        this.setOdKontakti = setOdKontakti;
    }

    @Override
    public String toString() {
        return "PhoneBook{" +
                "setOdKontakti=" + setOdKontakti +
                '}';
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}


