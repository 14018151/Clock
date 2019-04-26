package clock;

import java.util.Calendar;
import java.util.Observable;
//import java.util.GregorianCalendar;

public class Model extends Observable {
    Alarms alarm = new Alarms();
    
    int hour = 0;
    int minute = 0;
    int second = 0;
    
    int oldSecond = 0;
    
    public Model() {
        update();
    }
    
    public void update() {
        Calendar date = Calendar.getInstance();
        hour = date.get(Calendar.HOUR);
        minute = date.get(Calendar.MINUTE);
        oldSecond = second;
        second = date.get(Calendar.SECOND);
        if (oldSecond != second) {
            setChanged();
            notifyObservers();
        }
        
        String time = hour +":"+minute+":"+second;
        
        if(!alarm.isEmpty()){
            if(alarm.head().equals(time)){
            alarm.pop();
                System.out.println("test");
            }
        }
    }
    
    void nextAlarm() {
        System.out.println(alarm.head());
    }

    void addAlarm() {
        
    
        alarm.add("18:53:00");
    }

    void removeHead() {
        alarm.pop();
    }
    
    void printQueue(){
        System.out.println(alarm.toString());
    }
    
}
