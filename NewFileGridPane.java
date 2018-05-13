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

        add(browse, 0, 0);
        add(selectedFilePath, 1, 0);
        add(format, 2, 0);
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
}
