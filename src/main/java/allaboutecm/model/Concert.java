package allaboutecm.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import java.sql.Date;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Haoyu Yang
 * this class stores the information of a concert hold by musicians
 */
@NodeEntity
public class Concert extends Entity{
    // store the concert holding date
    @DateLong
    private java.sql.Date concertDate;

    // store the performers of the concert
    @Relationship(type="concertPerformers")
    private Set<Musician> concertPerformers;

    // store the locations where the concert holds
    @Property(name="venue")
    private String venue;
    @Property(name="city")
    private String city;
    @Property(name="country")
    private String country;

    public Concert(){

    }
    public Concert(Date concertDate,
                   Set<Musician> concertPerformers,
                   String venue,
                   String city,
                   String country) {
        this.concertDate = concertDate;
        this.concertPerformers = concertPerformers;
        this.venue = venue;
        this.city = city;
        this.country = country;
    }

    public java.sql.Date getConcertDate() {
        return concertDate;
    }

    public void setConcertDate(java.sql.Date concertDate) {
        notNull(concertDate);
        if (concertDate.compareTo(new java.sql.Date(System.currentTimeMillis())) < 1 ) {
            throw new IllegalArgumentException();
        }
        this.concertDate = concertDate;
    }

    public Set<Musician> getConcertPerformers() {
        return concertPerformers;
    }

    public void setConcertPerformers(Set<Musician> concertPerformers) {
        notNull(concertPerformers);
        if (concertPerformers.isEmpty()) {
            throw new IllegalArgumentException("The musician set is empty");
        }
        this.concertPerformers = concertPerformers;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        notNull(venue);
        notBlank(venue);
        this.venue = venue;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        notNull(city);
        notBlank(city);
        if(city.contains("-") || city.contains(" ")){
            String[] cityStrArray = city.split("-| ");
            for (String s : cityStrArray)
                isAlpha(s);
        } else
            isAlpha(city);
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        notNull(country);
        notBlank(country);
        if(country.contains("-") || country.contains(" ")){
            String[] countryStrArray = country.split("-| ");
            for (String s : countryStrArray)
                isAlpha(s);
        } else
            isAlpha(country);
        this.country = country;
    }

    private void isAlpha(String letterString) {
        if (!letterString.chars().allMatch(Character::isLetter)) {
            throw new IllegalArgumentException("String should only contain letters.");
        }
    }
}
