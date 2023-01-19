package aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Token implements Serializable {

    private static final long serialVersionUID = -2743453326392792795L;
    UUID id;

    public Token(){
        this.id = UUID.randomUUID();
    }
}
