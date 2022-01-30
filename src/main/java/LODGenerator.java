import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class LODGenerator {

    Map<P3, Corner> associative = new HashMap<>();

    public LODGenerator(
            float[] data
    ) {
        int vertexSize = 9; // position + normals + color!
        int triangleSize = (vertexSize * 3);

        if ( data.length % triangleSize != 0 )
            throw new IllegalArgumentException(
                "The provided data is not divisible by the expected triangle size!"
            );

        int numberOfTriangles = ( data.length / triangleSize );

        IntStream
            .range( 0, numberOfTriangles )
            .forEach( i -> {
                int ti = i * 18;
                Vertex a = new Vertex(data, ti + 0 * vertexSize);
                Vertex b = new Vertex(data, ti + 1 * vertexSize);
                Vertex c = new Vertex(data, ti + 2 * vertexSize);
                Corner v1 = registered( data, ti + 0 * vertexSize );
                Corner v2 = registered( data, ti + 1 * vertexSize );
                Corner v3 = registered( data, ti + 2 * vertexSize );
                v1.addTriangle( new Triangle( a, b, c ) );
                v2.addTriangle( new Triangle( b, a, c ) );
                v3.addTriangle( new Triangle( c, a, b ) );
            });
    }

    private Corner registered(float[] data, int ti ) {
        Corner v = associative.get( new P3(data, ti) );
        if ( v == null ) {
            v = new Corner();
            associative.put( new P3(data, ti), v );
        }
        return v;
    }

    public Corner.Cost[] cost() {
        Corner[] vertecies = associative.values().toArray(Corner[]::new);
        Corner.Cost[] costs = cost(vertecies);
        return costs;
    }

    private Corner.Cost[] cost(Corner[] vertecies) {
        return
            IntStream
                .range(0,vertecies.length)
                .parallel()
                .mapToObj( i -> new Corner.Cost(vertecies[i], i) )
                .sorted( (a, b) -> (a.cost() > b.cost() ? 1 : 0) )
                .toArray(Corner.Cost[]::new);
    }

}
