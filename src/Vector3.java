package src;

public final class Vector3
{
    public float x, y, z;

    public Vector3(float xx, float yy, float zz)
    {
        x = xx;
        y = yy;
        z = zz;
    }
    public Vector3(float xx, float yy)
    {
        x = xx;
        y = yy;
        z = 0;
    }

    public Vector3(Vector3 v)
    {
        this(v.x, v.y, v.z);   
    }

    public Vector3 mult(float v)
    {
        x *= v;
        y *= v;
        z *= v;
        return this;
    }

    public Vector3 add(Vector3 v)
    {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append(x);
        str.append(" ");
        str.append(y);
        str.append(" ");
        str.append(z);
        return str.toString();
    }
}