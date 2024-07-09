package ComponentTest;

import java.util.*;
import java.util.stream.IntStream;

class InvalidPositionException extends Exception
{
    public InvalidPositionException(int position) {
        super(String.format("Invalid position %d, alredy taken!",position));
    }
}

class Component
{
    private String boja;
    private int tezina;
    private Set<Component>setODVnatresniKomponenti;
    Component(String color, int weight)
    {
        this.boja=color;
        this.tezina=weight;
        this.setODVnatresniKomponenti=new TreeSet<>(Comparator.comparing(Component::getTezina).thenComparing(Component::getBoja));
    }

    public static void createString(StringBuilder sb, Component component, int level) {
        //WINDOW FIREFOX
        //1:30:RED
        //---40:GREEN
        //------50:BLUE
        //---------60:CYAN
        //------50:RED
        //---90:MAGENTA
        //2:80:YELLOW
        //---35:WHITE
        IntStream.range(0,level)
                .forEach(i->sb.append("---"));
        sb.append(String.format("%d:%s\n",component.tezina,component.boja));
        component.setODVnatresniKomponenti.stream()
                .forEach(c->createString(sb,c,level+1));
    }

    void addComponent(Component component)
    {
        this.setODVnatresniKomponenti.add(component);
    }

    public String getBoja() {
        return boja;
    }

    public void setBoja(String boja) {
        this.boja = boja;
    }

    public int getTezina() {
        return tezina;
    }

    public void setTezina(int tezina) {
        this.tezina = tezina;
    }

    public Set<Component> getSetODVnatresniKomponenti() {
        return setODVnatresniKomponenti;
    }

    public void setSetODVnatresniKomponenti(Set<Component> setODVnatresniKomponenti) {
        this.setODVnatresniKomponenti = setODVnatresniKomponenti;
    }

    public void changeColor(int weight, String color) {
        if(this.tezina<weight)
        {
            this.boja=color;
        }
        this.setODVnatresniKomponenti.stream().forEach(c->change(c,weight,color));
    }

    private void change(Component c, int weight, String color) {
        if(c.tezina<weight)
        {
            c.boja=color;
        }
        c.setODVnatresniKomponenti.stream().forEach(com->change(com,weight,color));
    }

    //    @Override
//    public int compareTo(Component o) {
//        Comparator<Component> comparator=Comparator.comparing(Component::getTezina, ).thenComparing(Component::getBoja);
//        return comparator.compare(this,o);
//    }
}

class Window
{
    private String ime;
    private Map<Integer,Component>mapaOdKomponentaPoPozicija;

    Window(String ime)
    {
        this.ime=ime;
        this.mapaOdKomponentaPoPozicija=new TreeMap<>();
    }

    void addComponent(int position, Component component) throws InvalidPositionException {
        if(mapaOdKomponentaPoPozicija.containsKey(position))
        {
            throw new InvalidPositionException(position);
        }
        mapaOdKomponentaPoPozicija.put(position,component);
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public Map<Integer, Component> getMapaOdKomponentaPoPozicija() {
        return mapaOdKomponentaPoPozicija;
    }

    public void setMapaOdKomponentaPoPozicija(Map<Integer, Component> mapaOdKomponentaPoPozicija) {
        this.mapaOdKomponentaPoPozicija = mapaOdKomponentaPoPozicija;
    }

    void changeColor(int weight, String color)
    {
        mapaOdKomponentaPoPozicija.values().forEach(c->c.changeColor(weight,color));
    }

    void swichComponents(int pos1, int pos2)
    {
        Component c1=mapaOdKomponentaPoPozicija.get(pos1);
        Component c2=mapaOdKomponentaPoPozicija.get(pos2);
        mapaOdKomponentaPoPozicija.put(pos1,c2);
        mapaOdKomponentaPoPozicija.put(pos2,c1);
    }

    @Override
    public String toString() {
        //WINDOW FIREFOX
        //1:30:RED
        //---40:GREEN
        //------50:BLUE
        //---------60:CYAN
        //------50:RED
        //---90:MAGENTA
        //2:80:YELLOW
        //---35:WHITE
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("WINDOW %s\n",getIme()));
        mapaOdKomponentaPoPozicija.entrySet().forEach(entry->{
            sb.append(String.format("%d:",entry.getKey()));
            Component.createString(sb,entry.getValue(),0);
        });
        return sb.toString();
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде