package PatternTest_zad14;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Song
{
    private String naslov;
    private String izveduvac;

    public Song(String naslov, String izveduvac) {
        this.naslov = naslov;
        this.izveduvac = izveduvac;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getIzveduvac() {
        return izveduvac;
    }

    public void setIzveduvac(String izveduvac) {
        this.izveduvac = izveduvac;
    }

    @Override
    public String toString() {
        return String.format("Song{title=%s, artist=%s}",
                naslov,izveduvac);
    }
}
interface InterfejsZaAkcija
{
    void pressPlay();
    void pressStop();
    void pressFWD();
    void pressREW();
}
abstract class AbstraknaKlasa implements InterfejsZaAkcija
{
    MP3Player mp3Player;

    public AbstraknaKlasa(MP3Player mp3Player) {
        this.mp3Player = mp3Player;
    }
}

class PlayState extends AbstraknaKlasa
{

    public PlayState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song is already playing");
    }

    @Override
    public void pressStop() {
        System.out.println(String.format("Song %d is paused",mp3Player.getIndeksNaMomentalnaPesna()));
        mp3Player.setMomentalna_sostojba(mp3Player.stop);
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        mp3Player.setIndeksNaMomentalnaPesna((mp3Player.getIndeksNaMomentalnaPesna()+1)%mp3Player.getListaOdPesni().size());
        mp3Player.setMomentalna_sostojba(mp3Player.pause);
        //mp3Player.setMomentalna_sostojba(mp3Player.fwd);
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        mp3Player.setIndeksNaMomentalnaPesna((mp3Player.getIndeksNaMomentalnaPesna()+mp3Player.getListaOdPesni().size()-1)%mp3Player.getListaOdPesni().size());
        mp3Player.setMomentalna_sostojba(mp3Player.pause);
        //mp3Player.setMomentalna_sostojba(mp3Player.rew);
    }
}
class StopState extends AbstraknaKlasa
{

    public StopState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void pressPlay() {
        System.out.println(String.format("Song %d is playing",mp3Player.getIndeksNaMomentalnaPesna()));
        mp3Player.setMomentalna_sostojba(mp3Player.play);
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are already stopped");
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        mp3Player.setIndeksNaMomentalnaPesna((mp3Player.getIndeksNaMomentalnaPesna()+1)%mp3Player.getListaOdPesni().size());
        mp3Player.setMomentalna_sostojba(mp3Player.pause);
        //mp3Player.setMomentalna_sostojba(mp3Player.fwd);
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        mp3Player.setIndeksNaMomentalnaPesna((mp3Player.getIndeksNaMomentalnaPesna()+mp3Player.getListaOdPesni().size()-1)%mp3Player.getListaOdPesni().size());
        mp3Player.setMomentalna_sostojba(mp3Player.pause);
        //mp3Player.setMomentalna_sostojba(mp3Player.rew);
    }
}
class PauseState extends AbstraknaKlasa
{

    public PauseState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void pressPlay() {
        System.out.println(String.format("Song %d is playing",mp3Player.getIndeksNaMomentalnaPesna()));
        mp3Player.setMomentalna_sostojba(mp3Player.play);
    }

    @Override
    public void pressStop() {
        System.out.println(String.format("Songs are stopped"));
          mp3Player.setIndeksNaMomentalnaPesna(0);
          mp3Player.setMomentalna_sostojba(mp3Player.stop);
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        mp3Player.setIndeksNaMomentalnaPesna((mp3Player.getIndeksNaMomentalnaPesna()+1)%mp3Player.getListaOdPesni().size());
        mp3Player.setMomentalna_sostojba(mp3Player.pause);
        //mp3Player.setMomentalna_sostojba(mp3Player.fwd);
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        mp3Player.setIndeksNaMomentalnaPesna((mp3Player.getIndeksNaMomentalnaPesna()+mp3Player.getListaOdPesni().size()-1)%mp3Player.getListaOdPesni().size());
        mp3Player.setMomentalna_sostojba(mp3Player.pause);
        //mp3Player.setMomentalna_sostojba(mp3Player.rew);
    }
}

class FwdState extends AbstraknaKlasa
{

    public FwdState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void pressPlay() {

    }

    @Override
    public void pressStop() {

    }

    @Override
    public void pressFWD() {

    }

    @Override
    public void pressREW() {

    }
}
class RewState extends AbstraknaKlasa
{

    public RewState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void pressPlay() {

    }

    @Override
    public void pressStop() {

    }

    @Override
    public void pressFWD() {

    }

    @Override
    public void pressREW() {

    }
}

class MP3Player
{
    private List<Song> listaOdPesni;
    private int indeksNaMomentalnaPesna;
    InterfejsZaAkcija play;
    InterfejsZaAkcija stop;
    InterfejsZaAkcija fwd;
    InterfejsZaAkcija rew;
    InterfejsZaAkcija pause;
    InterfejsZaAkcija momentalna_sostojba;

    public MP3Player(List<Song> listaOdPesni) {
        this.listaOdPesni = listaOdPesni;
        this.indeksNaMomentalnaPesna=0;
        this.play=new PlayState(this);
        this.stop=new StopState(this);
        this.pause=new PauseState(this);
        this.fwd=new FwdState(this);
        this.rew=new RewState(this);
        this.momentalna_sostojba=new StopState(this);
    }

    public List<Song> getListaOdPesni() {
        return listaOdPesni;
    }

    public void setListaOdPesni(List<Song> listaOdPesni) {
        this.listaOdPesni = listaOdPesni;
    }

    public int getIndeksNaMomentalnaPesna() {
        return indeksNaMomentalnaPesna;
    }

    public void setIndeksNaMomentalnaPesna(int indeksNaMomentalnaPesna) {
        this.indeksNaMomentalnaPesna = indeksNaMomentalnaPesna;
    }

    public InterfejsZaAkcija getPlay() {
        return play;
    }

    public void setPlay(InterfejsZaAkcija play) {
        this.play = play;
    }

    public InterfejsZaAkcija getStop() {
        return stop;
    }

    public void setStop(InterfejsZaAkcija stop) {
        this.stop = stop;
    }

    public InterfejsZaAkcija getFwd() {
        return fwd;
    }

    public void setFwd(InterfejsZaAkcija fwd) {
        this.fwd = fwd;
    }

    public InterfejsZaAkcija getRew() {
        return rew;
    }

    public void setRew(InterfejsZaAkcija rew) {
        this.rew = rew;
    }

    public InterfejsZaAkcija getPause() {
        return pause;
    }

    public void setPause(InterfejsZaAkcija pause) {
        this.pause = pause;
    }

    public InterfejsZaAkcija getMomentalna_sostojba() {
        return momentalna_sostojba;
    }

    public void setMomentalna_sostojba(InterfejsZaAkcija momentalna_sostojba) {
        this.momentalna_sostojba = momentalna_sostojba;
    }

    public void pressPlay() {
        momentalna_sostojba.pressPlay();
    }

    public void pressStop() {
        momentalna_sostojba.pressStop();
    }

    public void pressFWD() {
        momentalna_sostojba.pressFWD();
    }

    public void pressREW() {
        momentalna_sostojba.pressREW();
    }

    public void printCurrentSong() {
        System.out.println(listaOdPesni.get(indeksNaMomentalnaPesna));
    }

    @Override
    public String toString() {
        return String.format("MP3Player{currentSong = %d, songList = [%s]}",
        this.indeksNaMomentalnaPesna,
        this.listaOdPesni.stream()
                .map(s->s.toString())
                .collect(Collectors.joining(", ")));
    }
}

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde