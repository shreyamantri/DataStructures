package ngrams;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (Integer year: ts.keySet()) {
            if (year >= startYear && year <= endYear) {
                this.put(year, ts.get(year));
            }
        }

    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        List<Integer> year = new ArrayList<>();
        for (Integer value: this.keySet()) {
            year.add(value);
        }
        return year;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List<Double> data = new ArrayList<>();
        for (Integer value: this.keySet()) {
            data.add(this.get(value));
        }
        return data;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries dataAdd = new TimeSeries();

        if (this.keySet().isEmpty() && ts.keySet().isEmpty()) {
            return dataAdd;
        }

        for (Integer year: this.keySet()) {
            if (!ts.containsKey(year)) {
                dataAdd.put(year, this.get(year));
            } else {
                dataAdd.put(year, this.get(year) + ts.get(year));
            }
        }

        for (Integer year: ts.keySet()) {
            if (!this.containsKey(year)) {
                dataAdd.put(year, ts.get(year));
            } else {
                dataAdd.put(year, this.get(year) + ts.get(year));
            }
        }
        return dataAdd;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries dataDivided = new TimeSeries();

        for (Integer year: this.keySet()) {
            if (!ts.containsKey(year)) {
                throw new IllegalArgumentException("Year value not in the inputted time series.");
            } else {
                dataDivided.put(year, this.get(year) / ts.get(year));
            }
        }
        return dataDivided;
    }
}
