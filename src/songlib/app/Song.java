package songlib.app;
public class Song implements Comparable<Song>{
    private String name;
    private String artist;
    private String album;
    private int year;

    public Song(String name, String artist){
        this.name = name;
        this.artist = artist;
        this.album = "";
        this.year = 0;
    }
    public Song(String name, String artist, String album, int year){
        this.name = name;
        this.artist = artist;
        this.album = album;
        if(year>0){
            this.year = year;
        }
    }

    // Getter and Setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Library.writeFile();
    }

    // Getter and Setter methods for artist
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
        Library.writeFile();
    }

    // Getter and Setter methods for album
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
        Library.writeFile();
    }

    // Getter and Setter methods for year
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year>=0?year:-year;
        Library.writeFile();
    }

    public int compareTo(Song song2){
        int songName = this.name.compareToIgnoreCase(song2.getName());
        return songName==0? this.artist.compareToIgnoreCase(song2.getArtist()) : songName;
    }

    public String toString(){
        return "Song Name: " + name +" Artist: "+ artist + " Album: "+ album +" year: "+ year;
    }


}

