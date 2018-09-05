import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
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
    private Path iTunesDirectory;
    private ArrayList<Path> oldFolders;

    public void start(Stage primaryStage) {
        readSetupFile();
        if(!checkWritePermissions()) {
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
                try {
                    addToItunes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                view.getConvertToAAC().setDisable(false);
                view.getConvertToMP3().setDisable(false);
                alert.setContentText("Done!");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
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
                try {
                    setEncoder("AAC Encoder");
                    convert();
                    view.getRelocateAAC().setDisable(false);
                    Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to copy these files to your phone?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                    alert2.showAndWait();

                    if (alert2.getResult() == ButtonType.YES) {
                        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                        alert3.setTitle("Processing");
                        alert3.setHeaderText("Copying to phone...");
                        alert3.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                        alert3.show();
                        copyToPhone();
                        alert3.setContentText("Done!");
                        alert3.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                alert.setContentText("Done!");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
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
                try {
                    relocate("AAC");
                    view.getCleanUpItunes().setDisable(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alert.setContentText("Done!");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
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
                try {
                    setEncoder("MP3 Encoder");
                    convert();
                    view.getRelocateMP3().setDisable(false);
                    Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to rename these MP3 files for usage on a car stereo?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                    alert2.showAndWait();

                    if (alert2.getResult() == ButtonType.YES) {
                        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                        alert3.setTitle("Processing");
                        alert3.setHeaderText("Renaming MP3 files...");
                        alert3.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                        alert3.show();
                        renameForCarStereo();
                        alert3.setContentText("Done!");
                        alert3.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                alert.setContentText("Done!");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
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
                try {
                    relocate("MP3");
                    view.getCleanUpItunes().setDisable(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alert.setContentText("Done!");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
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

    public void addToItunes() throws IOException {
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

    public void convert() throws IOException {
        oldFolders = new ArrayList<>();
        File directory = iTunesDirectory.toFile();
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

    public void cleanUpItunes() throws IOException {
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

    public void relocate(String conversionType) throws IOException {
        File directory = iTunesDirectory.toFile();
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

    public void copyToPhone() throws IOException {
        File directory = iTunesDirectory.toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                if(child.isDirectory() && !oldFolders.contains(child.toPath())) {
                    FileUtils.copyDirectoryToDirectory(child, devicePaths.get("Phone").toFile());
                }
            }
        }
    }

    public void renameForCarStereo() throws Exception {
        File directory = iTunesDirectory.toFile();
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


    public void readSetupFile() {
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
                iTunesDirectory = Paths.get(currentElement.getElementsByTagName("path").item(0).getTextContent());
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    public boolean checkWritePermissions() {
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
        if(!iTunesDirectory.toFile().canWrite()) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
