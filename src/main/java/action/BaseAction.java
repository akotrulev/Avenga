package action;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.specification.RequestSpecification;
import utility.SystemPropertyUtil;

import static io.restassured.RestAssured.given;

public class BaseAction {
    protected final String basePath;

    public BaseAction() {
        SystemPropertyUtil.loadAllPropsFromFiles();
        RestAssured.baseURI = SystemPropertyUtil.getBaseApiUrl();
        basePath = "/api/" + SystemPropertyUtil.getVersion();
    }

    public RestAssuredResponseImpl sendRequest(RequestSpecification requestSpecification, Method method) {
        requestSpecification.relaxedHTTPSValidation();
        RestAssuredResponseImpl restAssuredResponse = (RestAssuredResponseImpl) given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .when()
                .request(method)
                .then()
                .extract().response();
        return restAssuredResponse;
    }

    public RestAssuredResponseImpl post(RequestSpecification requestSpecification) {
        return sendRequest(requestSpecification, Method.POST);
    }

    public RestAssuredResponseImpl put(RequestSpecification requestSpecification) {
        return sendRequest(requestSpecification, Method.PUT);
    }

    public RestAssuredResponseImpl patch(RequestSpecification requestSpecification) {
        return sendRequest(requestSpecification, Method.PATCH);
    }

    public RestAssuredResponseImpl get(RequestSpecification requestSpecification) {
        return sendRequest(requestSpecification, Method.GET);
    }

    public RestAssuredResponseImpl delete(RequestSpecification requestSpecification) {
        return sendRequest(requestSpecification, Method.DELETE);
    }

}
