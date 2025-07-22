import action.AuthorAction;
import action.BookAction;
import io.restassured.internal.RestAssuredResponseImpl;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pojo.author.AuthorPojo;
import pojo.book.BookPojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class AuthorTest extends BaseTest {
    private int bookId;
    private final AuthorAction authorAction;

    public AuthorTest() {
        authorAction = new AuthorAction();
    }

    @BeforeMethod
    public void setup() {
        // In a real api, we would never assign the id via post, it should be done by the server and we would save the id from the response
        BookPojo bookBody = new BookPojo(803,
                "Setup book title",
                "Setup book description",
                25,
                "Setup book excerpt",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        RestAssuredResponseImpl response = new BookAction().post(bookBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        bookId = response.as(BookPojo.class).getId();
    }

    @AfterMethod
    public void cleanup() {
        RestAssuredResponseImpl response = new BookAction().delete(bookId);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
    }

    @Test(description = "User is able to get all authors")
    public void getAllAuthors() {
        RestAssuredResponseImpl response = authorAction.get();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        Assert.assertTrue(Arrays.stream(response.as(AuthorPojo[].class)).findAny().isPresent());
    }

    @Test(description = "User is able to get a author by id")
    public void getAuthorById() {
        RestAssuredResponseImpl response = authorAction.getById(1);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        Assert.assertEquals(response.as(AuthorPojo.class).getId(), 1);
    }

    @Test(description = "User can not get an author by id that does not exist")
    public void getAuthorByIdDoesNotExist() {
        RestAssuredResponseImpl response = authorAction.getById(1000000);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_NOT_FOUND, "Status code does not match");
    }

    @Test(description = "User can not find results when searching for an author by book id that does not exist")
    public void getAuthorByBookIdDoesNotExist() {
        RestAssuredResponseImpl response = authorAction.getByBookId(90000);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        Assert.assertEquals(response.as(AuthorPojo[].class).length, 0);
    }

    @Test(description = "User is able to create an author")
    public void postAuthor() {
        int authorId = 70000;
        AuthorPojo body = AuthorPojo.builder()
                .id(authorId)
                .idBook(bookId)
                .firstName("First name of author")
                .lastName("Last name of author")
                .build();
        RestAssuredResponseImpl response = authorAction.post(body);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");

        AuthorPojo responseBody = response.as(AuthorPojo.class);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(responseBody.getIdBook(), body.getIdBook());
        softAssert.assertEquals(responseBody.getFirstName(), body.getFirstName());
        softAssert.assertEquals(responseBody.getLastName(), body.getLastName());
        softAssert.assertAll("Different data in author creation");
    }

    @Test(description = "User is able to create and then search for an author")
    public void postAndGetAuthor() {
        int authorId = 70000;
        AuthorPojo body = AuthorPojo.builder()
                .id(authorId)
                .idBook(bookId)
                .firstName("First name of author")
                .lastName("Last name of author")
                .build();
        RestAssuredResponseImpl response = authorAction.post(body);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");

        AuthorPojo responseBody = response.as(AuthorPojo.class);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(responseBody.getIdBook(), body.getIdBook());
        softAssert.assertEquals(responseBody.getFirstName(), body.getFirstName());
        softAssert.assertEquals(responseBody.getLastName(), body.getLastName());
        softAssert.assertAll("Different data in author creation");

        response = authorAction.getById(authorId);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");

        responseBody = response.as(AuthorPojo.class);
        softAssert.assertEquals(responseBody.getIdBook(), body.getIdBook());
        softAssert.assertEquals(responseBody.getFirstName(), body.getFirstName());
        softAssert.assertEquals(responseBody.getLastName(), body.getLastName());
        softAssert.assertAll("Different data when getting a book");
    }

    @Test(description = "User is able to create and then delete an author")
    public void postAndDeleteAuthor() {
        int authorId = 60000;
        AuthorPojo body = AuthorPojo.builder()
                .id(authorId)
                .idBook(bookId)
                .firstName("First name of author")
                .lastName("Last name of author")
                .build();
        RestAssuredResponseImpl response = authorAction.post(body);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");

        AuthorPojo responseBody = response.as(AuthorPojo.class);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(responseBody.getIdBook(), body.getIdBook());
        softAssert.assertEquals(responseBody.getFirstName(), body.getFirstName());
        softAssert.assertEquals(responseBody.getLastName(), body.getLastName());
        softAssert.assertAll("Different data in author creation");

        response = authorAction.delete(authorId);

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");

        // Verify author was deleted
        response = authorAction.getById(authorId);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_NOT_FOUND, "Status code does not match");
    }
}
