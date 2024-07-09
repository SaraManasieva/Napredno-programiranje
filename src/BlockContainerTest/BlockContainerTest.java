package BlockContainerTest;

import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<T extends Comparable<T>>
{
    private List<Set<T>> listaOdSet;
    private int brNaElVoSet;
    public BlockContainer(int n)
    {
        //конструктор со еден аргумент, максималниот број на елементи во блокот
        this.brNaElVoSet=n;
        this.listaOdSet=new ArrayList<>();
    }
    public void add(T a)
    {
        //метод за додавање елемент во последниот блок од контејнерот (ако блокот е полн, се додава нов блок)
        if(listaOdSet.size()==0)//1
        {
            Set<T> set=new TreeSet<>(Comparator.naturalOrder());
            set.add(a);
            listaOdSet.add(set);

        }
        else
        {
            Set<T> setOdT=listaOdSet.get(listaOdSet.size()-1);
            if(setOdT.size()==brNaElVoSet)
            {
                Set<T> set=new TreeSet<>(Comparator.naturalOrder());
                set.add(a);
                listaOdSet.add(set);
            }
            else
            {
                listaOdSet.get(listaOdSet.size()-1).add(a);
            }
        }

    }
    public boolean remove(T a)
    {
        //метод за бришње на елемент од последниот блок (ако се избришат сите елементи од еден блок, тогаш и блокот се брише)
        if(!listaOdSet.isEmpty())
        {
            //list=1234
            //set=1234
            //set=124
            listaOdSet.get(listaOdSet.size()-1).remove(a);
            if(listaOdSet.get(listaOdSet.size()-1).size()==0)
            {
                return listaOdSet.remove(listaOdSet.get(listaOdSet.size()-1));
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }

    }

    public void sort()
    {
        List<T> listaOdSiteT=this.listaOdSet.stream().flatMap(set->set.stream()).sorted().collect(Collectors.toList());
        this.listaOdSet=new ArrayList<>();
        listaOdSiteT.stream().forEach(t->add(t));
    }

    public String toString()
    {
        //[7, 8, 9],[1, 2, 3],[5, 6, 12],[4, 10, 8]
        return this.listaOdSet.stream().map(set->set.toString())
                .collect(Collectors.joining(","));
    }

}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

// Вашиот код овде



