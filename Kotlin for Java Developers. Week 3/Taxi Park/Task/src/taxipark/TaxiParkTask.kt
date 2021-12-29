package taxipark

import java.util.stream.Collectors

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.stream()
                .filter {
                    driver -> driver !in trips.map { it.driver }.distinct().toSet()
                }
                .collect(Collectors.toSet())


/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        if (minTrips == 0) {
            allPassengers
        } else {
            trips.flatMap { it.passengers }
                    .groupBy { it }
                    .map { it.key to it.value.size }
                    .filter { it.second >= minTrips }
                    .map { it.first }
                    .stream()
                    .collect(Collectors.toSet())
        }

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        trips.stream()
                .filter {it.driver == driver}
                .flatMap { trip -> trip.passengers.stream() }
                .collect(Collectors.toList())
                .groupBy { it }
                .filter { it.value.size > 1 }
                .map { it.key }
                .toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        trips.flatMap { trip -> trip.passengers.map { passenger -> passenger to trip } }
                .map { it.first to (it.second.discount != null) }
                .groupBy { it.first }
                .filter {
                    val tripCount = it.value.size
                    val smartTrips = it.value.count { (_, smart) -> smart}
                    smartTrips > tripCount / 2.0
                }
                .map { it.key }
                .toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return trips.map { it.duration }
            .map {
                val lowerBound = (it - (it % 10))
                val upperBound = lowerBound + 10
                lowerBound until upperBound
            }
            .groupBy { it }
            .map { it.key to it.value.size }
            .maxBy { it.second }
            ?.first

}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) {
        return false
    }

    val sortedIncomes = trips.map { trip -> trip.driver to trip.cost }
            .groupBy { it.first }
            .map { it.value.map { pair -> pair.second }.sum() }
            .sorted()
            .reversed()
            .toMutableList()

    val zeroProfitDriversCount = allDrivers.size - sortedIncomes.size
    for (i in 0 until zeroProfitDriversCount) {
        sortedIncomes.add(0.0)
    }

    val topDriversCount = (sortedIncomes.size * 0.2).toInt()
    val totalIncome = sortedIncomes.sum()
    val topDriversIncome = sortedIncomes.subList(0, topDriversCount).sum()

    return topDriversIncome >= 0.8 * totalIncome
}