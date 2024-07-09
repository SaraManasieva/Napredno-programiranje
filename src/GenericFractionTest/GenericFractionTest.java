package GenericFractionTest;

import java.util.Scanner;//10:07

class ZeroDenominatorException extends Exception
{
    public ZeroDenominatorException(String message) {
        super(message);
    }
}

class GenericFraction<T extends Number,U extends Number>
{
    private T broitel;
    private U imenitel;

    public GenericFraction(T broitel, U imenitel) throws ZeroDenominatorException {
        this.broitel = broitel;
        if(imenitel.doubleValue()==0)
        {
           throw new ZeroDenominatorException("Denominator cannot be zero");
        }
        else
        {
            this.imenitel = imenitel;
        }
    }

    GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        return new GenericFraction<Double, Double>(getBroitel().doubleValue()*gf.getImenitel().doubleValue()
        +gf.getBroitel().doubleValue()*getImenitel().doubleValue(),getImenitel().doubleValue()*gf.getImenitel().doubleValue());
        //a/b + c/d= ad + cb/bd
    }

    double toDouble()
    {
        return getBroitel().doubleValue()/getImenitel().doubleValue();
    }

    public T getBroitel() {
        return broitel;
    }

    public void setBroitel(T broitel) {
        this.broitel = broitel;
    }

    public U getImenitel() {
        return imenitel;
    }

    public void setImenitel(U imenitel) {
        this.imenitel = imenitel;
    }

    public  static double presmetajNZD(double a,double b)
    {
        if(b==0)
        {
            return a;
        }
        if(a<b)
        {
            return presmetajNZD(a,b-a);
        }
        else
        {
            return presmetajNZD(b,a-b);
        }
    }

    @Override
    public String toString() {
        return String.format("%.2f / %.2f",getBroitel().doubleValue()/nzd(),getImenitel().doubleValue()/nzd());
    }

    private double nzd() {
        return presmetajNZD(getBroitel().doubleValue(),getImenitel().doubleValue());
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}