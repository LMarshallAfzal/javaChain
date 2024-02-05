package com.leonard.blockchain;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

/**
 * Represents a basic blockchain.
 * A blockchain is a decentralized, distributed ledger that records transactions across multiple computers.
 * This implementation uses a simple array list to store blocks.
 */
public class BlockChain {
    private static final Logger logger = Logger.getLogger(BlockChain.class.getName());
    private static final String DATABASE_URL = "jdbc:sqlite:blockchain:db";
    private final ArrayList<Block> chain;

    /**
     * Constructs com.blockchain.blockchain with an empty blockchain.
     * Initializes the blockchain with an empty array list.
     */
    public BlockChain() {
        this.chain = new ArrayList<>();
    }

    /**
     * Creates the 'blocks' table in the database if it does not exist.
     * The table schema includes columns for id, block_index, timestamp, data, previous_hash, and hash.
     * If the table already exists, no action is taken.
     */
    public void createTableIfNotExists() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS blocks ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "block_index INT NOT NULL," +
                    "timestamp TEXT NOT NULL," +
                    "data TEXT NOT NULL," +
                    "previous_hash TEXT NOT NULL," +
                    "hash TEXT NOT NULL)");

            logger.log(Level.INFO, "Table 'blocks' created or already exists.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating Database Table 'blocks'", e);
        }
    }

    /**
     * Loads the SQLite JDBC driver. If the driver is not found, a ClassNotFoundException is caught
     * and logged with a SEVERE level message.
     */
    public void loadJdbcDriver() {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error loading JDBC driver", e);
        }
    }

    /**
     * Adds a new block to the blockchain.
     *
     * @param newBlock The block to be added to the blockchain.
     * @throws IllegalArgumentException If the new block is invalid or cannot be added to the chain.
     */
    public void addBlock(Block newBlock) {
        if (chain.isEmpty()) {
            chain.add(newBlock);
        } else {
            try {
                Block lastBlock = chain.get(chain.size() - 1);
                if (
                        lastBlock.getIndex() + 1 != newBlock.getIndex() ||
                                !lastBlock.getHash().equals(newBlock.getPreviousHash()) ||
                                !isValidHash(newBlock)
                ) {
                    String errorMessage = "Invalid block. Unable to add to the chain. Details:\n +" +
                            "Index check: " + (lastBlock.getIndex() + 1 != newBlock.getIndex()) + "\n" +
                            "Hash check: " + !lastBlock.getHash().equals(newBlock.getPreviousHash()) + "\n" +
                            "Valid hash check: " + !isValidHash(newBlock);
                    throw new IllegalArgumentException(errorMessage);
                }

                chain.add(newBlock);
                logger.log(Level.INFO, "A new block has been added to the blockchain");

            } catch (IndexOutOfBoundsException e) {
                logger.log(Level.SEVERE, "Error: Chain is empty. Unable to get the last block", e);
            }
        }
    }

    /**
     * Saves the provided block to the 'blocks' table in the database.
     *
     * @param block The block to be saved to the database.
     */
    public void saveToDatabase(Block block) {

        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            if (blockExistsInDatabase(connection, block.getIndex())) {
                logger.log(Level.WARNING, "Block with index " + block.getIndex() + " already exists in the database. Skipped insertion");
                return;
            }


             try (PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO blocks (block_index, timestamp, data, previous_hash, hash) VALUES (?, ?, ?, ?, ?)")) {
                 statement.setInt(1, block.getIndex());
                 statement.setString(2, block.getTimestamp().toString());
                 statement.setString(3, block.getData());
                 statement.setString(4, block.getPreviousHash());
                 statement.setString(5, block.getHash());
                 statement.executeUpdate();

                 logger.log(Level.INFO, "New block has been saved to the database.");
             }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting values into the database", e);
        }
    }

    /**
     * Checks whether a block with a given index already exists in the database.
     *
     * @param connection The database connection.
     * @param blockIndex blockIndex The index of the block to check.
     * @return {@code true} if a block with the same index exists in the database, {@code false} otherwise.
     * @throws SQLException SQLException if a database error occurs.
     */
    private boolean blockExistsInDatabase(Connection connection, int blockIndex) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM blocks WHERE block_index = ?")) {
            statement.setInt(1, blockIndex);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * Loads blocks from the 'blocks' table in the database and populates the chain.
     * The chain is cleared before loading to avoid duplication.
     */
    public void loadFromDatabase() {
        chain.clear();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM blocks")) {

            while (resultSet.next()) {
                int index = resultSet.getInt("block_index");
                String timestamp = resultSet.getString("timestamp");
                String data = resultSet.getString("data");
                String previousHash = resultSet.getString("previous_hash");
                String hash = resultSet.getString("hash");

                chain.add(new Block(index, timestamp, data, previousHash, hash));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading values from the database", e);
        }
    }

    /**
     * Checks if the hash of a given block is valid.
     *
     * @param block The block to check.
     * @return True if the hash is valid, false otherwise.
     */
    public boolean isValidHash(Block block) {
        try {
            String calculatedHash = block.calculateHash();
            return calculatedHash.equals(block.getHash());
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error calculating hash for block.", e);
            return false;
        }
    }

    /**
     * Checks if the blockchain is valid.
     * Iterates through the blocks to ensure that each block's previous hash matches the hash of the preceding block.
     *
     * @return True if the blockchain is valid, false otherwise.
     */
    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if(!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Displays the blockchain by printing the details of each block in a formatted manner.
     */
    public void displayBlockchain() {
        for (Block block : chain) {
            String formattedBlock = String.format(
                    "Index: %d%nTimestamp: %s%nData: %s%nPrevious Hash: %s%nHash: %s%n-------------------------------------",
                    block.getIndex(), block.getTimestamp(), block.getData(), block.getPreviousHash(), block.getHash()
            );
            System.out.println(formattedBlock);
        }
    }
}
