package ru.netology.dataGenerator;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;


public class DataGenerator {

    static Faker faker = new Faker(new java.util.Locale("ru"));

    public static String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String generateCity() {
        String city = faker.address().city();
        return city;
    }

    public static String generateName() {
        return faker.name().fullName();
    }

    public static String generatePhone() {
        return faker.phoneNumber().phoneNumber();
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static void sendRequest(RegistrationDto user) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String getRandomLogin() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            var user = new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) {
            var registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    public static class RegistrationDto {
        private String login;
        private String password;
        private String status;

        public RegistrationDto(String login, String password, String status) {
            this.login = login;
            this.password = password;
            this.status = status;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getStatus() {
            return status;
        }
    }
}
