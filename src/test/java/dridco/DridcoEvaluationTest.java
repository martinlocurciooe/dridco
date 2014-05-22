package dridco;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import evaluation.exception.service.DijkstraAlgorithmServiceException;
import evaluation.model.AreaMap;
import evaluation.model.City;
import evaluation.model.Path;
import evaluation.service.DijkstraAlgorithmService;
import evaluation.service.impl.DijkstraAlgorithmServiceImpl;

public class DridcoEvaluationTest {

	private List<City> cities;
	private List<Path> paths;
	private DijkstraAlgorithmService dijkstra;

	@Before
	public void setUp() throws DijkstraAlgorithmServiceException {
		cities = new ArrayList<City>();
		paths = new ArrayList<Path>();
		City city1 = new City("A");
		City city2 = new City("B");
		City city3 = new City("C");
		City city4 = new City("D");
		City city5 = new City("E");
		cities.add(city1);
		cities.add(city2);
		cities.add(city3);
		cities.add(city4);
		cities.add(city5);
		addPath("Edge_0", 0, 1, 5);
	    addPath("Edge_1", 1, 2, 4);
	    addPath("Edge_2", 2, 3, 8);
	    addPath("Edge_3", 3, 2, 8);
	    addPath("Edge_4", 3, 4, 6);
	    addPath("Edge_5", 0, 3, 5);
	    addPath("Edge_6", 2, 4, 2);
	    addPath("Edge_7", 4, 1, 3);
	    addPath("Edge_8", 0, 4, 7);
	    AreaMap areaMap = new AreaMap(cities, paths);
	    dijkstra = new DijkstraAlgorithmServiceImpl(areaMap);
	    
	}

	@Test
	public void testSimple() {
		assertTrue(true);
	}
	
	/**
	 * 1. La distancia de la ruta A-B-C
	 * @throws DijkstraAlgorithmServiceException 
	 */
	@Test
	public void testDistanceBetweenABC() throws DijkstraAlgorithmServiceException {
		List<City> citiesToGetDistances = new ArrayList<City>();
		citiesToGetDistances.add(new City("A"));
		citiesToGetDistances.add(new City("B"));
		citiesToGetDistances.add(new City("C"));
		int distanceBetween = dijkstra.getDistanceBetween(citiesToGetDistances);
		assertEquals(9,distanceBetween);
	}
	
	/**
	 * 2. La distancia de la ruta A-D
	 * @throws DijkstraAlgorithmServiceException 
	 */
	@Test
	public void testDistanceBetweenAD() throws DijkstraAlgorithmServiceException {
		List<City> citiesToGetDistances = new ArrayList<City>();
		citiesToGetDistances.add(new City("A"));
		citiesToGetDistances.add(new City("D"));
		int distanceBetween = dijkstra.getDistanceBetween(citiesToGetDistances);
		assertEquals(5,distanceBetween);
	}
	
	/**
	 * 3. La distancia de la ruta A-D-C
	 * @throws DijkstraAlgorithmServiceException 
	 */
	@Test
	public void testDistanceBetweenADC() throws DijkstraAlgorithmServiceException {
		List<City> citiesToGetDistances = new ArrayList<City>();
		citiesToGetDistances.add(new City("A"));
		citiesToGetDistances.add(new City("D"));
		citiesToGetDistances.add(new City("C"));
		int distanceBetween = dijkstra.getDistanceBetween(citiesToGetDistances);
		assertEquals(13,distanceBetween);
	}
	
	/**
	 * 4. La distancia de la ruta A-E-B-C-D
	 * @throws DijkstraAlgorithmServiceException 
	 */
	@Test
	public void testDistanceBetweenAEBCD() throws DijkstraAlgorithmServiceException {
		List<City> citiesToGetDistances = new ArrayList<City>();
		citiesToGetDistances.add(new City("A"));
		citiesToGetDistances.add(new City("E"));
		citiesToGetDistances.add(new City("B"));
		citiesToGetDistances.add(new City("C"));
		citiesToGetDistances.add(new City("D"));
		int distanceBetween = dijkstra.getDistanceBetween(citiesToGetDistances);
		assertEquals(22,distanceBetween);
	}
	
	/**
	 * 5. La distancia de la ruta A-E-D
	 * @throws DijkstraAlgorithmServiceException 
	 */
	@Test(expected = DijkstraAlgorithmServiceException.class)
	public void testDistanceBetweenAED() throws DijkstraAlgorithmServiceException {
		List<City> citiesToGetDistances = new ArrayList<City>();
		citiesToGetDistances.add(new City("A"));
		citiesToGetDistances.add(new City("E"));
		citiesToGetDistances.add(new City("D"));
		dijkstra.getDistanceBetween(citiesToGetDistances);
	}
	
	/**
	 * La cantidad de viajes partiendo desde C y terminando en C con un máximo de tres paradas. 
	 * Con el grafo provisto de ejemplo, pueden encontrarse 2 viajes posibles con 
	 * dichas características: C-D-C (2 paradas) y C-E-B-C (3 paradas) testTravelsSourceAndDestinationCWithMaximumOf3Stops
	 */

	@Test
	public void testTravelsSourceAndDestinationCWithMaximumOf3Stops() throws DijkstraAlgorithmServiceException {
		City source = new City("C");
		City destination = new City("C");
		int maxStops = 3;
		int numberOfTravels = dijkstra.getNumberOfTravelsMaxStops(source, destination, maxStops);
		assertEquals(2,numberOfTravels);
	}
	
	/**
	 * 7. La cantidad de viajes partiendo desde A y terminando en C con exactamente 4 paradas.
	 *  Con el grafo provisto de ejemplo, pueden encontrarse 3 viajes posibles con 
	 *  dichas características: A hacia C (vía B-C-D); A hacia C (vía D-C-D); y A hacia C (vía D-E-B).
	 */
	@Test
	public void testTravelsSourceADestinationCWithNumberOfStops3() throws DijkstraAlgorithmServiceException {
		City source = new City("A");
		City destination = new City("C");
		int stops = 3;
		int numberOfTravels = dijkstra.getNumberOfTravelsWithStops(source, destination, stops);
		assertEquals(3,numberOfTravels);
	}
	
	/**
	 * 8. La distancia de la ruta más corta desde A hasta C
	 * @throws DijkstraAlgorithmServiceException
	 */
	@Test
	public void testShortDistanceBetweenAC() throws DijkstraAlgorithmServiceException {
		City source = new City("A");
		City destination = new City("C");
		int shortDistance = dijkstra.getShortestDistanceBetween(source, destination);
		assertEquals(9,shortDistance);
	}
	
	/**
	 * 9. La distancia de la ruta más corta desde B hasta B
	 * @throws DijkstraAlgorithmServiceException
	 */
	@Test
	public void testShortDistanceBetweenBB() throws DijkstraAlgorithmServiceException {
		City source = new City("B");
		City destination = new City("B");
		int shortDistance = dijkstra.getShortestDistanceBetween(source, destination);
		assertEquals(9,shortDistance);
	}
	
	/**
	 * 10. La cantidad de rutas distintas desde C hasta C con una distancia menor a 30.
	 *  Con el grafo provisto de ejemplo pueden encontrarse los siguientes viajes con
	 *  dichas características: C-D-C, C-E-B-C, C-E-B-C-D-C, C-D-C-E-B-C, C-D-E-B-C, C-E-B-C-E-B-C, C-E-B-C-E-B-C-E-B-C.
	 * @throws DijkstraAlgorithmServiceException
	 */
	@Test
	public void testNumberOfPathsCtoCWithDistanceLessThan30() throws DijkstraAlgorithmServiceException {
		City source = new City("C");
		City destination = new City("C");
		int distance = 30;
		int numberOfPaths = dijkstra.getNumberOfPathsWithDistanceLessThan(source, destination, distance);
		assertEquals(7,numberOfPaths);
	}
	
	
	private void addPath(String pathId, int sourceLocNo, int destLocNo,
			int duration) {
		Path path = new Path(pathId, cities.get(sourceLocNo),
				cities.get(destLocNo), duration);
		paths.add(path);
	}

}
