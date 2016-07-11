import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Implementation of Tries Data Structure
 * @author Sachin
 *
 */
public class Tries {

	/*
	 * Trie Node maps any type of character and point to next node that can have
	 * the next character.
	 */
	public class TrieNode {
		Map<Character, TrieNode> children;
		boolean endOfWord;

		// Constructor
		public TrieNode() {
			children = new TreeMap<>();
			endOfWord = false;
		}
	}

	TrieNode root = new TrieNode();

	/**
	 * To add a new word to the trie.
	 * 
	 * @param word
	 *            : String - to be added
	 */
	public void add(String word) {
		char ch = word.charAt(0);
		TrieNode temp = root;
		for (int i = 0; i < word.length(); i++) {
			ch = word.charAt(i);
			TrieNode newNode = temp.children.get(ch);
			// if the character is not present in the map of the current node
			// then map(in current node) this character to a new node.
			if (newNode == null) {
				newNode = new TrieNode();
				temp.children.put(ch, newNode);
			}
			temp = newNode;
		}
		// set the endofWord flag to true - indicating the word is presentF
		temp.endOfWord = true;
	}

	/**
	 * To remove the word from the trie
	 * 
	 * @param word
	 *            - String - to be removed
	 */
	public void remove(String word) {
		delete(root, word, 0);
	}

	/**
	 * An internal method that helps to delete the successive nodes if their map
	 * is empty i.e. it doesn't contain any references to the other nodes.
	 * 
	 * @param node:
	 *            TrieNode
	 * @param word:
	 *            String - word to be deleted
	 * @param index:
	 *            int - character index of the word
	 * @return: boolean - True if deleted else False
	 */
	private boolean delete(TrieNode node, String word, int index) {

		if (word.length() == index) {
			// if flag is False then the word doesn't exist
			if (!node.endOfWord)
				return false;
			// make it false - indicating this word is not present.
			node.endOfWord = false;
			// check if there are any more character references in the map
			return node.children.size() == 0;
		}

		char ch = word.charAt(index);
		TrieNode temp = node.children.get(ch);
		if (temp == null)
			return false;
		// recursive call to the next node and next character
		boolean deleteCurrentNode = delete(temp, word, index + 1);

		// if the node doesn't contain any other character references then
		// delete that node
		if (deleteCurrentNode) {
			node.children.remove(ch);
			return node.children.size() == 0;
		}
		return false;
	}

	public void importFromFile(String file) throws FileNotFoundException {
		File inputFile = new File(file);
		Scanner in = new Scanner(inputFile);
		String line, lineWithoutPunctuation;
		String words[];
		while (in.hasNextLine()) {
			line = in.nextLine();
			lineWithoutPunctuation = line.replaceAll("[^a-zA-Z ]", "").trim().toLowerCase();
			// System.out.println(lineWithoutPunctuation);
			words = lineWithoutPunctuation.split(" ");

			for (int i = 0; i < words.length; i++) {
				// System.out.println(words[i]);
				add(words[i]);
			}
		}

	}

	/**
	 * To check if the word is present in the trie.
	 * 
	 * @param word:
	 *            String - word
	 * @return: boolean - True if present else False
	 */
	public boolean contains(String word) {
		TrieNode t = find(word);
		return t != null ? t.endOfWord : false;
	}

	/**
	 * To find if the given word exists in the Trie
	 * 
	 * @param word:
	 *            String
	 * @return: TrieNode - node where the last character of the word ends
	 */
	public TrieNode find(String word) {
		char ch;
		TrieNode temp = root;
		// for (int i = 0; i < word.length(); i++) {
		// ch = word.charAt(i);
		// TrieNode nextNode = temp.children.get(ch);
		// if (nextNode == null)
		// return null;
		// temp = nextNode;
		// }
		int i = 0;
		int len = word.length() - 1;
		// loop till the word ends or if a null is encountered
		while (temp != null && i <= len) {
			ch = word.charAt(i++);
			TrieNode nextNode = temp.children.get(ch);
			temp = nextNode;
		}
		return temp;
	}

	/**
	 * To find the number of words with the given prefix
	 * 
	 * @param word:
	 *            String - prefix
	 * @return: int - number of words with the given prefix
	 */
	public int prefix(String word) {
		TrieNode n = find(word);
		// if the prefix itself doesn't exist
		if (n == null)
			return 0;
		return countWords(n);
	}

	/**
	 * An internal method to count the number of words from the given node.
	 * 
	 * @param node:
	 *            TrieNode - Starting node
	 * @return: int - number of words from the given node
	 */
	private int countWords(TrieNode node) {
		if (node.children.size() == 0) {
			if (node.endOfWord)
				return 1;
			else
				return 0;
		}
		int count = 0;
		// to go through all the entries in the map for the given node
		for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
			if (node.endOfWord)
				count += 1;
			// recursive call to count all the words
			count += countWords(entry.getValue());
		}
		return count;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Tries ts = new Tries();
		ts.add("abcd");
		ts.add("abc");
		ts.add("abgl");
		ts.add("cdf");
		ts.add("abtl");
		ts.add("cdp");
		ts.add("lop");
		ts.add("lmn");
		if (ts.contains("abc"))
			System.out.println("Exist");
		else
			System.out.println("Doesn't exist");
		System.out.println("Number of words with prefic \"ab\" = " + ts.prefix("ab"));

		/*
		 * ts.importFromFile("inputTries.txt"); String search = "prefixes"; if
		 * (ts.contains(search)) { System.out.println(search + " is present"); }
		 * else System.out.println(search + " doesn't exist");
		 */
	}

}
