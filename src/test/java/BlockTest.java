import com.leonard.blockchain.Block;
import java.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    @Test
    public void testCalculateHash() {
        Block block = new Block(1, "2022-01-01T12:00:00", "Transaction 1", "0");
        String expectedHash = "2d68f871bec345b099a25834bdba21abc04931f08017dad1376b68036dfdd094";
        assertEquals(expectedHash, block.calculateHash());
    }

    @Test
    public void testBlockConstructorWIthValidInput() {
        Block block = new Block(1, "2022-01-01T12:00:00", "Transaction 1", "0");
        assertEquals(1, block.getIndex());
        assertEquals(LocalDateTime.parse("2022-01-01T12:00:00"), block.getTimestamp());
        assertEquals("Transaction 1", block.getData());
        assertEquals("0", block.getPreviousHash());

        assertNotNull(block.getHash());
        assertFalse(block.getHash().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithNegativeIndex() {
        new Block(-1, "2022-01-01T12:00:00", "Transaction 1", "0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidTimestampFormat() {
        new Block(1, "2022/01/01 12:00:00", "Transaction 1", "0");
    }

    @Test
    public void testGetters() {
        Block block = new Block(1, "2022-01-01T12:00:00", "Transaction", "0");
        assertEquals(1, block.getIndex());
        assertEquals(LocalDateTime.parse("2022-01-01T12:00:00"), block.getTimestamp());
        assertEquals("Transaction", block.getData());
        assertEquals("0", block.getPreviousHash());
        assertNotNull(block.getHash());  // Ensure hash is not null
    }

    @Test
    public void testHashCalculationConsistency() {
        Block block = new Block(1, "2022-01-01T12:00:00", "Transaction", "0");
        String hash1 = block.calculateHash();
        String hash2 = block.calculateHash();
        assertEquals(hash1, hash2);
    }

    @Test(timeout = 5000)
    public void testHashCalculationPerformance() {
        for (int i = 0; i < 1000000; i++) {
            Block block = new Block(i, "2022-01-01T12:00:00", "Transaction", "0");
            block.calculateHash();
        }
    }

}
