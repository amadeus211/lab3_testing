package org.example;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

public class ApiTest
{

    final String API_KEY = "62127f813387b9c55376d67c36202d6d";

    @Test
    public void getSettlementAreas() {
        String requestBody = "{\n" +
                "   \"apiKey\": \"" + API_KEY + "\",\n" +
                "   \"modelName\": \"AddressGeneral\",\n" +
                "   \"calledMethod\": \"getSettlementAreas\",\n" +
                "   \"methodProperties\": {\n" +
                "        \"Ref\" : \"\"\n" +
                "   }\n" +
                "}";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("https://api.novaposhta.ua/v2.0/json/")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
        }

    @Test
    public void createContrAgent() {
        String requestBody = "{\n" +
                "   \"apiKey\": \"" + API_KEY + "\",\n" +
                "   \"modelName\": \"CounterpartyGeneral\",\n" +
                "   \"calledMethod\": \"save\",\n" +
                "   \"methodProperties\": {\n" +
                "        \"FirstName\" : \"Петро\",\n" +
                "        \"MiddleName\" : \"Олександрович\",\n" +
                "        \"LastName\" : \"Зеленський\",\n" +
                "        \"Phone\" : \"380123539911\",\n" +
                "        \"Email\" : \"maliuk04@ukr.net\",\n" +
                "        \"CounterpartyType\" : \"PrivatePerson\",\n" +
                "        \"CounterpartyProperty\" : \"Recipient\"\n" +
                "   }\n" +
                "}";

        given()
                .contentType("application/json")
                .body(requestBody)
//                .log().all()
                .when()
                .post("https://api.novaposhta.ua/v2.0/json/")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data[0].Ref", notNullValue())
                .body("data[0].FirstName", equalTo("Петро"))
                .body("data[0].MiddleName", equalTo("Олександрович"))
                .body("data[0].LastName", equalTo("Зеленський"));
    }

    @Test
    public void checkCookieYIICSRFTOKEN() {
        String requestBody = "{\n" +
                "   \"apiKey\": \"" + API_KEY + "\",\n" +
                "   \"modelName\": \"CounterpartyGeneral\",\n" +
                "   \"calledMethod\": \"getCounterpartyOptions\",\n" +
                "   \"methodProperties\": {\n" +
                "        \"Ref\" : \"61971cdd-6f92-11ee-a60f-48df37b921db\"\n" +
                "   }\n" +
                "}";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("https://api.novaposhta.ua/v2.0/json/")
                .then()
                .statusCode(200)
                .cookie("YIICSRFTOKEN", notNullValue());
    }

    @Test
    public void createContrAgentWithInvalidEmail() {
        String requestBody = "{\n" +
                "   \"apiKey\": \"" + API_KEY + "\",\n" +
                "   \"modelName\": \"CounterpartyGeneral\",\n" +
                "   \"calledMethod\": \"save\",\n" +
                "   \"methodProperties\": {\n" +
                "        \"FirstName\" : \"Петро\",\n" +
                "        \"MiddleName\" : \"Олександрович\",\n" +
                "        \"LastName\" : \"Зеленський\",\n" +
                "        \"Phone\" : \"380123539911\",\n" +
                "        \"Email\" : \"maliuk04ukr.net\",\n" +
                "        \"CounterpartyType\" : \"PrivatePerson\",\n" +
                "        \"CounterpartyProperty\" : \"Recipient\"\n" +
                "   }\n" +
                "}";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("https://api.novaposhta.ua/v2.0/json/")
                .then()
                .statusCode(200)
                .body("success", equalTo(false))
                .body("errors[0]", equalTo("Email invalid format"));
    }
    @Test
    public void getContAgentContactsWithInvalidRef() {
        String requestBody = "{\n" +
                "   \"apiKey\": \"" + API_KEY + "\",\n" +
                "   \"modelName\": \"CounterpartyGeneral\",\n" +
                "   \"calledMethod\": \"getCounterpartyContactPersons\",\n" +
                "   \"methodProperties\": {\n" +
                "        \"Ref\" : \"34ggfgfgdfgdfgfg\",\n" +
                "        \"Page\" : \"1\"\n" +
                "   }\n" +
                "}";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("https://api.novaposhta.ua/v2.0/json/")
                .then()
                .statusCode(200)
                .body("success", equalTo(false))
                .body("errors[0]", equalTo("Ref is not specified"));
    }
}
