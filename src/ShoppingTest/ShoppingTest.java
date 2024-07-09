package ShoppingTest;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InvalidOperationException extends Exception
{
    public InvalidOperationException(String message) {
        super(message);
    }
}
class Produkt
{
    private String productID;
    private String productName;
    private double productPrice;

    public Produkt(String productID, String productName, double productPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return "Produkt{" +
                "productID='" + productID + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }
}
abstract class Stavka
{
    protected Produkt produkt;

    public Stavka(Produkt produkt) {
        this.produkt = produkt;
    }

    abstract double getVkupnaCenaNaStavka();
}

class ProduktVoCelost extends Stavka
{
    private int quantity;

    public ProduktVoCelost(Produkt produkt,int quantity) throws InvalidOperationException {
        super(produkt);
        if(quantity==0)
        {
            throw new InvalidOperationException(String.format("The quantity of the product with id %s can not be 0.",produkt.getProductID()));
        }
        this.quantity=quantity;

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f",produkt.getProductID(),getVkupnaCenaNaStavka());
    }

    @Override
    double getVkupnaCenaNaStavka() {
        return getQuantity()*produkt.getProductPrice();
    }
}
class ProduktNaGram extends Stavka
{
    private double quantity;

    public ProduktNaGram(Produkt produkt,double quantity) throws InvalidOperationException {
        super(produkt);
        if(quantity==0)
        {
            throw new InvalidOperationException(String.format("The quantity of the product with id %s can not be 0.",produkt.getProductID()));
        }
        this.quantity=quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f",produkt.getProductID(),getVkupnaCenaNaStavka());
    }

    @Override
    double getVkupnaCenaNaStavka() {
        return getQuantity()/1000*produkt.getProductPrice();
    }
}

class ShoppingCart
{
    private List<Stavka> stavki;

    public ShoppingCart() {
        this.stavki=new ArrayList<>();
    }

    void addItem(String itemData) throws InvalidOperationException {
        //WS;productID;productName;productPrice;quantity (quantity е цел број, productPrice се однесува на цена на 1 продукт)
        //PS;productID;productName;productPrice;quantity (quantity е децимален број - во грамови, productPrice се однесува на цена на 1 кг продукт)
        String[]parts=itemData.split(";");
        if(parts[0].equals("WS"))
        {
            //WS;101569;Coca Cola;970;64
            stavki.add(new ProduktVoCelost(new Produkt(parts[1],parts[2],Double.parseDouble(parts[3])),Integer.parseInt(parts[4])));
        }
        else
        {
            stavki.add(new ProduktNaGram(new Produkt(parts[1],parts[2],Double.parseDouble(parts[3])),Double.parseDouble(parts[4])));
        }
    }


    public void printShoppingCart(OutputStream os) {
        PrintWriter pw=new PrintWriter(os);
        this.stavki.stream().sorted(Comparator.comparing(Stavka::getVkupnaCenaNaStavka).reversed())
                .forEach(stavka-> pw.println(stavka));
        pw.flush();
    }

    void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException{
        PrintWriter pw=new PrintWriter(os);
        List<String> list= discountItems.stream().map(i->i.toString()).collect(Collectors.toList());
        if(list.size()==0)
        {
            throw new InvalidOperationException("There are no products with discount.");
        }
        this.stavki.stream().filter(stavka->list.contains(stavka.produkt.getProductID()))
                .forEach(stavka -> {
                    double staraCenaNaStavka=stavka.getVkupnaCenaNaStavka();
                    stavka.produkt.setProductPrice( (0.9*stavka.produkt.getProductPrice()));
                    pw.println(String.format("%s - %.2f",stavka.produkt.getProductID(),staraCenaNaStavka-stavka.getVkupnaCenaNaStavka()));
                });

        pw.flush();
    }
}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}