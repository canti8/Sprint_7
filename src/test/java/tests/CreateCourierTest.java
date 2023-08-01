package tests;

import clients.CourierClient;
import clients.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.pojo.CourierLogin;
import org.example.pojo.CourierRegister;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateCourierTest {

    String courierId;

    CourierRegister courier = new CourierRegister("Paul767613>", "qwerty111999", "PaulAlex");

    @After
    public void tearDown(){
        CourierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Создание нового курьера, валидные данные")
    public void creatingANewCourierValidData() {
        Response createResponse = CourierClient.createNewCourier(this.courier);
        CourierClient.comparingTheActualResponseCodeWithTheSuccessfulOne(createResponse, "ok", 201);

        CourierLogin courierLogin = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        courierId = CourierClient.getCourierId(logInResponse);
    }


    @Test
    @DisplayName("Создание курьера, логин пустой")
    public void creationOfACourierEmptyLogin() {
        CourierRegister courier = new CourierRegister("", RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(5));
        Response response = CourierClient.createNewCourier(courier);
        CourierClient.comparingOfTheErroneousResponseCodeWithTheActualOne(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера, пароль пустой")
    public void creationOfACourierPasswordIsEmpty() {
        CourierRegister courier = new CourierRegister(RandomStringUtils.randomAlphanumeric(5), "", RandomStringUtils.randomAlphanumeric(5));

        Response response = CourierClient.createNewCourier(courier);
        CourierClient.comparingOfTheErroneousResponseCodeWithTheActualOne(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера, неуникальный логин")
    public void creatingACourierNonUniqueLogin() {
        CourierRegister courier = new CourierRegister("ninja", RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(5));
        Response response = CourierClient.createNewCourier(courier);
        CourierClient.comparingOfTheErroneousResponseCodeWithTheActualOne(response, 409, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("Создание курьера, имя пустое")
    public void creationOfACourierNameIsEmpty() {
        String login = RandomStringUtils.randomAlphanumeric(5);
        String password = RandomStringUtils.randomAlphanumeric(5);

        CourierRegister courier = new CourierRegister(login, password, null);

        Response response = CourierClient.createNewCourier(courier);
        CourierClient.comparingTheActualResponseCodeWithTheSuccessfulOne(response, "ok", 201);

        CourierLogin courierLogin = new CourierLogin(login, password);
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        courierId = CourierClient.getCourierId(logInResponse);

    }

    @Test
    @DisplayName("Создание курьера, поля не заполнены")
    public void creationOfACourierTheFieldsAreNotFilled() {
        CourierRegister courier = new CourierRegister("", "", "");
        Response response = CourierClient.createNewCourier(courier);
        CourierClient.comparingOfTheErroneousResponseCodeWithTheActualOne(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьеров-дубликатов")
    public void creatingTwoDuplicateCouriers() {
        Response response = CourierClient.createNewCourier(this.courier);
        CourierClient.comparingTheActualResponseCodeWithTheSuccessfulOne(response, "ok", 201);

        Response secondResponse = CourierClient.createNewCourier(this.courier);
        CourierClient.comparingOfTheErroneousResponseCodeWithTheActualOne(secondResponse, 409, "Этот логин уже используется. Попробуйте другой.");

        CourierLogin courierLogin = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        courierId = CourierClient.getCourierId(logInResponse);

    }


}
