package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {

    private RestTemplate template = new RestTemplate();

    @BeforeEach()
    public void clearDatabase() {
        template.postForLocation("http://localhost:8080/api/debug/clear", null);
    }

    @Test
    public void testPostAdmin() {
        RegisterAdminDtoRequest dtoRequest = new RegisterAdminDtoRequest("рома",
                "ромагов", "patronymic", "regularAdmin", "adminLogin",
                "adminPassword");

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "userType=" + "ADMIN");
        HttpEntity requestEntity = new HttpEntity(dtoRequest, requestHeaders);

        ReturnUserDtoResponse dtoResponse = template.postForObject(
                "http://localhost:8080/api/admins", requestEntity, ReturnAdminDtoResponse.class);
        assert dtoResponse != null;
        assertEquals(dtoRequest.getFirstName(), dtoResponse.getFirstName());
    }

    @Test
    public void testPostAdminFail() {
        RegisterAdminDtoRequest dtoRequest = new RegisterAdminDtoRequest("рома",
                "ромагов", "patronymic", "regularAdmin", "SuperAdmin",
                "adminPassword");

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "userType=" + "ADMIN");
        HttpEntity requestEntity = new HttpEntity(dtoRequest, requestHeaders);

        try {
            template.postForObject(
                    "http://localhost:8080/api/admins", requestEntity, ReturnAdminDtoResponse.class);
            Assertions.fail();
        } catch (HttpServerErrorException exc) {
            Assertions.assertEquals(500, exc.getStatusCode().value());
            System.out.println(exc.getResponseBodyAsString());
//            assertTrue(exc.getResponseBodyAsString().contains("\"allErrors\":"));
        }
    }
}
