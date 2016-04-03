# Description
Find the high level location information (city, state &amp; country) given an IP.

# How to Run
1. Get the [GeoLite2-City database](http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz) and unzip it.
2. Place the unzipped file under `src/main/resources/GeoLite2-City.mmdb`
3. Import the source as a new project in IntelliJ Idea and run the main class `IpLookupTool` from there.

The alternative is to run 
`mvn package && java -cp ./target/ip-country-lookup-1.0-SNAPSHOT.jar org.ishausa.analytics.ip.IpLookupTool src/main/resources/sample.csv` but that needs all the runtime dependencies added to the classpath - I haven't done that yet.
