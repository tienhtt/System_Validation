package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TODO: perform integration testing of both ECMMiner and the DAO classes together.
 */
class ECMMinerIntegrationTest {

    private static DAO dao;
    private static Session session;
    private static SessionFactory sessionFactory;

    private ECMMusicianMiner ecmMusicianMiner;
    private ECMAlbumMiner ecmAlbumMiner;

    private static Set<Album> albums = Sets.newHashSet();
    private static Set<Musician> musicians = Sets.newHashSet();
    private static Set<MusicianInstrument> musicianInstruments = Sets.newHashSet();

    @BeforeAll
    static void beforeAll() {
        // Impermanent embedded store
        Configuration configuration = new Configuration.Builder().build();
        sessionFactory = new SessionFactory(configuration, Musician.class.getPackage().getName());
        session = sessionFactory.openSession();
        dao = new Neo4jDAO(session);

    }

    @BeforeEach
    public void setUp() {
        ecmMusicianMiner = new ECMMusicianMiner(dao);
        ecmAlbumMiner = new ECMAlbumMiner(dao);
    }

    @AfterEach
    public void tearDownEach() {
        session.purgeDatabase();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        session.purgeDatabase();
        session.clear();
        sessionFactory.close();

        albums.clear();
        musicians.clear();
        musicianInstruments.clear();
    }

    private static void prepareObjects(String entity) {
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

        if (entity.equals("Musician")){
            musician1.setAlbums(Sets.newHashSet(album1, album2, album5));
            musician2.setAlbums(Sets.newHashSet(album4, album5, album6));
            musician3.setAlbums(Sets.newHashSet(album3, album5, album7, album1));
            musician4.setAlbums(Sets.newHashSet(album7, album5, album4, album2, album6));
            musician5.setAlbums(Sets.newHashSet(album6));
        } else if (entity.equals("Album")){
            album1.setFeaturedMusicians(Arrays.asList(musician1, musician3));
            album2.setFeaturedMusicians(Arrays.asList(musician1, musician4));
            album3.setFeaturedMusicians(Arrays.asList(musician3));
            album4.setFeaturedMusicians(Arrays.asList(musician2, musician4));
            album5.setFeaturedMusicians(Arrays.asList(musician1, musician2, musician3, musician4));
            album6.setFeaturedMusicians(Arrays.asList(musician2, musician5, musician4));
            album7.setFeaturedMusicians(Arrays.asList(musician3, musician4));
        }

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
        musicianInstruments = Sets.newHashSet(musicianInstrument1, musicianInstrument2, musicianInstrument3, musicianInstrument4, musicianInstrument5);

        int[] unitsSoldArray = {34,67,89,78,45,12,68};
        int i = 0;
        for (Album album: albums){
            album.setUnitsSold(unitsSoldArray[i++]);
        }
    }

    // [ DAO ]
    /**
     * test DAO for accessing the database
     */
    @Test
    public void dataAccessObjectTest() {
        prepareObjects("Musician");
        albums.forEach(album -> { dao.createOrUpdate(album); });
        musicians.forEach(musician -> { dao.createOrUpdate(musician); });
        musicianInstruments.forEach(musicianInstrument -> { dao.createOrUpdate(musicianInstrument); });

        List<Album> albumsList = new ArrayList<>(albums);
        List<Musician> musiciansList = new ArrayList<>(musicians);
        List<MusicianInstrument> musicianInstrumentsList = new ArrayList<>(musicianInstruments);

        Collection<Album> loadedAlbums = dao.loadAll(Album.class);
        Collection<Musician> loadedMusicians = dao.loadAll(Musician.class);
        Collection<MusicianInstrument> loadedMusicianInstruments = dao.loadAll(MusicianInstrument.class);
        List<Album> loadedAlbumsList = new ArrayList<>(loadedAlbums);
        List<Musician> loadedMusiciansList = new ArrayList<>(loadedMusicians);
        List<MusicianInstrument> loadedMusicianInstrumentsList = new ArrayList<>(loadedMusicianInstruments);

        assertNotNull(loadedAlbumsList);
        assertNotNull(loadedMusiciansList);
        assertNotNull(loadedMusicianInstrumentsList);

        loadedAlbumsList.forEach(Assertions::assertNotNull);
        loadedMusiciansList.forEach(Assertions::assertNotNull);
        loadedMusicianInstrumentsList.forEach(Assertions::assertNotNull);

        assertEquals(albumsList.size(), loadedAlbumsList.size());
        assertEquals(musiciansList.size(), loadedMusiciansList.size());
        assertEquals(musicianInstrumentsList.size(), loadedMusicianInstrumentsList.size());

        assertTrue(loadedAlbumsList.containsAll(albumsList) && albumsList.containsAll(loadedAlbumsList));
        assertTrue(loadedMusiciansList.containsAll(musiciansList) && musiciansList.containsAll(loadedMusiciansList));
        assertTrue(loadedMusicianInstrumentsList.containsAll(musicianInstrumentsList) && musicianInstrumentsList.containsAll(loadedMusicianInstrumentsList));
    }

    // [ ECM ]

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("k argument should not less than 1")
    public void throwIllegalArgExceptionIfArgumentKLessThanOne(int arg) {
        assertThrows(IllegalArgumentException.class, () -> this.ecmMusicianMiner.mostProlificMusicians(arg, 2010, 2014));
        assertThrows(IllegalArgumentException.class, () -> this.ecmMusicianMiner.mostTalentedMusicians(arg));
        assertThrows(IllegalArgumentException.class, () -> this.ecmMusicianMiner.mostSocialMusicians(arg));
        Album album = new Album(2010, "ECM 1234", "Home");
        assertThrows(IllegalArgumentException.class, () -> this.ecmAlbumMiner.mostSimilarAlbums(arg, album));
        assertThrows(IllegalArgumentException.class, () -> this.ecmMusicianMiner.busiestYears(arg));
    }

    @Test
    public void testCaseDAOReturnEmptyList() {
        assertEquals(0, ecmMusicianMiner.mostProlificMusicians(4, 2016, -1).size());
        assertEquals(0, ecmMusicianMiner.mostTalentedMusicians(4).size());
        assertEquals(0, ecmMusicianMiner.mostSocialMusicians(4).size());
        Album album = new Album(2010, "ECM 1234", "Home");
        assertEquals(0, ecmAlbumMiner.mostSimilarAlbums(4, album).size());
        assertEquals(0, ecmMusicianMiner.busiestYears(4).size());

    }

    //[ ECM ]---test cases for mostProlificMusicians
    @Test
    @DisplayName("startYear should not be greater than endYear")
    public void throwIllegalArgExceptionIfStartYearIsGreaterThanEndYear() {
        assertThrows(IllegalArgumentException.class, () -> this.ecmMusicianMiner.mostProlificMusicians(1, 2014, 2010));
    }

    // Integrated from Team 16 codebase
    @Test
    @DisplayName("multiple most prolific musician should return in order")
    public void multipleMostProlificMusicianShouldReturnInOrder()
    {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician1 = new Musician("Keith Jarrett");
        musician1.setAlbums(Sets.newHashSet(album1));

        Album album2 = new Album(2016, "ECM 2487", "ANDANDO EL TIEMPO");
        Album album3 = new Album(2013, "ECM 2287", "TRIOS");
        Musician musician2 = new Musician("Carla Bley");
        musician2.setAlbums(Sets.newHashSet(album2,album3));

        Album album4 = new Album(2020, "ECM 2669", "LIFE GOES ON");
        Musician musician3 = new Musician("Andy Sheppard");
        musician3.setAlbums(Sets.newHashSet(album2, album3, album4));

        dao.createOrUpdate(musician1);
        dao.createOrUpdate(musician2);
        dao.createOrUpdate(musician3);

        List<Musician> musicians = ecmMusicianMiner.mostProlificMusicians(2, -1, -1);
        assertEquals(2, musicians.size());
        assertTrue(musicians.get(0).equals(musician3));
        assertTrue(musicians.get(1).equals(musician2));
    }

    @Test
    public void shouldReturnTheMusicianWhenThereIsOnlyOne() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));
        dao.createOrUpdate(musician);

        List<Musician> musicians = ecmMusicianMiner.mostProlificMusicians(5, -1, -1);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @Test
    public void shouldReturnProlificMusiciansInAPeriod() {
        prepareObjects("Musician");
        musicians.forEach(musician -> { dao.createOrUpdate(musician); });
        List<Musician> results = ecmMusicianMiner.mostProlificMusicians(2, 2010, 2018);

        assertEquals(2, results.size());
        List<String> expectedNames = Arrays.asList("Julia Andrews", "Philip Wilson");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getName()));
        }
    }

    @Test
    public void shouldReturnEmptyListIfStartYearAndEndYearEqualZero() {
        musicians.forEach(musician -> { dao.createOrUpdate(musician); });
        List<Musician> results = ecmMusicianMiner.mostProlificMusicians(2, 0, 0);
        assertEquals(0, results.size());
    }

    @Test
    public void testCaseStartYearIsNegative() {
        prepareObjects("Musician");
        musicians.forEach(musician -> { dao.createOrUpdate(musician); });
        List<Musician> results = ecmMusicianMiner.mostProlificMusicians(4, -1, 2010);

        assertEquals(4, results.size());
        List<String> expectedNames = Arrays.asList("Julia Andrews", "Ayu Ko", "Jean Den", "Philip Wilson");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getName()));
        }
    }

    @Test
    public void testCaseEndYearIsNegative() {
        prepareObjects("Musician");
        musicians.forEach(musician -> { dao.createOrUpdate(musician); });
        List<Musician> results = ecmMusicianMiner.mostProlificMusicians(4, 2018, -1);

        assertEquals(1, results.size());
        assertTrue(results.get(0).getName().equals("Julia Andrews"));
    }

    @Test
    public void shouldReturnEmptyListWhenDataBaseReturnMusicianHasNoAlbum() {
        Musician musician = new Musician("Keith Jarrett");
        dao.createOrUpdate(musician);
        List<Musician> results = ecmMusicianMiner.mostProlificMusicians(4, 2016, -1);

        assertEquals(0, results.size());
    }

    //[ ECM ]---test cases for mostTalentedMusicians

    // Integrated from Team 16 codebase
    @Test
    @DisplayName("should return all in order if K bigger than musician size")
    public void shouldReturnAllIfKBiggerThanSize() {
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("GARY PEACOCK");
        Musician musician3 = new Musician("JACK DEJOHNETTE");
        MusicalInstrument musicalInstrument1 = new MusicalInstrument("piano");
        MusicalInstrument musicalInstrument2 = new MusicalInstrument("guitar");
        MusicalInstrument musicalInstrument3 = new MusicalInstrument("violin");
        MusicalInstrument musicalInstrument4 = new MusicalInstrument("suona");
        Set<MusicalInstrument> musicalInstrument11 =Sets.newHashSet();
        Set<MusicalInstrument> musicalInstrument22 =Sets.newHashSet();
        Set<MusicalInstrument> musicalInstrument33 =Sets.newHashSet();
        musicalInstrument11.add(musicalInstrument1);
        musicalInstrument11.add(musicalInstrument2);
        musicalInstrument11.add(musicalInstrument3);
        musicalInstrument11.add(musicalInstrument4);
        musicalInstrument22.add(musicalInstrument1);
        musicalInstrument22.add(musicalInstrument2);
        musicalInstrument22.add(musicalInstrument3);
        musicalInstrument33.add(musicalInstrument4);
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,musicalInstrument11);  //4
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,musicalInstrument22);  //3
        MusicianInstrument musicianInstrument3 = new MusicianInstrument(musician3,musicalInstrument33);  //1
        Set<MusicianInstrument> musicianInstruments = Sets.newHashSet();
        musicianInstruments.add(musicianInstrument1);
        musicianInstruments.add(musicianInstrument2);
        musicianInstruments.add(musicianInstrument3);
        HashSet<Musician> musicians = Sets.newHashSet();
        musicians.add(musician1);
        musicians.add(musician2);
        musicians.add(musician3);
        dao.createOrUpdate(musician1);
        dao.createOrUpdate(musician2);
        dao.createOrUpdate(musician3);
        dao.createOrUpdate(musicianInstrument1);
        dao.createOrUpdate(musicianInstrument2);
        dao.createOrUpdate(musicianInstrument3);

        List<Musician> talentedMusician = ecmMusicianMiner.mostTalentedMusicians(6);
        assertEquals(3, talentedMusician.size());
        assertEquals(musician1,talentedMusician.get(0));
        assertEquals(musician2,talentedMusician.get(1));
        assertEquals(musician3,talentedMusician.get(2));
    }

    @Test
    public void shouldReturnTalentedMusicians() {
        prepareObjects("Musician");
        musicianInstruments.forEach(musicianInstrument -> { dao.createOrUpdate(musicianInstrument); });
        List<Musician> results = ecmMusicianMiner.mostTalentedMusicians(3);

        assertEquals(3, results.size());
        List<String> expectedNames = Arrays.asList("Julia Andrews", "Ayu Ko", "Jean Den");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getName()));
        }
    }

    @Test
    public void shouldReturnFiveMusiciansWhenOnlyHaveFiveMusicians() {
        prepareObjects("Musician");
        musicianInstruments.forEach(musicianInstrument -> { dao.createOrUpdate(musicianInstrument); });
        List<Musician> results = ecmMusicianMiner.mostTalentedMusicians(10);

        assertEquals(5, results.size());
        List<String> expectedNames = Arrays.asList("Julia Andrews", "Ayu Ko", "Jean Den", "Katy Moore", "Philip Wilson");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getName()));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnMusicianInstrumentHasNoMusicianAndNoMusicalInstrument() {
        MusicianInstrument musicianInstrument = new MusicianInstrument();
        dao.createOrUpdate(musicianInstrument);
        List<Musician> results = this.ecmMusicianMiner.mostTalentedMusicians(4);
        assertEquals(0, results.size());
    }

    //[ ECM ]---test cases for mostSocialMusicians
    @Test
    public void shouldReturnTheMostSocialMusician() {
        prepareObjects("Album");
        albums.forEach(album -> { dao.createOrUpdate(album); });
        List<Musician> results = ecmMusicianMiner.mostSocialMusicians(1);

        assertEquals(1, results.size());
        assertEquals("Philip Wilson", results.get(0).getName());
    }

    // Integrated from the Team 16 codebase
    @Test
    @DisplayName("should return all in order if K bigger than albums size ")
    public void shouldReturnAllMusicianWhenBiggerThanK() {
        Album album1 = new Album(2019, "ECM 2667", "MUNICH 2016");
        Album album2 = new Album(2020, "ECM 2669", "LIFE GOES ON");
        Album album3 = new Album(2019, "ECM 1001", "FREE AT LAST (EXTENDED EDITION)");
        Album album4 = new Album(2019, "ECM 2665", "HEINZ HOLLIGER & GYÖRGY KURTÁG - ZWIEGESPRÄCHE");
        Album album5 = new Album(2019, "ECM 1744", "DIFFERENT RIVERS TRYGVE SEIM");
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("GARY PEACOCK");
        Musician musician3 = new Musician("JACK DEJOHNETTE");
        List<Musician> featuredMusicians1 = new ArrayList<>();
        List<Musician> featuredMusicians2 = new ArrayList<>();
        List<Musician> featuredMusicians3 = new ArrayList<>();
        List<Musician> featuredMusicians4 = new ArrayList<>();
        List<Musician> featuredMusicians5 = new ArrayList<>();
        featuredMusicians1.add(musician1);
        featuredMusicians2.add(musician2);
        featuredMusicians3.add(musician3);
        featuredMusicians4.add(musician1);
        featuredMusicians5.add(musician1);
        featuredMusicians1.add(musician3);
        album1.setFeaturedMusicians(featuredMusicians1);
        album2.setFeaturedMusicians(featuredMusicians2);
        album3.setFeaturedMusicians(featuredMusicians3);
        album4.setFeaturedMusicians(featuredMusicians4);
        album5.setFeaturedMusicians(featuredMusicians5);
        HashSet<Album> albums = Sets.newHashSet();
        albums.add(album1);
        albums.add(album2);
        albums.add(album3);
        albums.add(album4);
        albums.add(album5);
        HashSet<Musician> musicians = Sets.newHashSet();
        musicians.add(musician1);
        musicians.add(musician2);
        musicians.add(musician3);
        dao.createOrUpdate(musician1);
        dao.createOrUpdate(musician2);
        dao.createOrUpdate(musician3);
        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);
        dao.createOrUpdate(album4);
        dao.createOrUpdate(album5);
        //when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(albums));
        //when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musicians));

        List<Musician> widelyMusician = ecmMusicianMiner.mostSocialMusicians(5);

        assertEquals(3, widelyMusician.size());
        assertEquals(musician1,widelyMusician.get(0));
        assertEquals(musician3,widelyMusician.get(1));
        assertEquals(musician2,widelyMusician.get(2));
    }

    @Test
    public void shouldReturnMostSocialMusicians() {
        prepareObjects("Album");
        albums.forEach(album -> { dao.createOrUpdate(album); });
        List<Musician> results = ecmMusicianMiner.mostSocialMusicians(4);

        assertEquals(4, results.size());
        List<String> expectedNames = Arrays.asList("Julia Andrews", "Ayu Ko", "Jean Den", "Katy Moore", "Philip Wilson");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getName()));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnsAlbumWithEmptyFeaturedMusicians() {
        Album album = new Album();
        dao.createOrUpdate(album);
        List<Musician> results = this.ecmMusicianMiner.mostSocialMusicians(4);
        assertEquals(0, results.size());
    }

    //[ ECM ]---test cases for busiestYears
    @Test
    public void shouldReturnTheBusiestYear() {
        prepareObjects("Album");
        albums.forEach(album -> { dao.createOrUpdate(album); });
        List<Integer> results = ecmMusicianMiner.busiestYears(1);

        assertEquals(1, results.size());
        assertEquals(2011, results.get(0));
    }

    // Integrated from Team 16 code base
    @Test
    @DisplayName("should return the year when there is only one")
    public void shouldReturnTheYearWhenThereIsOnlyOne()
    {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);

        List<Integer> busiestYear = ecmMusicianMiner.busiestYears(1);

        assertEquals(1, busiestYear.size());
        assertEquals(1975, busiestYear.iterator().next());
    }

    @Test
    @DisplayName("busiest Year Should Return zero size collection When No Data")
    public void busiestYearShouldReturnZeroSizeCollectionWhenNoData()
    {
        List<Integer> busiestYear= ecmMusicianMiner.busiestYears(4);
        assertEquals(0, busiestYear.size());
    }

    // Integrated from Team 16 codebase
    @Test
    @DisplayName("should return all in order if K bigger than albums size")
    public void shouldReturnAllInOrderIfKBiggerThanAlbumsSize()
    {
        Album album1 = new Album(2019, "ECM 2667", "MUNICH 2016");
        Album album2 = new Album(2020, "ECM 2669", "LIFE GOES ON");
        Album album3 = new Album(2019, "ECM 1001", "FREE AT LAST (EXTENDED EDITION)");
        Album album4 = new Album(2019, "ECM 2665", "HEINZ HOLLIGER & GYÖRGY KURTÁG - ZWIEGESPRÄCHE");
        Album album5 = new Album(2019, "ECM 1744", "DIFFERENT RIVERS TRYGVE SEIM");

        HashSet<Album> albums = Sets.newHashSet();
        albums.add(album1);
        albums.add(album2);
        albums.add(album3);
        albums.add(album4);
        albums.add(album5);

        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);
        dao.createOrUpdate(album4);
        dao.createOrUpdate(album5);

        List<Integer> busiestYears = ecmMusicianMiner.busiestYears(2);

        assertEquals(2019, busiestYears.get(0));
        assertEquals(2020, busiestYears.get(1));
    }

    @Test
    public void shouldReturnBusiestYears() {
        prepareObjects("Album");
        albums.forEach(album -> { dao.createOrUpdate(album); });
        List<Integer> results = ecmMusicianMiner.busiestYears(2);
        List<Integer> expectedYears = Arrays.asList(2010, 2011);

        assertEquals(2, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedYears.contains(results.get(i)));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnsAlbumWithEmptyYear() {
        Album album = new Album(0, "ECM 1234", "Home");
        dao.createOrUpdate(album);
        List<Integer> results = this.ecmMusicianMiner.busiestYears(4);

        assertEquals(0, results.size());
    }

    @Test
    public void shouldOnlyReturnAListOfYearsItemsWhenThereIsOnlyFourYears() {
        prepareObjects("Album");
        albums.forEach(album -> { dao.createOrUpdate(album); });
        List<Integer> results = this.ecmMusicianMiner.busiestYears(7);

        List<Integer> expectedYears = Arrays.asList(2010, 2011, 2016, 2018);
        assertEquals(4, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedYears.contains(results.get(i)));
        }
    }

    //[ ECM ]---test cases for mostSimilarAlbums
    @Test
    @DisplayName("Album argument should not be null")
    public void throwNullPointerExceptionIfAlbumIsNull() {
        assertThrows(NullPointerException.class, () -> this.ecmAlbumMiner.mostSimilarAlbums(4, null));
    }

    // Integrated from Team 16 codebase
    @Test
    @DisplayName("similar Albums should return all when k is bigger than data size")
    public void similarAlbumsShouldReturnAllWhenKIsBiggerThanDataSize()
    {
        Album album2 = new Album(2020, "ECM 2669", "LIFE GOES ON");
        List<Musician> musicianList2 = Lists.newArrayList();
        musicianList2.add(new Musician("Carla Bley"));
        musicianList2.add(new Musician("Andy Sheppard"));
        musicianList2.add(new Musician("Steve Swallow "));
        album2.setFeaturedMusicians(musicianList2);

        Album album3 = new Album(2019, "ECM 1001", "FREE AT LAST (EXTENDED EDITION)");
        List<Musician> musicianList3 = Lists.newArrayList();
        musicianList3.add(new Musician("Carla Bley"));
        musicianList3.add(new Musician("Andy Sheppard"));
        musicianList3.add(new Musician("Steve Swallow "));
        album3.setFeaturedMusicians(musicianList3);

        List<Album> albums = Lists.newArrayList();
        albums.add(album2);
        albums.add(album3);

        Album album5 = new Album(2016, "ECM 2487", "ANDANDO EL TIEMPO");
        List<Musician> musicianList5 = Lists.newArrayList();
        musicianList5.add(new Musician("Carla Bley"));
        musicianList5.add(new Musician("Andy Sheppard"));
        musicianList5.add(new Musician("Steve Swallow"));
        album5.setFeaturedMusicians(musicianList5);

        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);

        List<Album> similarAlbums = ecmAlbumMiner.mostSimilarAlbums(4, album5);

        assertEquals(2, similarAlbums.size());
    }

    // Integrated from Team 16 codebase
    @Test
    @DisplayName("similar album should return empty list when there is no data in database")
    public void similarAlbumShouldReturnEmptyListWhenNoDataInDatabase()
    {
        Album album5 = new Album(2016, "ECM 2487", "ANDANDO EL TIEMPO");
        List<Musician> musicianList5 = Lists.newArrayList();
        musicianList5.add(new Musician("Carla Bley"));
        musicianList5.add(new Musician("Andy Sheppard"));
        musicianList5.add(new Musician("Steve Swallow"));
        album5.setFeaturedMusicians(musicianList5);

        List<Album> album = ecmAlbumMiner.mostSimilarAlbums(1, album5);
        assertEquals(0, album.size());
    }

    @Test
    public void shouldReturnCorrectSizeListOfSimilarAlbums() {
        Album album = new Album(2018, "ECM 8909", "Home");
        album.setFeaturedMusicians(Arrays.asList(new Musician("Julia Andrews")));

        prepareObjects("Album");
        albums.forEach(a -> { dao.createOrUpdate(a); });
        List<Album> results = this.ecmAlbumMiner.mostSimilarAlbums(3, album);

        List<String> expectedNames = Arrays.asList("Happy", "Home", "Kitaro");
        assertEquals(3, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getAlbumName()));
        }
    }

    @Test
    public void shouldReturnSimilarAlbumsHavingSimilarAlbumName(){
        Album album = new Album(2019, "ECM 3523", "Orange Lemon");
        prepareObjects("Album");
        albums.forEach(a -> { dao.createOrUpdate(a); });
        List<Album> results = this.ecmAlbumMiner.mostSimilarAlbums(4, album);

        assertEquals(1, results.size());
        assertEquals("Orange", results.get(0).getAlbumName());
    }

    @Test
    public void shouldReturnSimilarAlbumsHavingSimilarMusicGroupName(){
        Album album = new Album(2019, "ECM 3523", "Theory");
        MusicianGroup musicianGroup = new MusicianGroup("Andy");
        album.setMusicianGroup(musicianGroup);
        prepareObjects("Album");
        albums.forEach(a -> { dao.createOrUpdate(a); });
        List<Album> results = this.ecmAlbumMiner.mostSimilarAlbums(4, album);

        assertEquals(1, results.size());
        assertEquals("Home", results.get(0).getAlbumName());
        assertEquals("Andy", results.get(0).getMusicianGroup().getGroupName());
    }

    @Test
    public void shouldReturnSimilarAlbumsHavingSimilarMusicians(){
        Album album = new Album(2019, "ECM 3523", "Theory");
        Musician musician = new Musician("Jean Den");
        album.setFeaturedMusicians(Arrays.asList(musician));
        prepareObjects("Album");
        albums.forEach(a -> { dao.createOrUpdate(a); });
        List<Album> results = this.ecmAlbumMiner.mostSimilarAlbums(4, album);

        assertEquals(3, results.size());
        List<String> expectedNames = Arrays.asList("Home", "Morning", "Rose");
        assertEquals(3, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getAlbumName()));
            assertTrue(results.get(i).getFeaturedMusicians().contains(musician));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnsAlbumWithEmptyInformation() {
        Album album = new Album(2019, "ECM 3523", "Theory");
        Album albumDB = new Album();
        dao.createOrUpdate(albumDB);
        List<Album> results = this.ecmAlbumMiner.mostSimilarAlbums(4, album);

        assertEquals(0, results.size());
    }

    @Test
    public void shouldReturnTheHighestRatedAlbum() {
        prepareObjects("Album");
        albums.forEach(a -> { dao.createOrUpdate(a); });
        List<Album> results = ecmAlbumMiner.highestRatedAlbums(1);

        assertEquals(1, results.size());
        assertEquals("Kitaro", results.get(0).getAlbumName());
        List<Integer> expectedRatings = Arrays.asList(3,4,5);
        for (Rating rating: results.get(0).getRatings()) {
            assertTrue(expectedRatings.contains(rating.getScore()));
        }
    }

    @Test
    public void shouldReturnThreeHighestRatedAlbums() {
        prepareObjects("Album");
        albums.forEach(a -> { dao.createOrUpdate(a); });
        List<Album> results = ecmAlbumMiner.highestRatedAlbums(3);

        assertEquals(3, results.size());
        List<String> expectedNames = Arrays.asList("Kitaro", "Orange", "Morning");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getAlbumName()));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnsAlbumWithEmptyRatings() {
        Album album = new Album();
        dao.createOrUpdate(album);
        List<Album> results = this.ecmAlbumMiner.highestRatedAlbums(4);
        assertEquals(0, results.size());
    }

    @Test
    public void shouldReturnSixHighestRatedAlbumsOfSevenAlbumsWhenThereIsOneAlbumHavingNoRatings() {
        prepareObjects("Album");
        albums.forEach(a -> { dao.createOrUpdate(a); });
        List<Album> results = this.ecmAlbumMiner.highestRatedAlbums(7);

        assertEquals(6, results.size());
        List<String> expectedNames = Arrays.asList("Kitaro", "Orange", "Morning","Rose", "Happy","Guitar");
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedNames.contains(results.get(i).getAlbumName()));
        }
    }
    //---test cases for bestSellingAlbums
    @Test
    public void shouldReturnTheBestSellingAlbum() {
        prepareObjects("Album");
        albums.forEach(a -> { dao.createOrUpdate(a); });
        List<Album> results = ecmAlbumMiner.bestSellingAlbums(1);

        assertEquals(1, results.size());
        assertEquals(89, results.get(0).getUnitsSold());
    }

    @Test
    public void shouldReturnFourBestSellingAlbums() {
        prepareObjects("Album");
        albums.forEach(a -> { dao.createOrUpdate(a); });
        List<Album> results = ecmAlbumMiner. bestSellingAlbums(4);
        List<Integer> expectedUnitsSolds = Arrays.asList(67,89,78,68);

        assertEquals(4, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertTrue(expectedUnitsSolds.contains(results.get(i).getUnitsSold()));
        }
    }

    @Test
    public void shouldReturnEmptyListWhenDatabaseReturnsAlbumWithZeroUnitsSold() {
        Album album = new Album();
        dao.createOrUpdate(album);
        List<Album> results = this.ecmAlbumMiner.bestSellingAlbums(4);

        assertEquals(0, results.size());
    }
}