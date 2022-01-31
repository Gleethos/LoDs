public class Test {

    @org.junit.jupiter.api.Test
    public void testMe() {

        var triangleSoize = 3 * 9

        var data = (1..triangleSoize*10).collect({7**it%10-5}) as float[]

        var lod = new LODGenerator(data, 0.3)
        var lod2 = new LODGenerator(data, 0.1)

        println lod.result

        println data.length
        println lod.result.length / data.length
        println lod2.result.length / data.length

    }





}
