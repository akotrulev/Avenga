import action.BookAction;
import io.restassured.internal.RestAssuredResponseImpl;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.*;
import pojo.book.BookPojo;
import pojo.error.ErrorPojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PutBookTest extends BaseTest {

    private BookPojo body;
    private int bookId;
    private final BookAction bookAction;

    public PutBookTest() {
        bookAction = new BookAction();
    }

    @BeforeMethod
    public void setup() {
        // In a real api, we would never assign the id via post, it should be done by the server and we would save the id from the response
        body = new BookPojo(601,
                "Setup book title",
                "Setup book description",
                25,
                "Setup book excerpt",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        RestAssuredResponseImpl response = bookAction.post(body);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        bookId = response.as(BookPojo.class).getId();
    }

    @AfterMethod
    public void cleanup() {
        RestAssuredResponseImpl response = bookAction.delete(bookId);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
    }

    @Test(description = "User can update a book's title")
    public void updateBookTitle() {
        BookPojo bookRequestBody = body.toBuilder().build();
        bookRequestBody.setTitle("Updated title");
        RestAssuredResponseImpl response = bookAction.put(bookId, bookRequestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        BookPojo responseBody = response.as(BookPojo.class);
        Assert.assertEquals(responseBody, bookRequestBody);
    }

    @Test(description = "User can update a book's description")
    public void updateBookDescription() {
        BookPojo bookRequestBody = body.toBuilder().build();
        bookRequestBody.setDescription("Updated description");
        RestAssuredResponseImpl response = bookAction.put(bookId, bookRequestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        BookPojo responseBody = response.as(BookPojo.class);
        Assert.assertEquals(responseBody, bookRequestBody);
    }

    @Test(description = "User can update a book's page count")
    public void updateBookPageCount() {
        BookPojo bookRequestBody = body.toBuilder().build();
        bookRequestBody.setPageCount(30);
        RestAssuredResponseImpl response = bookAction.put(bookId, bookRequestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        BookPojo responseBody = response.as(BookPojo.class);
        Assert.assertEquals(responseBody, bookRequestBody);
    }

    @Test(description = "User can update a book's excerpt")
    public void updateBookExcerpt() {
        BookPojo bookRequestBody = body.toBuilder().build();
        bookRequestBody.setExcerpt("Updated excerpt");
        RestAssuredResponseImpl response = bookAction.put(bookId, bookRequestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        BookPojo responseBody = response.as(BookPojo.class);
        Assert.assertEquals(responseBody, bookRequestBody);
    }

    @Test(description = "User can not update a book's id after it has been created")
    public void cannotUpdateBookId() {
        BookPojo bookRequestBody = body.toBuilder().build();
        bookRequestBody.setId(700);
        RestAssuredResponseImpl response = bookAction.put(bookId, bookRequestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST, "Status code does not match");
        ErrorPojo responseBody = response.as(ErrorPojo.class);
        Assert.assertEquals(responseBody.getTitle(), "One or more validation errors occurred.");
    }
}
