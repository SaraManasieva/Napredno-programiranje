package FileSystemTestzad7;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
class File implements Comparable<File>
{
    private char folder;
    private String name;
    private int size;
    private LocalDateTime createdAt;

    public File(char folder, String name, int size, LocalDateTime createdAt) {
        this.folder = folder;
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    public char getFolder() {
        return folder;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStringMesecDen() {
        return String.format("%s-%d",getCreatedAt().getMonth().toString(),getCreatedAt().getDayOfMonth());
    }

    @Override
    public int compareTo(File o) {
        Comparator<File>comparator=Comparator.comparing(File::getCreatedAt).thenComparing(File::getName)
                .thenComparing(File::getSize);
        return comparator.compare(this,o);
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s",name,size,createdAt);
    }
}
class FileSystem
{
    private List<File>listaOdFajlovi;

    public FileSystem() {
        this.listaOdFajlovi=new ArrayList<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt)
    {
        this.listaOdFajlovi.add(new File(folder,name,size,createdAt));
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size)
    {
        return this.listaOdFajlovi.stream()
                .sorted()
                .filter(f->f.getName().startsWith(".")&&f.getSize()<size)
                .collect(Collectors.toList());
    }

    public int totalSizeOfFilesFromFolders(List<Character> folders)
    {
        return this.listaOdFajlovi.stream()
                .filter(file->folders.contains(file.getFolder()))
                .mapToInt(file->file.getSize()).sum();
    }

    public Map<Integer, Set<File>> byYear()
    {
        return this.listaOdFajlovi.stream()
                .collect(Collectors.groupingBy(f->f.getCreatedAt().getYear(),
                        TreeMap::new,
                        Collectors.toCollection(TreeSet::new)));
    }

    public Map<String, Long> sizeByMonthAndDay()
    {
        return this.listaOdFajlovi.stream()
                .collect(Collectors.groupingBy(f->f.getStringMesecDen(),
                        TreeMap::new,
                        Collectors.summingLong(f->f.getSize())));
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

