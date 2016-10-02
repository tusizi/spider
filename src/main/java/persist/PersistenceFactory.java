package persist;

public class PersistenceFactory {
    private static Persistence persistence;

    public static Persistence getInstance() {
        if (persistence == null) {
//            persistence = new FilePersistence();
            persistence = new DBPersistence();
        }
        return persistence;
    }
}
