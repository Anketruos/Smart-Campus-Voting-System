package network;

import java.io.*;
import java.net.Socket;

/**
 * Client that communicates with VotingServer over a socket (Unit 4: Networking).
 * Used to send vote requests and retrieve results from the central server.
 */
public class VotingClient {

    private final String host;
    private final int port;

    public VotingClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Sends a vote for the given candidate name.
     * @return server response message
     */
    public String castVote(String candidateName) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("VOTE:" + candidateName);
            return in.readLine();
        }
    }

    /**
     * Requests current vote results from the server.
     * @return server response with results
     */
    public String getResults() throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("RESULTS");
            return in.readLine();
        }
    }
}
