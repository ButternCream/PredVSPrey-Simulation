import java.awt.Color;
public final class MutatedPredator extends Predator {
    
    //ArrayList<Mutation> mutations;
    private int level = 0;

    public MutatedPredator()
    {
        super();
        type = "Mutated Predator";
        color = Color.ORANGE;
        birthRate = 6;
    }

    public MutatedPredator(Predator p)
    {
        this();
        age = p.age;
        daysSinceLastEaten = p.daysSinceLastEaten;
        preyEaten = p.preyEaten;
    }

    public boolean isStarving()
    {
        return (daysSinceLastEaten > (7+level)); 
    }

    public boolean isHungry()
    {
        return (daysSinceLastEaten >= 0);
    }

    public void levelUp()
    {
        if (level >= 3)
            return;
        level++;
        birthRate = 4;
        color = Color.BLACK;
    }
    
    @Override
    public String toString()
    {
        String s = new String();
        s += "Mutated Predator\n";
        s += "Age: " + age + ",";
        s += "Birth Rate: " + birthRate + ",";
        s += "Since Eaten: " + daysSinceLastEaten;
        return s;
    }
}
