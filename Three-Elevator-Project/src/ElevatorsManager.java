import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class ElevatorsManager extends Application {

    private static int relocationCalculate(Vector<Integer> requestArray, int newFloor, int referenceFloor, String direction) {
        int relocation = 0, currentFloor, distance;
        ArrayList<Integer> up = new ArrayList<>();
        ArrayList<Integer> down = new ArrayList<>();
        for (Integer request : requestArray)
            if (request < referenceFloor)
                down.add(request);
            else if (request > referenceFloor)
                up.add(request);

        if (newFloor < referenceFloor)
            down.add(newFloor);
        else if (newFloor > referenceFloor)
            up.add(newFloor);
        Collections.sort(down);
        Collections.sort(up);

        if (direction.equals("up")) {
            for (Integer upRequest : up) {
                currentFloor = upRequest;
                distance = Math.abs(currentFloor - referenceFloor);
                relocation += distance;
                referenceFloor = currentFloor;
            }
            if (down.size() != 0)
            {
                relocation += Math.abs(referenceFloor - down.get(0));
                referenceFloor = down.get(0);
            }
        }

        for (Integer downRequest : down) {
            currentFloor = downRequest;
            distance = Math.abs(currentFloor - referenceFloor);
            relocation += distance;
            referenceFloor = currentFloor;
        }

        if (direction.equals("down")) {
            if (up.size() != 0)
            {
                relocation += Math.abs(referenceFloor - up.get(up.size() - 1));
                referenceFloor = up.get(up.size() - 1);
                for (int i = up.size() - 1; i >= 0; i--) {
                    currentFloor = up.get(i);
                    distance = Math.abs(currentFloor - referenceFloor);
                    relocation += distance;
                    referenceFloor = currentFloor;
                }
            }
        }
        return relocation;
}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        int WIDTH = 1500;
        int HEIGHT = 900;
        int apartmentFloor = 15;
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("                       All elevators in 0 floor at first.");
        System.out.println("-------------------------------------------------------------------------------------------");
        Elevator elevator = new Elevator("elevator A", new Vector<>(), 0, "up", 0);
        Elevator elevator2 = new Elevator("elevator B", new Vector<>(), 0, "up", 0);
        Elevator elevator3 = new Elevator("elevator C", new Vector<>(), 0, "up", 0);
        Thread elevatorThread = new Thread(elevator);
        Thread elevator2Thread = new Thread(elevator2);
        Thread elevator3Thread = new Thread(elevator3);
        elevatorThread.start();
        elevator2Thread.start();
        elevator3Thread.start();

        GridPane[] elevatorsButtons = {new GridPane(), new GridPane(), new GridPane(), new GridPane()};
        buttonsCreate(elevator, elevator2, elevator3, elevatorsButtons);
        for (GridPane elevatorsButton : elevatorsButtons) {
            elevatorsButton.setHgap(20.0);
        }
        Rectangle[] elevatorsRectangles = new Rectangle[3];
        elevatorsRectanglesCreate(elevatorsRectangles);


        VBox[] columns = {new VBox(), new VBox(), new VBox(), new VBox()};
        columns[0].setAlignment(Pos.CENTER);
        for (int i = 0; i < 4; i++) {
            columns[i].prefWidthProperty().bind(stage.widthProperty().multiply(0.25));
            if (i > 0)
            {
                columns[i].setAlignment(Pos.BOTTOM_CENTER);
                columns[i].getChildren().add(elevatorsRectangles[i - 1]);
            }

            columns[i].getChildren().add(elevatorsButtons[i]);
        }


        HBox root = new HBox();

        Timeline showing = new Timeline(new KeyFrame(Duration.seconds(0.25), new EventHandler<>() {
            int elevatorNewFloor = 0, elevator2NewFloor = 0, elevator3NewFloor = 0;
            @Override
            public void handle(ActionEvent actionEvent) {
                elevatorNewFloor = elevator.getCurrentFloor();
                elevator2NewFloor = elevator2.getCurrentFloor();
                elevator3NewFloor = elevator3.getCurrentFloor();
                moveRectangle(elevatorsRectangles, elevatorNewFloor, elevator2NewFloor, elevator3NewFloor);
                root.getChildren().clear();
                root.getChildren().addAll(columns);
            }
        }));

        showing.setCycleCount(Timeline.INDEFINITE);
        showing.play();


        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Elevators");
        stage.setScene(scene);
        stage.show();
    }

    private void buttonsCreate(Elevator elevator, Elevator elevator2, Elevator elevator3, GridPane[] elevatorsButtons)
    {
        Button[] elevatorOutButtons = new Button[15];
        Button[] elevatorInButtons = new Button[15];
        Button[] elevator2InButtons = new Button[15];
        Button[] elevator3InButtons = new Button[15];

        for (int i = 0; i < 15; i++) {
            int buttonClickedNumber = i;
            elevatorOutButtons[i] = new Button(String.valueOf(buttonClickedNumber + 1));
            elevatorOutButtons[i].prefWidth(20);
            elevatorInButtons[i] = new Button(String.valueOf(buttonClickedNumber + 1));
            elevatorOutButtons[i].prefWidth(20);
            elevator2InButtons[i] = new Button(String.valueOf(buttonClickedNumber + 1));
            elevatorOutButtons[i].prefWidth(20);
            elevator3InButtons[i] = new Button(String.valueOf(buttonClickedNumber + 1));
            elevatorOutButtons[i].prefWidth(20);
            elevatorOutButtons[i].setOnAction(actionEvent -> {
                int elevatorRelocationNeeded = relocationCalculate(elevator.getRequestArray(), buttonClickedNumber, elevator.getReferenceFloor(), elevator.getDirection());
                int elevator2RelocationNeeded = relocationCalculate(elevator2.getRequestArray(), buttonClickedNumber, elevator2.getReferenceFloor(), elevator2.getDirection());
                int elevator3RelocationNeeded = relocationCalculate(elevator3.getRequestArray(), buttonClickedNumber, elevator3.getReferenceFloor(), elevator3.getDirection());
                int minRelocationNeeded = Math.min(Math.min(elevatorRelocationNeeded, elevator2RelocationNeeded), elevator3RelocationNeeded);
                if (minRelocationNeeded == elevatorRelocationNeeded)
                    elevator.getRequestArray().add(buttonClickedNumber);
                else if (minRelocationNeeded == elevator2RelocationNeeded)
                    elevator2.getRequestArray().add(buttonClickedNumber);
                else
                    elevator3.getRequestArray().add(buttonClickedNumber);
            });
            elevatorInButtons[i].setOnAction(actionEvent -> elevator.getRequestArray().add(buttonClickedNumber));
            elevator2InButtons[i].setOnAction(actionEvent -> elevator2.getRequestArray().add(buttonClickedNumber));
            elevator3InButtons[i].setOnAction(actionEvent -> elevator3.getRequestArray().add(buttonClickedNumber));
        }

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 3; j++) {
                elevatorsButtons[0].add(elevatorOutButtons[i * 3 + j], j, i);
                elevatorsButtons[1].add(elevatorInButtons[i * 3 + j], j, i);
                elevatorsButtons[2].add(elevator2InButtons[i * 3 + j], j, i);
                elevatorsButtons[3].add(elevator3InButtons[i * 3 + j], j, i);
            }
    }

    private void elevatorsRectanglesCreate(Rectangle[] elevators) {
        File input = new File("data/Silver.jpg");
        Image silver = new Image(input.toURI().toString());
        ImagePattern silverPattern = new ImagePattern(silver);
        for (int i = 0; i < 3; i++) {
            elevators[i] = new Rectangle(50, 100);
            elevators[i].setFill(silverPattern);
        }
    }

    private void moveRectangle(Rectangle[] elevators, int elevatorCurrentFloor, int elevator2CurrentFloor, int elevator3CurrentFloor) {
        elevators[0].setTranslateY(-elevatorCurrentFloor * 45);
        elevators[1].setTranslateY(-elevator2CurrentFloor * 45);
        elevators[2].setTranslateY(-elevator3CurrentFloor * 45);
    }
}
