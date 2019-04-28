package clock;

import java.util.Calendar;
import java.util.Observable;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
        hour = date.get(Calendar.HOUR_OF_DAY);
        minute = date.get(Calendar.MINUTE);
        oldSecond = second;
        second = date.get(Calendar.SECOND);
        if (oldSecond != second) {
            setChanged();
            notifyObservers();
            
            String time = hour +":"+minute+":"+second;
            if(!alarm.isEmpty()){
                if(alarm.head().equals(time)){
                    final JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "It is "+time,"Alarm", JOptionPane.OK_OPTION);
                    alarm.pop();
                }
            }
        }
    }
    
    String nextAlarm() {
        if(!alarm.isEmpty()){
            return alarm.head();
        }else{
            return "There are no alarms set";
        }
        
    }

    void addAlarm(String str) {
        alarm.add(str);
    }

    void removeHead() {
        alarm.pop();
    }
    
    void printQueue(){
        System.out.println(alarm.toString());
    }
    
    boolean checkEmpty(){
        return alarm.isEmpty();
    }
    
}
