package render;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Random;

public class VertexTest implements ApplicationListener {

	private ShaderProgram shaderProgram;
	private Mesh mesh;
	private Random rnd = new Random(0);

	@Override
	public void create() {
		String vertexShader = "attribute vec4 a_position;    \n" +
								"attribute vec4 a_color;\n" +
								"varying vec4 v_color;" +
								"void main()                  \n" +
								"{                            \n" +
								"   v_color = a_color; \n" +
								"   gl_Position =  a_position;  \n" +
								"}                            \n";
		String fragmentShader = "#ifdef GL_ES\n" +
								"precision mediump float;\n" +
								"#endif\n" +
								"varying vec4 v_color;\n" +
								"void main()                                  \n" +
								"{                                            \n" +
								"  gl_FragColor = v_color;\n" +
								"}";

		shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
	}

	private void generateMesh() {
		if (mesh != null)
			mesh.dispose();

		int numberOfRectangles = 10000;

		int numberOfVertices = 4 * numberOfRectangles;
		mesh = new Mesh(true, numberOfVertices, numberOfRectangles * 6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked());

		int vertexPositionValue = 3;
		int vertexColorValue = 4;

		int valuesPerVertex = vertexPositionValue + vertexColorValue;

		short[] vertexIndices = new short[numberOfRectangles * 6];
		float[] verticesWithColor = new float[numberOfVertices * valuesPerVertex];

		for (int i = 0; i < numberOfRectangles; i++) {
			float colorR = generateNumberBetweenZeroAndOne(rnd);
			float colorG = generateNumberBetweenZeroAndOne(rnd);
			float colorB = generateNumberBetweenZeroAndOne(rnd);

			float x = generateNumberBetweenNegativeOneAndPointNine(rnd);
			float y = generateNumberBetweenNegativeOneAndPointNine(rnd);

			int rectangleOffsetInArray = i * valuesPerVertex * 4;

			setValuesInArrayForVertex(verticesWithColor, colorR, colorG, colorB, x, y, rectangleOffsetInArray, 0);
			setValuesInArrayForVertex(verticesWithColor, colorR*0.7f, colorG, colorB, x + 0.1f, y, rectangleOffsetInArray, 1);
			setValuesInArrayForVertex(verticesWithColor, colorR, colorG, colorB*0.1f, x + 0.1f, y + 0.1f, rectangleOffsetInArray, 2);
			setValuesInArrayForVertex(verticesWithColor, colorR*0.5f, colorG, colorB, x, y + 0.1f, rectangleOffsetInArray, 3);

			vertexIndices[i * 6 + 0] = (short) (i * 4 + 0);
			vertexIndices[i * 6 + 1] = (short) (i * 4 + 1);
			vertexIndices[i * 6 + 2] = (short) (i * 4 + 2);
			vertexIndices[i * 6 + 3] = (short) (i * 4 + 2);
			vertexIndices[i * 6 + 4] = (short) (i * 4 + 3);
			vertexIndices[i * 6 + 5] = (short) (i * 4 + 0);
		}
		mesh.setVertices(verticesWithColor);
		mesh.setIndices(vertexIndices);
	}

	private void setValuesInArrayForVertex(
			float[] verticesWithColor,
			float colorR, float colorG, float colorB,
			float x, float y,
			int rectangleOffsetInArray,
			int vertexNumberInRect
	) {
		int vertexOffsetInArray = rectangleOffsetInArray + vertexNumberInRect * 7;
		// x position
		verticesWithColor[vertexOffsetInArray + 0] = x;
		// y position
		verticesWithColor[vertexOffsetInArray + 1] = y;
		// z position (screen coordinates)
		verticesWithColor[vertexOffsetInArray + 2] = (y+x)/2;
		// red value
		verticesWithColor[vertexOffsetInArray + 3] = colorR;
		// green value
		verticesWithColor[vertexOffsetInArray + 4] = colorG;
		// blue value
		verticesWithColor[vertexOffsetInArray + 5] = colorB;
		// alpha value
		verticesWithColor[vertexOffsetInArray + 6] = 0.2f;
	}

	private float generateNumberBetweenNegativeOneAndPointNine(Random rnd) {
		return rnd.nextFloat() * 1.9f - 1f;
	}

	private float generateNumberBetweenZeroAndOne(Random rnd) {
		return rnd.nextFloat();
	}

	boolean generated = false;

	@Override
	public void render() {
		long start = System.currentTimeMillis();

		if ( !generated ) generateMesh();
		generated = true;

		Gdx.gl.glClearColor(0, 0, 0, 1);
		// Clearing the buffer
		Gdx.gl.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
		Gdx.gl.glDepthFunc(GL20.GL_LESS);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0 ));

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shaderProgram.begin();
		mesh.render(shaderProgram, GL20.GL_TRIANGLES);
		shaderProgram.end();

		System.out.println("render() took: " + (System.currentTimeMillis() - start) + "ms");
	}

	@Override
	public void dispose() {
		mesh.dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

	}

}
