import java.util.Objects;

public class Vertex {

    private final float[] data;
    private final int position;

    Vertex(
            float[] data, int position
    ) {
        this.data = data;
        this.position = position;
    }

    public float x()  { return this.data[position+0]; }
    public float y()  { return this.data[position+1]; }
    public float z()  { return this.data[position+2]; }
    public float nx() { return this.data[position+3]; }
    public float ny() { return this.data[position+4]; }
    public float nz() { return this.data[position+5]; }
    public float r()  { return this.data[position+6]; }
    public float g()  { return this.data[position+7]; }
    public float b()  { return this.data[position+8]; }

    private float cost( Vertex other ) {
        return (float)
                Math.sqrt(
                        (Math.pow(x()-other.x(),  2) +
                                Math.pow(y()-other.y(),  2) +
                                Math.pow(z()-other.z(),  2) +
                                Math.pow(nx()-other.nx(),2) +
                                Math.pow(ny()-other.ny(),2) +
                                Math.pow(nz()-other.nz(),2) +
                                Math.pow(r()-other.r(),  2) +
                                Math.pow(g()-other.g(),  2) +
                                Math.pow(b()-other.b(),  2)) / 9
                );
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Vertex vertex = (Vertex) o;
        return
                Double.compare(vertex.x(), x()) == 0 &&
                        Double.compare(vertex.y(), y()) == 0 &&
                        Double.compare(vertex.z(), z()) == 0 &&
                        Double.compare(vertex.nx(), nx()) == 0 &&
                        Double.compare(vertex.ny(), ny()) == 0 &&
                        Double.compare(vertex.nz(), nz()) == 0 &&
                        Double.compare(vertex.r(), r()) == 0 &&
                        Double.compare(vertex.g(), g()) == 0 &&
                        Double.compare(vertex.b(), b()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x(), y(), z(), nx(), ny(), nz(), r(), g(), b());
    }

}
