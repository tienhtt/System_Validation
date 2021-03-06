package allaboutecm.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RatingUnitTest {
    private Rating rating;

    @BeforeEach
    void setUp(){
        rating = new Rating();
    }

    @ParameterizedTest
    @ValueSource(ints = {-20, -1, 11, 20})
    @DisplayName("Should throw illegal argument exception if rating score invalid")
    public void constructorShouldThrowIllegalArgWhenInvalidScore(int arg){
        assertThrows(IllegalArgumentException.class, () -> new Rating(arg, "Comment", "Source"));
    }

    // for testing legal score arguments for constructor
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 9, 10})
    @DisplayName("Rating score should be set and returned as expected")
    public void scoreShouldBeSetAsExpectedByConstructor(int arg){
        Rating rating1 = new Rating(arg, "Comment", "Source");
        assertEquals(arg,rating1.getScore());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("Should throw illegal argument exception if comment blank")
    public void constructorShouldThrowIllegalArgWhenBlankComment(String arg){
        assertThrows(IllegalArgumentException.class, () -> new Rating(10, arg, "Source"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("Should throw illegal argument exception if source blank")
    public void constructorShouldThrowIllegalArgWhenBlankSource(String arg){
        assertThrows(IllegalArgumentException.class, () -> new Rating(10, "Comment", arg));
    }

    @Test
    @DisplayName("Should throw illegal argument exception if comment null")
    public void constructorShouldThrowIllegalArgWhenNullComment(){
        assertThrows(NullPointerException.class, () -> new Rating(10, null, "Source"));
    }

    @Test
    @DisplayName("Should throw illegal argument exception if source null")
    public void constructorShouldThrowIllegalArgWhenNullSource(){
        assertThrows(NullPointerException.class, () -> new Rating(10, "Comment", null));
    }

    @Test
    @DisplayName("Non-default constructor works successfully")
    public void nonDefaultConstructorWorksSuccessfully(){
        Rating rating = new Rating(10, "Comment", "Source");
        assertEquals(rating.getSource(), "Source");
        assertEquals(rating.getComment(), "Comment");
        assertEquals(rating.getScore(), 10);
    }

    @ParameterizedTest
    @ValueSource(ints = {-20, -1, 11, 20})
    @DisplayName("Should throw illegal argument exception if rating score invalid")
    public void shouldThrowIllegalArgExceptionWhenInvalidScoreSet(int arg){
        assertThrows(IllegalArgumentException.class, () -> rating.setScore(arg));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 9, 10})
    @DisplayName("Rating score should be set and returned as expected")
    public void scoreShouldBeSetAsExpected(int arg){
        rating.setScore(arg);
        assertEquals(arg,rating.getScore());
    }

    @ParameterizedTest
    @ValueSource(strings = {"This is a valid comment. This comment is exactly" +
            " one hundred 1 characters in length to test the max+."})
    @DisplayName("Should throw illegal argument exception if comment exceeds 100 characters length.")
    public void shouldThrowIllegalArgExceptionIfCommentTooLong(String arg){
        assertThrows(IllegalArgumentException.class, () -> rating.setComment(arg));
    }

    @Test
    @DisplayName("Comment should not be null")
    public void shouldThrowNullPointerExceptionIfCommentNull(){
        assertThrows(NullPointerException.class, () -> rating.setComment(null));
    }

    @Test
    @DisplayName("Comment should not be blank string")
    public void shouldThrowNullPointerExceptionIfCommentBlank(){
        assertThrows(IllegalArgumentException.class, () -> rating.setComment(""));
    }

    @ParameterizedTest
    @ValueSource(strings = {"This is a valid comment. This comment is exactly" +
            " ninety nine characters in length to test the max-.",
            "This is another valid comment. This comment is exactly one" +
                    " hundred characters in length to test max."})
    @DisplayName("Comment should be set and returned as expected")
    public void commentSetAsExpected(String arg){
        rating.setComment(arg);
        assertEquals(arg,rating.getComment());
    }

    @Test
    @DisplayName("Source should not be null")
    public void shouldThrowNullPointerExceptionIfSourceNull(){
        assertThrows(NullPointerException.class, () -> rating.setSource(null));
    }

    @Test
    @DisplayName("Source should not be blank.")
    public void shouldThrowNullPointerExceptionIfSourceBlank(){
        assertThrows(IllegalArgumentException.class, () -> rating.setSource(""));
    }

    @ParameterizedTest
    @ValueSource(strings = {"This is a valid source. This source is length max+."})
    @DisplayName("Source should throw illegal arg exception if longer than 50 characters.")
    public void shouldThrowIllegalArgExceptionIfSourceTooLong(String arg){
        assertThrows(IllegalArgumentException.class, () -> rating.setSource(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"This is a valid source. This source is length max.",
                            "Source example"})
    @DisplayName("Source should be set and returned as expected")
    public void sourceSetAsExpected(String arg){
        rating.setSource(arg);
        assertEquals(arg,rating.getSource());
    }

    @AfterEach
    void tearDown() {
        rating = null;
    }
}
