package dk.dtu.ds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Objects;

public class AuthorizationServant implements AuthorizationService {
    private final AuthenticationService authenticationService;

    public AuthorizationServant(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public boolean hasPermission(String token, String action) throws RemoteException {
        String username = authenticationService.getUsernameFromToken(token);
        System.out.println("User " + username + " is trying to do " + action + " command! " + "at " + new java.util.Date());
        String[] permissions = getPermissionListFromDb(username);
        if (permissions.length == 0) {
            return false;
        }
        if (permissions[0].equals("all")) {
            return true;
        }
        return Arrays.asList(permissions).contains(action);
    }

    private String[] getPermissionListFromDb(String username) {
        String actionsToParse = readFromCsv(Config.DB_PERMISSIONS_PATH, username);
        if (actionsToParse == null) {
            return new String[]{};
        }
        return actionsToParse.split(";");
    }

    private String readFromCsv(String path, String firstColumn) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] columns = line.split(",");
                // columns[0] is username
                // columns[1] is a list of actions permitted to the user
                if (Objects.equals(columns[0], firstColumn)) {
                    return columns[1];
                }
            }
            return null;
        } catch (IOException e) {
            System.out.println(path + " not found");
            return null;
        }
    }
}