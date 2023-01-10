package aggregate;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Name {
    private String firstName;
    private String lastName;
}
