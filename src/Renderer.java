package src;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import src.*;
import src.Camera.*;

/*
    The whole purpose of this project is to learn about how graphics engines work,
    that is why this class rewrites Swing's drawing functions

    Most functions in this class are not optimized!
*/
public final class Renderer
{
    private int width;
    private int height;
    // faster rendering than calling repaint() on JFrame
    private BufferStrategy drawBuffer;

    public Renderer(JFrame window)
    {
        drawBuffer = window.getBufferStrategy();
        width = window.getWidth();
        height = window.getHeight();
    }

    private void point(int x, int y, Graphics g)
    {
        g.drawLine(x, y, x, y);
    }

    private void line(Vector3 start, Vector3 end, Graphics g)
    {
        g.drawLine((int)start.x, (int)start.y, (int)end.x, (int)end.y);
    }

    private void triangle(Vector3 p1, Vector3 p2, Vector3 p3, Graphics g)
    {
        line(p1, p2, g);
        line(p2, p3, g);
        line(p3, p1, g);
    }

    private void fillTriangle(Vector3 p1, Vector3 p2, Vector3 p3, Color color)
    {
        Graphics2D g = (Graphics2D) drawBuffer.getDrawGraphics();
        if(g == null)   return;
        g.setColor(color);
        g.translate(0, height);
        g.scale(1, -1);

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
        g.dispose();
    }

    private void fillTriangleZBuffer(Vector3 p1, Vector3 p2, Vector3 p3, Color color, float[][] buffer)
    {
        Graphics2D g = (Graphics2D) drawBuffer.getDrawGraphics();
        if(g == null)   return;
        g.setColor(color);
        g.translate(0, height);
        g.scale(1, -1);

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
                    try
                    {
                        if(z < buffer[x][y])
                        {
                            buffer[x][y] = z;
                            point(x, y, g);
                        }
                    } catch(ArrayIndexOutOfBoundsException e) {} // THIS IS NOT WORKING
                }
            }
        g.dispose();
    }

    double rotationDelta = 0;
    int incRate = 2;
    public void renderLightedZBuffer(Mesh mesh, Camera cam, float scale, Light light)
    {
        float[][] buffer = new float[width][height];
        for(int i = 0; i < buffer.length; i++)
            for(int j = 0; j < buffer[i].length; j++)
                buffer[i][j] = Float.MAX_VALUE;

        ArrayList<Vector3> verts = mesh.getVertices();
        ArrayList<Integer> tris = mesh.getTriangles();

        Vector3 camPos = cam.getPosition();
        Vector3 meshPos = mesh.transform.getPosition();

        Matrix4x4 translate = new Matrix4x4(Common.addVectors(meshPos, camPos));
        translate.scale(scale);

        for(int i = 0; i < tris.size(); i += 3)
        {
            // vertices is 0-indexed
            Vector3 p1 = verts.get(tris.get(i) - 1);
            Vector3 p2 = verts.get(tris.get(i+1) - 1);
            Vector3 p3 = verts.get(tris.get(i+2) - 1);

            double delta = Math.toRadians(rotationDelta);

            Matrix4x4 rotateY = new Matrix4x4();
            rotateY.m[0][0] = (float) Math.cos(delta);
            rotateY.m[2][0] = (float) -Math.sin(delta);
            rotateY.m[0][2] = (float) Math.sin(delta);
            rotateY.m[2][2] = (float) Math.cos(delta);   

            Matrix4x4 p1Mat = new Matrix4x4(p1);
            p1Mat.apply(rotateY).apply(translate);
            Vector3 p1New = p1Mat.getPosition();

            Matrix4x4 p2Mat = new Matrix4x4(p2);
            p2Mat.apply(rotateY).apply(translate);
            Vector3 p2New = p2Mat.getPosition();

            Matrix4x4 p3Mat = new Matrix4x4(p3);
            p3Mat.apply(rotateY).apply(translate);
            Vector3 p3New = p3Mat.getPosition();

            Vector3 normal = Common.crossProduct(Common.vectorFromVector3(p1New, p2New),
                                            Common.vectorFromVector3(p1New, p3New));
            float intensity = light.getIntensity(normal);
            
            // minimum and maximum grayscale value
            float minimumIntensity = 10f;
            int grayscale = (int) Math.max(minimumIntensity, 255 * intensity);
            fillTriangleZBuffer(p1New, p2New, p3New, new Color(grayscale, grayscale, grayscale), buffer);
        }
        rotationDelta += incRate;
    }

    public void wireFrameRender(Mesh mesh, Camera cam, float scale, Color color)
    {
        Graphics2D g = (Graphics2D) drawBuffer.getDrawGraphics();
        if(g == null)   return;
        g.setColor(color);
        g.translate(0, height);
        g.scale(1, -1);

        ArrayList<Vector3> verts = mesh.getVertices();
        ArrayList<Integer> tris = mesh.getTriangles();

        Vector3 camPos = cam.getPosition();
        Vector3 meshPos = mesh.transform.getPosition();

        Matrix4x4 translate = new Matrix4x4(Common.addVectors(meshPos, camPos));
        translate.scale(scale);

        for(int i = 0; i < tris.size(); i += 3)
        {
            // vertices is 0-indexed
            Vector3 p1 = verts.get(tris.get(i) - 1);
            Vector3 p2 = verts.get(tris.get(i+1) - 1);
            Vector3 p3 = verts.get(tris.get(i+2) - 1);

            Matrix4x4 p1Mat = new Matrix4x4(p1);
            p1Mat.apply(translate);

            Matrix4x4 p2Mat = new Matrix4x4(p2);
            p2Mat.apply(translate);

            Matrix4x4 p3Mat = new Matrix4x4(p3);
            p3Mat.apply(translate);

            // drawing the triangle
            triangle(p1Mat.getPosition(), p2Mat.getPosition(), p3Mat.getPosition(), g);
        }
        g.dispose();
    }

    public void Render(Scene scene)
    {
        Graphics g = drawBuffer.getDrawGraphics();

        // clearing the screen
        g.setColor(scene.backgroundColor);
        g.fillRect(0, 0, width, height);
        g.dispose();

        for(SceneObject obj : scene.getObjects())
            if(obj instanceof Mesh && scene.getCamera() instanceof OrthographicCamera)
            {
                Mesh mesh = (Mesh) obj;
                float scale = ((OrthographicCamera)scene.getCamera()).size();
                if(mesh.isLighted && scene.getLight() != null)
                    renderLightedZBuffer(mesh, scene.getCamera(), scale, scene.getLight());
                else
                    wireFrameRender(mesh, scene.getCamera(), scale, Color.white);
                drawBuffer.show();
            }
    }
}
