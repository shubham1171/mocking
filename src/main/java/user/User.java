package user;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {


    private Long userId;

    private String firstName;

    private String lastName;

    private String email;
}

