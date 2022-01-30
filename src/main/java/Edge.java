import java.util.Objects;

public class Edge {

    private final Corner first;
    private final Corner second;

    public Edge( Corner first, Corner second ) {
        this.first = first;
        this.second = second;
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
        return !first.notFlaggedAsCollapsed() && !second.notFlaggedAsCollapsed()
    }

    public int hashCode() {
        return first.getVertex().getPoint().hashCode() + second.getVertex().getPoint().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(first.getVertex().getPoint(), edge.first.getVertex().getPoint()) && Objects.equals(second.getVertex().getPoint(), edge.second.getVertex().getPoint())
                ||
               Objects.equals(first.getVertex().getPoint(), edge.second.getVertex().getPoint()) && Objects.equals(second.getVertex().getPoint(), edge.first.getVertex().getPoint());
    }
}
