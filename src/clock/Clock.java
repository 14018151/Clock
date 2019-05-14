package clock;

import java.text.ParseException;

/**
 * main
 * @author Graham
 */
public class Clock {
    Alarms alarm = new Alarms();
    
    /**
     * Initialises the classes to be used.
     * @param args unused and only even accessed when running this program from command line
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        Model model = new Model();
        View view = new View(model);
        model.addObserver(view);
        Controller controller = new Controller(model, view);

    }
}
