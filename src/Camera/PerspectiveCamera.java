package src.Camera;

import src.*;

public class PerspectiveCamera extends Camera
{
    public PerspectiveCamera(Vector3 pos, float distanceToProjPlane) throws IllegalArgumentException
    {
        super(pos);
        if(distanceToProjPlane == 0)
            throw new IllegalArgumentException("Distance cannot be 0 (division by zero)");
            
        // dividing by distance from center of projection to the projection plane
        projMatrix.m[3][2] = 1/distanceToProjPlane;
    }
}
