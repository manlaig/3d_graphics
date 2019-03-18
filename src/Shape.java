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
        Graphics g = window.getGraphics();
        if(g == null)
            return;
        Color original = g.getColor();
        g.setColor(color);
        g.fillPolygon(new int[]{(int)p1.x, (int)p2.x, (int)p3.x},
                    new int[]{(int)p1.y, (int)p2.y, (int)p3.y}, 3);
        g.setColor(original);
    }
}
