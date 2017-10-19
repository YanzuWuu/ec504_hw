package edu.bu.ec504.hw1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.bu.ec504.hw1.Compressor.Atom;

class myCompressor extends Compressor {

	private static final long serialVersionUID = 1L;

	
	private ArrayList<Integer> tempIndex = new ArrayList<Integer>();
	myCompressor() { super(); }

	@Override
	void readCompress(BufferedReader in) {
		// Read in strings, one by one, and add them as dictionary entries.
		dict = myDictInit();

		
		String str;
		String old = "";
		String read = "";
		String combine = "";
		dict.add("");
		
		try {
			while ((str = in.readLine()) != null) {
				// System.out.println(str);
				for(int i = 0; i < str.length(); i++){
					read = "" + str.charAt(i);	
					combine = old+read;

					if(dict.contains(combine)){
						old = combine;
					}else{
						if(!dict.contains(read)){
							dict.add(read);
						}
						if(!combine.equals(read)){
							dict.add(combine);
						}
						int index = dict.indexOf(old);
						if(index >= 0){
							tempIndex.add(index);
						}
						old = read;
					}				
				}
			}
			tempIndex.add(dict.indexOf(old));
		}catch (IOException e) {
			e.printStackTrace();
		}

		//int mark = 0;
		String last = "";
		String curr = "";
		// update compressed
		for(int i = 0; i < tempIndex.size(); i++){
			last += dict.get(tempIndex.get(i));
			if(dict.contains(last)){
				//mark++;
				curr = last;
			}else{
				compressed.add(new Atom(dict.indexOf(curr), ""));
				curr = dict.get(tempIndex.get(i));
				last = dict.get(tempIndex.get(i));
				//mark = i;
			}
		}
	}

	private ArrayList<String> myDictInit(){
		ArrayList<String> initDict = new ArrayList<String>();
		for(int i = 32; i < 127; i++){
			initDict.add("" + (char)i);
		}
		return initDict;
	}
}

