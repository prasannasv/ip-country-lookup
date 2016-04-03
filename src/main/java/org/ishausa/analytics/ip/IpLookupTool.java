package org.ishausa.analytics.ip;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Subdivision;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;

/**
 * Hello world!
 *
 */
public class IpLookupTool {
    private final DatabaseReader cityInfo;

    public IpLookupTool(final DatabaseReader cityInfo) {
        this.cityInfo = cityInfo;
    }

    public static void main(String[] args) throws Exception {
        final InputStream cityDatabase = IpLookupTool.class.getResourceAsStream("/GeoLite2-City.mmdb");
        final DatabaseReader cityInfo = new DatabaseReader.Builder(cityDatabase).withCache(new CHMCache()).build();

        final IpLookupTool tool = new IpLookupTool(cityInfo);
        for (final String ipsFile : args) {
            final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ipsFile)));
            final String outputFileName = ipsFile + "-city-info.tsv";
            final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)));
            try {
                String ip;
                while ((ip = br.readLine()) != null) {
                    try {
                        final CityResponse response = tool.getCityResponse(ip);
                        final String cityName = response.getCity().getName();
                        final Subdivision stateInfo = response.getMostSpecificSubdivision();
                        final String country = response.getCountry().getName();
                        bw.write(String.format("%s\t%s\t%s\t%s\t%s", ip, cityName, stateInfo.getIsoCode(), stateInfo.getName(), country));
                        bw.newLine();
                    } catch (final IOException e) {
                        System.err.println("Ignoring unparseable ip: " + ip);
                    }
                }
            } finally {
                br.close();
                bw.close();
            }
        }
    }

    public CityResponse getCityResponse(final String ip) throws IOException, GeoIp2Exception {
        return cityInfo.city(InetAddress.getByName(ip));
    }
}
