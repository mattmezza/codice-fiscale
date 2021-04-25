package codicefiscale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import codicefiscale.Citizen.Gender;

/**
 * This class is the main interface with users of this library.
 * It provides methods to calculate a TIN code and to reverse it.
 * 
 * It also contains some private helpers that are not exposed to the users of
 * the library.
 */
public class LibTIN {

    /**
     * The set of char to consider as vowels.
     */
    private static final String ALL_VOWEL = "aeiou";
    
    /**
     * The chars corresponding to each month of birth.
     * e.g. Jan is index 0 (A), Feb is index 1 (B), etc...
     */
    private static final String ALL_MONTH_OF_BIRTH_CHAR = "ABCDEHLMNPRST";

    /**
     * Amount to be added to the day of birth gender pair when gender is F.
     */
    private static final int DAY_OF_BIRTH_FEMALE_SURPLUS = 40;

    /**
     * String containing the digits.
     * Used for calculating the control char.
     */
    private static final String DIGITS_EVEN_INDEX = "0123456789";
    
    /**
     * String containing the alphabet.
     * Used for calculating the control char.
     */
    private static final String LETTERS_EVEN_INDEX =
        "abcdefghijklmnopqrstuvwxyz";

    /**
     * Array containing the value to sum for the specific digit.
     * Used for calculating the control char.
     */
    private static final int[] DIGITS_ODD_VALUES = new int[] {
        1, 0, 5, 7, 9, 13, 15, 17, 19, 21,
    };

    /**
     * Array containing the value to sum for the specific letter.
     * Used for calculating the control char.
     */
    private static final int[] LETTERS_ODD_VALUES = new int[] {
        1, 0, 5, 7, 9, 13, 15, 17, 19,
        21, 2, 4, 18, 20, 11, 3, 6, 8,
        12, 14, 16, 10, 22, 25, 24, 23,
    };

    /**
     * The name of the the default list of place of birth codes included with
     * this library.
     */
    private static final String FILENAME_DEFAULT_PLACE_OF_BIRTH_LIST =
        "codes.csv";


    /**
     * This is used to separate the names of the place of birth between Italian
     * (first) and foreign language (second).
     * The foreign language could be any (French, German, etc...).
     */
    private static final String REGEX_FOREIGN_LANGUAGE_NAME_SEPARATOR =
        " \\* . ";

    /**
     * Loads the default list of place of birth included in this library into
     * a list. This is then used to calculate the TIN back and forth.
     * @return A list of place of birth.
     * @throws IOException When the default csv file cannot be read.
     */
    public static List<PlaceOfBirth> loadDefaultPlaceOfBirthList(
    ) throws IOException {
        BufferedReader csvReader = new BufferedReader(
            new InputStreamReader(
                LibTIN.class.getClassLoader().getResourceAsStream(
                    LibTIN.FILENAME_DEFAULT_PLACE_OF_BIRTH_LIST
                )
            )
        );
        List<PlaceOfBirth> placeOfBirthList = new ArrayList<>();
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            String name = data[0];
            String province = data[1];
            Code code = new Code(data[2]);

            if (name.matches(LibTIN.REGEX_FOREIGN_LANGUAGE_NAME_SEPARATOR)) {
                String[] allName = name.split(
                    LibTIN.REGEX_FOREIGN_LANGUAGE_NAME_SEPARATOR
                );
                String nameItalian = allName[0];
                String nameForeignLanguage = allName[1];
                placeOfBirthList.add(
                    new CityBorder(
                        nameItalian, code, province, nameForeignLanguage
                    )
                );
            } else
                if (province.toUpperCase().equals(Country.PROVINCE_DEFAULT))
                    placeOfBirthList.add(new Country(name, code));
                else
                    placeOfBirthList.add(new City(name, code, province));
        }
        csvReader.close();

        return placeOfBirthList;
    }

    /**
     * This function calculates a TIN from a citizen object.
     * @param citizen The citizen to calculate the TIN for.
     * @return The calculated TIN number.
     */
    public static TIN calculateTIN(Citizen citizen) {
        String surnameTriplet = LibTIN.determineTriplet(citizen.getSurname());
        String nameTriplet = LibTIN.determineTriplet(citizen.getName());
        String yearOfBirthPair = LibTIN.determineYearOfBirthPair(
            citizen.getDateOfBirth()
        );
        char monthOfBirthChar = LibTIN.determineMonthOfBirthChar(
            citizen.getDateOfBirth()
        );
        String dayOfBirthGenderPair = LibTIN.determineDayOfBirthGenderPair(
            citizen.getDateOfBirth(), citizen.getGender()
        );

        String code = (
            surnameTriplet
            + nameTriplet
            + yearOfBirthPair
            + monthOfBirthChar
            + dayOfBirthGenderPair
            + citizen.getPlaceOfBirth().getCode().toString()
        );
        char controlChar = LibTIN.determineControlChar(code);

        return new TIN(code + controlChar);
    }

    /**
     * Extracts the gender from a TIN code.
     * @param tin The TIN code.
     * @return The gender.
     */
    public static Gender getGender(TIN tin) {
        return (
            Integer.parseInt(tin.getCode().substring(9, 11)) > 31
        ) ? Gender.FEMALE : Gender.MALE;
    }

    /**
     * Extracts the date of birth from a TIN code.
     * @param tin The TIN code.
     * @return The date of birth.
     */
    public static Date getDateOfBirth(TIN tin) {
        char monthChar = tin.getCode().substring(8, 9).toUpperCase().charAt(0);
        int month = LibTIN.ALL_MONTH_OF_BIRTH_CHAR.indexOf(monthChar);
        int year = Integer.parseInt(tin.getCode().substring(6, 8));
        Calendar cal = Calendar.getInstance();
        int yearNow = Integer.parseInt(
            String.valueOf(cal.get(Calendar.YEAR)).substring(2, 4)
        );

        if (year >= yearNow)
            year += 1900;
        else
            year += 2000;
        
        int day = Integer.parseInt(tin.getCode().substring(9, 11));
        cal.set(year, month, day);
        Instant instant = LocalDate.of(year, month + 1, day).atStartOfDay(
            ZoneId.systemDefault()
        ).toInstant();

        return Date.from(instant);
    }

    /**
     * Extracts the place of birth from a full TIN code.
     * @param tin The TIN code.
     * @param allPlaceOfBirth A list of all the possible place of birth.
     * @return The place of birth.
     * @throws RuntimeException When the provided TIN contains an unknown code.
     */
    public static PlaceOfBirth getPlaceOfBirth(
        TIN tin, List<PlaceOfBirth> allPlaceOfBirth
    ) {
        Code placeOfBirthCode = new Code(tin.getCode().substring(11, 15));
        
        for (PlaceOfBirth placeOfBirth : allPlaceOfBirth) {
            if (placeOfBirth.getCode().equals(placeOfBirthCode))
                return placeOfBirth;
        }

        throw new RuntimeException(
            String.format("Place of birth %s not found.", placeOfBirthCode)
        );
    }

    /**
     * Determines a TIN triplet from a string.
     * @param name The string representing a name or a surname.
     * @return The determined TIN triplet.
     */
    private static String determineTriplet(String name) {
        name = name.replace(" ", "").toUpperCase();
        String consonants = LibTIN.determineConsonants(name);
        String vowels = LibTIN.determineVowels(name);

        if (consonants.length() == 0)
            if (vowels.length() == 0)
				return "xxx";
			else if (vowels.length() == 1)
				return vowels + "xx";
			else if (vowels.length() == 2)
				return vowels + "x";
			else
				return vowels.substring(0, 3);
        else if (consonants.length() == 1)
            if (vowels.length() == 1)
                return consonants + vowels + "x";
            else
                return consonants + vowels.substring(0, 2);
        else if (consonants.length() == 2)
            if (vowels.length() > 0)
                return consonants + vowels.substring(0, 1);
            else
                return consonants + "x";
        else
            return consonants.substring(0, 3);
    }

    /**
     * Determines the consonants in a given string.
     * @param word The string with which to determine consonants.
     * @return A string containing only the consonants char.
     */
    private static String determineConsonants(String word) {
		word = word.toLowerCase();
		String consonants = "";
		for (char character : word.toCharArray())
            if (LibTIN.ALL_VOWEL.indexOf(character) == -1)
                consonants += character;

		return consonants;
	}

    /**
     * Determines the vowels in a given string.
     * @param word The string with which to determine vowels.
     * @return A string containing only the vowels char.
     */
	private static String determineVowels(String word) {
        word = word.toLowerCase();
        String vowels = "";

		for (char character : word.toCharArray())
            if (LibTIN.ALL_VOWEL.indexOf(character) >= 0)
                vowels += character;

		return vowels;
    }
    
    /**
     * Returns the short version of the year of a date.
     * @param dateOfBirth The date object from which to extract the year.
     * @return The short two chars version of the year of a date.
     */
    private static String determineYearOfBirthPair(Date dateOfBirth) {
        return new SimpleDateFormat("yy").format(dateOfBirth);
    }

    /**
     * Finds the characted corresponding to the month of birth.
     * @param dateOfBirth The date to extract the month index from.
     * @return A char corresponding to the month of birth.
     */
    private static char determineMonthOfBirthChar(Date dateOfBirth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOfBirth);

        return LibTIN.ALL_MONTH_OF_BIRTH_CHAR.charAt(
            calendar.get(Calendar.MONTH)
        );
    }

    /**
     * Finds the two character pair from the gender and day of birth of the 
     * citizen.
     * @param dateOfBirth The date of birth to use.
     * @param gender The gender of the citizen.
     * @return The two character pair.
     */
    private static String determineDayOfBirthGenderPair(
        Date dateOfBirth, Gender gender
    ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOfBirth);

        int dayOfBirth = calendar.get(Calendar.DAY_OF_MONTH);

        if (gender.equals(Gender.FEMALE))
            dayOfBirth += LibTIN.DAY_OF_BIRTH_FEMALE_SURPLUS;
        
        return String.format("%02d", dayOfBirth);
    }

    /**
     * Determines the control char for a given partial code.
     * @param code The partial TIN code.
     * @return The determined control char.
     */
    private static char determineControlChar(String code) {
        code = code.toLowerCase();
        short sumEven = 0;
        short sumOdd = 0;
        
        for (int i = 0; i < code.length(); i++) {
            char character = code.charAt(i);
            int indexOfDigits = LibTIN.DIGITS_EVEN_INDEX.indexOf(character);
            int indexOfLetters = LibTIN.LETTERS_EVEN_INDEX.indexOf(character);
            boolean isEven = i % 2 == 1 ? true : false;

            if (indexOfDigits > 0)
                if (isEven)
                    sumEven += indexOfDigits;
                else
                    sumOdd += LibTIN.DIGITS_ODD_VALUES[indexOfDigits];
            else
                if (isEven)
                    sumEven += indexOfLetters;
                else
                    sumOdd += LibTIN.LETTERS_ODD_VALUES[indexOfLetters];
        }

        int controlInteger = (sumEven + sumOdd) % 26;

        return LibTIN.LETTERS_EVEN_INDEX.charAt(controlInteger);
    }
}