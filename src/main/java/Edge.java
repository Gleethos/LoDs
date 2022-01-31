import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Edge {

    private final Corner first;
    private final Corner second;
    private Runnable collaps = ()->{};

    public Edge( Corner first, Corner second ) {
        this.first = first;
        this.second = second;
    }

    public List<Triangle> getTriangles() {
        List<Triangle> triangles = first.getTriangles();
        triangles.addAll( second.getTriangles() );
        if ( this.willBeCollapsed() ) {
            List<Triangle> removable =
                            triangles.stream()
                                    .filter( t ->
                                            getFirst().equals(t.main()) && getFirst().equals(t.second())
                                            ||
                                            getFirst().equals(t.main()) && getFirst().equals(t.third())
                                            ||
                                            getFirst().equals(t.second()) && getFirst().equals(t.main())
                                            ||
                                            getFirst().equals(t.second()) && getFirst().equals(t.third())
                                            ||
                                            getFirst().equals(t.third()) && getFirst().equals(t.main())
                                            ||
                                            getFirst().equals(t.third()) && getFirst().equals(t.second())
                                    )
                                    .collect(Collectors.toList());
            // TODO: Create modifies triangles!
            triangles.removeAll(removable);
        }

        return triangles;
    }

    //TODO: Create method that modifies vertex data!

    public P3 getFirst() {
        return first.getPoint();
    }

    public P3 getSecond() {
        return second.getPoint();
    }

    public double cost() {
        return (this.first.getCost() + this.second.getCost())/2;
    }

    public void flagAsCollapsable() {
        if ( first.notFlaggedAsCollapsed() && second.notFlaggedAsCollapsed()) {
            first.flagAsCollapsable();
            second.flagAsCollapsable();
        }
    }

    public boolean willBeCollapsed() {
        return !first.notFlaggedAsCollapsed() && !second.notFlaggedAsCollapsed();
    }

    public void collapse() {
        Vertex a1 = first.getVertices().get(0);
        Vertex b1 = second.getVertices().get(0);
        float  x = (a1. x()+b1. x())/2;
        float  y = (a1. y()+b1. y())/2;
        float  z = (a1. z()+b1. z())/2;
        float nx = (a1.nx()+b1.nx())/2;
        float ny = (a1.ny()+b1.ny())/2;
        float nz = (a1.nz()+b1.nz())/2;
        float  r = (a1. r()+b1. r())/2;
        float  g = (a1. g()+b1. g())/2;
        float  b = (a1. b()+b1. b())/2;
        a1.set(
                x, y, z,
                nx, ny, nz,
                r, g, b
        );
        b1.set(
                x, y, z,
                nx, ny, nz,
                r, g, b
        );
    }

    public int sizeNeeded() {
        if ( this.willBeCollapsed() ) {
            return first.numberOfNeighbours() + second.numberOfNeighbours() - 2;
        }
        else
            return first.numberOfNeighbours() + second.numberOfNeighbours();
    }

    public int hashCode() {
        return first.getPoint().hashCode() + second.getPoint().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(first.getPoint(), edge.first.getPoint()) && Objects.equals(second.getPoint(), edge.second.getPoint())
                ||
               Objects.equals(first.getPoint(), edge.second.getPoint()) && Objects.equals(second.getPoint(), edge.first.getPoint());
    }
}
