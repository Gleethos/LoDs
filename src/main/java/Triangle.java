public class Triangle {

    private final Vertex main, second, third;

    Triangle(Vertex main, Vertex second, Vertex third) {
        this.main = main; this.second = second; this.third = third;
    }

    public Vertex main() { return main; }
    public Vertex second() { return second; }
    public Vertex third() { return third; }

}
