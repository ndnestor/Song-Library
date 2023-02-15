package f2c.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public class Library {
    private ArrayList<Song> Lib;

    public Library(){
        this.Lib = new ArrayList<Song>();
    }

    //function return type.... throw exception?
    public void addSong(String name, String artist){
        if(!checkDuplicateSong(name,artist)){
            Lib.add(new Song(name,artist));
            sortLib(Lib);
            return;
        }
        System.out.println("Duplicate Song");
        // throw exception?
    }
    public void addSong(String name, String artist, String album, int year){
        if(!checkDuplicateSong(name,artist)){
            Lib.add(new Song(name,artist,album,year));
            sortLib(Lib);
            return;
        }
        System.out.println("Duplicate Song");
        // throw exception?
    }
    public void delete(String name, String artist){
        Lib.removeIf(s -> s.getName().equals(name) && s.getArtist().equals(artist));
    }
    public void editSong(String prevName, String prevArtist, String newName, String newArtist ){
        delete(prevName,prevArtist);
        addSong(newName,newArtist);
        sortLib(Lib);
    }
    public void editSong(String prevName, String prevArtist, String newName, String newArtist, String album, int year ){
        delete(prevName,prevArtist);
        addSong(newName,newArtist,album,year);
        sortLib(Lib);
    }
    public boolean checkDuplicateSong(String name, String artist){
        for(Song s: Lib){
            if(s.getName().equals(name)&&s.getArtist().equals(artist)){
                return true;
            }
        }
        return false;
    }
    public void printLibrary(){
        for(Song s: Lib){
            System.out.println("Song: "+s.getName()+" Artist: "+s.getArtist());
        }

    }
    public static void sortLib(List<Song> unsortedLib) {
        Collections.sort(unsortedLib, Comparator.comparing(Song::getName));
    }
}
