package clock;

import java.util.*;

/* Code for setting up the priority queue class
    GeeksforGeeks. (n.d.). PriorityQueue in Java - GeeksforGeeks. [online] 
    Available at: https://www.geeksforgeeks.org/priority-queue-class-in-java-2/ [Accessed 23 Apr. 2019].
*/
/**
 * @author 14018151 Joseph Kelly
 */
public class Alarms {
    // Creating empty priority queue 
    PriorityQueue<String> items = new PriorityQueue<String>(); 

    //Check if a specific item is in the queue
    public boolean checkContains(String test){
        return items.contains(test);
    }
    
    //Removes a specific alarm from the queue
    public void remove(String test){
        items.remove(test);
    }
   
    //Removes the head of the queue
    public void pop(){
        items.poll(); 
    }
    
    //Adds a new alarm to the queue
    public void add(String test){
        items.add(test);
    }
    
    //Returns the head of the queue
    public String head(){
        return items.peek(); 
    }
    
    //Removes all alarms from the queue
    public void clearAlarms(){
        items.clear();
    }
    
    //Returns true if queue is empty, otherwise returns false
    public boolean isEmpty(){
        return items.peek() == null;
    }
    
    //Returns the entire queue
    @Override
    public String toString(){
        Iterator itr = items.iterator(); 
        
        String str = "";
       
        while (itr.hasNext()){
           str += itr.next() + "\n";
        }
        return str;
    }
}
