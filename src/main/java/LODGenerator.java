import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LODGenerator {

    private final static int VERTEX_SIZE = 9; // position + normals + color!
    private final static int TRIANGLE_SIZE = (VERTEX_SIZE * 3);

    public LODGenerator(
            float[] data
    ) {

        if ( data.length % TRIANGLE_SIZE != 0 )
            throw new IllegalArgumentException(
                "The provided data is not divisible by the expected triangle size!"
            );

        int numberOfTriangles = ( data.length / TRIANGLE_SIZE);

        Map<P3, Corner> associative = new HashMap<>();
        Set<Edge> edges = new HashSet<>();

        IntStream
            .range( 0, numberOfTriangles )
            .forEach( i -> {
                int ti = i * 18;
                Corner v1 = registered( data, ti + 0 * VERTEX_SIZE, associative );
                Corner v2 = registered( data, ti + 1 * VERTEX_SIZE, associative );
                Corner v3 = registered( data, ti + 2 * VERTEX_SIZE, associative );
                v1.addNeighbour( v2, v3 );
                v2.addNeighbour( v1, v3 );
                v3.addNeighbour( v1, v2 );
                edges.add(new Edge( v2, v3 ));
                edges.add(new Edge( v1, v3 ));
                edges.add(new Edge( v1, v2 ));
            });

        double cull = 0.3;
        int[] i = {0};
        int limit = (int) (edges.size() * cull);

        edges.stream()
                .parallel()
                .sorted( (a, b) -> (a.cost() > b.cost() ? 1 : 0) )
                .sequential()
                .forEach( e -> {
                    if ( i[0] < limit && !e.willBeCollapsed() ) {
                        e.flagAsCollapsable();
                        i[0]++;
                    }
                } );



    }

    private static Corner registered(
            float[] data, int ti, Map<P3, Corner> associative
    ) {
        Corner v = associative.get( new P3(data, ti) );
        if ( v == null ) {
            v = new Corner(new Vertex(data, ti));
            associative.put( new P3(data, ti), v );
        }
        return v;
    }


}
