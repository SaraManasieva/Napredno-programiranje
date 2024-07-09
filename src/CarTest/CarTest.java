package CarTest;

import java.util.*;
import java.util.stream.Collectors;

class Car
{
    // Car(String manufacturer, String model, int price, float power).
    private String proizvoditel;
    private String model;
    private int cena;
    private float moknost;

    public Car(String proizvoditel, String model, int cena, float moknost) {
        this.proizvoditel = proizvoditel;
        this.model = model;
        this.cena = cena;
        this.moknost = moknost;
    }

    public String getProizvoditel() {
        return proizvoditel;
    }

    public void setProizvoditel(String proizvoditel) {
        this.proizvoditel = proizvoditel;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public float getMoknost() {
        return moknost;
    }

    public void setMoknost(float moknost) {
        this.moknost = moknost;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%.0fKW) %d",getProizvoditel(),getModel(),getMoknost(),getCena());
    }
}
class CarCollection
{
    private List<Car>listaODCar;

    public CarCollection() {
        this.listaODCar=new ArrayList<>();
    }

    public void addCar(Car car)
    {
        this.listaODCar.add(car);
    }

    public void sortByPrice(boolean ascending)
    {
        if(ascending==true)
        {
            this.listaODCar=listaODCar.stream().sorted(Comparator.comparing(Car::getCena).thenComparing(Car::getMoknost)).collect(Collectors.toList());
        }
        else
        {
            this.listaODCar=listaODCar.stream().sorted(Comparator.comparing(Car::getCena).thenComparing(Car::getMoknost).reversed()).collect(Collectors.toList());
        }
    }

    public List<Car> filterByManufacturer(String manufacturer)
    {
        return this.listaODCar.stream().filter(car->car.getProizvoditel().equalsIgnoreCase(manufacturer)).sorted(Comparator.comparing(Car::getModel)).collect(Collectors.toList());
    }

    public List<Car> getList()
    {
        return getListaODCar();
    }

    public List<Car> getListaODCar() {
        return listaODCar;
    }

    public void setListaODCar(List<Car> listaODCar) {
        this.listaODCar = listaODCar;
    }

    @Override
    public String toString() {
        return "CarCollection{" +
                "listaODCar=" + listaODCar +
                '}';
    }
}

public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if(parts.length < 4) return parts[0];
            Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]));
            cc.addCar(car);
        }
        scanner.close();
        return "";
    }
}


// vashiot kod ovde