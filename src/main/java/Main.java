import com.leonard.blockchain.Block;
import com.leonard.blockchain.BlockChain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        // Creating a blockchain
        BlockChain blockchain = new BlockChain();
        blockchain.createTableIfNotExists();

        // Adding blocks to the blockchain
        try {
            blockchain.loadJdbcDriver();
            Block block1 = new Block(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), "Transaction 1", "0");
            blockchain.addBlock(block1);
            blockchain.saveToDatabase(block1);

            Block block2 = new Block(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), "Transaction 2", block1.getHash());
            blockchain.addBlock(block2);
            blockchain.saveToDatabase(block2);

            Block block3 = new Block(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), "Transaction 3", block2.getHash());
            blockchain.addBlock(block3);
            blockchain.saveToDatabase(block3);

            // Load blockchain from the database
            blockchain.loadFromDatabase();

            // Display the loaded blockchain
            blockchain.displayBlockchain();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Checking if the blockchain is valid
        boolean isChainValid = blockchain.isChainValid();
        System.out.println("Is Blockchain Valid? " + isChainValid);

    }
}
