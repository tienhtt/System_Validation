package allaboutecm.model;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

import static org.apache.commons.lang3.Validate.*;

@NodeEntity
public class MusicianGroup extends Entity {
    @Property(name="groupName")
    private String groupName;
    @Relationship(type="groupMusicians")
    private Set<Musician> groupMusicians;
    @Relationship(type="albums")
    private Set<Album> albums;

    public MusicianGroup(){

    }

    public MusicianGroup(String groupName)
    {
        notNull(groupName);
        notBlank(groupName);


        this.groupName = groupName;
        albums = Sets.newHashSet();
        groupMusicians= Sets.newHashSet();


    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        notNull(groupName);
        notBlank(groupName);

        this.groupName = groupName;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        notNull(albums);
        albums.forEach(Validate::notNull);
        this.albums = albums;
    }


    public Set<Musician> getGroupMusicians() {
        return groupMusicians;
    }

    public void setGroupMusicians(Set<Musician> groupMusicians) {
        notNull(groupMusicians);
        notEmpty(groupMusicians);

        if (groupMusicians.size() < 2){
            throw new IllegalArgumentException("A musician group must contain at least one musician.");
        }

        this.groupMusicians = groupMusicians;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicianGroup musicianGroup = (MusicianGroup) o;
        return  groupName.equals(musicianGroup.groupName)
                && groupMusicians.equals(musicianGroup.groupMusicians)
                && albums.equals(musicianGroup.albums);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, groupMusicians, albums);
    }
}
