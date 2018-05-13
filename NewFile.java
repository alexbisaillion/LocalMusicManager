import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;

public class NewFile {
    private GridPane uiView;
    private Button browse;
    private FileChooser chooser;
    private File selectedFile;
    private TextField selectedFilePath;
    private ComboBox<ReleaseFormat> format;

    public NewFile() {
        uiView = new GridPane();
        uiView.setHgap(10);
        uiView.setVgap(10);

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
        format.getItems().setAll(ReleaseFormat.values());
        format.setDisable(true);
        format.setMinWidth(100);

        uiView.add(browse, 0, 0);
        uiView.add(selectedFilePath, 1, 0);
        uiView.add(format, 2, 0);

        browse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedFile = chooser.showOpenDialog(null);
                if(selectedFile != null) {
                    selectedFilePath.setText(selectedFile.getPath());
                    format.setDisable(false);
                }
            }
        });
    }

    public GridPane getUiView() {
        return uiView;
    }
}
