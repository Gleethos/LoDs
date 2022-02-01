package render;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LoadModelsTest implements ApplicationListener
{
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public List<ModelInstance> instances = new ArrayList<>();
    public Environment environment;
    public boolean loading;

    @Override
    public void create () {
        modelBatch = new ModelBatch(new DefaultShaderProvider() {
            @Override
            protected Shader createShader(Renderable renderable) {
                // Returning a Shader that only renders Lines instead of Triangles
                return new WireframeShader(renderable, config);
            }
        });//new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(1f, 1f, 1f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assets = new AssetManager();
        assets.load("mouse/teapot.obj", Model.class);
        loading = true;
    }

    private void doneLoading() {
        Model ship = assets.get("mouse/teapot.obj", Model.class);
        ship.materials.add(new Material(ColorAttribute.createSpecular(Color.GREEN)));
        ship.materials.add(new Material(ColorAttribute.createDiffuse(Color.GRAY)));
        ModelInstance shipInstance = new ModelInstance(ship);
        instances.add(shipInstance);
        modify(ship.meshes.get(0));
        loading = false;
    }

    @Override
    public void render () {
        if (loading && assets.update())
            doneLoading();
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    private void modify(Mesh mesh) {
        int vertexSize = mesh.getVertexSize();
        //this is how to get the properly sized mesh:
        float[] vertices = new float[mesh.getNumVertices() * vertexSize / 4];
        mesh.getVertices(vertices);

        int numberOfTriangles = vertices.length / vertexSize;

        for (int i = 0; i < vertices.length; i += vertexSize){
            vertices[i+0] *= 1f; // x
            vertices[i+1] *= 1f; // y
            vertices[i+2] *= 1f; // z

            vertices[i+3] *= 1f;
            vertices[i+4] *= 1f;
            vertices[i+5] *= 1f;

            vertices[i+6] *= 1f;
            vertices[i+7] *= 1f;
            vertices[i+8] *= 1f;

            vertices[i+9] *= 1f;
            vertices[i+10] *= 1f;
            vertices[i+11] *= 1f;
        }

        //vertices = new LODGenerator(vertices, 0.3).getResult();

        mesh.setVertices(vertices);
    }

}
