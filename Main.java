/*
 * Class: Main (for now)
 * Description: This is the main window class which contains the grid jpanel.
 * It will also contain a menu to load / save a configuration as well as starting a new
 * game. It also overrides the component listener to keep the panel panel attached when the window
 * is moved around.
 * TODO:
 * - Finish the menu
 * - Create new class to set the grid size and number tiles by File -> Create.
 *      - In addition to the above, File -> Save / Load implementations.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import sun.audio.*;
import java.text.*;

public class Main extends JFrame
{
    /*
     *  Class Variables
     */
    private InfoPanel panel;
    private JButton ok;
    private int screenSize = 1000;
    private static Grid g;

    // Menu related variables
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu;
    private JMenuItem loadMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem newMenuItem;

    private JMenu options;
    private JCheckBoxMenuItem musicItem;
    private JCheckBoxMenuItem circItems;
    private JCheckBoxMenuItem gridLinesItem;
    private JMenu paintMode;
    private JCheckBoxMenuItem paintPrey;
    private JCheckBoxMenuItem paintPredator;

    private Integer[] goals = new Integer[]{5000, 10000, 25000, 50000, 100000, 250000};
    private JCheckBoxMenuItem simulationMode;
    // New game menu
    private JFrame newGame;

    // Mutation options
    private JMenuItem mutationMenu;
    private JFrame mutateFrame;
    
  
    public Main()
    {
        super("Predator vs Prey");
        // Add elements
        panel = new InfoPanel(screenSize);
        g = new Grid(screenSize, panel);
        add(g, BorderLayout.CENTER);
        add(panel, BorderLayout.EAST);


        // Set properties
        setSize(screenSize+200,screenSize);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create Menus
        createMenu();
        createOptionsMenu();
        createNewGameMenu();
        createMutateMenu();

        this.setJMenuBar(menuBar);
        // Set visibility
        setVisible(true);
    }

    public static Grid getGridInstance()
    {
        return g;
    }
    
    // Create the menu
    private void createMenu()
    {
        fileMenu = new JMenu("File");
        loadMenuItem = new JMenuItem("Load");
        saveMenuItem = new JMenuItem("Save");
        newMenuItem = new JMenuItem("New");

        // Action Listeners
        newMenuItem.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e)
            {
                eventNewGame(e);
            }
        });

        loadMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                eventLoad();
            }            
        });

        saveMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                eventSave();
            }
        });

        

        fileMenu.add(newMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
    }

    public void createOptionsMenu()
    {
        options = new JMenu("Options");
        musicItem = new JCheckBoxMenuItem("Music");
        circItems = new JCheckBoxMenuItem("Use Circles");
        gridLinesItem = new JCheckBoxMenuItem("Show Grid Lines");
    	paintMode = new JMenu("Paint");
    	paintPrey = new JCheckBoxMenuItem("Prey");
    	paintPredator = new JCheckBoxMenuItem("Predator");
        mutationMenu = new JMenuItem("Conversion Rate");

	paintPrey.setState(false);
	paintPredator.setState(false);


        musicItem.setState(false);
        circItems.setState(false);
        gridLinesItem.setState(true);

        mutationMenu.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                openMutateMenu();
            }
        });

	paintPrey.addActionListener(new ActionListener(){
	    @Override
            public void actionPerformed(ActionEvent evt)
	    {
		paintPredator.setState(false);
		if (paintPrey.getState())
		    g.setPaintMode(1);
		else
		    g.setPaintMode(0);
		panel.setPaintStatus(g.getPaintMode());
	    }
	});

	paintPredator.addActionListener(new ActionListener(){
	    @Override
            public void actionPerformed(ActionEvent evt)
	    {
		paintPrey.setState(false);
		if (paintPredator.getState())
		    g.setPaintMode(2);
		else
		    g.setPaintMode(0);
		panel.setPaintStatus(g.getPaintMode());
	    }
	});


        musicItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                if (musicItem.getState())
                    SoundThread.loop("POL-astro-force-short.wav");
                else
                    SoundThread.end();
            }
        });

        circItems.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
               g.useCircles(circItems.getState());
               g.repaint();
            }
        });

        gridLinesItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
               g.useGridLines(gridLinesItem.getState());
               g.repaint();
            }
        });

	paintMode.add(paintPrey);
	paintMode.add(paintPredator);
        options.add(musicItem);
        options.add(circItems);
        options.add(gridLinesItem);
        options.add(mutationMenu);
        menuBar.add(options);
	menuBar.add(paintMode);
    }

    /*
     * Event handler for when File -> New is clicked
     */
    private void eventNewGame(MouseEvent e)
    {
        newGame.setLocationRelativeTo(null);
        newGame.setVisible(true);
        panel.setCycle(0);
    }

    /*
     * Event handler for Options -> Mutation Rate
     */
    private void openMutateMenu()
    {
        createMutateMenu();
        mutateFrame.setLocationRelativeTo(null);
        mutateFrame.setVisible(true);
    }

    /*
     * Event handler for when File -> Load is clicked.
     */
    private void eventLoad()
    {
        JFileChooser c = new JFileChooser();
        int rVal = c.showOpenDialog(Main.this);
        if (rVal == JFileChooser.APPROVE_OPTION)
        {
            // For now print file name
            try{
                Load(c.getSelectedFile().getName());
                System.out.println("Loaded configuration");
            } catch(IOException exc)
            {
                System.out.println(exc);
            }
        }
        else if (rVal == JFileChooser.CANCEL_OPTION)
        {
            System.out.println("Canceled");
        }
    }

    /*
     * Event handler for when File -> Save is clicked.
     */
    private void eventSave()
    {
        JFileChooser c = new JFileChooser();
        int rVal = c.showSaveDialog(Main.this);
        if (rVal == JFileChooser.APPROVE_OPTION)
        {
            // For now print file name
            System.out.println(c.getSelectedFile().getName());
            try{
                Save(c.getSelectedFile().getName());
                System.out.println("Saved current configuration");
            } catch(IOException exc)
            {
                System.out.println(exc);
            }
        }
        else if (rVal == JFileChooser.CANCEL_OPTION)
        {
            System.out.println("Canceled");
        }
    }

    public void Save(String fileName) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(g.gridSize() + "\n");
        writer.write(g.numCycles() + "\n");
        writer.write(g.getGoal() + "\n");
        for (GameSquare[] sqlist : g.gameGrid)
        {
            for (GameSquare sq : sqlist)
            {
                if (sq.isEmpty())
                    writer.write("0 ");
                else if (sq.getType().equals("Prey"))
                    writer.write("1 ");
                else if (sq.getType().equals("Predator"))
                    writer.write("2 ");
                else if (sq.getType().equals("Mutated Predator"))
                    writer.write("3 ");
                else if (sq.getType().equals("Mutated Prey"))
                    writer.write("4 ");
            }
            writer.write("\n");
        }
        writer.close();
    }

    public void Load(String filename) throws IOException
    {
        Scanner scanner = new Scanner(new File(filename));
        int size = scanner.nextInt();
        g.resize(size);
        long cycle = scanner.nextInt();
        g.setLoadedCycles(cycle);
        panel.setCycle(cycle);
        int goal = scanner.nextInt();
        g.setGoal(goal);
        int count = 0;
        while (scanner.hasNextInt())
        {
            int row = (int)(count / (double)size);
            int col = count % size;
            int type = scanner.nextInt();
            switch (type)
            {
                case 0:
                    g.gameGrid[row][col].setOccupied(false);
                    break;
                case 1:
                    g.gameGrid[row][col].setLocation(new Location(row,col));
                    g.gameGrid[row][col].setCharacter(new Prey());
                    g.gameGrid[row][col].setOccupied(true);
                    break;
                case 2:
                    g.gameGrid[row][col].setLocation(new Location(row,col));
                    g.gameGrid[row][col].setCharacter(new Predator());
                    g.gameGrid[row][col].setOccupied(true);
                    break;
                case 3:
                    g.gameGrid[row][col].setLocation(new Location(row,col));
                    g.gameGrid[row][col].setCharacter(new MutatedPredator());
                    g.gameGrid[row][col].setOccupied(true);
                    break;
                case 4:
                    g.gameGrid[row][col].setLocation(new Location(row,col));
                    g.gameGrid[row][col].setCharacter(new MutatedPrey());
                    g.gameGrid[row][col].setOccupied(true);
                    
            }
            count++;
        }
        scanner.close();
        g.repaint();

           
    }

    private void createMutateMenu()
    {
        mutateFrame = new JFrame("Conversion Rate");
        mutateFrame.setSize(new Dimension(225,80));
        JLabel rateLabel = new JLabel("Rate (%):");
        JButton set = new JButton("Set");
        JButton cancel = new JButton("Cancel");

        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(5);

        JFormattedTextField r = new JFormattedTextField(format);
        r.setValue(new Double(g.getConversionRate())*100);
        r.setToolTipText("<html>This is the conversion rate for prey converting to predators when they die.<br>The default is 0.1%, so when prey die there is a 0.1% chance of them turning into a predator</html>");
        r.setColumns(5);

        set.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                double rate = ((Number)r.getValue()).doubleValue();
                g.setConversionRate(rate/100);
                mutateFrame.setVisible(false);
                System.out.println("Set conversion rate to " + rate);
            }
        });

        cancel.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                mutateFrame.setVisible(false);
            }
        });

        mutateFrame.setResizable(false);
        mutateFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Add the elements
        c.ipadx = 5;
        c.ipady = 5;

        c.gridx = 0;
        c.gridy = 0;
        mutateFrame.add(rateLabel, c);

        c.weightx = 0.5;        
        c.gridx = 1;
        c.gridy = 0;
        mutateFrame.add(r, c);
        
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 5;
        mutateFrame.add(set, c);

        c.weightx = 0.5;        
        c.gridx = 1;
        c.gridy = 5;
        mutateFrame.add(cancel, c);
    }

    private void createNewGameMenu()
    {
         //Create elements for popup menu
        newGame = new JFrame("New Game");
        newGame.setSize(new Dimension(300,250));
        JButton create = new JButton("Create");
        JButton cancel = new JButton("Cancel");
        JFormattedTextField size = new JFormattedTextField();
        size.setValue(new Integer(25));
        size.setColumns(5);
        JFormattedTextField prey = new JFormattedTextField();
        prey.setValue(new Integer(50));
        prey.setColumns(5);
        JFormattedTextField predator = new JFormattedTextField();
        predator.setValue(new Integer(50));
        predator.setColumns(5);
        JLabel sizeLbl = new JLabel("Grid Size: ");
        JLabel predLbl = new JLabel("# Of Predators: ");
        JLabel preyLbl = new JLabel("# Of Prey: ");
        JLabel goalLbl = new JLabel("Win Goal: ");
        JComboBox<Integer> diff = new JComboBox<>(goals);
        diff.setSelectedIndex(0);
        JCheckBoxMenuItem simMode = new JCheckBoxMenuItem("Simulation Mode");

        create.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                g.resize(((Number)size.getValue()).intValue());
                int numPred = ((Number)predator.getValue()).intValue();
                int numPrey = ((Number)prey.getValue()).intValue();
                g.generatePositions(numPred,numPrey);
                if (simMode.getState())
                    g.setGoal(0);
                else
                    g.setGoal((Integer)diff.getSelectedItem());
                g.shuffle();
                newGame.setVisible(false);
                panel.Enable();
                panel.setCycle(0);
                panel.setStatus("");
                System.out.println("Created new game");
            }
        });
        cancel.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                newGame.setVisible(false);
            }
        });

	newGame.setResizable(false);

        // Set the layout
        newGame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Add the elements
        c.ipadx = 5;
        c.ipady = 5;

        c.gridx = 0;
        c.gridy = 0;
        newGame.add(sizeLbl, c);

        c.weightx = 0.5;        
        c.gridx = 1;
        c.gridy = 0;
        newGame.add(size, c);

        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        newGame.add(preyLbl, c);

        c.weightx = 0.5;        
        c.gridx = 1;
        c.gridy = 1;
        newGame.add(prey, c);

        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        newGame.add(predLbl, c);

        c.weightx = 0.5;        
        c.gridx = 1;
        c.gridy = 2;
        newGame.add(predator, c);

        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        newGame.add(goalLbl, c);

        c.weightx = 0.5;        
        c.gridx = 1;
        c.gridy = 3;
        newGame.add(diff, c);
        
        c.weightx = 0.5;        
        c.gridx = 0;
        c.gridy = 4;
        newGame.add(simMode, c);

        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 5;
        newGame.add(create, c);

        c.weightx = 0.5;        
        c.gridx = 1;
        c.gridy = 5;
        newGame.add(cancel, c);
        
    }

    // Main
    public static void main(String args[])
    {
        new Main();
    }
    
}
