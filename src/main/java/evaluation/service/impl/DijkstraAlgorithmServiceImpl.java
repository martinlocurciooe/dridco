package evaluation.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import evaluation.exception.service.DijkstraAlgorithmServiceException;
import evaluation.model.Path;
import evaluation.model.AreaMap;
import evaluation.model.City;
import evaluation.service.DijkstraAlgorithmService;

public class DijkstraAlgorithmServiceImpl implements DijkstraAlgorithmService{

	private final List<City> nodes;
	private final List<Path> edges;
	private Set<City> settledNodes;
	private Set<City> unSettledNodes;
	private Map<City, City> predecessors;
	private Map<City, Integer> distance;

	public DijkstraAlgorithmServiceImpl(AreaMap graph) {
		// create a copy of the array so that we can operate on this array
		this.nodes = new ArrayList<City>(graph.getCities());
		this.edges = new ArrayList<Path>(graph.getPaths());
	}

	private void executeDijkstraAlgorithm(City source) throws DijkstraAlgorithmServiceException {
		settledNodes = new HashSet<City>();
		unSettledNodes = new HashSet<City>();
		distance = new HashMap<City, Integer>();
		predecessors = new HashMap<City, City>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			City node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(City node) throws DijkstraAlgorithmServiceException {
		List<City> adjacentNodes = getNeighbors(node);
		for (City target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private int getDistance(City node, City target) throws DijkstraAlgorithmServiceException {
		for (Path edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getDistance();
			}
		}
		throw new DijkstraAlgorithmServiceException("There are not path for the given cities");
	}

	private List<City> getNeighbors(City node) {
		List<City> neighbors = new ArrayList<City>();
		for (Path edge : edges) {
			if (edge.getSource().equals(node)
					&& !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	private City getMinimum(Set<City> vertexes) {
		City minimum = null;
		for (City vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(City vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(City destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public LinkedList<City> getPath(City target) {
		LinkedList<City> path = new LinkedList<City>();
		City step = target;
		// check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}
	
	/**
	 * 
	 * @param cities - the first city in the list is the source
	 * @return 0 if there are an error
	 * @throws DijkstraAlgorithmServiceException 
	 */
	public int getDistanceBetween(List<City> cities) throws DijkstraAlgorithmServiceException {
		int acumulatedDistance = 0;
		if(cities != null && !cities.isEmpty()) {
			City source = cities.get(0);
			cities.remove(0);
			for(City city : cities) {
				acumulatedDistance = acumulatedDistance + getDistance(source, city);
				source = city;
			}
		}
		return acumulatedDistance;
		
	}

	public int getNumberOfTravelsMaxStops(City source, City destination, int maxStops) {
		//Another solution could be to call the getNumberOfTravelsWithStops for 1,2... maxStops.
		int numberOfTravels = 0;
		int numberOfStops = 0;
		List<City> cities = getNeighborsSettledNodes(source);
		for (City city : cities) {
			if(city.equals(destination)){
				numberOfTravels ++;
			} else {
				numberOfStops ++;
				if(numberOfStops < maxStops) {
					numberOfTravels = numberOfTravels + getNumberOfTravelsMaxStops(city, destination, maxStops - 1);					
				}
			}
		}
		return numberOfTravels;
	}
	
	public int getNumberOfTravelsWithStops(City source, City destination, int stops) {
		int numberOfTravels = 0;
		int numberOfStops = 0;
		List<City> cities = getNeighborsSettledNodes(source);
		for (City city : cities) {
			if(city.equals(destination) && numberOfStops == stops){
				numberOfTravels ++;
			} else {
				numberOfStops ++;
				if(numberOfStops <= stops) {
					numberOfTravels = numberOfTravels + getNumberOfTravelsWithStops(city, destination, stops - 1);					
				}
			}
		}
		return numberOfTravels;
	}
	
	private List<City> getNeighborsSettledNodes(City node) {
		List<City> neighbors = new ArrayList<City>();
		for (Path edge : edges) {
			if (edge.getSource().equals(node)) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}
	
	public int getShortestDistanceBetween(City source, City destination) throws DijkstraAlgorithmServiceException{
		if(source.equals(destination)){
			List<City> cities = getNeighborsSettledNodes(source);
			int shortestDistance = Integer.MAX_VALUE;
			for(City city : cities) {
				executeDijkstraAlgorithm(city);
				int shortDistance = getShortestDistance(destination) + getDistance(source, city);
				if(shortDistance < shortestDistance)
				shortestDistance = shortDistance;
			}
			return shortestDistance;
		} else {
			executeDijkstraAlgorithm(source);
			return getShortestDistance(destination);
		}
		
	}

	public int getNumberOfPathsWithDistanceLessThan(City source,
			City destination, int distance) throws DijkstraAlgorithmServiceException {
		int distanceTotal = 0;
		int numberOfPaths = 0;
		List<City> cities = getNeighborsSettledNodes(source);
		for (City city : cities) {
			distanceTotal = distanceTotal + getDistance(source, city);
			if(city.equals(destination) && (distanceTotal < distance) ){
				numberOfPaths ++;
				numberOfPaths = numberOfPaths + getNumberOfPathsWithDistanceLessThan(city, destination, distance - distanceTotal);
			} else {
				if(distanceTotal < distance) {
					numberOfPaths = numberOfPaths + getNumberOfPathsWithDistanceLessThan(city, destination, distance - distanceTotal);
				}
				distanceTotal = 0;
			}
		}
		return numberOfPaths;
	}
	
}
