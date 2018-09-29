import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class LocalMusicManagerApp extends Application {
    private ArrayList<NewFile> model;
    private SetupModel setupModel;
    private LocalMusicManagerView view;
    private ArrayList<Path> sortedFiles;
    private ArrayList<Path> oldFolders;

    public void start(Stage primaryStage) {
        try {
            readSetupFile();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        if(!setupModel.checkWritePermissions()) {
            SetupDialog setupDialog = new SetupDialog(setupModel);
            setupDialog.showAndWait();

            HashMap<ReleaseFormat, Path> revisedFormatPaths = new HashMap<>();
            HashMap<String, Path> revisedConversionPaths = new HashMap<>();
            HashMap<String, Path> revisedDevicePaths = new HashMap<>();
            Path revisediTunesMediaFolder;

            revisedFormatPaths.put(ReleaseFormat.Album, setupDialog.getAlbumPath());
            revisedFormatPaths.put(ReleaseFormat.Mixtape, setupDialog.getMixtapePath());
            revisedFormatPaths.put(ReleaseFormat.EP, setupDialog.getEpPath());
            revisedFormatPaths.put(ReleaseFormat.Single, setupDialog.getSinglePath());
            revisedFormatPaths.put(ReleaseFormat.Soundtrack, setupDialog.getSoundtrackPath());
            revisedFormatPaths.put(ReleaseFormat.Unreleased, setupDialog.getUnreleasedPath());

            revisedConversionPaths.put("AAC", setupDialog.getAacPath());
            revisedConversionPaths.put("MP3", setupDialog.getMp3Path());

            revisedDevicePaths.put("Phone", setupDialog.getPhonePath());
            revisedDevicePaths.put("PC", setupDialog.getPCPath());

            revisediTunesMediaFolder = setupDialog.getiTunesPath();

            setupModel = new SetupModel(revisedFormatPaths, revisedConversionPaths, revisedDevicePaths, revisediTunesMediaFolder);

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
                view.getAddToQueue().setDisable(true);
                view.getAddToLibrary().setDisable(true);
                view.getCompleteAllActions().setDisable(true);
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
                        addToLibrary();
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
                view.getConvertToMP3().setDisable(true);
                Date currentDate = new Date();

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
                Runnable confirmCopyToPC = new Runnable() {
                    @Override
                    public void run() {
                        Alert alert4 = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to copy these files to your PC?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                        alert4.showAndWait();
                        if (alert4.getResult() == ButtonType.YES) {
                            Alert alert5 = new Alert(Alert.AlertType.INFORMATION);
                            alert5.setTitle("Processing");
                            alert5.setHeaderText("Copying to PC...");
                            alert5.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                            alert5.show();
                            Runnable update2 = new Runnable() {
                                @Override
                                public void run() {
                                    alert5.setContentText("Done!");
                                    alert5.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                                }
                            };
                            Runnable r2 = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        copyToPC();
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
                Runnable confirmDeleteFromItunes = new Runnable() {
                    @Override
                    public void run() {
                        Alert alert6 = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to delete the converted songs from iTunes?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                        alert6.showAndWait();
                        if (alert6.getResult() == ButtonType.YES) {
                            Alert alert7 = new Alert(Alert.AlertType.INFORMATION);
                            alert7.setTitle("Processing");
                            alert7.setHeaderText("Deleting from iTunes...");
                            alert7.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                            alert7.show();
                            Runnable update3 = new Runnable() {
                                @Override
                                public void run() {
                                    alert7.setContentText("Done!");
                                    alert7.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                                }
                            };
                            Runnable r2 = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        deleteFromItunesByDate(currentDate);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Platform.runLater(update3);
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
                            Platform.runLater(confirmDeleteFromItunes);
                            Platform.runLater(confirmCopyToPhone);
                            Platform.runLater(confirmCopyToPC);
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
                        view.getConvertToMP3().setDisable(false);
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
                view.getConvertToAAC().setDisable(true);
                Date currentDate = new Date();

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
                Runnable confirmDeleteFromItunes = new Runnable() {
                    @Override
                    public void run() {
                        Alert alert4 = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to delete the converted songs from iTunes?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                        alert4.showAndWait();
                        if (alert4.getResult() == ButtonType.YES) {
                            Alert alert5 = new Alert(Alert.AlertType.INFORMATION);
                            alert5.setTitle("Processing");
                            alert5.setHeaderText("Deleting from iTunes...");
                            alert5.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                            alert5.show();
                            Runnable update3 = new Runnable() {
                                @Override
                                public void run() {
                                    alert5.setContentText("Done!");
                                    alert5.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                                }
                            };
                            Runnable r2 = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        deleteFromItunesByDate(currentDate);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Platform.runLater(update3);
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
                            Platform.runLater(confirmDeleteFromItunes);
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
                        view.getConvertToAAC().setDisable(false);
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

        view.getCompleteAllActions().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.disableAll();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Processing");
                alert.setHeaderText("Completing all actions...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                alert.show();
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
                        view.getCompleteAllActions().setDisable(false);
                        alert.setContentText("Done!");
                        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                    }
                };
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            completeAllActions();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(update);
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        });

        primaryStage.setTitle("Local Music Manager");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(view, 1075, 500));
        primaryStage.show();
    }

    private void addToLibrary() {
        for(NewFile nf: model) {
            switch(nf.getExtension()) {
                case ".zip":
                    try {
                        Path p = SortArchiveFile.extractZIP(nf.getSelectedFile(), setupModel.getFormatPaths().get(nf.getFormat()));
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
                        Path p = SortArchiveFile.extractRar(nf.getSelectedFile(), setupModel.getFormatPaths().get(nf.getFormat()));
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
                        Path p = SortArchiveFile.extract7z(nf.getSelectedFile(), setupModel.getFormatPaths().get(nf.getFormat()));
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
                            Path p = SortAudioFile.moveSingle(nf.getSelectedFile(), setupModel.getFormatPaths().get(nf.getFormat()));
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
                            Path p = SortAudioFile.moveUnreleased(nf.getSelectedFile(), setupModel.getFormatPaths().get(nf.getFormat()));
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

    private void addToItunesFromPC() throws IOException {
        String[] args  = {"python", "src/scripts/addToLibrary.py", setupModel.getDevicePaths().get("PC").toString()};
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

    private void convert() throws IOException {
        oldFolders = new ArrayList<>();
        File directory = setupModel.getiTunesMediaFolder().toFile();
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

    private void relocate(String conversionType) throws IOException {
        File directory = setupModel.getiTunesMediaFolder().toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                if(child.isDirectory() && !oldFolders.contains(child.toPath())) {
                    FileUtils.copyDirectoryToDirectory(child, setupModel.getConversionPaths().get(conversionType).toFile());
                    FileUtils.deleteDirectory(child);
                }
            }
        }
    }

    private void copyToPhone() throws IOException {
        File directory = setupModel.getiTunesMediaFolder().toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                if(child.isDirectory() && !oldFolders.contains(child.toPath())) {
                    FileUtils.copyDirectoryToDirectory(child, setupModel.getDevicePaths().get("Phone").toFile());
                }
            }
        }
    }

    private void copyToPC() throws IOException {
        File directory = setupModel.getiTunesMediaFolder().toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                if(child.isDirectory() && !oldFolders.contains(child.toPath())) {
                    FileUtils.copyDirectoryToDirectory(child, setupModel.getDevicePaths().get("PC").toFile());
                }
            }
        }
    }

    private void renameForCarStereo() throws Exception {
        File directory = setupModel.getiTunesMediaFolder().toFile();
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

    private void readSetupFile() throws ParserConfigurationException, IOException, SAXException {
        HashMap<ReleaseFormat, Path> formatPaths = new HashMap<>();
        HashMap<String, Path> conversionPaths = new HashMap<>();
        HashMap<String, Path> devicePaths = new HashMap<>();
        Path iTunesMediaFolder;

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
        Element currentElement = (Element) iTunesList.item(0);
        iTunesMediaFolder = Paths.get(currentElement.getElementsByTagName("path").item(0).getTextContent());

        if(!SetupModel.isComplete(formatPaths, conversionPaths, devicePaths, iTunesMediaFolder)) {
            SetupDialog setupDialog = new SetupDialog(formatPaths, conversionPaths, devicePaths, iTunesMediaFolder);
            setupDialog.showAndWait();

            HashMap<ReleaseFormat, Path> revisedFormatPaths = new HashMap<>();
            HashMap<String, Path> revisedConversionPaths = new HashMap<>();
            HashMap<String, Path> revisedDevicePaths = new HashMap<>();
            Path revisediTunesMediaFolder;

            revisedFormatPaths.put(ReleaseFormat.Album, setupDialog.getAlbumPath());
            revisedFormatPaths.put(ReleaseFormat.Mixtape, setupDialog.getMixtapePath());
            revisedFormatPaths.put(ReleaseFormat.EP, setupDialog.getEpPath());
            revisedFormatPaths.put(ReleaseFormat.Single, setupDialog.getSinglePath());
            revisedFormatPaths.put(ReleaseFormat.Soundtrack, setupDialog.getSoundtrackPath());
            revisedFormatPaths.put(ReleaseFormat.Unreleased, setupDialog.getUnreleasedPath());

            revisedConversionPaths.put("AAC", setupDialog.getAacPath());
            revisedConversionPaths.put("MP3", setupDialog.getMp3Path());

            revisedDevicePaths.put("Phone", setupDialog.getPhonePath());
            revisedDevicePaths.put("PC", setupDialog.getPCPath());

            revisediTunesMediaFolder = setupDialog.getiTunesPath();

            setupModel = new SetupModel(revisedFormatPaths, revisedConversionPaths, revisedDevicePaths, revisediTunesMediaFolder);

        }
        else {
            setupModel = new SetupModel(formatPaths, conversionPaths, devicePaths, iTunesMediaFolder);
        }
    }

    private void deleteFromItunesByDate(Date currentDate) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String[] args  = {"python", "src/scripts/deleteFromItunesByDate.py", setupModel.getiTunesMediaFolder().toString(), dateFormat.format(currentDate) + " GMT+0000"};
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

    private void completeAllActions() throws Exception {
        Date addToLibraryDate = new Date();
        addToLibrary();
        addToItunes();

        setEncoder("AAC Encoder");
        Date convertToAACDate = new Date();
        convert();
        copyToPhone();
        copyToPC();
        deleteFromItunesByDate(convertToAACDate);
        relocate("AAC");

        setEncoder("MP3 Encoder");
        Date convertToMP3Date = new Date();
        convert();
        renameForCarStereo();
        deleteFromItunesByDate(convertToMP3Date);
        relocate("MP3");

        deleteFromItunesByDate(addToLibraryDate);
        addToItunesFromPC();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
