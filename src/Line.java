import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Line extends JFrame
{
    int width = 800;
    int height = 800;

    Line()
    {
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void line(Point start, Point end, Color color)
    {
        float dirX = end.x - start.x;
        float dirY = end.y - start.y;

        for(double t = 0; t <= 1; t += 0.05)
        {
            double x = start.x + t * dirX;
            double y = start.y + t * dirY;

            Graphics2D g = (Graphics2D) getGraphics();
            g.translate(width/2, height/2);
            g.rotate(Math.toRadians(180));
            Color original = g.getColor();
            g.setColor(color);

            if(g != null)
                g.drawLine((int) x, (int) y, (int) x, (int) y);
            
            g.setColor(original);
        }
    }

    void circle(Point pos, int radius)
    {
        for(double degree = 0; degree <= 360; degree += 0.1)
        {
            double x = pos.x + Math.cos(degree) * radius;
            double y = pos.y + Math.sin(degree) * radius;

            Graphics g = getGraphics();
            if(g != null)
                g.drawLine((int) x, (int) y, (int) x, (int) y);
        }
    }

    void triangle(Point p1, Point p2, Point p3, Color color)
    {
        line(p1, p2, color);
        line(p2, p3, color);
        line(p3, p1, color);
    }

    void wireFrameRender(String fileName, Point position, float scale) throws FileNotFoundException
    {
        ArrayList<Point> vertices = new ArrayList<>();
        ArrayList<Integer> triangles = new ArrayList<>();
        Scanner sc = new Scanner(new File(fileName));
        
        while(sc.hasNextLine())
        {
            String str = sc.nextLine();
            if(str.length() < 7)
                continue;
            if(str.charAt(0) == 'v' && str.charAt(1) == ' ')
            {
                // vertex
                String[] nums = str.split(" ");
                try
                {
                    float x = Float.parseFloat(nums[1]);
                    float y = Float.parseFloat(nums[2]);
                    float z = Float.parseFloat(nums[3]);

                    Point vertex = new Point(x * scale + position.x,
                                             y * scale + position.y,
                                             z * scale + position.z);
                    vertices.add(vertex);
                } catch(NumberFormatException e) {}
            } else if (str.charAt(0) == 'f')
            {
                // triangle
                String[] points = str.split(" ");
                // points[0] is "f"
                // the triangle data starts from points[1]
                String[] v0 = points[1].split("/");
                String[] v1 = points[2].split("/");
                String[] v2 = points[3].split("/");

                triangles.add(Integer.valueOf(v0[0]));
                triangles.add(Integer.valueOf(v1[0]));
                triangles.add(Integer.valueOf(v2[0]));
            }
        }

        for(int i = 0; i < triangles.size(); i += 3)
        {
            try
            {
                Point p1 = vertices.get(triangles.get(i) - 1);
                Point p2 = vertices.get(triangles.get(i+1) - 1);
                Point p3 = vertices.get(triangles.get(i+2) - 1);
                // drawing the triangle
                triangle(p1, p2, p3, Color.gray);
            } catch(IndexOutOfBoundsException e) {}
        }
    }
}
