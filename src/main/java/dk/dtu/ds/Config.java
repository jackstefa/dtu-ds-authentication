package dk.dtu.ds;

public class Config {
    /**
     * The port on which the server will be listening.
     */
    public static final int SERVER_PORT = 5099;

    public static final String ENDPOINT_PREFIX = "rmi://localhost:" + SERVER_PORT + "/";

    public static final String AUTH_BIND = "auth";

    public static final String PRINT_BIND = "print";

    public static final String AUTHENTICATION_ENDPOINT = ENDPOINT_PREFIX + AUTH_BIND;

    public static final String PRINT_ENDPOINT = ENDPOINT_PREFIX + PRINT_BIND;

    public static final int TOKEN_EXPIRATION_TIME = 30 * 60 * 1000; // 30 minutes

    public static final String DB_PATH = "db.csv";
}