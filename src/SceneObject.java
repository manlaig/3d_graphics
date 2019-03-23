package src;

import src.*;

public class SceneObject
{
    protected Vector3 position;
    //protected Vector3 rotation;

    protected SceneObject(Vector3 pos)
    {
        position = pos;
    }

    public void setPosition(Vector3 pos)
    {
        position = pos;
    }

    public Vector3 getPosition()
    {
        return position;
    }
}