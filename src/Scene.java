package src;

import src.*;
import src.Camera.*;
import javax.swing.JFrame;
import java.awt.Color;
import java.util.ArrayList;

public abstract class Scene
{
    private Camera camera;
    private Light light;
    private ArrayList<SceneObject> scene_objects;
    private Renderer renderer;
    private JFrame window;

    public int updateDelay = 100; // ms
    public Color backgroundColor = Color.black;

    public Scene(JFrame _window, Camera _camera)
    {
        window = _window;
        window.createBufferStrategy(4);
        camera = _camera;
        renderer = new Renderer(window);
        scene_objects = new ArrayList<>();

        Thread t = new Thread() {
          public void run()
          {
            while(true)
            {
                Thread t = new Thread() {
                    public void run()
                    {
                        Update();
                        Render();
                    }
                };
                t.setPriority(1);
                t.start();

                long timeStart = System.currentTimeMillis();
                while(System.currentTimeMillis() - timeStart < updateDelay);
            }
          }
        };
        t.start();
    }

    public Scene(JFrame _window, Camera _camera, Light _light)
    {
        this(_window, _camera);
        light = _light;
    }

    public void add(SceneObject obj)
    {
        scene_objects.add(obj);
    }

    public void Render()
    {
        renderer.Render(this);
    }

    public abstract void Update();

    public Camera getCamera()
    {
        return camera;
    }

    public Light getLight()
    {
        return light;
    }

    public ArrayList<SceneObject> getObjects()
    {
        return scene_objects;
    }
}
