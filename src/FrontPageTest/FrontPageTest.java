package FrontPageTest;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

class CategoryNotFoundException extends Exception
{
    public CategoryNotFoundException(String message) {
        super(String.format("Category %s was not found",message));
    }
}

class Category
{
    private String imeNaKategorija;

    public Category(String imeNaKategorija) {
        this.imeNaKategorija = imeNaKategorija;
    }

    public String getImeNaKategorija() {
        return imeNaKategorija;
    }

    public void setImeNaKategorija(String imeNaKategorija) {
        this.imeNaKategorija = imeNaKategorija;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return imeNaKategorija.equals(category.imeNaKategorija);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imeNaKategorija);
    }

    @Override
    public String toString() {
        return "Category{" +
                "imeNaKategorija='" + imeNaKategorija + '\'' +
                '}';
    }
}

abstract class NewsItem
{
    private String naslov;
    private Date datumNaObjavuvanje;
    private Category kategorija;

    public NewsItem(String naslov, Date datumNaObjavuvanje, Category kategorija) {
        this.naslov = naslov;
        this.datumNaObjavuvanje = datumNaObjavuvanje;
        this.kategorija = kategorija;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public Date getDatumNaObjavuvanje() {
        return datumNaObjavuvanje;
    }

    public void setDatumNaObjavuvanje(Date datumNaObjavuvanje) {
        this.datumNaObjavuvanje = datumNaObjavuvanje;
    }

    abstract String getTeaser();

    int predKolkuMinEObjVesta()
    {
        Date segasnaData=new Date();
        //ms->min
        //ms->10^-3 s
        //10^-3 s-> 10^-3 / 60 min
        return (int) (segasnaData.getTime()-getDatumNaObjavuvanje().getTime())/(1000*60);
    }

    public Category getKategorija() {
        return kategorija;
    }

    public void setKategorija(Category kategorija) {
        this.kategorija = kategorija;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "naslov='" + naslov + '\'' +
                ", datumNaObjavuvanje=" + datumNaObjavuvanje +
                ", kategorija=" + kategorija +
                '}';
    }
}

class TextNewsItem extends NewsItem
{
    private String tekstNaVest;

    public TextNewsItem(String naslov, Date datumNaObjavuvanje, Category kategorija,String tekstNaVest) {
        super(naslov, datumNaObjavuvanje, kategorija);
        this.tekstNaVest=tekstNaVest;
    }

    public String getTekstNaVest() {
        return tekstNaVest;
    }

    public void setTekstNaVest(String tekstNaVest) {
        this.tekstNaVest = tekstNaVest;
    }

    @Override
    String getTeaser() {
        StringBuilder sb=new StringBuilder();
        sb.append(getNaslov()).append("\n");
        sb.append(predKolkuMinEObjVesta()).append("\n");
        String tekst=getTekstNaVest();
        if(getTekstNaVest().length()>80)
        {
            tekst=getTekstNaVest().substring(0,80);
        }
        sb.append(tekst);
        return sb.toString();
    }

    @Override
    public String toString() {
        return "TextNewsItem{" +
                "tekstNaVest='" + tekstNaVest + '\'' +
                '}';
    }
}
class MediaNewsItem extends NewsItem
{
    private String url;
    private int brNaPregledi;

    public MediaNewsItem(String naslov, Date datumNaObjavuvanje, Category kategorija,String url,int brNaPregledi) {
        super(naslov, datumNaObjavuvanje, kategorija);
        this.url=url;
        this.brNaPregledi=brNaPregledi;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBrNaPregledi() {
        return brNaPregledi;
    }

    public void setBrNaPregledi(int brNaPregledi) {
        this.brNaPregledi = brNaPregledi;
    }

    @Override
    String getTeaser() {
        StringBuilder sb=new StringBuilder();
        sb.append(getNaslov()).append("\n");
        sb.append(predKolkuMinEObjVesta()).append("\n");
        sb.append(getUrl()).append("\n");
        sb.append(getBrNaPregledi());
        return sb.toString();
    }

    @Override
    public String toString() {
        return "MediaNewsItem{" +
                "url='" + url + '\'' +
                ", brNaPregledi=" + brNaPregledi +
                '}';
    }
}

class FrontPage
{
    private List<NewsItem> listaOdVesti;
    private Category[] nizaOdSiteKategorii;

    FrontPage(Category[] categories)
    {
        this.listaOdVesti=new ArrayList<>();
        this.nizaOdSiteKategorii=categories;
    }

    void addNewsItem(NewsItem newsItem)
    {
        this.listaOdVesti.add(newsItem);
    }

    List<NewsItem> listByCategory(Category category)
    {
        return this.listaOdVesti.stream().filter(v->v.getKategorija().equals(category))
                .collect(Collectors.toList());
    }

    List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        if(!Arrays.stream(nizaOdSiteKategorii).anyMatch(k->k.getImeNaKategorija().equals(category)))
        {
            throw new CategoryNotFoundException(category);
        }
        return this.listaOdVesti.stream().filter(v->v.getKategorija().getImeNaKategorija().equals(category))
                .collect(Collectors.toList());
    }



    public FrontPage(List<NewsItem> listaOdVesti, Category[] nizaOdSiteKategorii) {
        this.listaOdVesti = listaOdVesti;
        this.nizaOdSiteKategorii = nizaOdSiteKategorii;
    }

    public List<NewsItem> getListaOdVesti() {
        return listaOdVesti;
    }

    public void setListaOdVesti(List<NewsItem> listaOdVesti) {
        this.listaOdVesti = listaOdVesti;
    }

    public Category[] getNizaOdSiteKategorii() {
        return nizaOdSiteKategorii;
    }

    public void setNizaOdSiteKategorii(Category[] nizaOdSiteKategorii) {
        this.nizaOdSiteKategorii = nizaOdSiteKategorii;
    }

    @Override
    public String toString() {
        String s= this.listaOdVesti.stream().map(v->v.getTeaser())
                .collect(Collectors.joining("\n"));
        return String.format("%s\n",s);

    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde