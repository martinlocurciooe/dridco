package evaluation.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import evaluation.exception.service.AreaMapServiceException;
import evaluation.model.AreaMap;
import evaluation.model.City;
import evaluation.model.Path;
import evaluation.service.AreaMapService;
import evaluation.service.impl.AreaMapServiceImpl;

public class Application {

	private static String DISTANCE_BETWEEN_CITIES = "getDistanceBetweenCities";
	private static String NUMBER_TRAVEL_SOURCE_DESTINATION_MAX_STOPS = "getNumberTravelSourceDestinationMaxStops";
	private static String NUMBER_TRAVEL_SOURCE_DESTINATION_WITH_STOPS = "getNumberTravelSourceDestinationWithStops";
	private static String SHORTEST_PATH_DISTANCE = "getShortestPathDistanceSourceDestination";
	private static String NUMBER_PATHS_SOURCE_DESTINATION_DISTANCE_LESS_THAN = "getNumberOfPathsSourceDestinationDistanceLessThan";

	private static AreaMapService areaMapService;
	private static Set<String> entries;
	private static List<City> cities = new ArrayList<City>();
	private static List<Path> paths = new ArrayList<Path>();
	private static boolean methodFound = false;
	private static String method = "";
	
	//Executions 1 - Standard Output: AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getDistanceBetweenCities A B C
	//Executions 2 - Standard Output: AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getDistanceBetweenCities A D
	//Executions 3 - Standard Output: AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getDistanceBetweenCities A D C
	//Executions 4 - Standard Output: AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getDistanceBetweenCities A E B C D
	//Executions 5 - Exception catched an error shown: AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getDistanceBetweenCities A E D
	//Executions 6 - Standard Output: AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getNumberTravelSourceDestinationMaxStops C C 3
	//Executions 7 - Standard Output: AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getNumberTravelSourceDestinationWithStops A C 4
	//Executions 8 - Standard Output: AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getShortestPathDistanceSourceDestination A C
	//Executions 9 - Standard Output: AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getShortestPathDistanceSourceDestination B B
	//Executions 10 - Standard Output: AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getNumberOfPathsSourceDestinationDistanceLessThan C C 30
	
	public static void main(String[] args) {
		entries = new HashSet<String>();
		List<String> params = new ArrayList<String>();
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("Starting application...");
		System.out.println("-------------------------------------------------------------------------------");
		try {
			handleParameters(args, params);
			loadAreaMap();
			handleMethod(params);
		} catch (Exception e) {
			System.out.println("Error: " +  e.getMessage());
		}


	}

	private static void handleMethod(List<String> params)
			throws AreaMapServiceException {
		System.out.println("-------------------------------------------------------------------------------");
		if (DISTANCE_BETWEEN_CITIES.equals(method)) {
			handleDistanceBetweenCities(params);
		} else if (NUMBER_TRAVEL_SOURCE_DESTINATION_MAX_STOPS.equals(method)) {
			handleNumberTravelSourceDestinationMaxStops(params);
		} else if (NUMBER_TRAVEL_SOURCE_DESTINATION_WITH_STOPS.equals(method)) {
			handleNumberTravelSourceDestinationWithStops(params);
		} else if (SHORTEST_PATH_DISTANCE.equals(method)) {
			handleShortestPathDistance(params);
		} else if (NUMBER_PATHS_SOURCE_DESTINATION_DISTANCE_LESS_THAN.equals(method)) {
			handleNumberPathsSourceDestinationDistanceLessThan(params);
		} else {
			System.out.println("ERROR: UNKNOWN METHOD");
		}
		System.out.println("-------------------------------------------------------------------------------");
	}

	private static void handleNumberPathsSourceDestinationDistanceLessThan(
			List<String> params) throws AreaMapServiceException {
		System.out.println("Calculating number of paths from to with distance less than");
		City source = new City(params.get(0));
		City destination = new City(params.get(1));
		int distance = Integer.valueOf(params.get(2));
		System.out.println("Number of paths from to with distance less than: " + areaMapService.getNumberOfPathsWithDistanceLessThan(source, destination, distance));
	}

	private static void handleShortestPathDistance(List<String> params)
			throws AreaMapServiceException {
		System.out.println("Calulating shortest distance");
		City source = new City(params.get(0));
		City destination = new City(params.get(1));
		System.out.println("Shortest distance: " + areaMapService.getShortestDistanceBetween(source, destination));
	}

	private static void handleNumberTravelSourceDestinationWithStops(
			List<String> params) {
		System.out.println("Caluclating number of travel from to with stops");
		City source = new City(params.get(0));
		City destination = new City(params.get(1));
		int stops = Integer.valueOf(params.get(2));
		System.out.println("Number of travel from to with stops: " + areaMapService.getNumberOfTravelsWithStops(source, destination, stops));
	}

	private static void handleNumberTravelSourceDestinationMaxStops(
			List<String> params) {
		System.out.println("Calculating number of travel from to with max stops");
		City source = new City(params.get(0));
		City destination = new City(params.get(1));
		int maxStops = Integer.valueOf(params.get(2));
		System.out.println("Number of travel from to with max stops: " + String.valueOf(areaMapService.getNumberOfTravelsMaxStops(source, destination, maxStops)));
	}

	private static void handleDistanceBetweenCities(List<String> params)
			throws AreaMapServiceException {
		List<City> citiesToGetDistanceBetween = new ArrayList<City>();
		System.out.println("Calculating distance between cities");
		for(String param : params){
			citiesToGetDistanceBetween.add(new City(param));
		}
		System.out.println("Distance Between cities: " + String.valueOf(areaMapService.getDistanceBetween(citiesToGetDistanceBetween)));
	}

	private static void handleParameters(String[] args, List<String> params) {
		for (String entry : args) {
			if (!methodFound) {
				if (!entry.contains("get")) {
					entries.add(entry);
				} else {
					methodFound = true;
					method = entry;
				}
			} else {
				params.add(entry);
			}
		}
	}

	private static void loadAreaMap() {
		int i = 0;
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("Loading Area Map....");
		for (String entry : entries) {
			City city = new City(String.valueOf(entry.charAt(0)));
			City city2 = new City(String.valueOf(entry.charAt(1)));
			if (!cities.contains(city)) {
				cities.add(city);
			}
			if (!cities.contains(city2)) {
				cities.add(city2);
			}
			String distanceString = "";
			for (i = 2 ; i < entry.length() ; i ++) {
				distanceString = distanceString + entry.charAt(i);
			}
			int distance = Integer.valueOf(distanceString);
			addPath("Edge_" + i, cities.indexOf(city), cities.indexOf(city2), distance);
			i++;
		}
		AreaMap areaMap = new AreaMap(cities,paths);
		areaMapService = new AreaMapServiceImpl(areaMap);
		System.out.println("Area Map Loaded! - " + cities.size() + " cities found! - " + paths.size() + " paths found");
		System.out.println("-------------------------------------------------------------------------------");

	}

	private static void addPath(String pathId, int sourceLocNo, int destLocNo,
			int duration) {
		Path path = new Path(pathId, cities.get(sourceLocNo),
				cities.get(destLocNo), duration);
		paths.add(path);
	}

}
