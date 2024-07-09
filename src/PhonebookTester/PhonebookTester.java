package PhonebookTester;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InvalidNameException extends Exception
{
    public String name;

    public InvalidNameException(String name) {
        this.name=name;
    }
}

class InvalidNumberException extends Exception
{
    public InvalidNumberException() {
    }
}

class MaximumSizeExceddedException extends Exception
{
    public MaximumSizeExceddedException() {
    }
}
class InvalidFormatException extends Exception
{
    public InvalidFormatException() {
    }
}

class Contact
{
    private String ime;
    private List<String> listaOdTelBroevi;

    Contact(String name, String... phonenumber) throws InvalidNameException,InvalidNumberException,MaximumSizeExceddedException
    {
        if(!napraviProverkaZaIme(name))
        {
            throw new InvalidNameException(name);
        }
        this.ime = name;
        this.listaOdTelBroevi=new ArrayList<>();
        for(String s:phonenumber)
        {
            if(!proveriBroj(s))
            {
                throw new InvalidNumberException();
            }
            listaOdTelBroevi.add(s);
        }
//        IntStream.range(0,phonenumber.length)
//                .forEach(i-> {
//                    if(!proveriBroj(phonenumber[i]))
//                    {
//                        throw new InvalidNumberException();
//                    }
//                    listaOdTelBroevi.add(phonenumber[i]);
//
//                });
        if(listaOdTelBroevi.size()>5)
        {
            throw new MaximumSizeExceddedException();
        }
        this.listaOdTelBroevi=listaOdTelBroevi.stream().sorted().collect(Collectors.toList());
    }

    private boolean napraviProverkaZaIme(String ime) {
        return ime.length()>=4&&ime.length()<=10&&proveri(ime);
    }

    private boolean proveri(String ime) {
        return IntStream.range(0,ime.length())
                .map(i->ime.charAt(i))
                .allMatch(c->Character.isLetterOrDigit(c));
    }

    void addNumber(String phonenumber) throws InvalidNumberException {
        //"070", "071", "072", "075","076","077" или "078"
        if(!proveriBroj(phonenumber))
        {
            throw new InvalidNumberException();
        }
        this.listaOdTelBroevi.add(phonenumber);
    }

    private boolean proveriBroj(String phonenumber) {
        return phonenumber.length()==9&&(phonenumber.startsWith("070")||phonenumber.startsWith("071")
        ||phonenumber.startsWith("072")||phonenumber.startsWith("075")
        ||phonenumber.startsWith("076")||phonenumber.startsWith("077")
        ||phonenumber.startsWith("078"))&&daliESostavenSamoOdBrojki(phonenumber);
    }

    private boolean daliESostavenSamoOdBrojki(String phonenumber) {
        return IntStream.range(0,phonenumber.length())
                .map(i->phonenumber.charAt(i))
                .allMatch(c->Character.isDigit(c));
    }

    String getName()
    {
        return ime;
    }
    String[] getNumbers()
    {
        List<String>pom=this.listaOdTelBroevi.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        String[]niza= new String[pom.size()];
        IntStream.range(0,pom.size())
                .forEach(i->niza[i]=pom.get(i));
        return niza;
    }

    @Override
    public String toString() {
        //Gajduk
        //2
        //071123456
        //076123456
        StringBuilder sb=new StringBuilder();
        sb.append(getName()).append("\n");
        List<String>list=this.listaOdTelBroevi.stream().sorted().collect(Collectors.toList());
        sb.append(list.size()).append("\n");
        IntStream.range(0,list.size())
                .forEach(i->sb.append(list.get(i)).append("\n"));
        return sb.toString();
    }

    public static Contact valueOf(String s) throws InvalidFormatException {
        //Gajduk
        //2
        //071123456
        //076123456
        String[]parts=s.split("\\n");
        String ime=parts[0];
        List<String>lista=new ArrayList<>();
        //Arrays.stream(parts).skip(2).forEach(str->lista.add(str));

        String[]niza=lista.stream().toArray(String[]::new);
        try {
            return new Contact(ime,niza);
        }
        catch (InvalidNameException|InvalidNumberException|MaximumSizeExceddedException e)
        {
            throw new InvalidFormatException();
        }
        //return new Contact(ime,lista);
    }

//    public String getIme() {
//        return ime;
//    }

    public List<String> getListaOdTelBroevi() {
        return listaOdTelBroevi;
    }
}

class PhoneBook
{
    private List<Contact>listaOdKontakti;

    public PhoneBook() {
        this.listaOdKontakti=new ArrayList<>();
    }

    public PhoneBook(List<Contact> listaOdKontakti) {
        this.listaOdKontakti = listaOdKontakti;
    }

    void addContact(Contact contact) throws MaximumSizeExceddedException,InvalidNameException
    {
        if(listaOdKontakti.size()==250)
        {
            throw new MaximumSizeExceddedException();
        }
        if(proveriKOntaktIme(contact))
        {
            throw new InvalidNameException(contact.getName());
        }

        this.listaOdKontakti.add(contact);
    }

    private boolean proveriKOntaktIme(Contact contact) {
        return this.listaOdKontakti.stream().anyMatch(c->c.getName().equals(contact.getName()));
    }

    Contact getContactForName(String name)
    {
        List<Contact>pom=this.listaOdKontakti.stream().filter(k->k.getName().equals(name)).collect(Collectors.toList());
        if(pom.isEmpty())
        {
            return null;
        }
        else
        {
            return pom.get(0);
        }
    }
    int numberOfContacts()
    {
        return this.listaOdKontakti.size();
    }
    Contact[] getContacts()
    {
        List<Contact>pom=this.listaOdKontakti.stream().sorted(Comparator.comparing(Contact::getName)).collect(Collectors.toList());
        Contact[]niza= new Contact[pom.size()];
        IntStream.range(0,pom.size())
                .forEach(i->niza[i]=pom.get(i));
        return niza;
    }
    boolean removeContact(String name)
    {
        Optional<Contact> c=this.listaOdKontakti.stream().filter(k->k.getName().equals(name))
                .findFirst();
        if(c.isPresent())
        {
            return this.listaOdKontakti.remove(c.get());

        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString() {
        //Andrej
        //2
        //075123456
        //078123456
        //
        //Gajduk
        //2
        //071123456
        //076123456
        return this.listaOdKontakti.stream().sorted(Comparator.comparing(Contact::getName)).map(k->k.toString()).collect(Collectors.joining("\n"))+"\n";

    }

    public static boolean saveAsTextFile(PhoneBook phonebook,String path)
    {
        PrintWriter pw= null;
        try {
            pw = new PrintWriter(new File(path));
        } catch (IOException e) {
            return false;
        }
        pw.println(phonebook.toString());
        return true;
    }
    public static PhoneBook loadFromTextFile(String path) throws InvalidFormatException, IOException {
        try (BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path)))){
            List<String>pom=br.lines().collect(Collectors.toList());
            pom=pom.stream().map(str->{
                if(str.equals(""))
                {
                    return "*";
                }
                else
                {
                    return str+":";
                }
            }).collect(Collectors.toList());
            String str=pom.stream().collect(Collectors.joining(""));
            String[]strings=str.split("\\*");
            PhoneBook phoneBook=new PhoneBook();
            for(String s:strings)
            {
                phoneBook.addContact(Contact.valueOf(napraviStringBilderOdS(s)));
            }
//            Arrays.stream(strings).forEach(s->{
//                        phoneBook.addContact(Contact.valueOf(napraviStringBilderOdS(s)));
//                    }
//
//            );
            return phoneBook;

        } catch (InvalidNameException|MaximumSizeExceddedException e) {
            throw new InvalidFormatException();
        }


    }



    Contact[]getContactsForNumber(String number_prefix)
    {
        List<Contact> lista=this.listaOdKontakti.stream().filter(k->proveriKontakt(k,number_prefix)).collect(Collectors.toList());
        lista=lista.stream().sorted(Comparator.comparing(Contact::getName)).collect(Collectors.toList());
        Contact[]niza=lista.stream().toArray((int value) -> new Contact[value]);
        return niza;
    }

    private boolean proveriKontakt(Contact k,String prefix) {
        return k.getListaOdTelBroevi().stream().anyMatch(broj->broj.startsWith(prefix));
    }


    private static String napraviStringBilderOdS(String s) {
        String[]parts=s.split(":");
        return Arrays.stream(parts).collect(Collectors.joining("\n"));
    }
}
public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}
