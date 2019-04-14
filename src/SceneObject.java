package src;

import src.*;

public class SceneObject
{
    public Matrix4x4 transform;

    protected SceneObject(Vector3 pos)
    {
        transform = new Matrix4x4(pos);
    }

    public void setPosition(Vector3 pos)
    {
        transform.setPosition(pos);
    }

    public Vector3 getPosition()
    {
        return transform.getPosition();
    }
}