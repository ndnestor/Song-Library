package songlib.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SongLib extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/songlib/view/SongLibrary.fxml"));

        BorderPane root = (BorderPane)loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

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

        launch(args);
    }

}
