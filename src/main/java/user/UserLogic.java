package user;

import feign.Feign;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import static user.UserClient.USER_SERCIVE_URL;

public class UserLogic {

    private final UserClient userClient;

    public UserLogic() {
        this.userClient = Feign.builder()
                .errorDecoder(new ErrorDecoder.Default())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(UserClient.class, USER_SERCIVE_URL);

    }

    public User findUser(final Long userId) {
        return this.userClient.getUserById(userId);
    }
    public Long createUser(final String firstName, final String lastName, final String email) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return this.userClient.createUser(user).getUserId();
    }

}
