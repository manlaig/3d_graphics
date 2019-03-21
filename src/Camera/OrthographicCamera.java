package src.Camera;

import src.*;

public class OrthographicCamera extends Camera
{
    float size;

    public OrthographicCamera(Vector3 pos, float _size)
    {
        super(pos);
        size = _size;
    }

    public float size()
    {
        return size;
    }
}