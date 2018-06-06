import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

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

        chooser = new FileChooser();
        browse = new Button("Browse");
        browse.setMinWidth(75);
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files,", "*.m4a", "*.mp3", "*.aac", "*.wav", "*.aiff"),
                new FileChooser.ExtensionFilter("Archive Files,", "*.zip", "*.rar", "*.7z"));
        selectedFilePath = new TextField();
        selectedFilePath.setMinWidth(400);
        selectedFilePath.setEditable(false);
        selectedFilePath.setDisable(true);
        format = new ComboBox<ReleaseFormat>();
        format.setDisable(true);
        format.setMinWidth(100);

        artist = new TextField();
        artist.setPromptText("Artist");
        artist.setMinWidth(100);

        albumArtist = new TextField();
        albumArtist.setPromptText("Album Artist");
        albumArtist.setMinWidth(100);

        genre = new TextField();
        genre.setPromptText("Genre");
        genre.setMinWidth(100);

        delete = new Button("DELETE");
        delete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        delete.setMinWidth(50);
        //delete.setDisable(true);

        add(browse, 0, 0);
        add(selectedFilePath, 1, 0);
        add(format, 2, 0);
        add(artist, 3, 0);
        add(albumArtist, 4, 0);
        add(genre, 5, 0);
        add(delete, 6, 0);
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
