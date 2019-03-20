package src;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import src.Point;
import src.Common;

/*
    The whole purpose of this project is to learn about how graphics engines work,
    that is why this class rewrites Swing's drawing functions

    Most functions in this class are not optimized!
*/
public final class Shape extends JFrame
{
    JFrame window;
    int width;
    int height;

    public Shape(JFrame window)
    {
        this.window = window;
        width = window.getWidth();
        height = window.getHeight();
    }

    private void point(int x, int y, Graphics g)
    {
        g.drawLine(x, y, x, y);
    }

    // OPTIMIZE THIS FUNCTION
    public void line(Point start, Point end, Color color)
    {
        float dirX = end.x - start.x;
        float dirY = end.y - start.y;

        Graphics2D g = (Graphics2D) window.getGraphics();
        if(g == null)
            return;
        g.setColor(color);

        for(double t = 0; t <= 1; t += 0.05)
        {
            int x = (int) (start.x + t * dirX);
            int y = (int) (start.y + t * dirY);
            point(x, y, g);
        }
    }

    public void circle(Point pos, int radius)
    {
        Graphics g = window.getGraphics();
        if(g != null)
            for(double degree = 0; degree <= 360; degree += 0.1)
            {
                int x = (int) (pos.x + Math.cos(degree) * radius);
                int y = (int) (pos.y + Math.sin(degree) * radius);
                point(x, y, g);
            }
    }

    public void triangle(Point p1, Point p2, Point p3, Color color)
    {
        line(p1, p2, color);
        line(p2, p3, color);
        line(p3, p1, color);
    }

    public void fillTriangle(Point p1, Point p2, Point p3, Color color)
    {
        Graphics g = window.getGraphics();
        if(g == null || p1.y==p2.y && p1.y==p3.y)   return;
        g.setColor(color);

        int smallestX = (int) Math.min(p1.x, Math.min(p2.x, p3.x));
        int smallestY = (int) Math.min(p1.y, Math.min(p2.y, p3.y));
        int biggestX = (int) Math.max(p1.x, Math.max(p2.x, p3.x));
        int biggestY = (int) Math.max(p1.y, Math.max(p2.y, p3.y));

        for(int y = smallestY; y <= biggestY; y++)
        {
            for(int x = smallestX; x <= biggestX; x++)
            {
                Point p = Common.getBarycentricPoint(new Point(x, y), p1, p2, p3);
                if(p.x > 0 && p.y > 0 && p.z > 0)
                    point(x, y, g);
            }
        }
    }

    public void fillTriangleZBuffer(Point p1, Point p2, Point p3, Color color, float[][] buffer)
    {
        Graphics g = window.getGraphics();
        if(g == null)   return;
        g.setColor(color);

        int smallestX = (int) Math.min(p1.x, Math.min(p2.x, p3.x));
        int smallestY = (int) Math.min(p1.y, Math.min(p2.y, p3.y));
        int biggestX = (int) Math.max(p1.x, Math.max(p2.x, p3.x));
        int biggestY = (int) Math.max(p1.y, Math.max(p2.y, p3.y));

        for(int y = smallestY; y <= biggestY; y++)
        {
            for(int x = smallestX; x <= biggestX; x++)
            {
                Point p = Common.getBarycentricPoint(new Point(x, y), p1, p2, p3);
                if(p.x > 0 && p.y > 0 && p.z > 0)
                {
                    float z = p1.z * p.x + p2.z * p.y + p3.z * p.z;
                    if(z < buffer[x][y])
                    {
                        buffer[x][y] = z;
                        point(x, y, g);
                    } 
                }
            }
        }
    }
}
