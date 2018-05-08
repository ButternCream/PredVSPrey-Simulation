// File: Square.java
// Author: Scott Mitchell
// Description: An implementation of the Square class to create Square objects.
// Extends the Quadrilateral class.
// Functions:
// Accessors / Mutators
// area - gets the area
// perimeter - gets the perimeter
// getName - Get the object type / name
import static java.lang.Math.*;
import java.awt.*;

public final class Square extends Quadrilateral
{
    public Square()
    {
        side = 0;
    }

    public Square(Square S)
    {
        side = S.side;
        centerX = S.centerX;
        centerY = S.centerY;
        color = S.color;
    }

    public Square(int S, int X, int Y, Color C)
    {
        side = S;
        centerX = X;
        centerY = Y;
        color = C;
        setVertices();
    }

    public double GetSide()
    {
        return side;
    }

    public void SetSide(int S)
    {
        side = S;
        setVertices();
    }

    public double area()
    {
        return side * side;
    }

    public double perimeter()
    {
        return side*4;
    }

    public String getName()
    {
        return "Square";
    }

    public void paintComponent(Graphics2D g2)
    {
        super.paintComponent(g2);
    }

    public void fromString (String str)
	{
		String [] parts = str.split (" ");
		try
		{
			centerX = Integer.parseInt(parts[0]);
			centerY = Integer.parseInt(parts[1]);
            if (parts.length <= 4)
            {
			    side = Double.parseDouble(parts[2]);
                color = new Color(Integer.parseInt(parts[3]));
                setVertices();
            }
            else
            {
                color = new Color(Integer.parseInt(parts[2]));
                int size = Integer.parseInt(parts[3]);
                for (int i = 0; i < 4; i++)
                {
                    doubleVertexX[i] = Double.parseDouble(parts[i*2+4]);
                    doubleVertexY[i] = Double.parseDouble(parts[i*2+5]);
                    vertexX[i] = (int)doubleVertexX[i];
                    vertexY[i] = (int)doubleVertexY[i];
                    pivotX[i] = vertexX[i];
                    pivotY[i] = vertexY[i];
                }
                polygon = new Polygon(vertexX, vertexY, size);
            }
        }
		catch (NumberFormatException e)
		{
			System.out.println ("File input error - invalid integer - Square");
		}
        
	}
    
    public String toString ()
	{
		String string = new String ();
		string += centerX + " ";
		string += centerY + " ";
		string += color.getRGB() + " ";
		string += "4 ";
        for (int i = 0; i < 4; i++)
        {
            if (angle == 0)
            {
                string += vertexX[i] + " " + vertexY[i] + " ";
            }
            else
            {
                string += pivotX[i] + " " + pivotY[i] + " ";
            }
        }
		return string;
	}
    
    public void setVertices()
    {
        doubleVertexX[0] = vertexX[0] = (int)(centerX-(side/2));
        doubleVertexX[1] = vertexX[1] = (int)(centerX+(side/2));
        doubleVertexX[2] = vertexX[2] = (int)(centerX+(side/2));
        doubleVertexX[3] = vertexX[3] = (int)(centerX-(side/2));

        doubleVertexY[0] = vertexY[0] = (int)(centerY-(side/2));
        doubleVertexY[1] = vertexY[1] = (int)(centerY-(side/2));
        doubleVertexY[2] = vertexY[2] = (int)(centerY+(side/2));
        doubleVertexY[3] = vertexY[3] = (int)(centerY+(side/2));
        polygon = new Polygon(vertexX, vertexY, 4);
        super.rotate(0);
    }

    
}
