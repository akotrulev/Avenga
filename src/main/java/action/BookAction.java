package action;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.specification.RequestSpecification;

public class BookAction extends BaseAction {
    private final String GET_PUT_DELETE_BY_ID_PATH = "/Books/{id}";
    private final String GET_POST_PATH = "/Books";

    public RestAssuredResponseImpl delete(String id) {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_PUT_DELETE_BY_ID_PATH)
                .addPathParam("id", id)
                .build();
        return super.delete(requestSpecification);
    }

    public RestAssuredResponseImpl getById(String id) {
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

    public RestAssuredResponseImpl post() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_POST_PATH)
                .build();
        return super.post(requestSpecification);
    }

    public RestAssuredResponseImpl put(String id) {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBasePath(GET_PUT_DELETE_BY_ID_PATH)
                .addPathParam("id", id)
                .build();
        return super.put(requestSpecification);
    }
}
