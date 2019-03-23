package src.Camera;

import src.*;

public abstract class Camera
{
    protected Vector3 position;

    public Camera(Vector3 pos)
    {
        position = pos;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 pos)
    {
        position = pos;
    }
}
