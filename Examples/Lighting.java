package examples;

import java.awt.Color;
import java.io.FileNotFoundException;
import javax.swing.*;
import src.*;
import src.Camera.*;

public class Lighting
{
    public static void main(String[] args)
    {
        int width = 800, height = 800;
        
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.black);
        window.setVisible(true);

        int cameraSize = 250;
        OrthographicCamera camera = new OrthographicCamera(new Vector3(0, 0, 0), cameraSize);
        Light light = new Light(new Vector3(0, 0, 1));
        Scene scene = new Scene(window, camera, light) {
            @Override
            public void Update()
            {
                System.out.println("STARTING");
                // have a loop here
            }
        };
        
        try
        {
            Mesh pose = new Mesh(window, "./Models/pose.obj", new Vector3(3*width/4, height/2));
            Mesh head = new Mesh(window, "./Models/head.obj", new Vector3(width/4, height/2));
            scene.add(pose);
            scene.add(head);
            scene.Render();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("file not found");
        }
    }
}
