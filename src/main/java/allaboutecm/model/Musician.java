package allaboutecm.model;

import allaboutecm.dataaccess.neo4j.URLConverter;
import com.google.common.collect.Sets;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.net.URL;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * An artist that has been featured in (at least) one ECM record.
 * <p>
 * See {@https://www.ecmrecords.com/artists/1435045745}
 */
@NodeEntity
public class Musician extends Entity {
    @Property(name = "name")
    private String name;
    @Convert(URLConverter.class)
    @Property(name = "musicianURL")
    private URL musicianUrl;
    @Relationship(type = "albums")
    private Set<Album> albums;

    // store the musician's brief biography
    @Property(name = "musicianBiography")
    private String musicianBiography;

    // store the Wikipedia website url for the musician
    @Convert(URLConverter.class)
    @Property(name = "musicianWikipediaURL")
    private URL musicianWikipediaURL;

    // store the fans sites of the musician
    @Convert(URLConverter.class)
    @Property(name = "fansSitesURLs")
    private Set<URL> fansSitesURLs;

    // store the concerts hold by the musician
    @Relationship(type = "concerts")
    private Set<Concert> concerts;


    public Musician(String name) {
        notNull(name);
        notBlank(name);

        this.name = formatMusicianName(name);
        this.musicianUrl = null;
        this.musicianBiography = "";
        this.fansSitesURLs = null;
        this.musicianWikipediaURL = null;
        this.concerts = null;

        albums = Sets.newLinkedHashSet();
    }

    public String getName() {
        return name;
    }

    // the name setter for Musician
    public void setName(String name) {
        notNull(name);
        notBlank(name);
        this.name = formatMusicianName(name);
    }

    // check whether the input string is only alphabetical
    private static void isAlpha(String singleName) {
        if (!singleName.chars().allMatch(Character::isLetter)) {
            throw new IllegalArgumentException("The single name is not alpha");
        }
    }


    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        notNull(albums);

        // Check whether any album in the set is non-null
        for (Album album : albums) {
            if (album == null) throw new NullPointerException("albums null element");
        }
        // Check whether album set is non-empty
        if (albums.isEmpty()) {
            throw new IllegalArgumentException("The album set is empty");
        }
        this.albums = albums;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician that = (Musician) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public URL getMusicianUrl() {
        return musicianUrl;
    }

    public void setMusicianUrl(URL musicianUrl) {
        notNull(musicianUrl);
        this.musicianUrl = musicianUrl;
    }

    // musician biography setter
    public void setMusicianBiography(String musicianBiography) {
        notNull(musicianBiography);
        this.musicianBiography = musicianBiography;
    }

    // musician biography getter
    public String getMusicianBiography() {
        return this.musicianBiography;
    }

    // musician Wikipedia Url getter
    public URL getMusicianWikipediaURL() {
        return musicianWikipediaURL;
    }

    // musician Wikipedia Url setter
    public void setMusicianWikipediaURL(URL musicianWikipediaURL) {
        notNull(musicianWikipediaURL);
        this.musicianWikipediaURL = musicianWikipediaURL;
    }

    public Set<URL> getFansSitesURLs() {
        return fansSitesURLs;
    }

    public void setFansSitesURLs(Set<URL> fansSitesURLs) {
        notNull(fansSitesURLs);
        if (fansSitesURLs.isEmpty()) {
            throw new IllegalArgumentException("The fansSitesURLs set is empty");
        }
        this.fansSitesURLs = fansSitesURLs;
    }

    // musician concerts getter
    public Set<Concert> getConcerts() {
        return concerts;
    }

    // musician concerts setter
    public void setConcerts(Set<Concert> concerts) {
        notNull(concerts);
        if (concerts.isEmpty()) {
            throw new IllegalArgumentException("The concerts set is empty");
        }
        this.concerts = concerts;
    }

    // change the name to valid format
    private static String formatMusicianName(String name) {
        StringBuilder validName = new StringBuilder();
        String[] names;

        // Check if name has two parts separated by space
        if (name.trim().contains(" ")) {
            names = name.trim().split(" ");
        } else {
            throw new IllegalArgumentException("The name is not separated by space");
        }

        for (int k = 0; k < names.length; k++) {
            validName.append(k == 0 ? "" : " ");

            if (names[k].trim().contains("-")) {
                String[] oneNames = names[k].trim().split("-");

                for (int i = 0; i < oneNames.length; i++) {
                    isAlpha(oneNames[i]);
                    validName.append(i == 0 ? "" : "-")
                            .append(oneNames[i].trim().substring(0, 1).toUpperCase())
                            .append(oneNames[i].trim().substring(1, oneNames[i].length()).toLowerCase());
                }
            } else {
                isAlpha(names[k]);
                validName.append(names[k].trim().substring(0, 1).toUpperCase())
                        .append(names[k].trim().substring(1, names[k].length()).toLowerCase());
            }
        }
        return validName.toString();
}
}
