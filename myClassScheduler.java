package edu.bu.ec504;

import java.util.*;
import javafx.util.Pair;

/**
 * Student implementation of the ClassScheduler
 * Created by Prof. Ari Trachtenberg on 9/10/17.
 */
public class myClassScheduler extends ClassScheduler {

    @Override
    public AssignmentSchedule makeSchedule() {
        AssignmentSchedule result=new AssignmentSchedule(nn);
        int targetClassLength = 0;
        int timeMarkStart = 0;
        TimeSlot targetTimeSlot = new TimeSlot(0);
        SchoolRoom targetRoomNumber = new SchoolRoom(-1);
        
        for(int i = 0; i < this.nn; i++){
        	targetClassLength = this.classLength.get(new SchoolClass(i));
        	SchoolClass targetClassNumber = new SchoolClass(i);
        	timeMarkStart = 0;
        	targetRoomNumber = new SchoolRoom(-1);
        	int[] availableTimePeriod = findAvailableTimeSlot(targetClassNumber, result);
        	
        	while(targetRoomNumber.rawDatum().intValue() == -1){
	        	targetTimeSlot = findTimeSlot(availableTimePeriod, targetClassLength, timeMarkStart);
	        	targetRoomNumber = findAvailableRoom(targetTimeSlot, targetClassLength, result);
	        	timeMarkStart = targetTimeSlot.rawDatum().intValue() + targetClassLength;
        	}
        	
        	result.addAssignment(targetClassNumber, targetTimeSlot, targetRoomNumber);
        	//timeMarkStart += targetClassLength;
        	//System.out.println("Finished scheduling one class !!!!");
        }
        
        //debug testing function
		//int[] availableTimeSlot = findAvailableTimeSlot(new SchoolClass(2), result);
		//findTimeSlot(availableTimeSlot, 2, 0);
        return result;
    }
    
    // Helper Methods
    
    /*
     * This method take the test class, and find all the scheduled class that is conflict 
     * with this class. And return a time spectrum that shows the scheduled time that has
     * already been taken by those conflicted classes. For the time spectrum, the time slot
     * which has been taken would be larger than zero indicating the time slot is not available.
     */
    private int[] findAvailableTimeSlot(SchoolClass myClass, AssignmentSchedule result){
    	int[] myTimeSlot = new int[1500];
    
    	// Retrieve all the scheduled class and iterate through each class to check conflict
    	Iterator<Map.Entry<SchoolClass, Pair<TimeSlot, SchoolRoom>>> itr 
    		= result.getAssignments().iterator();
    	while(itr.hasNext()){
    		
    		// get one scheduled class
    		Map.Entry<SchoolClass, Pair<TimeSlot, SchoolRoom>> element = itr.next();
    		
    		// get the class number
    		SchoolClass existClassTemp = element.getKey();
    		
    		if(this.conflicts.isConflicted(myClass, existClassTemp)){
    			//System.out.printf("Class: %d and Class: %d conflict\n", myClass.rawDatum().intValue(), existClassTemp.rawDatum().intValue());
    			int tempClassStartTime = element.getValue().getKey().rawDatum().intValue();
    			int tempClassLength = this.classLength.get(existClassTemp).intValue();
    			for(int i=tempClassStartTime; i < tempClassStartTime + tempClassLength; i++){
    				myTimeSlot[i]++;
    			}
    		}
    	}
    	
    	// debug output display the conflict check result
//    	System.out.println("Finished find conflict for class: " + myClass.rawDatum());
//    	for(int j : myTimeSlot){
//    		System.out.print(j);
//    	}
//    	System.out.println("\n");
    	return myTimeSlot;
    }
    
    
    /*
     * This method takes the time slot spectrum and the desired time length. It finds the 
     * first available begin time that is available and has the free space for the desired
     * length of slot. Start from certain time, and assume their always an available space.
     */
    private TimeSlot findTimeSlot(int[] timeAvailable, int length, int startTime){
    	Boolean found = false;
    	while(!found){
    		
    		if(timeAvailable[startTime] == 0){
    			int sumTemp = 0;
    			
    			// only execute while length is larger than 1
    			for(int i = startTime+1; i < startTime+length; i++){
    				sumTemp += timeAvailable[i];
    			}
    			
    			if(sumTemp == 0){
    				found = true;
    			}else{
    				startTime += length;
    			}
    		}else{
    			startTime++;
    		}
    	}
//    	System.out.println("Starting time: " + startTime);
    	return new TimeSlot(startTime);
    }
    
    /*
     * This method takes a time slot and return the available room number at this time slot.
     * Check the first available room at specific time slot. Assume Their is always an 
     * available room.
     */
    private SchoolRoom findAvailableRoom(TimeSlot classTime, int classLength, AssignmentSchedule result){
    	int availableRoomNumber = -1;
    	int newStartTime = classTime.rawDatum().intValue();
    	int newEndTime = newStartTime + classLength;
    	Boolean foundRoom = true;
    	
    	for(int i = 0; i < rr; i++){
    		foundRoom = true;
    		
    		// check if the room is occupied by some class at the desire time period
    		// if exist a class is scheduled at the same room, check if the time overlap 
    		// with the new class.
    		Iterator<Map.Entry<SchoolClass, Pair<TimeSlot, SchoolRoom>>> itr 
    			= result.getAssignments().iterator();
    		while(itr.hasNext() && foundRoom){
    			Map.Entry<SchoolClass, Pair<TimeSlot, SchoolRoom>> element = itr.next();
    			
    			// if a registered class using the same room, check if their time overlap
    			// with the new class
    			if(element.getValue().getValue().rawDatum().intValue() == i){
    				int existClassStartTime = element.getValue().getKey().rawDatum().intValue();
    				int existClassLength = this.classLength.get(element.getKey());
        			int existClassEndTime = existClassStartTime + existClassLength;
//        			
//        			System.out.printf("new start time: %d\nnew end time: %d\ntemp start time:"
//        					+ " %d\ntemp end time: %d\n", newStartTime
//        					, newEndTime, existClassStartTime, existClassEndTime);
//        			System.out.printf("new l: %d\nl: %d\n", classLength, existClassLength);
//        			
        			int min = Math.min(existClassStartTime, newStartTime);
        			int max = Math.max(existClassEndTime, newEndTime);
        			
        			// if the time span of both class is small than the total amount of these
        			// classed length, those two class must be using the same time slot
        			if(Math.abs(max - min) < Math.abs(existClassLength + classLength)) {
        				foundRoom = false;
        			}
    			}
    		}
    		
    		// if the room is available record the number and exit the loop
    		if(foundRoom){
    			availableRoomNumber = i;
    			break;
    		}
    	}
    	
    	//System.out.println("room number: " + availableRoomNumber);
    	
    	return new SchoolRoom(availableRoomNumber);
    }
}
