package lod;

import java.util.Objects;

public class P3 {

    private final float[] data;
    private final int position;

    P3( float[] data, int position ) {
        this.data = data;
        this.position = position;
    }

    public float x()  { return this.data[position+0]; }
    public float y()  { return this.data[position+1]; }
    public float z()  { return this.data[position+2]; }
    public float nx() { return this.data[position+3]; }
    public float ny() { return this.data[position+4]; }
    public float nz() { return this.data[position+5]; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        P3 p3 = (P3) o;
        return Float.compare(p3.x(), x()) == 0 && Float.compare(p3.y(), y()) == 0 && Float.compare(p3.z(), z()) == 0 && Float.compare(p3.nx(), nx()) == 0 && Float.compare(p3.ny(), ny()) == 0 && Float.compare(p3.nz(), nz()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x(), y(), z(), nx(), ny(), nz());
    }
}
