package src;

import src.*;
import src.Renderer;
import src.Camera.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Window;
import java.util.ArrayList;

public abstract class Scene
{
    private Camera camera;
    private Light light;
    private ArrayList<SceneObject> scene_objects;
    private Renderer renderer;
    private Window window;

    private int updateDelay = 150; // ms

    public Scene(JFrame _window, Camera _camera)
    {
        window = _window;
        camera = _camera;
        renderer = new Renderer(_window);
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

    public void add(SceneObject obj)
    {
        scene_objects.add(obj);
    }

    private synchronized void redraw()
    {
        window.paint(window.getGraphics());
    }

    public void Render()
    {
        Thread t = new Thread() {
            public void run()
            {
                redraw();
            }
        };
        t.setPriority(2);
        t.start();
        renderer.Render(this);
    }

    public abstract void Update();
}
