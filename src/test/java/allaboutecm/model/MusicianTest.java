package allaboutecm.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Haoyu Yang
 * this test class is for Musician class
 */

class MusicianTest {
    private Musician musician;

    @BeforeEach
    public void setup() {
        this.musician = new Musician("Keith Jarrett");
    }

    // if the actual result is equal to the expected set name, the test passes
    @ParameterizedTest
    @CsvSource({
            "Frances-Marie Uitti, Frances-Marie Uitti",
            "Keith Jarrett, Keith Jarrett",
            "hoy yang, Hoy Yang",
            "CHRISTOPHER BOWERS-BROADBENT, Christopher Bowers-Broadbent"
    })
    @DisplayName("Should get the expected musician name ")
    public void shouldReturnTheExpectedMusicianName(String arg, String expectedName) {
        this.musician.setName(arg);
        assertEquals(expectedName, this.musician.getName());
    }

    // if the musician name is null, the test passes
    @Test
    @DisplayName("Musician name cannot be null when setting")
    public void throwNullPointerExceptionIfSetMusicianNameIsNull() {
        assertThrows(NullPointerException.class, () -> this.musician.setName(null));
        assertThrows(NullPointerException.class, () -> new Musician(null));
    }

    // if the musician name is empty or the blank string, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t", "\n"})
    @DisplayName("Musician name cannot be empty or blank when setting")
    public void throwIllegalArgumentExceptionIfSetMusicianNameIsEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> this.musician.setName(arg));
        assertThrows(NullPointerException.class, () -> new Musician(null));
    }

    // if the musician name contains the characters that are not alphabetical, the test passes
    // add test case "Frances123-Marie Uitti" for "isAlpha()" in line 194 of the source code
    @ParameterizedTest
    @ValueSource(strings = {"Keith 1234", "*sss #@!", "^^&&1234", "Mike123 Jone", "Frances123-Marie Uitti"})
    @DisplayName("Musician name should only be alphabetical when setting")
    public void throwIllegalArgumentExceptionIfSetMusicianNameIsNotAlphabeticalExceptHyphen(String arg) {
        assertThrows(IllegalArgumentException.class, () -> this.musician.setName(arg));
        assertThrows(NullPointerException.class, () -> new Musician(null));
    }

    // if the musician name contains hyphen, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"Frances-Marie Uitti", "Keith Jarrett", "Christopher Bowers-Broadbent"})
    @DisplayName("Musician name that contains the hyphen should be valid")
    public void notThrowExceptionIfSetMusicianNameContainsHyphen(String arg){
        assertDoesNotThrow(() ->this.musician.setName(arg));
        assertThrows(NullPointerException.class, () -> new Musician(null));
    }

    //if the musician name is not separated by space, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"KeithJarrett", " KeithJarrett  ", "Keith-Jarrett"})
    @DisplayName("Musician name should be separated by space when setting")
    public void throwIllegalArgumentExceptionIfSetMusicianNameIsNotSeparatedBySpace(String arg) {
        assertThrows(IllegalArgumentException.class, () -> musician.setName(arg));
        assertThrows(NullPointerException.class, () -> new Musician(null));
    }


    // if the actual result is equals to the expected one, the test passes
    @ParameterizedTest
    @ValueSource(strings = {"jean-louis matinier", "JEAN-LOUIS MATINIER", "jeaN-lOuIs maTiniER"})
    @DisplayName("The first letter of each the separated musician name should be uppercase and the rests are lowercase after setting.")
    public void testMusicianNameShouldBeValidCase(String arg){
        this.musician.setName(arg);
        assertEquals("Jean-Louis Matinier", this.musician.getName());
    }


    // if the musician url is null when setting, the test passes
    @Test
    @DisplayName("Musician url cannot be null when setting")
    public void throwNullPointerExceptionIfSetMusicianUrlIsNullWhenSetting(){
        assertThrows(NullPointerException.class, () -> this.musician.setMusicianUrl(null));
    }

    // if the actual result is equal to the set musician URL, the test passes
    @ParameterizedTest
    @CsvSource({
            "https://www.ecmrecords.com/artists/1435046707/michael-cain/, https://www.ecmrecords.com/artists/1435046707/michael-cain/",
            "https://www.ecmrecords.com/artists/1435047312/vox-clamantis/, https://www.ecmrecords.com/artists/1435047312/vox-clamantis/",
            "https://www.ecmrecords.com/artists/1443513226/anthony-de-mare/, https://www.ecmrecords.com/artists/1443513226/anthony-de-mare/"
    })
    @DisplayName("Should get the expected musician URL ")
    public void shouldReturnTheExpectedMusicianURL(URL arg, URL expectedURL){
        this.musician.setMusicianUrl(arg);
        assertEquals(expectedURL, this.musician.getMusicianUrl());
    }

    // if the musician Album Set is null when setting, the test passes
    @Test
    @DisplayName("Musician Album Set cannot be null when setting")
    public void throwNullPointerExceptionIfSetMusicianAlbumSetIsNullWhenSetting(){
        assertThrows(NullPointerException.class, () -> this.musician.setAlbums(null));
    }

    // if the album set is empty, the test passes
    @Test
    @DisplayName("There should be at least one album when setting albums")
    public void throwIllegalArgExceptionIfAlbumSetIsEmptyWhenSetting(){
        Set<Album> args = new HashSet<>();
        assertThrows(IllegalArgumentException.class, () -> this.musician.setAlbums(args));
    }

    // if the actual result is equal to the set album set, the test passes
    @Test
    @DisplayName("Should get the expected album set ")
    public void shouldReturnTheExpectedAlbumSet(){
        Set<Album> albums = new HashSet<>();
        albums.add(new Album(2020, "ECM 2676", "HERE BE DRAGONS"));
        albums.add(new Album(2018, "ECM 2511", "IN CONCERT - BEETHOVEN / LISZT"));
        albums.add(new Album(2010, "ECM 2111", "THOMAS LARCHER: MADHARES"));
        this.musician.setAlbums(albums);
        assertEquals(albums, this.musician.getAlbums());
    }

    // if the musician biography is null, the test passes
    @Test
    @DisplayName("Musician biography cannot be null when setting")
    public void throwNullPointerExceptionIfSetMusicianBiographyIsNull() {
        assertThrows(NullPointerException.class, () -> this.musician.setMusicianBiography(null));
    }

    // if the actual result is equal to the set musician biography, the test passes
    @ParameterizedTest
    @CsvSource({
            "He is excellent, He is excellent",
            "nice musician, nice musician",
            "His fans love him, His fans love him",
    })
    @DisplayName("Should get the expected musician biography")
    public void shouldReturnTheExpectedMusicianBiography(String arg, String expectedBirography){
        this.musician.setMusicianBiography(arg);
        assertEquals(expectedBirography, this.musician.getMusicianBiography());
    }

    // if the musician Wikipedia url is null when setting, the test passes
    @Test
    @DisplayName("Musician Wikipedia url cannot be null when setting")
    public void throwNullPointerExceptionIfSetMusicianWikipediaUrlIsNullWhenSetting(){
        assertThrows(NullPointerException.class, () -> this.musician.setMusicianWikipediaURL(null));
    }

    // if the actual result is equal to the set musician Wikipedia URL, the test passes
    @ParameterizedTest
    @CsvSource({
            "https://en.wikipedia.org/wiki/Carla_Bley, https://en.wikipedia.org/wiki/Carla_Bley",
            "https://en.wikipedia.org/wiki/Sidsel_Endresen, https://en.wikipedia.org/wiki/Sidsel_Endresen",
            "https://en.wikipedia.org/wiki/Elina_Duni, https://en.wikipedia.org/wiki/Elina_Duni"
    })
    @DisplayName("Should get the expected musician Wikipedia URL ")
    public void shouldReturnTheExpectedMusicianWikipediaURL(URL arg, URL expectedURL){
        this.musician.setMusicianWikipediaURL(arg);
        assertEquals(expectedURL, this.musician.getMusicianWikipediaURL());
    }

    // if the musician fansSitesURLs Set is null when setting, the test passes
    @Test
    @DisplayName("Musician fansSitesURLs Set cannot be null when setting")
    public void throwNullPointerExceptionIfSetFansSitesURLsSetIsNullWhenSetting(){
        assertThrows(NullPointerException.class, () -> this.musician.setFansSitesURLs(null));
    }

    // if the fansSitesURLs set is empty, the test passes
    @Test
    @DisplayName("There should be at least one fans sites URL when setting fansSitesURLs")
    public void throwIllegalArgExceptionIfFansSitesURLsSetIsEmptyWhenSetting(){
        Set<URL> args = new HashSet<>();
        assertThrows(IllegalArgumentException.class, () -> this.musician.setFansSitesURLs(args));
    }

    // if the actual result is equal to the set fansSitesURLs set, the test passes
    @Test
    @DisplayName("Should get the expected fansSitesURLs set ")
    public void shouldReturnTheExpectedFansSitesURLsSet() throws MalformedURLException {
        Set<URL> fansSitesURLs = new HashSet<>();
        fansSitesURLs.add(new URL("https://www.ecmrecords.com/artists/1435046707/michael-cain/"));
        fansSitesURLs.add(new URL("https://www.ecmrecords.com/artists/1435047312/vox-clamantis/"));
        fansSitesURLs.add(new URL("https://www.ecmrecords.com/artists/1443513226/anthony-de-mare/"));

        this.musician.setFansSitesURLs(fansSitesURLs);
        assertEquals(fansSitesURLs, this.musician.getFansSitesURLs());
    }

    // if the musician concerts Set is null when setting, the test passes
    @Test
    @DisplayName("Musician concerts Set cannot be null when setting")
    public void throwNullPointerExceptionIfSetConcertsSetIsNullWhenSetting(){
        assertThrows(NullPointerException.class, () -> this.musician.setConcerts(null));
    }

    // if the concerts Set is empty, the test passes
    @Test
    @DisplayName("There should be at least one concert when setting concerts")
    public void throwIllegalArgExceptionIfConcertsSetIsEmptyWhenSetting(){
        Set<Concert> args = new HashSet<>();
        assertThrows(IllegalArgumentException.class, () -> this.musician.setConcerts(args));
    }

    // if the actual result is equal to the set concerts set, the test passes
    @Test
    @DisplayName("Should get the expected concerts set ")
    public void shouldReturnTheExpectedConcertsSet(){
        Set<Concert> concerts = new HashSet<>();

        Set<Musician> performers = new HashSet<>();
        performers.add(new Musician("JACK DEJOHNETTE"));
        performers.add(new Musician("Ravi Coltrane"));
        performers.add(new Musician("Matt Garrison"));

        Concert concert1 = new Concert(
                java.sql.Date.valueOf("2020-04-18"),
                performers,
                "Temple Israel Minneapolis",
                "Minneapolis, MN",
                "United States of America");
        concerts.add(concert1);

        Concert concert2 = new Concert(
                java.sql.Date.valueOf("2020-05-26"),
                performers,
                "SF Jazz, Miner Auditorium",
                "San Francisco, CA",
                "United States of America");
        concerts.add(concert2);

        this.musician.setConcerts(concerts);
        assertEquals(concerts, this.musician.getConcerts());
    }

    @AfterEach
    void tearDown() {
        musician = null;
    }
}