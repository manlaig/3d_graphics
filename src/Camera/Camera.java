package src.Camera;

import src.*;

public abstract class Camera
{
    protected Vector3 position, rotation;

    public Camera(Vector3 pos)
    {
        position = pos;
    }
}
