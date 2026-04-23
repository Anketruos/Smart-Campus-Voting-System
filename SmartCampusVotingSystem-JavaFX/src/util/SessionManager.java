package util;

import model.Admin;
import model.Voter;

public class SessionManager {
    private static Voter currentVoter;
    private static Admin currentAdmin;

    public static void setCurrentVoter(Voter voter) { currentVoter = voter; }
    public static Voter getCurrentVoter() { return currentVoter; }

    public static void setCurrentAdmin(Admin admin) { currentAdmin = admin; }
    public static Admin getCurrentAdmin() { return currentAdmin; }

    public static boolean isAdminLoggedIn() { return currentAdmin != null; }
    public static boolean isVoterLoggedIn() { return currentVoter != null; }

    public static void logout() {
        currentVoter = null;
        currentAdmin = null;
    }
}
