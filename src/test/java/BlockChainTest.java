import com.leonard.blockchain.Block;
import com.leonard.blockchain.BlockChain;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockChainTest {
    @Test
    public void TestAddValidBlock() {
        BlockChain blockChain = new BlockChain();
        Block block = new Block(1, "2022-01-01T12:00:00", "Transaction 1", "0");
        blockChain.addBlock(block);
        assertTrue(blockChain.isChainValid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidBlock() {
        BlockChain blockChain = new BlockChain();
        Block block1 = new Block(1, "2022-01-01T12:00:00", "Transaction 1", "0");
        Block block2 = new Block(1, "2022-01-02T14:30:00", "Transaction 2", block1.getHash());
        blockChain.addBlock(block1);
        blockChain.addBlock(block2);
    }

    @Test
    public void testIsChainValidWithValidChain() {
        BlockChain blockChain = new BlockChain();
        Block block1 = new Block(1, "2022-01-01T12:00:00", "Transaction 1", "0");
        Block block2 = new Block(2, "2022-01-02T14:30:00", "Transaction 2", block1.getHash());
        blockChain.addBlock(block1);
        blockChain.addBlock(block2);
        assertTrue(blockChain.isChainValid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsChainValidWithInvalidChain() {
        BlockChain blockchain = new BlockChain();
        Block block1 = new Block(1, "2022-01-01T12:00:00", "Transaction 1", "0");
        Block block2 = new Block(2, "2022-01-02T14:30:00", "Transaction 2", "invalidPreviousHash");
        blockchain.addBlock(block1);
        blockchain.addBlock(block2);
        assertFalse(blockchain.isChainValid());
    }

    @Test
    public void testDisplayBlockchain() {
        BlockChain blockchain = new BlockChain();
        Block block1 = new Block(1, "2022-01-01T12:00:00", "Transaction 1", "0");
        Block block2 = new Block(2, "2022-01-02T14:30:00", "Transaction 2", block1.getHash());
        blockchain.addBlock(block1);
        blockchain.addBlock(block2);

        // Manually inspect the console output during testing
        blockchain.displayBlockchain();
    }

    @Test
    public void testCreateTableIfNotExists() {
        BlockChain blockChain = new BlockChain();
        blockChain.createTableIfNotExists();
        Block block = new Block(1, "2022-01-01T12:00:00", "Transaction 1", "0");
        blockChain.addBlock(block);
        blockChain.saveToDatabase(block);
        blockChain.loadFromDatabase();
        assertTrue(blockChain.isChainValid());
    }

    @Test
    public void testLoadJdbcDriver() {
        BlockChain blockChain = new BlockChain();
        blockChain.loadJdbcDriver();
    }

    @Test
    public void testSaveAndLoadFromDatabase() {
        BlockChain blockChain = new BlockChain();
        Block block1 = new Block(1, "2022-01-01T12:00:00", "Transaction 1", "0");
        Block block2 = new Block(2, "2022-01-02T14:30:00", "Transaction 2", block1.getHash());
        blockChain.addBlock(block1);
        blockChain.addBlock(block2);
        blockChain.saveToDatabase(block1);
        blockChain.saveToDatabase(block2);
        blockChain.loadFromDatabase();
        assertTrue(blockChain.isChainValid());
    }

}
