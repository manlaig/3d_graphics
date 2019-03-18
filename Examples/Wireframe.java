package examples;

import src.Line;
import src.Point;
import java.io.FileNotFoundException;

public class Wireframe
{
    public static void main(String[] args)
    {
        Line line = new Line();
        try
        {
            line.wireFrameRender("./Models/african_head.obj", new Point(-line.width/4, 0), 250);
            line.wireFrameRender("./Models/diablo3_pose.obj", new Point(line.width/4, 0), 250);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("file not found");
        }
    }
}
