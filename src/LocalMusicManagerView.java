import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.ArrayList;

public class LocalMusicManagerView extends GridPane {
    public ArrayList<NewFile> model;
    public GridPane top;
    public Label title;
    public GridPane middle;
    public ScrollPane middleScrollPane;
    public GridPane bottom;
    private Button addToQueue;
    private Button addToLibrary;
    private Button addToItunes;
    private Button convertToAAC;
    private Button convertToMP3;
    private Button relocateAAC;
    private Button relocateMP3;
    private Button completeAllActions;

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
        middle.setMinWidth(1070);
        middle.setMaxWidth(1070);
        middleScrollPane = new ScrollPane();
        middleScrollPane.setPadding(new Insets(10,10,10,10));
        middleScrollPane.setFitToWidth(true);
        middleScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        middleScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        middleScrollPane.setMinWidth(1090);
        middleScrollPane.setMaxWidth(1090);
        middleScrollPane.setMinHeight(300);
        middleScrollPane.setMaxHeight(300);
        middleScrollPane.setContent(middle);
        add(middleScrollPane, 0, 1);

        //BOTTOM COMPONENTS
        bottom = new GridPane();
        bottom.setHgap(10);
        bottom.setVgap(10);
        addToQueue = new Button("ADD TO QUEUE");
        addToQueue.setMinWidth(195);
        addToQueue.setMaxWidth(195);
        bottom.add(addToQueue, 0, 0, 1, 1);
        addToLibrary = new Button("ADD TO LIBRARY");
        addToLibrary.setMinWidth(885);
        addToLibrary.setMaxWidth(885);
        bottom.add(addToLibrary, 1, 0, 2, 1);
        addToLibrary.setDisable(true);
        addToItunes = new Button("ADD TO iTUNES");
        addToItunes.setDisable(true);
        addToItunes.setMinWidth(1090);
        addToItunes.setMaxWidth(1090);
        bottom.add(addToItunes, 0, 1, 7, 1);

        GridPane convertAndRelocate = new GridPane();
        convertAndRelocate.setHgap(10);
        convertToAAC = new Button("CONVERT TO AAC");
        convertToAAC.setDisable(true);
        convertToAAC.setMinWidth(265);
        convertToAAC.setMaxWidth(265);
        convertAndRelocate.add(convertToAAC, 0, 0);
        relocateAAC = new Button("RELOCATE AAC");
        relocateAAC.setDisable(true);
        relocateAAC.setMinWidth(265);
        relocateAAC.setMaxWidth(265);
        convertAndRelocate.add(relocateAAC, 1, 0);
        convertToMP3 = new Button("CONVERT TO MP3");
        convertToMP3.setDisable(true);
        convertToMP3.setMinWidth(265);
        convertToMP3.setMaxWidth(265);
        convertAndRelocate.add(convertToMP3, 2, 0);
        relocateMP3 = new Button("RELOCATE MP3");
        relocateMP3.setDisable(true);
        relocateMP3.setMinWidth(265);
        relocateMP3.setMaxWidth(265);
        convertAndRelocate.add(relocateMP3, 3, 0);
        bottom.add(convertAndRelocate, 0, 2, 7, 1);

        completeAllActions = new Button("COMPLETE ALL ACTIONS");
        completeAllActions.setMinWidth(1090);
        completeAllActions.setDisable(true);
        bottom.add(completeAllActions, 0, 3, 7, 1);

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
    public Button getCompleteAllActions() {
        return completeAllActions;
    }

    public void update() {
        middle = new GridPane();
        middle.setVgap(10);
        for(NewFile nf: model) {
            middle.add(nf.getView(), 0, middle.getChildren().size());
        }
        middle.setMinWidth(1070);
        middle.setMaxWidth(1070);
        middleScrollPane = new ScrollPane();
        middleScrollPane.setPadding(new Insets(10,10,10,10));
        middleScrollPane.setFitToWidth(true);
        middleScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        middleScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        middleScrollPane.setMinWidth(1090);
        middleScrollPane.setMaxWidth(1090);
        middleScrollPane.setMinHeight(300);
        middleScrollPane.setMaxHeight(300);
        middleScrollPane.setContent(middle);
        add(middleScrollPane, 0, 1);

        if(model.size() == 0) {
            addToLibrary.setDisable(true);
            completeAllActions.setDisable(true);
        }
        else {
            addToLibrary.setDisable(false);
            completeAllActions.setDisable(false);
            for (NewFile nf : model) {
                if (nf.getSelectedFile() == null || nf.getFormat() == null) {
                    addToLibrary.setDisable(true);
                    completeAllActions.setDisable(true);
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

    public void disableAll() {
        addToQueue.setDisable(true);
        addToLibrary.setDisable(true);
        addToItunes.setDisable(true);
        convertToAAC.setDisable(true);
        convertToMP3.setDisable(true);
        relocateAAC.setDisable(true);
        relocateMP3.setDisable(true);
        completeAllActions.setDisable(true);
    }
}
