/**
 * @author Montek Singh
 * @date 2/24/13
 * @version 1.0.1.0.1.10.101011
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class HashTable<K,V> {

	/**
	 * The maximum load factor for this hashtable
	 */
	private final double MAX_LOAD_FACTOR = .64;

	/**
	 * The number of entries in this hashtable
	 */
	private int size = 0;
	

	/**
	 * The underlying array for this hashtable
	 */
	private Entry<K,V>[] table = new Entry[11];
	
	
	/**
	 * Puts the key value pair into the table. If the
	 * key already exists in the table, replace the
	 * old value with the new one and return the old value
	 * 
	 * @param key, never null
	 * @param value, possibly null
	 * @return the replaced value, null if nothing existed previously
	 */
	public V put(K key ,V value) {
		int hashCode = Math.abs(key.hashCode());
		V oldVal = null;
		hashCode = compress(hashCode);
		
		if(table[hashCode] == null){
			table[hashCode] = new Entry(key,value);
		}else
			if(table[hashCode].getKey() == key){
				table[hashCode].setValue(value);
			}else
				if(table[hashCode].isAvailable()){
					oldVal = table[hashCode].getValue();
					table[hashCode].setKey(key);
					table[hashCode].setValue(value);
					table[hashCode].setAvailable(false);
				}else{
					while(table[hashCode] != null && !table[hashCode].isAvailable()){
						if(table[hashCode].isAvailable()){ 
							oldVal = table[hashCode].getValue();
							table[hashCode].setKey(key);
							table[hashCode].setValue(value);
							table[hashCode].setAvailable(false);
							break;
						}
						if(hashCode==table.length-1){
							hashCode = -1;
						}
						hashCode++;
					}
				table[hashCode] = new Entry(key,value);
			} 
		size++;
		checkLoadFactor();
		
		return oldVal;
	}
	// ---------------------Update SIZE + 1
	private void checkLoadFactor(){
		if ((double)size/table.length > MAX_LOAD_FACTOR) {
			size = 0;
			int new_size = 2 * table.length + 1;
		    Entry<K,V>[] new_table = new Entry[table.length];
		    for(int i = 0; i < table.length; i++){
		        if(table[i] != null && !(table[i].isAvailable())){
		            new_table[i] = table[i];
		        }
		    }
		    table = new Entry[new_size];
		    for(int i = 0; i < new_table.length; i++){
		        if(new_table[i] != null && !(new_table[i].isAvailable())){
		            put(new_table[i].getKey(),new_table[i].getValue());
		        }
		    }
		    
		    
		}
		
		
	}
	
	private int compress(int hashCode){
		return hashCode % table.length;
	}
	
	/**
	 * Removes the entry containing the given key
	 * 
	 * (remember that all objects have a hashCode method)
	 * 
	 * @param key, never null
	 * @return the value of the removed entry
	 */
	public V remove(Object key) {
		int hashCode = Math.abs(key.hashCode());
		hashCode = compress(hashCode);
		
		while(table[hashCode] != null){
			if(table[hashCode].getKey().equals(key)){
				table[hashCode].setAvailable(true);
				size--;
				return table[hashCode].getValue();
			}
			if(hashCode==table.length){
				hashCode = -1;
			}
			hashCode++;
		}
		size--;
		return null;
	}
	
	/**
	 * Gets the value of the entry given a specific key
	 * 
	 * (remember that all objects have a hashCode method)
	 * 
	 * @param key, never null
	 * @return
	 */
	public V get(Object key) {
		int hashCode = Math.abs(key.hashCode());
		hashCode = compress(hashCode);
		
		while(table[hashCode] != null){
			if(table[hashCode].getKey().equals(key)){
				return table[hashCode].getValue();
			}
			if(hashCode==table.length-1){
				hashCode = -1;
			}
			hashCode++;
		}
		return null;
	}
	
	/**
	 * @param key, never null
	 * @return true if this table contains the given key, false otherwise
	 */
	public boolean containsKey(Object key) {
		int hashCode = Math.abs(key.hashCode());
		hashCode = compress(hashCode);
		
		while(table[hashCode] != null){
			if(table[hashCode].getKey().equals(key)){
				return true;
			}
			if(hashCode==table.length-1){
				hashCode = -1;
			}
			hashCode++;
		}
		return false;
	}
	
	/**
	 * Clears this hashTable
	 */
	public void clear() {
		table = new Entry[11];
		size = 0;
		
	}
	
	/**
	 * @return true if this hashtable is empty, false otherwise
	 */
	public boolean isEmpty() {
		if(size == 0 && table == null)
			return true;
		return false;
	}
	
	/**
	 * @return the value from this hashtable
	 */
    public Collection<V> values() {
    	ArrayList<V> broseph = new ArrayList<V>();
        for(int i = 0; i < table.length; i++){
        	if(table[i] != null && !table[i].isAvailable())
            broseph.add(table[i].getValue());
        }
        return broseph;
    }
       
    /**
     * @return the unique keys from this hashtable
     */
    public Set<K> keySet() {
    	HashSet<K> bro = new HashSet<K>();
        for(int i = 0; i < table.length; i++){
        	if(table[i] != null && !(table[i].isAvailable()) && !(bro.contains(table[i].getKey())))
        		bro.add(table[i].getKey());
        }
        return bro;
    }

	
	/**
	 * @return the unique entries from this hashtable
	 */
	public Set<Entry<K, V>> entrySet() {
		HashSet<Entry<K,V>> set = new HashSet<Entry<K, V>>();
        for(int i = 0; i < table.length; i++){
            if(table[i] != null && !(table[i].isAvailable()) && !(set.contains(table[i])))
                set.add(table[i]);
        }
        return set;
	}
	
	/**
	 * @return the size of this hashtable
	 */
	public int size() {
		return size;
	}
    public String toString(){
        ArrayList<String> broseph = new ArrayList<String>();
        for(int i = 0; i < table.length; i++){
            if(table[i] == null)
                broseph.add(null);
            else{
            	if(table[i].isAvailable())
            		broseph.add("hidden");
            	else
            		broseph.add((String)table[i].getValue());
            }
        }
        return broseph.toString();
    }


	/*
	 * Don't modify any code below this point
	 */
	
	public void setSize(int size) {
		this.size = size;
	}

	public Entry<K,V>[] getTable() {
		return table;
	}

	public void setTable(Entry<K,V>[] table) {
		this.table = table;
	}

	public double getMaxLoadFactor() {
		return MAX_LOAD_FACTOR;
	}

	public static class Entry<K,V> {
		private K key;
		private V value;
		private boolean available;
		
		public Entry(K key, V value) {
			this.setKey(key);
			this.setValue(value);
			this.setAvailable(false);
		}
		
		public void setKey(K key) {
			this.key = key;
		}
		
		public K getKey() {
			return this.key;
		}
		
		public void setValue(V value) {
			this.value = value;
		}
		
		public V getValue() {
			return this.value;
		}

		public boolean isAvailable() {
			return available;
		}

		public void setAvailable(boolean available) {
			this.available = available;
		}
	}
}
