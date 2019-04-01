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

        int cameraSize = 250;
        OrthographicCamera camera = new OrthographicCamera(new Vector3(0, 0, 0), cameraSize);
        Scene scene = new Scene(window, camera)
        {
            public void Update()
            {
            }
        };

        try
        {
            Mesh head = new Mesh(window, "./Models/hoop.obj", new Vector3(3*width/4, height/2));
            Mesh pose = new Mesh(window, "./Models/ball.obj", new Vector3(width/4, height/2));
            head.isLighted = false;
            pose.isLighted = false;
            scene.add(head);
            scene.add(pose);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("file not found");
        }
    }
}
