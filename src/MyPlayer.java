import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class MyPlayer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        playMp3("path/to/your/soundtrack.mp3");
    }

    private void playMp3(String filePath) {
        String uriString = new File(filePath).toURI().toString();
        Media media = new Media(uriString);
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnEndOfMedia(() -> {
            // You can add any custom logic here when the playback finishes
            mediaPlayer.stop();
        });

        mediaPlayer.play();
    }
}
