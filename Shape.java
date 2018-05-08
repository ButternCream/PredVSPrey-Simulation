// File: Shape.java
// Author: Dr. Watts
// Contents: This file contains the description and implementation
// of a virtual class called Shape. 

import java.text.NumberFormat;
import java.awt.*;

public class Shape implements Comparable<Shape>
{
	public enum ShapeType {CIRCLE, EQUILATERAL, RECTANGLE, RIGHT, SCALENE, SQUARE};
	protected int centerX;
	protected int centerY;
	protected double side;
	protected Color color;
    protected boolean isSelected = false;

	public Shape ()
	{
		side = 0;
		centerX = 0;
		centerY = 0;
		color = Color.WHITE;
	}

	public double area ()
	{
		return 0;
	}

	public double perimeter ()
	{
		return 0;
	}

	public void fromString (String str)
	{
		String [] parts = str.split (" ");
		try
		{
			centerX = Integer.parseInt(parts[0]);
			centerY = Integer.parseInt(parts[1]);
			side = Integer.parseInt(parts[2]);
			color = new Color(Integer.parseInt(parts[3]));
		}
		catch (NumberFormatException e)
		{
			//System.out.println ("Numeric input error");
		}
	}

	public String toString ()
	{
		String string = new String ();
		string += centerX + " ";
		string += centerY + " ";
		string += side + " ";
		string += color.getRGB() + " ";
		return string;
	}

	public int compareTo (Shape S)
	{
		double a1 = area (), a2 = S.area ();
		double p1 = perimeter (), p2 = S.perimeter ();
		if (a1 < a2) return -1;
		if (a1 > a2) return 1;
		if (p1 < p2) return -2;
		if (p1 > p2) return 2;
		return 0;
	}

	public String getName ()
	{
		return "Shape";
	}

	public void paintComponent (Graphics2D g2)
	{
	}

	public boolean isIn (int X, int Y)
	{
		return false;
	}

	public void move (int deltaX, int deltaY)
	{
		centerX += deltaX;
		centerY += deltaY;
		//System.out.println ("Moving shape " + deltaX + "," + deltaY + " units");
	}

    public void rotate(double angle)
    {

    }

    public void resize(double scale)
    {

    }

    public void setSelected(boolean bSelected)
    {
        isSelected = bSelected;
    }
}
