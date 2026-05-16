package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class VotingClient {

    private final String host;
    private final int port;

    public VotingClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String castVote(int voterId, int electionId, int candidateId) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("VOTE:" + voterId + ":" + electionId + ":" + candidateId);
            return in.readLine();
        }
    }

    public String getResults(int electionId) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("RESULTS:" + electionId);
            return in.readLine();
        }
    }
}
