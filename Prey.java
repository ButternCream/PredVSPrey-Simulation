/*
 * Class: Prey
 * Description: This class will represent the prey character on the grid
 */
import java.awt.Color;
public class Prey extends Character {
    /*
     * Class Variables
     */
    public Prey()
    {
        type = "Prey";
        color = Color.GREEN; // TODO: Add image
        age = 1;
        birthRate = 6;
    }

    @Override
    public String toString()
    {
        String s = new String();
        s += "Prey\n";
        s += "Age: " + age + ",";
        s += "Birth Rate: " + birthRate + ",";
        return s;
    }

}
