import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import lod.LODGenerator
import render.LoadModelsTest
import render.VertexTest

import javax.swing.JFrame

public class Test {

    @org.junit.jupiter.api.Test
    public void testMe() {

        var triangleSoize = 3 * 9

        var data = (1..triangleSoize*10).collect({7**it%10-5}) as float[]

        var lod = new LODGenerator(data, 0.3)
        var lod2 = new LODGenerator(data, 0.1)

        println lod.result

        println data.length
        println lod.result.length / data.length
        println lod2.result.length / data.length

    }


    @org.junit.jupiter.api.Test
    void testMe2(){
        var frame = new JFrame("Test")
        var lgdxPlot = new LoadModelsTest()
        var applicationConfig = new LwjglApplicationConfiguration();
        applicationConfig.title = "libgdx-canvas-prototype";
        applicationConfig.r = 8;
        applicationConfig.g = 8;
        applicationConfig.b = 8;
        applicationConfig.a = 8;
        applicationConfig.depth = 16;
        applicationConfig.stencil = 0;
        applicationConfig.samples = 4;
        applicationConfig.foregroundFPS = 60;

        var canvas = new LwjglAWTCanvas(lgdxPlot, applicationConfig);
        //canvas.getCanvas().addMouseListener(lgdxPlot);
        //canvas.getCanvas().addMouseMotionListener(lgdxPlot);
        //canvas.getCanvas().addMouseWheelListener(lgdxPlot);
        //canvas.getCanvas().addComponentListener(lgdxPlot);
        frame.add(canvas.getCanvas())
        frame.setSize(1000, 1000)
        frame.setVisible(true)
        Thread.sleep(100000)
    }


    @org.junit.jupiter.api.Test
    void testMe3(){
        var frame = new JFrame("Test")
        var lgdxPlot = new VertexTest()
        var applicationConfig = new LwjglApplicationConfiguration();
        applicationConfig.title = "libgdx-canvas-prototype";
        applicationConfig.r = 8;
        applicationConfig.g = 8;
        applicationConfig.b = 8;
        applicationConfig.a = 8;
        applicationConfig.depth = 16;
        applicationConfig.stencil = 0;
        applicationConfig.samples = 4;
        applicationConfig.foregroundFPS = 60;

        var canvas = new LwjglAWTCanvas(lgdxPlot, applicationConfig);
        //canvas.getCanvas().addMouseListener(lgdxPlot);
        //canvas.getCanvas().addMouseMotionListener(lgdxPlot);
        //canvas.getCanvas().addMouseWheelListener(lgdxPlot);
        //canvas.getCanvas().addComponentListener(lgdxPlot);
        frame.add(canvas.getCanvas())
        frame.setSize(1000, 1000)
        frame.setVisible(true)
        Thread.sleep(100000)
    }

}
