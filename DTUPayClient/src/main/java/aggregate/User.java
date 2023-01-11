
package aggregate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class User {

    Name name;

    UserId userId;

    List<Payment> payments = new ArrayList<Payment>();


    public User(String firstName, String lastName) {
        this.userId = new UserId(UUID.randomUUID());
        this.name = new Name(firstName, lastName);
    }

}
