package allaboutecm.model;

import com.google.common.collect.Sets;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlbumUnitTest {
    private Album album;

    @BeforeEach
    public void setUp() {
        album = new Album(1975, "ECM 1064/65", "The Köln Concert");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1968, 2021, 3000})
    @DisplayName("Release year of an album must be a whole number between 0 and the current year (inclusive).")
    public void releaseYearGreaterThanZeroLessThanEqualCurrentYear(int arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setReleaseYear(arg));
    }

    @ParameterizedTest
    @ValueSource(ints = {1969, 1970, 2000, 2019, 2020})
    @DisplayName("Release year is set and returned as expected.")
    public void releaseYearSetAsExpected(int arg) {
        album.setReleaseYear(arg);
        assertEquals(arg, album.getReleaseYear());
    }

    @Test
    @DisplayName("Record number cannot be null.")
    public void recordNumberCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setRecordNumber(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Record number cannot be empty or blank")
    public void recordNumberCannotBeBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setRecordNumber(arg));
    }

    // add "ECM ECM" for isNumeric; add "2009 2009" for isAlpha
    @ParameterizedTest
    @ValueSource(strings = {"EC ECM 2009", "2009 ECM", "ECM2009", "2009", "ECM  2009", "ECM ECM", "2009 2009"})
    @DisplayName("Record number must letters followed by a single whitespace, followed by numbers.")
    public void recordNumberFormatMustBeCorrect(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setRecordNumber(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ECM 2009", "Label 1111", "Record 101010"})
    @DisplayName("Record number is set and returned as expected.")
    public void recordNumberIsSetAsExpected(String arg) {
        album.setRecordNumber(arg);
        assertEquals(arg, album.getRecordNumber());
    }

    @Test
    @DisplayName("Album name cannot be null")
    public void albumNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setAlbumName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Album name cannot be empty or blank")
    public void albumNameCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setAlbumName(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Life Goes On", "Angular Blues", "Love of Life"})
    @DisplayName("Album name should be set and returned as expected.")
    public void albumNameSetAndReturnedAsExpected(String arg) {
        album.setAlbumName(arg);
        assertEquals(arg, album.getAlbumName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"This name is about 50 characters in length exactly"})
    @DisplayName("Should throw illegal argument exception and returned as expected.")
    public void throwIllegalArgExceptionIfNameTooLong(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setAlbumName(arg));
    }

    @Test
    @DisplayName("Tracks cannot be null.")
    public void tracksListCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setTracks(null));
    }

    @Test
    @DisplayName("The list of tracks cannot be empty.")
    public void tracksListCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> album.setTracks(Sets.newHashSet()));
    }

    @Test
    @MethodSource
    @DisplayName("No element in the list of tracks can be null.")
    public void individualTracksCannotBeNull() {
        Set<Track> args = Sets.newHashSet();
        args.add(new Track(1, "Overcome", 243));
        args.add(null);
        args.add(new Track(2, "Home", 250));
        assertThrows(NullPointerException.class, () -> album.setTracks(args));
    }

    @Test
    @MethodSource
    @DisplayName("No element in the list of tracks can be null.")
    public void tracksSetAsExpected() {
        Set<Track> args = Sets.newHashSet();
        args.add(new Track(1, "Overcome", 243));
        args.add(new Track(2, "Home", 250));
        album.setTracks(args);

        assertEquals(args, album.getTracks());
    }

    @Test
    @DisplayName("The instruments list cannot be null")
    public void throwNullPointerExceptionIfNoInstrumentList() {
        assertThrows(NullPointerException.class, () -> album.setInstruments(null));
    }

    @Test
    @DisplayName("Album should have at least one MusicianInstrument.")
    public void throwIllegalArgExceptionIfInstrumentsSetEmpty() {
        Set<MusicianInstrument> args = new HashSet<>();
        assertThrows(IllegalArgumentException.class, () -> album.setInstruments(args));
    }

    @Test
    @DisplayName("Instruments set as expected.")
    public void instrumentsSetAsExpected() {
        Set<MusicianInstrument> args = new HashSet<>();
        Set<MusicalInstrument> musicalInstruments = Sets.newHashSet();
        musicalInstruments.add(new MusicalInstrument("Guitar"));
        MusicianInstrument instrument = new MusicianInstrument(new Musician("Paul McCartney"), musicalInstruments);
        args.add(instrument);
        album.setInstruments(args);

        assertEquals(args, album.getInstruments());
    }

    @Test
    @DisplayName("FeaturedMusicians cannot be null")
    public void throwNullPointerExceptionIfFeaturedMusiciansNull() {
        assertThrows(NullPointerException.class, () -> album.setFeaturedMusicians(null));
    }

    @Test
    @DisplayName("Album should have at least one Featured Musician")
    public void throwIllegalArgExceptionIfFeaturedMusiciansSetEmpty() {
        List<Musician> args = Lists.newArrayList();
        assertThrows(IllegalArgumentException.class, () -> album.setFeaturedMusicians(args));
    }

    @Test
    @DisplayName("Featured musicians should not contain any null Musicians")
    public void throwNullPointerExceptionIfFeaturedMusicianNull() {
        Musician musician = null;
        List<Musician> args = Lists.newArrayList();
        args.add(musician);
        assertThrows(NullPointerException.class, () -> album.setFeaturedMusicians(args));
    }

    @Test
    @DisplayName("No duplicate musicians should appear in the set")
    public void throwIllegalArgExceptionIfDuplicateMusicians(){
        Musician m1 = new Musician("Jon Denver");
        Musician m2 = new Musician("Neil Diamond");
        Musician m3 = new Musician("Jon Denver");

        List<Musician> args = Lists.newArrayList();
        args.add(m1);
        args.add(m2);
        args.add(m3);

        assertThrows(IllegalArgumentException.class, () -> album.setFeaturedMusicians(args));
    }

    @Test
    @DisplayName("Featured musicians should be set as expected.")
    public void featuredMusiciansSetAsExpected() {
        List<Musician> args = Lists.newArrayList();
        args.add(new Musician("Paul McCartney"));
        album.setFeaturedMusicians(args);

        assertEquals(args, album.getFeaturedMusicians());
    }

    @Test
    @DisplayName("Provided album URL should not be null.")
    public void nullPointerExceptionIfAlbumURLIsNull() {
        assertThrows(NullPointerException.class, () -> album.setAlbumURL(null));
    }

    @ParameterizedTest
    @CsvSource({
            "https://www.ecmrecords.com/artists/1435046707/michael-cain/",
            "https://www.ecmrecords.com/artists/1435047312/vox-clamantis/",
            "https://www.ecmrecords.com/artists/1443513226/anthony-de-mare/"
    })

    @DisplayName("Album URL should be set as expected")
    public void albumURLSetAsExpected(URL arg) {
        album.setAlbumURL(arg);
        assertEquals(arg, album.getAlbumURL());
    }

    @Test
    @DisplayName("Genre cannot be null.")
    public void genreCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setGenre(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Genre cannot be empty or blank")
    public void genreCannotBeBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setGenre(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Rock", "Hip Hop", "Classical"})
    @DisplayName("Genre should be set and returned as expected.")
    public void genreIsSetAndReturnedAsExpected(String arg) {
        album.setGenre(arg);
        assertEquals(arg, album.getGenre());
    }

    @Test
    @DisplayName("Format should not be null.")
    public void formatShouldNotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setFormat(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Single", "L P", "CDD"})
    @DisplayName("Format can only be a format recognised by ECM.")
    public void providedFormatShouldBeValidEcmFormat(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setFormat(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"CD", "LP", "DVD", "Bluray"})
    @DisplayName("Format should be set and returned as expected.")
    public void formatSetAndReturnedAsExpected(String arg) {
        album.setFormat(arg);
        assertEquals(arg, album.getFormat());
    }

    @Test
    @DisplayName("The provided musician group cannot be null.")
    public void throwNullPointerExceptionIfGroupMusiciansNull(){
        assertThrows(NullPointerException.class, () -> album.setMusicianGroup(null));
    }

    @Test
    @DisplayName("Group musicians should be set as expected.")
    public void groupMusiciansSetAsExpected(){
        MusicianGroup arg = new MusicianGroup("Jan Garbarek Quartet");
        album.setMusicianGroup(arg);

        assertEquals(arg,album.getMusicianGroup());
    }

    @ParameterizedTest
    @ValueSource(ints ={-1, -20})
    @DisplayName("Units sold throws illegalArgumentException when invalid number provided")
    public void throwsIllegalArgExceptionWhenInvalidUnitsSoldEntered(int arg){
        assertThrows(IllegalArgumentException.class, () -> album.setUnitsSold(arg));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 999999999, 100000000})
    @DisplayName("Units sold is set and returned as expected")
    public void unitsSoldSetAsExpected(int arg){
        album.setUnitsSold(arg);
        assertEquals(arg,album.getUnitsSold());
    }

    @Test
    public void sameNameAndNumberMeansSameAlbum() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        assertEquals(album, album1);
    }

    @Test
    @DisplayName("Ratings should not be null")
    public void shouldThrowNullPointerExceptionIfRatingsNull(){
        assertThrows(NullPointerException.class, () -> album.setRatings(null));
    }

    @Test
    @DisplayName("No element in the ratings set should not be null")
    public void shouldThrowNullPointerExceptionIfAnyRatingIsNull(){
        Set ratingSet = new HashSet<Rating>();
        Rating rating = new Rating();
        rating.setSource("Example source");
        rating.setComment("Example comment");
        rating.setScore(9);

        ratingSet.add(rating);
        ratingSet.add(null);

        assertThrows(NullPointerException.class, () -> album.setRatings(ratingSet));
    }

    @Test
    @DisplayName("Ratings should be set and returned as expected")
    public void ratingsSetAsExpected(){
        Set ratingSet = new HashSet<Rating>();
        Rating rating = new Rating();
        rating.setSource("Example source");
        rating.setComment("Example comment");
        rating.setScore(9);

        album.setRatings(ratingSet);
        assertEquals(ratingSet,album.getRatings());
    }

    @AfterEach
    void tearDown() {
        album = null;
    }
}