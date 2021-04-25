package codicefiscale;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class holds information about the personal data of a citizen.
 */
public class Citizen {

    /**
     * The name of the citizen.
     */
    private String name;

    /**
     * The surname of the citizen.
     */
    private String surname;

    /**
     * The date of birth of the citizen.
     */
    private Date dateOfBirth;

    /**
     * The gender of the citizen.
     */
    private Gender gender;

    /**
     * The place of birth of the citizen.
     */
    private PlaceOfBirth placeOfBirth;

    /**
     * Creates an instance of a citizen object.
     * @param name The name of the citizen.
     * @param surname The surname of the citizen.
     * @param dateOfBirth The date of birth of the citizen.
     * @param gender The gender of the citizen.
     * @param placeOfBirth The place of birth of the citizen.
     */
    public Citizen(
        String name,
        String surname,
        Date dateOfBirth,
        Gender gender,
        PlaceOfBirth placeOfBirth
    ) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.placeOfBirth = placeOfBirth;
    }

    /**
     * Returns the name of the citizen.
     * @return The name of the citizen.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the surname of the citizen.
     * @return The surname of the citizen.
     */
    public String getSurname() {
        return this.surname;
    }

    /**
     * Returns the date of birth of the citizen.
     * @return The date of birth of the citizen.
     */
    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
     * Return the gender of the citizen.
     * @return The gender of the citizen.
     */
    public Gender getGender() {
        return this.gender;
    }

    /**
     * Returns the place of birth of the citizen.
     * @return The place of birth of the citizen.
     */
    public PlaceOfBirth getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    /**
     * Gives a string representaiton of a Citizen.
     */
    public String toString() {
        return String.format(
            "%s %s (%s), %s %s",
            this.name,
            this.surname,
            this.gender.equals(Gender.FEMALE) ? "F" : "M",
            new SimpleDateFormat("d/M/Y").format(this.dateOfBirth),
            this.placeOfBirth.toString()
        );
    }

    /**
     * Determines if two instances of Citizen are equal.
     */
    public boolean equals(Object other) {
        if (other instanceof Citizen) {
            Citizen citizen = (Citizen) other;
            return (
                this.name.equals(citizen.getName())
                && this.surname.equals(citizen.getSurname())
                && this.gender.equals(citizen.getGender())
                && this.dateOfBirth.equals(citizen.getDateOfBirth())
                && this.placeOfBirth.equals(citizen.getPlaceOfBirth())
            );
        } else {
            return false;
        }
    }

    /**
     * An enumeration for the gender of an Italian citizen.
     * Yep, you gotta be either a male or a female for the government
     * of the old boot.
     */
    public enum Gender {
        MALE,
        FEMALE,
    }
}