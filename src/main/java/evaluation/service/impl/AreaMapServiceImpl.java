package evaluation.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import evaluation.exception.service.AreaMapServiceException;
import evaluation.model.Path;
import evaluation.model.AreaMap;
import evaluation.model.City;
import evaluation.service.AreaMapService;

public class AreaMapServiceImpl implements AreaMapService{

	private final List<City> cities;
	private final List<Path> paths;
	private Set<City> settledcities;
	private Set<City> unSettledcities;
	private Map<City, City> predecessors;
	private Map<City, Integer> distance;

	public AreaMapServiceImpl(AreaMap areaMap) {
		this.cities = new ArrayList<City>(areaMap.getCities());
		this.paths = new ArrayList<Path>(areaMap.getPaths());
	}

	/**
	 * execute dijkstra algorithm to find the shortest path
	 * @param source
	 * @throws AreaMapServiceException
	 */
	private void executeDijkstraAlgorithm(City source) throws AreaMapServiceException {
		settledcities = new HashSet<City>();
		unSettledcities = new HashSet<City>();
		distance = new HashMap<City, Integer>();
		predecessors = new HashMap<City, City>();
		distance.put(source, 0);
		unSettledcities.add(source);
		while (unSettledcities.size() > 0) {
			City node = getMinimum(unSettledcities);
			settledcities.add(node);
			unSettledcities.remove(node);
			findMinimalDistances(node);
		}
	}

	/**
	 * find minimal distances
	 * @param city
	 * @throws AreaMapServiceException
	 */
	private void findMinimalDistances(City city) throws AreaMapServiceException {
		List<City> adjacentCities = getNeighbors(city);
		for (City target : adjacentCities) {
			if (getShortestDistance(target) > getShortestDistance(city)
					+ getDistance(city, target)) {
				distance.put(target,
						getShortestDistance(city) + getDistance(city, target));
				predecessors.put(target, city);
				unSettledcities.add(target);
			}
		}

	}

	/**
	 * get the inmediate distance between 2 cities
	 * @param source
	 * @param destination
	 * @return
	 * @throws AreaMapServiceException
	 */
	private int getDistance(City source, City destination) throws AreaMapServiceException {
		for (Path edge : paths) {
			if (edge.getSource().equals(source)
					&& edge.getDestination().equals(destination)) {
				return edge.getDistance();
			}
		}
		throw new AreaMapServiceException("There are not path for the given cities");
	}

	/**
	 * get neighbors for a given city
	 * @param city
	 * @return
	 */
	private List<City> getNeighbors(City city) {
		List<City> neighbors = new ArrayList<City>();
		for (Path path : paths) {
			if (path.getSource().equals(city)
					&& !isSettled(path.getDestination())) {
				neighbors.add(path.getDestination());
			}
		}
		return neighbors;
	}

	/**
	 * get the closer city
	 * @param cities
	 * @return
	 */
	private City getMinimum(Set<City> cities) {
		City minimum = null;
		for (City city : cities) {
			if (minimum == null) {
				minimum = city;
			} else {
				if (getShortestDistance(city) < getShortestDistance(minimum)) {
					minimum = city;
				}
			}
		}
		return minimum;
	}

	/**
	 * check if the city is settled
	 * @param city
	 * @return
	 */
	private boolean isSettled(City city) {
		return settledcities.contains(city);
	}

	/**
	 * get shortest distance from a given source to a destination
	 * @param destination
	 * @return
	 */
	private int getShortestDistance(City destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/**
	 *  This method returns the path from the source to the selected target and
	 *  NULL if no path exists
	 */
	public LinkedList<City> getPath(City destination) {
		LinkedList<City> path = new LinkedList<City>();
		City step = destination;
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		Collections.reverse(path);
		return path;
	}
	
	/**
	 * 
	 * @param cities - the first city in the list is the source
	 * @return 0 if there are an error
	 * @throws AreaMapServiceException 
	 */
	public int getDistanceBetween(List<City> cities) throws AreaMapServiceException {
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

	/**
	 * return the number of travel with a number of max stops for the source and destination given.
	 */
	public int getNumberOfTravelsMaxStops(City source, City destination, int maxStops) {
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
					numberOfStops--;
				}
			}
		}
		return numberOfTravels;
	}
	
	/**
	 * return the number of travels with a given number of stops for the source and destination given.
	 */
	public int getNumberOfTravelsWithStops(City source, City destination, int stops) {
		int numberOfTravels = 0;
		int numberOfStops = 0;
		//Search for adjacent cities
		List<City> cities = getNeighborsSettledNodes(source);
		//If i've arrived in the exact amount of stops
		if(source.equals(destination) && numberOfStops == stops){
			numberOfTravels ++;
		} else {
			for (City city : cities) {
				//Travel to another city, and increment stops
				numberOfStops ++;
				if(city.equals(destination) && numberOfStops == stops){
					numberOfTravels ++;
					numberOfStops --;
					//I've arrived, so, i decrease the stops for the next iteration
				} else {
					if(numberOfStops <= stops) {
						//Travel to the adjacent city with the same destiny with one less stop
						numberOfTravels = numberOfTravels + getNumberOfTravelsWithStops(city, destination, stops - 1);	
						numberOfStops--;
						//I have not arrived, so, i decrease the stops for the next iteration
					}
				}
			}
		}
		return numberOfTravels;
		
	}
	
	/**
	 * get neighbors
	 * @param node
	 * @return
	 */
	private List<City> getNeighborsSettledNodes(City node) {
		List<City> neighbors = new ArrayList<City>();
		for (Path edge : paths) {
			if (edge.getSource().equals(node)) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}
	
	/**
	 * return the shortest distance between source and destination traveling around all the area map
	 */
	public int getShortestDistanceBetween(City source, City destination) throws AreaMapServiceException{
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

	/**
	 * return the number of paths with a distance minor of @param distance
	 */
	public int getNumberOfPathsWithDistanceLessThan(City source,
			City destination, int distance) throws AreaMapServiceException {
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
