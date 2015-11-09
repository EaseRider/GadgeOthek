/*
 * HSR - Uebungen 'Algorithmen & Datenstrukturen 2'
 * Version: Wed Nov  4 22:26:24 CET 2015
 */

package uebung08.as.aufgabe02;

import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;

public class TrieMultimap<V> implements Multimap<String, V> {

	private TrieNode<V> root;

	public TrieMultimap() {
		this.root = new TrieNode<V>();
	}

	/**
	 * Inserts a key/value pair into the multimap.
	 */
	public void insert(String key, V value) {
		insert(key, value, root);
	}

	protected void insert(String keySubstr, V value, TrieNode<V> node) {
		if (keySubstr.length() > 0) {
			TrieNode<V> nextNode = findKeyPath(keySubstr, node);
			insert(keySubstr.substring(1, keySubstr.length()), value, nextNode);
		} else {
			node.getValues().add(value);
		}
	}

	protected TrieNode<V> findKeyPath(String keySubstr, TrieNode<V> node) {
		String searchKey = keySubstr.substring(0, 1);
		for (TrieNode<V> child : node.getChilds()) {
			if (child.getKeySubstr().equals(searchKey)) {
				return child;
			}
		}
		TrieNode<V> newNode = new TrieNode<V>();
		newNode.setKeySubstr(searchKey);
		node.getChilds().add(newNode);
		return newNode;
	}

	/**
	 * Returns the first value for a given key. null if key is not found.
	 */
	public V find(String key) {
		TrieNode<V> node = findNode(key, root);
		if (node != null) {
			return node.getValues().get(0);
		} else {
			return null;
		}
	}

	/**
	 * Returns all values for a given key.
	 * 
	 * @return Iterator over all values. If key is not found: Iterator without
	 *         next.
	 */
	public Iterator<V> findAll(String key) {
		TrieNode<V> node = findNode(key, root);
		if (node != null) {
			return node.getValues().iterator();
		} else {
			return new ArrayList<V>().iterator();
		}
	}

	protected TrieNode<V> findNode(String key, TrieNode<V> node) {
		if (key.length() > 0) {
			String keySub = key.substring(node.getKeySubstr().length(), key.length());
			for (TrieNode<V> child : node.getChilds()) {
				for (int i = 1; i <= child.getKeySubstr().length(); ++i) {
					String childKeySub = child.getKeySubstr().substring(0, i);
					if (keySub.startsWith(childKeySub)) {
						return findNode(key.substring(node.getKeySubstr().length(), key.length()), child);
					}
				}
			}

			if (node.getKeySubstr().equals(key)) {
				return node;
			}
		}

		return null;
	}

	/**
	 * Removes all values for a given key.
	 */
	public void remove(String key) {
		TrieNode<V> node = findNode(key, root);
		if (node != null) {
			node.getValues().clear();
		}
	}

	/**
	 * Returns the number of values in the trie.
	 */
	public int size() {
		return size(root);
	}

	/**
	 * @return Number of values in this node and its child nodes.
	 */
	private int size(TrieNode<V> element) {
		int size = element.getValues().size();
		for (TrieNode<V> child : element.getChilds()) {
			size += size(child);
		}
		return size;
	}

	/**
	 * Print the tree.
	 */
	public void print() {
		print(0, root);
	}

	/**
	 * 
	 * @param depth
	 *            Depth in which the node is.
	 * @param node
	 *            Node to print.
	 */
	private void print(int depth, TrieNode<V> node) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append("\t");
		}
		sb.append(node.getKeySubstr());
		sb.append(":");
		for (V value : node.getValues()) {
			sb.append(" ");
			sb.append(value.toString());
		}
		System.out.println(sb.toString());
		++depth;
		for (TrieNode<V> child : node.getChilds()) {
			print(depth, child);
		}

	}

	public static void main(String[] args) {
		TrieMultimap<String> multimap = new TrieMultimap<>();

		multimap.insert("Büro", "bureau");
		multimap.insert("Büro", "office");
		multimap.insert("Büro", "agency");
		multimap.insert("Hallo", "hello");
		multimap.insert("Held", "hero");
		multimap.insert("halten", "keep");
		multimap.insert("Hall", "hall");
		multimap.insert("Halle", "hall");
		multimap.insert("hast", "have");
		multimap.insert("Ekstase", "ecstasy");
		multimap.insert("Ecke", "corner");
		multimap.insert("Ecken", "corners");

		if (multimap.size() != 12) {
			System.err.println("wrong size after insertion");
			System.exit(-1);
		}

		System.out.println("after insertion");
		multimap.print();

		System.out.println("\nfind test:");
		System.out.println(multimap.find("Büro"));
		System.out.println(multimap.find("Hallo"));
		System.out.println(multimap.find("Held"));
		System.out.println(multimap.find("halten"));
		System.out.println(multimap.find("Hall"));
		System.out.println(multimap.find("Halle"));
		System.out.println(multimap.find("hast"));
		System.out.println(multimap.find("Ekstase"));
		System.out.println(multimap.find("Ecke"));
		System.out.println(multimap.find("Ecken"));
		System.out.println(multimap.find("XYZ"));

		System.out.println("\nfindall test:");
		Iterator<String> it = multimap.findAll("Büro");
		while (it.hasNext()) {
			System.out.print(it.next() + " ");
		}
		System.out.println();
		System.out.println(multimap.findAll("XYZ").hasNext());

		if (multimap.size() != 12) {
			System.err.println("wrong after find routines");
			System.exit(-1);
		}

		multimap.remove("Hallo");
		multimap.remove("halten");
		multimap.remove("Ecke");
		multimap.remove("hast");
		multimap.remove("H");
		System.out.println();

		if (multimap.size() != 8) {
			System.err.println("wrong after remove");
			System.exit(-1);
		}
		System.out.println("after remove");
		multimap.print();
	}
}

/*
 * Session-Log:
 * 
 * after insertion : Büro: bureau, office, agency H: eld: hero all: hall o:
 * hello e: hall ha: lten: keep st: have E: kstase: ecstasy cke: corner n:
 * corners
 * 
 * find test: bureau hello hero keep hall hall have ecstasy corner corners null
 * 
 * findall test: bureau office agency false
 * 
 * after remove : Büro: bureau, office, agency H: eld: hero all: hall e: hall E:
 * kstase: ecstasy cken: corners
 */

