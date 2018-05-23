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
    private Button finish;
    private Button addToItunes;

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
        bottom.add(addToQueue, 0, 0, 1, 1);
        finish = new Button("ADD TO LIBRARY");
        finish.setMinWidth(720);
        bottom.add(finish, 1, 0, 2, 1);
        finish.setDisable(true);
        addToItunes = new Button("ADD TO iTUNES");
        addToItunes.setMinWidth(925);
        bottom.add(addToItunes, 0, 1, 3, 1);
        add(bottom, 0, 2);
    }

    public Button getAddToQueue() { return addToQueue; }
    public Button getFinish() { return finish; }
    public Button getAddToItunes() { return addToItunes; }

    public void update() {
        middle = new GridPane();
        middle.setVgap(10);
        for(NewFile nf: model) {
            middle.add(nf.getView(), 0, middle.getChildren().size());
        }
        add(middle, 0, 1);

        if(model.size() == 0) {
            finish.setDisable(true);
        }
        else {
            finish.setDisable(false);
            for (NewFile nf : model) {
                if (nf.getSelectedFile() == null || nf.getFormat() == null) {
                    finish.setDisable(true);
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
        }
    }
}
