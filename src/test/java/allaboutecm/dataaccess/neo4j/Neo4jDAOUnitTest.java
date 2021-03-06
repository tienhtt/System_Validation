package allaboutecm.dataaccess.neo4j;


import allaboutecm.dataaccess.DAO;
import allaboutecm.model.*;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: add test cases to adequately test the Neo4jDAO class.
 */
class Neo4jDAOUnitTest {
    //private static final String TEST_DB = "target/test-data/test-db.neo4j";

    private static DAO dao;
    private static Session session;
    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void setUp() {
        // See @https://neo4j.com/docs/ogm-manual/current/reference/ for more information.

        // To use an impermanent embedded data store which will be deleted on shutdown of the JVM,
        // you just omit the URI attribute.

        // Impermanent embedded store
        Configuration configuration = new Configuration.Builder().build();

        // Disk-based embedded store
        // Configuration configuration = new Configuration.Builder().uri(new File(TEST_DB).toURI().toString()).build();

        // HTTP data store, need to install the Neo4j desktop app and create & run a database first.
//        Configuration configuration = new Configuration.Builder().uri("http://neo4j:password@localhost:7474").build();

        sessionFactory = new SessionFactory(configuration, Musician.class.getPackage().getName());
        session = sessionFactory.openSession();

        dao = new Neo4jDAO(session);
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
//        File testDir = new File(TEST_DB);
//        if (testDir.exists()) {
////            FileUtils.deleteDirectory(testDir.toPath());
//        }
    }

    @Test
    public void daoIsNotEmpty() {
        assertNotNull(dao);
    }

    // DAO methods should throw null pointer exceptions if called on null
    @Test
    @DisplayName("load all should throw null pointer exception if passed a null")
    public void loadAllShouldThrowNullPointerExceptionIfInputIsNull()
    {
        assertThrows(NullPointerException.class, ()->dao.loadAll(null));
    }

    @Test
    @DisplayName("createOrUpdate should throw null pointer exception if passed a null")
    public void createOrUpdateShouldThrowNullPointerExceptionIfInputIsNull()
    {
        assertThrows(NullPointerException.class, ()->dao.createOrUpdate(null));
    }

    @Test
    @DisplayName("createOrUpdate should throw null pointer exception if passed a null")
    public void createOrUpdateShouldThrowNullPointerExceptionIfOutputIsNull()
    {
        assertThrows(NullPointerException.class, ()->dao.createOrUpdate(null));
    }

    @Test
    @DisplayName("load should throw null pointer exception if passed a null")
    public void loadShouldThrowNullPointerExceptionIfInputIsNull()
    {
        assertThrows(NullPointerException.class, ()->dao.load(null, null));
    }

    @Test
    @DisplayName("delete should throw null pointer exception if passed a null")
    public void deleteShouldThrowNullPointerExceptionIfInputIsNull()
    {
        assertThrows(NullPointerException.class, ()->dao.delete(null));
    }

    @Test
    @DisplayName("Re-use same id if entity already exists")
    public void shouldUseSameIdIfEntityAlreadyExists()
    {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);

        Album album2 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album2.setUnitsSold(1000);
        dao.createOrUpdate(album2);

        assertEquals(dao.loadAll(Album.class).size(), 1);
        assertEquals(dao.load(Album.class, album.getId()).getUnitsSold(), 1000);
    }

    @Test
    @DisplayName("Create or update should throw null pointer exception")
    public void createOrUpdateShouldNotReturnNull()
    {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        assertNotEquals(dao.createOrUpdate(album), null);
    }

    // Successful creation and loading of entity objects
    @Test
    public void successfulCreationAndLoadingOfConcert() {
        assertEquals(0, dao.loadAll(Concert.class).size());
        Concert concert = new Concert();
        dao.createOrUpdate(concert);

        Concert loadedConcert = dao.load(Concert.class, concert.getId());

        assertNotNull(concert.getId());
        assertEquals(concert, loadedConcert);
        assertEquals(1, dao.loadAll(Concert.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfTrack() {
        assertEquals(0, dao.loadAll(Track.class).size());
        Track track = new Track(1, "First song", 120L);
        dao.createOrUpdate(track);

        Track loadedTrack = dao.load(Track.class, track.getId());

        assertNotNull(track.getId());
        assertEquals(track, loadedTrack);
        assertEquals(1, dao.loadAll(Track.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfMusicianGroup() {
        assertEquals(0, dao.loadAll(MusicianGroup.class).size());
        MusicianGroup musicianGroup = new MusicianGroup("A Band");
        dao.createOrUpdate(musicianGroup);

        MusicianGroup loadedGroup = dao.load(MusicianGroup.class, musicianGroup.getId());

        assertNotNull(musicianGroup.getId());
        assertEquals(musicianGroup, loadedGroup);
        assertEquals(1, dao.loadAll(MusicianGroup.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfRating() {
        assertEquals(0, dao.loadAll(Rating.class).size());

        Rating rating = new Rating();
        rating.setSource("Example source");
        rating.setComment("Example comment");
        rating.setScore(9);

        dao.createOrUpdate(rating);

        Rating loadedRating = dao.load(Rating.class, rating.getId());
        assertEquals(rating.getComment(), loadedRating.getComment());
        assertEquals(1, dao.loadAll(Rating.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfMusicianInstrument() {
        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
        Set<MusicalInstrument> musicalInstruments = Sets.newHashSet();
        MusicalInstrument musicalInstrument = new MusicalInstrument("Guitar");
        musicalInstruments.add(musicalInstrument);
        Musician musician = new Musician("Jon Denver");

        MusicianInstrument musicianInstrument = new MusicianInstrument(musician, musicalInstruments);
        dao.createOrUpdate(musicianInstrument);

        MusicianInstrument loadedMusicianInstrument = dao.load(MusicianInstrument.class, musicianInstrument.getId());

        assertNotNull(musicianInstrument.getId());
        assertEquals(musicianInstrument, loadedMusicianInstrument);
        assertEquals(1, dao.loadAll(MusicianInstrument.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfMusicalInstrument() {
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());
        MusicalInstrument createdInstrument = new MusicalInstrument("Guitar");

        dao.createOrUpdate(createdInstrument);
        MusicalInstrument loadedInstrument = dao.load(MusicalInstrument.class, createdInstrument.getId());

        assertNotNull(createdInstrument.getId());
        assertEquals(createdInstrument, loadedInstrument);

        assertEquals(1, dao.loadAll(MusicalInstrument.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfMusician() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Musician.class).size());

        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        dao.createOrUpdate(musician);
        Musician loadedMusician = dao.load(Musician.class, musician.getId());

        assertNotNull(loadedMusician.getId());
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());

        assertEquals(1, dao.loadAll(Musician.class).size());

//        dao.delete(musician);
//        assertEquals(0, dao.loadAll(Musician.class).size());
    }

    @Test
    public void successfulCreationOfConcertAndPerformers() {
        Concert concert = new Concert();
        Musician performer = new Musician("Jon Denver");
        Set<Musician> performers = new HashSet<>();
        performers.add(performer);
        concert.setConcertPerformers(performers);

        dao.createOrUpdate(concert);

        Collection<Musician> loadedPerformers = dao.loadAll(Musician.class);
        assertEquals(1, loadedPerformers.size());
        Musician loadedPerformer = loadedPerformers.iterator().next();

        Concert loadedConcert = dao.load(Concert.class, concert.getId());
        assertEquals(concert, loadedConcert);
        assertEquals(performer.getName(), loadedPerformer.getName());
        assertEquals(concert.getConcertPerformers(), loadedConcert.getConcertPerformers());
    }

    @Test
    public void successfulCreationOfMusicianGroupAndMusiciansAndAlbums() {
        MusicianGroup musicianGroup = new MusicianGroup("The Band");

        // Set musicians (members) for the group
        Musician musician1 = new Musician("Jon Denver");
        Musician musician2 = new Musician("Neil Diamond");
        Set<Musician> groupMusicians = new HashSet<>();

        groupMusicians.add(musician1);
        groupMusicians.add(musician2);
        musicianGroup.setGroupMusicians(groupMusicians);

        // Set albums for the group
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Set<Album> albums = new HashSet<Album>();
        albums.add(album);
        musicianGroup.setAlbums(albums);

        dao.createOrUpdate(musicianGroup);

        MusicianGroup loadedMusicianGroup = dao.load(MusicianGroup.class, musicianGroup.getId());

        Collection<Musician> loadedMusicians = dao.loadAll(Musician.class);
        assertEquals(2, loadedMusicians.size());
        Musician loadedMusician = loadedMusicians.iterator().next();
        assertEquals(musicianGroup.getGroupMusicians(), loadedMusicianGroup.getGroupMusicians());

        Collection<Album> loadedAlbums = dao.loadAll(Album.class);
        assertEquals(1, loadedAlbums.size());
        Album loadedAlbum = loadedAlbums.iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(musicianGroup.getAlbums(), loadedMusicianGroup.getAlbums());
    }


    @Test
    public void successfulCreationAndLoadingOfAlbumAndRatings() {
        Set ratingSet = new HashSet<Rating>();
        Rating rating = new Rating();
        rating.setSource("Example source");
        rating.setComment("Example comment");
        rating.setScore(9);
        ratingSet.add(rating);

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setRatings(ratingSet);

        dao.createOrUpdate(album);

        Album loadedAlbum = dao.load(Album.class, album.getId());
        Collection<Rating> loadedRatings = dao.loadAll(Rating.class);
        Rating loadedRating = loadedRatings.iterator().next();

        assertEquals(album.getRatings(), loadedAlbum.getRatings());
        assertEquals(rating, loadedRating);
    }

    @Test
    public void successfulCreationOfAlbumAndTracks() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track1 = new Track(1, "First song", 120L);

        Set albumTracks = new HashSet<Track>();
        albumTracks.add(track1);
        album.setTracks(albumTracks);

        dao.createOrUpdate(album);

        Collection<Track> tracks = dao.loadAll(Track.class);
        assertEquals(1, tracks.size());
        Track loadedTrack = tracks.iterator().next();

        Album loadedAlbum = dao.load(Album.class, album.getId());
        assertEquals(track1, loadedTrack);
        assertEquals(track1.getTitle(), loadedTrack.getTitle());
        assertEquals(album.getTracks(), loadedAlbum.getTracks());
    }

    @Test
    public void successfulCreationOfMusicianAndAlbum() throws MalformedURLException {
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        musician.setAlbums(Sets.newHashSet(album));

        dao.createOrUpdate(album);
        dao.createOrUpdate(musician);

        Collection<Musician> musicians = dao.loadAll(Musician.class);
        assertEquals(1, musicians.size());
        Musician loadedMusician = musicians.iterator().next();
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());
        assertEquals(musician.getAlbums(), loadedMusician.getAlbums());
    }

    @Test
    @DisplayName("Test delete method for Album entities")
    public void successfulDeletionOfAlbumFromDatabase() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        assertEquals(1, dao.loadAll(Album.class).size());

        dao.delete(album);
        assertEquals(0, dao.loadAll(Album.class).size());
    }

    @Test
    @DisplayName("Test delete method for Musician entities")
    public void successfulDeletionOfMusicianFromDatabase() {
        Musician musician = new Musician("Jon Denver");
        dao.createOrUpdate(musician);
        assertEquals(1, dao.loadAll(Musician.class).size());

        dao.delete(musician);
        assertEquals(0, dao.loadAll(Musician.class).size());

    }

    @Test
    @DisplayName("Test delete method for MusicalInstrument entities")
    public void successfulDeletionOfMusicalInstrumentFromDatabase() {
        MusicalInstrument musicalInstrument = new MusicalInstrument("Guitar");
        dao.createOrUpdate(musicalInstrument);
        assertEquals(1, dao.loadAll(MusicalInstrument.class).size());

        dao.delete(musicalInstrument);
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());
    }

    @Test
    @DisplayName("Test delete method for MusicianInstrument entities")
    public void successfulDeletionOfMusicianInstrumentFromDatabase() {
        MusicianInstrument musicianInstrument = new MusicianInstrument();
        dao.createOrUpdate(musicianInstrument);
        assertEquals(1, dao.loadAll(MusicianInstrument.class).size());

        dao.delete(musicianInstrument);
        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
    }

    @Test
    @DisplayName("Test delete method for Concert entities")
    public void successfulDeletionOfConcertFromDatabase() {
        Concert concert = new Concert();
        dao.createOrUpdate(concert);
        assertEquals(1, dao.loadAll(Concert.class).size());

        dao.delete(concert);
        assertEquals(0, dao.loadAll(Concert.class).size());
    }

    @Test
    @DisplayName("Test delete method for MusicianGroup entities")
    public void successfulDeletionOfMusicianGroupFromDatabase() {
        MusicianGroup musicianGroup = new MusicianGroup();
        dao.createOrUpdate(musicianGroup);
        assertEquals(1, dao.loadAll(MusicianGroup.class).size());

        dao.delete(musicianGroup);
        assertEquals(0, dao.loadAll(MusicianGroup.class).size());
    }

    @Test
    @DisplayName("Test delete method for Track entities")
    public void successfulDeletionOfTrackFromDatabase() {
        Track track = new Track();
        dao.createOrUpdate(track);
        assertEquals(1, dao.loadAll(Track.class).size());

        dao.delete(track);
        assertEquals(0, dao.loadAll(Track.class).size());
    }

    @Test
    @DisplayName("Deletion of a non-existent Track node should throw a 'no such record' exception.")
    public void nonExistentTrackDeletionShouldThrowNoSuchRecordException() {
        Track track = new Track(2, "Song 2", 120L);
        dao.createOrUpdate(track);
        assertEquals(1, dao.loadAll(Track.class).size());

        dao.delete(track);
        assertEquals(0, dao.loadAll(Track.class).size());

        assertThrows(NoSuchRecordException.class, () -> dao.delete(track));
    }

    @Test
    @DisplayName("Deletion of a non-existent Album node should throw a 'no such record' exception.")
    public void nonExistentAlbumDeletionShouldThrowNoSuchRecordException() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        assertEquals(1, dao.loadAll(Album.class).size());

        dao.delete(album);
        assertEquals(0, dao.loadAll(Album.class).size());

        assertThrows(NoSuchRecordException.class, () -> dao.delete(album));
    }

    @Test
    @DisplayName("Deletion of a non-existent Concert node should throw a 'no such record' exception.")
    public void nonExistentConcertDeletionShouldThrowNoSuchRecordException() {
        Concert concert = new Concert();
        dao.createOrUpdate(concert);
        assertEquals(1, dao.loadAll(Concert.class).size());

        dao.delete(concert);
        assertEquals(0, dao.loadAll(Concert.class).size());

        assertThrows(NoSuchRecordException.class, () -> dao.delete(concert));
    }

    @Test
    @DisplayName("Deletion of a non-existent Musician node should throw a 'no such record' exception.")
    public void nonExistentMusicianDeletionShouldThrowNoSuchRecordException() {
        Musician musician = new Musician("Jon Denver");
        dao.createOrUpdate(musician);
        assertEquals(1, dao.loadAll(Musician.class).size());

        dao.delete(musician);
        assertEquals(0, dao.loadAll(Musician.class).size());

        assertThrows(NoSuchRecordException.class, () -> dao.delete(musician));
    }

    @Test
    @DisplayName("Deletion of a non-existent MusicalInstrument node should throw a 'no such record' exception.")
    public void nonExistentInstrumentDeletionShouldThrowNoSuchRecordException() {
        MusicalInstrument musicalInstrument = new MusicalInstrument("Guitar");
        dao.createOrUpdate(musicalInstrument);
        assertEquals(1, dao.loadAll(MusicalInstrument.class).size());

        dao.delete(musicalInstrument);
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());

        assertThrows(NoSuchRecordException.class, () -> dao.delete(musicalInstrument));
    }

    @Test
    @DisplayName("Deletion of a non-existent MusicianInstrument node should throw a 'no such record' exception.")
    public void nonExistentMusicianInstrumentDeletionShouldThrowNoSuchRecordException() {
        MusicianInstrument musicianInstrument = new MusicianInstrument();
        dao.createOrUpdate(musicianInstrument);
        assertEquals(1, dao.loadAll(MusicianInstrument.class).size());

        dao.delete(musicianInstrument);
        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());

        assertThrows(NoSuchRecordException.class, () -> dao.delete(musicianInstrument));
    }

    @Test
    @DisplayName("Deletion of a non-existent MusicianGroup node should throw a 'no such record' exception.")
    public void nonExistentMusicianGroupDeletionShouldThrowNoSuchRecordException() {
        MusicianGroup musicianGroup = new MusicianGroup();
        dao.createOrUpdate(musicianGroup);
        assertEquals(1, dao.loadAll(MusicianGroup.class).size());

        dao.delete(musicianGroup);
        assertEquals(0, dao.loadAll(MusicianGroup.class).size());

        assertThrows(NoSuchRecordException.class, () -> dao.delete(musicianGroup));
    }

    @Test
    @DisplayName("Loading a non-existent entity should throw a 'no such record' exception.")
    public void loadingNonExistentEntityShouldThrowNonExistentRecordException() {
        MusicalInstrument musicalInstrument = new MusicalInstrument("Guitar");

        dao.createOrUpdate(musicalInstrument);
        long fakeId = musicalInstrument.getId() + 1;

        assertThrows(NoSuchRecordException.class, () -> dao.load(MusicalInstrument.class, fakeId));
    }

    @Test
    @DisplayName("Updates should be conducted and loaded successfully.")
    public void successfulUpdateAndLoadAlbum() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        assertEquals(album, dao.load(Album.class, album.getId()));

        Album updatedAlbum = album;
        updatedAlbum.setGenre("Orchestra");
        dao.createOrUpdate(updatedAlbum);

        assertEquals(1, dao.loadAll(Album.class).size());

        Album loadedAlbum = dao.load(Album.class, updatedAlbum.getId());
        assertEquals(loadedAlbum.getId(), album.getId());
        assertEquals(loadedAlbum.getGenre(), updatedAlbum.getGenre());
        assertEquals(loadedAlbum.getGenre(), updatedAlbum.getGenre());
    }

    @Test
    @DisplayName("Test update method for Musician entities")
    public void successfulUpdatingAndLoadingOfMusician() throws MalformedURLException {
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        dao.createOrUpdate(musician);

        assertEquals(musician, dao.load(Musician.class, musician.getId()));
        Musician updatedMusician = musician;
        updatedMusician.setMusicianWikipediaURL(new URL("https://www.keithjarrett.org/"));
        dao.createOrUpdate(updatedMusician);
        assertEquals(1, dao.loadAll(Musician.class).size());

        Musician loadedMusician = dao.load(Musician.class, updatedMusician.getId());
        assertEquals(loadedMusician.getId(), musician.getId());
        assertEquals(loadedMusician.getMusicianWikipediaURL(), updatedMusician.getMusicianWikipediaURL());
    }

    @Test
    @DisplayName("Test update method for Musician entities")
    public void successfulUpdatingAndLoadingOfRating() {
        Rating rating = new Rating(10, "Comment", "Source");
        dao.createOrUpdate(rating);
        assertEquals(rating, dao.load(Rating.class, rating.getId()));

        Rating newRating = rating;
        dao.createOrUpdate(newRating);
        assertEquals(1, dao.loadAll(Rating.class).size());
    }

        @Test
    @DisplayName("Test update method for MusicalInstrument entities")
    public void successfulUpdatingAndLoadingOfMusicalInstrument() {

        MusicalInstrument musicalInstrument = new MusicalInstrument("Guitar");
        dao.createOrUpdate(musicalInstrument);

        assertEquals(musicalInstrument, dao.load(MusicalInstrument.class, musicalInstrument.getId()));
        MusicalInstrument updatedInstrument = musicalInstrument;
        updatedInstrument.setName("violin");
        dao.createOrUpdate(updatedInstrument);
        assertEquals(1, dao.loadAll(MusicalInstrument.class).size());


        MusicalInstrument loadedInstrument = dao.load(MusicalInstrument.class, updatedInstrument.getId());
        assertEquals(loadedInstrument.getId(), musicalInstrument.getId());
        assertEquals(loadedInstrument.getName(), updatedInstrument.getName());
    }

    @Test
    @DisplayName("Test update method for MusicianInstrument entities")
    public void successfulUpdatingAndLoadingOfMusicianInstrument() {

        Set<MusicalInstrument> musicalInstruments = Sets.newHashSet();
        MusicalInstrument musicalInstrument = new MusicalInstrument("Guitar");
        musicalInstruments.add(musicalInstrument);
        Musician musician = new Musician("Jon Denver");

        MusicianInstrument musicianInstrument = new MusicianInstrument(musician, musicalInstruments);
        dao.createOrUpdate(musicianInstrument);

        assertEquals(musicianInstrument, dao.load(MusicianInstrument.class, musicianInstrument.getId()));
        MusicianInstrument updatedMusicianInstrument = musicianInstrument;
        updatedMusicianInstrument.setMusician(new Musician("Philipp Wachsmann"));
        dao.createOrUpdate(updatedMusicianInstrument);
        assertEquals(1, dao.loadAll(MusicianInstrument.class).size());


        MusicianInstrument loadedMusicianInstrument = dao.load(MusicianInstrument.class, updatedMusicianInstrument.getId());

        assertEquals(loadedMusicianInstrument.getId(), musicianInstrument.getId());
        assertEquals(loadedMusicianInstrument.getMusician(), updatedMusicianInstrument.getMusician());
    }

    @Test
    @DisplayName("Test update method for MusicianGroup entities")
    public void successfulUpdatingAndLoadingOfMusicianGroup() {

        MusicianGroup musicianGroup = new MusicianGroup("A Band");
        dao.createOrUpdate(musicianGroup);

        assertEquals(musicianGroup, dao.load(MusicianGroup.class, musicianGroup.getId()));
        MusicianGroup updatedMusicianGroup = musicianGroup;
        updatedMusicianGroup.setGroupName("Jan Garbarek Quartet");
        dao.createOrUpdate(updatedMusicianGroup);
        assertEquals(1, dao.loadAll(MusicianGroup.class).size());


        MusicianGroup loadedGroup = dao.load(MusicianGroup.class, updatedMusicianGroup.getId());
        assertEquals(loadedGroup.getId(), musicianGroup.getId());
        assertEquals(loadedGroup.getGroupName(), updatedMusicianGroup.getGroupName());
    }

    @Test
    @DisplayName("Test update method for Concert entities")
    public void successfulUpdatingAndLoadingOfConcert() {
        Concert concert = new Concert();
        dao.createOrUpdate(concert);
        assertEquals(concert, dao.load(Concert.class, concert.getId()));

        //current day + 1 day
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        date = c.getTime();

        Concert updatedConcert = concert;
        updatedConcert.setConcertDate(new java.sql.Date(date.getTime()));
        dao.createOrUpdate(updatedConcert);
        assertEquals(1, dao.loadAll(Concert.class).size());

        Concert loadedConcert = dao.load(Concert.class, updatedConcert.getId());

        assertEquals(loadedConcert.getId(), concert.getId());
        assertEquals(loadedConcert.getConcertDate(), updatedConcert.getConcertDate());
    }

    @Test
    public void successfulUpdatingAndLoadingOfTrack() {
        Track track = new Track(1, "First song", 120L);
        dao.createOrUpdate(track);

        assertEquals(track, dao.load(Track.class, track.getId()));
        Track updatedTrack = track;
        updatedTrack.setTitle("INCANDESCENT CLOUDS");
        dao.createOrUpdate(updatedTrack);
        assertEquals(1, dao.loadAll(Track.class).size());


        Track loadedTrack = dao.load(Track.class, updatedTrack.getId());
        assertEquals(loadedTrack.getId(), track.getId());
        assertEquals(loadedTrack.getTitle(), updatedTrack.getTitle());
    }

    @Test
    public void successfulUpdateAndLoadOfRating() {
        Rating rating = new Rating();
        rating.setSource("Example source");
        rating.setComment("Example comment");
        rating.setScore(9);

        dao.createOrUpdate(rating);

        Rating updatedRating = rating;
        updatedRating.setComment("Updated comment");
        dao.createOrUpdate(updatedRating);

        Rating loadedRating = dao.load(Rating.class, updatedRating.getId());
        assertEquals(1, dao.loadAll(Rating.class).size());
        assertEquals(updatedRating.getComment(), loadedRating.getComment());
    }

    @Test
    @DisplayName("Existing entity should not return null when entity exists")
    public void createOrUpdateShouldFindExistingEntity()
    {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);

        Album album2 = album;
        album2.setUnitsSold(1000);
        dao.createOrUpdate(album2);

        assertEquals(dao.loadAll(Album.class).size(), 1);
        assertEquals(dao.load(Album.class, album.getId()).getUnitsSold(), 1000);
    }

}