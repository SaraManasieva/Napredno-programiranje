package ResizableArrayTest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
class ResizableArray<T>
{
    private T[]niza;
    private int size;

    public ResizableArray() {
        this.niza=(T[])new Object[1];
        this.size=0;
    }

    void  addElement(T element)
    {
        if(size==niza.length)
        {
            //niza= Arrays.copyOf(niza,niza.length+1);
            niza= Arrays.copyOf(niza,size<<1);
        }
        niza[size++]=element;
    }
    int findEl(T element)
    {
//        for(int i=0;i<size;i++)
//        {
//            if(element.equals(niza[i]))
//            {
//                return i;
//            }
//        }
//        return -1;
        return IntStream.range(0,size)
                .filter(i->element.equals(niza[i]))
                .findFirst().orElse(-1);
//        OptionalInt optionalInt=IntStream.range(0,niza.length)
//                .filter(i->element.equals(niza[i]))
//                .findFirst();
//        if(optionalInt.isPresent())
//        {
//            return optionalInt.getAsInt();
//        }
//        else
//        {
//            return -1;
//        }
    }

    boolean removeElement(T element)
    {
        int idx=findEl(element);
        if(idx==-1)
        {
            return false;
        }
        niza[idx]=niza[--size];
        if(size<<2 <= niza.length)
        {
            niza=Arrays.copyOf(niza,size<<1>0?size<<1:1);
        }
        return true;

//        List<T>pom=Arrays.stream(niza).collect(Collectors.toList());
//        boolean flag=pom.remove(element);
//        if (flag==true)
//        {
//            size--;
//            this.niza=pom.toArray((T[])new Object[size]);
//        }
//        return flag;
    }
    boolean contains(T element)
    {
        return findEl(element)!=-1;
       //return Arrays.stream(niza).anyMatch(el->el.equals(element));
    }
    Object[]toArray()
    {
        return Arrays.copyOf(niza,size);
        //return Arrays.stream(niza).toArray(Object[]::new);
    }
    boolean isEmpty()
    {
        return size==0;
    }
    int count()
    {
        return size;
    }
    T elementAt(int idx)
    {
//        if(idx>size||idx<0)
//        {
//            throw new ArrayIndexOutOfBoundsException();
//        }
        return niza[idx];
    }

    static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src)
    {
        IntStream.range(0, src.count())
                        .forEach(i->dest.addElement(src.elementAt(i)));
       //Arrays.stream(src.toArray()).forEach(el->dest.addElement((T) el));
    }


}

@SuppressWarnings("unchecked")
class IntegerArray extends ResizableArray<Integer>
{
    double sum()
    {
        return IntStream.range(0,count())
                .map(i->elementAt(i))
                .sum();
    }
    double mean()
    {
        return IntStream.range(0,count())
                .map(i->elementAt(i))
                .average().orElse(0);
    }

    int countNonZero()
    {
        return (int) IntStream.range(0,count())
                .map(i->elementAt(i))
                .filter(el->el!=0)
                .count();
    }

    IntegerArray distinct()
    {
        IntegerArray integerArray=new IntegerArray();
        Set<Integer>set=new HashSet<>();
        IntStream.range(0,count())
                .mapToObj(i->elementAt(i))
                .forEach(el->set.add(el));
        set.stream().forEach(in->integerArray.addElement(in));
        return integerArray;
    }
    IntegerArray increment(int offset)
    {
        IntegerArray integerArray=new IntegerArray();
        IntStream.range(0,count())
                .map(i->elementAt(i)+offset)
                .forEach(el->integerArray.addElement(el));
        return integerArray;
    }

}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
