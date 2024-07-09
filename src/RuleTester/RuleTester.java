package RuleTester;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Student {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public static Student create(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        return new Student(id, grades);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }
}

class Rule<T,K>{
    Predicate<T> predikat;
    Function<T,K> funkcija;

    public Rule(Predicate<T> predikat, Function<T, K> funkcija) {
        this.predikat = predikat;
        this.funkcija = funkcija;
    }

    public Optional<K> apply(T vlez)
    {
        if(predikat.test(vlez))
        {
            return Optional.of(funkcija.apply(vlez));
        }
        else
        {
            return Optional.empty();
        }
        //return Optional.of(funkcija.apply(predikat.test(vlez)));
        //return Optional.of(funkcija.apply(predikat.test(vlez)));
    }

}

class RuleProcessor
{

    public static <T,K> void process(List<T> listaOdVlez,List<Rule<T,K>> listaPravila)
    {
        listaOdVlez.forEach(vlez->{
            System.out.println(String.format("Input: %s",vlez));
            listaPravila.forEach(pravilo->{
                if(!pravilo.apply(vlez).isEmpty())
                {
                    System.out.println(String.format("Result: %s",pravilo.apply(vlez).get()));
                }
                else
                {
                    System.out.println("Condition not met");
                }
            });
        });
    }


}

public class RuleTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Test for String,Integer
            List<Rule<String, Integer>> rules = new ArrayList<Rule<String, Integer>>();

            /*
            TODO: Add a rule where if the string contains the string "NP", the result would be index of the first occurrence of the string "NP"
            * */
            rules.add(new Rule<String, Integer>(str->str.contains("NP"),str->str.indexOf("NP")));


            /*
            TODO: Add a rule where if the string starts with the string "NP", the result would be length of the string
            * */
            rules.add(new Rule<String, Integer>(str->str.startsWith("NP"),str->str.length()));


            List<String> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(sc.nextLine());
            }

            RuleProcessor.process(inputs, rules);


        } else { //Test for Student, Double
            List<Rule<Student, Double>> rules = new ArrayList<Rule<Student, Double>>();

            //TODO Add a rule where if the student has at least 3 grades, the result would be the max grade of the student
            rules.add(new Rule<Student, Double>(student -> student.grades.size()>=3,student ->student.grades.stream().mapToDouble(oc->oc).max().orElse(5.0)));


            //TODO Add a rule where if the student has an ID that starts with 21, the result would be the average grade of the student
            //If the student doesn't have any grades, the average is 5.0
            rules.add(new Rule<Student, Double>(student -> student.id.startsWith("20"),student -> student.grades.stream().mapToDouble(oc->oc).average().orElseGet(()->5.0)));


            List<Student> students = new ArrayList<Student>();
            while (sc.hasNext()){
                students.add(Student.create(sc.nextLine()));
            }

            RuleProcessor.process(students, rules);
        }
    }
}
