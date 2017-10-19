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
	
	private ArrayList<Integer> compressed = ArrayList<Integer>();
	
	myCompressor() { super(); }

	@Override
	void readCompress(BufferedReader in) {
		String str;
		String old = "";
		String combine = "";
		for(int i=0; i<256; i++){
			dict.add("" + (char)i);
		}
		
		try {
			while ((str = in.readLine()) != null) {
				for(char read : str.toCharArray()){
					combine = old+read;
					if(dict.contains(combine)){
						old = combine;
					}else{
						dict.add(combine);
						//System.out.println(old);
						int index = dict.indexOf(old);
						compressed.add(new Atom(index, ""));				
						old = "" + read;
					}			
				}
			}
			if(!old.equals(""))
				compressed.add(new Atom(dict.indexOf(old), ""));
		}catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(dict.toString());
	}
}

