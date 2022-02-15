package lod;

import java.util.stream.IntStream;

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

    public float[] getResultOnlyVertecies() {

        int numberOfVertices = this.result.length / Internal.VERTEX_SIZE;

        float[] out = new float[numberOfVertices * 3];

        IntStream.range(0,numberOfVertices)
                .parallel()
                .forEach( i -> {
                    int ri = i * Internal.VERTEX_SIZE;
                    int oi = i * 3;
                    out[ oi + 0 ] = this.result[ ri + 0 ];
                    out[ oi + 1 ] = this.result[ ri + 1 ];
                    out[ oi + 2 ] = this.result[ ri + 2 ];
                });

        return out;
    }

}
