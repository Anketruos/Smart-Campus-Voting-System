package service;

import dao.CandidateDAO;
import dao.VoteDAO;
import model.Candidate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates streams and lambdas for real-time result processing.
 */
public class ResultService {
    private final VoteDAO voteDAO = new VoteDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();

    public Map<String, Integer> getResults(int electionId) throws Exception {
        Map<Integer, Integer> rawResults = voteDAO.getResultsByElection(electionId);
        List<Candidate> candidates = candidateDAO.findByElection(electionId);

        Map<String, Integer> results = new LinkedHashMap<>();
        candidates.stream()
            .sorted((first, second) -> Integer.compare(
                rawResults.getOrDefault(second.getId(), 0),
                rawResults.getOrDefault(first.getId(), 0)
            ))
            .forEach(candidate -> results.put(candidate.getName(), rawResults.getOrDefault(candidate.getId(), 0)));

        return results;
    }

    public String getWinner(int electionId) throws Exception {
        Map<String, Integer> results = getResults(electionId);
        return results.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("No votes cast");
    }

    public int getTotalVotes(int electionId) throws Exception {
        return getResults(electionId).values().stream()
            .reduce(0, Integer::sum);
    }
}
