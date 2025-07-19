import action.BookAction;
import io.restassured.internal.RestAssuredResponseImpl;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pojo.book.BookPojo;

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

    @Test(description = "User is able to create a book")
    public void postBook() {
        BookPojo body = BookPojo.builder().title("test title")
                .pageCount(20).build();
        RestAssuredResponseImpl response = new BookAction().post(body);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        response.prettyPrint();
        softAssert.assertAll();
    }
}
