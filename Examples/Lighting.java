package examples;

import java.io.FileNotFoundException;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import src.Mesh;
import src.Shape;
import src.Point;
import src.Light;

public class Lighting
{
    public static void main(String[] args)
    {
        int width = 800;
        int height = 800;
        Point scale = new Point(150, 150, 150);
        Light light = new Light(new Point(0, 0, 1));
        JFrame window = new JFrame();

        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        try
        {
            Mesh head = new Mesh(window, "./Models/ball.obj");
            Mesh pose = new Mesh(window, "./Models/basketball_hoop.obj");

            // the origin is bottom-left
            head.renderLighted(new Point(3*width/4, height/2), scale, light);
            pose.renderLighted(new Point(width/4, height/2), scale, light);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("file not found");
        }
    }
}
