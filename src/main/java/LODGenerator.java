import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LODGenerator {

    private final static int VERTEX_SIZE = 9; // position + normals + color!
    private final static int TRIANGLE_SIZE = (VERTEX_SIZE * 3);
    private final float[] result;

    public LODGenerator(
            float[] sourceData, double cull
    ) {
        float[] data = sourceData.clone();

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
                int ti = i * TRIANGLE_SIZE;
                Corner c1 = registered( data, ti + 0 * VERTEX_SIZE, associative );
                Corner c2 = registered( data, ti + 1 * VERTEX_SIZE, associative );
                Corner c3 = registered( data, ti + 2 * VERTEX_SIZE, associative );
                Vertex v1 = new Vertex( data, ti + 0 * VERTEX_SIZE );
                Vertex v2 = new Vertex( data, ti + 1 * VERTEX_SIZE );
                Vertex v3 = new Vertex( data, ti + 2 * VERTEX_SIZE );
                c1.addNeighbour( c2, c3 );
                c2.addNeighbour( c1, c3 );
                c3.addNeighbour( c1, c2 );
                c1.addTriangle( new Triangle( v1, v2, v3 ) );
                c2.addTriangle( new Triangle( v2, v1, v3 ) );
                c3.addTriangle( new Triangle( v3, v1, v2 ) );
                edges.add(new Edge( c2, c3 ));
                edges.add(new Edge( c1, c3 ));
                edges.add(new Edge( c1, c2 ));
            });

        int[] i = {0};
        int limit = (int) (edges.size() * cull);

        List<Edge> sortedList = new ArrayList<>();
        edges.stream()
                .parallel()
                .sorted( (a, b) -> (a.cost() > b.cost() ? 1 : 0) )
                .sequential()
                .forEach( e -> {
                    sortedList.add(e);
                    if ( i[0] < limit && !e.willBeCollapsed() ) {
                        e.flagAsCollapsable();
                        i[0]++;
                    }
                });

        Set<Vertex> resultSet = new HashSet<>();
        sortedList.stream().parallel().forEach( e -> {
            if ( e.willBeCollapsed() ) e.collapse();
        });

        resultSet.addAll(
                associative
                        .values()
                        .stream()
                        .parallel()
                        .flatMap( c -> c.getVertices().stream() )
                        .collect(Collectors.toList())
        );

        List<Vertex> vertecies = new ArrayList<>(resultSet);

        this.result = new float[resultSet.size() * VERTEX_SIZE];

        IntStream.range(0, vertecies.size())
                .parallel()
                .forEach( vi -> {
                    int di = vi * VERTEX_SIZE;
                    Vertex v = vertecies.get(vi);
                    result[di + 0] = v.x();
                    result[di + 1] = v.y();
                    result[di + 2] = v.z();
                    result[di + 3] = v.nx();
                    result[di + 4] = v.ny();
                    result[di + 5] = v.nz();
                    result[di + 6] = v.r();
                    result[di + 7] = v.g();
                    result[di + 8] = v.b();
                });
    }

    private static Corner registered(
            float[] data, int ti, Map<P3, Corner> associative
    ) {
        Corner v = associative.get( new P3(data, ti) );
        if ( v == null ) {
            v = new Corner( new P3(data, ti) );
            associative.put( new P3(data, ti), v );
        }
        return v;
    }

    public float[] getResult() {
        return result;
    }

}
