// File: Quadrilateral.java
// Author: Scott Mitchell
// Description: An implementation of the Quadrilateral class to create Quadrilateral objects
// and server as a base class for Square and Rectangle.
// Functions:
// sides - gets the number of sides
// getName - Get the object type / name
import java.awt.*;
public class Quadrilateral extends Shape
{
    protected double [] doubleVertexX = new double [4];
    protected double [] doubleVertexY = new double [4];
    protected int [] vertexX = new int [4];
    protected int [] vertexY = new int [4];
    protected int [] pivotX = new int[4];
    protected int [] pivotY = new int[4];
    protected Polygon polygon =  new Polygon (vertexX, vertexY, 4);
    protected double angle = 0.0;

    public Quadrilateral()
    {

    }

    public String sides()
    {
        return "4";
    } 

    public String getName()
    {
        return "Quadrilateral";
    }

    public void paintComponent(Graphics2D g2)
    {
        g2.setPaint (color);
        g2.fillPolygon (vertexX, vertexY, 4);
        g2.drawPolygon (vertexX, vertexY, 4);
    }

    public boolean isIn (int X, int Y)
    {
        return polygon.contains (X, Y);
    }

    public void move (int deltaX, int deltaY)
    {
        for (int i = 0; i < 4; i++)
        {
            doubleVertexX[i] += deltaX;
            doubleVertexY[i] += deltaY;
            vertexX[i] += deltaX;
            vertexY[i] += deltaY;
            pivotX[i] += deltaX;
            pivotY[i] += deltaY;

        }
        polygon = (angle == 0 ? new Polygon(vertexX,vertexY,4) : new Polygon(pivotX, pivotY, 4));
        centerX += deltaX;
        centerY += deltaY;
    }

    protected void setVertices ()
    {
    }

    public void rotate(double degree)
    {
        double sin_theta = Math.sin(Math.toRadians(degree));
        double cos_theta = Math.cos(Math.toRadians(degree));
        for (int i = 0; i < 4; i++)
        {
            double orig_x = doubleVertexX[i] - (double)centerX;
            double orig_y = doubleVertexY[i] - (double)centerY;
            doubleVertexX[i] = ((orig_x*cos_theta - orig_y*sin_theta) + centerX);
            doubleVertexY[i] = ((orig_x*sin_theta + orig_y*cos_theta) + centerY);
            vertexX[i] = (int)(doubleVertexX[i] + .5);
            vertexY[i] = (int)(doubleVertexY[i] + .5);
        }       
        polygon =  new Polygon(vertexX, vertexY, 4);
    }

    private double dist(int x1, int y1, int x2, int y2)
    {
        int x = (x2-x1)*(x2-x1);
        int y = (y2-y1)*(y2-y1);
        return Math.sqrt(x+y);
    }
    
    public void resize(double scale)
    {
        for (int i = 0; i < 4; i++)
        {
            double transX = (doubleVertexX[i] - centerX);
            double transY = (doubleVertexY[i] - centerY);
            doubleVertexX[i] = scale * transX;
            doubleVertexY[i] = scale * transY;
            doubleVertexX[i] += centerX;
            doubleVertexY[i] += centerY;
            vertexX[i] = (int)(doubleVertexX[i] + .5);
            vertexY[i] = (int)(doubleVertexY[i] + .5);
        }
        polygon = new Polygon(vertexX,vertexY,4);
    }
}
