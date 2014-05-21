package evaluation.service;

import java.util.LinkedList;

import evaluation.model.City;

public interface DijkstraAlgorithmService {
	
	public void execute(City source);
	
	public LinkedList<City> getPath(City target);

}
