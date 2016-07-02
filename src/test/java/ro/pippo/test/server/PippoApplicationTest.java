package ro.pippo.test.server;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.SessionConfig;
import ro.pippo.test.server.PippoApplication;
import com.jayway.restassured.filter.session.SessionFilter;
import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.junit.ClassRule;
import ro.pippo.test.PippoRule;
import ro.pippo.test.PippoTest;

/**
 * @author Herman Barrantes
 */
public class PippoApplicationTest extends PippoTest {

    @ClassRule
    public static PippoRule pippoRule = new PippoRule(new PippoApplication());

    @Test
    public void testText() {
        given()
             .accept(ContentType.TEXT)
        .when()
            .get("/text")
        .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.TEXT)
            .and()
            .body(containsString("Hello World"));
    }
    
    @Test
    public void testJson() {
        given()
             .accept(ContentType.JSON)
        .when()
            .get("/json")
        .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.JSON)
            .and()
            .body("id",equalTo(12345))
            .body("name",equalTo("John"))
            .body("phone",equalTo("0733434435"))
            .body("address",equalTo("Sunflower Street, No. 6"));
    }
    
    @Test
    public void testXml() {
        given()
             .accept(ContentType.XML)
        .when()
            .get("/xml")
        .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.XML)
            .and()
            .body("Contact.id",equalTo("12345"))// I do not know why requires string
            .body("Contact.name",equalTo("John"))
            .body("Contact.phone",equalTo("0733434435"))
            .body("Contact.address",equalTo("Sunflower Street, No. 6"));
    }
    
    @Test
    public void testNegotiate() {
        //XML
        given()
             .accept(ContentType.XML)
        .when()
            .get("/negotiate")
        .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.XML)
            .and()
            .body("Contact.id",equalTo("12345"))// I do not know why requires string
            .body("Contact.name",equalTo("John"))
            .body("Contact.phone",equalTo("0733434435"))
            .body("Contact.address",equalTo("Sunflower Street, No. 6"));
        //JSON
        given()
             .accept(ContentType.JSON)
        .when()
            .get("/negotiate")
        .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.JSON)
            .and()
            .body("id",equalTo(12345))
            .body("name",equalTo("John"))
            .body("phone",equalTo("0733434435"))
            .body("address",equalTo("Sunflower Street, No. 6"));
    }
    
    @Test
    public void testTemplate() {
        given()
             .accept(ContentType.HTML)
        .when()
            .get("/template")
        .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.HTML)
            .and()
            .body("html.body.h1",equalTo("Hello"));
    }
    
    @Test
    public void testSession() {
        SessionFilter sessionFilter = new SessionFilter();
        RestAssured.config = RestAssured.config().sessionConfig(new SessionConfig().sessionIdName("SESSIONID"));
        //SET
        given()
             .accept(ContentType.TEXT)
             .filter(sessionFilter)
        .when()
            .get("/session/set")
        .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.TEXT)
            .and()
            .body(containsString("Hello World"));
        //GET
        given()
             .accept(ContentType.TEXT)
             .filter(sessionFilter)
        .when()
            .get("/session/get")
        .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.TEXT)
            .and()
            .body(containsString("Hello World"));
        //DELETE
        given()
             .accept(ContentType.TEXT)
             .filter(sessionFilter)
        .when()
            .get("/session/delete")
        .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.TEXT)
            .and()
            .body(containsString("Hello World"));
        //GET
        given()
             .accept(ContentType.TEXT)
             .filter(sessionFilter)
        .when()
            .get("/session/get")
        .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.TEXT)
            .and()
            .body(isEmptyString());
    }

}
