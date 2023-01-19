package aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserId implements Serializable {
    private static final long serialVersionUID = -6293079839223584771L;
    UUID uuid;
}
