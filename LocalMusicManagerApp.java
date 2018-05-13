import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LocalMusicManagerApp extends Application {
    public ArrayList<NewFile> model;
    public LocalMusicManagerView view;

    public void start(Stage primaryStage) {
        model = new ArrayList<>();
        view = new LocalMusicManagerView(model);

        view.getAddToQueue().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.add(new NewFile());
                view.update();
            }
        });
        primaryStage.setTitle("Local Music Manager");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(view, 615, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
