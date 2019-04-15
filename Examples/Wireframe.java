package examples;

import java.io.FileNotFoundException;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import src.*;
import src.Camera.*;

public class Wireframe
{
    public static void main(String[] args)
    {
        int width = 800;
        int height = 800;
        JFrame window = new JFrame();

        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        OrthographicCamera camera = new OrthographicCamera(new Vector3(0, 0, 0));
        Scene scene = new Scene(window, camera)
        {
            public void Update()
            {
            }
        };

        try
        {
            Mesh head = new Mesh(window, "./Models/hoop.obj", new Vector3(width/2, height/2), 250);
            head.isLighted = false;
            scene.add(head);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("file not found");
        }
    }
}
