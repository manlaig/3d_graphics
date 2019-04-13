package examples;

import java.awt.Color;
import java.io.FileNotFoundException;
import javax.swing.*;
import java.awt.*;
import src.*;
import src.Renderer;
import src.Camera.*;

public class Gradient
{
    public static void main(String[] args)
    {
        JFrame window = new JFrame();
        window.setSize(800, 800);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        Renderer r = new Renderer(window);
        Graphics2D g = (Graphics2D) window.getGraphics();
        g.setStroke(new BasicStroke(8));
        r.gradientLine(new Vector3(100, 100), new Vector3(500,500), Color.GREEN, Color.BLUE, g);
        r.gradientLine(new Vector3(100, 300), new Vector3(500,700), Color.RED, Color.YELLOW, g);
    }
}
