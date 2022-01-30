import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Corner {

    private final List<Triangle> triangles = new ArrayList<>();

    public Corner() {}

    public void addTriangle(Triangle t) {
        triangles.add(t);
    }

    public double cost() {
        double edginess = _localCost( p -> new V3( p.nx(), p.nx(), p.nz() ) );
        double colorful = _localCost( p -> new V3( p.r(), p.g(), p.b() ).minus(.5f) );
        return (edginess * colorful);
    }

    private double _localCost( Function<Vertex, V3> selector )
    {
        List<V3> normals = triangles.stream()
                .map(Triangle::main)
                .map( selector )
                .collect(Collectors.toList());
        double max = normals.stream()
                .mapToDouble(V3::length)
                .max()
                .orElse(1);
        V3 sum =
                normals.stream()
                        .reduce(V3::plus)
                        .get();

        V3 avg = sum.divide(normals.size());

        return avg.length() / max;
    }

    public int hashCode() {
        Vertex p = this.triangles.get(0).main();
        return Objects.hash( p.x(), p.y(), p.z(), p.nx(), p.ny(), p.nz() );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corner corner = (Corner) o;
        Vertex a = corner.triangles.get(0).main();
        Vertex b = this.triangles.get(0).main();
        return new V3(a.x(), a.y(), a.z()).equals(new V3(b.x(), b.y(), b.z())) &&
               new V3(a.nx(), a.ny(), a.nz()).equals(new V3(b.nx(), b.ny(), b.nz()));
    }

    public static class Cost {

        private final double cost;
        private final int position;
        private final Corner corner;

        public Cost(Corner v, int p) {
            this.cost = v.cost();
            this.corner = v;
            this.position = p;
        }

        public double cost() {
            return cost;
        }

        public Corner vertex() {
            return corner;
        }

        public int position() {
            return position;
        }

    }

}
