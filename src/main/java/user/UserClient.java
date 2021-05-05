package user;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface UserClient {

// here we mock local server instead of actual host server

//        String USER_SERCIVE_URL = "https://reqres.in/api";

    String USER_SERCIVE_URL = "http://localhost:8888/api";

    String USERS_RESOURCE = "/users";

    @RequestLine("POST " + USERS_RESOURCE)
    @Headers("Content-Type: application/json; charset=utf-8")
    User createUser(final User user);

    @RequestLine("GET " + USERS_RESOURCE + "/{userId}")
    User getUserById(final @Param("userId") Long userId);

}
