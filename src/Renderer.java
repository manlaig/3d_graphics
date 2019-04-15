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

    public void point(int x, int y, Graphics g)
    {
        g.drawLine(x, y, x, y);
    }

    public void line(Vector3 start, Vector3 end, Graphics g)
    {
        g.drawLine((int)start.x, (int)start.y, (int)end.x, (int)end.y);
    }

    public void gradientLine(Vector3 start, Vector3 end, Color from, Color to, Graphics g3d)
    {
        float r = to.getRed() - from.getRed();
        float g = to.getGreen() - from.getGreen();
        float b = to.getBlue() - from.getBlue();

        float x = end.x - start.x;
        float y = end.y - start.y;

        for(float i = 0; i <= 1f; i += 0.001)
        {
            int rr = (int)Math.max(0, Math.min(from.getRed() + (int)r*i, 255));
            int gg = (int)Math.max(0, Math.min(from.getGreen() + (int)g*i, 255));
            int bb = (int)Math.max(0, Math.min(from.getBlue() + (int)b*i, 255));
            g3d.setColor(new Color(rr, gg, bb));
            point((int)(start.x + x*i), (int)(start.y + y*i), g3d);
        }
    }

    public void triangle(Vector3 p1, Vector3 p2, Vector3 p3, Graphics g)
    {
        //g.drawPolygon(new int[]{(int)p1.x, (int)p2.x, (int)p3.x},
                    //new int[]{(int)p1.y, (int)p2.y, (int)p3.y}, 3);
        line(p1, p2, g);
        line(p2, p3, g);
        line(p3, p1, g);
    }

    public void fillTriangle(Vector3 p1, Vector3 p2, Vector3 p3, Color color)
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

    public void renderLightedZBuffer(Mesh mesh, Matrix4x4 transformation, Light light)
    {
        float[][] buffer = new float[width][height];
        for(int i = 0; i < buffer.length; i++)
            for(int j = 0; j < buffer[i].length; j++)
                buffer[i][j] = Float.MAX_VALUE;

        ArrayList<Vector3> verts = mesh.getVertices();
        ArrayList<Vector3> normals = mesh.getNormals();
        ArrayList<Integer> tris = mesh.getTriangles();

        for(int i = 0; i < tris.size(); i += 3)
        {
            // vertices is 0-indexed
            Vector3 p1 = verts.get(tris.get(i) - 1);
            Vector3 p2 = verts.get(tris.get(i+1) - 1);
            Vector3 p3 = verts.get(tris.get(i+2) - 1);

            Matrix4x4 p1Mat = new Matrix4x4(p1).apply(transformation);
            Vector3 p1New = p1Mat.getPosition();

            Matrix4x4 p2Mat = new Matrix4x4(p2).apply(transformation);
            Vector3 p2New = p2Mat.getPosition();

            Matrix4x4 p3Mat = new Matrix4x4(p3).apply(transformation);
            Vector3 p3New = p3Mat.getPosition();

            if(!mesh.vertexShadingSupported())
            {
                Vector3 normal = Common.crossProduct(Common.vectorFromVector3(p1New, p2New),
                                                    Common.vectorFromVector3(p1New, p3New));
                float intensity = light.getIntensity(normal);
                
                // minimum and maximum grayscale value
                float minimumIntensity = 10f;
                int grayscale = (int) Math.max(minimumIntensity, 255 * intensity);
                fillTriangleZBuffer(p1New, p2New, p3New, new Color(grayscale, grayscale, grayscale), buffer);
            }
            else
            {
                Vector3 n1 = normals.get(tris.get(i) - 1);
                Vector3 n2 = normals.get(tris.get(i+1) - 1);
                Vector3 n3 = normals.get(tris.get(i+2) - 1);
                /*Matrix4x4 n1Mat = new Matrix4x4(n1).apply(transformation);
                Vector3 n1New = n1Mat.getPosition();
                Matrix4x4 n2Mat = new Matrix4x4(n2).apply(transformation);
                Vector3 n2New = n2Mat.getPosition();
                Matrix4x4 n3Mat = new Matrix4x4(n3).apply(transformation);
                Vector3 n3New = n3Mat.getPosition();*/
                
                Graphics2D g = (Graphics2D) drawBuffer.getDrawGraphics();
                if(g == null)   return;
                g.translate(0, height);
                g.scale(1, -1);
    
                int smallestX = (int) Math.min(p1New.x, Math.min(p2New.x, p3New.x));
                int smallestY = (int) Math.min(p1New.y, Math.min(p2New.y, p3New.y));
                int biggestX = (int) Math.max(p1New.x, Math.max(p2New.x, p3New.x));
                int biggestY = (int) Math.max(p1New.y, Math.max(p2New.y, p3New.y));
    
                for(int y = smallestY; y <= biggestY; y++)
                    for(int x = smallestX; x <= biggestX; x++)
                    {
                        Vector3 p = Common.getBarycentricCoordinates(new Vector3(x, y), p1New, p2New, p3New);
                        //p.x: influence of p1 on the point at (x, y)
                        //p.y: influence of p2 on the point at (x, y)
                        //p.z: influence of p3 on the point at (x, y)
                        if(p.x > 0 && p.y > 0 && p.z > 0)
                        {
                            // influence of normal vectors on the point (x, y)
                            Vector3 n1Inf = new Vector3(n1).mult(p.x);
                            Vector3 n2Inf = new Vector3(n2).mult(p.y);
                            Vector3 n3Inf = new Vector3(n3).mult(p.z);
                            Vector3 sum = Common.addVectors(n1Inf, n2Inf, n3Inf);

                            float intensity = light.getIntensity(sum);
                            float minimumIntensity = 10f;
                            int grayscale = (int) Math.max(minimumIntensity, 255 * intensity);
                            g.setColor(new Color(grayscale, grayscale, grayscale));
                            float z = p1New.z * p.x + p2New.z * p.y + p3New.z * p.z;
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
            }
        }
    }

    public void wireFrameRender(Mesh mesh, Matrix4x4 transformation, Color color)
    {
        Graphics2D g = (Graphics2D) drawBuffer.getDrawGraphics();
        if(g == null)   return;
        g.setColor(color);
        g.translate(0, height);
        g.scale(1, -1);

        ArrayList<Vector3> verts = mesh.getVertices();
        ArrayList<Integer> tris = mesh.getTriangles();

        for(int i = 0; i < tris.size(); i += 3)
        {
            // vertices is 0-indexed
            Vector3 p1 = verts.get(tris.get(i) - 1);
            Vector3 p2 = verts.get(tris.get(i+1) - 1);
            Vector3 p3 = verts.get(tris.get(i+2) - 1);

            Matrix4x4 p1Mat = new Matrix4x4(p1).apply(transformation);
            Matrix4x4 p2Mat = new Matrix4x4(p2).apply(transformation);
            Matrix4x4 p3Mat = new Matrix4x4(p3).apply(transformation);

            // drawing the triangle
            triangle(p1Mat.getPosition(), p2Mat.getPosition(), p3Mat.getPosition(), g);
        }
        g.dispose();
    }

    // used for rotation demo
    double rotationDelta = 180;
    int incRate = 2;
    public void Render(Scene scene)
    {
        Graphics g = drawBuffer.getDrawGraphics();

        // clearing the screen
        g.setColor(scene.backgroundColor);
        g.fillRect(0, 0, width, height);
        g.dispose();

        for(SceneObject obj : scene.getObjects())
        {
            Camera cam = scene.getCamera();
            if(obj instanceof Mesh)
            {
                Mesh mesh = (Mesh) obj;

                Vector3 camPos = cam.getPosition();
                Vector3 meshPos = mesh.transform.getPosition();

                Matrix4x4 transformation = new Matrix4x4(Common.addVectors(meshPos, camPos));
                transformation.scale(mesh.getScale());
                Matrix4x4 newT = Common.matrixMultiply(transformation, cam.projMatrix);

                double delta = Math.toRadians(rotationDelta);
                rotationDelta += incRate;
                Matrix4x4 rotateY = new Matrix4x4();
                rotateY.m[0][0] = (float) Math.cos(delta);
                rotateY.m[2][0] = (float) -Math.sin(delta);
                rotateY.m[0][2] = (float) Math.sin(delta);
                rotateY.m[2][2] = (float) Math.cos(delta);
                
                // merging both 2 transformation into 1 matrix
                // this means we'll rotate, then translate and scale
                rotateY.apply(newT);

                if(mesh.isLighted && scene.getLight() != null)
                {
                    renderLightedZBuffer(mesh, rotateY, scene.getLight());
                }
                else
                {
                    wireFrameRender(mesh, rotateY, Color.white);
                }
                drawBuffer.show();
            }
        }
    }
}
