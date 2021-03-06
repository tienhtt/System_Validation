package allaboutecm.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import static org.apache.commons.lang3.Validate.*;


@NodeEntity
public class Rating extends Entity{
    @Property(name="score")
    private int score;
    @Property(name="comment")
    private String comment;
    @Property(name="source")
    private String source;

    public Rating(){

    }

    public Rating(int score, String comment, String source){
        if(score < 0 || score > 10){
            throw new IllegalArgumentException("Score should be between 0 and 10 inclusive.");
        }
        notNull(comment);
        notBlank(comment);
        notNull(source);
        notBlank(source);

        this.score = score;
        this.comment = comment;
        this.source = source;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
            if(score < 0 || score > 10){
                throw new IllegalArgumentException("Score should be between 0 and 10 inclusive.");
            }
            else {
                this.score = score;
            }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        notNull(comment);
        notEmpty(comment);

        if(comment.length() > 100){
            throw new IllegalArgumentException("Comment should be 100 characters or less");
        }
        else{
            this.comment = comment;
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        notNull(source);
        notEmpty(source);

        if(source.length() > 50){
            throw new IllegalArgumentException("Source should be 50 characters or less");
        }
        else{
            this.source = source;
        }
    }
}
