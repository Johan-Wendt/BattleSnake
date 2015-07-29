/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import javafx.animation.Interpolator;

/**
 *
 * @author johanwendt
 */
public class MainBoard extends Application {
    //Set fields.
    private Pane pane = new Pane();
    private Circle playerOne;
    private boolean alive = true;
    private static double GAME_SPEED = 3000;
    private static double GRID_WIDTH = 804;
    private static double GRID_HEIGTH = 600;
    private Line currentLine;
    private Line turningLine;
    private PathTransition currentPath = new PathTransition();
    private ArrayList<Line> verticalGrid = new ArrayList();
    private ArrayList<Line> horizontalGrid = new ArrayList();
    private PathTransition path;
    
    
    public MainBoard () {
        
    }
    
    @Override
    public void start(Stage BattleStage) throws InterruptedException {
        Scene scene = new Scene(pane, GRID_WIDTH, GRID_HEIGTH);
        makeGameGrid();
        makePlayer();
        makePath();
        randomMove();
        BattleStage.setScene(scene);
        BattleStage.show();
        
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DOWN: moveDown(); break;
                case RIGHT: moveRight(); break;
                case LEFT: moveLeft(); break;
                case UP: moveUp(); break;
                default:System.out.println("default");
            }
        });
        
        
        

        
        
    
    }
    /**
     * This creates the game grid
     */
    public void makeGameGrid() {

        //Make the vertical grid
        
        for(int i = 6; i < GRID_WIDTH; i += 6) {
        Line line = new Line(i, 0, i, GRID_HEIGTH);
        line.setStroke(Color.TRANSPARENT);
        pane.getChildren().add(line);
        verticalGrid.add(line);
        }
        
        //Make the horizontal grid
        
        for(int i = 6; i < GRID_HEIGTH; i += 6) {
        Line line = new Line(0, i, GRID_WIDTH, i);
        line.setStroke(Color.TRANSPARENT);
        pane.getChildren().add(line);
        horizontalGrid.add(line);
        }

    }
    
    /**
     * Create the path that the player is moving along. The path is the current
     * line the player is moving along in the grid.
     */
    public void makePath() {
        path = new PathTransition();
        path.setDuration(Duration.millis(GAME_SPEED));
        path.setInterpolator(Interpolator.LINEAR);
        path.setNode(playerOne);
        path.setCycleCount(Timeline.INDEFINITE);
        path.setAutoReverse(true);
        
    }
    
    /**
     * This creates the player. Possible better to make this dependent
     * om an instance of the not yet created Player-class
     */
    public void makePlayer() {
        playerOne = new Circle(100, 100, 6);
        pane.getChildren().add(playerOne);
        
        
    }
    
    /**
     * Returns the vertical line that is closest to the player moving horizontal
     * for the moment.Used for turning left or right.
     * @return the closest vertical line.
     */
    public Line closestVerticalLine() {
        for(Line line:verticalGrid) {
            if(line.getEndX() >= getCurrentPixelX()) {
                return line;
            }
        }
        return currentLine;
    }
    
     /**
     * Returns the vertical line that is closest to the player moving vertical
     * for the moment. Used for turning upp or down.
     */
    public Line closestHorizontalLine() {

        for(Line line:horizontalGrid) {
            if(line.getEndY() >= getCurrentPixelY()) {
                return line;
            }
        }
        return currentLine;
    }
    
    /**
     * Get the current x-position of the player.
     * @return the current x-position of the player
     */
    public double getCurrentPixelX() {
        return (currentPath.getCurrentTime().toMillis()*GRID_WIDTH)/GAME_SPEED;

    }
    
     /**
     * Get the current y-position of the player.
     * @return the current x-position of the player
     */
    public double getCurrentPixelY() {
        return (currentPath.getCurrentTime().toMillis()*GRID_HEIGTH)/GAME_SPEED;
    }
    
    
    /**
     * Initiate movement for starting the player. Should possibly be made 
     * random. Other possible use is teleportation. N.B. Not random yet!
     */
    public void randomMove() {
        makePath();
        currentLine = horizontalGrid.get(15);
        turningLine = currentLine;
        currentPath = path;
        path.setPath(turningLine);


        
        //For testing. To be removed for real game (Or kept as a bonus "No walls".
        path.setCycleCount(Timeline.INDEFINITE);
        
        
        path.play();
        currentLine = turningLine;
    }
    
    /**
     * Makes the player move right. For the future this should probable move to
     * the player-class. Or possibly just get the playr and its direction.
     */
    public void moveRight() {
        makePath();
        double startTime = (currentLine.getEndX()/GRID_WIDTH)*GAME_SPEED;
        turningLine = closestHorizontalLine();
        currentPath = path;
        path.setPath(turningLine);   
        path.playFrom(Duration.millis(startTime));
        currentLine = turningLine;
 
    }
    
    /**
     * Makes the player move left. For the future this should probable move to
     * the player-class. Or possibly just get the playr and its direction.
     */
    public void moveLeft() {
        makePath();
        double startTime = (currentLine.getEndX()/GRID_WIDTH)*GAME_SPEED;
        turningLine = closestHorizontalLine();
        currentPath = path;
        path.setPath(turningLine);
        path.playFrom(Duration.millis(2*GAME_SPEED - startTime));
        currentLine = turningLine;
 
    }
    /**
     * Makes the player move down. For the future this should probable move to
     * the player-class. Or possibly just get the playr and its direction.
     */
    public void moveDown() {
        makePath();
        double startTime = (currentLine.getEndY()/GRID_HEIGTH)*GAME_SPEED;
        turningLine = closestVerticalLine();
        currentPath = path;
        path.setPath(turningLine);
        path.playFrom(Duration.millis(startTime));
        currentLine = turningLine;
        
        
    }
    
     /**
     * Makes the player move upp. For the future this should probable move to
     * the player-class. Or possibly just get the playr and its direction.
     */
    public void moveUp() {
        makePath();
        double startTime = (currentLine.getEndY()/GRID_HEIGTH)*GAME_SPEED;
        turningLine = closestVerticalLine();
        currentPath = path;
        path.setPath(turningLine);
        path.playFrom(Duration.millis(2*GAME_SPEED - startTime));
        currentLine = turningLine;
        
        
    }
    
    
}
