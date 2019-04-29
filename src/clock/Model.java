package clock;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
                    JOptionPane.showMessageDialog(null, "It is "+time,"Alarm", JOptionPane.OK_OPTION);
                    
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
    
    String printQueue(){
        return alarm.toString();
    }
    
    boolean checkEmpty(){
        return alarm.isEmpty();
    }
    
    void viewAlarms(){
        if (checkEmpty()) {
            JFrame viewFrame = new JFrame();
            JOptionPane.showMessageDialog(viewFrame, "There are no alarms set", "Empty", JOptionPane.OK_OPTION);
        } else {
            Object[] allAlarms  = alarm.items.toArray();
            
            Arrays.sort(allAlarms);            
            
            JPanel viewPanel = new JPanel();
            
            for(int x = 0; x < allAlarms.length; x++){
                JTextField alarmEdit = new JTextField(8);
                
                alarmEdit.setText(allAlarms[x].toString());
                viewPanel.add(alarmEdit);
                
                JButton editButton = new JButton("Edit");
                viewPanel.add(editButton);
                
                JButton delButton = new JButton("Delete");
                viewPanel.add(delButton);
                
                viewPanel.add(new JLabel(" "));
            }
             
            JOptionPane.showMessageDialog(null, viewPanel, "Alarms", JOptionPane.PLAIN_MESSAGE );
        }
    }
    
}
