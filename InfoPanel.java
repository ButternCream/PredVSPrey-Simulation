/*
 *  Class: InfoPanel
 *  Description: The info panel class will create a panel below the main window which
 *  will contain information about the a grid square when it is selected.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InfoPanel extends JPanel
{
    /*
     * Class Variables
     */
    private JLabel header = new JLabel("<html><h2><u>Selected</u></h2></html>");
    private JLabel coord;
    private JLabel type;
    private JLabel age;
    private JLabel totalEaten;
    private JButton run;
    private JButton stop;
    private JLabel status;
    private JLabel iter;
    private JLabel goalLbl;
    private boolean running = false;
    private static int delay;

    /*
     * Construct a panel based on a parent window size modification
     */
    public InfoPanel(int parentWindowSizeMod)
    {
        this.setPreferredSize(new Dimension(200,parentWindowSizeMod));
        //this.setUndecorated(true);        

        age = new JLabel("Age");
        coord = new JLabel("Location");
        type = new JLabel("Type");
        totalEaten = new JLabel("Eaten");
        status = new JLabel();
        iter = new JLabel("Cycle");
        goalLbl = new JLabel("Goal");
        run = new JButton("Play");
        run.setPreferredSize(new Dimension(100,35));
        stop = new JButton("Pause");
        stop.setPreferredSize(new Dimension(100,35));
        run.setEnabled(true);
        stop.setEnabled(false);
        run.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (!run.isEnabled())
                    return;
                running = true;
                run.setEnabled(false);
                stop.setEnabled(true);
                new Thread()
                {
                    public void run(){
                        while(running) 
                        {
                            Main.getGridInstance().eventNextCycle();
                            try{
                                Thread.sleep(delay);
                            } catch(Exception e){}
                        }
                    }
                }.start();
            }

        });

        stop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!stop.isEnabled())
                    return;
                running = false;
                run.setEnabled(true);
                stop.setEnabled(false);
            }
        });

        // Use a grid bag layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        header.setHorizontalAlignment(JLabel.CENTER);
        age.setHorizontalAlignment(JLabel.CENTER);
        totalEaten.setHorizontalAlignment(JLabel.CENTER);
        coord.setHorizontalAlignment(JLabel.CENTER);
        type.setHorizontalAlignment(JLabel.CENTER);
        run.setHorizontalAlignment(JLabel.CENTER);
        stop.setHorizontalAlignment(JLabel.CENTER);
        iter.setHorizontalAlignment(JLabel.CENTER);
        goalLbl.setHorizontalAlignment(JLabel.CENTER);
        status.setHorizontalAlignment(JLabel.CENTER);

        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 0.5;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;

        add(header, c);

        c.gridy = 1;

        add(age, c);
        c.gridy = 2;

        add(totalEaten, c);
        c.gridy = 3;

        add(coord, c);
        c.gridy = 4;

        add(type, c);
        c.gridy = 5;
        
        add(run, c);
        c.gridy = 6;

        add(stop, c);
        c.gridy = 7;

        add(iter, c);
        c.gridy = 8;

        add(goalLbl, c);
        c.gridy = 9;

        add(status, c);
        c.gridy = 10;

    }

    public static void setDelay(int gridSize)
    {
        if (gridSize < 45)
            delay = 1000;
        else if (gridSize < 100)
            delay = 500;
        else if (gridSize < 150)
            delay = 100;
        else
            delay = 10;
    }
    public void Disable()
    {
        run.setEnabled(false);
        stop.setEnabled(false);
        running = false;
    }
    
    public void Enable()
    {
        run.setEnabled(true);
    }

    /*
     * Label setting functions
     */
    private void setCoord(String c)
    {
        coord.setText("<html>Location: " + c + "</html>");
    }

    private void setType(String t)
    {
        type.setText("<html>Type: " + t + "</html>");
    }

    private void setAge(String t)
    {
        age.setText("<html>Age: " + t + "</html>");
    }
    
    private void setEaten(String e)
    {
        totalEaten.setText(e);
    }

    // 
    public void setStatus(String s)
    {
        status.setText(s);
    }

    public void setCycle(long num)
    {
        iter.setText("Cycle: " + num);
    }

    public void displayGoal(int goal)
    {
        goalLbl.setText("Goal: " + goal);
    }

    public void displayInfo(GameSquare square)
    {
        Location loc = square.getLocation();
        setCoord(loc.toString());
        setType(square.getType());
        setAge(Integer.toString(square.entity.getAge()));
        if (square.entity.getType().equals("Predator") || square.entity.getType().equals("Mutated Predator"))
            setEaten("Eaten: " + Long.toString(square.Eaten()));
        else
            setEaten("");
    }

}
