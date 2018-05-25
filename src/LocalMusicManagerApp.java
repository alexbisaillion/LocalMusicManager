import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LocalMusicManagerApp extends Application {
    public ArrayList<NewFile> model;
    public LocalMusicManagerView view;
    public HashMap<ReleaseFormat, Path> formatPaths;
    public HashMap<String, Path> conversionPaths;
    public HashMap<String, Path> devicePaths;
    public ArrayList<Path> sortedFiles;
    public Path iTunesDirectory;
    public ArrayList<Path> oldFolders;

    public void start(Stage primaryStage) {
        readSetupFile();
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

        view.getFinish().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(NewFile nf: model) {
                    switch(nf.getExtension()) {
                        case ".zip":
                            try {
                                Path p = SortArchiveFile.extract(nf.getSelectedFile(), formatPaths.get(nf.getFormat()), ArchiveStreamFactory.ZIP);
                                if(p != null) {
                                    if(nf.getNewArtistTag() != null) {
                                        System.out.println(SortArchiveFile.changeArtist(p, nf.getNewArtistTag()));
                                    }
                                    if(nf.getNewAlbumArtistTag() != null) {
                                        System.out.println(SortArchiveFile.changeAlbumArtist(p, nf.getNewAlbumArtistTag()));
                                    }
                                    if(nf.getNewGenreTag() != null) {
                                        System.out.println(SortArchiveFile.changeGenre(p, nf.getNewGenreTag()));
                                    }
                                    System.out.println(SortArchiveFile.analyze(p));
                                    System.out.println(SortArchiveFile.correct(p));
                                    sortedFiles.add(SortArchiveFile.move(p));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ".rar":
                            try {
                                Path p = SortArchiveFile.extractRar(nf.getSelectedFile(), formatPaths.get(nf.getFormat()));
                                if(p != null) {
                                    if(nf.getNewArtistTag() != null) {
                                        System.out.println(SortArchiveFile.changeArtist(p, nf.getNewArtistTag()));
                                    }
                                    if(nf.getNewAlbumArtistTag() != null) {
                                        System.out.println(SortArchiveFile.changeAlbumArtist(p, nf.getNewAlbumArtistTag()));
                                    }
                                    if(nf.getNewGenreTag() != null) {
                                        System.out.println(SortArchiveFile.changeGenre(p, nf.getNewGenreTag()));
                                    }
                                    System.out.println(SortArchiveFile.analyze(p));
                                    System.out.println(SortArchiveFile.correct(p));
                                    sortedFiles.add(SortArchiveFile.move(p));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ".7z":
                            try {
                                Path p = SortArchiveFile.extract(nf.getSelectedFile(), formatPaths.get(nf.getFormat()), ArchiveStreamFactory.SEVEN_Z);
                                if(p != null) {
                                    if(nf.getNewArtistTag() != null) {
                                        System.out.println(SortArchiveFile.changeArtist(p, nf.getNewArtistTag()));
                                    }
                                    if(nf.getNewAlbumArtistTag() != null) {
                                        System.out.println(SortArchiveFile.changeAlbumArtist(p, nf.getNewAlbumArtistTag()));
                                    }
                                    if(nf.getNewGenreTag() != null) {
                                        System.out.println(SortArchiveFile.changeGenre(p, nf.getNewGenreTag()));
                                    }
                                    System.out.println(SortArchiveFile.analyze(p));
                                    System.out.println(SortArchiveFile.correct(p));
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
                                    System.out.println(SortAudioFile.analyze(p));
                                    System.out.println(SortAudioFile.correct(p));
                                    sortedFiles.add(p);
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
                                    System.out.println(SortAudioFile.analyze(p));
                                    System.out.println(SortAudioFile.correct(p));
                                    sortedFiles.add(p);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                    }
                }
                view.getAddToItunes().setDisable(false);
            }
        });

        view.getAddToItunes().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    addToItunes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                view.getConvertToAAC().setDisable(false);
                view.getConvertToMP3().setDisable(false);
            }
        });

        view.getConvertToAAC().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    setEncoder("AAC Encoder");
                    convert();
                    //relocate("AAC");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        view.getConvertToMP3().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    //setEncoder("MP3 Encoder");
                    //convert();
                    //relocate("MP3");
                    relocate("AAC");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        primaryStage.setTitle("Local Music Manager");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(view, 945, 500));
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
            String[] args  = {"python", "src/scripts/convert.py", p.toString()};
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

    public void relocate(String conversionType) throws IOException {
        File directory = iTunesDirectory.toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                if(child.isDirectory() && !oldFolders.contains(child.toPath())) {
                    FileUtils.copyDirectory(child.getParentFile(), conversionPaths.get(conversionType).toFile());
                }
            }
        }
        System.out.println(devicePaths);
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

    public static void main(String[] args) {
        launch(args);
    }
}
