import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;

public class NewFile {
    private NewFileGridPane view;
    private File selectedFile;
    private ReleaseFormat format;

    public NewFile() {
        view = new NewFileGridPane();
    }

    public NewFileGridPane getView() {
        return view;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public ReleaseFormat getFormat() {
        return format;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public void setFormat(ReleaseFormat format) {
        this.format = format;
    }

    public String getExtension() {
        if(selectedFile != null) {
            String fileName = selectedFile.getName();
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return null;
    }

    @Override
    public String toString() {
        return(selectedFile.getPath() + ", " + format);
    }
}
