public class CitiesDistance {

  // A.length == X.length
  // A[i]: fuel at the Ith city, X[i]: distance at the Ith city
  // Car starts with 0 fuel and can fill up at whichever city we start in (unlimited fuel capacity)
  // return max number of cities that can be visited.
  // We can go from city 1 to city 2 if the absolute distance between the two is <= the current amount of fuel we have
  public void main() {
    int[] A = {4, 1, 4, 3, 3};
    int[] X = {8, 10, 11, 13, 100};
    int result = solution(A, X);
    System.out.println(result);
  }
  
  private class City {
    int fuel;
    int distance;
    public City(int fuel, int distance) {
      this.fuel = fuel;
      this.distance = distance;
    }
  }

  class CachedItinerary {
    int count;
    int currentFuel;
    City currentCity;
    Set<City> traversedCities;
    public CachedItinerary(int count, int currentFuel, City currentCity) {
      this(count, currentFuel, currentCity, null);
    }

    public CachedItinerary(int count, int currentFuel, City currentCity, Set<City> traversedCities) {
      this.count = count;
      this.currentFuel = currentFuel;
      this.currentCity = currentCity;
      if (traversedCities == null) {
        this.traversedCities = new HashSet<>();
      } else {
        this.traversedCities = new HashSet<>(traversedCities);
      }
      this.traversedCities.add(currentCity);
    }

    public void addCity(City city) {
      traversedCities.add(city);
    }
  }

  public int solution(int[] A, int[] X) {
    int n = A.length;
    CachedItinerary[][] cache = new CachedItinerary[n+1][n+1];
    City[] cities = new City[n];

    for (int i=0; i<n; i++) {
      cities[i] = new City(A[i], X[i]);
      cache[i+1][0] = new CachedItinerary(1, cities[i].fuel, cities[i]);
      cache[0][i+1] = new CachedItinerary(1, cities[i].fuel, cities[i]);
    }

    for (int i=1; i<=n; i++) {
      for (int j=1; j<=n; j++) {
        // check from left
        if (canGoFromTo(cache[i][j-1], cities[j-1])) {
          int fuel = calculateRemainingFuel(cache[i][j-1], cities[j-1]);
          cache[i][j] = new CachedItinerary(cache[i][j-1].count + 1, fuel, cities[j-1], cache[i][j-1].traversedCities);
        }
        // check from top
        else if (canGoFromTo(cache[i-1][j], cities[i-1])) {
          int fuel = calculateRemainingFuel(cache[i-1][j], cities[i-1]);
          cache[i][j] = new CachedItinerary(cache[i-1][j].count + 1, fuel, cities[i-1], cache[i-1][j].traversedCities);
        } else {
          cache[i][j] = cache[i][j-1].count >= cache[i-1][j].count ? cache[i][j-1] : cache[i-1][j];
        }
      }
    }

    return cache[n+1][n+1].count;
  }

  private boolean canGoFromTo(CachedItinerary from, City to) {
    return from.currentCity != to && !from.traversedCities.contains(to) && Math.abs(to.distance - from.currentCity.distance) <= from.currentFuel;
  }

  private int calculateRemainingFuel(CachedItinerary from, City to) {
    return from.currentFuel - Math.abs(to.distance - from.currentCity.distance) + to.fuel;
  }

  public int solutionRecursive(int[] A, int[] X) {
    int n = A.length;
    Set<City> cities = new HashSet<>();
    for (int i=0; i<n; i++) {
      cities.add(new City(A[i], X[i]));
    }

    return maxCities(cities, null, 0, 0);
  }

  private int maxCities(Set<City> cities, City currentCity, int currentFuel, int count) {
    // base case
    if (cities.size() == 0) {
      return count;
    }
    int max = count;
    for (City city : cities) {
      Set<City> copy = new HashSet<>(cities);
      copy.remove(city);
      if (currentCity == null) {
        max = Math.max(maxCities(copy, city, city.fuel, count + 1), max);
      } else if (Math.abs(city.distance - currentCity.distance) <= currentFuel) {
        int newFuel = currentFuel - Math.abs(city.distance - currentCity.distance) + city.fuel;
        max = Math.max(max, maxCities(copy, city, newFuel, count + 1));
      }
    }
    return max;
  }
}
