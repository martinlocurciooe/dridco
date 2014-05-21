package evaluation.model;

import java.util.List;

public class AreaMap {
  private final List<City> cities;
  private final List<Path> paths;

  public AreaMap(List<City> cities, List<Path> paths) {
    this.cities = cities;
    this.paths = paths;
  }

  public List<City> getCities() {
    return cities;
  }

  public List<Path> getPaths() {
    return paths;
  }
  
  
  
} 
