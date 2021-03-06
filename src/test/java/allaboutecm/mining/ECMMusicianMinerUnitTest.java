package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.*;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: perform unit testing on the ECMMiner class, by making use of mocking.
 */
class ECMMusicianMinerUnitTest {
    private DAO dao;
    private ECMMusicianMiner ecmMusicianMiner;
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
        ecmMusicianMiner = new ECMMusicianMiner(dao);
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
        assertThrows(IllegalArgumentException.class, () -> this.ecmMusicianMiner.mostProlificMusicians(arg, 2010, 2014));
        assertThrows(IllegalArgumentException.class, () -> this.ecmMusicianMiner.mostTalentedMusicians(arg));
        assertThrows(IllegalArgumentException.class, () -> this.ecmMusicianMiner.mostSocialMusicians(arg));
        assertThrows(IllegalArgumentException.class, () -> this.ecmMusicianMiner.busiestYears(arg));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,9})
    @DisplayName("should Not Throw Illegal Argument Exception When arguments are valid")
    public void shouldNotThrowIllegalArgumentException(int arg) {
        assertDoesNotThrow(() -> this.ecmMusicianMiner.mostProlificMusicians(arg, 2010, 2014));
        assertDoesNotThrow(() -> this.ecmMusicianMiner.mostTalentedMusicians(arg));
        assertDoesNotThrow(() -> this.ecmMusicianMiner.mostSocialMusicians(arg));
        assertDoesNotThrow(() -> this.ecmMusicianMiner.busiestYears(arg));
    }

    @Test
    public void testCaseDAOReturnEmptyList() {
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet());
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet());
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet());

        assertEquals(0, ecmMusicianMiner.mostProlificMusicians(4, 2016, -1).size());
        assertEquals(0, ecmMusicianMiner.mostTalentedMusicians(3).size());
        assertEquals(0, ecmMusicianMiner.mostSocialMusicians(6).size());
        assertEquals(0, ecmMusicianMiner.busiestYears(8).size());
        Album album = new Album(2010, "ECM 1234", "Home");
//        assertEquals(0, ecmMiner.mostSimilarAlbums(4, album).size());
//        assertEquals(0, ecmMiner.highestRatedAlbums(3).size());
//        assertEquals(0, ecmMiner.bestSellingAlbums(9).size());
    }

    //---test cases for mostProlificMusicians
    @Test
    @DisplayName("startYear should not be greater than endYear")
    public void throwIllegalArgExceptionIfStartYearIsGreaterThanEndYear() {
        assertThrows(IllegalArgumentException.class, () -> this.ecmMusicianMiner.mostProlificMusicians(1, 2014, 2010));
    }

    @Test
    public void shouldReturnTheMusicianWhenThereIsOnlyOne() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));

        List<Musician> musicians = ecmMusicianMiner.mostProlificMusicians(5, -1, -1);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @Test
    public void shouldReturnProlificMusiciansInAPeriod() {
        when(dao.loadAll(Musician.class)).thenReturn(this.musicians);
        List<Musician> results = ecmMusicianMiner.mostProlificMusicians(2, 2010, 2018);

        assertEquals(2, results.size());
        List<String> expectedNames = Arrays.asList("Julia Andrews", "Philip Wilson");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getName()));
        }
    }



    @Test
    public void shouldReturnEmptyListIfStartYearAndEndYearEqualZero() {
        when(dao.loadAll(Musician.class)).thenReturn(this.musicians);
        List<Musician> results = ecmMusicianMiner.mostProlificMusicians(2, 0, 0);
        assertEquals(0, results.size());
    }

    @Test
    public void testCaseStartYearIsNegative() {
        when(dao.loadAll(Musician.class)).thenReturn(musicians);
        List<Musician> results = ecmMusicianMiner.mostProlificMusicians(4, -1, 2010);

        assertEquals(4, results.size());
        List<String> expectedNames = Arrays.asList("Julia Andrews", "Ayu Ko", "Jean Den", "Philip Wilson");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getName()));
        }

    }

    @Test
    public void testCaseEndYearIsNegative() {
        when(dao.loadAll(Musician.class)).thenReturn(musicians);
        List<Musician> results = ecmMusicianMiner.mostProlificMusicians(4, 2018, -1);

        assertEquals(1, results.size());
        assertTrue(results.get(0).getName().equals("Julia Andrews"));
    }

    @Test
    public void shouldReturnEmptyListWhenDataBaseReturnMusicianHasNoAlbum() {
        Musician musician = mock(Musician.class);
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));
        List<Musician> results = ecmMusicianMiner.mostProlificMusicians(4, 2016, -1);

        assertEquals(0, results.size());
        assertNotEquals(null, results);
    }

    //---test cases for mostTalentedMusicians
    @Test
    public void shouldReturnTalentedMusicians() {
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(musicianInstruments);
        List<Musician> results = ecmMusicianMiner.mostTalentedMusicians(3);

        assertEquals(3, results.size());
        List<String> expectedNames = Arrays.asList("Julia Andrews", "Ayu Ko", "Jean Den");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getName()));
        }
    }

    @Test
    public void shouldReturnFiveMusiciansWhenOnlyHaveFiveMusicians() {
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(musicianInstruments);
        List<Musician> results = ecmMusicianMiner.mostTalentedMusicians(10);

        assertEquals(5, results.size());
        List<String> expectedNames = Arrays.asList("Julia Andrews", "Ayu Ko", "Jean Den", "Katy Moore", "Philip Wilson");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getName()));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnMusicianInstrumentHasNoMusicianAndNoMusicalInstrument() {
        MusicianInstrument musicianInstrument = mock(MusicianInstrument.class);
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstrument));
        List<Musician> results = this.ecmMusicianMiner.mostTalentedMusicians(4);
        assertEquals(0, results.size());
    }

    //---test cases for mostSocialMusicians
    @Test
    public void shouldReturnTheMostSocialMusician() {
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Musician> results = ecmMusicianMiner.mostSocialMusicians(1);

        assertEquals(1, results.size());
        assertEquals("Philip Wilson", results.get(0).getName());

    }

    @Test
    public void shouldReturnMostSocialMusicians() {
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Musician> results = ecmMusicianMiner.mostSocialMusicians(4);

        assertEquals(4, results.size());
        List<String> expectedNames = Arrays.asList("Julia Andrews", "Ayu Ko", "Jean Den", "Katy Moore", "Philip Wilson");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getName()));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnsAlbumWithEmptyFeaturedMusicians() {
        Album album = mock(Album.class);
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album));
        List<Musician> results = this.ecmMusicianMiner.mostSocialMusicians(4);
        assertEquals(0, results.size());
    }

    //---test cases for busiestYears
    @Test
    public void shouldReturnTheBusiestYear() {
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Integer> results = ecmMusicianMiner.busiestYears(1);

        assertEquals(1, results.size());
        assertEquals(2011, results.get(0));
    }

    @Test
    public void shouldReturnBusiestYears() {
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Integer> results = ecmMusicianMiner.busiestYears(2);
        List<Integer> expectedYears = Arrays.asList(2010, 2011);

        assertEquals(2, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedYears.contains(results.get(i)));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnsAlbumWithEmptyYear() {
        Album album = mock(Album.class);
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album));
        List<Integer> results = this.ecmMusicianMiner.busiestYears(4);

        assertEquals(0, results.size());
    }

    @Test
    public void shouldOnlyReturnAListOfYearsItemsWhenThereIsOnlyFourYears() {
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(albums));
        List<Integer> results = this.ecmMusicianMiner.busiestYears(7);

        List<Integer> expectedYears = Arrays.asList(2010, 2011, 2016, 2018);
        assertEquals(4, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedYears.contains(results.get(i)));
        }
    }

    @Test
    public void shouldReturnAnEmptyListNotEmptyAnyCollection() {
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet());
        assertEquals(ArrayList.class, this.ecmMusicianMiner.mostProlificMusicians(3, 1950, 2000).getClass());
        assertEquals(ArrayList.class, this.ecmMusicianMiner.mostTalentedMusicians(3).getClass());
        assertEquals(ArrayList.class, this.ecmMusicianMiner.mostSocialMusicians(3).getClass());
        assertEquals(ArrayList.class, this.ecmMusicianMiner.busiestYears(3).getClass());
    }
    
    @Test
    public void shouldNotThrowExceptionsWhenStartYearAndEndYearIsCorrect() {
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet());
        assertDoesNotThrow(()->this.ecmMusicianMiner.mostProlificMusicians(2, 2010, 2018));
        assertDoesNotThrow(()->this.ecmMusicianMiner.mostProlificMusicians(2, -1, 2018));
        assertDoesNotThrow(()->this.ecmMusicianMiner.mostProlificMusicians(2, 2019, -1));
        assertDoesNotThrow(()->this.ecmMusicianMiner.mostProlificMusicians(2, -1, -1));
        assertDoesNotThrow(()->this.ecmMusicianMiner.mostProlificMusicians(2, 1986, 1986));
        assertDoesNotThrow(()->this.ecmMusicianMiner.mostProlificMusicians(2, 0, 0));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("Invalid start and end year should always return appropriate lists")
    public void returnAllIfInvalidEndYearProvided(int arg){
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musicians));
        assertEquals(ecmMusicianMiner.mostProlificMusicians(2, 500, arg).size(), 2);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("Invalid start and end year should always return appropriate lists")
    public void returnAllIfInvalidStartYearProvided(int arg){
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musicians));
        assertEquals(ecmMusicianMiner.mostProlificMusicians(2, arg+1, arg).size(), 2);
    }


    // Final changes
    @ParameterizedTest
    @ValueSource(ints = {1,2,9})
    @DisplayName("should Not Throw Illegal Argument Exception When arguments are valid")
    public void shouldNotReturnNull(int arg) {
        assertNotEquals(null, ecmMusicianMiner.busiestYears(arg));
    }


    @AfterEach
    public void tearDownEach() {
        dao = null;
        ecmMusicianMiner = null;
    }

    @AfterAll
    static void afterAll() {
        albums.clear();
        musicians.clear();
        musicianInstruments.clear();
    }
}