import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;

public class NewFile {
    private NewFileGridPane view;
    private File selectedFile;

    public NewFile() {
        view = new NewFileGridPane();

        view.getBrowse().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedFile = view.getChooser().showOpenDialog(null);
                if(selectedFile != null) {
                    view.getSelectedFilePath().setText(selectedFile.getPath());
                    view.getFormat().setDisable(false);
                }
            }
        });
    }

    public NewFileGridPane getView() {
        return view;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

}
