package action;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.specification.RequestSpecification;
import pojo.author.AuthorPojo;

public class AuthorAction extends BaseAction {
    private final String GET_PUT_DELETE_BY_ID_PATH = basePath + "/Authors/{id}";
    private final String GET_POST_PATH = basePath + "/Authors";
    private final String GET_BY_BOOK_PATH = basePath + "/Authors/authors/books/{idBook}";

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

    public RestAssuredResponseImpl getByBookId(int id) {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_BY_BOOK_PATH)
                .addPathParam("idBook", id)
                .build();
        return super.get(requestSpecification);
    }
    public RestAssuredResponseImpl get() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_POST_PATH)
                .build();
        return super.get(requestSpecification);
    }

    public RestAssuredResponseImpl post(AuthorPojo body) {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_POST_PATH)
                .setBody(body)
                .build();
        return super.post(requestSpecification);
    }

    public RestAssuredResponseImpl put(int id, AuthorPojo body) {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_PUT_DELETE_BY_ID_PATH)
                .addPathParam("id", id)
                .setBody(body)
                .build();
        return super.put(requestSpecification);
    }
}
