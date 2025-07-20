import action.BookAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.internal.RestAssuredResponseImpl;
import org.apache.http.HttpStatus;
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
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        softAssert.assertTrue(Arrays.stream(response.as(BookPojo[].class)).findAny().isPresent());
        softAssert.assertAll();
    }

    @Test(description = "User is able to get a book by id")
    public void getBookById() {
        RestAssuredResponseImpl response = new BookAction().getById(1);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        softAssert.assertEquals(response.as(BookPojo.class).getId(), 1);
        softAssert.assertAll();
    }

    @Test(description = "User can not get a book by id that does not exist")
    public void getBookByIdDoesNotExist() {
        RestAssuredResponseImpl response = new BookAction().getById(1000000);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.statusCode(), HttpStatus.SC_NOT_FOUND);
        softAssert.assertAll();
    }

    @DataProvider(name = "page_count")
    public Object[][] pageCountProvider() {
        return new Object[][]{{1}, {5}, {0}, {-1}, {-2000000000}, {2000000000}};
    }
    @Test(description = "User is able to create a book", dataProvider = "page_count")
    public void postValidBook(int pageCount) {
        String publishDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        BookPojo body = BookPojo.builder()
                .title("test title")
                .publishDate(publishDate)
                .pageCount(pageCount)
                .build();
        RestAssuredResponseImpl response = new BookAction().post(body);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        BookPojo responseBody = response.as(BookPojo.class);
        softAssert.assertEquals(responseBody.getPageCount(), body.getPageCount());
        softAssert.assertEquals(responseBody.getTitle(), body.getTitle());
        softAssert.assertEquals(responseBody.getPublishDate(), publishDate);
        softAssert.assertAll();
    }


    @Test(description = "User is not able to create a book when page count is a double")
    public void postDoublePageCountBook() {
        ObjectNode objectNode = new ObjectMapper().valueToTree(new BookPojo());
        objectNode.put("pageCount", 1.3435);

        RestAssuredResponseImpl response = new BookAction().postGenericBody(objectNode);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);
        ErrorPojo responseBody = response.as(ErrorPojo.class);
        softAssert.assertEquals(responseBody.getErrors().getPageCountErrors().getFirst(), "The JSON value could not be converted to System.Int32. Path: $.pageCount | LineNumber: 0 | BytePositionInLine: 26.");
        softAssert.assertAll();
    }

    @Test(description = "User is not able to create a book when page count is a string")
    public void postStringPageCountBook() {
        ObjectNode objectNode = new ObjectMapper().valueToTree(new BookPojo());
        objectNode.put("pageCount", "bad string");

        RestAssuredResponseImpl response = new BookAction().postGenericBody(objectNode);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);
        ErrorPojo responseBody = response.as(ErrorPojo.class);
        //   "title": "One or more validation errors occurred.",
        //    "status": 400,
        softAssert.assertEquals(responseBody.getErrors().getPageCountErrors().getFirst(), "The JSON value could not be converted to System.Int32. Path: $.pageCount | LineNumber: 0 | BytePositionInLine: 32.");
        softAssert.assertAll();
    }
}
