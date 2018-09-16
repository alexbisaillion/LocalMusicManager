import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.ArrayList;

public class LocalMusicManagerView extends GridPane {
    public ArrayList<NewFile> model;
    public GridPane top;
    public Label title;
    public GridPane middle;
    public GridPane bottom;
    private Button addToQueue;
    private Button addToLibrary;
    private Button addToItunes;
    private Button convertToAAC;
    private Button convertToMP3;
    private Button relocateAAC;
    private Button relocateMP3;
    private Button cleanUpItunes;

    public LocalMusicManagerView(ArrayList<NewFile> m) {
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10,10,10,10));

        model = m;

        //TOP COMPONENTS
        top = new GridPane();
        title = new Label("LOCAL MUSIC MANAGER");
        top.add(title, 0, 0, 3, 1);
        add(top, 0, 0);

        //MIDDLE COMPONENTS
        middle = new GridPane();
        middle.setVgap(10);
        for(NewFile nf: model) {
            middle.add(nf.getView(), 0, middle.getChildren().size());
        }
        add(middle, 0, 1);

        //BOTTOM COMPONENTS
        bottom = new GridPane();
        bottom.setHgap(10);
        bottom.setVgap(10);
        addToQueue = new Button("ADD TO QUEUE");
        addToQueue.setMinWidth(195);
        addToQueue.setMaxWidth(195);
        bottom.add(addToQueue, 0, 0, 1, 1);
        addToLibrary = new Button("ADD TO LIBRARY");
        addToLibrary.setMinWidth(850);
        addToLibrary.setMaxWidth(850);
        bottom.add(addToLibrary, 1, 0, 2, 1);
        addToLibrary.setDisable(true);
        addToItunes = new Button("ADD TO iTUNES");
        addToItunes.setDisable(true);
        addToItunes.setMinWidth(1055);
        addToItunes.setMaxWidth(1055);
        bottom.add(addToItunes, 0, 1, 7, 1);

        GridPane convertAndRelocate = new GridPane();
        convertAndRelocate.setHgap(10);
        convertToAAC = new Button("CONVERT TO AAC");
        convertToAAC.setDisable(true);
        convertToAAC.setMinWidth(256);
        convertToAAC.setMaxWidth(256);
        convertAndRelocate.add(convertToAAC, 0, 0);
        relocateAAC = new Button("RELOCATE AAC");
        relocateAAC.setDisable(true);
        relocateAAC.setMinWidth(256);
        relocateAAC.setMaxWidth(256);
        convertAndRelocate.add(relocateAAC, 1, 0);
        convertToMP3 = new Button("CONVERT TO MP3");
        convertToMP3.setDisable(true);
        convertToMP3.setMinWidth(256);
        convertToMP3.setMaxWidth(256);
        convertAndRelocate.add(convertToMP3, 2, 0);
        relocateMP3 = new Button("RELOCATE MP3");
        relocateMP3.setDisable(true);
        relocateMP3.setMinWidth(256);
        relocateMP3.setMaxWidth(256);
        convertAndRelocate.add(relocateMP3, 3, 0);
        bottom.add(convertAndRelocate, 0, 2, 7, 1);

        cleanUpItunes = new Button("CLEAN UP iTUNES");
        cleanUpItunes.setDisable(true);
        cleanUpItunes.setMinWidth(1055);
        bottom.add(cleanUpItunes, 0, 3, 7, 1);

        add(bottom, 0, 2);
    }

    public Button getAddToQueue() { return addToQueue; }
    public Button getAddToLibrary() { return addToLibrary; }
    public Button getAddToItunes() { return addToItunes; }
    public Button getConvertToAAC() {
        return convertToAAC;
    }
    public Button getConvertToMP3() {
        return convertToMP3;
    }
    public Button getRelocateAAC() {
        return relocateAAC;
    }
    public Button getRelocateMP3() {
        return relocateMP3;
    }
    public Button getCleanUpItunes() {
        return cleanUpItunes;
    }

    public void update() {
        middle = new GridPane();
        middle.setVgap(10);
        for(NewFile nf: model) {
            middle.add(nf.getView(), 0, middle.getChildren().size());
        }
        add(middle, 0, 1);

        if(model.size() == 0) {
            addToLibrary.setDisable(true);
        }
        else {
            addToLibrary.setDisable(false);
            for (NewFile nf : model) {
                if (nf.getSelectedFile() == null || nf.getFormat() == null) {
                    addToLibrary.setDisable(true);
                    break;
                }
            }
        }

        for(NewFile nf: model) {
            nf.getView().getBrowse().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    File selectedFile = nf.getView().getChooser().showOpenDialog(null);
                    if(selectedFile != null) {
                        nf.setSelectedFile(selectedFile);
                        nf.getView().getSelectedFilePath().setText(selectedFile.getPath());
                        nf.getView().getFormat().setDisable(false);
                        if(!nf.getExtension().equals(".zip") && !nf.getExtension().equals(".rar") && !nf.getExtension().equals(".7z")) {
                            nf.getView().getFormat().getItems().setAll(ReleaseFormat.Single, ReleaseFormat.Unreleased);
                        }
                        else {
                            nf.getView().getFormat().getItems().setAll(ReleaseFormat.values());
                        }
                    }
                    update();
                }
            });

            nf.getView().getFormat().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(nf.getView().getFormat().getValue() != null) {
                        ReleaseFormat format = nf.getView().getFormat().getValue();
                        nf.setFormat(format);
                    }
                    update();
                }
            });

            nf.getView().getArtist().setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(nf.getView().getArtist().getText().length() > 0) {
                        nf.setNewArtistTag(nf.getView().getArtist().getText());
                    }
                    update();
                }
            });

            nf.getView().getAlbumArtist().setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(nf.getView().getAlbumArtist().getText().length() > 0) {
                        nf.setNewAlbumArtistTag(nf.getView().getAlbumArtist().getText());
                    }
                    update();
                }
            });

            nf.getView().getGenre().setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(nf.getView().getGenre().getText().length() > 0) {
                        nf.setNewGenreTag(nf.getView().getGenre().getText());
                    }
                    update();
                }
            });

            nf.getView().getDelete().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    nf.getView().getChildren().clear();
                    model.remove(nf);
                    update();
                }
            });

        }
    }
}
