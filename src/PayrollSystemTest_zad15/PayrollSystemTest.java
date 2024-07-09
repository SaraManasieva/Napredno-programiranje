package PayrollSystemTest_zad15;

import java.io.*;
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
}
class HourlyEmployee extends Employee
{
    private double hours;
    private Map<String,Double> hourlyRateByLevel;

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
        return getBrojNaPrekuvremeniCasovi()*hourlyRateByLevel.get(level)*1.5+getBrojNaRegularniCasovi()*hourlyRateByLevel.get(level);
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",
                id,level,getPlata(),getBrojNaRegularniCasovi(),getBrojNaPrekuvremeniCasovi());
    }
}
class FreelanceEmployee extends Employee
{
    private List<Integer>poeniNaTiketi;
    private Map<String,Double> ticketRateByLevel;

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
        return getSumaNaPoeniNaTiketi()*ticketRateByLevel.get(level);
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",
                id,level,getPlata(),poeniNaTiketi.size(),getSumaNaPoeniNaTiketi());
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

    void readEmployees (InputStream is)
    {
        //H;157f3d;level10;63.14
        //F;72926a;level7;5;6;8;1
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        this.listaOdEmloyee=br.lines().map(linija->{
            String[]parts=linija.split(";");
            if(parts[0].equals("H"))
            {
                return new HourlyEmployee(parts[1],parts[2],Double.parseDouble(parts[3]),hourlyRateByLevel);
            }
            else//F
            {
                return new FreelanceEmployee(parts[1],parts[2], Arrays.stream(parts).skip(3)
                        .map(str->Integer.parseInt(str)).collect(Collectors.toList()),ticketRateByLevel);
            }
        }).collect(Collectors.toList());
    }

    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels)
    {
        return this.listaOdEmloyee.stream()
                .filter(emloyee->levels.contains(emloyee.level))
                        .collect(Collectors.groupingBy(employee -> employee.level,
                                TreeMap::new,
                                Collectors.toCollection(() -> new TreeSet<Employee>(Comparator.comparing(Employee::getPlata).reversed()
                                        .thenComparing(Employee::getLevel)))));
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}