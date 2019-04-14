package src;

import src.*;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;

public class Mesh extends SceneObject
{
    public boolean isLighted = true;
    private boolean canVertexShade = false;

    private ArrayList<Vector3> vertices;
    private ArrayList<Vector3> normals;
    private ArrayList<Integer> triangles;


    public Mesh(JFrame window, String filePath, Vector3 pos) throws FileNotFoundException
    {
        super(pos);
        parseOBJFile(filePath);
    }

    public ArrayList<Vector3> getVertices()
    {
        return vertices;
    }

    public ArrayList<Integer> getTriangles()
    {
        return triangles;
    }

    public ArrayList<Vector3> getNormals()
    {
        return normals;
    }

    public boolean vertexShadingSupported()
    {
        return canVertexShade;
    }

    // look at .obj file format before reading this function
    private void parseOBJFile(String filePath) throws FileNotFoundException
    {
        normals = new ArrayList<>();
        vertices = new ArrayList<>();
        triangles = new ArrayList<>();

        // to normalize the positions of the meshes
        Vector3 smallest = new Vector3(1, 1, 1);
        
        Scanner sc = new Scanner(new File(filePath));

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
                    Vector3 vertex = new Vector3(x, y, z);
                    vertices.add(vertex);
                }
                catch(NumberFormatException e) {}
            }
            else if (nums[0].equals("vn"))
            {
                float x = Float.parseFloat(nums[1]);
                float y = Float.parseFloat(nums[2]);
                float z = Float.parseFloat(nums[3]);

                Vector3 normal = new Vector3(x, y, z);
                normals.add(normal);
            }
            else if (nums[0].equals("f"))
            {
                // triangle
                // the triangle data starts from Vector3s[1]
                String[] v0 = nums[1].split("/");
                String[] v1 = nums[2].split("/");
                String[] v2 = nums[3].split("/");

                // vi[0] is the vertex that we want
                triangles.add(Integer.valueOf(v0[0]));
                triangles.add(Integer.valueOf(v1[0]));
                triangles.add(Integer.valueOf(v2[0]));
            }
        }
        sc.close();

        if(vertices.size() == normals.size())
            canVertexShade = true;
        
        // some .obj file have too big position values
        for(int i = 0; i < vertices.size(); i++)
        {
            vertices.get(i).x /= smallest.x;
            vertices.get(i).y /= smallest.y;
            vertices.get(i).z /= smallest.z;
            if(canVertexShade)
                Common.normalize(normals.get(i));
        }
    }
}
