import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalMusicManagerApp extends Application {
    public ArrayList<NewFile> model;
    public LocalMusicManagerView view;
    public HashMap<ReleaseFormat, Path> formatPaths;
    public HashMap<String, Path> conversionPaths;
    public HashMap<String, Path> devicePaths;

    public void start(Stage primaryStage) {
        readSetupFile();
        model = new ArrayList<>();
        view = new LocalMusicManagerView(model);

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
                                    System.out.println(SortArchiveFile.analyze(p));
                                    System.out.println(SortArchiveFile.correct(p));
                                    System.out.println(SortArchiveFile.move(p));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ".rar":
                            try {
                                SortArchiveFile.extractRar(nf.getSelectedFile(), formatPaths.get(nf.getFormat()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ".7z":
                            try {
                                SortArchiveFile.extract(nf.getSelectedFile(), formatPaths.get(nf.getFormat()), ArchiveStreamFactory.SEVEN_Z);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            if(nf.getFormat() == ReleaseFormat.Single) {
                                try {
                                    SortAudioFile.moveSingle(nf.getSelectedFile(), formatPaths.get(nf.getFormat()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                try {
                                    SortAudioFile.moveUnreleased(nf.getSelectedFile(), formatPaths.get(nf.getFormat()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                    }
                }
            }
        });


        primaryStage.setTitle("Local Music Manager");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(view, 615, 500));
        primaryStage.show();
    }

    public void readSetupFile() {
        formatPaths = new HashMap<>();
        conversionPaths = new HashMap<>();
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

            NodeList deviceList = doc.getElementsByTagName("deviceList");
            for(int i=0; i<deviceList.getLength(); i++) {
                Element currentElement = (Element) deviceList.item(i);
                devicePaths.put(currentElement.getElementsByTagName("device").item(0).getTextContent(), Paths.get(currentElement.getElementsByTagName("path").item(0).getTextContent()));
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
