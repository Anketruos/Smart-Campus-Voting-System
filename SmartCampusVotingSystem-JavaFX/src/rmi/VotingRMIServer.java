package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Starts the RMI registry and binds the VotingRemote service (Unit 5: RMI).
 * Run this on the server machine before clients connect.
 */
public class VotingRMIServer {

    public static final int RMI_PORT = 1099;
    public static final String SERVICE_NAME = "VotingService";

    public static void main(String[] args) {
        try {
            VotingRemoteImpl impl = new VotingRemoteImpl();
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            registry.rebind(SERVICE_NAME, impl);
            System.out.println("VotingRMIServer is running on port " + RMI_PORT);
        } catch (Exception e) {
            System.err.println("RMI Server error: " + e.getMessage());
        }
    }
}
