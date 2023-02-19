/**
 * @author Abdullah Alam, Nathan Nestor
 */

package songlib.app;

import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Library {
    public static ArrayList<Song> Lib = new ArrayList<Song>();

    //check before inserting
    public static boolean allowInsert(String name, String artist){
        for(Song s: Lib){
            if(s.getName().equalsIgnoreCase(name)&&s.getArtist().equalsIgnoreCase(artist)){
                return false;
            }
        }
        return true;
    }

    public static void addSong(String name, String artist){
        if(allowInsert(name, artist)){
            String name1 = name.replace("|","");
            String artist1 = artist.replace("|","");
            Lib.add(new Song(name1.trim(),artist1.trim()));
            Collections.sort(Lib);
            writeFile();
            return;
        }
        System.out.println("Duplicate Song");
    }
    public static void addSong(String name, String artist, String album, int year){
        if(allowInsert(name, artist)){
            name = name.replace("|","");
            artist = artist.replace("|","");
            album = album.replace("|","");
            Lib.add(new Song(name.trim(),artist.trim(),album,year));
            Collections.sort(Lib);
            writeFile();
            return;
        }
        System.out.println("Duplicate Song");
    }
    public static void delete(String name, String artist){
        Lib.removeIf(s -> s.getName().equalsIgnoreCase(name) && s.getArtist().equalsIgnoreCase(artist));
        writeFile();
    }

    //check before editing
    public static boolean allowEdit(String prevName, String prevArtist, String newName, String newArtist ){
        return allowInsert(newName,newArtist);
    }

    public static void editSong(String prevName, String prevArtist, String newName, String newArtist ){
        editSong(prevName, prevArtist, newName, newArtist, "", 0);
    }
    public static void editSong(String prevName, String prevArtist, String newName, String newArtist, String album, int year ){
        delete(prevName,prevArtist);
        addSong(newName,newArtist,album,year);
    }
    public static void sort() {
        Collections.sort(Lib);
    }

    public static Song getSong(int index){
        return Lib.get(index);
    }
    public static Song getSong(String name,String artist){
        for(Song s : Lib){
            if(s.getName().equalsIgnoreCase(name)&&s.getArtist().equalsIgnoreCase(artist)){
                return s;
            }
        }
        System.out.println("no song found");
        return null;
    }
    public static void createFile(){
        File data = new File("Data.txt");
        try {
            if (data.createNewFile()) {
                System.out.println("File created: " + data.getName());
            } else {
                System.out.println("File already exists.");
            }
        }catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
        }
    }
    public static void writeFile(){
        createFile();
        try{
            FileWriter writer = new FileWriter("Data.txt");
            for(Song s :Lib){
                writer.write(s.getName()+"'|'"+s.getArtist()+"'|'"+s.getAlbum()+"'|'"+s.getYear()+"\n");
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void readFile(){
        try {
            File data = new File("Data.txt");
            Scanner reader = new Scanner(data);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] objAttributes = line.split("'|'",7);
                Lib.add(new Song(objAttributes[0],objAttributes[2],objAttributes[4].equals("null")?null:objAttributes[4],objAttributes[6].equals("0")?0:Integer.parseInt(objAttributes[6])));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void printLibrary(){
        for(Song s: Lib){
            System.out.println(s.toString());
        }
    }
}
