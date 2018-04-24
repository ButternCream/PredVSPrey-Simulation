/*
 * Class: Predator
 * Description: This class will represent the prey character on the grid
 */
import java.awt.Color;
public class Predator extends Character {
    /*
     * Class Variables
     */
    
    protected int daysSinceLastEaten;
    protected long preyEaten;

    public Predator()
    {
        type = "Predator";
        color = Color.RED; // TODO: Add image
        age = 1;
        birthRate = 7;
        daysSinceLastEaten = 0;
        preyEaten = 0;
    }

    public void setEaten(boolean bAte)
    {
        if (bAte)
        {
            preyEaten++;
            daysSinceLastEaten = 0;
        }
        else
            daysSinceLastEaten++;
    }

    public long Eaten()
    {
        return preyEaten;
    }

    public boolean isStarving()
    {
        return (daysSinceLastEaten > 5);
    }
    
    public boolean isHungry()
    {
        return (daysSinceLastEaten > 2);
    }

    @Override
    public String toString()
    {
        String s = new String();
        s += "Predator\n";
        s += "Age: " + age + ",";
        s += "Birth Rate: " + birthRate + ",";
        s += "Since Eaten: " + daysSinceLastEaten;
        return s;
    }
}
