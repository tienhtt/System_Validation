package allaboutecm.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
@NodeEntity
public class Track extends Entity {
    @Property(name="title")
    private String title;
    @Property(name="trackNumber")
    private int trackNumber;
    @Property(name="duration")
    private long duration;
    @Property(name="composers")
    private String[] composers;

    public Track(){
    }

    public Track(int trackNumber, String title, long duration) {
        notNull(title);
        notBlank(title);
        notLessThanAndEqualZero(trackNumber <= 0);
        notLessThanAndEqualZero(duration <= 0);

        this.trackNumber = trackNumber;
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        notNull(title);
        notBlank(title);
        this.title = title;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        notLessThanAndEqualZero(trackNumber <= 0);
        this.trackNumber = trackNumber;
    }

    private void notLessThanAndEqualZero(boolean b) {
        if (b)
            throw new IllegalArgumentException("Value cannot be less than and equal 0");
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        notLessThanAndEqualZero(duration <= 0);
        this.duration = duration;
    }

    public String[] getComposers() {
        return composers;
    }

    public void setComposers(String[] composers) {
        this.composers = composers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track that = (Track) o;
        return trackNumber == (that.trackNumber) &&
                title.equals(that.title) &&
                duration == that.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, trackNumber, duration);
    }
}