package f2c.view;

import f2c.app.Library;
import f2c.app.Song;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class SongLibraryController {
    @FXML private ListView<String> songList;
    @FXML private ListView<String> detailsList;

    @FXML
    private void initialize() {
        System.out.println("Window loaded!");

        Library.addSong("Song B", "Artist B", "Album B", 2022);
        Library.addSong("Song A", "Artist A", "Album A", 2023);
        Library.addSong("Song B", "Artist B", "Album B", 2022);

        for(Song song : Library.Lib) {
            songList.getItems().add(song.getName() + " by " + song.getArtist());
        }

        //songList.getItems().add("Song A by Artist A");
        //songList.getItems().add("Song B by Artist B");

        songList.setOnMouseClicked(mouseEvent -> {
            String selectedItem = songList.getSelectionModel().getSelectedItem();
            String[] parts = selectedItem.split(" by ");
            String songName = parts[0];
            String artistName = parts[1];
            showDetails(songName, artistName);
        });
    }

    private void showDetails(String songName, String artistName) {
        Song song = Library.getSong(songName, artistName);

        detailsList.getItems().clear();

        detailsList.getItems().add("Name: " + songName);
        detailsList.getItems().add("Artist: " + artistName);
        detailsList.getItems().add("Album: " + song.getAlbum());
        detailsList.getItems().add("Year: " + song.getYear());
    }
}
