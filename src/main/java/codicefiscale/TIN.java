package codicefiscale;

/**
 * This class represents an Italian Taxpayer Identification Number 
 * as per governmental definition.
 */
public class TIN {

    /**
     * The 16-char string representing the actual code.
     */
    private String code;

    /**
     * The actual length of any Italian TIN.
     */
    private static final short CODE_LENGTH = 16;

    /**
     * Creates an instance of a TIN.
     * @param code The actual TIN code.
     */
    public TIN(String code) {
        if (code.length() == TIN.CODE_LENGTH)
            this.code = code;
        else
            throw new RuntimeException(
                String.format(
                    String.join(
                        "",
                        "The TIN code must be of %i characters, ",
                        "given code of length %i instead."
                    ),
                    TIN.CODE_LENGTH,
                    code.length()
                )
            );
    }

    /**
     * Returns the actual string version of the TIN.
     * @return The TIN string.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Return a string representation of a TIN.
     */
    public String toString() {
        return this.getCode().toUpperCase();
    }

    /**
     * Determines if two instances of TIN are equal.
     */
    public boolean equals(Object other) {
        if (other instanceof TIN)
            return this.code.equalsIgnoreCase(((TIN) other).getCode());
        else
            return false;
    }
}