package FileSystemTest6;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class FileNameExistsException extends Exception
{
    public FileNameExistsException(String message) {
        super(message);
    }
}
interface IFile
{
    String getFileName();
    long getFileSize();
    String getFileInfo(int brTabovi);
    void sortBySize();
    long findLargestFile ();
}

class KlasaZaTab
{

    public static String napraviVovlekuvanje(int brTabovi) {
        return IntStream.range(0,brTabovi)
                .mapToObj(i->"\t")
                .collect(Collectors.joining());
    }
}

class File implements IFile
{
    protected String name;
    protected long size;

    public File() {
    }

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public File(String name) {
        this.name = name;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(int brTabovi) {
        return String.format("%sFile name: %10s File size: %10d\n",
                KlasaZaTab.napraviVovlekuvanje(brTabovi),
                name,size);
    }

    @Override
    public void sortBySize() {
        return;
    }

    @Override
    public long findLargestFile() {
        return this.size;
    }
}
class Folder extends File
{
    private List<IFile>listaOdFajlovi;



    public Folder(String name) {
        super(name);
        this.listaOdFajlovi=new ArrayList<>();
    }

    void addFile (IFile file) throws FileNameExistsException {
        if(dokolkuPostoi(file))
        {
            throw new FileNameExistsException(String.format("There is already a file named %s in the folder %s",
                    file.getFileName(),this.getFileName()));
        }
        this.listaOdFajlovi.add(file);
    }

    private boolean dokolkuPostoi(IFile file) {
        return this.listaOdFajlovi.stream().anyMatch(ifile->ifile.getFileName().equals(file.getFileName()));
    }

    @Override
    public String getFileName() {
        return super.getFileName();
    }

    @Override
    public long getFileSize() {
        return this.listaOdFajlovi.stream().mapToLong(ifile->ifile.getFileSize())
                .sum();
    }

    @Override
    public String getFileInfo(int brTabovi) {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%sFolder name: %10s Folder size: %10d\n",KlasaZaTab.napraviVovlekuvanje(brTabovi)
                ,this.getFileName(),this.getFileSize()));
        this.listaOdFajlovi.forEach(ifile->sb.append(ifile.getFileInfo(brTabovi+1)));
        //sb.setLength(sb.length()-1);
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        this.listaOdFajlovi=this.listaOdFajlovi.stream().sorted(Comparator.comparing(IFile::getFileSize))
                .collect(Collectors.toList());
        this.listaOdFajlovi.forEach(ifile->ifile.sortBySize());
    }

    @Override
    public long findLargestFile() {
        return this.listaOdFajlovi.stream()
                .map(iFile -> iFile.findLargestFile())
                .max(Comparator.naturalOrder()).orElse(0L);
    }
}

class FileSystem
{
    private Folder rootDir;

    public FileSystem() {
        rootDir=new Folder("root");
    }

    void addFile (IFile file) throws FileNameExistsException {
        rootDir.addFile(file);
    }

    long findLargestFile ()
    {
        return rootDir.findLargestFile();
    }

    void sortBySize()
    {
        rootDir.sortBySize();
    }

    @Override
    public String toString() {
        return rootDir.getFileInfo(0);
    }
}


public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}