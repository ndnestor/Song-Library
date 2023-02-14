package f2c.app;
public class BackendMain {
    public static void main(String[] args) {
        Song s1 = new Song("coolSong","elon");
        System.out.println(s1.getName());
        Library MySongs = new Library();
        MySongs.addSong("asong1", "artist1");
        MySongs.addSong("csong1", "artist2");
        MySongs.addSong("bsong2", "artist1");
        MySongs.addSong("dsong2", "artist2");
        MySongs.delete("csong1","artist2");
        MySongs.printLibrary();
    }

}
