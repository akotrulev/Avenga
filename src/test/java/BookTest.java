import action.BookAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.internal.RestAssuredResponseImpl;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pojo.error.ErrorPojo;
import pojo.book.BookPojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class BookTest extends BaseTest {

    @Test(description = "User is able to get all books")
    public void getAllBooks() {
        RestAssuredResponseImpl response = new BookAction().get();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        Assert.assertTrue(Arrays.stream(response.as(BookPojo[].class)).findAny().isPresent());
    }

    @Test(description = "User is able to get a book by id")
    public void getBookById() {
        RestAssuredResponseImpl response = new BookAction().getById(1);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        Assert.assertEquals(response.as(BookPojo.class).getId(), 1);
    }

    @Test(description = "User can not get a book by id that does not exist")
    public void getBookByIdDoesNotExist() {
        RestAssuredResponseImpl response = new BookAction().getById(1000000);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_NOT_FOUND, "Status code does not match");
    }

    @DataProvider(name = "page_count")
    public Object[][] pageCountProvider() {
        return new Object[][]{{1}, {5}, {0}, {2000000000}};
    }

    @Test(description = "User is able to create a book with integer pages", dataProvider = "page_count")
    public void postValidBook(int pageCount) {
        BookPojo body = BookPojo.builder()
                .title("test title")
                .publishDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .pageCount(pageCount)
                .build();
        RestAssuredResponseImpl response = new BookAction().post(body);
        SoftAssert softAssert = new SoftAssert();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        BookPojo responseBody = response.as(BookPojo.class);
        softAssert.assertEquals(responseBody.getPageCount(), body.getPageCount());
        softAssert.assertEquals(responseBody.getTitle(), body.getTitle());
        softAssert.assertEquals(responseBody.getPublishDate(), body.getPublishDate());
        softAssert.assertAll();
    }

    @Test(description = "User is not able to create a book when page count is a double")
    public void postDoublePageCountBook() {
        ObjectNode objectNode = new ObjectMapper().valueToTree(new BookPojo());
        objectNode.put("pageCount", 1.3435);

        RestAssuredResponseImpl response = new BookAction().postGenericBody(objectNode);
        SoftAssert softAssert = new SoftAssert();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST, "Status code does not match");
        ErrorPojo responseBody = response.as(ErrorPojo.class);
        softAssert.assertEquals(responseBody.getTitle(), "One or more validation errors occurred.");
        softAssert.assertEquals(responseBody.getErrors().getPageCountErrors().getFirst(), "The JSON value could not be converted to System.Int32. Path: $.pageCount | LineNumber: 0 | BytePositionInLine: 26.");
        softAssert.assertAll();
    }

    @Test(description = "User is not able to create a book when page count is a string")
    public void postStringPageCountBook() {
        ObjectNode objectNode = new ObjectMapper().valueToTree(new BookPojo());
        objectNode.put("pageCount", "bad string");

        RestAssuredResponseImpl response = new BookAction().postGenericBody(objectNode);
        SoftAssert softAssert = new SoftAssert();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST, "Status code does not match");
        ErrorPojo responseBody = response.as(ErrorPojo.class);
        softAssert.assertEquals(responseBody.getTitle(), "One or more validation errors occurred.");
        softAssert.assertEquals(responseBody.getErrors().getPageCountErrors().getFirst(), "The JSON value could not be converted to System.Int32. Path: $.pageCount | LineNumber: 0 | BytePositionInLine: 32.");
        softAssert.assertAll();
    }

    @Test(description = "User is able to create a book with empty body")
    public void postValidBookWithEmptyBody() {
        RestAssuredResponseImpl response = new BookAction().post(new BookPojo());
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
    }

    @DataProvider(name = "negative_page_count")
    public Object[][] negativePageCountProvider() {
        return new Object[][]{{-1}, {-2000000000}};
    }

    @Test(description = "User is not able to create a book with negative number of pages", dataProvider = "negative_page_count")
    public void postInvalidBook(int pageCount) {
        String publishDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        BookPojo body = BookPojo.builder()
                .title("test title")
                .publishDate(publishDate)
                .pageCount(pageCount)
                .build();
        RestAssuredResponseImpl response = new BookAction().post(body);
        SoftAssert softAssert = new SoftAssert();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST, "Status code does not match");
        ErrorPojo responseBody = response.as(ErrorPojo.class);
        softAssert.assertEquals(responseBody.getTitle(), "One or more validation errors occurred.");
        softAssert.assertAll();
    }

    @Test(description = "User is able to create and then search for a book")
    public void postAndGetBook() {
        int bookId = 700;
        BookAction bookAction = new BookAction();
        BookPojo body = BookPojo.builder()
                .id(bookId)
                .title("test title")
                .publishDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .pageCount(20)
                .build();
        RestAssuredResponseImpl response = bookAction.post(body);
        SoftAssert softAssert = new SoftAssert();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        BookPojo responseBody = response.as(BookPojo.class);
        softAssert.assertEquals(responseBody.getPageCount(), body.getPageCount());
        softAssert.assertEquals(responseBody.getTitle(), body.getTitle());
        softAssert.assertEquals(responseBody.getPublishDate(), body.getPublishDate());
        softAssert.assertAll("Different data in book creation");

        response = bookAction.getById(bookId);

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        responseBody = response.as(BookPojo.class);
        softAssert.assertEquals(responseBody.getPageCount(), body.getPageCount());
        softAssert.assertEquals(responseBody.getTitle(), body.getTitle());
        softAssert.assertEquals(responseBody.getPublishDate(), body.getPublishDate());
        softAssert.assertAll("Different data when getting a book");
    }

    @Test(description = "User is able to create and then delete a book")
    public void postAndDeleteBook() {
        int bookId = 600;
        BookAction bookAction = new BookAction();
        BookPojo body = BookPojo.builder()
                .id(bookId)
                .title("test title")
                .publishDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .pageCount(20)
                .build();
        RestAssuredResponseImpl response = bookAction.post(body);
        SoftAssert softAssert = new SoftAssert();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");
        BookPojo responseBody = response.as(BookPojo.class);
        softAssert.assertEquals(responseBody.getPageCount(), body.getPageCount());
        softAssert.assertEquals(responseBody.getTitle(), body.getTitle());
        softAssert.assertEquals(responseBody.getPublishDate(), body.getPublishDate());
        softAssert.assertAll("Different data in book creation");

        response = bookAction.delete(bookId);

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Status code does not match");

        // Verify book was deleted
        response = bookAction.getById(bookId);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_NOT_FOUND, "Status code does not match");
    }
}
