package blessed_data_hw;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashedDictionary<K extends Comparable<? super K>, V> implements DictionaryInterface<K, V> {
	
	private int numberOfEntries;
	private static final int DEFAULT_CAPACITY = 5;
	private static final int MAX_CAPACITY = 10000;
	
	private TableEntry<K, V>[] hashTable;
	private int tableSize;
	private static final int MAX_SIZE = 2 * MAX_CAPACITY;
	public boolean initialized = false;
	private static double MAX_LOAD_FACTOR = 0.5;
	
	public int chosenHF = 0;
	public int chosenCH = 0;
	
	public int collision_count = 0;
	
	public void chosenLoadFactor(double load_factor) {
		MAX_LOAD_FACTOR = load_factor;
	}
	
	public void chosenHashFunction(String hash_function) {
		if (hash_function.equalsIgnoreCase("ssf"))
			chosenHF = 1;
	}
	
	public void chosenCollisionHandling(String collision_handling) {
		if (collision_handling.equalsIgnoreCase("lp"))
			chosenCH = 1;
	}
	
	public HashedDictionary() {
		this(DEFAULT_CAPACITY);
	}
	
	public HashedDictionary(int initialCapacity) {
		checkCapacity(initialCapacity);
		numberOfEntries = 0;
		int tableSize = getNextPrime(initialCapacity);
		checkSize(tableSize);
		
		@SuppressWarnings("unchecked")
		TableEntry<K, V>[] temp = (TableEntry<K, V>[])new TableEntry[tableSize];
		hashTable = temp;
		initialized = true;
	}
	
	private void checkCapacity(int initialCapacity) {
		// TODO Auto-generated method stub
		
	}

	private void checkSize(int tableSize2) {
		if (tableSize2 == MAX_SIZE) {
			//TODO
		}
	}

	private int getNextPrime(int initialCapacity) {
		if (initialCapacity <= 1)
            return 2;
     
        int prime = initialCapacity;
        boolean found = false;
     
        // Loop continuously until isPrime returns
        // true for a number greater than n
        while (!found)
        {
            prime++;
     
            if (isPrime(prime))
                found = true;
        }
     
        return prime;
	}

	private boolean isPrime(int n) {
		if (n <= 1) return false;
        if (n <= 3) return true;
         
        // This is checked so that we can skip
        // middle five numbers in below loop
        if (n % 2 == 0 || n % 3 == 0) return false;
         
        for (int i = 5; i * i <= n; i = i + 6)
            if (n % i == 0 || n % (i + 2) == 0)
            return false;
         
        return true;
	}

	public V getValue(K key) {
		checkInitialization();
		V result = null;
		
		int index = getHashIndex(key);
		index = locate(index, key);
		
		if (index != -1) {
			//result = hashTable[index].getValue();
			System.out.println(">Search: " + key + "\n" + hashTable[index].sll.size() + " documents found");
			hashTable[index].sll.display();
		}
		else
			System.out.println("not found!");
		
		return result;
	}
	
	public V remove(K key) {
		checkInitialization();
		V removedValue = null;
		
		int index = getHashIndex(key);
		index = locate(index, key);
		
		if (index != -1) {
			removedValue = hashTable[index].getValue();
			hashTable[index].setToRemoved();
			numberOfEntries--;
		}
		
		return removedValue;
	}
	
	private void checkInitialization() {
		// TODO Auto-generated method stub
		
	}

	private int getHashIndex(K key) {
		int index = 0;
		char c = ' ';
		int prime = 31;
		for (int i = 0; i < key.toString().length(); i++) {
			if (chosenHF == 1) {						//SSF
				c = (char)(key.toString().charAt(i));
				index += c - 96;
			}
			else {										//PAF
				c = (char)(key.toString().charAt(i));
				index += (c - 96) * Math.pow(prime, key.toString().length() - 1 - i);
			}
			
		}
		
		return index;
	}

	private int locate(int index, K key) {
		boolean found = false;
		int up = 0;
		int hk = 0;
		int dk = 0;
		
		index = index % hashTable.length;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn() && key.equals(hashTable[index].getKey()))
				found = true;
			else {
				if (chosenCH == 1) {
					index = (index + 1) % hashTable.length; //Linear probing
				}
				else {
					hk = index % hashTable.length;
					dk = 31 - (index % 31);
					index = (hk + (dk * up)) % hashTable.length; //double hashing
					up++;
				}
			}
		}
		
		int result = -1;
		if (found)
			result = index;
		
		return result;
	}
	
	public V put(K key, V value) {
		checkInitialization();
		if ((key == null) || (value == null))
			throw new IllegalArgumentException();
		else {
			V oldValue;
			
			int index = getHashIndex(key);
			index = probe(index, key);
			
			assert (index >= 0) && (index < hashTable.length);
			
			if ((hashTable[index] == null) || hashTable[index].isRemoved()) {
				hashTable[index] = new TableEntry<>(key, value);
				hashTable[index].sll.add(value);
				hashTable[index].sll.containOfValue(value);
				numberOfEntries++;
				oldValue = null;
			}
			else {
				collision_count++;
				if (!(hashTable[index].sll.isContainOfValue(value))) {
					hashTable[index].sll.add(value);
				}
				hashTable[index].sll.containOfValue(value);
				oldValue = hashTable[index].getValue();
				//hashTable[index].setValue(value);
			}
			//System.out.println(index+ "-"+hashTable.length+"-"+numberOfEntries+"-"+hashTable[index].sll.getFreqOfValue(value));
			if (isHashTableTooFull())
				enlargeHashTable();
			
			return oldValue;
		}
	}
	
	private boolean isHashTableTooFull() {
		if (((double)numberOfEntries / (double)hashTable.length) >= MAX_LOAD_FACTOR)
			return true;
		else
			return false;
	}

	private int probe(int index, K key) {
		boolean found = false;
		int removedStateIndex = -1;
		int hk = 0;
		int dk = 0;
		int up = 0;
		index = index % hashTable.length;
		
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn()) {
				if (key.equals(hashTable[index].getKey())) {
					found = true;
				}
				else {
					if (chosenCH == 1) {
						index = (index + 1) % hashTable.length; //Linear probing
					}
					else {
						hk = index % hashTable.length;
						dk = 31 - (index % 31);
						index = (hk + (dk * up)) % hashTable.length; //double hashing
						up++;
					}
				}
			}
			else {
				if (removedStateIndex == -1)
					removedStateIndex = index;
				if (chosenCH == 1) {
					index = (index + 1) % hashTable.length; //Linear probing
				}
				else {
					hk = index % hashTable.length;
					dk = 31 - (index % 31);
					index = (hk + (dk * up)) % hashTable.length; //double hashing
					up++;
				}
				
			}
		}
		
		if (found || (removedStateIndex == -1))
			return index;
		else
			return removedStateIndex;
	}
	
	private void enlargeHashTable() {
		TableEntry<K, V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(oldSize + oldSize);
		checkSize(newSize);
		
		@SuppressWarnings("unchecked")
		TableEntry<K, V>[] temp = (TableEntry<K, V>[])new TableEntry[newSize];
		hashTable = temp;
		numberOfEntries = 0;
		
		for (int index = 0; index < oldSize; index++) {
			if ((oldTable[index] != null) && oldTable[index].isIn())
				put(oldTable[index].getKey(), oldTable[index].getValue());
		}
	}
	
	public int getCC() {
		return collision_count;
	}
	
	//////////////////////////////////////////////////////////
	private static class TableEntry<S, T>{
		private S key;
		private T value;
		private States state;
		private enum States {CURRENT, REMOVED}
		private SingleLinkedList sll;
		
		private TableEntry(S searchKey, T dataValue){
			key = searchKey;
			value = dataValue;
			state = States.CURRENT;
			sll = new SingleLinkedList();
		}
		
		public boolean isIn() {
			if ((key != null) && (value != null))
				return true;
			else
				return false;
		}

		public boolean isRemoved() {
			if ((key == null) && (value == null))
				return true;
			else
				return false;
		}

		public void setToRemoved() {
			key = null;
			value = null;
		}

		public S getKey() {
			return key;
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}
		
	}
	
	private class KeyIterator implements Iterator<K>{
		private int currentIndex;
		private int numberLeft;
		
		private KeyIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}
		
		public boolean hasNext() {
			return numberLeft > 0;
		}
		
		public K next() {
			K result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || 
						hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				}
				
				result = hashTable[currentIndex].getKey();
				numberLeft--;
				currentIndex++;
			}
			else
				throw new NoSuchElementException();
			
			return result;
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
