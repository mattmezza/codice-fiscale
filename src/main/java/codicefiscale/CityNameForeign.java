package codicefiscale;

/**
 * This class represents a name of a city in a foreign language.
 */
public class CityNameForeign {

    /**
     * The actual foreign name for the city.
     */
    private String cityNameForeign;

    /**
     * The foreign language of the name.
     */
    private ForeignLanguage foreignLanguage;

    /**
     * Creates an instance of a foreign language city name.
     * @param cityName The city name in a foreign language.
     * @param language The foreign language.
     */
    public CityNameForeign(String cityName, ForeignLanguage language) {
        this.cityNameForeign = cityName;
        this.foreignLanguage = language;
    }

    /**
     * Gives back the foreign name of the city.
     * @return The foreign name of the city.
     */
    public String getCityName() {
        return this.cityNameForeign;
    }

    /**
     * Return the foreign language of the city name.
     * @return The foreign language of the city name (e.g. German, French...).
     */
    public ForeignLanguage getForeignLanguage() {
        return this.foreignLanguage;
    }

    /**
     * The string representation of the CityNameForeign.
     */
    public String toString() {
        return this.cityNameForeign;
    }

    /**
     * Determines if two instances of CityNameForeign are equal.
     */
    public boolean equals(Object other) {
        if (other instanceof CityNameForeign)
            return (
                this.cityNameForeign.equals(
                    ((CityNameForeign) other).getCityName()
                )
                &&
                this.foreignLanguage.equals(
                    ((CityNameForeign) other).getForeignLanguage()
                )
            );
        else
            return false;
    }

    /**
     * This enum lists all the possible foreign language that a CityNameForeign
     * can have.
     */
    public enum ForeignLanguage {
        FRENCH,
        GERMAN,
    }
}