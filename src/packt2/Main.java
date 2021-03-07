package packt2;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int V = 800;
    private static final int V_1 = 600;

    private double anchorX,anchorY;
    private double anchorAngleX=0;
    private double anchorAngleY=0;
    private final DoubleProperty angleX=new SimpleDoubleProperty ( 0 );
    private final DoubleProperty angleY=new SimpleDoubleProperty ( 0 );


    @Override
    public void start(Stage primaryStage) throws Exception {
        Box box = new Box (100, 20, 50);
        SmartGroup group = new SmartGroup ();
        group.getChildren ( ).add (box);
        Camera camera = new PerspectiveCamera ( );
        Scene scene = new Scene (group, V, V_1);
        scene.setFill (Color.SILVER);
        scene.setCamera (camera);
        group.translateXProperty ( ).set (V / 2);
        group.translateYProperty ( ).set (V_1 / 2);
        group.translateZProperty ( ).set (-850);
        initMouseControl( group, scene,primaryStage);

        primaryStage.addEventHandler (KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode ( )) {
                case W:

                    group.translateZProperty ( ).set (group.getTranslateZ ( ) + 100);
                    break;
                case S:
                    group.translateZProperty ( ).set (group.getTranslateZ ( ) - 100);
                    break;
                case Q:
                    group.rotateByX (10);
                    break;
                case E:
                    group.rotateByX (-10);
                    break;
                case NUMPAD6:
                    group.rotateByY (10);
                    break;
                case NUMPAD4:
                    group.rotateByY (-10);
                    break;


            }
        });



        primaryStage.setTitle ("Cuboid Transformation 3D ");
        primaryStage.setScene (scene);
        primaryStage.show ( );

    }
    private void initMouseControl(SmartGroup group, Scene scene,Stage stage){
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms ().addAll (
                xRotate=new Rotate ( 0,Rotate.X_AXIS ),
                yRotate=new Rotate ( 0,Rotate.Y_AXIS )
        );
        xRotate.angleProperty ().bind (angleX);
        yRotate.angleProperty ().bind (angleY);
        scene.setOnMousePressed (event->{
            anchorX=event.getSceneX ();
            anchorY=event.getSceneY ();
            anchorAngleX=angleX.get ();
            anchorAngleY=angleY.get ();

        });
        scene.setOnMouseDragged (event->{
            angleX.set (anchorAngleX-(anchorY-event.getSceneY ()));
            angleY.set (anchorAngleY+anchorX-event.getSceneX ());

        });
        stage.addEventHandler (ScrollEvent.SCROLL,event->{
            double delta=event.getDeltaY ();
            group.translateZProperty ().set (group.getTranslateZ()+delta);
        });




    }



    public static void main(String[] args) {
        launch (args);}
    class SmartGroup extends Group {
        Rotate r;
        Transform t = new Rotate ( );

        void rotateByX(int ang) {
            r = new Rotate (ang, Rotate.X_AXIS);
            t = t.createConcatenation (r);
            this.getTransforms ( ).clear ( );
            this.getTransforms ( ).addAll (t);
        }

        void rotateByY(int ang) {
            r = new Rotate (ang, Rotate.Y_AXIS);
            t = t.createConcatenation (r);
            this.getTransforms ( ).clear ( );
            this.getTransforms ( ).addAll (t);
        }
    }
}

