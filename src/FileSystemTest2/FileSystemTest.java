package FileSystemTest2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017 //9:27 //10:08
 * //12:27
 */
class File implements Comparable<File>
{
    //File со податоци за име (String), големина (Integer) и време на креирање (LocalDateTime)
    private String imeNaFajl;
    private char imeNaFolder;
    private int goleminaNaFajl;
    private LocalDateTime vremeNaKreiranjeNaFajl;

    public File(String imeNaFajl, char imeNaFolder, int goleminaNaFajl, LocalDateTime vremeNaKreiranjeNaFajl) {
        this.imeNaFajl = imeNaFajl;
        this.imeNaFolder = imeNaFolder;
        this.goleminaNaFajl = goleminaNaFajl;
        this.vremeNaKreiranjeNaFajl = vremeNaKreiranjeNaFajl;
    }

    public String getInformacijaZaMesec_Den()
    {
        return String.format("%s-%d",getVremeNaKreiranjeNaFajl().getMonth().toString(),getVremeNaKreiranjeNaFajl().getDayOfMonth());
    }

    public String getImeNaFajl() {
        return imeNaFajl;
    }

    public void setImeNaFajl(String imeNaFajl) {
        this.imeNaFajl = imeNaFajl;
    }

    public char getImeNaFolder() {
        return imeNaFolder;
    }

    public void setImeNaFolder(char imeNaFolder) {
        this.imeNaFolder = imeNaFolder;
    }

    public int getGoleminaNaFajl() {
        return goleminaNaFajl;
    }

    public void setGoleminaNaFajl(int goleminaNaFajl) {
        this.goleminaNaFajl = goleminaNaFajl;
    }

    public LocalDateTime getVremeNaKreiranjeNaFajl() {
        return vremeNaKreiranjeNaFajl;
    }

    public void setVremeNaKreiranjeNaFajl(LocalDateTime vremeNaKreiranjeNaFajl) {
        this.vremeNaKreiranjeNaFajl = vremeNaKreiranjeNaFajl;
    }

    @Override
    public String toString() {
        //%-10[name] %5[size]B %[createdAt]
        return String.format("%-10s %5dB %s",getImeNaFajl(),getGoleminaNaFajl(),getVremeNaKreiranjeNaFajl());
    }


    @Override
    public int compareTo(File o) {
        Comparator<File> comparator=Comparator.comparing(File::getVremeNaKreiranjeNaFajl).
                thenComparing(File::getImeNaFajl).thenComparing(File::getGoleminaNaFajl);
        return comparator.compare(this,o);
    }
}
class FileSystem
{
    private List<File> listaOdFajlovi;

    public FileSystem() {
        this.listaOdFajlovi=new ArrayList<>();
    }

    //public void addFile(char folder, String name, int size, LocalDateTime createdAt)
    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        // метод за додавање нова датотека File во фолдер со даденото име (името на фолдерот е еден знак, може да биде . или голема буква)
        this.listaOdFajlovi.add(new File(name,folder,size,createdAt));
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        //public List<File> findAllHiddenFilesWithSizeLessThen(int size)
        //враќа листа на сите скриени датотеки (тоа се датотеки чие што име започнува со знакот за точка .) со големина помала од size.
        return this.listaOdFajlovi.stream().sorted().filter(file->(file.getImeNaFajl().startsWith(".")))
                .filter(file->file.getGoleminaNaFajl()<size).collect(Collectors.toList());
    }

    public int totalSizeOfFilesFromFolders(List<Character> folders) {
        //public int totalSizeOfFilesFromFolders(List<Character> folders)
        //враќа вкупна големина на сите датотеки кои се наоѓаат во фолдерите кои се зададени во листата folders
        return this.listaOdFajlovi.stream().filter(file->folders.contains(file.getImeNaFolder()))
                .mapToInt(file->file.getGoleminaNaFajl()).sum();
    }

    public Map<Integer, Set<File>> byYear() {
        //public Map<Integer, Set<File>> byYear()
        //враќа мапа Map во која за датотеките се групирани според годината на креирање.
        return this.listaOdFajlovi.stream().collect(Collectors.groupingBy(file->file.getVremeNaKreiranjeNaFajl().getYear(),
                Collectors.toCollection(TreeSet::new)));
    }

    public Map<String, Long> sizeByMonthAndDay() {
        //public Map<String, Long> sizeByMonthAndDay()
        // враќа мапа Map во која за секој месец и ден (независно од годината) се пресметува вкупната големина
        // на сите датотеки креирани во тој месец и тој ден. Месецот се добива со повик на методот getMonth(), а денот getDayOfMonth().
        return this.listaOdFajlovi.stream().collect(Collectors.groupingBy(file->file.getInformacijaZaMesec_Den(),
                Collectors.summingLong(file->file.getGoleminaNaFajl())));
    }
}
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

