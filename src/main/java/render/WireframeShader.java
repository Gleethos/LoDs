package render;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * This class is used as an alternative shader for the {@link com.badlogic.gdx.graphics.g3d.ModelBatch}.
 * Therefore, it extends the {@link DefaultShader} and overrides both {@link DefaultShader#render(Renderable)}
 * and {@link DefaultShader#render(Renderable, Attributes)} functions. The key difference is the usage of
 * {@link GL20#GL_LINES} as the primitive type instead of the default {@link GL20#GL_TRIANGLES}.
 */
public class WireframeShader extends DefaultShader {

    public static final int PRIMITIVE_TYPE = GL20.GL_LINES;
    // Just in case the primitive needs to be
    // restored and is not the default primitive
    private int mSavedPrimitiveType;

    public WireframeShader(Renderable renderable) {
        super(renderable);
    }

    public WireframeShader(Renderable renderable, Config config) {
        super(renderable, config);
    }

    public WireframeShader(Renderable renderable, Config config, String prefix) {
        super(renderable, config, prefix);
    }

    public WireframeShader(Renderable renderable, Config config, String prefix, String vertexShader, String fragmentShader) {
        super(renderable, config, prefix, vertexShader, fragmentShader);
    }

    public WireframeShader(Renderable renderable, Config config, ShaderProgram shaderProgram) {
        super(renderable, config, shaderProgram);
    }

    @Override
    public void render(Renderable renderable) {
        setPrimitiveType(renderable);
        super.render(renderable);
        restorePrimitiveType(renderable);
    }

    @Override
    public void render(Renderable renderable, Attributes combinedAttributes) {
        setPrimitiveType(renderable);
        super.render(renderable, combinedAttributes);
        restorePrimitiveType(renderable);
    }

    private void restorePrimitiveType(Renderable renderable) {
        renderable.meshPart.primitiveType = mSavedPrimitiveType;

    }

    private void setPrimitiveType(Renderable renderable) {
        mSavedPrimitiveType = renderable.meshPart.primitiveType;
        renderable.meshPart.primitiveType = PRIMITIVE_TYPE;

    }
}
