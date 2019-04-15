package src;

import src.*;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

public class Mesh extends SceneObject
{
    public boolean isLighted = true;
    private boolean canVertexShade = false;

    private ArrayList<Vector3> vertices;
    private ArrayList<Vector3> normals;
    private ArrayList<Integer> vertexTriangles;
    private ArrayList<Integer> textureTriangles;
    private ArrayList<Vector3> textureCoordinates;
    private BufferedImage texture;

    public Mesh(JFrame window, String filePath, Vector3 pos, float scale) throws FileNotFoundException
    {
        super(pos, scale);
        parseOBJFile(filePath);
    }

    public Mesh(JFrame window, String filePath, Vector3 pos, float scale, BufferedImage img) throws FileNotFoundException
    {
        super(pos, scale);
        parseOBJFile(filePath);
        texture = img;
    }

    public ArrayList<Vector3> getVertices()
    {
        return vertices;
    }

    public ArrayList<Integer> getVertexTriangles()
    {
        return vertexTriangles;
    }

    public ArrayList<Integer> getTextureTriangles()
    {
        return textureTriangles;
    }

    public ArrayList<Vector3> getNormals()
    {
        return normals;
    }

    public ArrayList<Vector3> getTextureCoords()
    {
        return textureCoordinates;
    }

    public boolean vertexShadingSupported()
    {
        return canVertexShade;
    }

    public BufferedImage getTexture()
    {
        return texture;
    }

    public boolean isTextured()
    {
        return texture != null && !textureCoordinates.isEmpty();
    }

    // look at .obj file format before reading this function
    private void parseOBJFile(String filePath) throws FileNotFoundException
    {
        normals = new ArrayList<>();
        vertices = new ArrayList<>();
        vertexTriangles = new ArrayList<>();
        textureTriangles = new ArrayList<>();
        textureCoordinates = new ArrayList<>();

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
                vertices.add(new Vector3(x, y, z));
            }
            else if (nums[0].equals("vt"))
            {
                float x = Float.parseFloat(nums[1]);
                float y = Float.parseFloat(nums[2]);
                textureCoordinates.add(new Vector3(x, y, 0));
            }
            else if (nums[0].equals("vn"))
            {
                float x = Float.parseFloat(nums[1]);
                float y = Float.parseFloat(nums[2]);
                float z = Float.parseFloat(nums[3]);
                normals.add(new Vector3(x, y, z));
            }
            else if (nums[0].equals("f"))
            {
                // triangle
                // the triangle data starts from Vector3s[1]
                String[] v0 = nums[1].split("/");
                String[] v1 = nums[2].split("/");
                String[] v2 = nums[3].split("/");

                // vi[0] is the vertex that we want
                vertexTriangles.add(Integer.valueOf(v0[0]));
                vertexTriangles.add(Integer.valueOf(v1[0]));
                vertexTriangles.add(Integer.valueOf(v2[0]));

                // vi[1] is the index of the texture coordinate array
                // we will use that value for map textures onto triangles
                textureTriangles.add(Integer.valueOf(v0[1]));
                textureTriangles.add(Integer.valueOf(v1[1]));
                textureTriangles.add(Integer.valueOf(v2[1]));
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
