package PatternTest;

import java.util.ArrayList;
import java.util.List;

interface InterfejsZaSostojba
{
    public void pressPlay();

    public void pressStop();

    public void pressFWD();

    public void pressREW();

}

abstract class AbstraktnaKlasa implements InterfejsZaSostojba
{
    MP3Player mp3;

    public AbstraktnaKlasa(MP3Player mp3) {
        this.mp3 = mp3;
    }
}

class PlayState extends AbstraktnaKlasa
{

    public PlayState(MP3Player mp3) {
        super(mp3);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song is already playing");
    }

    @Override
    public void pressStop() {
        System.out.println(String.format("Song %d is paused",mp3.getIndeksNaMomentalnaPesna()));
        mp3.setMomentalna_sostojba(mp3.getPause());
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        mp3.setMomentalna_sostojba(mp3.getFwd());
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        mp3.setMomentalna_sostojba(mp3.getRew());
    }
}

class StopState extends AbstraktnaKlasa
{

    public StopState(MP3Player mp3) {
        super(mp3);
    }

    @Override
    public void pressPlay() {
        System.out.println(String.format("Song %d is playing",mp3.getIndeksNaMomentalnaPesna()));
        mp3.setMomentalna_sostojba(mp3.getPlay());
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are already stopped");
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        mp3.setMomentalna_sostojba(mp3.getFwd());
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        mp3.setMomentalna_sostojba(mp3.getRew());
    }
}

class PauseState extends AbstraktnaKlasa
{

    public PauseState(MP3Player mp3) {
        super(mp3);
    }

    @Override
    public void pressPlay() {
        System.out.println(String.format("Song %d is playing",mp3.getIndeksNaMomentalnaPesna()));
        mp3.setMomentalna_sostojba(mp3.getPlay());
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are stopped");
        mp3.setIndeksNaMomentalnaPesna(0);
        mp3.setMomentalna_sostojba(mp3.getStop());
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        mp3.setMomentalna_sostojba(mp3.getFwd());
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        mp3.setMomentalna_sostojba(mp3.getRew());
    }

}

class ForwardState extends AbstraktnaKlasa
{

    public ForwardState(MP3Player mp3) {
        super(mp3);
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

class RewardState extends AbstraktnaKlasa
{

    public RewardState(MP3Player mp3) {
        super(mp3);
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



class Song
{
    private String naslovNaPesna;
    private String izveduvacNaPesna;
    public Song(String s, String s1) {
        this.naslovNaPesna=s;
        this.izveduvacNaPesna=s1;
    }

    public String getNaslovNaPesna() {
        return naslovNaPesna;
    }

    public void setNaslovNaPesna(String naslovNaPesna) {
        this.naslovNaPesna = naslovNaPesna;
    }

    public String getIzveduvacNaPesna() {
        return izveduvacNaPesna;
    }

    public void setIzveduvacNaPesna(String izveduvacNaPesna) {
        this.izveduvacNaPesna = izveduvacNaPesna;
    }

    @Override
    public String toString() {
        //Song{title=first-title, artist=first-artist}
        return String.format("Song{title=%s, artist=%s}",getNaslovNaPesna(),getIzveduvacNaPesna());
    }

}

class MP3Player
{
    private List<Song> listaOdPesni;
    private int indeksNaMomentalnaPesna;

    InterfejsZaSostojba play;
    InterfejsZaSostojba pause;
    InterfejsZaSostojba stop;
    InterfejsZaSostojba fwd;
    InterfejsZaSostojba rew;
    InterfejsZaSostojba momentalna_sostojba;
    public MP3Player(List<Song> listSongs) {
        this.listaOdPesni=new ArrayList<>(listSongs);
        this.indeksNaMomentalnaPesna=0;
        this.play=new PlayState(this);
        this.pause=new PauseState(this);
        this.stop=new StopState(this);
        this.fwd=new ForwardState(this);
        this.rew=new RewardState(this);
        this.momentalna_sostojba=this.stop;
    }
    public void pressPlay() {
        momentalna_sostojba.pressPlay();
    }

    public void printCurrentSong() {
        System.out.println(listaOdPesni.get(indeksNaMomentalnaPesna));
    }

    public void pressStop() {
        momentalna_sostojba.pressStop();
    }

    public void pressFWD() {
        momentalna_sostojba.pressFWD();
        premestiIndeksNaNapred();
        setMomentalna_sostojba(this.getPause());
    }

    public void pressREW() {
        momentalna_sostojba.pressREW();
        premestiIndeksNaNazad();
        setMomentalna_sostojba(this.getPause());
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
        this.indeksNaMomentalnaPesna = indeksNaMomentalnaPesna%listaOdPesni.size();
    }

    public InterfejsZaSostojba getPlay() {
        return play;
    }

    public void setPlay(InterfejsZaSostojba play) {
        this.play = play;
    }

    public InterfejsZaSostojba getPause() {
        return pause;
    }

    public void setPause(InterfejsZaSostojba pause) {
        this.pause = pause;
    }

    public InterfejsZaSostojba getStop() {
        return stop;
    }

    public void setStop(InterfejsZaSostojba stop) {
        this.stop = stop;
    }

    public InterfejsZaSostojba getFwd() {
        return fwd;
    }

    public void setFwd(InterfejsZaSostojba fwd) {
        this.fwd = fwd;
    }

    public InterfejsZaSostojba getRew() {
        return rew;
    }

    public void setRew(InterfejsZaSostojba rew) {
        this.rew = rew;
    }

    public InterfejsZaSostojba getMomentalna_sostojba() {
        return momentalna_sostojba;
    }

    public void setMomentalna_sostojba(InterfejsZaSostojba momentalna_sostojba) {
        this.momentalna_sostojba = momentalna_sostojba;
    }
    public void premestiIndeksNaNapred()
    {
        this.indeksNaMomentalnaPesna=(indeksNaMomentalnaPesna+1)%listaOdPesni.size();
    }
    public void premestiIndeksNaNazad()
    {
        this.indeksNaMomentalnaPesna=(indeksNaMomentalnaPesna-1+listaOdPesni.size())%listaOdPesni.size();
    }

    @Override
    public String toString() {
        return String.format("MP3Player{currentSong = %d, songList = %s}",indeksNaMomentalnaPesna,listaOdPesni);
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