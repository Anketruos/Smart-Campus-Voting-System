package service;

import dao.CandidateDAO;
import dao.ElectionDAO;
import model.Candidate;
import model.Election;
import java.util.List;

public class ElectionService {
    private final ElectionDAO electionDAO = new ElectionDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();

    public void createElection(Election election) throws Exception {
        if (election.getTitle() == null || election.getTitle().isBlank())
            throw new IllegalArgumentException("Election title is required.");
        if (election.getStartDate() == null || election.getEndDate() == null)
            throw new IllegalArgumentException("Start and end dates are required.");
        if (election.getEndDate().isBefore(election.getStartDate()))
            throw new IllegalArgumentException("End date must be after start date.");
        electionDAO.insert(election);
    }

    public void updateElection(Election election) throws Exception {
        electionDAO.update(election);
    }

    public void deleteElection(int id) throws Exception {
        electionDAO.delete(id);
    }

    public List<Election> getAllElections() throws Exception {
        return electionDAO.findAll();
    }

    public List<Election> getActiveElections() throws Exception {
        return electionDAO.findActive();
    }

    public void addCandidate(Candidate candidate) throws Exception {
        if (candidate.getName() == null || candidate.getName().isBlank())
            throw new IllegalArgumentException("Candidate name is required.");
        candidateDAO.insert(candidate);
    }

    public void removeCandidate(int candidateId) throws Exception {
        candidateDAO.delete(candidateId);
    }

    public List<Candidate> getCandidatesForElection(int electionId) throws Exception {
        return candidateDAO.findByElection(electionId);
    }
}
