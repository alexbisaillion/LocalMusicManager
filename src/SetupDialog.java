import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;

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

        albumDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.Album).toFile());
        mixtapeDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.Mixtape).toFile());
        epDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.EP).toFile());
        singleDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.Single).toFile());
        soundtrackDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.Soundtrack).toFile());
        unreleasedDirectory.setInitialDirectory(setupModel.getFormatPaths().get(ReleaseFormat.Unreleased).toFile());
        aacDirectory.setInitialDirectory(setupModel.getConversionPaths().get("AAC").toFile());
        mp3Directory.setInitialDirectory(setupModel.getConversionPaths().get("MP3").toFile());
        phoneDirectory.setInitialDirectory(setupModel.getDevicePaths().get("Phone").toFile());
        iTunesDirectory.setInitialDirectory(setupModel.getiTunesMediaFolder().toFile());

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(gridPane);

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
            } else {
                albumTextField.setStyle("-fx-control-inner-background: green;");
            }
        }
        else {
            albumTextField = new TextField();
            albumTextField.setStyle("-fx-control-inner-background: red;");
        }

        mixtapeTextField = new TextField(fp.get(ReleaseFormat.Mixtape).toString());
        mixtapeTextField.setEditable(false);
        mixtapeTextField.setDisable(true);
        mixtapeTextField.setPrefWidth(400);
        if(!fp.get(ReleaseFormat.Mixtape).toFile().canWrite()) {
            mixtapeTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            mixtapeTextField.setStyle("-fx-control-inner-background: green;");
        }

        epTextField = new TextField(fp.get(ReleaseFormat.EP).toString());
        epTextField.setEditable(false);
        epTextField.setDisable(true);
        epTextField.setPrefWidth(400);
        if(!fp.get(ReleaseFormat.EP).toFile().canWrite()) {
            epTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            epTextField.setStyle("-fx-control-inner-background: green;");
        }

        singleTextField = new TextField(fp.get(ReleaseFormat.Single).toString());
        singleTextField.setEditable(false);
        singleTextField.setDisable(true);
        singleTextField.setPrefWidth(400);
        if(!fp.get(ReleaseFormat.Single).toFile().canWrite()) {
            singleTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            singleTextField.setStyle("-fx-control-inner-background: green;");
        }

        soundtrackTextField = new TextField(fp.get(ReleaseFormat.Soundtrack).toString());
        soundtrackTextField.setEditable(false);
        soundtrackTextField.setDisable(true);
        soundtrackTextField.setPrefWidth(400);
        if(!fp.get(ReleaseFormat.Soundtrack).toFile().canWrite()) {
            soundtrackTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            soundtrackTextField.setStyle("-fx-control-inner-background: green;");
        }

        unreleasedTextField = new TextField(fp.get(ReleaseFormat.Unreleased).toString());
        unreleasedTextField.setEditable(false);
        unreleasedTextField.setDisable(true);
        unreleasedTextField.setPrefWidth(400);
        if(!fp.get(ReleaseFormat.Unreleased).toFile().canWrite()) {
            unreleasedTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            unreleasedTextField.setStyle("-fx-control-inner-background: green;");
        }

        aacTextField = new TextField(cp.get("AAC").toString());
        aacTextField.setEditable(false);
        aacTextField.setDisable(true);
        aacTextField.setPrefWidth(400);
        if(!cp.get("AAC").toFile().canWrite()) {
            aacTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            aacTextField.setStyle("-fx-control-inner-background: green;");
        }

        mp3TextField = new TextField(cp.get("MP3").toString());
        mp3TextField.setEditable(false);
        mp3TextField.setDisable(true);
        mp3TextField.setPrefWidth(400);
        if(!cp.get("MP3").toFile().canWrite()) {
            mp3TextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            mp3TextField.setStyle("-fx-control-inner-background: green;");
        }

        phoneTextField = new TextField(dp.get("Phone").toString());
        phoneTextField.setEditable(false);
        phoneTextField.setDisable(true);
        phoneTextField.setPrefWidth(400);
        if(!dp.get("Phone").toFile().canWrite()) {
            phoneTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            phoneTextField.setStyle("-fx-control-inner-background: green;");
        }

        iTunesTextField = new TextField(imf.toString());
        iTunesTextField.setEditable(false);
        iTunesTextField.setDisable(true);
        iTunesTextField.setPrefWidth(400);
        if(!imf.toFile().canWrite()) {
            iTunesTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
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

        albumDirectory.setInitialDirectory(fp.get(ReleaseFormat.Album).toFile());
        mixtapeDirectory.setInitialDirectory(fp.get(ReleaseFormat.Mixtape).toFile());
        epDirectory.setInitialDirectory(fp.get(ReleaseFormat.EP).toFile());
        singleDirectory.setInitialDirectory(fp.get(ReleaseFormat.Single).toFile());
        soundtrackDirectory.setInitialDirectory(fp.get(ReleaseFormat.Soundtrack).toFile());
        unreleasedDirectory.setInitialDirectory(fp.get(ReleaseFormat.Unreleased).toFile());
        aacDirectory.setInitialDirectory(cp.get("AAC").toFile());
        mp3Directory.setInitialDirectory(cp.get("MP3").toFile());
        phoneDirectory.setInitialDirectory(dp.get("Phone").toFile());
        iTunesDirectory.setInitialDirectory(imf.toFile());

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(gridPane);

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

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(gridPane);

    }

    private void update() {

    }

}
