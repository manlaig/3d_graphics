package src;

import src.*;
import src.Renderer;
import src.Camera.*;
import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;

public abstract class Scene
{
    private Camera camera;
    private Light light;
    private ArrayList<SceneObject> scene_objects;
    private Renderer renderer;

    public Scene(JFrame _window, Camera _camera)
    {
        camera = _camera;
        renderer = new Renderer(_window);
        scene_objects = new ArrayList<>();

        Thread t = new Thread() {
          public void run()
          {
            Update();
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
        for(SceneObject obj : scene_objects)
            if(obj instanceof Mesh && camera instanceof OrthographicCamera)
            {
                Mesh mesh = (Mesh) obj;
                float scale = ((OrthographicCamera)camera).size();
                if(mesh.isLighted && light != null)
                    renderer.renderLightedZBuffer(mesh, scale, light);
                else
                    renderer.wireFrameRender(mesh, scale, Color.black);
            }
    }

    public abstract void Update();
}
