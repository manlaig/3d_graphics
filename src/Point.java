package src;

public class Point
{
    public float x, y, z;

    public Point(float xx, float yy, float zz)
    {
        x = xx;
        y = yy;
        z = zz;
    }
    public Point(float xx, float yy)
    {
        x = xx;
        y = yy;
        z = 0;
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