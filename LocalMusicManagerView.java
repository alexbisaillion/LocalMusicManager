import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class LocalMusicManagerView extends GridPane {
    public ArrayList<NewFile> model;
    public GridPane top;
    public Label title;
    public GridPane middle;
    public GridPane bottom;
    private Button addToQueue;
    private Button finish;

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
        addToQueue = new Button("ADD TO QUEUE");
        addToQueue.setMinWidth(195);
        bottom.add(addToQueue, 0, 0, 1, 1);
        finish = new Button("ADD TO LIBRARY");
        finish.setMinWidth(390);
        bottom.add(finish, 1, 0, 2, 1);
        add(bottom, 0, 2);
    }

    public Button getAddToQueue() { return addToQueue; }
    public Button getFinish() { return finish; }

    public void update() {
        middle = new GridPane();
        middle.setVgap(10);
        for(NewFile nf: model) {
            middle.add(nf.getView(), 0, middle.getChildren().size());
        }
        add(middle, 0, 1);

        for(NewFile nf: model) {

        }
    }
}
