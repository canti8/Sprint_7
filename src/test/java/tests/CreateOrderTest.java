package tests;


import clients.CourierClient;
import clients.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.OrderCreate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    String orderTrack;

    @After
    public void tearDown(){
        OrderClient.deleteOrder(orderTrack);
    }

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;

    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                { "Antuan", "Alex", "Tver, 34.", "5", "+7 800 800 80 80", 5, "2023-07-07", "comment", new String [] { "GRAY" } },
                { "Antuan", "Alex", "Tver, 34.", "5", "+7 800 800 80 80", 5, "2023-07-07", "comment", new String [] { "BLACK" } },
                { "Antuan", "Alex", "Tver, 34.", "5", "+7 800 800 80 80", 5, "2023-07-07", "comment", new String [] { "GRAY", "BLACK" } },
                { "Antuan", "Alex", "Tver, 34.", "5", "+7 800 800 80 80", 5, "2023-07-07", "comment", new String [] { } },
                { "Antuan", "Alex", "Tver, 34.", "5", "+7 800 800 80 80", 5, "2023-07-07", "comment", new String [] { "BLACK" } },
        };
    }


    @Test
    @DisplayName("Создание заказов, параметризация цветов")
    public void createOrderParameterizedColorScooterTest() {
        OrderCreate orderCreate  = new OrderCreate(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response createResponse = OrderClient.createNewOrder(orderCreate);
        OrderClient.comparingSuccessfulOrderSet(createResponse, 201);
        orderTrack = OrderClient.getOrderTrack(createResponse);

    }
}
