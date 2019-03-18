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

/*
    The whole purpose of this project is to learn about how graphics engines work,
    that is why this class rewrites Swing's drawing functions

    Most functions in this class are not optimized!
*/
public class Shape extends JFrame
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

    public void line(Point start, Point end, Color color)
    {
        float dirX = end.x - start.x;
        float dirY = end.y - start.y;

        Graphics2D g = (Graphics2D) window.getGraphics();
        if(g == null)
            return;
            
        Color original = g.getColor();
        g.setColor(color);

        for(double t = 0; t <= 1; t += 0.007)
        {
            int x = (int) (start.x + t * dirX);
            int y = (int) (start.y + t * dirY);
            point(x, y, g);
        }
        g.setColor(original);
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
        // Swing's algorithm is much faster than mine
        Graphics g = window.getGraphics();
        if(g == null)
            return;
        Color original = g.getColor();
        g.setColor(color);
        g.fillPolygon(new int[]{(int)p1.x, (int)p2.x, (int)p3.x},
                    new int[]{(int)p1.y, (int)p2.y, (int)p3.y}, 3);
        g.setColor(original);

        /*
        This algorithm will draw a line and remember the current point's 'y' value,
        then it will draw a line between two lines if that y is already in the map
        */
        /*HashMap<Integer, Point> map = new HashMap<>();

        Graphics2D g = (Graphics2D) window.getGraphics();
        if(g == null)
            return;
        Color original = g.getColor();
        g.setColor(color);

        for(double t = 0; t <= 1; t += 0.005)
        {
            int x = (int) (p2.x + t * (p3.x - p2.x));
            int y = (int) (p2.y + t * (p3.y - p2.y));
            point(x, y, g);
            if(map.containsKey(y))
                line(new Point(x, y), new Point(map.get(y).x, y), color);
            else
                map.put(y, new Point(x, y));


            int x2 = (int) (p1.x + t * (p3.x - p1.x));
            int y2 = (int) (p1.y + t * (p3.y - p1.y));
            point(x2, y2, g);
            if(map.containsKey(y2))
                line(new Point(x2, y2), new Point(map.get(y2).x, y2), color);
            else
                map.put(y2, new Point(x2, y2));


            int x3 = (int) (p1.x + t * (p2.x - p1.x));
            int y3 = (int) (p1.y + t * (p2.y - p1.y));
            point(x3, y3, g);
            if(map.containsKey(y3))
                line(new Point(x3, y3), new Point(map.get(y3).x, y3), color);
            else
                map.put(y3, new Point(x3, y3));
        }

        g.setColor(original);*/
    }
}
