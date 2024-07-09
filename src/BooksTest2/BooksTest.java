package BooksTest2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
//1:38 //1:46

class Book
{
    private String naslov;
    private String kategorija;
    private float cena;

    public Book(String naslov, String kategorija, float cena) {
        this.naslov = naslov;
        this.kategorija = kategorija;
        this.cena = cena;
    }

    public String getNaslov() {
        return naslov;
    }

    public String getKategorija() {
        return kategorija;
    }

    public float getCena() {
        return cena;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f",getNaslov(),getKategorija(),getCena());
    }
}

class BookCollection
{
    private List<Book> listaOdKnigi;

    public BookCollection() {
        this.listaOdKnigi=new ArrayList<>();
    }

    public void addBook(Book book)
    {
        this.listaOdKnigi.add(book);
    }

    public void printByCategory(String category)
    {
        this.listaOdKnigi.stream().sorted(Comparator.comparing(Book::getNaslov).thenComparing(Book::getCena).thenComparing(Book::getKategorija))
                .filter(b->b.getKategorija().equalsIgnoreCase(category))
                .forEach(b-> System.out.println(b));
    }

    public List<Book> getCheapestN(int n)
    {
        return this.listaOdKnigi.stream().sorted(Comparator.comparing(Book::getCena).thenComparing(Book::getNaslov))
                .limit(n).collect(Collectors.toList());
    }
}

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде