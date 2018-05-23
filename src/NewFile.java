import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;

public class NewFile {
    private NewFileGridPane view;
    private File selectedFile;
    private ReleaseFormat format;
    private String newArtistTag;
    private String newAlbumArtistTag;
    private String newGenreTag;

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

    public String getNewArtistTag() {
        return newArtistTag;
    }

    public String getNewAlbumArtistTag() {
        return newAlbumArtistTag;
    }

    public String getNewGenreTag() {
        return newGenreTag;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public void setFormat(ReleaseFormat format) {
        this.format = format;
    }

    public void setNewArtistTag(String newArtistTag) {
        this.newArtistTag = newArtistTag;
    }

    public void setNewAlbumArtistTag(String newAlbumArtistTag) {
        this.newAlbumArtistTag = newAlbumArtistTag;
    }

    public void setNewGenreTag(String newGenreTag) {
        this.newGenreTag = newGenreTag;
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
