package src;

import src.*;

public class SceneObject
{
    public Matrix4x4 transform;
    private float scale;

    protected SceneObject(Vector3 pos, float _scale)
    {
        transform = new Matrix4x4(pos);
        scale = _scale;
    }

    public void setPosition(Vector3 pos)
    {
        transform.setPosition(pos);
    }

    public Vector3 getPosition()
    {
        return transform.getPosition();
    }

    public float getScale()
    {
        return scale;
    }
}