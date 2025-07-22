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
import pojo.error.ErrorPojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PutAuthorTest extends BaseTest {
    private int bookId;
    private int authorId;
    private AuthorPojo authorBody;
    private final AuthorAction authorAction;

    public PutAuthorTest() {
        authorAction = new AuthorAction();
    }

    @BeforeMethod
    public void setup() {
        // In a real api, we would never assign the id via post, it should be done by the server and we would save the id from the response
        BookPojo body = new BookPojo(601,
                "Setup book title",
                "Setup book description",
                25,
                "Setup book excerpt",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        RestAssuredResponseImpl response = new BookAction().post(body);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        bookId = response.as(BookPojo.class).getId();

        authorBody = AuthorPojo.builder()
                .id(70000)
                .idBook(bookId)
                .firstName("First name of author")
                .lastName("Last name of author")
                .build();
        response = authorAction.post(authorBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        authorId = response.as(AuthorPojo.class).getId();
    }

    @AfterMethod
    public void cleanup() {
        RestAssuredResponseImpl response = new BookAction().delete(bookId);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");

        response = authorAction.delete(authorId);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
    }

    @Test(description = "User can update an author's first name")
    public void updateAuthorFirstName() {
        AuthorPojo authorRequestBody = authorBody.toBuilder().build();
        authorRequestBody.setFirstName("Updated first name");
        RestAssuredResponseImpl response = authorAction.put(authorId, authorRequestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        AuthorPojo responseBody = response.as(AuthorPojo.class);
        Assert.assertEquals(responseBody, authorRequestBody);
    }

    @Test(description = "User can update an author's last name")
    public void updateAuthorLastName() {
        AuthorPojo authorRequestBody = authorBody.toBuilder().build();
        authorRequestBody.setLastName("Updated last name");
        RestAssuredResponseImpl response = authorAction.put(authorId, authorRequestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        AuthorPojo responseBody = response.as(AuthorPojo.class);
        Assert.assertEquals(responseBody, authorRequestBody);
    }

    @Test(description = "User can update an author's book id")
    public void updateAuthorBookId() {
        AuthorPojo authorRequestBody = authorBody.toBuilder().build();
        authorRequestBody.setIdBook(30);
        RestAssuredResponseImpl response = authorAction.put(authorId, authorRequestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        AuthorPojo responseBody = response.as(AuthorPojo.class);
        Assert.assertEquals(responseBody, authorRequestBody);
    }

    @Test(description = "User can not update a author's id after they has been created")
    public void cannotUpdateAuthorId() {
        AuthorPojo authorRequestBody = authorBody.toBuilder().build();
        authorRequestBody.setId(700);
        RestAssuredResponseImpl response = authorAction.put(authorId, authorRequestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST, "Status code does not match");
        ErrorPojo responseBody = response.as(ErrorPojo.class);
        Assert.assertEquals(responseBody.getTitle(), "One or more validation errors occurred.");
    }
}
