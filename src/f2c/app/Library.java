package f2c.app;

import java.util.ArrayList;
import java.util.Collections;

public class Library {
    private ArrayList<Song> Lib;

    public Library(){
        this.Lib = new ArrayList<Song>();
    }

    public void addSong(String name, String artist){
        if(!checkDuplicateSong(name,artist)){
            String name1 = name.replace("|","");
            String artist1 = artist.replace("|","");
            Lib.add(new Song(name1.trim(),artist1.trim()));
            Collections.sort(Lib);
            return;
        }
        System.out.println("Duplicate Song");
    }
    public void addSong(String name, String artist, String album, int year){
        if(!checkDuplicateSong(name,artist)){
            name = name.replace("|","");
            artist = artist.replace("|","");
            album = album.replace("|","");
            Lib.add(new Song(name.trim(),artist.trim(),album,year));
            Collections.sort(Lib);
            return;
        }
        System.out.println("Duplicate Song");
    }
    public void delete(String name, String artist){
        Lib.removeIf(s -> s.getName().equalsIgnoreCase(name) && s.getArtist().equalsIgnoreCase(artist));
    }
    public void editSong(String prevName, String prevArtist, String newName, String newArtist ){
        delete(prevName,prevArtist);
        addSong(newName,newArtist);
        Collections.sort(Lib);
    }
    public void editSong(String prevName, String prevArtist, String newName, String newArtist, String album, int year ){
        delete(prevName,prevArtist);
        addSong(newName,newArtist,album,year);
        Collections.sort(Lib);
    }
    public boolean checkDuplicateSong(String name, String artist){
        for(Song s: Lib){
            if(s.getName().equalsIgnoreCase(name)&&s.getArtist().equalsIgnoreCase(artist)){
                return true;
            }
        }
        return false;
    }
    public ArrayList<Song>getLib(){
        return Lib;
    }
    public Song getSong(int index){
        return Lib.get(index);
    }
    public Song getSong(String name,String artist){
        for(Song s : Lib){
            if(s.getName().equalsIgnoreCase(name)&&s.getArtist().equalsIgnoreCase(artist)){
                return s;
            }
        }
        System.out.println("no song found");
        return null;
    }
    public void printLibrary(){
        for(Song s: Lib){
            System.out.println("Song: "+s.getName()+" Artist: "+s.getArtist());
        }
    }
}
