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
 * This class provides the services of:
 * - converting language codes to their names
 * - converting language names to their codes
 */
public class LanguageCodeConverter {

    private final Map<String, String> languageCodeToLanguage = new HashMap<>();
    private final Map<String, String> languageToLanguageCode = new HashMap<>();

    /**
     * Default constructor that loads the language codes from "language-codes.txt"
     * in the resources folder.
     */
    public LanguageCodeConverter() {
        this("language-codes.txt");
    }

    /**
     * Overloaded constructor that allows us to specify the filename to load the language code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resources file can't be loaded properly
     */
    public LanguageCodeConverter(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            Iterator<String> iterator = lines.iterator();
            iterator.next(); // skip header
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String languageName = parts[0].trim();
                    String code = parts[1].trim();
                    languageCodeToLanguage.put(code, languageName);
                    languageToLanguageCode.put(languageName, code);
                }
            }
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Return the name of the language for a given code. */
    public String fromLanguageCode(String code) {
        return languageCodeToLanguage.get(code);
    }

    /** Return the code of the language for a given name. */
    public String fromLanguage(String language) {
        return languageToLanguageCode.get(language);
    }

    /** Return all language names (for use in GUI dropdown). */
    public List<String> getAllLanguageNames() {
        return new ArrayList<>(languageToLanguageCode.keySet());
    }

    /** Return the code for a given language name. */
    public String getLanguageCode(String languageName) {
        return languageToLanguageCode.get(languageName);
    }

    /** Return the name for a given language code. */
    public String getLanguageName(String code) {
        return languageCodeToLanguage.get(code);
    }

    /** Return how many languages are included. */
    public int getNumLanguages() {
        return languageCodeToLanguage.size();
    }
}
