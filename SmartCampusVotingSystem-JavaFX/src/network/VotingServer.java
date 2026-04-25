package network;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Central Voting Server (Unit 4: Networking & Client-Server Interaction).
 * Listens for vote requests from client applications.
 *
 * Uses multithreading to handle multiple concurrent voters (Unit 6: Multithreading).
 * Uses synchronized blocks to safely update vote counts (Unit 6: Synchronization).
 */
public class VotingServer {

    public static final int PORT = 9090;

    // Shared vote count map — accessed by multiple threads, so access is synchronized
    private static final Map<String, Integer> voteCounts = new HashMap<>();
    private static final Object lock = new Object();

    public static void main(String[] args) throws IOException {
        System.out.println("VotingServer started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Each client handled in its own thread (Unit 6: Multithreading)
                new Thread(new ClientHandler(clientSocket)).start();
            }
        }
    }

    /**
     * Handles a single client connection.
     * Protocol: client sends "VOTE:<candidateName>", server replies "OK" or "ERROR:<msg>"
     */
    static class ClientHandler implements Runnable {
        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String message = in.readLine();
                if (message != null && message.startsWith("VOTE:")) {
                    String candidate = message.substring(5).trim();
                    recordVote(candidate);
                    out.println("OK:Vote recorded for " + candidate);
                } else if ("RESULTS".equals(message)) {
                    out.println(getResults());
                } else {
                    out.println("ERROR:Unknown command");
                }
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
            }
        }

        /** Thread-safe vote recording using synchronized block (Unit 6: Synchronization). */
        private void recordVote(String candidate) {
            synchronized (lock) {
                voteCounts.merge(candidate, 1, Integer::sum);
            }
        }

        private String getResults() {
            synchronized (lock) {
                StringBuilder sb = new StringBuilder("RESULTS:");
                voteCounts.forEach((k, v) -> sb.append(k).append("=").append(v).append(";"));
                return sb.toString();
            }
        }
    }
}
