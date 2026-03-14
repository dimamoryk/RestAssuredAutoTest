package main;

import config.ConfigConstants;
import dto.User;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserService;

import static config.ConfigConstants.BASE_URL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    public void testSuccessfulUserCreation() {
        User user = User.builder()
                .id(1001L)
                .username("test_user_1001")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .phone("+1234567890")
                .userStatus(1)
                .build();

        // Отправляем POST-запрос на создание пользователя
        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(200)              // Проверяем успешный статус
                .body("code", equalTo(200))   // Проверяем поле code
                .body("type", equalTo("unknown"))
                .body("message", equalTo("1001"))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(ConfigConstants.SCHEMA_PATH));
    }

    @Test
    void testDuplicateUserCreation() {
        User duplicateUser = User.builder()
                .id(1001L)
                .username("test_user_1001")
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .password("another_password")
                .phone("+0987654321")
                .userStatus(1)
                .build();

        UserService service = new UserService();
        ValidatableResponse response = service.getUserByUserName("test_user_1001");
        response.statusCode(200); // Проверка статуса ответа

        userService.createUser(duplicateUser)
                .statusCode(400); // Ожидается ошибка Bad Request
    }
}
