package edu.bu.ec504.hw1p2;

import java.util.*;
import java.util.*;

public class myLSDS extends LSDS {

	private final int resortLimit = 1024;
	private int n = 1;
	
	@Override
	public void INSERT(Integer x) {
		unsorted.add(x);
		if(unsorted.size() % resortLimit == 0){
			this.RESORT();
			n++;
		}
	}

	@Override
	public boolean QUERY(Integer x) {
		if(Collections.binarySearch(sorted, x) >= 0)
			return true;
		return unsorted.indexOf(x) != -1;
	}

	@Override
	protected void RESORT() {
//		sorted.addAll(unsorted);
//		unsorted.clear();
//		Collections.sort(sorted);
		Collections.sort(unsorted);
		sorted = merge(sorted, unsorted);
		unsorted.clear();	
	}
	
	private ArrayList<Integer> merge(ArrayList<Integer> list1, ArrayList<Integer> list2){
		ArrayList<Integer> result = new ArrayList<Integer>(300000);
		int length = list1.size() + list2.size();
		int index1 = 0;
		int index2 = 0;
		list1.add(Integer.MAX_VALUE);
		list2.add(Integer.MAX_VALUE);
		
		for(int i = 0; i < length; i++){
			// picked the smaller one
			if(list1.get(index1) > list2.get(index2)){
				result.add(list2.get(index2));
				index2++;
			}else{
				result.add(list1.get(index1));
				index1++;
			}
		}
		return result;
	}
}
