import java.awt.*;
import java.util.Objects;

class Location {
    public int ROW, COLUMN;
    public Location(int row, int column)
    {
        this.ROW = row;
        this.COLUMN = column;
    }

    public Location(Location l)
    {
        ROW = l.ROW;
        COLUMN = l.COLUMN;
    }
    
    @Override
    public String toString()
    {
        String s = new String();
        s += "(" + ROW + "," + COLUMN + ")";
        return s; 
    }

    @Override
    public boolean equals(Object v)
    {
        boolean retval = false;

        if (v instanceof Location)
        {
            Location l = (Location) v;
            retval = (l.ROW == this.ROW) && (l.COLUMN == this.COLUMN);
        }

        return retval;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(ROW,COLUMN);
    }       
}

public class GameSquare {

    public Location loc;
    public Character entity;
    private boolean empty;

    public GameSquare()
    {
        entity = new Character();
        empty = true;
        loc = new Location(0,0);
    }

    public GameSquare(GameSquare gs)
    {
        loc = new Location(gs.loc);
        entity = gs.entity;
        empty = gs.empty;
    }

    public GameSquare(Character occupyingChar, Location loc)
    {
        this.loc = new Location(loc);
        entity = occupyingChar;
        empty = false;
    }

    public boolean isEmpty()
    {
        return empty;
    }

    public long Eaten()
    {
        if (entity instanceof Predator)
            return ((Predator)entity).Eaten();
        else if (entity instanceof MutatedPredator)
            return ((MutatedPredator)entity).Eaten();
        else
            return 0;
    }

    public void setCharacter(Character entity)
    {
        if (entity instanceof Predator)
            this.entity = (Predator) entity;
        else if (entity instanceof Prey)
            this.entity = (Prey) entity;
        else
            this.entity = entity;
    }

    public void setOccupied(boolean isOccupied)
    {
        empty = !isOccupied;
    }

    public Color getColor()
    {
        if (entity == null)
            return Color.WHITE;
        return entity.getColor();
    }

    public void setLocation(int row, int col)
    {
        loc = new Location(row,col);
    }

    public void setLocation(Location l)
    {
        loc = new Location(l);
    }

    public Location getLocation()
    {
        return loc;
    }

    public String getType()
    {
        return entity.getType();
    }

}


