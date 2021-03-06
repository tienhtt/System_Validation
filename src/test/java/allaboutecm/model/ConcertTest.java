package allaboutecm.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ConcertTest {
    private Concert concert;

    @BeforeEach
    public void setup(){
        Set<Musician> performers = new HashSet<>();
        performers.add(new Musician("JACK DEJOHNETTE"));
        performers.add(new Musician("Ravi Coltrane"));
        performers.add(new Musician("Matt Garrison"));

        this.concert = new Concert(
                java.sql.Date.valueOf("2020-04-18"),
                performers,
                "Temple Israel Minneapolis",
                "Minneapolis, MN",
                "United States of America");
    }

    // if the date is null, the test passes
    @Test
    @DisplayName("Concert date cannot be null when setting")
    public void throwNullPointerExceptionIfSetConcertDateIsNull() {
        assertThrows(NullPointerException.class, () -> this.concert.setConcertDate(null));
    }

    // if the date is not in the future compared with today, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"2009-09-08", "2020-01-01", "2019-10-10"})
    @DisplayName("Concert date should be in the future")
    public void throwIllegalArgumentIfSetConcertDateIsNotInTheFuture(java.sql.Date arg) {
        assertThrows(IllegalArgumentException.class, () -> this.concert.setConcertDate(arg));
    }

    // if the date is in the future compared with today, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"2020-09-08", "2021-01-01", "2020-12-12"})
    @DisplayName("Concert date should be in the future")
    public void notThrowIllegalArgumentIfSetConcertDateIsInTheFuture(java.sql.Date arg) {
        assertDoesNotThrow(() ->this.concert.setConcertDate(arg));
    }

    // if the actual result is equal to the expected date, the test passes
    @ParameterizedTest
    @CsvSource({
            "2021-10-10, 2021-10-10",
            "2020-12-12, 2020-12-12",
            "2022-01-01, 2022-01-01",
    })
    @DisplayName("Should get the expected concert date")
    public void shouldReturnTheExpectedConcertDate(java.sql.Date arg, java.sql.Date expectedConcertDate) {
        this.concert.setConcertDate(arg);
        assertEquals(expectedConcertDate, this.concert.getConcertDate());
    }

    // if the concertPerformers Set is null when setting, the test passes
    @Test
    @DisplayName("concertPerformers Set cannot be null when setting")
    public void throwNullPointerExceptionIfSetConcertPerformersSetIsNullWhenSetting(){
        assertThrows(NullPointerException.class, () -> this.concert.setConcertPerformers(null));
    }

    // if the fansSitesURLs set is empty, the test passes
    @Test
    @DisplayName("There should be at least one fans sites URL when setting fansSitesURLs")
    public void throwIllegalArgExceptionIfFansSitesURLsSetIsEmptyWhenSetting(){
        Set<Musician> args = new HashSet<>();
        assertThrows(IllegalArgumentException.class, () -> this.concert.setConcertPerformers(args));
    }

    // if the actual result is equal to the set concertPerformers set, the test passes
    @Test
    @DisplayName("Should get the expected concertPerformers set ")
    public void shouldReturnTheExpectedConcertPerformersSet(){
        Set<Musician> performers = new HashSet<>();
        performers.add(new Musician("JACK DEJOHNETTE"));
        performers.add(new Musician("Ravi Coltrane"));
        performers.add(new Musician("Matt Garrison"));

        this.concert.setConcertPerformers(performers);
        assertEquals(performers, this.concert.getConcertPerformers());
    }

    // if the venue is null when setting, the test passes
    @Test
    @DisplayName("venue cannot be null when setting")
    public void throwNullPointerExceptionIfSetVenueIsNullWhenSetting(){
        assertThrows(NullPointerException.class, () -> this.concert.setVenue(null));
    }

    // if the venue is empty or the blank string, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t", "\n"})
    @DisplayName("venue cannot be empty or blank when setting")
    public void throwIllegalArgumentExceptionIfSetVenueIsEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> this.concert.setVenue(arg));
    }

    // if the actual result is equal to the expected set venue, the test passes
    @ParameterizedTest
    @CsvSource({
            "Temple Israel Minneapolis, Temple Israel Minneapolis",
            "Ordway Concert Hall, Ordway Concert Hall",
            "Stadthaus, Stadthaus",
    })
    @DisplayName("Should get the expected venue")
    public void shouldReturnTheExpectedVenue(String arg, String expectedVenue) {
        this.concert.setVenue(arg);
        assertEquals(expectedVenue, this.concert.getVenue());
    }

    // if the city is null when setting, the test passes
    @Test
    @DisplayName("city cannot be null when setting")
    public void throwNullPointerExceptionIfSetCityIsNullWhenSetting(){
        assertThrows(NullPointerException.class, () -> this.concert.setCity(null));
    }

    // if the city is empty or the blank string, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t", "\n"})
    @DisplayName("city cannot be empty or blank when setting")
    public void throwIllegalArgumentExceptionIfSetCityIsEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> this.concert.setCity(arg));
    }

    // if the city contains illegal character, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"city123", "123", "123city", "**#"})
    @DisplayName("city cannot contain illegal character when setting")
    public void throwIllegalArgumentExceptionIfSetCityContainsIllegalCharacter(String arg) {
        assertThrows(IllegalArgumentException.class, () -> this.concert.setCity(arg));
    }

    // if the city with space or hyphen contains illegal character, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"city123 good", "city123-good", "city good-city123"})  // add illegal character test case
    @DisplayName("city cannot contain numbers when setting")
    public void throwIllegalArgumentExceptionIfSetCityWithSpaceOrHyphenContainsIllegalCharacter(String arg) {
        assertThrows(IllegalArgumentException.class, () -> this.concert.setCity(arg));
    }

    // if the actual result is equal to the expected set city, the test passes
    @ParameterizedTest
    @CsvSource({
            "Wismar, Wismar",
            "Winterthur city, Winterthur city",
            "Hannover-city, Hannover-city",
    })
    @DisplayName("Should get the expected city")
    public void shouldReturnTheExpectedCity(String arg, String expectedCity) {
        this.concert.setCity(arg);
        assertEquals(expectedCity, this.concert.getCity());
    }

    // if the country is null when setting, the test passes
    @Test
    @DisplayName("country cannot be null when setting")
    public void throwNullPointerExceptionIfSetCountryIsNullWhenSetting(){
        assertThrows(NullPointerException.class, () -> this.concert.setCountry(null));
    }

    // if the country contains illegal character, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"country123", "123", "123country", "**#"}) // add illegal character test case
    @DisplayName("country cannot contain illegal character when setting")
    public void throwIllegalArgumentExceptionIfSetCountryContainsIllegalCharacter(String arg) {
        assertThrows(IllegalArgumentException.class, () -> this.concert.setCountry(arg));
    }

    // if the country with space or hyphen contains illegal character, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"country123 good", "country123-good", "country good-country123"})
    @DisplayName("country cannot contain numbers when setting")
    public void throwIllegalArgumentExceptionIfSetCountryWithSpaceOrHyphenContainsIllegalCharacter(String arg) {
        assertThrows(IllegalArgumentException.class, () -> this.concert.setCountry(arg));
    }

    // if the country is empty or the blank string, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t", "\n"})
    @DisplayName("country cannot be empty or blank when setting")
    public void throwIllegalArgumentExceptionIfSetCountryIsEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> this.concert.setCountry(arg));
    }

    // if the actual result is equal to the expected set country, the test passes
    @ParameterizedTest
    @CsvSource({
            "Spain, Spain",
            "United Kingdom, United Kingdom",
            "United-States, United-States",
    })
    @DisplayName("Should get the expected country")
    public void shouldReturnTheExpectedCountry(String arg, String expectedCountry) {
        this.concert.setCountry(arg);
        assertEquals(expectedCountry, this.concert.getCountry());
    }

    @AfterEach
    void tearDown() {
        concert = null;
    }
}