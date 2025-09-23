package translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes (alpha-3, lowercase)
 * to their names and back.
 */
public class CountryCodeConverter {

    private final Map<String, String> countryCodeToCountry = new HashMap<>();
    private final Map<String, String> countryToCountryCode = new HashMap<>();

    /**
     * Default constructor that loads the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor that allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resources file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            Iterator<String> iterator = lines.iterator();
            iterator.next(); // skip header
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] parts = line.split("\t");

                String countryName = parts[0].trim();
                String countryCode = parts[2].trim().toLowerCase(); // Alpha-3, lowercase

                countryCodeToCountry.put(countryCode, countryName);
                countryToCountryCode.put(countryName, countryCode);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Return the name of the country for the given alpha-3 code. */
    public String fromCountryCode(String code) {
        return countryCodeToCountry.get(code.toLowerCase());
    }

    /** Return the alpha-3 code of the country for the given country name. */
    public String fromCountry(String country) {
        return countryToCountryCode.get(country);
    }

    /** Return all country names (for use in GUI list). */
    public List<String> getAllCountryNames() {
        return new ArrayList<>(countryToCountryCode.keySet());
    }

    /** Return the ISO alpha-3 code for a given country name. */
    public String getCountryCode(String countryName) {
        return countryToCountryCode.get(countryName);
    }

    /** Return the country name for a given ISO alpha-3 code. */
    public String getCountryName(String countryCode) {
        return countryCodeToCountry.get(countryCode.toLowerCase());
    }

    /** Return how many countries are included. */
    public int getNumCountries() {
        return countryCodeToCountry.size();
    }
}
