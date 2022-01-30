import java.util.Objects;

public class Edge {

    private final Corner.Cost first;
    private final Corner.Cost second;

    public Edge(Corner.Cost first, Corner.Cost second) {
        this.first = first;
        this.second = second;
    }

    public void collapse() {

    }

    public int hashCode() {
        return first.vertex().hashCode() + second.vertex().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(first, edge.first) && Objects.equals(second, edge.second)
                ||
               Objects.equals(first, edge.second) && Objects.equals(second, edge.first);
    }
}
