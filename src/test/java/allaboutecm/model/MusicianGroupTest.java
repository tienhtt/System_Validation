package allaboutecm.model;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;


class  MusicianGroupTest{
        private MusicianGroup musicianGroup;
        @BeforeEach
        void setUp() {
            musicianGroup = new MusicianGroup("Jan Garbarek Quartet");

        }

    @Test
    @DisplayName("Group name cannot be null")
    public void groupNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicianGroup.setGroupName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Group name cannot be empty or blank")
    public void groupNameCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> musicianGroup.setGroupName(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jan Garbarek Quartet"})
    @DisplayName("Group name should be set and returned as expected.")
    public void groupNameSetAndReturnedAsExpected(String arg) {
        musicianGroup.setGroupName(arg);
        assertEquals(arg,musicianGroup.getGroupName());
    }

    @Test
    @DisplayName("Albums cannot be null")
    public void groupAlbumNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicianGroup.setAlbums(null));
    }

    @Test
    @DisplayName("Albums set cannot contain nulls.")
    public void albumsSetCannotContainNulls() {
        Set<Album> arg = new HashSet<>();
        arg.add(null);

        assertThrows(NullPointerException.class, () -> musicianGroup.setAlbums(arg));
    }

    @Test
    @DisplayName("Albums set and returned as expected.")
    public void albumsSetAndReturnedAsExpected() {
        Set<Album> arg = new HashSet<>();
        Album album1 = new Album(2015,"ECM 2009", "Afric Pepperbird");
        arg.add(album1);
        musicianGroup.setAlbums(arg);

        assertEquals(arg, musicianGroup.getAlbums());
    }


    @Test
    @DisplayName("groupMusicians cannot be null")
    public void throwNullPointerExceptionIfGroupMusiciansNull(){
        assertThrows(NullPointerException.class, () -> musicianGroup.setGroupMusicians(null));
    }

    @Test
    @DisplayName("A musician group should have at least two musicians.")
    public void throwIllegalArgExceptionIfGroupMusiciansSetEmpty(){
        Set<Musician> args = Sets.newHashSet();
        args.add(new Musician("Jan Garbarek"));

        assertThrows(IllegalArgumentException.class, () -> musicianGroup.setGroupMusicians(args));
    }

    @Test
    @DisplayName("Group musicians should be set as expected.")
    public void musiciansSetAsExpected(){
        Set<Musician> args = Sets.newHashSet();
        args.add(new Musician("Jan Garbarek"));
        args.add(new Musician("Arild Anderson"));
        musicianGroup.setGroupMusicians(args);

        assertEquals(args,musicianGroup.getGroupMusicians());
    }

    @Test
    @DisplayName("should Return True If Two Musician Groups Have Same Name, Musicians, Albums")
    public void shouldReturnTrueIfTwoMusicianGroupHaveSameName_Musicians_Albums() {
        Set<Musician> musicians = Sets.newHashSet();
        musicians.add(new Musician("Jan Garbarek"));
        musicians.add(new Musician("Jack Scott"));
        Set<Album> albums = new HashSet<>();
        Album album1 = new Album(2015,"ECM 2009", "Afric Pepperbird");

        MusicianGroup musicianGroup1 = new MusicianGroup("AKE");
        musicianGroup1.setAlbums(albums);
        musicianGroup1.setGroupMusicians(musicians);
        MusicianGroup musicianGroup2 = new MusicianGroup("AKE");
        musicianGroup2.setAlbums(albums);
        musicianGroup2.setGroupMusicians(musicians);

        assertTrue(musicianGroup1.equals(musicianGroup2));
    }

    @Test
    @DisplayName("should Return False If Two Musician Groups Have Different Names")
    public void shouldReturnFalseIfTwoMusicianGroupsHaveDifferentName() {
        MusicianGroup musicianGroup1 = new MusicianGroup("AKE");
        MusicianGroup musicianGroup2 = new MusicianGroup("The Four");
        assertFalse(musicianGroup1.equals(musicianGroup2));
    }

    @Test
    @DisplayName("should Return False If One Of Musicians Is Null When Comparing Two Musician Groups")
    public void shouldReturnFalseIfOneOfMusiciansIsNullWhenComparingTwoMusicianGroups() {
        MusicianGroup musicianGroup = new MusicianGroup("AKE");
        assertFalse(musicianGroup.equals(null));
    }

    @AfterEach
    void tearDown() {
        musicianGroup = null;
    }
}
