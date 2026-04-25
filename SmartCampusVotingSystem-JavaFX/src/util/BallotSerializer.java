package util;

import model.Candidate;
import java.io.*;
import java.util.List;

/**
 * Demonstrates Object Serialization (Unit 2: Java API Packages & Object Serialization).
 * Saves and loads a list of Candidate objects to/from a binary file.
 */
public class BallotSerializer {

    /**
     * Serialize a list of candidates to a file.
     */
    public static void saveCandidates(List<Candidate> candidates, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(candidates);
        }
    }

    /**
     * Deserialize a list of candidates from a file.
     */
    @SuppressWarnings("unchecked")
    public static List<Candidate> loadCandidates(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Candidate>) ois.readObject();
        }
    }
}
