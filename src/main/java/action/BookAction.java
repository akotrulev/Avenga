package action;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.specification.RequestSpecification;

public class BookAction extends BaseAction {

    public RestAssuredResponseImpl delete(String id) {
        RequestSpecification requestSpecification = new RequestSpecBuilder().build();
        return super.delete(requestSpecification);
    }

    public RestAssuredResponseImpl getById(String id) {
        RequestSpecification requestSpecification = new RequestSpecBuilder().build();
        return super.get(requestSpecification);
    }

    public RestAssuredResponseImpl get() {
        RequestSpecification requestSpecification = new RequestSpecBuilder().build();
        return super.get(requestSpecification);
    }

    public RestAssuredResponseImpl post() {
        RequestSpecification requestSpecification = new RequestSpecBuilder().build();
        return super.post(requestSpecification);
    }

    public RestAssuredResponseImpl put(String id) {
        RequestSpecification requestSpecification = new RequestSpecBuilder().build();
        return super.put(requestSpecification);
    }
}
