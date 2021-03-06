package allaboutecm.dataaccess.neo4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class URLConverterUnitTest {

    URLConverter urlConverter = new URLConverter();

    // Convert to Graph Property
    // 1. If the URL is null, then return null
    @Test
    public void shouldReturnNullURLIfURLIsNull(){
        assertEquals(null, urlConverter.toGraphProperty(null));
    }

    // 2. If the URL is not null, then return toString
    @Test
    public void shouldConvertToString() throws MalformedURLException {
        String url = "http://www.ecmrecords.com//catelogue/";
        URL value = new URL(url);
        assertEquals(url, urlConverter.toGraphProperty(value));
    }


    // Convert to Entity Attribute
    // 1. If the value is null, then return null
    @Test
    public void shouldReturnNullValueIfValueIsNull(){
        assertEquals(null, urlConverter.toEntityAttribute(null));
    }

    // 2. If the value is not null, then return new URL
    @Test
    public void shouldConvertToURL () throws MalformedURLException {
        String value = "https://www.keithjarrett.org/";
        URL url = new URL(value);
        assertEquals(url, urlConverter.toEntityAttribute(value));
    }

    // 3. If the input value is illegal, then display an error message: "Cannot convert input String to URL"
    @Test
    @DisplayName("Cannot convert string to URL")
    public void inputInvalidStringCannotConvertToURL() {
        String value = "123";
        assertThrows(IllegalArgumentException.class, ()->urlConverter.toEntityAttribute(value));
    }
}
