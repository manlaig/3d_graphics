package examples;

import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.*;
import src.*;
import src.Camera.*;

public class Texturing extends Scene
{
    public Texturing(JFrame window, Camera camera, Light light)
    {
        super(window, camera, light);
    }

    public void Update()
    {

    }

    public static void main(String[] args) throws Exception
    {
        int width = 800, height = 800;
        
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        PerspectiveCamera camera = new PerspectiveCamera(new Vector3(0,0,0), 4f);
        Light light = new Light(new Vector3(0, 0, 1));
        Texturing scene = new Texturing(window, camera, light);
        scene.backgroundColor = Color.darkGray;

        BufferedImage tex = ImageReader.getTGAImage("./Textures/body_diffuse.tga");
        Mesh pose = new Mesh(window, "./Models/body.obj", new Vector3(width/2, height/2), 300, tex);

        scene.add(pose);
    }
}
