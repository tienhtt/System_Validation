package allaboutecm.model;

import allaboutecm.dataaccess.neo4j.URLConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static org.apache.commons.lang3.Validate.*;

/**
 * Represents an album released by ECM records.
 * See {@https://www.ecmrecords.com/catalogue/143038750696/the-koln-concert-keith-jarrett}
 */
@NodeEntity
public class Album extends Entity {
    @Property(name="releaseYear")
    private int releaseYear;
    @Property(name="recordNumber")
    private String recordNumber;
    @Property(name="albumName")
    private String albumName;
    /**
     * CHANGE: instead of a set, now featuredMusicians is a list,
     * to better represent the order in which musicians are featured in an album.
     */
    @Relationship(type="featuredMusicians")
    private List<Musician> featuredMusicians;
    @Relationship(type="instruments")
    private Set<MusicianInstrument> instruments;
    @Convert(URLConverter.class)
    @Property(name="albumURL")
    private URL albumURL;
    @Relationship(type="tracks")
    private Set<Track> tracks;
    @Property(name="genre")
    private String genre;
    @Property(name="format")
    private String format;
    @Relationship(type="musicianGroup")
    private MusicianGroup musicianGroup;
    @Property(name="unitsSold")
    private int unitsSold;
    @Relationship(type="rating")
    private Set<Rating> ratings;

    public Album(){

    }

    public Album(int releaseYear, String recordNumber, String albumName) {
        notNull(recordNumber);
        notNull(albumName);

        notBlank(recordNumber);
        notBlank(albumName);

        this.releaseYear = releaseYear;
        this.recordNumber = recordNumber;
        this.albumName = albumName;

        this.albumURL = null;

        featuredMusicians = Lists.newArrayList();
        instruments = Sets.newHashSet();
        tracks = Sets.newHashSet();
        musicianGroup = new MusicianGroup();
        ratings = Sets.newHashSet();
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        notNull(recordNumber);
        notBlank(recordNumber);

        String[] recNumComponents = recordNumber.trim().split(" ");

        if (recNumComponents.length != 2) {
            throw new IllegalArgumentException("Record number does not adhere to ECM format.");
        }

        // Validate the first component as letters, and second component as numbers
        isAlpha(recNumComponents[0]);
        isNumeric(recNumComponents[1]);

        this.recordNumber = recordNumber;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        notNull(genre);
        notBlank(genre);
        this.genre = genre;
    }

    public List<Musician> getFeaturedMusicians() {
        return featuredMusicians;
    }

    public void setFeaturedMusicians(List<Musician> featuredMusicians) {
        notNull(featuredMusicians);
        notEmpty(featuredMusicians);
        //validate
        validateDuplication(featuredMusicians);
        this.featuredMusicians = featuredMusicians;
    }

    private void validateDuplication(List<Musician> featuredMusicians) {
        Set<Musician> musicianSet = Sets.newHashSet();
        for (Musician musician: featuredMusicians){
            notNull(musician);
            if (!musicianSet.add(musician))
                throw new IllegalArgumentException("A musician should only be listed once as a featured musician.");
        }
    }

    public Set<MusicianInstrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<MusicianInstrument> instruments) {
        notNull(instruments);
        notEmpty(instruments);
        this.instruments = instruments;
    }

    public URL getAlbumURL() {
        return albumURL;
    }

    public void setAlbumURL(URL albumURL) {
        notNull(albumURL);
        this.albumURL = albumURL;
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        notNull(tracks);
        notEmpty(tracks);
        tracks.forEach(Validate::notNull);
        this.tracks = tracks;
    }



    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {

        //Check whether release year is valid
        if (releaseYear < 1969 || releaseYear > Year.now().getValue()) {
            throw new IllegalArgumentException();
        } else {
            this.releaseYear = releaseYear;
        }
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        notNull(albumName);
        notBlank(albumName);
        if(albumName.length() >= 50){
            throw new IllegalArgumentException("Album name is too long.");
        }

        this.albumName = albumName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        notNull(format);
        validateFormat(format);
        this.format = format;
    }

    // Validate String to ensure a valid format.
    private void validateFormat(String format) {
        if (Stream.of(AlbumFormat.values()).map(AlbumFormat::name)
                .noneMatch(name -> name.equalsIgnoreCase(format))) {
            throw new IllegalArgumentException("The format is incorrect");
        }
    }

    // Validate individual characters of String to ensure they are letters
    private void isAlpha(String letterString) {
        if (!letterString.chars().allMatch(Character::isLetter)) {
            throw new IllegalArgumentException("String should only contain letters.");
        }
    }

    // Validate individual characters of String to ensure they are numbers
    private void isNumeric(String numString) {
        if (!numString.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("String should only contain numbers.");
        }
    }

    public MusicianGroup getMusicianGroup() {
        return musicianGroup;
    }


    public void setMusicianGroup(MusicianGroup musicianGroup) {
        notNull(musicianGroup);

        this.musicianGroup = musicianGroup;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(int unitsSold) {
        if(unitsSold < 0){
            throw new IllegalArgumentException("Invalid sales amount.");
        }
            this.unitsSold = unitsSold;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        notNull(ratings);
        for(Rating r : ratings){
            notNull(r);
        }
        this.ratings = ratings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return releaseYear == album.releaseYear &&
                recordNumber.equals(album.recordNumber) &&
                albumName.equals(album.albumName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(releaseYear, recordNumber, albumName);
    }
}