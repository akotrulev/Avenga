package action;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.specification.RequestSpecification;
import pojo.book.BookPojo;

public class BookAction extends BaseAction {
    private final String GET_PUT_DELETE_BY_ID_PATH = basePath + "/Books/{id}";
    private final String GET_POST_PATH = basePath + "/Books";

    public RestAssuredResponseImpl delete(int id) {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_PUT_DELETE_BY_ID_PATH)
                .addPathParam("id", id)
                .build();
        return super.delete(requestSpecification);
    }

    public RestAssuredResponseImpl getById(int id) {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_PUT_DELETE_BY_ID_PATH)
                .addPathParam("id", id)
                .build();
        return super.get(requestSpecification);
    }

    public RestAssuredResponseImpl get() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_POST_PATH)
                .build();
        return super.get(requestSpecification);
    }

    public RestAssuredResponseImpl postGenericBody(Object body) {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_POST_PATH)
                .setBody(body)
                .build();
        return super.post(requestSpecification);
    }

    public RestAssuredResponseImpl post(BookPojo body) {
        return postGenericBody(body);
    }

    public RestAssuredResponseImpl put(int id, BookPojo body) {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_PUT_DELETE_BY_ID_PATH)
                .addPathParam("id", id)
                .setBody(body)
                .build();
        return super.put(requestSpecification);
    }
}
