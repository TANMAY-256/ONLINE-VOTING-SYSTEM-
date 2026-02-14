import java.util.*;

/**
 * Represents a political party with a name and vote count.
 */
class Party {
    private String name;
    private int votes;

    public Party(String name) {
        this.name = name;
        this.votes = 0;
    }

    public String getName() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    public void incrementVote() {
        votes++;
    }

    @Override
    public String toString() {
        return name + " (Votes: " + votes + ")";
    }
}

/**
 * Represents a voter with a unique ID, name, and voting status.
 */
class Voter {
    private String id;          // e.g., Aadhar number
    private String name;
    private boolean hasVoted;

    public Voter(String id, String name) {
        this.id = id;
        this.name = name;
        this.hasVoted = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public void setVoted(boolean voted) {
        this.hasVoted = voted;
    }
}

/**
 * Main voting system that manages parties, voters, and voting process.
 */
public class OnlineVotingSystem {
    private Map<String, Party> parties;          // Party name -> Party object
    private Map<String, Voter> voters;           // Voter ID -> Voter object
    private boolean votingActive;                 // Voting status (open/closed)

    public OnlineVotingSystem() {
        parties = new HashMap<>();
        voters = new HashMap<>();
        votingActive = false;
        initializeParties();                       // Add default Indian parties
    }

    /** Pre-load some major Indian political parties */
    private void initializeParties() {
        addParty("BJP");
        addParty("Indian National Congress");
        addParty("Aam Aadmi Party");
        addParty("Bahujan Samaj Party");
        addParty("Communist Party of India (Marxist)");
        addParty("Nationalist Congress Party");
        addParty("Trinamool Congress");
        addParty("Others");
    }

    /** Add a new party to the election */
    public void addParty(String partyName) {
        if (!parties.containsKey(partyName)) {
            parties.put(partyName, new Party(partyName));
            System.out.println("Party '" + partyName + "' added successfully.");
        } else {
            System.out.println("Party already exists!");
        }
    }

    /** Register a new voter with a unique ID */
    public void registerVoter(String id, String name) {
        if (voters.containsKey(id)) {
            System.out.println("Voter with ID " + id + " already registered!");
        } else {
            voters.put(id, new Voter(id, name));
            System.out.println("Voter '" + name + "' registered successfully.");
        }
    }

    /** Start voting (only admin can do this) */
    public void startVoting() {
        if (!votingActive) {
            votingActive = true;
            System.out.println("Voting has started.");
        } else {
            System.out.println("Voting is already active.");
        }
    }

    /** Stop voting (only admin) */
    public void stopVoting() {
        if (votingActive) {
            votingActive = false;
            System.out.println("Voting has been stopped.");
        } else {
            System.out.println("Voting is already stopped.");
        }
    }

    /** Cast a vote for a specific party */
    public void castVote(String voterId, String partyName) {
        if (!votingActive) {
            System.out.println("Voting is not active right now.");
            return;
        }

        Voter voter = voters.get(voterId);
        if (voter == null) {
            System.out.println("Voter not registered!");
            return;
        }

        if (voter.hasVoted()) {
            System.out.println("You have already voted! Multiple votes are not allowed.");
            return;
        }

        Party party = parties.get(partyName);
        if (party == null) {
            System.out.println("Invalid party name! Please choose from the list.");
            return;
        }

        // Record the vote
        party.incrementVote();
        voter.setVoted(true);
        System.out.println("Vote cast successfully for " + partyName + ".");
    }

    /** Display current voting results (only after voting ends) */
    public void displayResults() {
        if (votingActive) {
            System.out.println("Voting is still in progress. Results will be available after voting ends.");
            return;
        }

        if (parties.isEmpty()) {
            System.out.println("No parties to display.");
            return;
        }

        System.out.println("\n=== ELECTION RESULTS ===");
        List<Party> sortedParties = new ArrayList<>(parties.values());
        sortedParties.sort((p1, p2) -> p2.getVotes() - p1.getVotes()); // Descending by votes

        for (Party p : sortedParties) {
            System.out.println(p);
        }

        // Calculate total votes
        int totalVotes = sortedParties.stream().mapToInt(Party::getVotes).sum();
        System.out.println("Total votes cast: " + totalVotes);
        System.out.println("=========================");
    }

    /** List all parties */
    public void listParties() {
        System.out.println("\n--- Political Parties ---");
        parties.keySet().forEach(System.out::println);
        System.out.println("--------------------------");
    }

    /** List all registered voters */
    public void listVoters() {
        System.out.println("\n--- Registered Voters ---");
        voters.values().forEach(v -> 
            System.out.println(v.getId() + " : " + v.getName() + " [Voted: " + v.hasVoted() + "]"));
        System.out.println("--------------------------");
    }

    /** Main menu-driven interface */
    public static void main(String[] args) {
        OnlineVotingSystem system = new OnlineVotingSystem();
        Scanner scanner = new Scanner(System.in);
        String choice;

        System.out.println("=== ONLINE VOTING SYSTEM (INDIAN ELECTIONS) ===");
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Register Voter");
            System.out.println("2. List Parties");
            System.out.println("3. List Voters");
            System.out.println("4. Start Voting (Admin)");
            System.out.println("5. Stop Voting (Admin)");
            System.out.println("6. Cast Vote");
            System.out.println("7. Display Results");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter Voter ID (e.g., Aadhar number): ");
                    String id = scanner.nextLine().trim();
                    System.out.print("Enter Voter Name: ");
                    String name = scanner.nextLine().trim();
                    system.registerVoter(id, name);
                    break;

                case "2":
                    system.listParties();
                    break;

                case "3":
                    system.listVoters();
                    break;

                case "4":
                    system.startVoting();
                    break;

                case "5":
                    system.stopVoting();
                    break;

                case "6":
                    System.out.print("Enter your Voter ID: ");
                    String voterId = scanner.nextLine().trim();
                    System.out.print("Enter Party Name you want to vote for: ");
                    String partyName = scanner.nextLine().trim();
                    system.castVote(voterId, partyName);
                    break;

                case "7":
                    system.displayResults();
                    break;

                case "8":
                    System.out.println("Exiting... Thank you for using the system.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}