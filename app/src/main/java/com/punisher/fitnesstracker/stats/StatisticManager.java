package com.punisher.fitnesstracker.stats;

import com.punisher.fitnesstracker.dto.FitnessActivity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a simple Statistic Manager that encapsulates the calculations. It provides public constants to
 * retreive the data from the underlying HashMap.
 */
public class StatisticManager {

    public static final String TOTAL_ACTIVITY = "TotalActivity";
    public static final String LONGEST_DURATION = "LongestDuration";
    public static final String LONGEST_DISTANCE = "LongestDistance";
    public static final String BEST_AVERAGE = "BestAverage";
    public static final String TOTAL_DURATION = "TotalDuration";
    public static final String TOTAL_DISTANCE = "TotalDistance";

    /**
     * the compiled stats
     */
    private Map<String, Object> _compiledStats = null;

    /**
     * default constructor
     */
    public StatisticManager() {
        _compiledStats = new HashMap<String, Object>();
    }

    /**
     * calculate the statistics based on the list it receives
     * @param list the list of FitnessActivity to derive the stats from
     */
    public void crunchStatistic(List<FitnessActivity> list) {

        if (list != null) {

            // calculate the total activity
            _compiledStats.put(TOTAL_ACTIVITY, list.size());

            // calculate the total distance
            int totalDistance = 0;
            int totalDuration = 0;

            for (FitnessActivity a : list) {
                totalDistance += a.getDistance();
                totalDuration += a.getDuration();
            }
            _compiledStats.put(TOTAL_DISTANCE, totalDistance);
            _compiledStats.put(TOTAL_DURATION, totalDuration);

            // find the longest distance
            int longestDistanace = 0;
            for (FitnessActivity a : list) {
                if (a.getDistance() > longestDistanace) {
                    longestDistanace = a.getDistance();
                }
            }
            _compiledStats.put(LONGEST_DISTANCE, longestDistanace);

            // find the longest duration
            int longestDuration = 0;
            for (FitnessActivity a : list) {
                if (a.getDuration() > longestDuration) {
                    longestDuration = a.getDuration();
                }
            }
            _compiledStats.put(LONGEST_DURATION, longestDuration);

            // find the best average
            float bestAverage = 0;
            for (FitnessActivity a : list) {
                if (a.getAverage() < bestAverage) {
                    bestAverage = a.getAverage();
                }
            }
            _compiledStats.put(BEST_AVERAGE, bestAverage);
        }
    }

    /**
     * utility method to get the statistics by their key
     * @param key the unique key that identifies them
     * @return the values associated to the key
     */
    public Object getValues(String key) {
        return _compiledStats.get(key);
    }

    /**
     * return the underlying HashMap that contains the statistics
     * @return the HashMap containing the statistics
     */
    protected Map<String, Object> getCompiledStatMap() {
        return _compiledStats;
    }
}
