package examples;

import java.awt.Color;
import java.io.FileNotFoundException;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import src.*;
import src.Camera.*;

public class Lighting extends Scene
{
    // used for rotation demo
    double rotationDelta = 0;
    int incRate = 1;

    public Lighting(JFrame window, Camera camera, Light light)
    {
        super(window, camera, light);
        updateDelay = 400;
    }

    public void Update()
    {
        double delta = Math.toRadians(rotationDelta);
        rotationDelta += incRate;
        Matrix4x4 rotateY = new Matrix4x4();
        rotateY.m[0][0] = (float) Math.cos(delta);
        rotateY.m[2][0] = (float) -Math.sin(delta);
        rotateY.m[0][2] = (float) Math.sin(delta);
        rotateY.m[2][2] = (float) Math.cos(delta);

        Matrix4x4 m = Common.matrixMultiply(rotateY, new Matrix4x4(light.direction));
        light.direction = m.getPosition();
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        int width = 800, height = 800;
        
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        //OrthographicCamera camera = new OrthographicCamera(new Vector3(0, 0, 0));
        PerspectiveCamera camera = new PerspectiveCamera(new Vector3(0,0,0), 4f);
        Light light = new Light(new Vector3(0, 0, 1));
        Lighting scene = new Lighting(window, camera, light);

        Mesh pose = new Mesh(window, "./Models/head.obj", new Vector3(width/2, height/2), 300);
        scene.add(pose);
        scene.backgroundColor = Color.DARK_GRAY;

        window.addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent e)
            {
                int key = e.getKeyCode();
                
                if(key == KeyEvent.VK_LEFT)
                    scene.getCamera().getPosition().x += 50;
                if(key == KeyEvent.VK_RIGHT)
                    scene.getCamera().getPosition().x -= 50;
            }

            public void keyTyped(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {}   
        });
    }
}
