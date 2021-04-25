package codicefiscale;


/**
 * This class represents a city located close to the border. Such cities,
 * in facts, often have more than one (Italian) name. Sometimes they are 
 * recognized by their German names, sometimes by their French names, etc...
 */
public class CityBorder extends City {

    /**
     * Holds the info about a foreign name for the city.
     * It's used especially for municipalities at the border.
     */
    private String cityNameForeign;

    /**
     * Creates an instance of a city of fronteer.
     * @param cityName The name of the city.
     * @param code The ISTAT code.
     * @param province The province of the city.
     * @param foreignName The city name in a foreign language.
     */
    public CityBorder(
        String cityName, Code code, String province, String foreignNames
    ) {
        super(cityName, code, province);
        this.cityNameForeign = foreignNames;
    }

    /**
     * Returns the foreign name of the border city.
     * @return A string representing the foreign name of the border city.
     */
    public String getCityNameForeign() {
        return this.cityNameForeign;
    }

    /**
     * Gives a String representation of a border city.
     */
    public String toString() {
        return String.format("%s, %s", super.toString(), this.cityNameForeign);
    }
}