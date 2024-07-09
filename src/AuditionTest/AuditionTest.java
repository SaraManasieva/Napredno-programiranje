package AuditionTest;

import java.util.*;
import java.util.stream.Collectors;

class Participant
{
    private String city;
    private String code;
    private String name;
    private int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d",getCode(),getName(),getAge());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
class Audition
{
    private Map<String, Set<Participant>> mapa;

    public Audition(Map<String, Set<Participant>> mapa) {
        this.mapa = mapa;
    }

    public Audition() {
        this.mapa=new HashMap<>();
    }

    public void addParticpant(String city, String code, String name, int age) {
        Participant p=new Participant(city,code,name,age);
        mapa.putIfAbsent(city,new HashSet<Participant>());
        mapa.get(p.getCity()).add(p);
    }

    public void listByCity(String city) {
        //и сите кандидати од даден град подредени според името, а ако е исто според возраста
        this.mapa.get(city).stream().sorted(Comparator.comparing(Participant::getName)
                .thenComparing(Participant::getAge).thenComparing(Participant::getCode))
                .forEach(participant -> System.out.println(participant));
    }

    public Map<String, Set<Participant>> getMapa() {
        return mapa;
    }

    public void setMapa(Map<String, Set<Participant>> mapa) {
        this.mapa = mapa;
    }

    @Override
    public String toString() {
        return "Audition{" +
                "mapa=" + mapa +
                '}';
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}