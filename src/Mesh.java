package src;

import src.Shape;
import src.Point;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;

public class Mesh
{
    Shape renderer;
    String fileName;
    JFrame window;

    public Mesh(JFrame window, String fileName)
    {
        renderer = new Shape(window);
        this.fileName = fileName;
        this.window = window;
    }

    public void renderLighted()
    {
        // use this to rasterize
        //window.getGraphics().fillPolygon(new int[]{(int)p1.x, (int)p2.x, (int)p3.x},
                                        //new int[]{(int)p1.y, (int)p2.y, (int)p3.y}, 3);
    }

    // ONLY supports .OBJ files
    // look at .obj file format before reading this function
    public void wireFrameRender(Point position, float scale) throws FileNotFoundException
    {
        // TODO: handle rotation also

        ArrayList<Point> vertices = new ArrayList<>();
        ArrayList<Integer> triangles = new ArrayList<>();
        Scanner sc = new Scanner(new File(fileName));
        
        while(sc.hasNextLine())
        {
            String line = sc.nextLine();
            if(line.length() < 7)
                continue;
            if(line.charAt(0) == 'v' && line.charAt(1) == ' ')
            {
                // vertex
                String[] nums = line.split(" ");
                try
                {
                    // nums[0] is "v"
                    // vertices data starts from nums[1]
                    float x = Float.parseFloat(nums[1]);
                    float y = Float.parseFloat(nums[2]);
                    float z = Float.parseFloat(nums[3]);

                    /*
                        the default origin is at top left,
                        so the mesh is flipped on the z-axis
                        fix: subtract from window height
                    */
                    Point vertex = new Point(x * scale + position.x,
                                             window.getHeight() - y * scale + position.y,
                                             z * scale + position.z);
                    vertices.add(vertex);
                }
                catch(NumberFormatException e) {}
            }
            else if (line.charAt(0) == 'f')
            {
                // triangle
                String[] points = line.split(" ");
                // points[0] is "f"
                // the triangle data starts from points[1]
                String[] v0 = points[1].split("/");
                String[] v1 = points[2].split("/");
                String[] v2 = points[3].split("/");

                // vi[0] is the vertex that we want
                triangles.add(Integer.valueOf(v0[0]));
                triangles.add(Integer.valueOf(v1[0]));
                triangles.add(Integer.valueOf(v2[0]));
            }
        }

        for(int i = 0; i < triangles.size(); i += 3)
        {
            try
            {
                Point p1 = vertices.get(triangles.get(i) - 1);
                Point p2 = vertices.get(triangles.get(i+1) - 1);
                Point p3 = vertices.get(triangles.get(i+2) - 1);
                // drawing the triangle
                renderer.triangle(p1, p2, p3, Color.black);
            }
            catch(IndexOutOfBoundsException e) {}
        }
    }
}
