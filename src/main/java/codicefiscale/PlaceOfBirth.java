package codicefiscale;

/**
 * This class represents a place of birth for the ISTAT.
 */
public abstract class PlaceOfBirth {

    /**
     * The actual code for this place of birth.
     */
    protected Code code;

    /**
     * Returns the code of this city.
     * @return The code of the city.
     */
    public Code getCode() {
        return this.code;
    }

    /**
     * Returns the province of birth.
     * @return A string representing a province of birth.
     */
    public abstract String getProvinceOfBirth();
    
    /**
     * Determines if two instances of PlaceOfBirth are equal.
     */
    public boolean equals(Object other) {
        if (other instanceof PlaceOfBirth)
            return this.code.equals(((PlaceOfBirth) other).getCode());
        else
            return false;
    }
}