package mock_tests;

import error.UserClientException;
import feign.FeignException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;
import user.User;
import user.UserLogic;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;
import static user.UserClient.USERS_RESOURCE;

public class UserMockServerTest {

    private ClientAndServer mockServer;

    @Before
    public void startMockServer() {
        this.mockServer = startClientAndServer(8888);
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }

    @Test
    public void testFindUser() throws Exception {
        this.mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/api" + USERS_RESOURCE + "/1"),
                        exactly(1))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("{\n \"userId\": \"1\", \"firstName\": \"shubham\",\n  \"lastName\": \"jaiswal\",\n  \"email\": \"shubham.jaiswal@gamil.com\"\n}")
                                .withDelay(new Delay(SECONDS, 1))
                );

        User user = new UserLogic().findUser(1L);

        Assert.assertEquals("shubham", user.getFirstName());
        Assert.assertEquals("jaiswal", user.getLastName());
        Assert.assertEquals("shubham.jaiswal@gamil.com", user.getEmail());
    }

    @Test
    public void testFindUserError() throws Exception {
        this.mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/api" + USERS_RESOURCE + "/2"),
                        exactly(1))
                .respond(
                        response()
                                .withStatusCode(404)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("{\n \"errorCode\": \"1001\", \"errorMessage\": \"User with the given identifier cannot be found\"}")
                                .withDelay(new Delay(SECONDS, 1))
                );


        try {
            new UserLogic().findUser(2L);
        } catch (FeignException e) {
            Assert.assertEquals(404, e.status());
            Assert.assertTrue(e.getMessage().contains("User with the given identifier cannot be found"));
        }
    }


    @Test
    public void testCreateUser() throws Exception {
        this.mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api" + USERS_RESOURCE)
                                .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                                .withBody(exact("{\n  \"firstName\": \"shubham\",\n  \"lastName\": \"jaiswal\",\n  \"email\": \"shubham.jaiswal@gmail.com\"\n}")),
                        exactly(1))
                .respond(
                        response()
                                .withStatusCode(201)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("{\"userId\": \"1\"}")
                                .withDelay(new Delay(SECONDS, 1))
                );


        Long userId = new UserLogic().createUser("shubham", "jaiswal", "shubham.jaiswal@gmail.com");
        Assert.assertEquals(new Long(1), userId);
    }

}
