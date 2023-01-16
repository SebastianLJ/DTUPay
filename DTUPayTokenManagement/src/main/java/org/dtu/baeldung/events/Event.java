package org.dtu.baeldung.events;

import java.util.Date;
import java.util.UUID;

public abstract class Event {
    public final UUID id = UUID.randomUUID();
    public final Date created = new Date();
}
