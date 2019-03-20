package examples;

import java.io.FileNotFoundException;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import src.*;

public class Lighting
{
    public static void main(String[] args)
    {
        int width = 800;
        int height = 800;
        Point scale = new Point(250, 250, 250);
        Light light = new Light(new Point(0, 0, 1));
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setBackground(Color.yellow);
        
        try
        {
            Mesh pose = new Mesh(window, "./Models/pose.obj");
            Mesh head = new Mesh(window, "./Models/head.obj");

            // the origin is bottom-left
            head.renderLightedZBuffer(new Point(3*width/4, height/2), scale, light);
            pose.renderLightedZBuffer(new Point(width/4, height/2), scale, light);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("file not found");
        }
    }
}
