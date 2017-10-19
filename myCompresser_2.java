package edu.bu.ec504.hw1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// import edu.bu.ec504.hw1.Compressor.Atom;

class myCompressor extends Compressor {

	private static final long serialVersionUID = 1L;

	private Map<Integer, Integer> count = new HashMap<Integer, Integer>();
	private List<Integer> tempIndex = new ArrayList<Integer>();
	private List<String> tempDict = new ArrayList<String>();
	private ArrayList<String> mostOccur = new ArrayList<String>();

	myCompressor() { super(); }

	@Override
	void readCompress(BufferedReader in) {
		// Read in strings, one by one, and add them as dictionary entries.
		List<String> copyInput = new ArrayList<String>();
		String str;
		try {
			while((str = in.readLine()) != null){
				copyInput.add(str);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}


		initDict(copyInput);

		String old = "";
		String read = "";
		String combine = "";
		//dict.add("");
		dict = mostOccur;

		for(int i = 0; i < copyInput.size(); i++){
			str = copyInput.get(i);
			for(int j = 0; j < str.length(); j++){
				read = "" + str.charAt(j);	
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

					short index = (short) dict.indexOf(old);
					if(index >= 0){
						compressed.add(new Atom(index, ""));
					}
					old = read;
				}				
			}
		}


		compressed.add(new Atom(dict.indexOf(old), ""));

	}


	private void initDict(List<String> copyInput){
		// do one round of lempel ziv
		String str;
		String old = "";
		String read = "";
		String combine = "";
		tempDict.add("");

		for(int i = 0; i < copyInput.size(); i++){
			str = copyInput.get(i);
			for(int j = 0; j < str.length(); j++){
				read = "" + str.charAt(j);	
				combine = old+read;

				if(tempDict.contains(combine)){
					old = combine;
				}else{

					tempDict.add(combine);

					int index = tempDict.indexOf(old);
					if(index >= 0){
						tempIndex.add(index);
					}
					old = "";
				}				
			}
		}

		tempIndex.add(tempDict.indexOf(old));


		for(Integer i : tempIndex){
			if(count.containsKey(i)){
				count.put(i, count.get(i) + 1);
			}else{
				count.put(i,  1);
			}
		}

		Iterator<Entry<Integer, Integer>> itr = count.entrySet().iterator();
		while(itr.hasNext()){
			Entry<Integer, Integer> pair = itr.next();

			if(pair.getValue() > 5){
				mostOccur.add(tempDict.get(pair.getKey()));
			}
		}
		System.out.println(mostOccur);
	}

}

