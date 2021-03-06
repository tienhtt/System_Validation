package allaboutecm.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MusicalInstrumentTest {
    private MusicalInstrument musicalInstrument;

    @BeforeEach
    void setUp() {
        musicalInstrument = new MusicalInstrument("piano");
    }

    @Test
    @DisplayName("Musical Instrument should return same name as expected value")
    public void musicalInstrumentNameIsValidAsExpectation() {
        assertEquals("piano", musicalInstrument.getName());
    }

    @Test
    @DisplayName("Musical Instrument name cannot be null")
    public void musicalInstrumentNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicalInstrument.setName(null));
        assertThrows(NullPointerException.class, () -> new MusicalInstrument(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Musical Instrument name cannot be empty or blank")
    public void musicalInstrumentNameCannotBeBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> musicalInstrument.setName(arg));
        assertThrows(IllegalArgumentException.class, () -> new MusicalInstrument(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Guitar34234"})
    @DisplayName("Musical Instrument name is only alphabetical")
    public void musicalInstrumentNameIsOnlyAlphabetical(String arg) {
        assertThrows(IllegalArgumentException.class, () -> musicalInstrument.setName(arg));
        assertThrows(IllegalArgumentException.class, () -> new MusicalInstrument(arg));
    }

    @Test
    @DisplayName("Same name means same musical instrument")
    public void sameNameMeansSameMusicalInstrument() {
        MusicalInstrument musicalInstrument1 = new MusicalInstrument("piano");
        assertEquals(musicalInstrument, musicalInstrument1);
    }

    @Test
    @DisplayName("Same string name without case considerations means same Musical Instrument")
    public void sameStringNameWithoutConsiderationsMeansSameMusicalInstrument() {
        MusicalInstrument musicalInstrument1 = new MusicalInstrument("PIano");
        assertEquals(musicalInstrument, musicalInstrument1);
    }

    @AfterEach
    void tearDown() {
        musicalInstrument = null;
    }
}