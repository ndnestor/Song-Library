/**
 * @author Abdullah Alam, Nathan Nestor
 */
package songlib.view;

import songlib.app.Library;
import songlib.app.Song;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

public class SongLibraryController {
    @FXML private ListView<String> songList;
    @FXML private ListView<String> detailsList;
    @FXML private TextField newValueTextField;
    @FXML private Button button;

    private final String defaultNewValueTextFieldPrompt = "Select a song and a property to edit";
    private final String addSongText = "> Add song <";

    private Song lastSelectedSong;
    private Song selectedSong;
    private Song templateSong;

    @FXML
    private void initialize() {
        System.out.println("Window loaded!");

        setTemplateSong();

        updateSongList();

        // NOTE: > 1 instead of > 0 because the song list includes the add song button
        if(songList.getItems().size() > 1) {
            songList.getSelectionModel().selectFirst();
            showDetails(Library.getSong(0));
        }

        songList.setOnMouseClicked(mouseEvent -> {
            if(selectedSong != lastSelectedSong) {
                newValueTextField.setPromptText(defaultNewValueTextFieldPrompt);
                newValueTextField.setText("");
                newValueTextField.setDisable(true);
            }

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

            showDetails(songFromString(selectedItem));

            button.setDisable(false);
            button.setText("Delete");
        });

        detailsList.setOnMouseClicked(mouseEvent -> {
            String selectedItem = detailsList.getSelectionModel().getSelectedItem();

            if(selectedItem == null) {
                newValueTextField.setPromptText(defaultNewValueTextFieldPrompt);
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

        newValueTextField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue)
                onNewValueTextFieldChanged();
        }));

        newValueTextField.setOnAction(actionEvent -> {
            //onNewValueTextFieldChanged();
        });

        button.setOnAction(actionEvent -> {
            switch(button.getText()) {
                case "Delete" -> {
                    if(!displayConfirmation("Are you sure you want to delete this song?"))
                        return;

                    int songIndex = songList.getSelectionModel().getSelectedIndex();

                    Library.delete(selectedSong.getName(), selectedSong.getArtist());

                    newValueTextField.setPromptText(defaultNewValueTextFieldPrompt);
                    newValueTextField.setText("");
                    newValueTextField.setDisable(true);

                    updateSongList();

                    lastSelectedSong = selectedSong;
                    if(songList.getItems().size() > songIndex + 1) {
                        songList.getSelectionModel().select(songIndex);
                        selectedSong = Library.Lib.get(songIndex);
                    } else if(songIndex > 0) {
                        songList.getSelectionModel().select(songIndex - 1);
                        selectedSong = songFromString(songList.getSelectionModel().getSelectedItem());
                    } else {
                        selectedSong = null;
                        button.setDisable(true);
                    }

                    showDetails(selectedSong);
                }
                case "Add" -> {
                    if(!displayConfirmation("Are you sure you want to add this song?"))
                        return;

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

                    int songIndex = getSongIndex(songName, songArtist);

                    songList.getSelectionModel().select(songIndex);
                    lastSelectedSong = selectedSong;
                    selectedSong = templateSong;
                    showDetails(selectedSong);

                    button.setText("Delete");

                    setTemplateSong();
                }
            }
        });
    }

    private int getSongIndex(String songName, String artistName) {
        try {
            return Library.Lib.indexOf(songFromString(songName + " by\u0000 " + artistName));
        } catch (ArrayIndexOutOfBoundsException exception) {
            return -1;
        }
    }

    private Song songFromString(String songString) {
        String[] parts = songString.split(" by\u0000 ");
        String songName = parts[0];
        String artistName = parts[1];

        return Library.getSong(songName, artistName);
    }

    private void onNewValueTextFieldChanged() {
        if(selectedSong == null)
            return;

        boolean canDisplayConfirmation = selectedSong != templateSong;

        String input = newValueTextField.getText();
        String prevSongName = selectedSong.getName();
        String prevArtistName = selectedSong.getArtist();
        String prevAlbumName = selectedSong.getAlbum();
        int prevYear = selectedSong.getYear();
        switch(newValueTextField.getPromptText()) {
            case "Name" -> {
                if(selectedSong.getName().equals(input))
                    return;

                if(canDisplayConfirmation &&
                        !displayConfirmation("Are you sure you want to change the name of this song?")) {
                    newValueTextField.setText(prevSongName);
                    return;
                }

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
                if(selectedSong.getArtist().equals(input))
                    return;

                if(canDisplayConfirmation &&
                        !displayConfirmation("Are you sure you want to change the artist of this song?")) {
                    newValueTextField.setText(prevArtistName);
                    return;
                }

                if(!Library.allowEdit(prevSongName, prevArtistName, prevSongName, input)) {
                    displayError(
                            "Duplicate Song",
                            "A song with this name and artist already exists in the library"
                    );
                    return;
                }

                selectedSong.setArtist(input);
            }
            case "Album" -> {
                if(selectedSong.getAlbum().equals(input))
                    return;

                if(canDisplayConfirmation &&
                        !displayConfirmation("Are you sure you want to change the album of this song?")) {
                    newValueTextField.setText(selectedSong.getAlbum());
                    return;
                }

                selectedSong.setAlbum(input);
            }
            case "Year" -> {
                if(String.valueOf(selectedSong.getYear()).equals(input))
                    return;

                if(canDisplayConfirmation &&
                        !displayConfirmation("Are you sure you want to change the year of this song?")) {
                    int year = selectedSong.getYear();

                    if(year == 0)
                        newValueTextField.setText("");
                    else
                        newValueTextField.setText(String.valueOf(selectedSong.getYear()));

                    return;
                }

                try {
                    int year;
                    if(input.equals(""))
                        year = 0;
                    else
                        year = Integer.parseInt(input);

                    selectedSong.setYear(year);
                } catch (NumberFormatException exception) {
                    displayError(
                            "Invalid Year",
                            "The year must only be comprised of numeric characters"
                    );
                }
            }
            default -> System.out.println("Something went wrong...");
        }

        updateSongList();
        showDetails(selectedSong);

        int songIndex = getSongIndex(selectedSong.getName(), selectedSong.getArtist());

        if(songIndex > -1)
            songList.getSelectionModel().select(songIndex);
        else
            songList.getSelectionModel().selectLast();
    }

    private void setTemplateSong() {
        templateSong = new Song("", "", "", 0);
    }

    private void updateSongList() {
        Library.sort();

        songList.getItems().clear();

        for(Song song : Library.Lib) {
            songList.getItems().add(song.getName() + " by\u0000 " + song.getArtist());
        }

        songList.getItems().add(addSongText);
    }

    private void showDetails(Song song) {
        lastSelectedSong = selectedSong;
        selectedSong = song;

        detailsList.getItems().clear();

        if(selectedSong == null)
            return;

        detailsList.getItems().add("Name:\u0000 " + selectedSong.getName());
        detailsList.getItems().add("Artist:\u0000 " + selectedSong.getArtist());
        detailsList.getItems().add("Album:\u0000 " + selectedSong.getAlbum());
        int year = selectedSong.getYear();
        if(year == 0)
            detailsList.getItems().add("Year:\u0000 ");
        else
            detailsList.getItems().add("Year:\u0000 " + selectedSong.getYear());
    }

    private void displayError(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private boolean displayConfirmation(String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }
}
