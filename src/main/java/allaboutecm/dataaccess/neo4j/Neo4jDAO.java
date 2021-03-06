package allaboutecm.dataaccess.neo4j;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.*;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;

import java.util.Collection;

import static org.neo4j.ogm.cypher.ComparisonOperator.EQUALS;

public class Neo4jDAO implements DAO {
    private static final int DEPTH_LIST = 0;
    private static final int DEPTH_ENTITY = 1;

    private Session session;

    public Neo4jDAO(Session session) {
        this.session = session;
    }

    @Override
    public <T extends Entity> T load(Class<T> clazz, Long id) {
        // Throw exception if node doesn't exist
        if (session.load(clazz, id, DEPTH_ENTITY) == null) {
            throw new NoSuchRecordException(("That " + clazz.toString() + " does not exist in the database."));
        }
        return session.load(clazz, id, DEPTH_ENTITY);
    }

    @Override
    public <T extends Entity> T createOrUpdate(T entity) {
        Class clazz = entity.getClass();

        T existingEntity = findExistingEntity(entity, clazz);
        if (null != existingEntity) {
            entity.setId(existingEntity.getId());
        }

        Transaction tx = session.beginTransaction();
        session.save(entity, DEPTH_ENTITY);
        tx.commit();

        return entity;
    }

    @Override
    public <T extends Entity> Collection<T> loadAll(Class<T> clazz) {
        return session.loadAll(clazz, DEPTH_LIST);
    }

    @Override
    public <T extends Entity> void delete(T entity) {
        Class clazz = entity.getClass();
        Object loadedEntity = session.load(clazz, entity.getId());

        // Throw exception if node doesn't exist.
        if (null == loadedEntity) {
            throw new NoSuchRecordException("No matching " + clazz.toString() + " found in database.");
        } else {
            session.delete(entity);
        }
    }

    private <T extends Entity> T findExistingEntity(Entity entity, Class clazz) {
        Filters filters = new Filters();
        Collection<? extends Entity> collection;
        if (clazz.equals(Album.class)) {
            // Album
            Album album = (Album) entity;
            filters.add(new Filter("albumName", EQUALS, album.getAlbumName())
                    .and(new Filter("recordNumber", EQUALS, album.getRecordNumber()))
                    .and(new Filter("releaseYear", EQUALS, album.getReleaseYear())));
            collection = session.loadAll(Album.class, filters);
        } else if (clazz.equals(Musician.class)) {
            // Musician
            Musician musician = (Musician) entity;
            filters.add(new Filter("name", EQUALS, musician.getName()));
            collection = session.loadAll(Musician.class, filters);
        } else if (clazz.equals(MusicalInstrument.class)) {
            // MusicalInstrument
            MusicalInstrument musicalInstrument = (MusicalInstrument) entity;
            filters.add(new Filter("name", EQUALS, musicalInstrument.getName()));
            collection = session.loadAll(MusicalInstrument.class, filters);
        } else if (clazz.equals(MusicianInstrument.class)) {
            // MusicianInstrument
            MusicianInstrument musicianInstrument = (MusicianInstrument) entity;
            filters.add(new Filter("musician", EQUALS, musicianInstrument.getMusician()))
                    .and(new Filter("musicalInstruments", EQUALS, musicianInstrument.getMusicalInstruments()));
            collection = session.loadAll(MusicianInstrument.class, filters);
        } else if (clazz.equals(Concert.class)) {
            // Concert
            Concert concert = (Concert) entity;
            filters.add(new Filter("concertDate", EQUALS, concert.getConcertDate()))
                    .and(new Filter("concertVenue", EQUALS, concert.getVenue()));
            collection = session.loadAll(Concert.class, filters);
        } else if (clazz.equals(MusicianGroup.class)) {
            // MusicianGroup
            MusicianGroup mg = (MusicianGroup) entity;
            filters.add(new Filter("groupName", EQUALS, mg.getGroupName()));
            collection = session.loadAll(MusicianGroup.class, filters);
        } else if (clazz.equals(Track.class)) {
            // Track
            Track track = (Track) entity;
            filters.add(new Filter("title", EQUALS, track.getTitle()))
                    .and(new Filter("duration", EQUALS, track.getDuration()));
            collection = session.loadAll(Track.class, filters);
        } else {
            // Rating
            Rating rating = (Rating) entity;
            filters.add(new Filter("score", EQUALS, rating.getScore()))
                    .and(new Filter("comment", EQUALS, rating.getComment()))
                    .and(new Filter("source", EQUALS, rating.getSource()));
            collection = session.loadAll(Rating.class, filters);
        }

        Entity existingEntity = null;
        if (!collection.isEmpty()) {
            existingEntity = collection.iterator().next();
        }

        return (T) existingEntity;
    }


}