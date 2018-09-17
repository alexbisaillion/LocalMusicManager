import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class SetupDialog extends Dialog {
    private GridPane gridPane = new GridPane();
    private Label albumLabel = new Label("Albums");
    private Label mixtapeLabel = new Label("Mixtapes");
    private Label epLabel = new Label("EPs");
    private Label singleLabel = new Label("Singles");
    private Label soundtrackLabel = new Label("Soundtracks");
    private Label unreleasedLabel = new Label("Unreleased");
    private Label aacLabel = new Label("AAC");
    private Label mp3Label = new Label("MP3");
    private Label phoneLabel = new Label("Phone");
    private Label iTunesLabel = new Label("iTunes Media Folder");
    private Button albumBrowse = new Button("BROWSE");
    private Button mixtapeBrowse = new Button("BROWSE");
    private Button epBrowse = new Button("BROWSE");
    private Button singleBrowse = new Button("BROWSE");
    private Button soundtrackBrowse = new Button("BROWSE");
    private Button unreleasedBrowse = new Button("BROWSE");
    private Button aacBrowse = new Button("BROWSE");
    private Button mp3Browse = new Button("BROWSE");
    private Button phoneBrowse = new Button("BROWSE");
    private Button iTunesBrowse = new Button("BROWSE");
    private TextField albumTextField;
    private TextField mixtapeTextField;
    private TextField epTextField;
    private TextField singleTextField;
    private TextField soundtrackTextField;
    private TextField unreleasedTextField;
    private TextField aacTextField;
    private TextField mp3TextField;
    private TextField phoneTextField;
    private TextField iTunesTextField;
    private DirectoryChooser albumDirectory = new DirectoryChooser();
    private DirectoryChooser mixtapeDirectory = new DirectoryChooser();
    private DirectoryChooser epDirectory = new DirectoryChooser();
    private DirectoryChooser singleDirectory = new DirectoryChooser();
    private DirectoryChooser soundtrackDirectory = new DirectoryChooser();
    private DirectoryChooser unreleasedDirectory = new DirectoryChooser();
    private DirectoryChooser aacDirectory = new DirectoryChooser();
    private DirectoryChooser mp3Directory = new DirectoryChooser();
    private DirectoryChooser phoneDirectory = new DirectoryChooser();
    private DirectoryChooser iTunesDirectory = new DirectoryChooser();
    private Path albumPath;
    private Path mixtapePath;
    private Path epPath;
    private Path singlePath;
    private Path soundtrackPath;
    private Path unreleasedPath;
    private Path aacPath;
    private Path mp3Path;
    private Path phonePath;
    private Path iTunesPath;

    //Setup for complete but faulty paths
    public SetupDialog(SetupModel setupModel) {
        setTitle("Setup");
        setHeaderText("You have a faulty setup file. Please fix before proceeding:");
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.add(albumLabel, 0, 0);
        gridPane.add(mixtapeLabel, 0, 1);
        gridPane.add(epLabel, 0, 2);
        gridPane.add(singleLabel, 0, 3);
        gridPane.add(soundtrackLabel, 0, 4);
        gridPane.add(unreleasedLabel, 0, 5);
        gridPane.add(aacLabel, 0, 6);
        gridPane.add(mp3Label, 0, 7);
        gridPane.add(phoneLabel, 0, 8);
        gridPane.add(iTunesLabel, 0, 9);

        gridPane.add(albumBrowse, 1, 0);
        gridPane.add(mixtapeBrowse, 1, 1);
        gridPane.add(epBrowse, 1, 2);
        gridPane.add(singleBrowse, 1, 3);
        gridPane.add(soundtrackBrowse, 1, 4);
        gridPane.add(unreleasedBrowse, 1, 5);
        gridPane.add(aacBrowse, 1, 6);
        gridPane.add(mp3Browse, 1, 7);
        gridPane.add(phoneBrowse, 1, 8);
        gridPane.add(iTunesBrowse, 1, 9);

        albumTextField = new TextField(setupModel.getFormatPaths().get(ReleaseFormat.Album).toString());
        albumTextField.setEditable(false);
        albumTextField.setDisable(true);
        albumTextField.setPrefWidth(400);
        if(!setupModel.getFormatPaths().get(ReleaseFormat.Album).toFile().canWrite()) {
            albumTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            albumPath = setupModel.getFormatPaths().get(ReleaseFormat.Album);
            albumTextField.setStyle("-fx-control-inner-background: green;");
        }

        mixtapeTextField = new TextField(setupModel.getFormatPaths().get(ReleaseFormat.Mixtape).toString());
        mixtapeTextField.setEditable(false);
        mixtapeTextField.setDisable(true);
        mixtapeTextField.setPrefWidth(400);
        if(!setupModel.getFormatPaths().get(ReleaseFormat.Mixtape).toFile().canWrite()) {
            mixtapeTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            mixtapePath = setupModel.getFormatPaths().get(ReleaseFormat.Mixtape);
            mixtapeTextField.setStyle("-fx-control-inner-background: green;");
        }

        epTextField = new TextField(setupModel.getFormatPaths().get(ReleaseFormat.EP).toString());
        epTextField.setEditable(false);
        epTextField.setDisable(true);
        epTextField.setPrefWidth(400);
        if(!setupModel.getFormatPaths().get(ReleaseFormat.EP).toFile().canWrite()) {
            epTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            epPath = setupModel.getFormatPaths().get(ReleaseFormat.EP);
            epTextField.setStyle("-fx-control-inner-background: green;");
        }

        singleTextField = new TextField(setupModel.getFormatPaths().get(ReleaseFormat.Single).toString());
        singleTextField.setEditable(false);
        singleTextField.setDisable(true);
        singleTextField.setPrefWidth(400);
        if(!setupModel.getFormatPaths().get(ReleaseFormat.Single).toFile().canWrite()) {
            singleTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            singlePath = setupModel.getFormatPaths().get(ReleaseFormat.Single);
            singleTextField.setStyle("-fx-control-inner-background: green;");
        }

        soundtrackTextField = new TextField(setupModel.getFormatPaths().get(ReleaseFormat.Soundtrack).toString());
        soundtrackTextField.setEditable(false);
        soundtrackTextField.setDisable(true);
        soundtrackTextField.setPrefWidth(400);
        if(!setupModel.getFormatPaths().get(ReleaseFormat.Soundtrack).toFile().canWrite()) {
            soundtrackTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            soundtrackPath = setupModel.getFormatPaths().get(ReleaseFormat.Soundtrack);
            soundtrackTextField.setStyle("-fx-control-inner-background: green;");
        }

        unreleasedTextField = new TextField(setupModel.getFormatPaths().get(ReleaseFormat.Unreleased).toString());
        unreleasedTextField.setEditable(false);
        unreleasedTextField.setDisable(true);
        unreleasedTextField.setPrefWidth(400);
        if(!setupModel.getFormatPaths().get(ReleaseFormat.Unreleased).toFile().canWrite()) {
            unreleasedTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            unreleasedPath = setupModel.getFormatPaths().get(ReleaseFormat.Unreleased);
            unreleasedTextField.setStyle("-fx-control-inner-background: green;");
        }

        aacTextField = new TextField(setupModel.getConversionPaths().get("AAC").toString());
        aacTextField.setEditable(false);
        aacTextField.setDisable(true);
        aacTextField.setPrefWidth(400);
        if(!setupModel.getConversionPaths().get("AAC").toFile().canWrite()) {
            aacTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            aacPath = setupModel.getConversionPaths().get("AAC");
            aacTextField.setStyle("-fx-control-inner-background: green;");
        }

        mp3TextField = new TextField(setupModel.getConversionPaths().get("MP3").toString());
        mp3TextField.setEditable(false);
        mp3TextField.setDisable(true);
        mp3TextField.setPrefWidth(400);
        if(!setupModel.getConversionPaths().get("MP3").toFile().canWrite()) {
            mp3TextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            mp3Path = setupModel.getConversionPaths().get("MP3");
            mp3TextField.setStyle("-fx-control-inner-background: green;");
        }

        phoneTextField = new TextField(setupModel.getDevicePaths().get("Phone").toString());
        phoneTextField.setEditable(false);
        phoneTextField.setDisable(true);
        phoneTextField.setPrefWidth(400);
        if(!setupModel.getDevicePaths().get("Phone").toFile().canWrite()) {
            phoneTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            phonePath = setupModel.getDevicePaths().get("Phone");
            phoneTextField.setStyle("-fx-control-inner-background: green;");
        }

        iTunesTextField = new TextField(setupModel.getiTunesMediaFolder().toString());
        iTunesTextField.setEditable(false);
        iTunesTextField.setDisable(true);
        iTunesTextField.setPrefWidth(400);
        if(!setupModel.getiTunesMediaFolder().toFile().canWrite()) {
            iTunesTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            iTunesPath = setupModel.getiTunesMediaFolder();
            iTunesTextField.setStyle("-fx-control-inner-background: green;");
        }

        gridPane.add(albumTextField, 2, 0);
        gridPane.add(mixtapeTextField, 2, 1);
        gridPane.add(epTextField, 2, 2);
        gridPane.add(singleTextField, 2, 3);
        gridPane.add(soundtrackTextField, 2, 4);
        gridPane.add(unreleasedTextField, 2, 5);
        gridPane.add(aacTextField, 2, 6);
        gridPane.add(mp3TextField, 2, 7);
        gridPane.add(phoneTextField, 2, 8);
        gridPane.add(iTunesTextField, 2, 9);

        if(setupModel.getFormatPaths().get(ReleaseFormat.Album).toFile().canWrite()) {
            albumDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.Album).toFile());
        }
        if(setupModel.getFormatPaths().get(ReleaseFormat.Mixtape).toFile().canWrite()) {
            mixtapeDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.Mixtape).toFile());
        }
        if(setupModel.getFormatPaths().get(ReleaseFormat.EP).toFile().canWrite()) {
            epDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.EP).toFile());
        }
        if(setupModel.getFormatPaths().get(ReleaseFormat.Single).toFile().canWrite()) {
            singleDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.Single).toFile());
        }
        if(setupModel.getFormatPaths().get(ReleaseFormat.Soundtrack).toFile().canWrite()) {
            soundtrackDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.Soundtrack).toFile());
        }
        if(setupModel.getFormatPaths().get(ReleaseFormat.Unreleased).toFile().canWrite()) {
            unreleasedDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.Unreleased).toFile());
        }
        if(setupModel.getConversionPaths().get("AAC").toFile().canWrite()) {
            aacDirectory.setInitialDirectory(setupModel.getConversionPaths().get("AAC").toFile());
        }
        if(setupModel.getConversionPaths().get("MP3").toFile().canWrite()) {
            mp3Directory.setInitialDirectory(setupModel.getConversionPaths().get("MP3").toFile());
        }
        if(setupModel.getDevicePaths().get("Phone").toFile().canWrite()) {
            phoneDirectory.setInitialDirectory(setupModel.getDevicePaths().get("Phone").toFile());
        }
        if(setupModel.getiTunesMediaFolder().toFile().canWrite()) {
            iTunesDirectory.setInitialDirectory(setupModel.getiTunesMediaFolder().toFile());
        }

        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        getDialogPane().setContent(gridPane);

        setEventHandlers();
        update();
    }

    //Setup for incomplete paths
    public SetupDialog(HashMap<ReleaseFormat, Path> fp, HashMap<String, Path> cp, HashMap<String, Path> dp, Path imf) {
        setTitle("Setup");
        setHeaderText("Your setup file is incomplete. Please fix before proceeding:");
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.add(albumLabel, 0, 0);
        gridPane.add(mixtapeLabel, 0, 1);
        gridPane.add(epLabel, 0, 2);
        gridPane.add(singleLabel, 0, 3);
        gridPane.add(soundtrackLabel, 0, 4);
        gridPane.add(unreleasedLabel, 0, 5);
        gridPane.add(aacLabel, 0, 6);
        gridPane.add(mp3Label, 0, 7);
        gridPane.add(phoneLabel, 0, 8);
        gridPane.add(iTunesLabel, 0, 9);

        gridPane.add(albumBrowse, 1, 0);
        gridPane.add(mixtapeBrowse, 1, 1);
        gridPane.add(epBrowse, 1, 2);
        gridPane.add(singleBrowse, 1, 3);
        gridPane.add(soundtrackBrowse, 1, 4);
        gridPane.add(unreleasedBrowse, 1, 5);
        gridPane.add(aacBrowse, 1, 6);
        gridPane.add(mp3Browse, 1, 7);
        gridPane.add(phoneBrowse, 1, 8);
        gridPane.add(iTunesBrowse, 1, 9);

        albumTextField.setEditable(false);
        albumTextField.setDisable(true);
        albumTextField.setPrefWidth(400);
        if(fp.containsKey(ReleaseFormat.Album)) {
            albumTextField = new TextField(fp.get(ReleaseFormat.Album).toString());
            if (!fp.get(ReleaseFormat.Album).toFile().canWrite()) {
                albumTextField.setStyle("-fx-control-inner-background: red;");
            }
            else {
                albumPath = fp.get(ReleaseFormat.Album);
                albumTextField.setStyle("-fx-control-inner-background: green;");
            }
        }
        else {
            albumTextField = new TextField();
            albumTextField.setStyle("-fx-control-inner-background: red;");
        }

        mixtapeTextField.setEditable(false);
        mixtapeTextField.setDisable(true);
        mixtapeTextField.setPrefWidth(400);
        if(fp.containsKey(ReleaseFormat.Mixtape)) {
            mixtapeTextField = new TextField(fp.get(ReleaseFormat.Mixtape).toString());
            if (!fp.get(ReleaseFormat.Mixtape).toFile().canWrite()) {
                albumPath = fp.get(ReleaseFormat.Album);
                mixtapeTextField.setStyle("-fx-control-inner-background: red;");
            }
            else {
                mixtapePath = fp.get(ReleaseFormat.Mixtape);
                mixtapeTextField.setStyle("-fx-control-inner-background: green;");
            }
        }
        else {
            mixtapeTextField = new TextField();
            mixtapeTextField.setStyle("-fx-control-inner-background: red;");
        }

        epTextField.setEditable(false);
        epTextField.setDisable(true);
        epTextField.setPrefWidth(400);
        if(fp.containsKey(ReleaseFormat.EP)) {
            epTextField = new TextField(fp.get(ReleaseFormat.EP).toString());
            if (!fp.get(ReleaseFormat.EP).toFile().canWrite()) {
                epTextField.setStyle("-fx-control-inner-background: red;");
            }
            else {
                epPath = fp.get(ReleaseFormat.EP);
                epTextField.setStyle("-fx-control-inner-background: green;");
            }
        }
        else {
            epTextField = new TextField();
            epTextField.setStyle("-fx-control-inner-background: red;");
        }

        singleTextField.setEditable(false);
        singleTextField.setDisable(true);
        singleTextField.setPrefWidth(400);
        if(fp.containsKey(ReleaseFormat.Single)) {
            singleTextField = new TextField(fp.get(ReleaseFormat.Single).toString());
            if (!fp.get(ReleaseFormat.Single).toFile().canWrite()) {
                singleTextField.setStyle("-fx-control-inner-background: red;");
            }
            else {
                singlePath = fp.get(ReleaseFormat.Single);
                singleTextField.setStyle("-fx-control-inner-background: green;");
            }
        }
        else {
            singleTextField = new TextField();
            singleTextField.setStyle("-fx-control-inner-background: red;");
        }

        soundtrackTextField.setEditable(false);
        soundtrackTextField.setDisable(true);
        soundtrackTextField.setPrefWidth(400);
        if(fp.containsKey(ReleaseFormat.Soundtrack)) {
            soundtrackTextField = new TextField(fp.get(ReleaseFormat.Soundtrack).toString());
            if (!fp.get(ReleaseFormat.Soundtrack).toFile().canWrite()) {
                soundtrackTextField.setStyle("-fx-control-inner-background: red;");
            }
            else {
                soundtrackPath = fp.get(ReleaseFormat.Soundtrack);
                soundtrackTextField.setStyle("-fx-control-inner-background: green;");
            }
        }
        else {
            soundtrackTextField = new TextField();
            soundtrackTextField.setStyle("-fx-control-inner-background: red;");
        }

        unreleasedTextField.setEditable(false);
        unreleasedTextField.setDisable(true);
        unreleasedTextField.setPrefWidth(400);
        if(fp.containsKey(ReleaseFormat.Unreleased)) {
            unreleasedTextField = new TextField(fp.get(ReleaseFormat.Unreleased).toString());
            if (!fp.get(ReleaseFormat.Unreleased).toFile().canWrite()) {
                unreleasedTextField.setStyle("-fx-control-inner-background: red;");
            }
            else {
                unreleasedPath = fp.get(ReleaseFormat.Unreleased);
                unreleasedTextField.setStyle("-fx-control-inner-background: green;");
            }
        }
        else {
            unreleasedTextField = new TextField();
            unreleasedTextField.setStyle("-fx-control-inner-background: red;");
        }

        aacTextField.setEditable(false);
        aacTextField.setDisable(true);
        aacTextField.setPrefWidth(400);
        if(cp.containsKey("AAC")) {
            aacTextField = new TextField(cp.get("AAC").toString());
            if (!cp.get("AAC").toFile().canWrite()) {
                aacTextField.setStyle("-fx-control-inner-background: red;");
            }
            else {
                aacPath = cp.get("AAC");
                aacTextField.setStyle("-fx-control-inner-background: green;");
            }
        }
        else {
            aacTextField = new TextField();
            aacTextField.setStyle("-fx-control-inner-background: red;");
        }

        mp3TextField.setEditable(false);
        mp3TextField.setDisable(true);
        mp3TextField.setPrefWidth(400);
        if(cp.containsKey("MP3")) {
            mp3TextField = new TextField(cp.get("MP3").toString());
            if (!cp.get("MP3").toFile().canWrite()) {
                mp3TextField.setStyle("-fx-control-inner-background: red;");
            }
            else {
                mp3Path = cp.get("MP3");
                mp3TextField.setStyle("-fx-control-inner-background: green;");
            }
        }
        else {
            mp3TextField = new TextField();
            mp3TextField.setStyle("-fx-control-inner-background: red;");
        }

        phoneTextField.setEditable(false);
        phoneTextField.setDisable(true);
        phoneTextField.setPrefWidth(400);
        if(dp.containsKey("Phone")) {
            phoneTextField = new TextField(dp.get("Phone").toString());
            if (!dp.get("Phone").toFile().canWrite()) {
                phoneTextField.setStyle("-fx-control-inner-background: red;");
            }
            else {
                phonePath = dp.get("Phone");
                phoneTextField.setStyle("-fx-control-inner-background: green;");
            }
        }
        else {
            phoneTextField = new TextField();
            phoneTextField.setStyle("-fx-control-inner-background: red;");
        }

        iTunesTextField = new TextField(imf.toString());
        iTunesTextField.setEditable(false);
        iTunesTextField.setDisable(true);
        iTunesTextField.setPrefWidth(400);
        if(!imf.toFile().canWrite()) {
            iTunesTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            iTunesPath = imf;
            iTunesTextField.setStyle("-fx-control-inner-background: green;");
        }

        gridPane.add(albumTextField, 2, 0);
        gridPane.add(mixtapeTextField, 2, 1);
        gridPane.add(epTextField, 2, 2);
        gridPane.add(singleTextField, 2, 3);
        gridPane.add(soundtrackTextField, 2, 4);
        gridPane.add(unreleasedTextField, 2, 5);
        gridPane.add(aacTextField, 2, 6);
        gridPane.add(mp3TextField, 2, 7);
        gridPane.add(phoneTextField, 2, 8);
        gridPane.add(iTunesTextField, 2, 9);

        if(fp.get(ReleaseFormat.Album).toFile().canWrite()) {
            albumDirectory.setInitialDirectory(fp.get(ReleaseFormat.Album).toFile());
        }
        if(fp.get(ReleaseFormat.Mixtape).toFile().canWrite()) {
            mixtapeDirectory.setInitialDirectory(fp.get(ReleaseFormat.Mixtape).toFile());
        }
        if(fp.get(ReleaseFormat.EP).toFile().canWrite()) {
            epDirectory.setInitialDirectory(fp.get(ReleaseFormat.EP).toFile());
        }
        if(fp.get(ReleaseFormat.Single).toFile().canWrite()) {
            singleDirectory.setInitialDirectory(fp.get(ReleaseFormat.Single).toFile());
        }
        if(fp.get(ReleaseFormat.Soundtrack).toFile().canWrite()) {
            soundtrackDirectory.setInitialDirectory(fp.get(ReleaseFormat.Soundtrack).toFile());
        }
        if(fp.get(ReleaseFormat.Unreleased).toFile().canWrite()) {
            unreleasedDirectory.setInitialDirectory(fp.get(ReleaseFormat.Unreleased).toFile());
        }
        if(cp.get("AAC").toFile().canWrite()) {
            aacDirectory.setInitialDirectory(cp.get("AAC").toFile());
        }
        if(cp.get("MP3").toFile().canWrite()) {
            mp3Directory.setInitialDirectory(cp.get("MP3").toFile());
        }
        if(dp.get("Phone").toFile().canWrite()) {
            phoneDirectory.setInitialDirectory(dp.get("Phone").toFile());
        }
        if(imf.toFile().canWrite()) {
            iTunesDirectory.setInitialDirectory(imf.toFile());
        }
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        getDialogPane().setContent(gridPane);

        setEventHandlers();
        update();
    }

    //Setup for missing setup file
    public SetupDialog() {
        setTitle("Setup");
        setHeaderText("You do not have a setup file configured. Complete the following before proceeding:");
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.add(albumLabel, 0, 0);
        gridPane.add(mixtapeLabel, 0, 1);
        gridPane.add(epLabel, 0, 2);
        gridPane.add(singleLabel, 0, 3);
        gridPane.add(soundtrackLabel, 0, 4);
        gridPane.add(unreleasedLabel, 0, 5);
        gridPane.add(aacLabel, 0, 6);
        gridPane.add(mp3Label, 0, 7);
        gridPane.add(phoneLabel, 0, 8);
        gridPane.add(iTunesLabel, 0, 9);

        gridPane.add(albumBrowse, 1, 0);
        gridPane.add(mixtapeBrowse, 1, 1);
        gridPane.add(epBrowse, 1, 2);
        gridPane.add(singleBrowse, 1, 3);
        gridPane.add(soundtrackBrowse, 1, 4);
        gridPane.add(unreleasedBrowse, 1, 5);
        gridPane.add(aacBrowse, 1, 6);
        gridPane.add(mp3Browse, 1, 7);
        gridPane.add(phoneBrowse, 1, 8);
        gridPane.add(iTunesBrowse, 1, 9);

        albumTextField = new TextField();
        albumTextField.setEditable(false);
        albumTextField.setDisable(true);
        albumTextField.setPrefWidth(400);
        albumTextField.setStyle("-fx-control-inner-background: red;");

        mixtapeTextField = new TextField();
        mixtapeTextField.setEditable(false);
        mixtapeTextField.setDisable(true);
        mixtapeTextField.setPrefWidth(400);
        mixtapeTextField.setStyle("-fx-control-inner-background: red;");

        epTextField = new TextField();
        epTextField.setEditable(false);
        epTextField.setDisable(true);
        epTextField.setPrefWidth(400);
        epTextField.setStyle("-fx-control-inner-background: red;");

        singleTextField = new TextField();
        singleTextField.setEditable(false);
        singleTextField.setDisable(true);
        singleTextField.setPrefWidth(400);
        singleTextField.setStyle("-fx-control-inner-background: red;");

        soundtrackTextField = new TextField();
        soundtrackTextField.setEditable(false);
        soundtrackTextField.setDisable(true);
        soundtrackTextField.setPrefWidth(400);
        soundtrackTextField.setStyle("-fx-control-inner-background: red;");

        unreleasedTextField = new TextField();
        unreleasedTextField.setEditable(false);
        unreleasedTextField.setDisable(true);
        unreleasedTextField.setPrefWidth(400);
        unreleasedTextField.setStyle("-fx-control-inner-background: red;");

        aacTextField = new TextField();
        aacTextField.setEditable(false);
        aacTextField.setDisable(true);
        aacTextField.setPrefWidth(400);
        aacTextField.setStyle("-fx-control-inner-background: red;");

        mp3TextField = new TextField();
        mp3TextField.setEditable(false);
        mp3TextField.setDisable(true);
        mp3TextField.setPrefWidth(400);
        mp3TextField.setStyle("-fx-control-inner-background: red;");

        phoneTextField = new TextField();
        phoneTextField.setEditable(false);
        phoneTextField.setDisable(true);
        phoneTextField.setPrefWidth(400);
        phoneTextField.setStyle("-fx-control-inner-background: red;");

        iTunesTextField = new TextField();
        iTunesTextField.setEditable(false);
        iTunesTextField.setDisable(true);
        iTunesTextField.setPrefWidth(400);
        iTunesTextField.setStyle("-fx-control-inner-background: red;");

        gridPane.add(albumTextField, 2, 0);
        gridPane.add(mixtapeTextField, 2, 1);
        gridPane.add(epTextField, 2, 2);
        gridPane.add(singleTextField, 2, 3);
        gridPane.add(soundtrackTextField, 2, 4);
        gridPane.add(unreleasedTextField, 2, 5);
        gridPane.add(aacTextField, 2, 6);
        gridPane.add(mp3TextField, 2, 7);
        gridPane.add(phoneTextField, 2, 8);
        gridPane.add(iTunesTextField, 2, 9);

        albumDirectory = new DirectoryChooser();
        mixtapeDirectory = new DirectoryChooser();
        epDirectory = new DirectoryChooser();
        singleDirectory = new DirectoryChooser();
        soundtrackDirectory = new DirectoryChooser();
        unreleasedDirectory = new DirectoryChooser();
        aacDirectory = new DirectoryChooser();
        mp3Directory = new DirectoryChooser();
        phoneDirectory = new DirectoryChooser();
        iTunesDirectory = new DirectoryChooser();

        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        getDialogPane().setContent(gridPane);

        setEventHandlers();
        update();
    }

    public Path getAlbumPath() { return albumPath; }
    public Path getMixtapePath() { return mixtapePath; }
    public Path getEpPath() { return epPath; }
    public Path getSinglePath() { return singlePath; }
    public Path getSoundtrackPath() { return soundtrackPath; }
    public Path getUnreleasedPath() { return unreleasedPath; }
    public Path getAacPath() { return aacPath; }
    public Path getMp3Path() { return mp3Path; }
    public Path getPhonePath() { return phonePath; }
    public Path getiTunesPath() { return iTunesPath; }

    private void setEventHandlers() {
        albumBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = albumDirectory.showDialog(null);
                if(selectedFile != null) {
                    albumPath = selectedFile.toPath();
                    albumTextField.setText(selectedFile.getPath());
                    if(selectedFile.canWrite()) {
                        albumTextField.setStyle("-fx-control-inner-background: green;");
                    }
                    else {
                        albumTextField.setStyle("-fx-control-inner-background: red;");
                    }
                }
                update();
            }
        });
        mixtapeBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = mixtapeDirectory.showDialog(null);
                if(selectedFile != null) {
                    mixtapePath = selectedFile.toPath();
                    mixtapeTextField.setText(selectedFile.getPath());
                    if(selectedFile.canWrite()) {
                        mixtapeTextField.setStyle("-fx-control-inner-background: green;");
                    }
                    else {
                        mixtapeTextField.setStyle("-fx-control-inner-background: red;");
                    }
                }
                update();
            }
        });
        epBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = epDirectory.showDialog(null);
                if(selectedFile != null) {
                    epPath = selectedFile.toPath();
                    epTextField.setText(selectedFile.getPath());
                    if(selectedFile.canWrite()) {
                        epTextField.setStyle("-fx-control-inner-background: green;");
                    }
                    else {
                        epTextField.setStyle("-fx-control-inner-background: red;");
                    }
                }
                update();
            }
        });
        singleBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = singleDirectory.showDialog(null);
                if(selectedFile != null) {
                    singlePath = selectedFile.toPath();
                    singleTextField.setText(selectedFile.getPath());
                    if(selectedFile.canWrite()) {
                        singleTextField.setStyle("-fx-control-inner-background: green;");
                    }
                    else {
                        singleTextField.setStyle("-fx-control-inner-background: red;");
                    }
                }
                update();
            }
        });
        soundtrackBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = soundtrackDirectory.showDialog(null);
                if(selectedFile != null) {
                    soundtrackPath = selectedFile.toPath();
                    soundtrackTextField.setText(selectedFile.getPath());
                    if(selectedFile.canWrite()) {
                        soundtrackTextField.setStyle("-fx-control-inner-background: green;");
                    }
                    else {
                        soundtrackTextField.setStyle("-fx-control-inner-background: red;");
                    }
                }
                update();
            }
        });
        unreleasedBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = unreleasedDirectory.showDialog(null);
                if(selectedFile != null) {
                    unreleasedPath = selectedFile.toPath();
                    unreleasedTextField.setText(selectedFile.getPath());
                    if(selectedFile.canWrite()) {
                        unreleasedTextField.setStyle("-fx-control-inner-background: green;");
                    }
                    else {
                        unreleasedTextField.setStyle("-fx-control-inner-background: red;");
                    }
                }
                update();
            }
        });
        aacBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = aacDirectory.showDialog(null);
                if(selectedFile != null) {
                    aacPath = selectedFile.toPath();
                    aacTextField.setText(selectedFile.getPath());
                    if(selectedFile.canWrite()) {
                        aacTextField.setStyle("-fx-control-inner-background: green;");
                    }
                    else {
                        aacTextField.setStyle("-fx-control-inner-background: red;");
                    }
                }
                update();
            }
        });
        mp3Browse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = mp3Directory.showDialog(null);
                if(selectedFile != null) {
                    mp3Path = selectedFile.toPath();
                    mp3TextField.setText(selectedFile.getPath());
                    if(selectedFile.canWrite()) {
                        mp3TextField.setStyle("-fx-control-inner-background: green;");
                    }
                    else {
                        mp3TextField.setStyle("-fx-control-inner-background: red;");
                    }
                }
                update();
            }
        });
        phoneBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = phoneDirectory.showDialog(null);
                if(selectedFile != null) {
                    phonePath = selectedFile.toPath();
                    phoneTextField.setText(selectedFile.getPath());
                    if(selectedFile.canWrite()) {
                        phoneTextField.setStyle("-fx-control-inner-background: green;");
                    }
                    else {
                        phoneTextField.setStyle("-fx-control-inner-background: red;");
                    }
                }
                update();
            }
        });
        iTunesBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = iTunesDirectory.showDialog(null);
                if(selectedFile != null) {
                    iTunesPath = selectedFile.toPath();
                    iTunesTextField.setText(selectedFile.getPath());
                    if(selectedFile.canWrite()) {
                        iTunesTextField.setStyle("-fx-control-inner-background: green;");
                    }
                    else {
                        iTunesTextField.setStyle("-fx-control-inner-background: red;");
                    }
                }
                update();
            }
        });
    }

    private void update() {
        if(albumPath != null && albumPath.toFile().canWrite()
                && mixtapePath != null && mixtapePath.toFile().canWrite()
                && epPath != null && epPath.toFile().canWrite()
                && singlePath != null && singlePath.toFile().canWrite()
                && soundtrackPath != null && soundtrackPath.toFile().canWrite()
                && unreleasedPath != null && unreleasedPath.toFile().canWrite()
                && aacPath != null && aacPath.toFile().canWrite()
                && mp3Path != null && mp3Path.toFile().canWrite()
                && phonePath != null && phonePath.toFile().canWrite()
                && iTunesPath != null && iTunesPath.toFile().canWrite()
                ) {
            getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
        }
        else {
            getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        }
    }

}
