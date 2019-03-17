import java.io.FileNotFoundException;

class Main
{
    public static void main(String[] args)
    {
        Line line = new Line();
        try
        {
            line.wireRender("./Models/african_head.obj", new Point(-line.width/4, 0), 250);
            line.wireRender("./Models/diablo3_pose.obj", new Point(line.width/4, 0), 250f);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("file not found");
        }
    }
}