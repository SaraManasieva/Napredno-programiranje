package StopCoronaTest_zad20;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

interface ILocation{
    double getLongitude();

    double getLatitude();

    LocalDateTime getTimestamp();
}
class User
{
    private String name;
    private String id;
    private List<ILocation> iLocations;
    private LocalDateTime timestamp;
    private boolean daliEZarazen;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
        this.iLocations=new ArrayList<>();
        this.timestamp=LocalDateTime.MAX;
        this.daliEZarazen=false;
    }

    public void setiLocations(List<ILocation> iLocations) {
        this.iLocations = iLocations;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setDaliEZarazen(boolean daliEZarazen) {
        this.daliEZarazen = daliEZarazen;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isDaliEZarazen() {
        return daliEZarazen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
class StopCoronaApp
{
    private Map<String,User>mapaOdUserPoId;
    private Map<User,Map<User,Integer>>mapaOdMapaOdBliskiKontaktiNaKorisnik;
    public StopCoronaApp() {
        this.mapaOdUserPoId=new HashMap<>();
        this.mapaOdMapaOdBliskiKontaktiNaKorisnik=new HashMap<>();
    }
    void addUser(String name, String id) throws UserAlreadyExistException {
        if(mapaOdUserPoId.containsKey(id))
        {
            throw new UserAlreadyExistException(String.format("User with id %s already exists",id));
        }
        mapaOdUserPoId.put(id,new User(name,id));
    }
    void addLocations (String id, List<ILocation> iLocations)
    {
        mapaOdUserPoId.get(id).setiLocations(iLocations);
    }
    void detectNewCase (String id, LocalDateTime timestamp)
    {
        this.mapaOdUserPoId.get(id).setDaliEZarazen(true);
        this.mapaOdUserPoId.get(id).setTimestamp(timestamp);
    }
    Map<User, Integer> getDirectContacts (User u)
    {
        return mapaOdMapaOdBliskiKontaktiNaKorisnik.get(u)
                .entrySet().stream()
                .filter(entry->entry.getValue()!=0)
                .collect(Collectors.toMap(entry->entry.getKey(),
                        entry->entry.getValue()));
    }
    Collection<User> getIndirectContacts (User u)
    {
        Set<User>indirektniKonatkti=new TreeSet<>(Comparator.comparing(User::getName)
                .thenComparing(User::getId));
        Map<User,Integer>mapaOdDirektniNaU= getDirectContacts(u);
        mapaOdDirektniNaU.keySet().stream()
                .flatMap(user->getDirectContacts(user).keySet().stream())
                .filter(user->!user.equals(u))
                .filter(user->!indirektniKonatkti.contains(user))
                .filter(user->!getDirectContacts(u).containsKey(user))
                .forEach(user->indirektniKonatkti.add(user));
        return indirektniKonatkti;
    }
    void createReport ()
    {
        this.mapaOdUserPoId.values().stream()
                //.filter(user -> user.isDaliEZarazen()==true)
                .forEach(user->{
            this.mapaOdUserPoId.values().stream()
                    .filter(drugUser->!drugUser.equals(user))
                    .forEach(drugUser->{
                        mapaOdMapaOdBliskiKontaktiNaKorisnik.putIfAbsent(user,new HashMap<>());
                        mapaOdMapaOdBliskiKontaktiNaKorisnik.computeIfPresent(user,(k,v)->{
                            v.putIfAbsent(drugUser,0);
                            v.computeIfPresent(drugUser,(k1,v1)->{
                                v1=v1+KlasaZaProverka.brojNaBliskiKontakti(user,drugUser);
                                return v1;
                            });
                            return v;
                        });
                    });
        });
        //create report
        StringBuilder sb=new StringBuilder();
        List<Integer>listaOdBrojNaDirektni=new ArrayList<>();
        List<Integer>listaOdBrojIndirekni=new ArrayList<>();
        mapaOdUserPoId.values().stream().filter(user->user.isDaliEZarazen()==true)
                .sorted(Comparator.comparing(User::getTimestamp))
                .forEach(user->{
                    sb.append(String.format("%s %s %s\n",
                            user.getName(),user.getId(),user.getTimestamp()));
                    sb.append("Direct contacts:").append("\n");
                    getDirectContacts(user).entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEach(entry->{
                                sb.append(String.format("%s %s %d\n",
                                        entry.getKey().getName(),
                                        entry.getKey().getId().substring(0,4)+"***",
                                        entry.getValue()));
                            });
                    sb.append(String.format("Count of direct contacts: %d\n",
                            getDirectContacts(user).values().stream().mapToInt(i->i).sum()));
                    listaOdBrojNaDirektni.add( getDirectContacts(user).values().stream().mapToInt(i->i).sum());
                    sb.append("Indirect contacts:").append("\n");
                    getIndirectContacts(user).stream()
                            .forEach(us->{
                                sb.append(String.format("%s %s\n",
                                        us.getName(),us.getId().substring(0,4)+"***"));
                            });
                    sb.append(String.format("Count of indirect contacts: %d\n",
                            getIndirectContacts(user).size()));
                    listaOdBrojIndirekni.add(getIndirectContacts(user).size());
                });

        sb.append(String.format("Average direct contacts: %.4f\n",listaOdBrojNaDirektni.stream().mapToInt(i->i).average().orElse(0)));
        sb.append(String.format("Average indirect contacts: %.4f\n",
                listaOdBrojIndirekni.stream().mapToInt(i->i).average().orElse(0)));
        System.out.println(sb.toString());
    }


}
class KlasaZaProverka
{
    private static boolean proveriDaliEBlizokKontakt(ILocation i1, ILocation i2) {
        return KlasaZaProverka.getEvklidovorRastojanie(i1,i2)<=2&&KlasaZaProverka.getVremenskoRAstojanie(i1,i2)<=300;
    }

    private static long getVremenskoRAstojanie(ILocation i1, ILocation i2) {
        return Math.abs(Duration.between(i1.getTimestamp(),i2.getTimestamp()).toSeconds());
    }

    private static double getEvklidovorRastojanie(ILocation i1, ILocation i2) {
        return Math.sqrt(Math.pow(i1.getLatitude()-i2.getLatitude(), 2)+
                Math.pow(i1.getLongitude()-i2.getLongitude(),2));
    }

    public static int brojNaBliskiKontakti(User u1, User u2) {
        int counter=0;
        for(ILocation i1:u1.getiLocations())
        {
            for(ILocation i2:u2.getiLocations())
            {
                if(KlasaZaProverka.proveriDaliEBlizokKontakt(i1,i2))
                {
                    counter++;
                }
            }
        }
        return counter;
    }


}
class UserAlreadyExistException extends Exception
{
    public UserAlreadyExistException(String message) {
        super(message);
    }
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
