package PizzaOrderTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

interface Item
{
    int getPrice();
    boolean isPizza();
    String getType();
}

class EmptyOrder extends Exception
{
    public EmptyOrder() {
    }

    public EmptyOrder(String message) {
        super(message);
    }
}
class OrderLockedException extends Exception
{
    public OrderLockedException() {
    }

    public OrderLockedException(String message) {
        super(message);
    }
}

class InvalidExtraTypeException extends Exception
{
    public InvalidExtraTypeException() {
    }

    public InvalidExtraTypeException(String message) {
        super(message);
    }
}

class InvalidPizzaTypeException extends Exception
{
    public InvalidPizzaTypeException() {
    }

    public InvalidPizzaTypeException(String message) {
        super(message);
    }
}

class ItemOutOfStockException extends Exception
{
    public ItemOutOfStockException() {
    }

    public ItemOutOfStockException(Item item) {
        super(String.format(""));
    }
}

enum TipNaExtraItem
{
    Coke(0), Ketchup(1);

    private int value;
    private int cost;

    TipNaExtraItem(int value) {
        switch (value)
        {
            case 0:
                cost=5;
                break;
            case 1:
                cost=3;
                break;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
enum TipNaPizzaItem
{
    Standard(0) , Pepperoni(1) , Vegetarian(2);

    private int value;
    private int cost;

    TipNaPizzaItem(int value) {
        switch (value)
        {
            case 0:
                cost=10;
                break;
            case 1:
                cost=12;
                break;
            case 2:
                cost=8;
                break;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}

class ExtraItem implements Item
{
    private TipNaExtraItem type;
    ExtraItem(String type) throws InvalidExtraTypeException {
        try {
            this.type=TipNaExtraItem.valueOf(type);
        }
        catch (IllegalArgumentException e)
        {
            throw new InvalidExtraTypeException("InvalidExtraTypeException");
        }
    }

    @Override
    public int getPrice() {
        return this.type.getCost();
    }

    @Override
    public boolean isPizza() {
        return false;
    }

    @Override
    public String getType() {
        return type.name();
    }


    public void setType(TipNaExtraItem type) {
        this.type = type;
    }
}
class PizzaItem implements Item
{
    private TipNaPizzaItem type;
    PizzaItem(String type) throws InvalidPizzaTypeException {
        try {
            this.type=TipNaPizzaItem.valueOf(type);
        }
        catch (IllegalArgumentException e)
        {
            throw new InvalidPizzaTypeException("InvalidPizzaTypeException");
        }

    }
    @Override
    public int getPrice() {
        return this.type.getCost();
    }

    @Override
    public boolean isPizza() {
        return true;
    }

    @Override
    public String getType() {
        return type.name();
    }


    public void setType(TipNaPizzaItem type) {
        this.type = type;
    }
}

class Order
{
    private class OrderItem
    {
        private final Item item;
        private int count;

        public OrderItem(Item item, int count) {
            this.item = item;
            this.count = count;
        }

        public Item getItem() {
            return item;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPrice()
        {
            return this.item.getPrice()*count;
        }
    }
    private List<OrderItem> items;
    private boolean locked;

    public Order() {
        this.items=new ArrayList<>();
        this.locked=false;
    }

    void addItem(Item item, int count) throws OrderLockedException, ItemOutOfStockException {
        if(locked)
        {
            throw new OrderLockedException();
        }
        if(count>10)
        {
            throw new ItemOutOfStockException(item);
        }
        Optional<OrderItem>orderItem=this.items.stream().filter(oitem->oitem.getItem().getType().equals(item.getType())).findFirst();
        if(orderItem.isPresent())
        {
            orderItem.ifPresent(oi->oi.setCount(count));
            return;
        }
        items.add(new OrderItem(item,count));
    }

    int getPrice()
    {
        return this.items.stream().mapToInt(oi->oi.getPrice()).sum();
    }

    void removeItem(int idx) throws OrderLockedException {
        if(locked)
        {
            throw new OrderLockedException();
        }
        items.remove(idx);

    }

    void lock() throws EmptyOrder {
        if(this.items.size()==0)
        {
            throw new EmptyOrder();
        }
        locked=true;
    }

    void displayOrder()
    {
        IntStream.range(0,this.items.size())
                .forEach(i->{
                    OrderItem orderItem=items.get(i);
                    System.out.println(String.format("%3d.%-15sx%2d%5d$", i+1,orderItem.getItem().getType(),
                            orderItem.getCount(),orderItem.getPrice()));
                });
        System.out.println(String.format("%-22s%5d$","Total:",getPrice()));
    }


}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}