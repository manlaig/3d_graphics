package src;

import src.Point;

public class Light
{
    public Point origin, direction;

    public Light(Point origin, Point direction)
    {
        // copying rather than sharing an object
        this.origin = new Point(origin.x, origin.y, origin.z);
        this.direction = new Point(direction.x, direction.y, direction.z);
    }
}