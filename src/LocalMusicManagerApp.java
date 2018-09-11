import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalMusicManagerApp extends Application {
    private ArrayList<NewFile> model;
    private LocalMusicManagerView view;
    private HashMap<ReleaseFormat, Path> formatPaths;
    private HashMap<String, Path> conversionPaths;
    private HashMap<String, Path> devicePaths;
    private ArrayList<Path> sortedFiles;
    private Path iTunesMediaFolder;
    private ArrayList<Path> oldFolders;

    public void start(Stage primaryStage) {
        readSetupFile();
        if(!checkWritePermissions()) {
            promptNewSetup();
            Alert alert = new Alert(Alert.AlertType.ERROR, "You have a faulty directory in your setup!");
            alert.showAndWait();
        }
        model = new ArrayList<>();
        view = new LocalMusicManagerView(model);
        sortedFiles = new ArrayList<>();

        view.getAddToQueue().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.add(new NewFile());
                view.update();
            }
        });

        view.getAddToLibrary().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Processing");
                alert.setHeaderText("Adding music to library...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                alert.show();
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
                        view.getAddToItunes().setDisable(false);
                        alert.setContentText("Done!");
                        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                    }
                };
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        for(NewFile nf: model) {
                            switch(nf.getExtension()) {
                                case ".zip":
                                    try {
                                        Path p = SortArchiveFile.extractZIP(nf.getSelectedFile(), formatPaths.get(nf.getFormat()));
                                        if(nf.getNewArtistTag() != null) {
                                            SortArchiveFile.changeArtist(p, nf.getNewArtistTag());
                                        }
                                        if(nf.getNewAlbumArtistTag() != null) {
                                            SortArchiveFile.changeAlbumArtist(p, nf.getNewAlbumArtistTag());
                                        }
                                        if(nf.getNewGenreTag() != null) {
                                            SortArchiveFile.changeGenre(p, nf.getNewGenreTag());
                                        }
                                        SortArchiveFile.correct(p);
                                        if(!SortArchiveFile.isFaulty(p)) {
                                            sortedFiles.add(SortArchiveFile.move(p));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case ".rar":
                                    try {
                                        Path p = SortArchiveFile.extractRar(nf.getSelectedFile(), formatPaths.get(nf.getFormat()));
                                        if(nf.getNewArtistTag() != null) {
                                            SortArchiveFile.changeArtist(p, nf.getNewArtistTag());
                                        }
                                        if(nf.getNewAlbumArtistTag() != null) {
                                            SortArchiveFile.changeAlbumArtist(p, nf.getNewAlbumArtistTag());
                                        }
                                        if(nf.getNewGenreTag() != null) {
                                            SortArchiveFile.changeGenre(p, nf.getNewGenreTag());
                                        }
                                        SortArchiveFile.correct(p);
                                        if(!SortArchiveFile.isFaulty(p)) {
                                            sortedFiles.add(SortArchiveFile.move(p));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case ".7z":
                                    try {
                                        Path p = SortArchiveFile.extract7z(nf.getSelectedFile(), formatPaths.get(nf.getFormat()));
                                        if(nf.getNewArtistTag() != null) {
                                            SortArchiveFile.changeArtist(p, nf.getNewArtistTag());
                                        }
                                        if(nf.getNewAlbumArtistTag() != null) {
                                            SortArchiveFile.changeAlbumArtist(p, nf.getNewAlbumArtistTag());
                                        }
                                        if(nf.getNewGenreTag() != null) {
                                            SortArchiveFile.changeGenre(p, nf.getNewGenreTag());
                                        }
                                        SortArchiveFile.correct(p);
                                        if(!SortArchiveFile.isFaulty(p)) {
                                            sortedFiles.add(SortArchiveFile.move(p));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                default:
                                    if(nf.getFormat() == ReleaseFormat.Single) {
                                        try {
                                            if(nf.getNewArtistTag() != null) {
                                                System.out.println(SortAudioFile.changeArtist(nf.getSelectedFile().toPath(), nf.getNewArtistTag()));
                                            }
                                            if(nf.getNewAlbumArtistTag() != null) {
                                                System.out.println(SortAudioFile.changeAlbumArtist(nf.getSelectedFile().toPath(), nf.getNewAlbumArtistTag()));
                                            }
                                            if(nf.getNewGenreTag() != null) {
                                                System.out.println(SortAudioFile.changeGenre(nf.getSelectedFile().toPath(), nf.getNewGenreTag()));
                                            }
                                            Path p = SortAudioFile.moveSingle(nf.getSelectedFile(), formatPaths.get(nf.getFormat()));
                                            SortAudioFile.correct(p);
                                            if(!SortAudioFile.isFaulty(p)) {
                                                sortedFiles.add(p);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        try {
                                            if(nf.getNewArtistTag() != null) {
                                                System.out.println(SortAudioFile.changeArtist(nf.getSelectedFile().toPath(), nf.getNewArtistTag()));
                                            }
                                            if(nf.getNewAlbumArtistTag() != null) {
                                                System.out.println(SortAudioFile.changeAlbumArtist(nf.getSelectedFile().toPath(), nf.getNewAlbumArtistTag()));
                                            }
                                            if(nf.getNewGenreTag() != null) {
                                                System.out.println(SortAudioFile.changeGenre(nf.getSelectedFile().toPath(), nf.getNewGenreTag()));
                                            }
                                            Path p = SortAudioFile.moveUnreleased(nf.getSelectedFile(), formatPaths.get(nf.getFormat()));
                                            SortAudioFile.correct(p);
                                            if(!SortAudioFile.isFaulty(p)) {
                                                sortedFiles.add(p);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                            }
                        }
                        Platform.runLater(update);
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        });

        view.getAddToItunes().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Processing");
                alert.setHeaderText("Adding to iTunes...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                alert.show();
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
                        view.getConvertToAAC().setDisable(false);
                        view.getConvertToMP3().setDisable(false);
                        alert.setContentText("Done!");
                        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);

                    }
                };
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            addToItunes();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(update);
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        });

        view.getConvertToAAC().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Processing");
                alert.setHeaderText("Converting to AAC...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                alert.show();
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
                        view.getConvertToAAC().setDisable(false);
                        view.getConvertToMP3().setDisable(false);
                        alert.setContentText("Done!");
                        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                    }
                };
                Runnable confirmCopyToPhone = new Runnable() {
                    @Override
                    public void run() {
                        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to copy these files to your phone?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                        alert2.showAndWait();
                        if (alert2.getResult() == ButtonType.YES) {
                            Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                            alert3.setTitle("Processing");
                            alert3.setHeaderText("Copying to phone...");
                            alert3.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                            alert3.show();
                            Runnable update2 = new Runnable() {
                                @Override
                                public void run() {
                                    alert3.setContentText("Done!");
                                    alert3.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                                }
                            };
                            Runnable r2 = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        copyToPhone();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Platform.runLater(update2);
                                }
                            };
                            Thread t2 = new Thread(r2);
                            t2.start();
                        }
                    }
                };
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setEncoder("AAC Encoder");
                            convert();
                            view.getRelocateAAC().setDisable(false);
                            Platform.runLater(confirmCopyToPhone);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(update);
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        });

        view.getRelocateAAC().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Processing");
                alert.setHeaderText("Relocating AAC files...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                alert.show();
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
                        view.getCleanUpItunes().setDisable(false);
                        alert.setContentText("Done!");
                        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                    }
                };
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            relocate("AAC");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(update);
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        });

        view.getConvertToMP3().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Processing");
                alert.setHeaderText("Converting to MP3...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                alert.show();
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
                        alert.setContentText("Done!");
                        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                    }
                };
                Runnable confirmRenameForCarStereo = new Runnable() {
                    @Override
                    public void run() {
                        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to rename these MP3 files for usage on a car stereo?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                        alert2.showAndWait();

                        if (alert2.getResult() == ButtonType.YES) {
                            Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                            alert3.setTitle("Processing");
                            alert3.setHeaderText("Renaming MP3 files...");
                            alert3.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                            alert3.show();
                            Runnable update2 = new Runnable() {
                                @Override
                                public void run() {
                                    alert3.setContentText("Done!");
                                    alert3.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                                }
                            };
                            Runnable r2 = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        renameForCarStereo();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Platform.runLater(update2);
                                }
                            };
                            Thread t2 = new Thread(r2);
                            t2.start();
                        }
                    }
                };
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setEncoder("MP3 Encoder");
                            convert();
                            view.getRelocateMP3().setDisable(false);
                            Platform.runLater(confirmRenameForCarStereo);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(update);
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        });

        view.getRelocateMP3().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Processing");
                alert.setHeaderText("Relocating MP3 files...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                alert.show();
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
                        view.getCleanUpItunes().setDisable(false);
                        alert.setContentText("Done!");
                        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                    }
                };
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            relocate("MP3");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(update);
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        });

        view.getCleanUpItunes().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Processing");
                alert.setHeaderText("Cleaning up iTunes...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                alert.show();
                try {
                    cleanUpItunes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alert.setContentText("Done!");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
            }
        });


        primaryStage.setTitle("Local Music Manager");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(view, 1075, 500));
        primaryStage.show();
    }

    private void addToItunes() throws IOException {
        for(Path p: sortedFiles) {
            String[] args  = {"python", "src/scripts/addToLibrary.py", p.toString()};
            Process pro = Runtime.getRuntime().exec(args);
            try {
                pro.waitFor();
                BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                String line;
                while((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                br = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
                while((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void convert() throws IOException {
        oldFolders = new ArrayList<>();
        File directory = iTunesMediaFolder.toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                oldFolders.add(child.toPath());
            }
        }
        for(Path p: sortedFiles) {
            if(p.toFile().isDirectory()) {
                File[] songs = p.toFile().listFiles();
                if(songs != null) {
                    for(File song : songs) {
                        String[] args = {"python", "src/scripts/convert.py", song.getPath()};
                        Process pro = Runtime.getRuntime().exec(args);
                        try {
                            pro.waitFor();
                            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                            String line;
                            while ((line = br.readLine()) != null) {
                                System.out.println(line);
                            }
                            br = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
                            while ((line = br.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else {
                String[] args = {"python", "src/scripts/convert.py", p.toString()};
                Process pro = Runtime.getRuntime().exec(args);
                try {
                    pro.waitFor();
                    BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    br = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void cleanUpItunes() throws IOException {
        String[] args  = {"python", "src/scripts/deleteMissingTracks.py"};
        Process pro = Runtime.getRuntime().exec(args);
        try {
            pro.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void relocate(String conversionType) throws IOException {
        File directory = iTunesMediaFolder.toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                if(child.isDirectory() && !oldFolders.contains(child.toPath())) {
                    FileUtils.copyDirectoryToDirectory(child, conversionPaths.get(conversionType).toFile());
                    FileUtils.deleteDirectory(child); //possible error
                }
            }
        }
    }

    private void copyToPhone() throws IOException {
        File directory = iTunesMediaFolder.toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                if(child.isDirectory() && !oldFolders.contains(child.toPath())) {
                    FileUtils.copyDirectoryToDirectory(child, devicePaths.get("Phone").toFile());
                }
            }
        }
    }

    private void renameForCarStereo() throws Exception {
        File directory = iTunesMediaFolder.toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File artistDirectory : files) {
                if(artistDirectory.isDirectory() && !oldFolders.contains(artistDirectory.toPath())) {
                    File[] projects = artistDirectory.listFiles();
                    if(projects != null) {
                        for (File project : projects) {
                            File[] tracks = project.listFiles();
                            if(tracks != null) {
                                for (File track : tracks) {
                                    AudioFile f = AudioFileIO.read(track);
                                    Tag tag = f.getTag();
                                    String name = tag.getFirst(FieldKey.ARTIST) + " - " + tag.getFirst(FieldKey.TITLE);
                                    name = SortArchiveFile.stripIllegalCharacters(name);
                                    track.renameTo(new File(track.getParent() + "\\" + name + ".mp3"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setEncoder(String encoder) throws IOException {
        String[] args  = {"python", "src/scripts/setEncoder.py", encoder};
        Process pro = Runtime.getRuntime().exec(args);
        try {
            pro.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void readSetupFile() {
        formatPaths = new HashMap<>();
        conversionPaths = new HashMap<>();
        devicePaths = new HashMap<>();
        try {
            File inputFile = new File("setup.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList formatList = doc.getElementsByTagName("formatDirectory");
            for(int i=0; i<formatList.getLength(); i++) {
                Element currentElement = (Element) formatList.item(i);
                formatPaths.put(ReleaseFormat.valueOf(currentElement.getElementsByTagName("format").item(0).getTextContent()), Paths.get(currentElement.getElementsByTagName("path").item(0).getTextContent()));
            }

            NodeList conversionList = doc.getElementsByTagName("conversionDirectory");
            for(int i=0; i<conversionList.getLength(); i++) {
                Element currentElement = (Element) conversionList.item(i);
                conversionPaths.put(currentElement.getElementsByTagName("fileType").item(0).getTextContent(), Paths.get(currentElement.getElementsByTagName("path").item(0).getTextContent()));
            }

            NodeList deviceList = doc.getElementsByTagName("deviceDirectory");
            for(int i=0; i<deviceList.getLength(); i++) {
                Element currentElement = (Element) deviceList.item(i);
                devicePaths.put(currentElement.getElementsByTagName("device").item(0).getTextContent(), Paths.get(currentElement.getElementsByTagName("path").item(0).getTextContent()));
            }

            NodeList iTunesList = doc.getElementsByTagName("iTunesDirectory");
            for(int i=0; i<iTunesList.getLength(); i++) {
                Element currentElement = (Element) iTunesList.item(i);
                iTunesMediaFolder = Paths.get(currentElement.getElementsByTagName("path").item(0).getTextContent());
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkWritePermissions() {
        for(Path p: formatPaths.values()) {
            if(!p.toFile().canWrite()) {
                return false;
            }
        }
        for(Path p: conversionPaths.values()) {
            if(!p.toFile().canWrite()) {
                return false;
            }
        }
        for(Path p: devicePaths.values()) {
            if(!p.toFile().canWrite()) {
                return false;
            }
        }
        if(!iTunesMediaFolder.toFile().canWrite()) {
            return false;
        }
        return true;
    }

    private void promptNewSetup() {
        Dialog dialog = new Dialog();
        dialog.setTitle("Setup");
        dialog.setHeaderText("You have a faulty setup file. Please fix before proceeding:");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        Label albumLabel = new Label("Albums");
        Label mixtapeLabel = new Label("Mixtapes");
        Label epLabel = new Label("EPs");
        Label singleLabel = new Label("Singles");
        Label soundtrackLabel = new Label("Soundtracks");
        Label unreleasedLabel = new Label("Unreleased");
        Label aacLabel = new Label("AAC");
        Label mp3Label = new Label("MP3");
        Label phoneLabel = new Label("Phone");
        Label iTunesLabel = new Label("iTunes Media Folder");

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

        Button albumBrowse = new Button("BROWSE");
        Button mixtapeBrowse = new Button("BROWSE");
        Button epBrowse = new Button("BROWSE");
        Button singleBrowse = new Button("BROWSE");
        Button soundtrackBrowse = new Button("BROWSE");
        Button unreleasedBrowse = new Button("BROWSE");
        Button aacBrowse = new Button("BROWSE");
        Button mp3Browse = new Button("BROWSE");
        Button phoneBrowse = new Button("BROWSE");
        Button iTunesBrowse = new Button("BROWSE");

        albumBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

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

        TextField albumTextField = new TextField(formatPaths.get(ReleaseFormat.Album).toString());
        albumTextField.setEditable(false);
        albumTextField.setDisable(true);
        albumTextField.setPrefWidth(400);
        if(!formatPaths.get(ReleaseFormat.Album).toFile().canWrite()) {
            albumTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            albumTextField.setStyle("-fx-control-inner-background: green;");
        }

        TextField mixtapeTextField = new TextField(formatPaths.get(ReleaseFormat.Mixtape).toString());
        mixtapeTextField.setEditable(false);
        mixtapeTextField.setDisable(true);
        mixtapeTextField.setPrefWidth(400);
        if(!formatPaths.get(ReleaseFormat.Mixtape).toFile().canWrite()) {
            mixtapeTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            mixtapeTextField.setStyle("-fx-control-inner-background: green;");
        }

        TextField epTextField = new TextField(formatPaths.get(ReleaseFormat.EP).toString());
        epTextField.setEditable(false);
        epTextField.setDisable(true);
        epTextField.setPrefWidth(400);
        if(!formatPaths.get(ReleaseFormat.EP).toFile().canWrite()) {
            epTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            epTextField.setStyle("-fx-control-inner-background: green;");
        }

        TextField singleTextField = new TextField(formatPaths.get(ReleaseFormat.Single).toString());
        singleTextField.setEditable(false);
        singleTextField.setDisable(true);
        singleTextField.setPrefWidth(400);
        if(!formatPaths.get(ReleaseFormat.Single).toFile().canWrite()) {
            singleTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            singleTextField.setStyle("-fx-control-inner-background: green;");
        }

        TextField soundtrackTextField = new TextField(formatPaths.get(ReleaseFormat.Soundtrack).toString());
        soundtrackTextField.setEditable(false);
        soundtrackTextField.setDisable(true);
        soundtrackTextField.setPrefWidth(400);
        if(!formatPaths.get(ReleaseFormat.Soundtrack).toFile().canWrite()) {
            soundtrackTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            soundtrackTextField.setStyle("-fx-control-inner-background: green;");
        }

        TextField unreleasedTextField = new TextField(formatPaths.get(ReleaseFormat.Unreleased).toString());
        unreleasedTextField.setEditable(false);
        unreleasedTextField.setDisable(true);
        unreleasedTextField.setPrefWidth(400);
        if(!formatPaths.get(ReleaseFormat.Unreleased).toFile().canWrite()) {
            unreleasedTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            unreleasedTextField.setStyle("-fx-control-inner-background: green;");
        }

        TextField aacTextField = new TextField(conversionPaths.get("AAC").toString());
        aacTextField.setEditable(false);
        aacTextField.setDisable(true);
        aacTextField.setPrefWidth(400);
        if(!conversionPaths.get("AAC").toFile().canWrite()) {
            aacTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            aacTextField.setStyle("-fx-control-inner-background: green;");
        }

        TextField mp3TextField = new TextField(conversionPaths.get("MP3").toString());
        mp3TextField.setEditable(false);
        mp3TextField.setDisable(true);
        mp3TextField.setPrefWidth(400);
        if(!conversionPaths.get("MP3").toFile().canWrite()) {
            mp3TextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            mp3TextField.setStyle("-fx-control-inner-background: green;");
        }

        TextField phoneTextField = new TextField(devicePaths.get("Phone").toString());
        phoneTextField.setEditable(false);
        phoneTextField.setDisable(true);
        phoneTextField.setPrefWidth(400);
        if(!devicePaths.get("Phone").toFile().canWrite()) {
            phoneTextField.setStyle("-fx-control-inner-background: red;");
        }
        else {
            phoneTextField.setStyle("-fx-control-inner-background: green;");
        }

        TextField iTunesTextField = new TextField(iTunesMediaFolder.toString());
        iTunesTextField.setEditable(false);
        iTunesTextField.setDisable(true);
        iTunesTextField.setPrefWidth(400);
        if(!iTunesMediaFolder.toFile().canWrite()) {
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

        DirectoryChooser albumDirectory = new DirectoryChooser();
        albumDirectory.setInitialDirectory(formatPaths.get(ReleaseFormat.Album).toFile());

        DirectoryChooser mixtapeDirectory = new DirectoryChooser();
        mixtapeDirectory.setInitialDirectory(formatPaths.get(ReleaseFormat.Mixtape).toFile());

        DirectoryChooser epDirectory = new DirectoryChooser();
        epDirectory.setInitialDirectory(formatPaths.get(ReleaseFormat.EP).toFile());

        DirectoryChooser singleDirectory = new DirectoryChooser();
        singleDirectory.setInitialDirectory(formatPaths.get(ReleaseFormat.Single).toFile());

        DirectoryChooser soundtrackDirectory = new DirectoryChooser();
        soundtrackDirectory.setInitialDirectory(formatPaths.get(ReleaseFormat.Soundtrack).toFile());

        DirectoryChooser unreleasedDirectory = new DirectoryChooser();
        unreleasedDirectory.setInitialDirectory(formatPaths.get(ReleaseFormat.Unreleased).toFile());

        DirectoryChooser aacDirectory = new DirectoryChooser();
        aacDirectory.setInitialDirectory(conversionPaths.get("AAC").toFile());

        DirectoryChooser mp3Directory = new DirectoryChooser();
        mp3Directory.setInitialDirectory(conversionPaths.get("MP3").toFile());

        DirectoryChooser phoneDirectory = new DirectoryChooser();
        phoneDirectory.setInitialDirectory(devicePaths.get("Phone").toFile());

        DirectoryChooser iTunesDirectory = new DirectoryChooser();
        iTunesDirectory.setInitialDirectory(iTunesMediaFolder.toFile());

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);

        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
