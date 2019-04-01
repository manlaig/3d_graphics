package examples;

import java.awt.Color;
import java.io.FileNotFoundException;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import src.*;
import src.Camera.*;

public class Lighting
{
    public static void main(String[] args) throws FileNotFoundException
    {
        int width = 1400, height = 800;
        
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        int cameraSize = 250;
        OrthographicCamera camera = new OrthographicCamera(new Vector3(0, 0, 0), cameraSize);
        Light light = new Light(new Vector3(0, 0, -1));

        Scene scene = new Scene(window, camera, light)
        {
            public void Update()
            {
            }
        };

        Mesh pose = new Mesh(window, "./Models/head.obj", new Vector3(width/2, height/2));
        scene.add(pose);
        scene.backgroundColor = Color.DARK_GRAY;

        window.addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent e)
            {
                /*int key = e.getKeyCode();
            
                if (key == KeyEvent.VK_A)
                    pose.getPosition().x -= 15;
                if (key == KeyEvent.VK_D)
                    pose.getPosition().x += 15;
                if (key == KeyEvent.VK_W)
                    pose.getPosition().y += 15;
                if (key == KeyEvent.VK_S)
                    pose.getPosition().y -= 15;
                if(key == KeyEvent.VK_LEFT)
                    scene.getCamera().getPosition().x += 50;
                if(key == KeyEvent.VK_RIGHT)
                    scene.getCamera().getPosition().x -= 50;*/
            }

            public void keyTyped(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {}   
        });
    }
}
