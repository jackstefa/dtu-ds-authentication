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
        // find the role associated with username
        String username = authenticationService.getUsernameFromToken(token);
        System.out.println("User " + username + " is trying to do " + action + " command!");
        String[] roles = getRolesFromDb(username);

        // check if the role has the permission to do the action
        for (String role : roles) {
            String[] permissions = getRolePermissionsFromDb(role);
            if (permissions.length == 0) {
                continue;
            }
            if (Arrays.asList(permissions).contains("all") || Arrays.asList(permissions).contains(action)) {
                return true;
            }
        }
        return false;
    }

    private String[] getRolesFromDb(String username) {
        String rolesToParse = readFromCsv(Config.DB_USERS_ROLES_PATH, username);
        if (rolesToParse == null) {
            return new String[]{};
        }
        System.out.println("User " + username + " has role" + (rolesToParse.split(";").length > 1 ? "s" : "") + ": " + rolesToParse.replaceAll(";", " & "));
        return rolesToParse.split(";");
    }

    private String[] getRolePermissionsFromDb(String role) {
        String actionsToParse = readFromCsv(Config.DB_ROLES_PERMISSIONS_PATH, role);
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