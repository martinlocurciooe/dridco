package evaluation.service;

import java.util.LinkedList;
import java.util.List;

import evaluation.exception.service.DijkstraAlgorithmServiceException;
import evaluation.model.City;

public interface DijkstraAlgorithmService {
	
	public LinkedList<City> getPath(City target);
	
	public int getDistanceBetween(List<City> cities) throws DijkstraAlgorithmServiceException;

	public int getNumberOfTravelsMaxStops(City source, City destination, int maxStops);
	
	public int getNumberOfTravelsWithStops(City source, City destination, int stops);
	
	public int getShortestDistanceBetween(City source, City destination) throws DijkstraAlgorithmServiceException;

	public int getNumberOfPathsWithDistanceLessThan(City source,
			City destination, int distance) throws DijkstraAlgorithmServiceException;
	
}
