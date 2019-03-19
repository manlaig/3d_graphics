package src;

import src.Shape;
import src.Point;
import src.Common;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;

public class Mesh
{
    Shape renderer;
    JFrame window;

    ArrayList<Point> vertices;
    ArrayList<Integer> triangles;

    public Mesh(JFrame window, String fileName) throws FileNotFoundException
    {
        renderer = new Shape(window);
        this.window = window;

        vertices = new ArrayList<>();
        triangles = new ArrayList<>();

        // to normalize the positions of the meshes
        Point smallest = new Point(1, 1, 1);
        
        Scanner sc = new Scanner(new File(fileName));

        while(sc.hasNextLine())
        {
            String line = sc.nextLine();
            if(line.length() < 7)
            continue;
            String[] nums = line.split("\\s+");
            if(nums[0].equals("v"))
            {
                // vertex
                try
                {
                    // nums[0] is "v"
                    // vertices data starts from nums[1]
                    float x = Float.parseFloat(nums[1]);
                    float y = Float.parseFloat(nums[2]);
                    float z = Float.parseFloat(nums[3]);
                    
                    if(x > smallest.x)  smallest.x = x;
                    if(y > smallest.y)  smallest.y = y;
                    if(z > smallest.z)  smallest.z = z;

                    /*
                        the default origin is at top left,
                        so the mesh is flipped on the z-axis
                        fix: subtract from window height
                    */
                    Point vertex = new Point(x, y, z);
                    vertices.add(vertex);
                }
                catch(NumberFormatException e) {}
            }
            else if (nums[0].equals("f"))
            {
                // triangle
                // points[0] is "f"
                // the triangle data starts from points[1]
                String[] v0 = nums[1].split("/");
                String[] v1 = nums[2].split("/");
                String[] v2 = nums[3].split("/");

                // vi[0] is the vertex that we want
                triangles.add(Integer.valueOf(v0[0]));
                triangles.add(Integer.valueOf(v1[0]));
                triangles.add(Integer.valueOf(v2[0]));
            }
        }

        for(Point vertex : vertices)
        {
            vertex.x /= smallest.x;
            vertex.y /= smallest.y;
            vertex.z /= smallest.z;
        }
    }

    public void renderLighted(Point position, Point scale, Light light)
    {
        for(int i = 0; i < triangles.size(); i += 3)
        {
            // vertices is 0-indexed
            Point p1 = vertices.get(triangles.get(i) - 1);
            Point p2 = vertices.get(triangles.get(i+1) - 1);
            Point p3 = vertices.get(triangles.get(i+2) - 1);

            // applying transformation and scaling on the vertex
            Point p1New = new Point(p1.x * scale.x + position.x,
                        window.getHeight() - p1.y * scale.y - position.y,
                        p1.z * scale.z + position.z);
            Point p2New = new Point(p2.x * scale.x + position.x,
                        window.getHeight() - p2.y * scale.y - position.y,
                        p2.z * scale.z + position.z);
            Point p3New = new Point(p3.x * scale.x + position.x,
                        window.getHeight() - p3.y * scale.y - position.y,
                        p3.z * scale.z + position.z);

            Point normal = Common.crossProduct(Common.vectorFromPoint(p1New, p2New),
                                            Common.vectorFromPoint(p3New, p1New));
            float intensity = light.getIntensity(normal);
            
            int grayscale = intensity <= 0 ? 1 : (int) (255 * intensity);

            renderer.fillTriangle(p1New, p2New, p3New, new Color(grayscale, grayscale, grayscale));
        }
    }

    // ONLY supports .OBJ files
    // look at .obj file format before reading this function
    public void wireFrameRender(Point position, Point scale, Color color)
    {
        // TODO: handle rotation also

        for(int i = 0; i < triangles.size(); i += 3)
        {
            // vertices is 0-indexed
            Point p1 = vertices.get(triangles.get(i) - 1);
            Point p2 = vertices.get(triangles.get(i+1) - 1);
            Point p3 = vertices.get(triangles.get(i+2) - 1);

            // applying transformation and scaling on the vertex
            Point p1New = new Point(p1.x * scale.x + position.x,
                        window.getHeight() - p1.y * scale.y - position.y,
                        p1.z * scale.z + position.z);
            Point p2New = new Point(p2.x * scale.x + position.x,
                        window.getHeight() - p2.y * scale.y - position.y,
                        p2.z * scale.z + position.z);
            Point p3New = new Point(p3.x * scale.x + position.x,
                        window.getHeight() - p3.y * scale.y - position.y,
                        p3.z * scale.z + position.z);

            // drawing the triangle
            renderer.triangle(p1New, p2New, p3New, color);
        }
    }
}
