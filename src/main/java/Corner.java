import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Corner {

    private final Vertex vertex;
    private final List<Corner> neighbours = new ArrayList<>();

    private double cost = Double.NaN;
    private boolean toBeCollapsed = false;

    public Corner( Vertex vertex ) { this.vertex = vertex; }

    public void addNeighbour( Corner... corners ) {
        this.neighbours.addAll(Arrays.asList(corners));
    }

    public List<Edge> getEdges() {
        return this.neighbours
                    .stream()
                    .map( n -> new Edge( this, n ) )
                    .collect(Collectors.toList());
    }

    public boolean notFlaggedAsCollapsed() { return !this.toBeCollapsed; }

    public void flagAsCollapsable() { this.toBeCollapsed = true; }

    private void _calculateCost() {
        if ( this.cost != Double.NaN ) return;
        double edginess = _localCost( p -> new V3( p.nx(), p.nx(), p.nz() ) );
        double colorful = _localCost( p -> new V3( p.r(), p.g(), p.b() ).minus(.5f) );
        this.cost = (edginess * colorful);
    }

    private double _localCost( Function<Vertex, V3> selector )
    {
        List<V3> normals = neighbours.stream()
                                    .map( n -> n.vertex)
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

    public double getCost() {
        if ( this.cost == Double.NaN ) this._calculateCost();
        return this.cost;
    }

    public Vertex getVertex() {
        return vertex;
    }


    public int hashCode() {
        Vertex p = this.vertex;
        return Objects.hash( p.x(), p.y(), p.z(), p.nx(), p.ny(), p.nz() );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corner corner = (Corner) o;
        Vertex a = corner.vertex;
        Vertex b = this.vertex;
        return new V3(a.x(), a.y(), a.z()).equals(new V3(b.x(), b.y(), b.z())) &&
               new V3(a.nx(), a.ny(), a.nz()).equals(new V3(b.nx(), b.ny(), b.nz()));
    }

}
