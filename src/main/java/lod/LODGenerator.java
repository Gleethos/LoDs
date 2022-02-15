package lod;

public class LODGenerator {

    private final float[] result;


    public LODGenerator(
            float[] vertices, float[] colors, float[] normals, double cull
    ) {
        this.result = Internal.generate(vertices, colors, normals, cull);
    }

    public LODGenerator(
            float[] sourceData, double cull
    ) {
        this.result = Internal.generate(sourceData, cull);
    }

    public float[] getResult() {
        return result;
    }

}
