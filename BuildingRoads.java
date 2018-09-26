// you can also use imports, for example:
import java.util.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");

class Solution {
    
    class CachedCities {
        Set<Integer> cities;
        public CachedCities(Set<Integer> cities) {
            this.cities = new HashSet<>(cities);
        }
    }
    
    public int[] solution(int[] A, int[] B, int N) {
        int length = A.length;
        int[] ans = new int[length];
        Set<Integer> cities = new HashSet<>();
        for (int i=1; i<=N; i++) {
            cities.add(i);
        }
        
        CachedCities[][] memo = new CachedCities[length+1][length+1];
        for (int i=0; i<=length; i++) {
            for (int j=0; j<=length; j++) {
                if (i==0 || j==0) {
                    memo[i][j] = new CachedCities(cities);
                } else {
                    Set<Integer> left = new HashSet<>(memo[i][j-1].cities);
                    Set<Integer> top = new HashSet<>(memo[i-1][j].cities);
                    if (i < j) {
                        // use j
                        memo[i][j] = chooseCities(left, top, A[j-1], B[j-1]);
                    } else if (i > j) {
                        // use i
                        memo[i][j] = chooseCities(left, top, A[i-1], B[i-1]);
                    } else {
                        memo[i][j] = chooseCities(left, top, A[i-1], B[j-1]);
                        ans[i-1] = memo[i][j].cities.size() - 1;
                    }
                }
            }
        }
        return ans;
    }
    
    private CachedCities chooseCities(Set<Integer> left, Set<Integer> top, int start, int end) {
        remove(left, start, end);
        remove(top, start, end);
        return left.size() <= top.size() ? new CachedCities(left) : new CachedCities(top);
    }
    
    private void remove(Set<Integer> cities, int start, int end) {
        if (cities.contains(start) && cities.contains(end)) {
            for (int i = start+1; i<end; i++) {
                cities.remove(i);
            }
        }
    }
}
