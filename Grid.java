/*
 *  Class: Grid
 *  Description: This class is for drawing a grid to the window for the game.
 *  We're overriding the paint comp. in order to draw rectangles and black lines
 *  to visually represent the grid.
 *
 *  TODO:
 *  - Dynamic grid size, screen size (maybe), and be able to choose number of game tiles.
 *  - Replace the array of colors with predators / preys array of game characters.
 *      - After above, redo click event to select, move, add, etc. elements to grid.
 *  - Add A GridPoint class that contains
 *      - Row, Column, Character Object
 *
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Grid extends JPanel implements MouseListener, ActionListener {
    /*
     *  Class Variables
     */
    private int numTiles = 25;
    private double cellSize;
    private int screenSize;
    private int offset = 0;
    public GameSquare[][] gameGrid;
    private GameSquare selected = null;
    private int selectedRow, selectedCol;
    private int clickedRow, clickedCol;
    private InfoPanel panel;
    private long cycles = 0;
    private int goal = 1000;
     /*
     * The menu that pops up when you right click on the grid.
     */
    private JPopupMenu menu;
    private JMenu add = new JMenu("Add");
    private JMenuItem addPrey = new JMenuItem("Prey");
    private JMenuItem addPred = new JMenuItem("Predator");
    private JMenuItem remove = new JMenuItem("Remove");

    private boolean GRID_LINES = true;
    private boolean CIRCLES = false;


    public Grid(int screenSize, InfoPanel d)
    {
        addMouseListener(this);
        gameGrid = new GameSquare[numTiles][numTiles];
        for (int i = 0; i < numTiles; i++)
        {
            gameGrid[i] = new GameSquare[numTiles];
            for (int j = 0; j < numTiles; j++)
            {
                gameGrid[i][j] = new GameSquare();
            }
        }

        this.screenSize = screenSize;
        cellSize = screenSize / numTiles;
        panel = d;

        menu = new JPopupMenu();
        // Add entity to grid
        addPrey.addActionListener(this);
        addPred.addActionListener(this);
        // Remove entity from grid
        remove.addActionListener(this);
        InfoPanel.setDelay(numTiles);

    }

    public int gridSize() { return numTiles; }
    public long numCycles() { return cycles; }
    public void useCircles(boolean bCirc){ CIRCLES = bCirc; }
    public void useGridLines(boolean bLines){ GRID_LINES = bLines; }

    public void setGoal(int goal)
    {
        this.goal = goal;
        panel.displayGoal(goal);
    }

    public int getGoal()
    {
        return goal;
    }

    // Resizes the grid and repaints
    public void resize(int tiles)
    {
        numTiles = tiles;
        gameGrid = new GameSquare[numTiles][numTiles];
        for (int i = 0; i < numTiles; i++)
        {
            gameGrid[i] = new GameSquare[numTiles];
            for (int j = 0; j < numTiles; j++)
            {
                gameGrid[i][j] = new GameSquare();
            }
        }

        cellSize = screenSize / numTiles;
        InfoPanel.setDelay(numTiles);
        cycles = 0;
        repaint();
    }

    // Generate prey / predator positions for a new game
    public void generatePositions(int numPred, int numPrey)
    {
        if ((numPred + numPrey) > (numTiles*numTiles))
        {
            panel.setStatus("<html>You have supplied more characters than grid spaces<html>");
            return;
        }
        int placed = 0;
        while (placed < numPred)
        {
            int row = (int)(Math.random()*numTiles);
            int col = (int)(Math.random()*numTiles);

            if (gameGrid[row][col].isEmpty())
            {
                gameGrid[row][col].setLocation(new Location(row,col));
                gameGrid[row][col].setCharacter(new Predator());
                gameGrid[row][col].setOccupied(true);
                placed++;
            }
        }
        placed = 0;
        while (placed < numPrey)
        {
            int row = (int)(Math.random()*numTiles);
            int col = (int)(Math.random()*numTiles);

            if (gameGrid[row][col].isEmpty())
            {
                gameGrid[row][col].setLocation(new Location(row,col));
                gameGrid[row][col].setCharacter(new Prey());
                gameGrid[row][col].setOccupied(true);
                placed++;
            }
        }
        repaint();
    }

    // Use this to take care of the grids right click menu popup
    @Override
    public void actionPerformed(ActionEvent evt)
    {
        if (evt.getActionCommand().equals("Prey"))
        {
            gameGrid[clickedRow][clickedCol].setCharacter(new Prey());
            gameGrid[clickedRow][clickedCol].setLocation(new Location(clickedRow,clickedCol));
            gameGrid[clickedRow][clickedCol].setOccupied(true);

        }
        else if (evt.getActionCommand().equals("Predator"))
        {
            gameGrid[clickedRow][clickedCol].setCharacter(new Predator());
            gameGrid[clickedRow][clickedCol].setLocation(new Location(clickedRow,clickedCol));
            gameGrid[clickedRow][clickedCol].setOccupied(true);

        }
        else if (evt.getActionCommand().equals("Remove"))
        {
            gameGrid[clickedRow][clickedCol].setOccupied(false);
        }
        repaint();
    }

    // Goes to the next game cycle
    // Randomly move non-mutated
    // If Predators have an adjacent Prey(s) they randomly pick an index and eat them
    public void eventNextCycle()
    {
        // Predators have move priority
        randomPredatorMoves();
        // Prey then move
        randomPreyMoves();
        // Check predator starvation and kill those who havent eaten
        checkStarvation();
        // Check for reproduction
        reproduce();
        // Check ages to randomly mutate long survivors
        tryMutations();
        //Reset moves
        resetMoves();

        panel.setCycle(++cycles);

        repaint();
        //Check for end game
        if(checkEndGame() < 0)
        {
            panel.setStatus("<html>Game over, you lose!</html>");
            panel.Disable();
            cycles = 0;
            return;
        }
        else if (checkEndGame() > 0)
        {
            panel.setStatus("<html>Congratulations, you survived!</html>");
            panel.Disable();
            cycles = 0;
            return;
        }
    }

    public void tryMutations()
    {
        Random r = new Random();
        for (GameSquare[] sqlist : gameGrid)
        {
            for (GameSquare sq : sqlist)
            {
                if (!sq.isEmpty() && sq.getType().equals("Predator") && sq.entity.getAge() >= 10)
                {
                    if (r.nextDouble() < (sq.entity.getAge() / 100.0))
                    {
                        sq.setCharacter(new MutatedPredator((Predator)sq.entity));
                    }
                }
                else if (!sq.isEmpty() && sq.getType().equals("Prey") && sq.entity.getAge() >= 10)
                {
                    if (r.nextDouble() < (sq.entity.getAge() / 100.0))
                    {
                        sq.setCharacter(new MutatedPrey((Prey)sq.entity));
                    }
                }
                else if (!sq.isEmpty() && (sq.getType().equals("Mutated Predator") || sq.getType().equals("Mutated Prey")) && sq.entity.getAge() % 25 == 0)
                {
                    if (r.nextDouble() < 0.1)
                    {
                        if (sq.entity instanceof MutatedPredator)
                            ((MutatedPredator)sq.entity).levelUp();
                        if (sq.entity instanceof MutatedPrey)
                            ((MutatedPrey)sq.entity).levelUp();
                    }
                }
                else if (!sq.isEmpty() && (sq.getType().equals("Prey") || sq.getType().equals("Mutated Prey")) && sq.entity.getAge() >= 100)
                {
                    if (r.nextDouble() < 0.001)
                        sq.setCharacter(new Predator());
                    else
                        sq.setOccupied(false);
                }
            }
        }
    }

    // Check to see if the game is over
    public int checkEndGame()
    {
        if (cycles == goal)
        {
            return 1;
        }
        int cPrey = 0;
        int cPred = 0;
        for (GameSquare[] sqlist : gameGrid)
        {
            for (GameSquare sq : sqlist)
            {
                if (sq.getType().equals("Prey") || sq.getType().equals("Mutated Prey"))
                    cPrey++;
                else if (sq.getType().equals("Predator") || sq.getType().equals("Mutated Predator"))
                    cPred++;
                if (cPrey > 0 && cPred > 0)
                    return 0; // No winner yet
            }
        }
        return -1; // Predators won
    }

    // Reset the characters moves at end of cycle
    public void resetMoves()
    {
        for (GameSquare[] sqlist : gameGrid)
            for (GameSquare sq : sqlist)
                sq.entity.setMoved(false);
    }
    
    // Try and reproduce
    public void reproduce()
    {
        Random r = new Random();
        for (GameSquare[] sqlist : gameGrid)
        {
            for (GameSquare sq : sqlist)
            {
                if (!sq.isEmpty() && sq.entity.canReproduce())
                {
                    String type = sq.getType();
                    ArrayList<Location> vacant = getVacantNeighbors(sq);
                    if (vacant.size() > 0) //Space to reproduce
                    {
                        int index = (int)(Math.random()*vacant.size());
                        Location dest = vacant.get(index);
                        if (type.equals("Prey"))
                            gameGrid[dest.ROW][dest.COLUMN] = new GameSquare(new Prey(), new Location(dest.ROW, dest.COLUMN));
                        else if (type.equals("Predator"))
                            gameGrid[dest.ROW][dest.COLUMN] = new GameSquare(new Predator(), new Location(dest.ROW, dest.COLUMN));
                        else if (type.equals("Mutated Predator"))
                        {
                            double val = r.nextDouble();
                            if (val > 0.7)
                                gameGrid[dest.ROW][dest.COLUMN] = new GameSquare(new MutatedPredator(), new Location(dest.ROW, dest.COLUMN));
                            else
                                gameGrid[dest.ROW][dest.COLUMN] = new GameSquare(new Predator(), new Location(dest.ROW, dest.COLUMN));
                        }
                        else if (type.equals("Mutated Prey"))
                        {
                            double val = r.nextDouble();
                            if (val > 0.5)
                                gameGrid[dest.ROW][dest.COLUMN] = new GameSquare(new MutatedPrey(), new Location(dest.ROW, dest.COLUMN));
                            else
                                gameGrid[dest.ROW][dest.COLUMN] = new GameSquare(new Prey(), new Location(dest.ROW, dest.COLUMN));
                        }
                    }
                }
            }
        }
    }

    // Check if predator is starving. If so, kill them.
    public void checkStarvation()
    {
        for (GameSquare[] sqlist : gameGrid)
        {
            for (GameSquare sq : sqlist)
            {
                if (!sq.isEmpty() && sq.getType().equals("Predator"))
                {
                    // Kill them
                    if (((Predator)sq.entity).isStarving())
                    {
                        sq.setOccupied(false);
                    }
                }
                if (!sq.isEmpty() && sq.getType().equals("Mutated Predator"))
                {
                    // Kill them
                    if (((MutatedPredator)sq.entity).isStarving())
                    {
                        sq.setOccupied(false);
                    }
                }
            }
        }
    }

    // Predators move and first check for prey. If there is none just randomly move.
    public void randomPredatorMoves()
    {
        for (GameSquare[] sqlist : gameGrid)
        {
            for (GameSquare sq : sqlist)
            {
                if (!sq.isEmpty() && !sq.entity.hasMoved() && sq.getType().equals("Predator"))
                {
                    ArrayList<Location> prey = findPrey(sq);
                    if (((Predator)sq.entity).isHungry() && prey.size() > 0) // 
                    {
                        int index = (int)(Math.random()*prey.size());
                        Location dest = prey.get(index);
                        // sq "kills" prey at row and col
                        move(sq, dest.ROW, dest.COLUMN);
                        ((Predator)sq.entity).setEaten(true); // Predator has eaten so reset starvation
                    } 
                    else // No prey nearby
                    {
                        ((Predator)sq.entity).setEaten(false);
                        ArrayList<Location> vacant = getVacantNeighbors(sq);
                        if (vacant.size() > 0)
                        {
                            // Pick random open spot
                            int index = (int)(Math.random()*vacant.size());
                            Location dest = vacant.get(index);
                            move(sq, dest.ROW, dest.COLUMN);
                        }   
                    }
                    sq.entity.grow(); // age + 1
                    sq.entity.setMoved(true);
                }
                if (!sq.isEmpty() && !sq.entity.hasMoved() && sq.getType().equals("Mutated Predator"))
                {
                    ArrayList<Location> prey = findPrey(sq);
                    if (((MutatedPredator)sq.entity).isHungry() && prey.size() > 0) // 
                    {
                        int index = (int)(Math.random()*prey.size());
                        Location dest = prey.get(index);
                        // sq "kills" prey at row and col
                        move(sq, dest.ROW, dest.COLUMN);
                        ((MutatedPredator)sq.entity).setEaten(true); // MutatedPredator has eaten so reset starvation
                    } 
                    else // No prey nearby
                    {
                        ((MutatedPredator)sq.entity).setEaten(false);
                        ArrayList<Location> vacant = getVacantNeighbors(sq);
                        if (vacant.size() > 0)
                        {
                            // Pick random open spot
                            int index = (int)(Math.random()*vacant.size());
                            Location dest = vacant.get(index);
                            move(sq, dest.ROW, dest.COLUMN);
                        }   
                    }
                    sq.entity.grow(); // age + 1
                    sq.entity.setMoved(true);
                }

            }
        }
    }
    
    // unmutated prey randomly move
    public void randomPreyMoves()
    {
        for (GameSquare[] sqlist : gameGrid)
        {
            for (GameSquare sq : sqlist)
            {
                if (!sq.isEmpty() && !sq.entity.hasMoved() && (sq.getType().equals("Prey") || sq.getType().equals("Mutated Prey"))) // && not mutated
                {
                    ArrayList<Location> vacant = getVacantNeighbors(sq);
                    if (vacant.size() > 0)
                    {
                        // Pick random open spot
                        int index = (int)(Math.random()*vacant.size());
                        Location dest = vacant.get(index);
                        move(sq, dest.ROW, dest.COLUMN);
                    }
                    sq.entity.grow(); // age + 1
                    sq.entity.setMoved(true);
                }
            }
        }
    }

    /*
     *  Override the paint component to draw rectangles and
     *  lines to represent the grid.
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0,getWidth(),getHeight());
        int row, col;
        double cellWidth = (double)getWidth() / numTiles;
        double cellHeight = (double)getHeight() / numTiles;
        for (row = 0; row < numTiles; row++) {
            for (col = 0; col < numTiles; col++) {
                if (!gameGrid[row][col].isEmpty()) {
                    int x1 = (int)(col*cellWidth);
                    int y1 = (int)(row*cellHeight);
                    int x2 = (int)((col+1)*cellWidth);
                    int y2 = (int)((row+1)*cellHeight);
                    g.setColor(gameGrid[row][col].getColor());
                    if (CIRCLES)
                        g.fillOval( x1, y1, (int)cellWidth, (int)cellHeight );
                    else
                        g.fillRect( x1, y1, x2-x1, y2-y1 );
                }
            }
        }
        if (GRID_LINES){
           g.setColor(Color.BLACK);
            for (row = 1; row < numTiles; row++) {
                int y = (int)(row*cellHeight);
                g.drawLine(0,y,getWidth(),y);
            }
            for (col = 1; col < numTiles; col++) {
                int x = (int)(col*cellWidth);
                g.drawLine(x,0,x,getHeight());
            } 
        }
            
        
    }

    /*
     *  Implement the mouse event functions.
     *  Mouse pressed finds where you clicked and fills the selected rectangle in
     *  the grid with a random color for gameGriding. It also sets the labels
     *  in the panel class so we can see the details of the grid entities.. 
     */
    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){ selected = null; }
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) 
    {
        int row = (int)(((double)e.getY())/getHeight()*numTiles);
        int col = (int)(((double)e.getX())/getWidth()*numTiles);
        if (SwingUtilities.isRightMouseButton(e))
        {
            if (gameGrid[row][col].isEmpty())
            {
                menu.remove(remove);
                menu.add(add);
                add.add(addPrey);
                add.add(addPred);
            }
            else
            {
                menu.remove(add);
                add.remove(addPrey);
                add.remove(addPred);
                menu.add(remove);
                
            }
            clickedRow = row;
            clickedCol = col;
            menu.show(e.getComponent(),e.getX(),e.getY());
        }
        else if (SwingUtilities.isLeftMouseButton(e))
        {
            
            // Moving / Selection
            //if (selected == null && !gameGrid[row][col].isEmpty())
            //{
                //selected = gameGrid[row][col];
              //  System.out.println(selected.entity.toString());
            //}
            //else if (selected == gameGrid[row][col])
            //{
              //  System.out.println("Same object, unselecting.");
                //selected = null;
            //}
            /*else if (selected != null)
            {
                ArrayList<Location> vacants = getVacantNeighbors(selected);
                for (Location l : vacants)
                {
                    System.out.println(l);
                }
                if (!vacants.contains(new Location(row,col))) // Invalid Move
                {
                    System.out.println("Invalid move");
                    return;
                }

                move(selected, row, col);
                selected = null;

                repaint();
                String s = "Moved to (" + row + "," + col + ")";
                System.out.println(s);
            }*/

             // Set the info panel labels
            if (!gameGrid[row][col].isEmpty())
                panel.displayInfo(gameGrid[row][col]);

        }
    }

    // Move selected -> row & col
    public void move(GameSquare selected, int row, int col)
    {      
        GameSquare sq = new GameSquare(selected);
        gameGrid[row][col] = sq;
        gameGrid[row][col].setLocation(row,col);
        int oldRow = selected.getLocation().ROW;
        int oldCol = selected.getLocation().COLUMN;
        gameGrid[oldRow][oldCol].setOccupied(false);
        selected.setOccupied(false);
    }

    public ArrayList<Location> getVacantNeighbors(GameSquare sq)
    {
        int row = sq.getLocation().ROW;
        int col = sq.getLocation().COLUMN;
        ArrayList<Location> vacant = new ArrayList<Location>();
        for (int r = -1; r <= 1; r++)
        {
            for (int c = -1; c <= 1; c++)
            {
                if (r == 0 && c == 0)
                    continue;
                if (isValid(row+r, col+c) && gameGrid[row+r][col+c].isEmpty())
                {
                    vacant.add(new Location(row+r, col+c));
                }

            }
        }
        return vacant;
   }

    public ArrayList<Location> findPrey(GameSquare sq)
    {
        int row = sq.getLocation().ROW;
        int col = sq.getLocation().COLUMN;
        ArrayList<Location> prey = new ArrayList<Location>();
        for (int r = -1; r <= 1; r++)
        {
            for (int c = -1; c <= 1; c++)
            {
                if (r == 0 && c == 0)
                    continue;
                if (isValid(row+r, col+c) && (gameGrid[row+r][col+c].getType().equals("Prey") || gameGrid[row+r][col+c].getType().equals("Mutated Prey")))
                {
                    prey.add(new Location(row+r, col+c));
                }

            }
        }
        return prey;
   }

    public boolean isValid(int row, int col)
    {
        return row >= 0 && row < numTiles && col >= 0 && col < numTiles; 
    }

    public void shuffle()
    {
        Random rand = new Random();
        for (int i = 0; i < numTiles; i++)
        {
            for (int j = 0; j < numTiles; j++)
            {
                int m = rand.nextInt(i+1);
                int n = rand.nextInt(j+1);

                GameSquare temp = gameGrid[i][j];
                gameGrid[i][j] = gameGrid[m][n];
                gameGrid[m][n] = temp;
            }
        }
    }

    public void setLoadedCycles(long c) { cycles = c; }
 }       
