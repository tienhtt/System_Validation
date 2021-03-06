package allaboutecm.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
@NodeEntity
public class MusicalInstrument extends Entity {
    @Property(name="name")
    private String name;

    public MusicalInstrument(){
    }

    public MusicalInstrument(String name) {
        notNull(name);
        notBlank(name);
        isAlpha(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        notNull(name);
        notBlank(name);
        isAlpha(name);
        this.name = name;
    }

    private void isAlpha(String name) {
        if (!name.chars().allMatch(Character::isLetter)) {
            throw new IllegalArgumentException("The name is not alpha");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicalInstrument that = (MusicalInstrument) o;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
