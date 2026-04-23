package service;

import dao.CandidateDAO;
import dao.VoteDAO;
import model.Candidate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResultService {
    private final VoteDAO voteDAO = new VoteDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();

    // Returns candidate name -> vote count, sorted by count descending
    public Map<String, Integer> getResults(int electionId) throws Exception {
        Map<Integer, Integer> rawResults = voteDAO.getResultsByElection(electionId);
        List<Candidate> candidates = candidateDAO.findByElection(electionId);

        Map<String, Integer> results = new LinkedHashMap<>();
        candidates.stream()
            .sorted((a, b) -> rawResults.getOrDefault(b.getId(), 0) - rawResults.getOrDefault(a.getId(), 0))
            .forEach(c -> results.put(c.getName(), rawResults.getOrDefault(c.getId(), 0)));

        return results;
    }

    public String getWinner(int electionId) throws Exception {
        Map<String, Integer> results = getResults(electionId);
        return results.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("No votes cast");
    }
}
