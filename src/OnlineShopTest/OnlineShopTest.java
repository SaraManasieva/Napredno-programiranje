package OnlineShopTest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}

class NacinNaDobivanjeNaKomparator
{
    public static Comparator<Product> napraviKomparator(COMPARATOR_TYPE comparatorType)
    {
        switch (comparatorType){
            case NEWEST_FIRST : return Comparator.comparing(Product::getCreatedAt).reversed();
            case OLDEST_FIRST: return Comparator.comparing(Product::getCreatedAt);
            case LOWEST_PRICE_FIRST: return Comparator.comparing(Product::getPrice);
            case HIGHEST_PRICE_FIRST: return Comparator.comparing(Product::getPrice).reversed();
            case MOST_SOLD_FIRST: return Comparator.comparing(Product::getQuantitySold).reversed();
            case LEAST_SOLD_FIRST:return Comparator.comparing(Product::getQuantitySold);
            default: return Comparator.comparing(Product::getQuantitySold);
        }
    }
}


class Product {

    private String category;
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private double price;
    private int quantitySold =0;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
    }

    public double buy(int kolicina)
    {
        this.quantitySold +=kolicina;
        return kolicina*getPrice();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }
}


class OnlineShop {
    private Map<String,Product> mapaNaproduktPoId;
    private Map<String,List<Product>> mapaOdListaOdProizvodiPoKategorija;

    OnlineShop() {
        this.mapaNaproduktPoId=new HashMap<>();
        this.mapaOdListaOdProizvodiPoKategorija=new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        Product p=new Product(category,id,name,createdAt,price);
        this.mapaNaproduktPoId.put(id,p);
        this.mapaOdListaOdProizvodiPoKategorija.putIfAbsent(category,new ArrayList<>());
        //mapaOdListaOdProizvodiPoKategorija.get(category).add(p);
        mapaOdListaOdProizvodiPoKategorija.computeIfPresent(category,(k,v)->{
            v.add(p);
            return v;
        });
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if(!this.mapaNaproduktPoId.containsKey(id))
        {
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!",id));
        }
        this.mapaNaproduktPoId.get(id).setQuantitySold(this.mapaNaproduktPoId.get(id).getQuantitySold()+quantity);
        return this.mapaNaproduktPoId.get(id).getPrice()*quantity;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        Comparator<Product>comparator=NacinNaDobivanjeNaKomparator.napraviKomparator(comparatorType);
        List<Product> listaOdProduktiPomosna=(category==null)?new ArrayList<Product>(this.mapaNaproduktPoId.values()):mapaOdListaOdProizvodiPoKategorija.get(category);
        listaOdProduktiPomosna.sort(comparator);
        //listaOdProduktiPomosna= listaOdProduktiPomosna.stream().sorted(comparator).collect(Collectors.toList());
        List<List<Product>> result = new ArrayList<List<Product>>();
//        if(pageSize>listaOdProduktiPomosna.size())
//        {
//            result.add(listaOdProduktiPomosna);
//        }
//        else
//        {
//            int brojNAListi=(int)Math.ceil(listaOdProduktiPomosna.size()*1.0/pageSize);
//            List<Integer>listaPocetoci=IntStream.range(0,brojNAListi)
//                    .map(i->i*pageSize).boxed().collect(Collectors.toList());
//            //List<Product>temp=new ArrayList<>(listaOdProduktiPomosna);
//            listaPocetoci.forEach(i->result.add(listaOdProduktiPomosna.subList(i,Math.max(i+pageSize,listaOdProduktiPomosna.size()))));
//        }
        IntStream.range(0,(int)Math.ceil(listaOdProduktiPomosna.size()*1.0/pageSize))
                .map(i->{
                    return pageSize*i;
                })
                .forEach(broj->result.add(listaOdProduktiPomosna.subList(broj,Math.min(broj+pageSize,listaOdProduktiPomosna.size()))));


        //result.add(new ArrayList<>());
//        if(pageSize>listaOdProduktiPomosna.size())
//        {
//            result.add(listaOdProduktiPomosna);
//        }
//        else
//        {
//            int brojNaListi=(int)Math.ceil(listaOdProduktiPomosna.size()*1.0/pageSize);
//            IntStream.range(0,brojNaListi)
//                    .forEach(i->{
//                        result.add(listaOdProduktiPomosna.subList(pageSize*i,Math.min(pageSize*i+pageSize,listaOdProduktiPomosna.size())));
//                    });
//        }

        return result;
    }

    public Map<String, Product> getMapaNaproduktPoId() {
        return mapaNaproduktPoId;
    }

    public void setMapaNaproduktPoId(Map<String, Product> mapaNaproduktPoId) {
        this.mapaNaproduktPoId = mapaNaproduktPoId;
    }

    public Map<String, List<Product>> getMapaOdListaOdProizvodiPoKategorija() {
        return mapaOdListaOdProizvodiPoKategorija;
    }

    public void setMapaOdListaOdProizvodiPoKategorija(Map<String, List<Product>> mapaOdListaOdProizvodiPoKategorija) {
        this.mapaOdListaOdProizvodiPoKategorija = mapaOdListaOdProizvodiPoKategorija;
    }

    @Override
    public String toString() {
        return "OnlineShop{" +
                "mapaNaproduktPoId=" + mapaNaproduktPoId +
                ", mapaOdListaOdProizvodiPoKategorija=" + mapaOdListaOdProizvodiPoKategorija +
                '}';
    }
}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

