To exec the application from console:

Where the args have the following structure:

[PATHS] [METHOD] [PARAMS]

->[PATHS]:
	CHARACTER + CHARACTER + NUMBER
->[METHOD]
	One of the followings (Without Quotes):
			"getDistanceBetweenCities" (CityA CityB)
			"getNumberTravelSourceDestinationMaxStops" (CityA CityB MaxStops)
			"getNumberTravelSourceDestinationWithStops" (CityA CityB Stops)
			"getShortestPathDistanceSourceDestination" (CityA CityB)
			"getNumberOfPathsSourceDestinationDistanceLessThan" (CityA CityB Distance)
->[PARAMS]
	It dependes of the method

Example of ejecution:

mvn exec:java -Dexec.mainClass="com.dridco.application.Application" -Dexec.args="AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7 getDistanceBetweenCities A B C"



