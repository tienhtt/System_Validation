package allaboutecm.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TrackUnitTest {
    private Track track;

    @BeforeEach
    void setUp() {
        track = new Track(1, "Overcome", 243);
        String[] composers = {"David Neuve"};
        track.setComposers(composers);
    }

    @Test
    @DisplayName("Track should return same title as expected value")
    public void trackTitleIsValidAsExpectation() {
        assertEquals("Overcome", track.getTitle());
    }

    @Test
    @DisplayName("Track should return same track number as expected value")
    public void trackNumberIsValidAsExpectation() {
        assertEquals(1, track.getTrackNumber());
    }

    @Test
    @DisplayName("Track should return same duration as expected value")
    public void trackDurationIsValidAsExpectation() {
        assertEquals(243, track.getDuration());
    }

    @Test
    @DisplayName("Track should return same composers as expected value")
    public void trackComposersIsValidAsExpectation() {
        String[] expectedValue = {"David Neuve"};
        assertArrayEquals(expectedValue, track.getComposers());
    }

    @Test
    @DisplayName("Track title cannot be null")
    public void trackTitleCannotBeNull() {
        assertThrows(NullPointerException.class, () -> track.setTitle(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Track title cannot be empty or blank")
    public void trackTitleCannotBeBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> this.track.setTitle(arg));
    }

    @ParameterizedTest
    @ValueSource(ints = {0})
    @DisplayName("Track number cannot be equal 0")
    public void trackNumberCannotBeEqualZero(int arg) {
        assertThrows(IllegalArgumentException.class, () -> this.track.setTrackNumber(arg));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-3,-4,-100})
    @DisplayName("Track number cannot be equal 0")
    public void trackNumberCannotBeLessThanZero(int arg) {
        assertThrows(IllegalArgumentException.class, () -> this.track.setTrackNumber(arg));
    }

    @ParameterizedTest
    @ValueSource(longs = {0})
    @DisplayName("Track duration cannot be equal 0")
    public void trackDurationCannotBeEqualZero(long arg) {
        assertThrows(IllegalArgumentException.class, () -> this.track.setDuration(arg));
    }

    @ParameterizedTest
    @ValueSource(longs = {-13423,-1})
    @DisplayName("Track Duration cannot be less than 0")
    public void trackDurationCannotBeLessThanZero(long arg) {
        assertThrows(IllegalArgumentException.class, () -> this.track.setDuration(arg));
    }


    @Test
    @DisplayName("Should Return Correct Track Number")
    public void shouldReturnCorrectTrackNumber() {
        Track track = new Track();
        track.setTrackNumber(12);
        assertEquals(12, track.getTrackNumber());
    }

    @Test
    @DisplayName("Should Return Correct Track Duration")
    public void shouldReturnCorrectDuration() {
        Track track = new Track();
        track.setDuration(115454);
        assertEquals(115454, track.getDuration());
    }

    @Test
    @DisplayName("should Return True If Two Tracks Have Same Title Track Number Track Duration")
    public void shouldReturnTrueIfTwoTracksHaveSameTitle_TrackNumber_TrackDuration() {
        Track track1 = new Track(5,"Alibaba", 96445);
        Track track2 = new Track(5,"Alibaba", 96445);
        assertTrue(track1.equals(track2));
    }

    @Test
    @DisplayName("should Return False If Two Tracks Have Different Titles")
    public void shouldReturnFalseIfTwoTracksHaveDifferentTitles() {
        Track track1 = new Track(5,"Alibaba", 96445);
        Track track2 = new Track(5,"Hello", 96445);
        assertFalse(track1.equals(track2));
    }

    @Test
    @DisplayName("should Return False If One Of Tracks Is Null When Comparing Two Tracks")
    public void shouldReturnFalseIfOneOfTracksIsNullWhenComparingTwoTracks() {
        Track track = new Track(5,"Alibaba", 96445);
        assertFalse(track.equals(null));
    }

    @Test
    @DisplayName("throw Exceptions if arguments are invalid")
    public void throwExceptionsIfArgumentsAreInvalid() {
        assertThrows(NullPointerException.class, () -> new Track(4,null,454));
        assertThrows(IllegalArgumentException.class, () -> new Track(5,"",545));
        assertThrows(IllegalArgumentException.class, () -> new Track(-1,"Thy",545));
        assertThrows(IllegalArgumentException.class, () -> new Track(0,"Thy",545)); // add to test boundary
        assertThrows(IllegalArgumentException.class, () -> new Track(1,"Thy",-545));
        assertThrows(IllegalArgumentException.class, () -> new Track(1,"Thy",0));// add to test boundary
    }

    @AfterEach
    void tearDown() {
        track = null;
    }
}