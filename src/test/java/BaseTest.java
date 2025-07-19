import io.restassured.RestAssured;
import utility.SystemPropertyUtil;

public class BaseTest {
    public BaseTest() {
        SystemPropertyUtil.loadAllPropsFromFiles();
        RestAssured.baseURI = SystemPropertyUtil.getBaseApiUrl();
    }
}
