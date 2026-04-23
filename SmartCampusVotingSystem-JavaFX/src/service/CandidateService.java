package service;

import dao.CandidateDAO;
import model.Candidate;
import java.util.List;

public class CandidateService {
    private final CandidateDAO candidateDAO = new CandidateDAO();

    public void addCandidate(Candidate candidate) throws Exception {
        if (candidate.getName() == null || candidate.getName().isBlank())
            throw new IllegalArgumentException("Candidate name is required.");
        if (candidate.getElectionId() <= 0)
            throw new IllegalArgumentException("Valid election ID is required.");
        candidateDAO.insert(candidate);
    }

    public void removeCandidate(int candidateId) throws Exception {
        candidateDAO.delete(candidateId);
    }

    public List<Candidate> getCandidatesByElection(int electionId) throws Exception {
        return candidateDAO.findByElection(electionId);
    }
}
