package f2c.app;
public class Song {
    private String name;
    private String artist;
    private String album;
    private int year;

    public Song(String name, String artist){
        this.name = name;
        this.artist = artist;
    }
    public Song(String name, String artist, String album, int year){
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
    }

    // Getter and Setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter methods for artist
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    // Getter and Setter methods for album
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    // Getter and Setter methods for year
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public String toString(){
        return "song name: "+name+" artist: "+ artist;
    }


}

