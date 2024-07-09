package IntegerListTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//9:00
class IntegerList
{
    private LinkedList<Integer>listaODInteger;

    public IntegerList() {
        this.listaODInteger=new LinkedList<>();
    }
    IntegerList(Integer... numbers)
    {
        this();
        this.listaODInteger.addAll(Arrays.asList(numbers));
    }
    void add(int el, int idx)
    {
        while (idx>this.listaODInteger.size())
        {
            this.listaODInteger.add(0);

        }
        this.listaODInteger.add(idx,el);
    }
    int remove(int idx)
    {
        return this.listaODInteger.remove(idx);
    }
    void set(int el, int idx)
    {
        this.listaODInteger.set(idx,el);
    }
    int get(int idx)
    {
        return this.listaODInteger.get(idx);
    }
    int size()
    {
        return this.listaODInteger.size();
    }

    int count(int el)
    {
        return (int) this.listaODInteger.stream().filter(br->br==el).count();
    }
    void removeDuplicates()
    {
        Set<Integer> set=new LinkedHashSet<>();
        IntStream.range(0,this.listaODInteger.size())
                .forEach(i->{
                    set.add(this.listaODInteger.get(this.listaODInteger.size()-i-1));
                });
        List<Integer>list=new ArrayList<>(set);
        Collections.reverse(list);
        this.listaODInteger=new LinkedList<>();
        this.listaODInteger.addAll(list);
    }
    int sumFirst(int k)
    {
        return this.listaODInteger.stream().limit(k).mapToInt(i->i).sum();
    }
    int sumLast(int k)
    {
        return this.listaODInteger.stream().skip(this.listaODInteger.size()-k).mapToInt(i->i).sum();
    }

    void shift(int idx, int k)
    {
        int novaPozicija=((idx+k)%this.listaODInteger.size()+listaODInteger.size())%this.listaODInteger.size();
        //idx=7, k=-8, listSize=10;
        //0 1 2 3 4 5 6 7 8 9
        add(remove(idx),novaPozicija);
    }

    void shiftRight(int idx, int k)
    {
        shift(idx,k);
    }
    void shiftLeft(int idx , int k)
    {
        shiftRight(idx,-k);
    }
    IntegerList addValue(int value)
    {
        return new IntegerList(this.listaODInteger.stream().map(el->el+value).toArray(Integer[]::new));
    }

}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}