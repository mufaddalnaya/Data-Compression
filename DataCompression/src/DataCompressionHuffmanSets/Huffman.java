/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCompressionHuffmanSets;

/**
 *
 * @author Mufaddal Naya
 */
import java.util.*;
import java.io.*;

public class Huffman{
	String org, encoded, decoded;
	HashMap<Character, Integer> freq;  //Store Frequency of every Character
	HashMap<String, Integer> freqSet;  //Store Frequency of every Character
	HashMap<String, String> prefix;  //Mapping Character to its Prefix code (for encoding) chr to String
	HashMap<String, String> character;   //Mapping Prefix code to its Character (for decoding) String to char
	PriorityQueue<node> pq;   //heap
        HashMap<Character,String> hm;
	node root;   //root of our prefix tree

	static class node{  //node of prefix tree
		String ch;
		int freq;
		node left,right;
		node(String c, int f, node l, node r){
			ch = c; freq = f;
			left = l;
			right = r;
		}
	}
	
	Huffman(String orgStr){  //Huffman Constructor
		//initializing
		org = orgStr;
		encoded="";
		decoded="";
		freq=new HashMap<>();
		freqSet=new HashMap<>();
                hm = new HashMap<>();
		prefix=new HashMap<>();
		character = new HashMap();
		pq=new PriorityQueue<node>(1,new Comparator<node>(){
			public int compare(node n1, node n2){
				if(n1.freq < n2.freq)
					return -1;
				else if(n1.freq > n2.freq)
					return 1;
				return 0;
			}
		});
		countFreq();  // STEP 1: Count frequency of word
                            //System.out.println("return getHuffmanTree");
		getHuffmanTree();  // STEP 2: Build Huffman Tree
		buildCodeTable();     // STEP 3: Build Huffman Code Table
	}
	
	void buildCodeTable(){
		String code = "";
		node n = root;
		buildCodeRecursion(n, code);
	}
	
	void buildCodeRecursion(node n, String code){
		if(n!=null){
			if(!leaf(n)){
				buildCodeRecursion(n.left, code + '0');
				buildCodeRecursion(n.right, code + '1');
			}
			else{
				prefix.put(n.ch, code); // for {character:code}
				character.put(code, n.ch); // for {code:character}
			}
		}
	}
	
	boolean leaf(node n){
		return (n.left == null) && (n.right == null);
	}
	
	void getHuffmanTree(){
            //System.out.println("getHuffmanTree");
		buildLeaf();
		node left, right;
		while(!pq.isEmpty()){
			left = pq.poll();
			if(pq.peek() != null){
				right = pq.poll();
				root = new node("\0", left.freq+right.freq, left, right);
			}
			else{
				root = new node("\0", left.freq, left, null);
			}
			
			if(pq.peek() != null){
				pq.offer(root);
			}
		}
	}
	
	void buildLeaf(){
		for(Map.Entry<String, Integer> iter : freqSet.entrySet()){
			String c = iter.getKey();
			int freqChar = iter.getValue();
			node n = new node(c, freqChar, null, null);
                        pq.offer(n);
		}
	}
	
	void countFreq(){
		char c;
		int cnt, n = org.length();
                //System.out.println(""+org);
		for(int i=0; i<n; ++i){
			c = org.charAt(i);
			if(freq.containsKey(c))
				cnt = freq.get(c) + 1;
			else
				cnt = 1;
			freq.put(c, cnt);
		}
                
                int cntfreq[][] = new int[freq.size()][2];
                int cn = 0;
                for(Map.Entry<Character, Integer> iter : freq.entrySet()){
			char ch = iter.getKey();
			int freqChar = iter.getValue();
                        cntfreq[cn][0] = freqChar;
                        cntfreq[cn++][1] = ch;
		}
                sortbyColumn(cntfreq, 0);
                            //System.out.println("Hello");
                for(int i=freq.size()-1;i>=0;i--){
                    String st = ""+(char)cntfreq[i][1];
                    int cnn = cntfreq[i][0];
                    char s1 = (char)cntfreq[i][1];
                    char s2 = 0;
                    if(--i!=-1){
                        st+=(char)cntfreq[i][1];
                        cnn+=cntfreq[i][0];
                        s2 = (char)cntfreq[i][1];
                    }
                    hm.put(s1, st);
                    if(s2 != 0)
                        hm.put(s2, st);
                    freqSet.put(st, cnn);
                    //System.out.println(i+" "+st+" "+cnn);
                }
                            //System.out.println("returning ");

                
	}
        public static void sortbyColumn(int arr[][], int col) 
    {  
		
		Arrays.sort(arr, new Comparator<int[]>()
		{
			public int compare(int[] o1, int[] o2){
               return(Integer.valueOf(o1[col]).compareTo(o2[col]));
			}
		});
		
    }
	
	public String encode(){
		StringBuilder sb = new StringBuilder();
		char ch;
		for(int i=0; i<org.length(); i++){
			ch = org.charAt(i);
                        String str = hm.get(ch);
                        if(str.charAt(0) == ch)
                            sb.append(prefix.get(str)+"0");
			else
                            sb.append(prefix.get(str)+"1");
		}
		encoded = sb.toString();
		//System.out.println(encoded);
		return encoded;
	}
	
	public String decode(){
		StringBuilder sb = new StringBuilder();
		String t = "";
		//System.out.println(encoded.charAt(0));
		for(int i=0; i<encoded.length(); i++){
			t += encoded.charAt(i);
			if (character.containsKey(t)){
				sb.append(character.get(t));
				t = "";
			}
		}
		decoded = sb.toString();
		//System.out.println(decoded);
		return decoded;
	}
	
}
