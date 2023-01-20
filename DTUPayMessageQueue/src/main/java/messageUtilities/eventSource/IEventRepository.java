package messageUtilities.eventSource;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public interface IEventRepository<T1, T2> {
    T1 getByID(T2 obj);
    void save(T1 obj);
}
