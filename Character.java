/*
 * Class: Character
 * Description: The class that is the parent of the entities "Predator" and "Prey" 
 * represented on the grid.
 */
import javax.swing.*;
import java.awt.*;

public class Character {

    /*
     * Class variables
     */
    protected int age;
    protected String type;
    protected Color color;
    protected int birthRate; // Every X days it can reproduce
    protected boolean moved = false;
    private int row, col;

    public Character()
    {
        age = 1;
        type = "Character";
        color = Color.WHITE;
        birthRate = 0;
    }

    public Character(Character c)
    {
        age = c.age;
        type = c.type;
        color = c.color;
        birthRate = c.birthRate;
    }

    public void setMoved(boolean bMoved)
    {
        moved = bMoved;
    }

    public boolean hasMoved()
    {
        return moved;
    }

    protected String getType()
    {
        return type;
    }

    protected void grow()
    {
        age++;
    }
    
    // TODO: getImage() ?
    public Color getColor()
    {
        return color;
    }       

    public boolean canReproduce()
    {
        if (age > 18 || age < 100)
            return ((age % birthRate == 0) ? true : false);
        return false;
    }

    public void setCoordinates(int r, int c)
    {   
        this.row = r;
        this.col = c;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public int getAge()
    {
        return age;
    }
    
    @Override
    public String toString()
    {
        String s = new String();
        s += "Character -> Type: " + type + "\n";
        s += "Age: " + age + ",";
        s += "Birth Rate: " + birthRate;
        return s;
    }
}
