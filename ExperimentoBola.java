import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.animation.Animation;
import java.util.Random;
import javafx.scene.input.KeyCode;
import java.util.Timer;
import java.util.TimerTask;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.shape.Shape;
public class ExperimentoBola extends Application
{
    private int velocidadEnX;
    private int velocidadEnY;
    private int velocidadPlataforma;
    private static int RADIO = 10;
    private int tiempoEnSegundos;
    private int eliminados = 0 ;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage escenario)
    {
        int ANCHO_ESCENA = 500;
        int ALTO_ESCENA = 700;

        Group contenedor = new Group();

        velocidadEnX = 1;
        velocidadEnY = 1;
        tiempoEnSegundos = 70;

        Rectangle plataforma = new Rectangle();
        plataforma.setWidth(90);
        plataforma.setHeight(5);
        plataforma.setTranslateX(225);
        plataforma.setTranslateY(ALTO_ESCENA -20);
        plataforma.setFill(Color.BLUE);
        contenedor.getChildren().add(plataforma);
        velocidadPlataforma = 1;

        Random aleatorio = new Random();

        Label tiempoPasado = new Label("0");
        contenedor.getChildren().add(tiempoPasado);

        Scene escena = new Scene(contenedor, ANCHO_ESCENA, ALTO_ESCENA);
        escenario.setScene(escena);
        escenario.show();

        //MUESTRA EL Nº DE  BARRITAS QUE SE VAN ELIMINANDO,
        Label barritasEliminadas = new Label();
        barritasEliminadas.setTranslateX(2);
        barritasEliminadas.setTranslateY(20);
        contenedor.getChildren().add(barritasEliminadas);

        ////////////////////////////////////////*****************************************************************************

        ArrayList<Rectangle> rectangulos = new ArrayList<>();
        int ALTO_BARRITAS = 20;
        int EJE_Y = 50;//-------------POSICIÓN ININCIAL DE LA 1º FILA DE BARRITAS EN EL EJE Y.
        int NUM_FILAS_EN_Y = 4;// ---ES EL Nº DE FILAS EN EL EJE Y.
        int BARRITAS_EN_Y = 20;
        int longitudBarrita = aleatorio.nextInt(160) +70;//-- CADA BARRITA TIENE UNA LONGITUD ALEATORIA.
        int val = 0;
        while(val < BARRITAS_EN_Y){
            int largoR = aleatorio.nextInt(60) +60; //largo de la barra; aleatorio.
            int coorX = aleatorio.nextInt(ANCHO_ESCENA - (longitudBarrita *2)) +longitudBarrita; //coordenada X de la barra, aleatoria.
            int coorY = aleatorio.nextInt(ALTO_ESCENA /2) + ALTO_BARRITAS;
            Color color = new Color(aleatorio.nextDouble(), aleatorio.nextDouble(), aleatorio.nextDouble(), aleatorio.nextDouble());

            Rectangle rectangulo = new Rectangle();
            if(rectangulos.size() == 0){                     
                rectangulo.setLayoutX(coorX);
                rectangulo.setLayoutY(coorY);
                rectangulo.setWidth(longitudBarrita);//LONGITUD ALEATORIA DE LAS BARRITAS, EXCEPTO LA DE LA ÚLTIMA BARRITA.
                rectangulo.setHeight(ALTO_BARRITAS);
                contenedor.getChildren().add(rectangulo);
                rectangulo.setStroke(Color.BLACK);
                rectangulo.setFill(Color.YELLOW);
                rectangulo.setVisible(true);

                rectangulos.add(rectangulo);
            }
            else{ 
                while(rectangulos.size() < BARRITAS_EN_Y){
                    boolean esValido = true;
                    boolean add = false;
                    while(esValido == true && add == false){
                        longitudBarrita = aleatorio.nextInt(160) +70;
                        Color color2 = new Color(aleatorio.nextDouble(), aleatorio.nextDouble(), aleatorio.nextDouble(), aleatorio.nextDouble());
                        Rectangle rectangulo2 = new Rectangle();
                        coorX = aleatorio.nextInt(ANCHO_ESCENA - (largoR *2)) +largoR;
                        coorY = aleatorio.nextInt(ALTO_ESCENA /2) + ALTO_BARRITAS;
                        rectangulo2.setLayoutX(coorX);
                        rectangulo2.setLayoutY(coorY);
                        rectangulo2.setWidth(longitudBarrita);
                        rectangulo2.setHeight(ALTO_BARRITAS);
                        rectangulo2.setStroke(Color.BLACK);
                        rectangulo2.setFill(color2);

                        rectangulo2.setVisible(true);

                        for(int i = 0; i < rectangulos.size(); i ++){
                            Shape c = Shape.intersect(rectangulos.get(i), rectangulo2);
                            if(c.getBoundsInParent().getWidth() != -1){
                                esValido = false;
                            }

                        }
                        if (esValido == true ){
                            contenedor.getChildren().add(rectangulo2);
                            rectangulos.add(rectangulo2);
                            add = true;
                        }
                    }

                    // Lo comparamos contra todos los existentes
                    // Al final de TODAS las comparaciones estamos en disposicion de saber si el rectangulo es valido
                    // Si es valido lo añadimos
                    // Si no es valido tenemos que elegir un nuevo rect
                    val ++;
                }

            }
            val ++;//Shape c = Shape.intersect(rectangulo, rectangulo2);
        }       //////////////////////////////////////////////////////*********************************************************

        Circle circulo = new Circle();
        circulo.setFill(Color.RED);  
        circulo.setRadius(RADIO);
        circulo.setCenterX(20 + aleatorio.nextInt(500 - 40));
        circulo.setCenterY(50);
        contenedor.getChildren().add(circulo);

        Timeline timeline = new Timeline();
        KeyFrame keyframe = new KeyFrame(Duration.seconds(0.01), event -> {

                    // Controlamos si la bola rebota a ziquierda o derecha
                    if (circulo.getBoundsInParent().getMinX() <= 0 ||
                    circulo.getBoundsInParent().getMaxX() >= escena.getWidth()) {
                        velocidadEnX = -velocidadEnX;                              
                    }

                    // Conrolamos si la bola rebota arriba y abajo
                    if (circulo.getBoundsInParent().getMinY() <= 0) {
                        velocidadEnY = -velocidadEnY;
                    }

                    if (circulo.getBoundsInParent().getMaxY() == plataforma.getBoundsInParent().getMinY()) {
                        double centroEnXDeLaBola = circulo.getBoundsInParent().getMinX() + RADIO;
                        double minEnXDeLaPlataforma = plataforma.getBoundsInParent().getMinX();
                        double maxEnXDeLaPlataforma = plataforma.getBoundsInParent().getMaxX();
                        if ((centroEnXDeLaBola >= minEnXDeLaPlataforma) &&
                        (centroEnXDeLaBola <= maxEnXDeLaPlataforma)) {
                            //La bola esta sobre la plataforma
                            velocidadEnY = -velocidadEnY;
                        }
                    }

                    circulo.setTranslateX(circulo.getTranslateX() + velocidadEnX);
                    circulo.setTranslateY(circulo.getTranslateY() + velocidadEnY);

                    plataforma.setTranslateX(plataforma.getTranslateX() + velocidadPlataforma);
                    if (plataforma.getBoundsInParent().getMinX() == 0  || 
                    plataforma.getBoundsInParent().getMaxX() == escena.getWidth()) {
                        velocidadPlataforma = 0;
                    }

                    // Actualizamos la etiqueta del tiempo
                    int minutos = tiempoEnSegundos / 60;
                    int segundos = tiempoEnSegundos % 60;
                    tiempoPasado.setText(minutos + ":" + segundos);                        

                    // Comrpobamos si el juego debe detenerse
                    if (circulo.getBoundsInParent().getMinY() > escena.getHeight()) {
                        Label mensajeGameOver = new Label("Game over");
                        mensajeGameOver.setTranslateX(escena.getWidth() / 2);
                        mensajeGameOver.setTranslateY(escena.getHeight() / 2);
                        contenedor.getChildren().add(mensajeGameOver);
                        timeline.stop();
                    }

                    /////////////////////////////// PARA ELIMINAR BARRITAS AL COLISIONAR CON LA BOLA.

                    for(int i = 0; i < rectangulos.size(); i ++ ){
                        Shape c = Shape.intersect(rectangulos.get(i), circulo);

                        //COORDENADAS LATERALES DE CADA BARRITA Y DE LA BOLA.
                        double longitud_Barrita = rectangulos.get(i).getWidth();
                        double minimoDe_X_Barrita =  rectangulos.get(i).getBoundsInParent().getMinX();
                        double maximo_X_Barrita =  minimoDe_X_Barrita + longitud_Barrita;
                        double minimoDe_Y_Barrita = rectangulos.get(i).getBoundsInParent().getMinY();
                        double maximoDe_Y_Barrita = rectangulos.get(i).getBoundsInParent().getMaxY();

                        double maximoDe_X_Bolita = circulo.getBoundsInParent().getMaxX() -0.5;
                        double minimoDe_X_Bolita =  circulo.getBoundsInParent().getMinX() -0.5;
                        double maximoDe_Y_Bolita = circulo.getBoundsInParent().getMaxY() ;
                        double minimoDe_Y_Bolita = circulo.getBoundsInParent().getMinY() ;

                        if(c.getBoundsInParent().getWidth() != -1){
                            rectangulos.get(i).setFill(Color.WHITE);
                            rectangulos.get(i).setStroke(Color.WHITE);
                            rectangulos.remove(i);

                            if( (maximoDe_X_Bolita ) == minimoDe_X_Barrita && maximoDe_Y_Bolita >= minimoDe_Y_Barrita &&
                            minimoDe_Y_Bolita <= maximoDe_Y_Barrita ){
                                velocidadEnX = -velocidadEnX;                                  
                                eliminados ++;
                            }
                            else if( (maximoDe_X_Bolita +1) == minimoDe_X_Barrita && maximoDe_Y_Bolita >= minimoDe_Y_Barrita &&
                            minimoDe_Y_Bolita <= maximoDe_Y_Barrita ){
                                velocidadEnX = -velocidadEnX;                                  
                                eliminados ++;
                            }
                            else if( (minimoDe_X_Bolita ) == (minimoDe_X_Barrita + longitud_Barrita)
                            && maximoDe_Y_Bolita >= minimoDe_Y_Barrita &&
                            minimoDe_Y_Bolita <= maximoDe_Y_Barrita){
                                velocidadEnX = -velocidadEnX;
                                eliminados ++;
                            }
                            else{
                                velocidadEnY = -velocidadEnY;
                                eliminados ++;
                            }
                            barritasEliminadas.setText("Barritas eliminadas; " +eliminados);
                        }
                    }

                });  
        timeline.getKeyFrames().add(keyframe);

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();     

        escena.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.RIGHT && 
                plataforma.getBoundsInParent().getMaxX() != escena.getWidth()) {
                    velocidadPlataforma = 1;
                }
                else if (event.getCode() == KeyCode.LEFT && 
                plataforma.getBoundsInParent().getMinX() != 0) {
                    velocidadPlataforma = -1;
                }
            });

        TimerTask tarea = new TimerTask() {
                @Override
                public void run() {
                    tiempoEnSegundos++;
                }                        
            };
        Timer timer = new Timer();
        timer.schedule(tarea, 0, 1000);

    }

}

