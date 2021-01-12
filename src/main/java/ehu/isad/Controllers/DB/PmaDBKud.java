package ehu.isad.Controllers.DB;

public class PmaDBKud {

    private static final PmaDBKud instance = new PmaDBKud();

    public static PmaDBKud getInstance() {
        return instance;
    }
}
