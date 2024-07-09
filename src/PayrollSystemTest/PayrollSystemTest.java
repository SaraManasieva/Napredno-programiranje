package PayrollSystemTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

interface SposobnistZaPlata
{
    double getPlata();
}
abstract class Employee implements SposobnistZaPlata
{
    private String id;
    private String level;
     private double koeficient;

    public Employee(String id, String level) {
        this.id = id;
        this.level = level;
    }

    //Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel

    public static Employee napraviVraboten (String linija,Map<String, Double> hourlyRateByLevel,Map<String, Double> ticketRateByLevel)
    {
        //F;72926a;level7;5;6;8;1
        //H;2aba0f;level6;40.47
        String[]parts=linija.split(";");
        if(parts[0].equals("H"))
        {
            //H;ID;level;hours;
             return new RabotniksoCasovi(parts[1],parts[2],Double.parseDouble(parts[3]),hourlyRateByLevel.get(parts[2]));
        }
        else
        {
            return new RabotnikSoTiket(parts[1],parts[2],Arrays.stream(parts).skip(3).map(str->Integer.parseInt(str )).collect(Collectors.toList()),
                    ticketRateByLevel.get(parts[2]));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getKoeficient() {
        return koeficient;
    }
}

class RabotniksoCasovi extends Employee
{
    private double brojNaCasovi;
    private double koeficient;

    private double regularniCasovi;
    private double prekuvremeniCasovi;

    public RabotniksoCasovi(String id, String level, double brojNaCasovi,double koeficient) {
        super(id, level);
        this.brojNaCasovi = brojNaCasovi;
        this.koeficient=koeficient;
    }

    public double getBrojNaCasovi() {
        return brojNaCasovi;
    }

    public void setBrojNaCasovi(double brojNaCasovi) {
        this.brojNaCasovi = brojNaCasovi;
    }

    public double getKoeficient() {
        return koeficient;
    }

    public void setKoeficient(double koeficient) {
        this.koeficient = koeficient;
    }

    public double getRegularniCasovi() {
        return getBrojNaCasovi()-getPrekuvremeniCasovi();

    }

    public double getPrekuvremeniCasovi() {
        return Math.max(getBrojNaCasovi()-40,0);
    }

    @Override
    public double getPlata() {
        //Платата на HourlyEmployee се пресметува така што сите часови работа до 40 часа се множат со саатницата определена за
        // нивото, а сите часови работа над 40 часа, се множат со саатницата на нивото зголемена за коефициент 1.5.
        return getRegularniCasovi()*getKoeficient()+1.5*getPrekuvremeniCasovi()*getKoeficient();
    }

    //Employee ID: e911a6 Level: level6 Salary: 960.00 Tickets count: 9 Tickets points: 48
    //Employee ID: 2aba0f Level: level6 Salary: 944.36 Regular hours: 40.00 Overtime hours: 0.47


    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",
                getId(),getLevel(),getPlata(),getRegularniCasovi(),getPrekuvremeniCasovi());
    }
}

class RabotnikSoTiket extends Employee
{
    private List<Integer> poeniNaTiketi;
    private double koeficient;

    public RabotnikSoTiket(String id, String level, List<Integer> poeniNaTiketi,double koef) {
        super(id, level);
        this.poeniNaTiketi = poeniNaTiketi;
        this.koeficient=koef;
    }

    public List<Integer> getPoeniNaTiketi() {
        return poeniNaTiketi;
    }

    public void setPoeniNaTiketi(List<Integer> poeniNaTiketi) {
        this.poeniNaTiketi = poeniNaTiketi;
    }

    public double getKoeficient() {
        return koeficient;
    }

    public void setKoeficient(double koeficient) {
        this.koeficient = koeficient;
    }

    @Override
    public double getPlata() {
        //Платата на FreelanceEmployee се пресметува така што сумата на поените на тикетите коишто програмерот ги решил се множат
        // со плата по тикет (ticket rate) за нивото.
        return poeniNaTiketi.stream().mapToInt(i->i).sum()*getKoeficient();
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",
                getId(),getLevel(),getPlata(),getPoeniNaTiketi().size(),getPoeniNaTiketi().stream().mapToInt(i->i).sum());
    }
}
class PayrollSystem
{
    List<Employee> vraboteni;
    private Map<String,Double> hourlyRateByLevel;
    private Map<String,Double> ticketRateByLevel;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        this.vraboteni=new ArrayList<>();
    }
    public void readEmployees(InputStream is) {
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        this.vraboteni=br.lines().map(linija-> Employee.napraviVraboten(linija,hourlyRateByLevel,ticketRateByLevel)).collect(Collectors.toList());
    }
    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels)
    {
        return this.vraboteni.stream().filter(vraboten->levels.contains(vraboten.getLevel()))
                .collect(Collectors
                        .groupingBy(vraboten->vraboten.getLevel(),TreeMap::new,Collectors.toCollection(() ->
                                new TreeSet<Employee>(Comparator.comparing(Employee::getPlata).reversed().thenComparing(Employee::getLevel)))));

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