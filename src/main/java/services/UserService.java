package services;

import config.ConfigConstants;
import dto.User;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final RequestSpecification requestSpec;

    public UserService() {
        this.requestSpec = given()
                .baseUri(ConfigConstants.BASE_URL + "/user")
                .contentType(ContentType.JSON);
    }

    // Создает нового пользователя через POST-запрос.
    public ValidatableResponse createUser(User user) {
        try {
            return given(requestSpec)
                    .body(user)
                    .when()
                    .post()
                    .then()
                    .log().ifValidationFails(); // Логирует ошибки только в случае провала
        } catch (RuntimeException e) {
            log.error("An error occurred while creating user:", e);
            Assertions.fail("Failed to create user due to an exception.");
        }
        return null;
    }

    // Возвращает пользователя по имени пользователя (userName).
    public ValidatableResponse getUserByUserName(String userName) {
        return given(requestSpec)
                .pathParam("username", userName) // Замена параметра
                .when()
                .get("/user/{username}") // Шаблон URL
                .then()
                .log().ifValidationFails();
    }
}