package f2c.view;

import f2c.app.Library;
import f2c.app.Song;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SongLibraryController {
    @FXML private ListView<String> songList;
    @FXML private ListView<String> detailsList;
    @FXML private TextField newValueTextField;

    private Song selectedSong;

    @FXML
    private void initialize() {
        System.out.println("Window loaded!");

        Library.addSong("Song B", "Artist B", "Album B", 2022);
        Library.addSong("Song A", "Artist A", "Album A", 2023);
        Library.addSong("Song B", "Artist B", "Album B", 2022);

        for(Song song : Library.Lib) {
            songList.getItems().add(song.getName() + " by\u0000 " + song.getArtist());
        }

        songList.setOnMouseClicked(mouseEvent -> {
            String selectedItem = songList.getSelectionModel().getSelectedItem();
            String[] parts = selectedItem.split(" by\u0000 ");
            String songName = parts[0];
            String artistName = parts[1];
            showDetails(songName, artistName);
        });

        detailsList.setOnMouseClicked(mouseEvent -> {
            String selectedItem = detailsList.getSelectionModel().getSelectedItem();
            String[] parts = selectedItem.split(":\u0000 ");
            String key = parts[0];
            String value = parts[1];

            newValueTextField.setPromptText(key);
            newValueTextField.setText(value);
        });

        newValueTextField.setOnAction(actionEvent -> {
            String input = newValueTextField.getText();
            String prevSongName = selectedSong.getName();
            String prevArtistName = selectedSong.getArtist();
            switch(newValueTextField.getPromptText()) {
                case "Name" -> {
                    if(!Library.allowEdit(prevSongName, prevArtistName, input, prevArtistName)) {
                        // TODO: Display a popup message
                        return;
                    }
                    selectedSong.setName(input);
                }
                case "Artist" -> {
                    if(!Library.allowEdit(prevSongName, prevArtistName, prevSongName, input)) {
                        // TODO: Display a popup message
                        return;
                    }
                    selectedSong.setArtist(input);
                }
                case "Album" -> selectedSong.setAlbum(input);
                case "Year" -> {
                    try {
                        selectedSong.setYear(Integer.parseInt(input));
                    } catch (NumberFormatException exception) {
                        // TODO: Display a popup message
                    }
                }
                default -> System.out.println("Something went wrong...");
            }

            showDetails(selectedSong);
        });
    }

    private void showDetails(String songName, String artistName) {
        showDetails(Library.getSong(songName, artistName));
    }

    private void showDetails(Song song) {
        selectedSong = song;

        detailsList.getItems().clear();

        detailsList.getItems().add("Name:\u0000 " + selectedSong.getName());
        detailsList.getItems().add("Artist:\u0000 " + selectedSong.getArtist());
        detailsList.getItems().add("Album:\u0000 " + selectedSong.getAlbum());
        detailsList.getItems().add("Year:\u0000 " + selectedSong.getYear());
    }
}
