package SuperStringTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class SuperString {

    private LinkedList<String> listaOdStringovi;

    private List<Integer> pokazuvac;


    public SuperString () {
        listaOdStringovi = new LinkedList<String>();
        pokazuvac = new ArrayList<>();
    }

    public void append(String s) {
        listaOdStringovi.addLast(s);
        pokazuvac.add(1);
    }

    public void insert(String s) {
        listaOdStringovi.addFirst(s);
        pokazuvac.add(-1);
    }

    public String toString() {
        return this.listaOdStringovi.stream().collect(Collectors.joining(""));
    }

    public boolean contains(String s){
        return toString().contains(s);
    }

    public void reverse() {
        Collections.reverse(listaOdStringovi);
        this.listaOdStringovi=listaOdStringovi.stream().map(str->napraviObratenString(str))
                .collect(Collectors.toCollection(LinkedList::new));
        this.pokazuvac=pokazuvac.stream().map(el->el*(-1))
                .collect(Collectors.toList());

    }

    private String napraviObratenString(String str) {
        StringBuilder sb=new StringBuilder();
        return sb.append(str).reverse().toString();
    }

    public void removeLast(int k) {
        IntStream.range(0,k)
                .forEach(i->{
                    if(pokazuvac.remove(this.pokazuvac.size()-1)>0)
                    {
                        listaOdStringovi.removeLast();
                    }
                    else
                    {
                        listaOdStringovi.removeFirst();
                    }
                });
//        for ( int i = 0 ; i < k ; ++i )
//            if ( pokazuvac.removeFirst() < 0 ) listaOdStringovi.removeFirst();
//            else listaOdStringovi.removeLast();
    }

}


public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
