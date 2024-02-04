import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents a block in a blockchain
 * Each block contains an index, timestamp, data, previous hash, and its own hash
 */
public class Block {
    private final int index;
    private final LocalDateTime timestamp;
    private final String data;
    private final String previousHash;
    private final String hash;

    /**
     * Constructs a Block with the specified parameters.
     *
     * @param index        The index of the block in the blockchain. Must not be negative.
     * @param timestamp    The timestamp indicating when the block was created.
     *                    Should be in the format: yyyy-MM-ddTHH:mm:ss.
     * @param data         The data stored in the block.
     * @param previousHash The hash of the previous block in the blockchain.
     * @throws IllegalArgumentException If the index is negative or the timestamp has an invalid format.
     */
    public Block(int index, String timestamp, String data, String previousHash) {
        // Validation checks
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative.");
        }

        try {
            this.timestamp = LocalDateTime.parse(timestamp);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid timestamp format. Expected format: yyyy-MM-ddTHH:mm:ss");
        }

        this.index = index;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    /**
     * Gets the index of the block.
     *
     * @return The index of the block.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the timestamp of the block.
     *
     * @return The timestamp of the block.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the data stored in the block.
     *
     * @return The data stored in the block.
     */
    public String getData() {
        return data;
    }

    /**
     * Gets the hash of the previous block in the blockchain.
     *
     * @return The hash of the previous block.
     */
    public String getPreviousHash() {
        return previousHash;
    }

    /**
     * Gets the hash of the current block.
     *
     * @return The hash of the current block.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Calculates and returns the hash of the block using SHA-256 algorithm.
     *
     * @return The calculated hash of the block.
     * @throws RuntimeException If the SHA-256 algorithm is not supported.
     */
    public String calculateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String combinedData = Integer.toString(index) + timestamp + data + previousHash;
            byte[] combinedBytes = combinedData.getBytes();

            byte[] hashBytes = digest.digest(combinedBytes);

            StringBuilder hashStringBuilder = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hashStringBuilder.append("0");
                }
                hashStringBuilder.append(hex);
            }

            return hashStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not supported.", e);
        }
    }
}
