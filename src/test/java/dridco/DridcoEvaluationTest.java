package dridco;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
	public void setUp() {
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
	    dijkstra.execute(cities.get(0));
	    
	}

	@Test
	public void testSimple() {
		assertTrue(true);
	}
	
	@Test
	public void testDistanceBetweenABC() {

	}
	
	private void addPath(String pathId, int sourceLocNo, int destLocNo,
			int duration) {
		Path path = new Path(pathId, cities.get(sourceLocNo),
				cities.get(destLocNo), duration);
		paths.add(path);
	}

}
