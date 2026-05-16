package network;

import service.ResultService;
import service.VotingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket-based voting server for Unit 4 and Unit 6 demonstrations.
 */
public class VotingServer {

    public static final int PORT = 9090;

    public static void main(String[] args) throws IOException {
        System.out.println("VotingServer started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;
        private final VotingService votingService = new VotingService();
        private final ResultService resultService = new ResultService();

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String message = in.readLine();
                if (message != null && message.startsWith("VOTE:")) {
                    out.println(handleVote(message));
                } else if (message != null && message.startsWith("RESULTS:")) {
                    out.println(handleResults(message));
                } else {
                    out.println("ERROR:Unknown command");
                }
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
            }
        }

        private String handleVote(String message) {
            String[] parts = message.split(":");
            if (parts.length != 4) {
                return "ERROR:Expected VOTE:voterId:electionId:candidateId";
            }

            try {
                int voterId = Integer.parseInt(parts[1]);
                int electionId = Integer.parseInt(parts[2]);
                int candidateId = Integer.parseInt(parts[3]);
                votingService.castVote(voterId, electionId, candidateId);
                return "OK:Vote recorded successfully.";
            } catch (Exception e) {
                return "ERROR:" + e.getMessage();
            }
        }

        private String handleResults(String message) {
            String[] parts = message.split(":");
            if (parts.length != 2) {
                return "ERROR:Expected RESULTS:electionId";
            }

            try {
                int electionId = Integer.parseInt(parts[1]);
                StringBuilder builder = new StringBuilder("RESULTS:");
                resultService.getResults(electionId)
                    .forEach((candidate, total) -> builder.append(candidate).append('=').append(total).append(';'));
                return builder.toString();
            } catch (Exception e) {
                return "ERROR:" + e.getMessage();
            }
        }
    }
}
