package PayrollSystemTest2_zad16;

import java.util.*;
import java.util.stream.Collectors;

abstract class Employee
{
    protected String id;
    protected String level;

    public Employee(String id, String level) {
        this.id = id;
        this.level = level;
    }
    abstract double getPlata();

    public String getLevel() {
        return level;
    }
    public abstract void setBonus(double bonus);
    public abstract double getBonus();

    public abstract double getPlataZaPrekuvremenaRabota();
    public abstract int getPoeniNaTiketi();
}
class HourlyEmployee extends Employee
{
    private double hours;
    private Map<String,Double> hourlyRateByLevel;
    private  double bonus;

    public HourlyEmployee(String id, String level,double hours, Map<String,Double> hourlyRateByLevel) {
        super(id, level);
        this.hours=hours;
        this.hourlyRateByLevel=hourlyRateByLevel;
    }

    public double getBrojNaPrekuvremeniCasovi()
    {
        return Math.max(0,hours-40);
    }

    public double getBrojNaRegularniCasovi()
    {
        return hours-getBrojNaPrekuvremeniCasovi();
    }
    @Override
    double getPlata() {
        return getBrojNaPrekuvremeniCasovi()*hourlyRateByLevel.get(level)*1.5+getBrojNaRegularniCasovi()*hourlyRateByLevel.get(level)+bonus;
    }

    @Override
    public void setBonus(double bonus) {
        this.bonus=bonus;
    }

    @Override
    public double getBonus() {
        return bonus;
    }

    @Override
    public double getPlataZaPrekuvremenaRabota() {
        return 1.5*getBrojNaPrekuvremeniCasovi()*hourlyRateByLevel.get(level);
    }

    @Override
    public int getPoeniNaTiketi() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f %s",
                id,level,getPlata(),getBrojNaRegularniCasovi(),getBrojNaPrekuvremeniCasovi(),
                bonus!=0?String.format("Bonus: %.2f",bonus):"");
    }
}
class FreelanceEmployee extends Employee
{
    private List<Integer>poeniNaTiketi;
    private Map<String,Double> ticketRateByLevel;
    private double bonus;

    public FreelanceEmployee(String id, String level, List<Integer> poeniNaTiketi, Map<String,Double> ticketRateByLevel) {
        super(id, level);
        this.poeniNaTiketi = poeniNaTiketi;
        this.ticketRateByLevel=ticketRateByLevel;
    }

    public int getSumaNaPoeniNaTiketi()
    {
        return this.poeniNaTiketi.stream()
                .mapToInt(p->p).sum();
    }
    @Override
    double getPlata() {
        return getSumaNaPoeniNaTiketi()*ticketRateByLevel.get(level)+bonus;
    }

    @Override
    public void setBonus(double bonus) {
        this.bonus=bonus;
    }

    @Override
    public double getBonus() {
        return bonus;
    }

    @Override
    public double getPlataZaPrekuvremenaRabota() {
        return -1;
    }

    @Override
    public int getPoeniNaTiketi() {
        return poeniNaTiketi.size();
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d %s",
                id,level,getPlata(),poeniNaTiketi.size(),getSumaNaPoeniNaTiketi(),
                bonus!=0?String.format("Bonus: %.2f",bonus):"");
    }
}
class PayrollSystem
{
    private List<Employee> listaOdEmloyee;
    private Map<String,Double> hourlyRateByLevel;
    private Map<String,Double> ticketRateByLevel;
    PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel)
    {
        this.listaOdEmloyee=new ArrayList<>();
        this.hourlyRateByLevel=hourlyRateByLevel;
        this.ticketRateByLevel=ticketRateByLevel;
    }
    Employee createEmployee (String line) throws BonusNotAllowedException {
        //H;Stefan;level1;45.0 10%
        //H;78b26c;level4;37.79 509
        //F;Ana;level8;1;2;3;4;5;6;7;8 500
        //F;24f7e5;level5;7;7;3;4;3;7;6;5 7.50%
        String[]parts=line.split("\\s+");
        if(parts.length>1)//ima bonus
        {
            if(parts[1].contains("%"))//procentualen bonus
            {
                String []delovi=parts[0].split(";");
                if(delovi[0].equals("H"))//Hourly so %
                {
                    Employee e=new HourlyEmployee(delovi[1],delovi[2],Double.parseDouble(delovi[3]),hourlyRateByLevel);
                    double procent=Double.parseDouble(parts[1].substring(0,parts[1].length()-1));
                    if(procent>20)
                    {
                        throw new BonusNotAllowedException(String.format("Bonus of %.2f%% is not allowed", procent));
                    }
                    double bonus=e.getPlata()*procent/100.0;
                    e.setBonus(bonus);
                    listaOdEmloyee.add(e);
                    return e;
                }
                else// Freelancer so %
                {
                    Employee e=new FreelanceEmployee(delovi[1],delovi[2],Arrays.stream(delovi)
                            .skip(3).map(str->Integer.parseInt(str)).collect(Collectors.toList()),ticketRateByLevel );
                    double procent=Double.parseDouble(parts[1].substring(0,parts[1].length()-1));
                    if(procent>20)
                    {
                        throw new BonusNotAllowedException(String.format("Bonus of %.2f%% is not allowed", procent));
                    }
                    double bonus=e.getPlata()*procent/100.0;
                    e.setBonus(bonus);
                    listaOdEmloyee.add(e);
                    return e;
                }
            }
            else//fiksen bonus
            {
                String []delovi=parts[0].split(";");
                if(delovi[0].equals("H"))//Hourly so fiksen
                {
                    Employee e=new HourlyEmployee(delovi[1],delovi[2],Double.parseDouble(delovi[3]),hourlyRateByLevel);
                    if(Double.parseDouble(parts[1])>1000)
                    {
                        throw new BonusNotAllowedException(String.format("Bonus of %.0f$ is not allowed",Double.parseDouble(parts[1])));
                    }
                    e.setBonus(Double.parseDouble(parts[1]));
                    listaOdEmloyee.add(e);
                    return e;
                }
                else
                {
                    Employee e=new FreelanceEmployee(delovi[1],delovi[2],Arrays.stream(delovi)
                            .skip(3).map(str->Integer.parseInt(str)).collect(Collectors.toList()),ticketRateByLevel );
                    if(Double.parseDouble(parts[1])>1000)
                    {
                        throw new BonusNotAllowedException(String.format("Bonus of %.0f$ is not allowed",Double.parseDouble(parts[1])));
                    }
                    e.setBonus(Double.parseDouble(parts[1]));
                    listaOdEmloyee.add(e);
                    return e;
                }
            }
        }
        else //nema bonus
        {
            //F;Gjorgji;level2;1;5;10;6
            //H;dff48d;level4;63.26
            String[]delovi=parts[0].split(";");
            if(delovi[0].equals("H"))
            {
                Employee e=new HourlyEmployee(delovi[1],delovi[2],Double.parseDouble(delovi[3]),hourlyRateByLevel);
                e.setBonus(0);
                listaOdEmloyee.add(e);
                return e;
            }
            else//F
            {
                Employee e=new FreelanceEmployee(delovi[1],delovi[2],Arrays.stream(delovi)
                        .skip(3).map(str->Integer.parseInt(str)).collect(Collectors.toList()),ticketRateByLevel );
                e.setBonus(0);
                listaOdEmloyee.add(e);
                return e;
            }
        }
    }

    Map<String, Double> getOvertimeSalaryForLevels ()
    {
//        List<Employee>filtrirani=
//                this.listaOdEmloyee.stream()
//                        .filter(e->e.getPlataZaPrekuvremenaRabota()!=-1)
//                        .collect(Collectors.toList());
        Map<String,Double>mapa= listaOdEmloyee.stream()
                .collect(Collectors.groupingBy(e->e.level,
                        Collectors.summingDouble(e->e.getPlataZaPrekuvremenaRabota())));
        List<String>klucevi=mapa.keySet().stream().filter(k->mapa.get(k)==-1)
                .collect(Collectors.toList());
        klucevi.forEach(k->mapa.remove(k));
        return mapa;
    }

    void printStatisticsForOvertimeSalary ()
    {
        DoubleSummaryStatistics dss=this.listaOdEmloyee
                .stream()
                        .filter(e->e.getPlataZaPrekuvremenaRabota()!=-1).
                mapToDouble(e->e.getPlataZaPrekuvremenaRabota())
                .summaryStatistics();
        System.out.println(String.format("Statistics for overtime salary: Min: %.2f Average: %.2f Max: %.2f Sum: %.2f",
                dss.getMin(),dss.getAverage(),dss.getMax(),dss.getSum()));
    }

    Map<String, Integer> ticketsDoneByLevel()
    {
        return this.listaOdEmloyee.stream()
                .filter(e->e.getPoeniNaTiketi()!=0)
                .collect(Collectors.groupingBy(e->e.level,
                        Collectors.summingInt(e->e.getPoeniNaTiketi())));
    }

    Collection<Employee> getFirstNEmployeesByBonus (int n)
    {
        return this.listaOdEmloyee.stream().sorted(Comparator.comparing(Employee::getBonus).reversed())
                .limit(n).collect(Collectors.toList());
    }


}

class BonusNotAllowedException extends Exception
{
    public BonusNotAllowedException(String message) {
        super(message);
    }
}
public class PayrollSystemTest2 {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 11 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5.5 + i * 2.5);
        }

        Scanner sc = new Scanner(System.in);

        int employeesCount = Integer.parseInt(sc.nextLine());

        PayrollSystem ps = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
        Employee emp = null;
        for (int i = 0; i < employeesCount; i++) {
            try {
                emp = ps.createEmployee(sc.nextLine());
            } catch (BonusNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

        int testCase = Integer.parseInt(sc.nextLine());

        switch (testCase) {
            case 1: //Testing createEmployee
                if (emp != null)
                    System.out.println(emp);
                break;
            case 2: //Testing getOvertimeSalaryForLevels()
                ps.getOvertimeSalaryForLevels().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Overtime salary: %.2f\n", level, overtimeSalary);
                });
                break;
            case 3: //Testing printStatisticsForOvertimeSalary()
                ps.printStatisticsForOvertimeSalary();
                break;
            case 4: //Testing ticketsDoneByLevel
                ps.ticketsDoneByLevel().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Tickets by level: %d\n", level, overtimeSalary);
                });
                break;
            case 5: //Testing getFirstNEmployeesByBonus (int n)
                ps.getFirstNEmployeesByBonus(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
                break;
        }

    }
}