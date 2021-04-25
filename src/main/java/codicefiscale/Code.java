package codicefiscale;

/**
 * This class represents a code for a PlaceOfBirth.
 * This code is generated for each municipality and foreign country 
 * by the ISTAT (Italian Institute of Statistics).
 */
public class Code {

    /**
     * The actual 4 char code of the municipality.
     */
    private String code;

    /**
     * The maximum length that a code can have.
     */
    private static final short CODE_LENGTH = 4;
    
    /**
     * The message format of the error.
     */
    private static final String ERROR_MESSAGE_FORMAT = (
        "The code %s is not a valid TIN code."
    );

    /**
     * Creates an instance of an ISTAT code.
     * @param code The actual ISTAT code.
     */
    public Code(String code) {
        if (code.length() == Code.CODE_LENGTH)
            this.code = code;
        else
            throw new RuntimeException(
                String.format(Code.ERROR_MESSAGE_FORMAT, code)
            );
    }

    /**
     * Returns the actual code of this Code.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * A string representation of a Code.
     */
    public String toString() {
        return this.code;
    }

    /**
     * Determines if two instances of Code are equal.
     */
    public boolean equals(Object other) {
        if (other instanceof Code)
            return this.code.equals(((Code) other).getCode());
        else
            return false;
    }
}