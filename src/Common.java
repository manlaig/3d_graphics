package src;

import src.*;

/*
    Utility functions
*/
public final class Common
{
    public static Vector3 crossProduct(Vector3 p1, Vector3 p2)
    {
        float x = p1.y*p2.z - p2.y*p1.z;
        float y = p1.x*p2.z - p2.x*p1.z;
        float z = p1.x*p2.y - p2.x*p1.y;

        return new Vector3(x, y, z);
    }

    public static float dotProduct(Vector3 p1, Vector3 p2)
    {
        return p1.x*p2.x + p1.y*p2.y + p1.z*p2.z;
    }

    public static Vector3 vectorFromVector3(Vector3 from, Vector3 to)
    {
        float x = to.x - from.x;
        float y = to.y - from.y;
        float z = to.z - from.z;
        return new Vector3(x, y, z);
    }

    public static void normalize(Vector3 p)
    {
        float mag = (float) Math.sqrt(p.x*p.x + p.y*p.y + p.z*p.z);
        p.x = p.x / mag;
        p.y = p.y / mag;
        p.z = p.z / mag;
    }

    public static Vector3 addVectors(Vector3 a, Vector3 b)
    {
        return new Vector3(a.x+b.x, a.y+b.y, a.z+b.z);
    }

    // you need a wrapper to swapper class to swap objects
    // so we can't make a generic swap function
    public static void swapVector3s(Vector3 a, Vector3 b)
    {
        Vector3 t = new Vector3(a.x, a.y, a.z);
        a.x = b.x;  a.y = b.y;  a.z = b.z;
        b.x = t.x;  b.y = t.y;  b.z = t.z;
    }

    public static Vector3 getBarycentricCoordinates(Vector3 p, Vector3 a, Vector3 b, Vector3 c)
    {
        Vector3 u = Common.crossProduct(new Vector3(c.x-a.x, b.x-a.x, a.x-p.x), new Vector3(c.y-a.y, b.y-a.y, a.y-p.y));
        /* `pts` and `P` has integer value as coordinates
        so `abs(u[2])` < 1 means `u[2]` is 0, that means
        triangle is degenerate, in this case return something with negative coordinates */
        return new Vector3(1.f-(u.x-u.y)/u.z, -u.y/u.z, u.x/u.z); 
    }

    public static Matrix4x4 matrixMultiply(Matrix4x4 left, Matrix4x4 right)
    {
        Matrix4x4 result = new Matrix4x4();

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
            {
                float sum = 0;
                for(int k = 0; k < 4; k++)
                    sum += left.m[i][k] * right.m[k][j];
                result.m[i][j] = sum;
            }

        return result;
    }

    public static void printMatrix(Matrix4x4 mat)
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
                System.out.print(mat.m[i][j] + " ");
            System.out.println();
        }
    }
}
