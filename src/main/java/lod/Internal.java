package lod;

import com.badlogic.gdx.math.Vector3;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Internal {

	private final static int VERTEX_SIZE   = 9; // position + normals + color!
	private final static int TRIANGLE_SIZE = (VERTEX_SIZE * 3);


	public static float[] generate(
			float[] vertices, float[] colors, float[] normals, double cull
	) {
		if ( vertices == null )
			throw new IllegalArgumentException("Vertex coordinates cannot be null!");

		final int NUMBER_OF_TRIANGLES = (vertices.length / 3);

		float[] data = new float[NUMBER_OF_TRIANGLES * VERTEX_SIZE];

		IntStream.range(0, NUMBER_OF_TRIANGLES)
				.parallel()
				.forEach( ti -> {
					int vi = ti * 3;
					int di = ti * VERTEX_SIZE;
					Vector3 n = normal(vertices, vi, normals, vi);
					Vector3 c = color(colors, vi);
					data[ di + 0 ] = vertices[vi + 0];
					data[ di + 1 ] = vertices[vi + 1];
					data[ di + 2 ] = vertices[vi + 2];
					data[ di + 3 ] = n.x;
					data[ di + 4 ] = n.y;
					data[ di + 5 ] = n.z;
					data[ di + 6 ] = c.x;
					data[ di + 7 ] = c.y;
					data[ di + 8 ] = c.z;
					data[ di + 9 ] = vertices[vi + 3];
					data[ di + 10] = vertices[vi + 4];
					data[ di + 11] = vertices[vi + 5];
					data[ di + 12] = n.x;
					data[ di + 13] = n.y;
					data[ di + 14] = n.z;
					data[ di + 15] = c.x;
					data[ di + 16] = c.y;
					data[ di + 17] = c.z;
					data[ di + 18] = vertices[vi + 6];
					data[ di + 19] = vertices[vi + 7];
					data[ di + 20] = vertices[vi + 8];
					data[ di + 21] = n.x;
					data[ di + 22] = n.y;
					data[ di + 23] = n.z;
					data[ di + 24] = c.x;
					data[ di + 25] = c.y;
					data[ di + 26] = c.z;
				});

		return Internal.generate(data, cull);
	}

	public static float[] generate(
			float[] sourceData, double cull
	) {
		float[] data = sourceData.clone();

		if ( data.length % TRIANGLE_SIZE != 0 )
			throw new IllegalArgumentException(
					"The provided data is not divisible by the expected triangle size!"
			);

		int numberOfTriangles = ( data.length / TRIANGLE_SIZE);

		Map<P3, Corner> associative = new HashMap<>();
		Set<Edge> edges = new HashSet<>();

		IntStream
				.range( 0, numberOfTriangles )
				.forEach( i -> {
					int ti = i * TRIANGLE_SIZE;
					Corner c1 = registered( data, ti + 0 * VERTEX_SIZE, associative );
					Corner c2 = registered( data, ti + 1 * VERTEX_SIZE, associative );
					Corner c3 = registered( data, ti + 2 * VERTEX_SIZE, associative );
					Vertex v1 = new Vertex( data, ti + 0 * VERTEX_SIZE );
					Vertex v2 = new Vertex( data, ti + 1 * VERTEX_SIZE );
					Vertex v3 = new Vertex( data, ti + 2 * VERTEX_SIZE );
					c1.addNeighbour( c2, c3 );
					c2.addNeighbour( c1, c3 );
					c3.addNeighbour( c1, c2 );
					c1.addTriangle( new Triangle( v1, v2, v3 ) );
					c2.addTriangle( new Triangle( v2, v1, v3 ) );
					c3.addTriangle( new Triangle( v3, v1, v2 ) );
					edges.add(new Edge( c2, c3 ));
					edges.add(new Edge( c1, c3 ));
					edges.add(new Edge( c1, c2 ));
				});

		int[] i = {0};
		int limit = (int) (edges.size() * cull);

		List<Edge> sortedList = new ArrayList<>();
		edges.stream()
				.parallel()
				.sorted( (a, b) -> (a.cost() > b.cost() ? 1 : 0) )
				.sequential()
				.forEach( e -> {
					sortedList.add(e);
					if ( i[0] < limit && !e.willBeCollapsed() ) {
						e.flagAsCollapsable();
						i[0]++;
					}
				});

		Set<Vertex> resultSet = new HashSet<>();
		sortedList.stream().parallel().forEach( e -> {
			if ( e.willBeCollapsed() ) e.collapse();
		});

		resultSet.addAll(
				associative
						.values()
						.stream()
						.parallel()
						.flatMap( c -> c.getVertices().stream() )
						.collect(Collectors.toList())
		);

		List<Vertex> vertecies = new ArrayList<>(resultSet);

		float[] result = new float[resultSet.size() * VERTEX_SIZE];

		IntStream.range(0, vertecies.size())
				.parallel()
				.forEach( vi -> {
					int di = vi * VERTEX_SIZE;
					Vertex v = vertecies.get(vi);
					result[di + 0] = v.x();
					result[di + 1] = v.y();
					result[di + 2] = v.z();
					result[di + 3] = v.nx();
					result[di + 4] = v.ny();
					result[di + 5] = v.nz();
					result[di + 6] = v.r();
					result[di + 7] = v.g();
					result[di + 8] = v.b();
					for ( int ii = 9; ii < VERTEX_SIZE; ii++ ) {
						result[di + ii] = sourceData[ v.getPointer() + ii ];
					}
				});

		return result;
	}

	private static Corner registered(
			float[] data, int ti, Map<P3, Corner> associative
	) {
		Corner v = associative.get( new P3(data, ti) );
		if ( v == null ) {
			v = new Corner( new P3(data, ti) );
			associative.put( new P3(data, ti), v );
		}
		return v;
	}


	private static Vector3 color(float[] colors, int i) {
		if ( colors == null ) return new Vector3(0,0,0);
		return new Vector3(
				colors[ i + 0 ], colors[ i + 1 ], colors[ i + 2 ]
		);
	}

	private static Vector3 normal(float[] vertecies, int i, float[] normals, int ni) {

		if ( normals != null ) {
			return new Vector3(normals[ni + 0], normals[ni + 1], normals[ni + 2]);
		}

		Vector3 p1 = new Vector3(vertecies[i + 0], vertecies[i + 1], vertecies[i + 2]);
		Vector3 p2 = new Vector3(vertecies[i + 3], vertecies[i + 4], vertecies[i + 5]);
		Vector3 p3 = new Vector3(vertecies[i + 6], vertecies[i + 7], vertecies[i + 8]);

		//Get the UV data
		Vector3 u = new Vector3(p2.x - p1.x,p2.y - p1.y,p2.z - p1.z);
		Vector3 v = new Vector3(p3.x - p1.x,p3.y - p1.y,p3.z - p1.z);

		//Calculate normals
		float nx = (u.y * v.z) - (u.z * v.y);
		float ny = (u.z * v.x) - (u.x * v.z);
		float nz = (u.x * v.y) - (u.y * v.x);

		//Normalise them
		float nf = (float) Math.sqrt((nx * nx) + (ny * ny) + (nz * nz)); //Normalisation factor
		nx = nx / nf;
		ny = ny / nf;
		nz = nz / nf;

		return new Vector3(nx, ny, nz);
	}



}
