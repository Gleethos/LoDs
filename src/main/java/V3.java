public class V3 {

    private final float vx, vy, vz;

    V3(float vx, float vy, float vz) {
        this.vx = vx; this.vy = vy; this.vz = vz;
    }

    public V3 minus(V3 other) {
        return new V3( vx - other.vx, vx - other.vy, vz - other.vz );
    }

    public V3 minus(float other) {
        return new V3( vx - other, vx - other, vz - other );
    }

    public V3 plus(V3 other) {
        return new V3( vx + other.vx, vx + other.vy, vz + other.vz );
    }

    public V3 divide(int divider) {
        return new V3(vx/divider, vy/divider, vz/divider);
    }

    public float multiply( V3 other ) {
        return ( vx * other.vx + vy * other.vy + vz * other.vz );
    }

    public float length() {
        return (float) Math.pow(
                (Math.pow(vx,2)+Math.pow(vy,2)+Math.pow(vz,2))/3,
                0.5
        );
    }

}
