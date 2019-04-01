package src;

import src.*;

public final class Matrix4x4
{
    public float[][] m;

    public Matrix4x4()
    {
        m = new float[4][4];
        m[0][0] = 1;
        m[1][1] = 1;
        m[2][2] = 1;
        m[3][3] = 1;
    }

    public Matrix4x4(Vector3 pos)
    {
        this();
        setPosition(pos);
    }

    public void setPosition(Vector3 pos)
    {
        m[0][3] = pos.x;
        m[1][3] = pos.y;
        m[2][3] = pos.z;
    }

    public Vector3 getPosition()
    {
        return new Vector3(m[0][3], m[1][3], m[2][3]);
    }

    public void scale(float scale)
    {
        m[0][0] *= scale;
        m[1][1] *= scale;
        m[2][2] *= scale;
    }

    // applied the transformation on this.m
    public Matrix4x4 apply(Matrix4x4 transformation)
    {
        Matrix4x4 result = Common.matrixMultiply(transformation, this);
        m = result.m;
        // returning this to allow chaining
        return this;
    }
}
