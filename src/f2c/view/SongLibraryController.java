package f2c.view;

import f2c.app.Library;
import f2c.app.Song;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SongLibraryController {
    @FXML private ListView<String> songList;
    @FXML private ListView<String> detailsList;
    @FXML private TextField newValueTextField;
    @FXML private Button button;

    private final String addSongText = "> Add song <";

    private Song selectedSong;
    private Song templateSong;

    @FXML
    private void initialize() {
        System.out.println("Window loaded!");

        setTemplateSong();

        Library.addSong("Song B", "Artist B", "Album B", 2022);
        Library.addSong("Song A", "Artist A", "Album A", 2023);
        Library.addSong("Song B", "Artist B", "Album B", 2022);

        updateSongList();

        songList.setOnMouseClicked(mouseEvent -> {
            String selectedItem = songList.getSelectionModel().getSelectedItem();

            if(selectedItem == null) {
                showDetails(null);

                button.setDisable(true);
                return;
            } else if(selectedItem.equals(addSongText)) {
                showDetails(templateSong);

                button.setDisable(false);
                button.setText("Add");
                return;
            }

            String[] parts = selectedItem.split(" by\u0000 ");
            String songName = parts[0];
            String artistName = parts[1];
            showDetails(songName, artistName);

            button.setDisable(false);
            button.setText("Delete");
        });

        detailsList.setOnMouseClicked(mouseEvent -> {
            String selectedItem = detailsList.getSelectionModel().getSelectedItem();

            if(selectedItem == null) {
                newValueTextField.setPromptText("Select a song and a property to edit");
                newValueTextField.setText("");
                newValueTextField.setDisable(true);
                return;
            }

            String[] parts = selectedItem.split(":\u0000 ");
            String key = parts[0];
            String value;
            if(parts.length > 1)
                value = parts[1];
            else
                value = "";

            newValueTextField.setPromptText(key);
            newValueTextField.setText(value);
            newValueTextField.setDisable(false);
        });

        newValueTextField.setOnAction(actionEvent -> {
            if(selectedSong == null)
                return;

            String input = newValueTextField.getText();
            String prevSongName = selectedSong.getName();
            String prevArtistName = selectedSong.getArtist();
            switch(newValueTextField.getPromptText()) {
                case "Name" -> {
                    if(!Library.allowEdit(prevSongName, prevArtistName, input, prevArtistName)) {
                        displayError(
                                "Duplicate Song",
                                "A song with this name and artist already exists in the library"
                        );
                        return;
                    }
                    selectedSong.setName(input);
                }
                case "Artist" -> {
                    if(!Library.allowEdit(prevSongName, prevArtistName, prevSongName, input)) {
                        displayError(
                                "Duplicate Song",
                                "A song with this name and artist already exists in the library"
                        );
                        return;
                    }
                    selectedSong.setArtist(input);
                }
                case "Album" -> selectedSong.setAlbum(input);
                case "Year" -> {
                    try {
                        selectedSong.setYear(Integer.parseInt(input));
                    } catch (NumberFormatException exception) {
                        displayError(
                                "Invalid Year",
                                "The year must only be comprised of numeric characters"
                        );
                    }
                }
                default -> System.out.println("Something went wrong...");
            }

            showDetails(selectedSong);
        });

        button.setOnAction(actionEvent -> {
            switch(button.getText()) {
                case "Delete" -> {
                    Library.delete(selectedSong.getName(), selectedSong.getArtist());

                    newValueTextField.setPromptText("Select a song and a property to edit");
                    newValueTextField.setText("");
                    newValueTextField.setDisable(true);

                    updateSongList();
                    showDetails(null);

                    button.setDisable(true);
                }
                case "Add" -> {
                    String songName = templateSong.getName();
                    String songArtist = templateSong.getArtist();

                    if(songName.equals("") || songArtist.equals("")) {
                        displayError(
                                "Missing Required Fields",
                                "Songs cannot be added without a specified name and artist"
                        );
                        return;
                    }

                    Library.addSong(
                        songName,
                        songArtist,
                        templateSong.getAlbum(),
                        templateSong.getYear()
                    );

                    updateSongList();

                    int songIndex = songList
                            .getItems()
                            .indexOf(songName + " by\u0000 " + songArtist);

                    songList.getSelectionModel().select(songIndex);

                    selectedSong = templateSong;
                    showDetails(selectedSong);

                    button.setText("Delete");

                    setTemplateSong();
                }
            }
        });
    }

    private void setTemplateSong() {
        templateSong = new Song("", "", "", 0);
    }

    private void updateSongList() {
        songList.getItems().clear();

        for(Song song : Library.Lib) {
            songList.getItems().add(song.getName() + " by\u0000 " + song.getArtist());
        }

        songList.getItems().add(addSongText);
    }

    private void showDetails(String songName, String artistName) {
        showDetails(Library.getSong(songName, artistName));
    }

    private void showDetails(Song song) {
        selectedSong = song;

        detailsList.getItems().clear();

        if(selectedSong == null)
            return;

        detailsList.getItems().add("Name:\u0000 " + selectedSong.getName());
        detailsList.getItems().add("Artist:\u0000 " + selectedSong.getArtist());
        detailsList.getItems().add("Album:\u0000 " + selectedSong.getAlbum());
        detailsList.getItems().add("Year:\u0000 " + selectedSong.getYear());
    }

    private void displayError(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
