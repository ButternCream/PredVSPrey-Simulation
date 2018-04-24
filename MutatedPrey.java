/*
 * Class: Mutated Prey
 * Description: This class will represent prey with mutations on
 * the grid
 */
import java.awt.Color;
public final class MutatedPrey extends Prey {

    private int level = 0;

    /*
     * Class Variables
     */
    public MutatedPrey()
    {
        super();
        type = "Mutated Prey";
        age = 1;
        birthRate = 5;
        color = Color.BLUE;
    }
    
    public MutatedPrey(Prey p)
    {
        this();
        age = p.age;
    }

    public void levelUp()
    {
        if (level >= 3)
            return;
        level++;
        birthRate = 5 - level;
        color = Color.CYAN;
    }
}

