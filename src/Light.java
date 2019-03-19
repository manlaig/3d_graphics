package src;

import src.Point;
import src.Common;

public class Light
{
    public Point direction;

    public Light(Point direction)
    {
        // copying rather than sharing an object
        this.direction = new Point(direction.x, direction.y, direction.z);
        Common.normalize(this.direction);
    }

    // get light intensity on a triangle with the given normal
    public float getIntensity(Point normal)
    {
        Common.normalize(normal);
        float intensity = Common.dotProduct(normal, direction);
        return intensity;
    }
}