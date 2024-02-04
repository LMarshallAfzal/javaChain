package com.leonard.blockchain;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a basic blockchain.
 * A blockchain is a decentralized, distributed ledger that records transactions across multiple computers.
 * This implementation uses a simple array list to store blocks.
 */
public class BlockChain {
    private static final Logger logger = Logger.getLogger(BlockChain.class.getName());
    private final ArrayList<Block> chain;

    /**
     * Constructs an empty blockchain.
     * Initializes the blockchain with an empty array list.
     */
    public BlockChain() {
        this.chain = new ArrayList<>();
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
            } catch (IndexOutOfBoundsException e) {
                logger.log(Level.SEVERE, "Error: Chain is empty. Unable to get the last block", e);
            }
        }
    }

    /**
     * Checks if the hash of a given block is valid.
     *
     * @param block The block to check.
     * @return True if the hash is valid, false otherwise.
     */
    private boolean isValidHash(Block block) {
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
