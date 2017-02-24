
public class GameLife {
    
    private final int dx[] = {1, 1, 1, 0, -1, -1, -1, 0};
    private final int dy[] = {-1, 0, 1, 1, 1, 0, -1, -1};
    
    private char alive = '*';
    private char dead = '.';
    
    private char[][] space;
    
    boolean valid(int x, int y) {
        return (x >= 0 && x < space.length && y >= 0 && y < space[x].length);
    }
    
    boolean isAlive(int x, int y) {
        assert valid(x, y) : "Error with bounds.";
        return (space[x][y] == alive);
    }
    
    void updateCell(int x, int y, char state) {
        assert valid(x, y) : "Error with bounds.";
        space[x][y] = state;
    }
    
    void updateSpace(char[][] newSpace) {
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
            if (valid(x + dx[i], y + dy[i])) {
                cnt += isAlive(x + dx[i], y + dy[i]) ? 1 : 0;
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
                }
                if (isAlive(i, j) && cntNeighbours != 2 && cntNeighbours != 3) {
                    newSpace[i][j] = dead;
                }
            }
        }
        return newSpace;
    }
}