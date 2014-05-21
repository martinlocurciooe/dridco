package evaluation.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	public void execute(City source) {
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

	private void findMinimalDistances(City node) {
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

	private int getDistance(City node, City target) {
		for (Path edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getDistance();
			}
		}
		throw new RuntimeException("Should not happen");
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

}
