package examples;

import java.io.FileNotFoundException;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import src.Mesh;
import src.Shape;
import src.Point;

public class Wireframe
{
    public static void main(String[] args)
    {
        int width = 800;
        int height = 800;
        Point scale = new Point(250, 250, 250);
        JFrame window = new JFrame();

        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        try
        {
            Mesh head = new Mesh(window, "./Models/head.obj");
            Mesh pose = new Mesh(window, "./Models/pose.obj");
            // the origin is bottom-left
            head.wireFrameRender(new Point(3*width/4, height/2), scale, Color.black);
            pose.wireFrameRender(new Point(width/4, height/2), scale, Color.black);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("file not found");
        }
    }
}
