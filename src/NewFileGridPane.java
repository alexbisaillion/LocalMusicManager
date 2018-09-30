import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Paths;

public class NewFileGridPane extends GridPane {
    private Button browse;
    private FileChooser chooser;
    private TextField selectedFilePath;
    private ComboBox<ReleaseFormat> format;
    private TextField artist;
    private TextField albumArtist;
    private TextField genre;
    private Button delete;

    public NewFileGridPane() {
        setHgap(10);
        setVgap(10);

        browse = new Button("Browse");
        browse.setPrefWidth(60);

        chooser = new FileChooser();
        chooser.setInitialDirectory((Paths.get("C:\\Users\\abisa\\Downloads")).toFile());
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Audio/Archive Files,", "*.m4a", "*.mp3", "*.aac", "*.wav", "*.aiff", "*.zip", "*.rar", "*.7z"));

        selectedFilePath = new TextField();
        selectedFilePath.setPrefWidth(400);
        selectedFilePath.setEditable(false);
        selectedFilePath.setDisable(true);

        format = new ComboBox<ReleaseFormat>();
        format.setDisable(true);
        format.setPrefWidth(100);

        artist = new TextField();
        artist.setPromptText("Artist");
        artist.setPrefWidth(125);

        albumArtist = new TextField();
        albumArtist.setPromptText("Album Artist");
        albumArtist.setPrefWidth(125);

        genre = new TextField();
        genre.setPromptText("Genre");
        genre.setPrefWidth(125);

        delete = new Button("DELETE");
        delete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        delete.setPrefWidth(60);

        add(browse, 0, 0);
        add(selectedFilePath, 1, 0);
        add(format, 2, 0);
        add(artist, 3, 0);
        add(albumArtist, 4, 0);
        add(genre, 5, 0);
        add(delete, 6, 0);

        setMinWidth(1070);
        setMaxWidth(1070);
    }

    public Button getBrowse() {
        return browse;
    }

    public FileChooser getChooser() {
        return chooser;
    }

    public TextField getSelectedFilePath() {
        return selectedFilePath;
    }

    public ComboBox<ReleaseFormat> getFormat() {
        return format;
    }

    public TextField getArtist() {
        return artist;
    }

    public TextField getAlbumArtist() {
        return albumArtist;
    }

    public TextField getGenre() {
        return genre;
    }

    public Button getDelete() {
        return delete;
    }

}
