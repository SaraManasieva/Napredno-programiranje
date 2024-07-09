package StopCoronaTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class UserAlreadyExistException extends Exception
{
    public UserAlreadyExistException() {
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}

class KlasaZaPresmetuvanjePotrebniUslovi {
    public static double getEvklidovoRast(ILocation i1, ILocation i2) {
        return Math.sqrt(Math.pow(i1.getLatitude() - i2.getLatitude(), 2) + Math.pow(i1.getLongitude() - i2.getLongitude(), 2));
    }

    public static long getVremRast(ILocation i1, ILocation i2) {
        return Math.abs(Duration.between(i1.getTimestamp(), i2.getTimestamp()).getSeconds());
    }

    public static boolean proveriDaliEBlizokKontakt(ILocation l1, ILocation l2) {
        return getEvklidovoRast(l1, l2) <= 2 && getVremRast(l1, l2) <= 300;
    }

    public static int brojNAOstvareniBliskiKontakti(User u1, User u2) {
        int counter = 0;
        for (ILocation iLocation : u1.getiLocations()) {
            for (ILocation iLocation1 : u2.getiLocations()) {
                if (KlasaZaPresmetuvanjePotrebniUslovi.proveriDaliEBlizokKontakt(iLocation, iLocation1))
                    ++counter;
            }

        }
        return counter;
    }
}
class User
{
    private String name;
    private String id;
    private List<ILocation> iLocations;
    private boolean daliEZarazen;
    private LocalDateTime vremeNaZarazuvanje;


    public User(String id, String name) {
        this.id=id;
        this.name=name;
        this.daliEZarazen=false;
        this.iLocations=new ArrayList<ILocation>();
        this.vremeNaZarazuvanje=LocalDateTime.MAX;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ILocation> getiLocations() {
        return iLocations;
    }

    public void setiLocations(List<ILocation> iLocations) {
        this.iLocations = iLocations;
    }

    public boolean isDaliEZarazen() {
        return daliEZarazen;
    }

    public void setDaliEZarazen(boolean daliEZarazen) {
        this.daliEZarazen = daliEZarazen;
    }

    public LocalDateTime getVremeNaZarazuvanje() {
        return vremeNaZarazuvanje;
    }

    public void setVremeNaZarazuvanje(LocalDateTime vremeNaZarazuvanje) {
        this.vremeNaZarazuvanje = vremeNaZarazuvanje;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", iLocations=" + iLocations +
                ", daliEZarazen=" + daliEZarazen +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
class StopCoronaApp {
    private Map<String, User> mapaOdUserPoId;
    private Map<User, Map<User, Integer>> mapaOdMapaNaBrojNaBliskiKontPoUser;

    public StopCoronaApp() {
        this.mapaOdUserPoId = new HashMap<>();
        this.mapaOdMapaNaBrojNaBliskiKontPoUser = new TreeMap<>(Comparator.comparing(User::getVremeNaZarazuvanje).thenComparing(User::getId));
    }

    void addUser(String name, String id) throws UserAlreadyExistException {
        if (this.mapaOdUserPoId.containsKey(id)) {
            throw new UserAlreadyExistException(String.format("User with id %s already exists", id));
        }
        this.mapaOdUserPoId.put(id, new User(id, name));
    }

    void addLocations(String id, List<ILocation> iLocations) {
        this.mapaOdUserPoId.get(id).setiLocations(iLocations);
    }

    void detectNewCase(String id, LocalDateTime timestamp) {
        this.mapaOdUserPoId.get(id).setDaliEZarazen(true);
        this.mapaOdUserPoId.get(id).setVremeNaZarazuvanje(timestamp);

    }

    Map<User, Integer> getDirectContacts(User u) {
        //  return countingMapForNearContacts.get(u)
        //                .entrySet()
        //                .stream()
        //                .filter(entry -> entry.getValue() != 0)
        //                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return this.mapaOdMapaNaBrojNaBliskiKontPoUser.get(u)
                .entrySet().stream()
                .filter(entry -> entry.getValue() != 0)
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    Collection<User> getIndirectContacts(User u) {
        //За индиректни контакти се сметаат блиските контакти на директните контакти на u, при што еден корисник не може да биде и директен и инд
        Set<User> indirektniKontakti = new TreeSet<>(Comparator.comparing(User::getName).thenComparing(User::getId));
        Map<User, Integer> direktniKontaktiNaU = getDirectContacts(u);
        direktniKontaktiNaU.keySet().stream().flatMap(user -> getDirectContacts(user).keySet().stream())
                .filter(user -> !direktniKontaktiNaU.containsKey(user) && !user.equals(u) && !indirektniKontakti.contains(user))
                .forEach(user -> indirektniKontakti.add(user));
        return indirektniKontakti;
    }

    void createReport() {
        this.mapaOdUserPoId.values().stream().forEach(user -> {
            this.mapaOdUserPoId.values().stream().filter(drugUser -> !drugUser.equals(user))
                    .forEach(drugUser -> {
                        this.mapaOdMapaNaBrojNaBliskiKontPoUser
                                .putIfAbsent(user, new TreeMap<>(Comparator.comparing(User::getVremeNaZarazuvanje).thenComparing(User::getId)));
                        this.mapaOdMapaNaBrojNaBliskiKontPoUser.computeIfPresent(user, (k, v) -> {
                            v.putIfAbsent(drugUser, 0);
                            v.computeIfPresent(drugUser, (k1, v1) -> {
                                v1 = v1 + KlasaZaPresmetuvanjePotrebniUslovi.brojNAOstvareniBliskiKontakti(user, drugUser);
                                return v1;
                            });
                            return v;
                        });
                    });
        });

        List<Integer> listaOdBrojNaDirektniKontakti = new ArrayList<>();
        List<Integer> listaOdBrojNaIndirektniKontakti = new ArrayList<>();
        this.mapaOdMapaNaBrojNaBliskiKontPoUser.keySet().stream().filter(us -> us.isDaliEZarazen() == true)
                .forEach(us -> {
                    System.out.println(String.format("%s %s %s", us.getName(), us.getId(), us.getVremeNaZarazuvanje()));
                    System.out.println("Direct contacts:");
                    getDirectContacts(us).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEach(entry -> {
                                System.out.println(String.format("%s %s %d",
                                        entry.getKey().getName(), entry.getKey().getId().substring(0, 4) + "***",
                                        entry.getValue()));
                            });
                    listaOdBrojNaDirektniKontakti.add(getDirectContacts(us).values().stream().mapToInt(i->i).sum());
                    System.out.println(String.format("Count of direct contacts: %d", getDirectContacts(us).values().stream().mapToInt(i -> i).sum()));
                    System.out.println("Indirect contacts:");
                    getIndirectContacts(us).stream().forEach(user -> {
                                System.out.println(String.format("%s %s", user.getName(), user.getId().substring(0, 4) + "***"));
                            }
                    );
                    listaOdBrojNaIndirektniKontakti.add(getIndirectContacts(us).size());
                    System.out.println(String.format("Count of indirect contacts: %d", getIndirectContacts(us).size()));

                });
        System.out.println(String.format("Average direct contacts: %.4f", listaOdBrojNaDirektniKontakti.stream().mapToDouble(i -> i).average().orElse(0)));
        System.out.println(String.format("Average indirect contacts: %.4f", listaOdBrojNaIndirektniKontakti.stream().mapToDouble(i -> i).average().orElse(0)));


    }
}




interface ILocation{
    double getLongitude();

    double getLatitude();

    LocalDateTime getTimestamp();
}

public class StopCoronaTest {

    public static double timeBetweenInSeconds(ILocation location1, ILocation location2) {
        return Math.abs(Duration.between(location1.getTimestamp(), location2.getTimestamp()).getSeconds());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StopCoronaApp stopCoronaApp = new StopCoronaApp();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            switch (parts[0]) {
                case "REG": //register
                    String name = parts[1];
                    String id = parts[2];
                    try {
                        stopCoronaApp.addUser(name, id);
                    } catch (UserAlreadyExistException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "LOC": //add locations
                    id = parts[1];
                    List<ILocation> locations = new ArrayList<>();
                    for (int i = 2; i < parts.length; i += 3) {
                        locations.add(createLocationObject(parts[i], parts[i + 1], parts[i + 2]));
                    }
                    stopCoronaApp.addLocations(id, locations);

                    break;
                case "DET": //detect new cases
                    id = parts[1];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
                    stopCoronaApp.detectNewCase(id, timestamp);

                    break;
                case "REP": //print report
                    stopCoronaApp.createReport();
                    break;
                default:
                    break;
            }
        }
    }

    private static ILocation createLocationObject(String lon, String lat, String timestamp) {
        return new ILocation() {
            @Override
            public double getLongitude() {
                return Double.parseDouble(lon);
            }

            @Override
            public double getLatitude() {
                return Double.parseDouble(lat);
            }

            @Override
            public LocalDateTime getTimestamp() {
                return LocalDateTime.parse(timestamp);
            }
        };
    }
}
