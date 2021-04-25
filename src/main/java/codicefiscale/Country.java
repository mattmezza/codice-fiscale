package codicefiscale;

/**
 * This class represents a foreign country as a place of birth.
 */
public class Country extends PlaceOfBirth {

    /**
     * A default value for the province of birth.
     */
    public static final String PROVINCE_DEFAULT = "EE";

    /**
     * The name of the foreign country.
     */
    private String name;

    /**
     * Creates an instance of a country object.
     * @param name The name of the country.
     * @param code The ISTAT code.
     */
    public Country(String name, Code code) {
        this.name = name;
        this.code = code;
    }

    /**
     * Return the name of the foreign country.
     * @return The name of the foreign country.
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String getProvinceOfBirth() {
        return Country.PROVINCE_DEFAULT;
    }

    /**
     * Returns a string representation of a Country instance.
     */
    public String toString() {
        return String.format(
            "%s (%s) %s",
            this.name,
            Country.PROVINCE_DEFAULT,
            this.code.toString()
        );
    }
}