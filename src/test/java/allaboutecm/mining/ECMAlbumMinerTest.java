package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.*;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ECMAlbumMinerTest {
    private DAO dao;
    private ECMAlbumMiner ecmMiner;
    private static Set<Album> albums;
    private static Set<Musician> musicians;
    private static Set<MusicianInstrument> musicianInstruments;

    @BeforeAll
    static void beforeAll() {
        prepareObjects();
    }

    @BeforeEach
    public void setUp() {
        dao = mock(Neo4jDAO.class);
        ecmMiner = new ECMAlbumMiner(dao);
    }

    private static void prepareObjects() {
        Album album1 = new Album(2010, "ECM 1234", "Home");
        Album album2 = new Album(2016, "ECM 4567", "Morning");
        Album album3 = new Album(2018, "ECM 3567", "Happy");
        Album album4 = new Album(2011, "ECM 9897", "Guitar");
        Album album5 = new Album(2010, "ECM 5345", "Rose");
        Album album6 = new Album(2011, "ECM 5447", "Orange");
        Album album7 = new Album(2011, "ECM 5677", "Kitaro");

        Musician musician1 = new Musician("Jean Den");
        Musician musician2 = new Musician("Ayu Ko");
        Musician musician3 = new Musician("Julia Andrews");
        Musician musician4 = new Musician("Philip Wilson");
        Musician musician5 = new Musician("Katy Moore");

        musician1.setAlbums(Sets.newHashSet(album1, album2, album5));
        musician2.setAlbums(Sets.newHashSet(album4, album5, album6));
        musician3.setAlbums(Sets.newHashSet(album3, album5, album7, album1));
        musician4.setAlbums(Sets.newHashSet(album7, album5, album4, album2, album6));
        musician5.setAlbums(Sets.newHashSet(album6));

        album1.setFeaturedMusicians(Arrays.asList(musician1, musician3));
        album2.setFeaturedMusicians(Arrays.asList(musician1, musician4));
        album3.setFeaturedMusicians(Arrays.asList(musician3));
        album4.setFeaturedMusicians(Arrays.asList(musician2, musician4));
        album5.setFeaturedMusicians(Arrays.asList(musician1, musician2, musician3, musician4));
        album6.setFeaturedMusicians(Arrays.asList(musician2, musician5, musician4));
        album7.setFeaturedMusicians(Arrays.asList(musician3, musician4));

        MusicalInstrument instrument1 = new MusicalInstrument("Guitar");
        MusicalInstrument instrument2 = new MusicalInstrument("Piano");
        MusicalInstrument instrument3 = new MusicalInstrument("Violin");
        MusicalInstrument instrument4 = new MusicalInstrument("Viola");
        MusicalInstrument instrument5 = new MusicalInstrument("Cello");
        MusicalInstrument instrument6 = new MusicalInstrument("Flute");

        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1, Sets.newHashSet(instrument1, instrument6));
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2, Sets.newHashSet(instrument2, instrument5));
        MusicianInstrument musicianInstrument3 = new MusicianInstrument(musician3, Sets.newHashSet(instrument3, instrument2));
        MusicianInstrument musicianInstrument4 = new MusicianInstrument(musician4, Sets.newHashSet(instrument4));
        MusicianInstrument musicianInstrument5 = new MusicianInstrument(musician5, Sets.newHashSet(instrument5));

        album1.setInstruments(Sets.newHashSet(musicianInstrument1, musicianInstrument3));
        album2.setInstruments(Sets.newHashSet(musicianInstrument1, musicianInstrument4));
        album3.setInstruments(Sets.newHashSet(musicianInstrument3));
        album4.setInstruments(Sets.newHashSet(musicianInstrument2, musicianInstrument4));
        album5.setInstruments(Sets.newHashSet(musicianInstrument1, musicianInstrument2, musicianInstrument3, musicianInstrument4));
        album6.setInstruments(Sets.newHashSet(musicianInstrument2, musicianInstrument5));
        album7.setInstruments(Sets.newHashSet(musicianInstrument3, musicianInstrument4));

        MusicianGroup musicianGroup1 = new MusicianGroup("Andy");
        MusicianGroup musicianGroup2 = new MusicianGroup("Breakout");
        MusicianGroup musicianGroup3 = new MusicianGroup("Spring");
        MusicianGroup musicianGroup4 = new MusicianGroup("Metal");
        MusicianGroup musicianGroup5 = new MusicianGroup("Roll");

        album1.setMusicianGroup(musicianGroup1);
        album2.setMusicianGroup(musicianGroup2);
        album3.setMusicianGroup(musicianGroup3);
        album4.setMusicianGroup(musicianGroup4);
        album5.setMusicianGroup(musicianGroup5);

        Rating rating1 = new Rating();
        rating1.setScore(1);
        Rating rating2 = new Rating();
        rating2.setScore(2);
        Rating rating3 = new Rating();
        rating3.setScore(3);
        Rating rating4 = new Rating();
        rating4.setScore(4);
        Rating rating5 = new Rating();
        rating5.setScore(5);

        album2.setRatings(Sets.newHashSet(rating2,rating3,rating5));
        album3.setRatings(Sets.newHashSet(rating1,rating4,rating2));
        album4.setRatings(Sets.newHashSet(rating1,rating2,rating3));
        album5.setRatings(Sets.newHashSet(rating1,rating3,rating4));
        album6.setRatings(Sets.newHashSet(rating2,rating4,rating5));
        album7.setRatings(Sets.newHashSet(rating4,rating3,rating5));

        albums = Sets.newHashSet(album1, album2, album3, album4, album5, album6, album7);
        musicians = Sets.newHashSet(musician1, musician2, musician3, musician4, musician5);
        musicianInstruments = Sets.newHashSet(musicianInstrument1, musicianInstrument2,
                musicianInstrument3, musicianInstrument4, musicianInstrument5);

        //set units sold information
        int[] unitsSoldArray = {34,67,89,78,45,12,68};
        int i = 0;
        for (Album album: albums){
            album.setUnitsSold(unitsSoldArray[i++]);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("k argument should not less than 1")
    public void throwIllegalArgExceptionIfArgumentKLessThanOne(int arg) {
        Album album = new Album(2010, "ECM 1234", "Home");
        assertThrows(IllegalArgumentException.class, () -> this.ecmMiner.mostSimilarAlbums(arg, album));
        assertThrows(IllegalArgumentException.class, () -> this.ecmMiner.highestRatedAlbums(arg));
        assertThrows(IllegalArgumentException.class, () -> this.ecmMiner.bestSellingAlbums(arg));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,9})
    @DisplayName("should Not Throw Illegal Argument Exception When K is valid")
    public void shouldNotThrowIllegalArgumentException(int arg) {
        Album album = new Album(2010, "ECM 1234", "Home");
        assertDoesNotThrow(() -> this.ecmMiner.mostSimilarAlbums(arg, album));
        assertDoesNotThrow(() -> this.ecmMiner.highestRatedAlbums(arg));
        assertDoesNotThrow(() -> this.ecmMiner.bestSellingAlbums(arg));
    }

    @Test
    public void testCaseDAOReturnEmptyList() {
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet());
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet());
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet());

        Album album = new Album(2010, "ECM 1234", "Home");
        assertEquals(0, ecmMiner.mostSimilarAlbums(4, album).size());
        assertEquals(0, ecmMiner.highestRatedAlbums(3).size());
        assertEquals(0, ecmMiner.bestSellingAlbums(9).size());
    }
    //---test cases for mostSimilarAlbums
    @Test
    @DisplayName("Album argument should not be null")
    public void throwNullPointerExceptionIfAlbumIsNull() {
        assertThrows(NullPointerException.class, () -> this.ecmMiner.mostSimilarAlbums(4, null));
    }

    @Test
    public void shouldReturnCorrectSizeListOfSimilarAlbums() {
        Album album = new Album(2018, "ECM 8909", "Home");
        album.setFeaturedMusicians(Arrays.asList(new Musician("Julia Andrews")));

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(albums));
        List<Album> results = this.ecmMiner.mostSimilarAlbums(3, album);

        List<String> expectedNames = Arrays.asList("Home", "Happy", "Kitaro");
        assertEquals(3, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getAlbumName()));
        }
    }

    @Test
    public void shouldReturnSimilarAlbumsHavingSimilarAlbumName(){
        Album album = new Album(2019, "ECM 3523", "Orange Lemon");
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(albums));
        List<Album> results = this.ecmMiner.mostSimilarAlbums(4, album);

        assertEquals(1, results.size());
        assertEquals("Orange", results.get(0).getAlbumName());
    }

    @Test
    public void shouldReturnSimilarAlbumsHavingSimilarMusicGroupName(){
        Album album = new Album(2019, "ECM 3523", "Theory");
        MusicianGroup musicianGroup = new MusicianGroup("Andy");
        album.setMusicianGroup(musicianGroup);
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(albums));
        List<Album> results = this.ecmMiner.mostSimilarAlbums(4, album);

        assertEquals(1, results.size());
        assertEquals("Home", results.get(0).getAlbumName());
        assertEquals("Andy", results.get(0).getMusicianGroup().getGroupName());
    }

    @Test
    public void shouldReturnSimilarAlbumsHavingSimilarMusicians(){
        Album album = new Album(2019, "ECM 3523", "Theory");
        Musician musician = new Musician("Jean Den");
        album.setFeaturedMusicians(Arrays.asList(musician));
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(albums));
        List<Album> results = this.ecmMiner.mostSimilarAlbums(4, album);

        assertEquals(3, results.size());
        List<String> expectedNames = Arrays.asList("Home", "Morning", "Rose");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getAlbumName()));
            assertTrue(results.get(i).getFeaturedMusicians().contains(musician));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnsAlbumWithEmptyInformation() {
        Album album = new Album(2019, "ECM 3523", "Theory");
        Album albumDB = mock(Album.class);
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(albumDB));
        List<Album> results = this.ecmMiner.mostSimilarAlbums(4, album);

        assertEquals(0, results.size());
    }

    //---test cases for highestRatedAlbums
    @Test
    public void shouldReturnTheHighestRatedAlbum() {
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Album> results = ecmMiner.highestRatedAlbums(1);

        assertEquals(1, results.size());
        assertEquals("Kitaro", results.get(0).getAlbumName());
        List<Integer> expectedRatings = Arrays.asList(3,4,5);
        for (Rating rating: results.get(0).getRatings()) {
            assertTrue(expectedRatings.contains(rating.getScore()));
        }
    }

    @Test
    public void shouldReturnThreeHighestRatedAlbums() {
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Album> results = ecmMiner.highestRatedAlbums(3);

        assertEquals(3, results.size());
        List<String> expectedNames = Arrays.asList("Kitaro", "Orange", "Morning");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getAlbumName()));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnsAlbumWithEmptyRatings() {
        Album album = mock(Album.class);
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album));
        List<Album> results = this.ecmMiner.highestRatedAlbums(4);
        assertEquals(0, results.size());
    }

    @Test
    public void shouldReturnSixHighestRatedAlbumsOfSevenAlbumsWhenThereIsOneAlbumHavingNoRatings() {
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(albums));
        List<Album> results = this.ecmMiner.highestRatedAlbums(7);

        assertEquals(6, results.size());
        List<String> expectedNames = Arrays.asList("Kitaro", "Orange", "Morning","Rose", "Happy","Guitar");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getAlbumName()));
        }
    }
    //---test cases for bestSellingAlbums
    @Test
    public void shouldReturnTheBestSellingAlbum() {
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Album> results = ecmMiner.bestSellingAlbums(1);

        assertEquals(1, results.size());
        assertEquals(89, results.get(0).getUnitsSold());
    }

    @Test
    public void shouldReturnFourBestSellingAlbums() {

        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Album> results = ecmMiner. bestSellingAlbums(4);
        List<Integer> expectedUnitsSolds = Arrays.asList(67,89,78,68);

        assertEquals(4, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedUnitsSolds.contains(results.get(i).getUnitsSold()));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnsAlbumWithZeroUnitsSold() {
        Album album = mock(Album.class);
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album));
        List<Album> results = this.ecmMiner.bestSellingAlbums(4);

        assertEquals(0, results.size());
    }

    @Test
    public void shouldReturnAnEmptyListNotEmptyAnyCollection() {
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet());
        Album album = new Album(2010, "ECM 1234", "Home");
        assertEquals(ArrayList.class, this.ecmMiner.mostSimilarAlbums(3, album).getClass());
        assertEquals(ArrayList.class, this.ecmMiner.highestRatedAlbums(3).getClass());
        assertEquals(ArrayList.class, this.ecmMiner.bestSellingAlbums(3).getClass());
    }




    @AfterEach
    public void tearDownEach() {
        dao = null;
        ecmMiner = null;
    }

    @AfterAll
    static void afterAll() {
        albums.clear();
        musicians.clear();
        musicianInstruments.clear();
    }
}