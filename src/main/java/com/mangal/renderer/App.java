package com.mangal.renderer;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private double[] x = {0};
    private double[] y = {0};
    private Parent createContent() {
        Canvas canvas = new Canvas(600, 600);
        draw(canvas);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(canvas);

        canvas.setOnMouseDragged(e -> {
            double yi = 180.0 / canvas.getHeight();
            double xi = 180.0 / canvas.getWidth();
            x[0] = e.getX() * xi;
            y[0] = -e.getY() * yi;
            draw(canvas);
        });
        
        return stackPane;
    }

    private void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        List<Triangle> tris = new ArrayList<>();
        tris.add(new Triangle(new Vertex(100, 100, 100),
                new Vertex(-100, -100, 100),
                new Vertex(-100, 100, -100),
                Color.WHITE));
        tris.add(new Triangle(new Vertex(100, 100, 100),
                new Vertex(-100, -100, 100),
                new Vertex(100, -100, -100),
                Color.RED));
        tris.add(new Triangle(new Vertex(-100, 100, -100),
                new Vertex(100, -100, -100),
                new Vertex(100, 100, 100),
                Color.GREEN));
        tris.add(new Triangle(new Vertex(-100, 100, -100),
                new Vertex(100, -100, -100),
                new Vertex(-100, -100, 100),
                Color.BLUE));

        Matrix3 transform = getTransform();

        gc.setStroke(Color.WHITE);

        for (Triangle t : tris) {
            Vertex v1 = transform.transform(t.v1);
            v1.x += canvas.getWidth() / 2;
            v1.y += canvas.getHeight() / 2;

            Vertex v2 = transform.transform(t.v2);
            v2.x += canvas.getWidth() / 2;
            v2.y += canvas.getHeight() / 2;

            Vertex v3 = transform.transform(t.v3);
            v3.x += canvas.getWidth() / 2;
            v3.y += canvas.getHeight() / 2;

            gc.beginPath();
            gc.moveTo(v1.x, v1.y);
            gc.lineTo(v2.x, v2.y);
            gc.lineTo(v3.x, v3.y);
            gc.closePath();

            gc.stroke();
        }
    }

    private Matrix3 getTransform() {
        double heading = Math.toRadians(x[0]);
        Matrix3 headingTransform = new Matrix3(new double[] {
                Math.cos(heading), 0, -Math.sin(heading),
                0, 1, 0,
                Math.sin(heading), 0, Math.cos(heading)
        });

        double pitch = Math.toRadians(y[0]);
        Matrix3 pitchTransform = new Matrix3(new double[] {
                1, 0, 0,
                0, Math.cos(pitch), Math.sin(pitch),
                0, -Math.sin(pitch), Math.cos(pitch)
        });

        return headingTransform.multiply(pitchTransform);
    }
    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
