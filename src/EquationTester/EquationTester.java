package EquationTester;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
//8:49
//9:51

class Line {
    Double coeficient;
    Double x;
    Double intercept;

    public Line(Double coeficient, Double x, Double intercept) {
        this.coeficient = coeficient;
        this.x = x;
        this.intercept = intercept;
    }

    public static Line createLine(String line) {
        String[] parts = line.split("\\s+");
        return new Line(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2])
        );
    }

    public double calculateLine() {
        return coeficient * x + intercept;
    }

    @Override
    public String toString() {
        return String.format("%.2f * %.2f + %.2f", coeficient, x, intercept);
    }
}

class Equation<T,K>
{
    Supplier<T> supplier;
    Function<T,K> function;

    public Equation(Supplier<T> supplier, Function<T, K> function) {
        this.supplier = supplier;
        this.function = function;
    }
    //Во класата Equation да се дефинира метод calculate кој не прима агрументи, а враќа објект
    // од генеричката класата Optional со генерички параметар ист како излезниот тип на класата Equation.
    Optional<K> calculate()
    {
        //Методот треба да врати Optional објект пополнет со резултатот добиен од Function
        // имплементацијата применет на аргументот добиен со Supplier имплементацијата.
        return Optional.of(function.apply(supplier.get()));
    }

    public Supplier<T> getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public Function<T, K> getFunction() {
        return function;
    }

    public void setFunction(Function<T, K> function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return "Equation{" +
                "supplier=" + supplier +
                ", function=" + function +
                '}';
    }
}

class EquationProcessor<T,K>
{
    public static <T,K> void process(List<T>listaOdVlezniT,List<Equation<T,K>>listaOdRavenki)
    {
        listaOdVlezniT.forEach(t-> {
            System.out.println(String.format("Input: %s",t));
            listaOdRavenki.forEach(ravenka->{
                Optional<K> optional=ravenka.calculate();

                if(t.equals(listaOdVlezniT.get(listaOdVlezniT.size()-1)))
                {
                    if(optional.isPresent())
                    {
                        System.out.println(String.format("Result: %s",optional.get()));
                    }
                    else
                    {
                        System.out.println(" ");
                    }
                }
            });
        });

    }
}

public class EquationTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { // Testing with Integer, Integer
            List<Equation<Integer, Integer>> equations1 = new ArrayList<>();
            List<Integer> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(Integer.parseInt(sc.nextLine()));
            }

            // TODO: Add an equation where you get the 3rd integer from the inputs list, and the result is the sum of that number and the number 1000.
            equations1.add(new Equation<Integer,Integer>(()->inputs.get(2),x->x+1000));


            // TODO: Add an equation where you get the 4th integer from the inputs list, and the result is the maximum of that number and the number 100.
            equations1.add(new Equation<Integer,Integer>(()->inputs.get(3),x->Math.max(x,100)));


            EquationProcessor.process(inputs, equations1);

        } else { // Testing with Line, Integer
            List<Equation<Line, Double>> equations2 = new ArrayList<>();
            List<Line> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(Line.createLine(sc.nextLine()));
            }

            //TODO Add an equation where you get the 2nd line, and the result is the value of y in the line equation.
            equations2.add(new Equation<Line,Double>(()->inputs.get(1),line->line.calculateLine()));


            //TODO Add an equation where you get the 1st line, and the result is the sum of all y values for all lines that have a greater y value than that equation.

            equations2.add(new Equation<Line,Double>(()->inputs.get(0),
                            linija->inputs.stream().filter(line->line.calculateLine()>linija.calculateLine())
                                    .mapToDouble(l->l.calculateLine())
                                    .sum()

            ));

            EquationProcessor.process(inputs, equations2);
        }
    }
}
