package src;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import src.*;

/*
    The whole purpose of this project is to learn about how graphics engines work,
    that is why this class rewrites Swing's drawing functions

    Most functions in this class are not optimized!
*/
public final class Renderer
{
    private Graphics g;
    private int width;
    private int height;

    public Renderer(JFrame window)
    {
        g = window.getGraphics();
        width = window.getWidth();
        height = window.getHeight();
    }

    private void point(int x, int y, Graphics g)
    {
        g.drawLine(x, y, x, y);
    }

    // OPTIMIZE THIS FUNCTION
    private void line(Vector3 start, Vector3 end, Color color)
    {
        if(g == null)   return;
        float dirX = end.x - start.x;
        float dirY = end.y - start.y;

        g.setColor(color);

        for(double t = 0; t <= 1; t += 0.05)
        {
            int x = (int) (start.x + t * dirX);
            int y = (int) (start.y + t * dirY);
            point(x, y, g);
        }
    }

    private void triangle(Vector3 p1, Vector3 p2, Vector3 p3, Color color)
    {
        line(p1, p2, color);
        line(p2, p3, color);
        line(p3, p1, color);
    }

    private void fillTriangle(Vector3 p1, Vector3 p2, Vector3 p3, Color color)
    {
        if(g == null || p1.y==p2.y && p1.y==p3.y)   return;
        g.setColor(color);

        int smallestX = (int) Math.min(p1.x, Math.min(p2.x, p3.x));
        int smallestY = (int) Math.min(p1.y, Math.min(p2.y, p3.y));
        int biggestX = (int) Math.max(p1.x, Math.max(p2.x, p3.x));
        int biggestY = (int) Math.max(p1.y, Math.max(p2.y, p3.y));

        for(int y = smallestY; y <= biggestY; y++)
        {
            for(int x = smallestX; x <= biggestX; x++)
            {
                Vector3 p = Common.getBarycentricCoordinates(new Vector3(x, y), p1, p2, p3);
                if(p.x > 0 && p.y > 0 && p.z > 0)
                    point(x, y, g);
            }
        }
    }

    private void fillTriangleZBuffer(Vector3 p1, Vector3 p2, Vector3 p3, Color color, float[][] buffer)
    {
        if(g == null)   return;
        g.setColor(color);

        int smallestX = (int) Math.min(p1.x, Math.min(p2.x, p3.x));
        int smallestY = (int) Math.min(p1.y, Math.min(p2.y, p3.y));
        int biggestX = (int) Math.max(p1.x, Math.max(p2.x, p3.x));
        int biggestY = (int) Math.max(p1.y, Math.max(p2.y, p3.y));

        for(int y = smallestY; y <= biggestY; y++)
            for(int x = smallestX; x <= biggestX; x++)
            {
                Vector3 p = Common.getBarycentricCoordinates(new Vector3(x, y), p1, p2, p3);
                if(p.x > 0 && p.y > 0 && p.z > 0)
                {
                    float z = p1.z * p.x + p2.z * p.y + p3.z * p.z;
                    if(z < buffer[x][y])
                    {
                        buffer[x][y] = z;
                        point(x, y, g);
                    } 
                }
            }
    }

    public void renderLightedZBuffer(Mesh mesh, float scale, Light light)
    {
        float[][] buffer = new float[width][height];
        for(int i = 0; i < buffer.length; i++)
            for(int j = 0; j < buffer[i].length; j++)
                buffer[i][j] = Float.MAX_VALUE;

        ArrayList<Vector3> verts = mesh.getVertices();
        ArrayList<Integer> tris = mesh.getTriangles();

        for(int i = 0; i < tris.size(); i += 3)
        {
            // vertices is 0-indexed
            Vector3 p1 = verts.get(tris.get(i) - 1);
            Vector3 p2 = verts.get(tris.get(i+1) - 1);
            Vector3 p3 = verts.get(tris.get(i+2) - 1);

            // applying transformation and scaling on the vertex
            Vector3 p1New = new Vector3(p1.x * scale + mesh.position.x,
                        height - p1.y * scale - mesh.position.y,
                        p1.z * scale + mesh.position.z);
            Vector3 p2New = new Vector3(p2.x * scale + mesh.position.x,
                        height - p2.y * scale - mesh.position.y,
                        p2.z * scale + mesh.position.z);
            Vector3 p3New = new Vector3(p3.x * scale + mesh.position.x,
                        height - p3.y * scale - mesh.position.y,
                        p3.z * scale + mesh.position.z);

            Vector3 normal = Common.crossProduct(Common.vectorFromVector3(p1New, p2New),
                                            Common.vectorFromVector3(p1New, p3New));
            float intensity = light.getIntensity(normal);
            
            // minimum and maximum grayscale value
            int grayscale = (int) Math.max(10f, 255 * intensity);
            fillTriangleZBuffer(p1New, p2New, p3New, new Color(grayscale, grayscale, grayscale), buffer);
        }
    }

    public void wireFrameRender(Mesh mesh, float scale, Color color)
    {
        // TODO: handle rotation also
        ArrayList<Vector3> verts = mesh.getVertices();
        ArrayList<Integer> tris = mesh.getTriangles();

        for(int i = 0; i < tris.size(); i += 3)
        {
            // vertices is 0-indexed
            Vector3 p1 = verts.get(tris.get(i) - 1);
            Vector3 p2 = verts.get(tris.get(i+1) - 1);
            Vector3 p3 = verts.get(tris.get(i+2) - 1);

            // applying transformation and scaling on the vertices
            Vector3 p1New = new Vector3(p1.x * scale + mesh.position.x,
                        height - p1.y * scale - mesh.position.y,
                        p1.z * scale + mesh.position.z);
            Vector3 p2New = new Vector3(p2.x * scale + mesh.position.x,
                        height - p2.y * scale - mesh.position.y,
                        p2.z * scale + mesh.position.z);
            Vector3 p3New = new Vector3(p3.x * scale + mesh.position.x,
                        height - p3.y * scale - mesh.position.y,
                        p3.z * scale + mesh.position.z);

            // drawing the triangle
            triangle(p1New, p2New, p3New, color);
        }
    }
}
