package codicefiscale;

/**
 * This class represents a normal Italian city.
 */
public class City extends PlaceOfBirth {

    /**
     * The original city name in Italian.
     */
    private String name;

    /**
     * The province of birth.
     */
    protected String province;
    
    /**
     * Creates an instance of a city.
     * @param name The name of the city.
     * @param code The ISTAT code.
     * @param province The province of the city.
     */
    public City(String name, Code code, String province) {
        this.name = name;
        this.code = code;

        if (province.length() == 2)
            this.province = province;
        else
            throw new RuntimeException(
                "A province of birth must be a two character string."
            );
    }

    /**
     * Returns the original city name.
     * @return The original city name in Italian.
     */
    public String getCityName() {
        return this.name;
    }

    @Override
    public String getProvinceOfBirth() {
        return this.province;
    }

    /**
     * Gives a string representation of a city.
     */
    public String toString() {
        return String.format(
            "%s (%s) %s", this.name, this.province, this.code
        );
    }
}