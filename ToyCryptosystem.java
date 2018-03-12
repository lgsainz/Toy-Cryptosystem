import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * This program implements a toy symmetric cryptosystem.
 * Includes encryption, decryption, and bruteforce attack methods.
 * @author Leticia Garcia-Sainz
 * @date 2-11-18
 */
public class ToyCryptosystem {

    private static String msg;
    private static String msgE;
    private static String msgD;
    private static short key;
    private static String result = "";
    
    public static void main(String[] args){
        key = generateKey();
        msg = generateMsg();
        msgE = encryption(key,msg);
        decryption(key, msgE);
        bruteForce(msgE);
    }

    /*
     * Generate a random numerical 16-bit key.
     */
    private static short generateKey() {
    	Random r = new Random();
    	short s = (short) r.nextInt(Short.MAX_VALUE + 1);
        return s;
    }

    /*
     * Generate a random message (string of words) by using a dictionary.
     */
    private static String generateMsg() {
    	// Create scanner to ask for user input
    	Scanner in = new Scanner(System.in);
    	System.out.println("Please enter the complete path to the dictionary file..." + 
    	"\n(For example: /Users/leti/Downloads/dictionary.txt)");
    	String fileName = in.nextLine();
    	File wordList = new File(fileName);
    	
    	// Create list of words and read dictionary into it
        List<String> words = new ArrayList<>();
        Scanner reader = null;

        try {
            reader = new Scanner(wordList);
        } catch (FileNotFoundException e) {
            System.out.println("file \"" + fileName + "\" not found");
            System.exit(0);
        }
        
        // Add only words with an even number of letters into the list
        while(reader.hasNextLine()) {
            Scanner s2 = new Scanner(reader.nextLine());
            while (s2.hasNext()) {
                String word = s2.next();
                if (word.length() % 2 == 0) {
                	 words.add(word);
                }
            }
        }
        
        // Generate random string of words from dictionary
        int count = 0;
        while (count < 10) {
        	Random rand = new Random();
            result += words.get(rand.nextInt(words.size())) + " ";
            count++;
        }
        
        // Print generated message and return result
        System.out.println("Generated message: " + result);
        
        return result;
    }

    /*
     * Encrypt the generated message using the random key.
     */
    private static String encryption(short key, String msg) {
    	
    	// Convert string msg to bytes
        byte[] msgBytes = result.getBytes();
        int messageLength = msgBytes.length;
        
        // Convert key to string and then bytes
        String keyStr = String.valueOf(key);
        byte[] keyBytes = keyStr.getBytes();
        
        byte[] output = new byte[messageLength];
        // XOR the message with the key repeating as much as needed
        for (int i = 0; i < messageLength; i++) {
        	output[i] = (byte) (msgBytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        
        // Get the resulting message
        String msgE = new String(output);
        System.out.println("Encrypted message: " + msgE);

        return msgE;
    }

    /*
     * Decrypt a message back to its original state.
     */
    private static void decryption(short key, String msgE) {
    	
    	// Convert string msg to bytes
        byte[] msgBytes = msgE.getBytes();
        
        // Convert key to string and then bytes
        String keyStr = String.valueOf(key);
        byte[] keyBytes = keyStr.getBytes();
        
        byte[] output = new byte[msgBytes.length];
        // XOR the message with the key, repeating as much as needed
        for (int i = 0; i < msgBytes.length; i++) {
        	output[i] = (byte) (msgBytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        
        // Get the resulting message
        msgD = new String(output);
    	
    }

    /*
     * Implement a bruteforce algorithm using the decryption method.
     */
    private static void bruteForce(String msgE) {
    	 	
    	// Read dictionary into array list
    	String fileName = "/Users/leti/Downloads/dictionary.txt";
    	File wordList = new File(fileName);
        List<String> dictionary = new ArrayList<>();
        Scanner reader = null;

        try {
            reader = new Scanner(wordList);
        } catch (FileNotFoundException e) {
            System.out.println("file \"" + fileName + "\" not found");
            System.exit(0);
        }
        
        // Add only words with an even number of letters into the list
        while(reader.hasNextLine()) {
            Scanner in = new Scanner(reader.nextLine());
            while (in.hasNext()) {
                String word = in.next();
                if (word.length() % 2 == 0) {
                	 dictionary.add(word);
                }
            }
        }
    	

    	// Set some number of attempts for the purpose of this assignment
    	int attempts = 1;
    	boolean isValid = false;
    	while (isValid == false && attempts < 5) {
    		
    		// Generate new key and decrypt message
    		generateKey();
    		decryption(key, msgE);
    		String[] wordss = msgD.split(" ");
    		
    		// Check word in decrypted message against dictionary
    		for (int i = 0; i < wordss.length; i++) {
        		inner:
        		for (int j = 0; j < dictionary.size()-1; j++) {
        			if (wordss[i].equals(dictionary.get(j))) {
        				// if we find a match then print it and go to next word
        				isValid = true;
        				break inner;
        			}
        		}   
        	}
    		
    		// If we matched the message to the words in our dictionary print success
    		if (isValid == true) {
    			System.out.println("Bruteforce Successful. " + "Decrypted: " + msgD);
    		}
    		else {
    			// If we didn't find any matches then we will attempt again with new key
        		System.out.println("Attempt " + attempts + " failed." );
        		attempts++;
        		key = generateKey();
    		}
    	}
    }
	
}
