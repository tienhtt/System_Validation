package allaboutecm.model;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MusicianInstrumentTest {
    private MusicianInstrument musicianInstrument;

    @BeforeEach
    void setUp() {
        Musician musician = new Musician("Keith Jarrett");
        MusicalInstrument musicalInstrument = new MusicalInstrument("piano");
        Set<MusicalInstrument> musicalInstruments = Sets.newHashSet();
        musicalInstruments.add(musicalInstrument);
        musicianInstrument = new MusicianInstrument(musician, musicalInstruments);
    }

    @Test
    @DisplayName("Track should return same Musician as expected value")
    public void musicianNameIsValidAsExpectation() {
        Musician expectedValue = new Musician("Keith Jarrett");
        assertEquals(expectedValue, musicianInstrument.getMusician());
    }

    @Test
    @DisplayName("Musical instruments cannot contain null elements")
    public void musicalInstrumentsCannotContainNull() {
        MusicalInstrument m = null;
        MusicalInstrument actualInstrument= new MusicalInstrument("Flute");

        Set<MusicalInstrument> args = new HashSet<>();
        args.add(m);
        args.add(actualInstrument);

        assertThrows(NullPointerException.class, () -> musicianInstrument.setMusicalInstruments(args));
    }

    @Test
    @DisplayName("Musical Instruments set cannot be null")
    public void musicalInstrumentSetCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicianInstrument.setMusicalInstruments(null));
    }

    @Test
    @DisplayName("Musical instruments should be set and returned as expected")
    public void musicalInstrumentsCannotBeNull() {
        MusicalInstrument m1 = new MusicalInstrument("Flute");
        MusicalInstrument m2 = new MusicalInstrument("Guitar");

        Set<MusicalInstrument> args = new HashSet<>();
        args.add(m1);
        args.add(m2);

        musicianInstrument.setMusicalInstruments(args);

        assertEquals(musicianInstrument.getMusicalInstruments(), args);
    }

    @Test
    @DisplayName("Track should return same MusicalInstrument as expected value")
    public void musicalInstrumentNameIsValidAsExpectation() {
        MusicalInstrument musicalInstrument = new MusicalInstrument("piano");
        Set<MusicalInstrument> expectedValue = Sets.newHashSet();
        expectedValue.add(musicalInstrument);
        assertEquals(expectedValue, musicianInstrument.getMusicalInstruments());
    }

    @Test
    @DisplayName("Musician cannot be null")
    public void musicianCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicianInstrument.setMusician(null));
    }

    @Test
    @DisplayName("Same musician and musical instrument means same musician-instrument")
    public void sameMusicianAndMusicalInstrumentMeansSameMusicianInstrument() {
        Musician musician = new Musician("Keith Jarrett");
        MusicalInstrument musicalInstrument = new MusicalInstrument("piano");
        Set<MusicalInstrument> musicalInstruments = Sets.newHashSet();
        musicalInstruments.add(musicalInstrument);
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician, musicalInstruments);
        assertEquals(musicianInstrument, musicianInstrument1);
    }

    @AfterEach
    void tearDown() {
        musicianInstrument = null;
    }
}