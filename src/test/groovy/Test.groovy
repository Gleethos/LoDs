public class Test {

    @org.junit.jupiter.api.Test
    public void testMe() {

        var triangleSoize = 3 * 9

        var data = (1..triangleSoize*3).collect({7**it%10-5}) as float[]

        var lod = new LODGenerator(data)

        double[] cost = lod.cost()

        println data
        println cost

    }





}
