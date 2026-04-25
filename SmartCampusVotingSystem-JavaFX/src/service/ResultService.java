package service;

import dao.CandidateDAO;
import dao.VoteDAO;
import model.Candidate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates Lambdas & Streams (Unit 1: Functional Programming & OOP Fundamentals).
 * Uses Stream API with lambda expressions to process and sort voting results.
 */
public class ResultService {
    private final VoteDAO voteDAO = new VoteDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();

    /**
     * Returns candidate name -> vote count, sorted by count descending.
     * Uses Stream + Lambda (Unit 1: Higher-Order Functions / Lambdas & Streams).
     */
    public Map<String, Integer> getResults(int electionId) throws Exception {
        Map<Integer, Integer> rawResults = voteDAO.getResultsByElection(electionId);
        List<Candidate> candidates = candidateDAO.findByElection(electionId);

        Map<String, Integer> results = new LinkedHashMap<>();

        // Lambda expression to sort candidates by vote count descending (Unit 1)
        candidates.stream()
            .sorted((a, b) -> rawResults.getOrDefault(b.getId(), 0) - rawResults.getOrDefault(a.getId(), 0))
            .forEach(c -> results.put(c.getName(), rawResults.getOrDefault(c.getId(), 0)));

        return results;
    }

    /**
     * Returns the name of the candidate with the most votes.
     * Uses Stream + Lambda + Optional (Unit 1: Functional Programming).
     */
    public String getWinner(int electionId) throws Exception {
        Map<String, Integer> results = getResults(electionId);
        // Pure function: output depends only on input map (Unit 1: Pure Functions)
        return results.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("No votes cast");
    }

    /**
     * Returns total votes cast in an election using Stream reduce (Unit 1: Streams).
     */
    public int getTotalVotes(int electionId) throws Exception {
        return getResults(electionId).values().stream()
            .reduce(0, Integer::sum);
    }
}
