package dk.dtu.ds;

public class Config {
    /**
     * The port on which the server will be listening.
     */
    public static final int SERVER_PORT = 5099;

    public static final String ENDPOINT_PREFIX = "rmi://localhost:" + SERVER_PORT + "/";

    public static final String AUTH_BIND = "auth";

    public static final String PRINT_BIND = "print";

    public static final String AUTH_ENDPOINT = ENDPOINT_PREFIX + AUTH_BIND;

    public static final String PRINT_ENDPOINT = ENDPOINT_PREFIX + PRINT_BIND;

    public static final int TOKEN_EXPIRATION_TIME = 30 * 60 * 1000; // 30 minutes

    public static final String DB_AUTH_PATH = "db/auth.csv";    // users and passwords

    public static final String DB_USERS_ROLES_PATH = "db/user-role.csv";  // users and actions

    public static final String DB_ROLES_PERMISSIONS_PATH = "db/role-ac-list.csv";  // users and actions

    public static final String DB_REPLACE_COMMA = "_____";
}