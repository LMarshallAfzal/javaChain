import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        // Creating a blockchain
        BlockChain blockchain = new BlockChain();

        // Adding blocks to the blockchain
        try {
            Block block1 = new Block(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), "Transaction 1", "0");
            blockchain.addBlock(block1);

            Block block2 = new Block(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), "Transaction 2", block1.getHash());
            blockchain.addBlock(block2);

            Block block3 = new Block(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), "Transaction 3", block2.getHash());
            blockchain.addBlock(block3);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Displaying the blockchain
        blockchain.displayBlockchain();

        // Checking if the blockchain is valid
        boolean isChainValid = blockchain.isChainValid();
        System.out.println("Is Blockchain Valid? " + isChainValid);
    }
}
