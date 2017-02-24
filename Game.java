import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.RadioButton;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.scene.control.Button;

class GameLife {
    
    private final int dx[] = {1, 1, 1, 0, -1, -1, -1, 0};
    private final int dy[] = {-1, 0, 1, 1, 1, 0, -1, -1};
    
    private char alive = '*';
    private char dead = '.';
    
    private char[][] space;
    
    public GameLife(int n, int m) {
        space = new char[m][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; ++j)
                updateCell(i, j, dead);
    }
    
    public GameLife(char[][] newSpace) {
        updateSpace(newSpace);
    }
    
    private boolean valid(int x, int y) {
        return (x >= 0 && x < space.length && y >= 0 && y < space[x].length);
    }
    
    public boolean isAlive(int x, int y) {
        assert valid(x, y) : "Error with bounds.";
        return (space[x][y] == alive);
    }
    
    public void updateCell(int x, int y, char state) {
        assert valid(x, y) : "Error with bounds.";
        space[x][y] = state;
    }
    
    public void updateSpace(char[][] newSpace) {
        space = new char[newSpace.length][];
        for (int i = 0; i < space.length; i++) {
            space[i] = new char[newSpace[i].length];
            for (int j = 0; j < space.length; j++) {
                updateCell(i, j, newSpace[i][j]);
            } 
        }
    }
    
    private int getNeigbours(int x, int y) {
        int cnt = 0;
        for (int i = 0; i < 8; ++i) {
            if (valid((x + dx[i] + space.length) % space.length, (y + dy[i] + space[y].length) % space[y].length)) {
                cnt += isAlive((x + dx[i] + space.length) % space.length, (y + dy[i] + space[y].length) % space[y].length) ? 1 : 0;
            }
        }
        return cnt;
    }
    
    public char[][] nextGeneration() {
        char[][] newSpace = new char[space.length][];
        for (int i = 0; i < space.length; i++) {
            newSpace[i] = new char[space[i].length];
            for (int j = 0; j < space[i].length; j++) {
                int cntNeighbours = getNeigbours(i, j);
                if (cntNeighbours == 3 && !isAlive(i, j)) {
                    newSpace[i][j] = alive;
                } else if (isAlive(i, j) && cntNeighbours != 2 && cntNeighbours != 3) {
                    newSpace[i][j] = dead;
                } else 
                    newSpace[i][j] = space[i][j];
            }
        }
        return newSpace;
    }
    
    public void println() {
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[i].length; j++) {
                System.out.print(space[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}

public class Game extends Application {
    
    private static GameLife game;
    private GridPane gridPane = new GridPane();
    private RadioButton[][] radioButtons;
    // private Timeline timeline = new Timeline();
    
    private static int n;
    private static int m;
    
    private final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            game.updateSpace(game.nextGeneration());
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    radioButtons[i][j].setSelected(game.isAlive(i, j));
                }
            }
        }
    }));
    
    private Button buttonStop = new Button("Stop");
    private Button buttonStart = new Button("Start");
    private Button buttonClear = new Button("Clear");
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(m * 25);
        System.out.println(n * 25);
        Scene scene = new Scene(gridPane, m * 22 + 129, n * 18);
        
        radioButtons = new RadioButton[n][m];
        
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                radioButtons[i][j] = new RadioButton();
                gridPane.add(radioButtons[i][j], j, i, 1, 1);
            }
        }
        
        buttonStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                timeline.stop();
            }
        });
        
        buttonClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                for (int i = 0; i < n; i++) { 
                    for (int j = 0; j < m; j++) {
                        game.updateCell(i, j, '.');
                    }
                }
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        radioButtons[i][j].setSelected(game.isAlive(i, j));
                    }
                }
                timeline.stop();
            }
        });
        
        buttonStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        game.updateCell(i, j, (radioButtons[i][j].isSelected() ? '*' : '.'));
                        // gridPane.add(radioButtons[i][j], i, j, 1, 1);
                    }
                }
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
        });
        
        gridPane.add(buttonStart, m, 0, 1, m);
        gridPane.add(buttonStop, m + 1, 0, 1, m);
        gridPane.add(buttonClear, m + 2, 0, 1, m);
        
        primaryStage.setTitle("Game Life");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args){
        System.out.println(args.length);
        assert (args.length != 2) : "Not enough parameters";
        Scanner in = new Scanner(System.in);
        n = Integer.parseInt(args[0]);
        m = Integer.parseInt(args[1]);
        game = new GameLife(n, m);
        launch(args);
    }
}