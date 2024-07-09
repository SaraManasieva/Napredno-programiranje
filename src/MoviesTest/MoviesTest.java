package MoviesTest;

import java.util.*;
import java.util.stream.Collectors;

class Movie
{
    private String naslovNaFilm;
    private List<Integer> listaOdRejtinzi;
    public Movie(String naslovNaFilm, List<Integer> listaOdRejtinzi) {
        this.naslovNaFilm = naslovNaFilm;
        this.listaOdRejtinzi = listaOdRejtinzi;
    }

    public String getNaslovNaFilm() {
        return naslovNaFilm;
    }

    public void setNaslovNaFilm(String naslovNaFilm) {
        this.naslovNaFilm = naslovNaFilm;
    }

    public List<Integer> getListaOdRejtinzi() {
        return listaOdRejtinzi;
    }

    public void setListaOdRejtinzi(List<Integer> listaOdRejtinzi) {
        this.listaOdRejtinzi = listaOdRejtinzi;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",getNaslovNaFilm(),getProsecenRejting(),getListaOdRejtinzi().size());
    }

    public double getProsecenRejting() {
        return getListaOdRejtinzi().stream().mapToDouble(i->i).average().getAsDouble();
    }


    public double getRatingCoef()
    {
        return getProsecenRejting()*getListaOdRejtinzi().size()/MoviesList.getMaks();
    }


}
class MoviesList
{
    private List<Movie> listaOdFilmovi;
    private static int maks;

    public MoviesList() {

        this.listaOdFilmovi=new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings)
    {
        List<Integer> lista=new ArrayList<>();
        Arrays.stream(ratings).forEach(i->lista.add(i));
        this.listaOdFilmovi.add(new Movie(title,lista));
        maks=this.listaOdFilmovi.stream().mapToInt(f->f.getListaOdRejtinzi().size()).max().getAsInt();
    }

    public List<Movie> top10ByAvgRating()
    {
        //метод кој враќа листа од 10-те филмови со најдобар просечен рејтинг, подредени во опаѓачки редослед
        // според рејтингот (ако два филмови имаат ист просечен рејтинг, се подредуваат лексикографски според името)
        return this.listaOdFilmovi.stream().sorted(Comparator.comparing(Movie::getProsecenRejting).reversed().thenComparing(Movie::getNaslovNaFilm))
                .limit(10) .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef()
    {
        //метод кој враќа листа од 10-те филмови со најдобар рејтинг коефициент (се пресметува како просечен ретјтинг
        // на филмот x вкупно број на рејтинзи на филмот / максимален број на рејтинзи (од сите филмови во листата)
        return this.listaOdFilmovi.stream().sorted(Comparator.comparing(Movie::getRatingCoef).reversed().thenComparing(Movie::getNaslovNaFilm))
                .limit(10).collect(Collectors.toList());
    }

    public List<Movie> getListaOdFilmovi() {
        return listaOdFilmovi;
    }

    public void setListaOdFilmovi(List<Movie> listaOdFilmovi) {
        this.listaOdFilmovi = listaOdFilmovi;
    }

    public static int getMaks() {
        return maks;
    }

    @Override
    public String toString() {
        return "MoviesList{" +
                "listaOdFilmovi=" + listaOdFilmovi +
                '}';
    }

}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde