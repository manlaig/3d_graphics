package src;

import src.*;

public abstract class Common
{
    public static Point crossProduct(Point p1, Point p2)
    {
        float x = p1.y*p2.z - p2.y*p1.z;
        float y = p1.x*p2.z - p2.x*p1.z;
        float z = p1.x*p2.y - p2.x*p1.y;

        return new Point(x, y, z);
    }

    public static float dotProduct(Point p1, Point p2)
    {
        return p1.x*p2.x + p1.y*p2.y + p1.z*p2.z;
    }

    public static Point vectorFromPoint(Point from, Point to)
    {
        float x = to.x - from.x;
        float y = to.y - from.y;
        float z = to.z - from.z;
        return new Point(x, y, z);
    }

    public static void normalize(Point p)
    {
        float mag = (float) Math.sqrt(p.x*p.x + p.y*p.y + p.z*p.z);
        p.x = p.x / mag;
        p.y = p.y / mag;
        p.z = p.z / mag;
    }

    // you need a wrapper to swapper class to swap objects
    // so we can't make a generic swap function
    public static void swapPoints(Point a, Point b)
    {
        Point t = new Point(a.x, a.y, a.z);
        a.x = b.x;  a.y = b.y;  a.z = b.z;
        b.x = t.x;  b.y = t.y;  b.z = t.z;
    }
}