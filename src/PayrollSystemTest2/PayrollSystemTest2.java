package PayrollSystemTest2;

import java.util.*;
import java.util.stream.Collectors;

class BonusNotAllowedException extends Exception
{
    public BonusNotAllowedException(String string) {
        super(String.format("Bonus of %s is not allowed",string));
    }
}
interface SposobnistZaPlata
{
    double getPlata();
    double getBonus();
    double getPariZaPrekuvremena();
    int brojNaPoeniNatiketi();
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

    public static Employee napraviVraboten (String linija, Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel)
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
    private double bonus;

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

//    public double getPariZaPrekuvremena()
//    {
//        return getPrekuvremeniCasovi()*1.5*getKoeficient();
//    }

    @Override
    public double getPlata() {
        //Платата на HourlyEmployee се пресметува така што сите часови работа до 40 часа се множат со саатницата определена за
        // нивото, а сите часови работа над 40 часа, се множат со саатницата на нивото зголемена за коефициент 1.5.
        return getRegularniCasovi()*getKoeficient()+1.5*getPrekuvremeniCasovi()*getKoeficient()+getBonus();
    }

    @Override
    public double getBonus() {
        return bonus;
    }

    @Override
    public double getPariZaPrekuvremena() {
        return getPrekuvremeniCasovi()*1.50*getKoeficient();
    }

    @Override
    public int brojNaPoeniNatiketi() {
        return -1;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    //Employee ID: e911a6 Level: level6 Salary: 960.00 Tickets count: 9 Tickets points: 48
    //Employee ID: 2aba0f Level: level6 Salary: 944.36 Regular hours: 40.00 Overtime hours: 0.47


    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f %s",
                getId(),getLevel(),getPlata(),getRegularniCasovi(),getPrekuvremeniCasovi(),
                getBonus()!=0? String.format("Bonus: %.2f",getBonus()):"");
    }
}

class RabotnikSoTiket extends Employee
{
    private List<Integer> poeniNaTiketi;
    private double koeficient;
    private double bonus;

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
        return poeniNaTiketi.stream().mapToInt(i->i).sum()*getKoeficient()+getBonus();
    }

    @Override
    public double getBonus() {
        return bonus;
    }

    @Override
    public double getPariZaPrekuvremena() {
        return -1;
    }

    @Override
    public int brojNaPoeniNatiketi() {
        return poeniNaTiketi.size();
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d %s",
                getId(),getLevel(),getPlata(),getPoeniNaTiketi().size(),getPoeniNaTiketi().stream().mapToInt(i->i).sum(),
                getBonus()!=0?String.format("Bonus: %.2f",getBonus()):"");
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
    Employee createEmployee (String line) throws BonusNotAllowedException {
        //H;ID;level;hours; 100
        //F;ID;level;ticketPoints1;ticketPoints2;...;ticketPointsN; 10%
        //F;Ana;level8;1;2;3;4;5;6;7;8 500
        String []delovi=line.split("\\s+");
        if(delovi.length>1)//ima bonus
        {
            String[]parts=delovi[0].split(";");
            if(parts[0].equals("H"))//Hourly
            {
                if(delovi[1].contains("%"))
                {
                    return vratiRabotnikSoCasoviSoProcentualenBonus(parts,delovi[1],hourlyRateByLevel);
                }
                else
                {
                    return vratiRabotnikSoCasoviSoFiksenBonus(parts,delovi[1],hourlyRateByLevel);
                }
            }
            else//Freelancer
            {
                if(delovi[1].contains("%"))
                {
                    return vratiFreeLancerSoProcentualenBonus(parts,delovi[1],hourlyRateByLevel);
                }
                else
                {
                    return vratiFreeLancerSoFiksenBonus(parts,delovi[1],hourlyRateByLevel);
                }
            }

        }
        else//nema bonus
        {
            String[]parts=delovi[0].split(";");
            if(parts[0].equals("H"))
            {
                return vratiHourlyBezBonus(parts);
            }
            else
            {
               return vratiFreeLancerBezBonus(parts);
            }
        }

    }

    private RabotnikSoTiket vratiFreeLancerBezBonus(String[] parts) {
        RabotnikSoTiket rabotnikSoTiket= new RabotnikSoTiket(parts[1],parts[2],Arrays.stream(parts).skip(3).map(str->Integer.parseInt(str)).collect(Collectors.toList()),
                ticketRateByLevel.get(parts[2]));
        rabotnikSoTiket.setBonus(0);
        this.vraboteni.add(rabotnikSoTiket);
        return rabotnikSoTiket;
    }

    private RabotniksoCasovi vratiHourlyBezBonus(String[] parts) {
        RabotniksoCasovi rabotniksoCasovi=new RabotniksoCasovi(parts[1],parts[2],Double.parseDouble(parts[3]),hourlyRateByLevel.get(parts[2]));
        rabotniksoCasovi.setBonus(0);
        this.vraboteni.add(rabotniksoCasovi);
        return rabotniksoCasovi;
    }

    private RabotnikSoTiket vratiFreeLancerSoFiksenBonus(String[] parts, String del, Map<String, Double> hourlyRateByLevel) throws BonusNotAllowedException {
        RabotnikSoTiket rabotnikSoTiket=new RabotnikSoTiket(parts[1],parts[2],Arrays.stream(parts).skip(3).map(str->Integer.parseInt(str)).collect(Collectors.toList()),
                ticketRateByLevel.get(parts[2]));
        if(Double.parseDouble(del)>1000)
        {
            throw new BonusNotAllowedException(del+"$");
        }
        rabotnikSoTiket.setBonus(Double.parseDouble(del));
        this.vraboteni.add(rabotnikSoTiket);
        return rabotnikSoTiket;
    }

    private RabotnikSoTiket vratiFreeLancerSoProcentualenBonus(String[] parts, String del, Map<String, Double> hourlyRateByLevel) throws BonusNotAllowedException {
        RabotnikSoTiket rabotnikSoTiket=new RabotnikSoTiket(parts[1],parts[2],Arrays.stream(parts).skip(3).map(str->Integer.parseInt(str)).collect(Collectors.toList()),
                ticketRateByLevel.get(parts[2]));
        double procent=Double.parseDouble(del.substring(0,del.length()-1));
        if(procent>20)
        {
            throw new BonusNotAllowedException(del);
        }
        rabotnikSoTiket.setBonus(rabotnikSoTiket.getPlata()*procent/100);
        this.vraboteni.add(rabotnikSoTiket);
        return rabotnikSoTiket;
    }

    private RabotniksoCasovi vratiRabotnikSoCasoviSoFiksenBonus(String[] parts, String del, Map<String, Double> hourlyRateByLevel) throws BonusNotAllowedException {
        RabotniksoCasovi rabotniksoCasovi=new RabotniksoCasovi(parts[1],parts[2],Double.parseDouble(parts[3]),hourlyRateByLevel.get(parts[2]));
        if(Double.parseDouble(del)>1000)
        {
            throw new BonusNotAllowedException(del+"$");
        }
        rabotniksoCasovi.setBonus(Double.parseDouble(del));
        this.vraboteni.add(rabotniksoCasovi);
        return rabotniksoCasovi;
    }


    private RabotniksoCasovi vratiRabotnikSoCasoviSoProcentualenBonus(String[] parts, String del, Map<String, Double> hourlyRateByLevel) throws BonusNotAllowedException {
        RabotniksoCasovi rabotniksoCasovi=new RabotniksoCasovi(parts[1],parts[2],Double.parseDouble(parts[3]),hourlyRateByLevel.get(parts[2]));
        double procent=Double.parseDouble(del.substring(0,del.length()-1));
        if(procent>20)
        {
            throw new BonusNotAllowedException(del);
        }
        rabotniksoCasovi.setBonus(rabotniksoCasovi.getPlata()*procent/100);
        this.vraboteni.add(rabotniksoCasovi);
        return rabotniksoCasovi;
    }

    Map<String, Double> getOvertimeSalaryForLevels ()
    {
        Map<String,Double> pom= this.vraboteni.stream().collect(Collectors.groupingBy(vraboten->vraboten.getLevel()
                ,Collectors.summingDouble(vraboten->vraboten.getPariZaPrekuvremena())));
        List<String> klucevi=pom.keySet().stream().filter(k->pom.get(k)==-1).collect(Collectors.toList());
        klucevi.forEach(k->pom.remove(k));
        return pom;

    }

//    public void readEmployees(InputStream is) {
//        BufferedReader br=new BufferedReader(new InputStreamReader(is));
//        this.vraboteni=br.lines().map(linija-> Employee.napraviVraboten(linija,hourlyRateByLevel,ticketRateByLevel)).collect(Collectors.toList());
//    }
//    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels)
//    {
//        return this.vraboteni.stream().filter(vraboten->levels.contains(vraboten.getLevel()))
//                .collect(Collectors
//                        .groupingBy(vraboten->vraboten.getLevel(),TreeMap::new,Collectors.toCollection(() ->
//                                new TreeSet<Employee>(Comparator.comparing(Employee::getPlata).reversed().thenComparing(Employee::getLevel)))));
//
//    }
    void printStatisticsForOvertimeSalary ()
    {
        //Statistics for overtime salary: Min: 0.00 Average: 408.55 Max: 1735.47 Sum: 7762.52
        DoubleSummaryStatistics dss=vraboteni.stream().filter(v->v.getPariZaPrekuvremena()!=-1).mapToDouble(vraboten->vraboten.getPariZaPrekuvremena()).summaryStatistics();
        System.out.println(String.format("Statistics for overtime salary: Min: %.2f Average: %.2f Max: %.2f Sum: %.2f",
                dss.getMin(),dss.getAverage(),dss.getMax(),dss.getSum()));
    }
    Map<String, Integer> ticketsDoneByLevel()
    {
        return this.vraboteni.stream().filter(v->v.brojNaPoeniNatiketi()!=-1)
                .collect(Collectors.groupingBy(v->v.getLevel(),Collectors.summingInt(v->v.brojNaPoeniNatiketi())));

//        Map<String,Integer> pom= this.vraboteni.stream().collect(Collectors.groupingBy(vraboten->vraboten.getLevel()
//                ,Collectors.summingInt(vraboten->vraboten.brojNaPoeniNatiketi())));
//        List<String> klucevi=pom.keySet().stream().filter(k->pom.get(k)==-1).collect(Collectors.toList());
//        klucevi.forEach(k->pom.remove(k));
//        return pom;
    }
    Collection<Employee> getFirstNEmployeesByBonus (int n)
    {
        return this.vraboteni.stream().sorted(Comparator.comparing(Employee::getBonus).reversed()).limit(n).collect(Collectors.toList());
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