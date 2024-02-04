# JavaChain #

JavaChain is a Java-based blockchain implementation that provides
a simple yet powerful demonstration of blockchain technology. This 
project is designed to showcase the fundamental concepts behind 
blockchains, including blocks, hashing, and decentralization.

## Features ##

- **com.leonard.blockchain.Block Structure:** Each block in JavaChain consists of an index, data,
previous hash, and a unique hash calculated using a cryptographic hash function.
- **Blockchain management:** The blockchain class manages the creation of blocks,
ensuring the integrity of the chain through hash validation and linking.
- **Security:** JavaChain emphasises security by implementing standard cryptographic
hashing techniques to secure block data and maintain the integrity of the blockchain.

## Getting Started ##

1. Clone the repository
    ```bash
      git clone https://github.com/LMarshallAfzal/javaChain.git
    ```
2. Compile and run the Main class to see a basic demonstration of JavaChain.
3. Explore and modify the code to understand and extend the functionality of 
the blockchain.

## Usage ##

```java
    import com.leonard.blockchain.Block;// Create a new blockchain
Blockchain myBlockchain = new Blockchain();

// Add blocks to the blockchain
Block block1 = new Block(1, "01/01/2024", "Data1", "0");
    myBlockchain.

addBlock(block1);

Block block2 = new Block(2, "02/01/2024", "Data2", block1.getHash());
    myBlockchain.

addBlock(block2);

// Display the blockchain
    myBlockchain.

displayBlockchain();

// Validate the blockchain
boolean isValid = myBlockchain.isChainValid();
    System.out.

println("Is the blockchain valid? "+isValid);
```

## Contributing ##

Contributions are welcome! Feel free to open issues, submit pull requests, or
suggest improvements.

## License ##

This project is licensed under the [MIT License](LICENSE) - see the [LICENSE](LICENSE) file for details.