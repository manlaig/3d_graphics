package src.Camera;

import src.*;

public abstract class Camera
{
    protected Vector3 position;
    public Matrix4x4 projMatrix;   // projection matrix

    public Camera(Vector3 pos)
    {
        position = pos;
        projMatrix = new Matrix4x4();
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
