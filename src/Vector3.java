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

    public Vector3 subtract(Vector3 v)
    {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public void normalize()
    {
        float mag = (float) Math.sqrt(x*x + y*y + z*z);
        x = x / mag;
        y = y / mag;
        z = z / mag;
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