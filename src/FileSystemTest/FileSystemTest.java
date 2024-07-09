package FileSystemTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class KlasaZaPravenjeVovlekuvanje
{
    public static String napraviVovlekuvanje(int broj)
    {
        return IntStream.range(0,broj)
                .mapToObj(i->"\t").collect(Collectors.joining(""));
    }
}
//8:47 //10/14
interface IFile extends Comparable<IFile>
{
    String getFileName();
    long getFileSize();
    String getFileInfo(int brojNaTabovi);
    void sortBySize();
    long findLargestFile ();
}
class FileNameExistsException extends Exception
{
    public FileNameExistsException(String imeNaFajl,String imeNaFolder) {
        //There is already a file named test in the folder test
        super(String.format("There is already a file named %s in the folder %s",imeNaFajl,imeNaFolder));
    }
}
class File implements IFile
{
    private String imeNaFajl;
    private long goleminaNaFajl;


    public File(String imeNaFajl, long goleminaNaFajl) {
        this.imeNaFajl = imeNaFajl;
        this.goleminaNaFajl = goleminaNaFajl;
    }

    public File(String imeNaFajl) {
        this.imeNaFajl = imeNaFajl;
    }

    @Override
    public String getFileName() {
        return imeNaFajl;
    }

    @Override
    public long getFileSize() {
        return goleminaNaFajl;
    }

    // File name [името на фајлот со 10 места порамнето на десно] File size: [големината на фајлот со 10 места пораменета на десно ]
    //File name:     test_1 File size:       1070
    @Override
    public String getFileInfo(int brojNaTabovi) {
        return String.format("%sFile name: %10s File size: %10d\n",KlasaZaPravenjeVovlekuvanje.napraviVovlekuvanje(brojNaTabovi),
                imeNaFajl,goleminaNaFajl);
    }

    @Override
    public void sortBySize() {
        return;
    }

    @Override
    public long findLargestFile() {
        return goleminaNaFajl;
    }

    public String getImeNaFajl() {
        return imeNaFajl;
    }

    public void setImeNaFajl(String imeNaFajl) {
        this.imeNaFajl = imeNaFajl;
    }

    public long getGoleminaNaFajl() {
        return goleminaNaFajl;
    }

    public void setGoleminaNaFajl(long goleminaNaFajl) {
        this.goleminaNaFajl = goleminaNaFajl;
    }

    @Override
    public String toString() {
        return "File{" +
                "imeNaFajl='" + imeNaFajl + '\'' +
                ", goleminaNaFajl=" + goleminaNaFajl +
                '}';
    }

    @Override
    public int compareTo(IFile o) {
        Comparator<IFile> comparator=Comparator.comparing(IFile::getFileSize);
        return this.compareTo(o);
    }
}
class Folder extends File
{

    private List<IFile> listaOdIFile;


    public Folder(String imeNaFajl) {
        super(imeNaFajl);
        this.listaOdIFile=new ArrayList<>();
    }

    //void addFile (IFile file)
    public void addFile(IFile file) throws FileNameExistsException {
        if(dokolkuPostoi(file.getFileName()))
        {
            throw new FileNameExistsException(file.getFileName(),this.getImeNaFajl());
        }
        this.listaOdIFile.add(file);
    }

    private boolean dokolkuPostoi(String imeNaFajl) {
        return this.listaOdIFile.stream().anyMatch(file->file.getFileName().equals(imeNaFajl));
    }

    @Override
    public String getFileName() {
        return this.getImeNaFajl();
    }

    @Override
    public long getFileSize() {
        return this.listaOdIFile.stream().mapToLong(ifile->ifile.getFileSize()).sum();
    }

    @Override
    public String getFileInfo(int brojNaTabovi) {
        //Folder name [името на директориумот со 10 места порамнето на десно] Folder size: [големината на директориумот со 10 места пораменета на десно ]
        //Folder name:       root Folder size:      13070
        //	Folder name:       test Folder size:      13070
        //		File name:       test File size:      12000
        //		File name:     test_1 File size:       1070
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%sFolder name: %10s Folder size: %10d\n",KlasaZaPravenjeVovlekuvanje.napraviVovlekuvanje(brojNaTabovi),
                this.getFileName(),this.getFileSize()));
        sb.append(this.listaOdIFile.stream().map(iFile -> iFile.getFileInfo(brojNaTabovi+1))
                .collect(Collectors.joining()));

        return sb.toString();
    }

    @Override
    public void sortBySize() {
        this.listaOdIFile=this.listaOdIFile.stream().sorted(Comparator.comparing(IFile::getFileSize))
                .collect(Collectors.toList());
        this.listaOdIFile.stream().forEach(iFile -> iFile.sortBySize());
    }

    @Override
    public long findLargestFile() {
        return this.listaOdIFile.stream().map(iFile -> iFile.findLargestFile()).max(Comparator.naturalOrder()).orElse(0L);
    }

    @Override
    public String getImeNaFajl() {
        return super.getImeNaFajl();
    }

    @Override
    public void setImeNaFajl(String imeNaFajl) {
        super.setImeNaFajl(imeNaFajl);
    }

    @Override
    public long getGoleminaNaFajl() {
        return super.getGoleminaNaFajl();
    }

    @Override
    public void setGoleminaNaFajl(long goleminaNaFajl) {
        super.setGoleminaNaFajl(goleminaNaFajl);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int compareTo(IFile o) {
        return super.compareTo(o);
    }
}
class FileSystem
{
    private Folder rootDirectory;

    public FileSystem() {
        this.rootDirectory=new Folder("root");
    }

    //void addFile (IFile file)
    public void addFile(IFile file) throws FileNameExistsException {
        this.rootDirectory.addFile(file);
    }

    //void sortBySize()
    public void sortBySize() {
        this.rootDirectory.sortBySize();
    }

    //long findLargestFile ()
    public long findLargestFile() {
        return this.rootDirectory.findLargestFile();
    }

    @Override
    public String toString() {
        return this.rootDirectory.getFileInfo(0);
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