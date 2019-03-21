package src;

import src.*;

public class Light
{
    public Vector3 direction;

    public Light(Vector3 direction)
    {
        // copying rather than sharing an object
        this.direction = new Vector3(direction.x, direction.y, direction.z);
        Common.normalize(this.direction);
    }

    // get light intensity on a triangle with the given normal
    public float getIntensity(Vector3 normal)
    {
        Common.normalize(normal);
        float intensity = Common.dotProduct(normal, direction);
        return intensity;
    }
}