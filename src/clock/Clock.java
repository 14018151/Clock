package clock;

import java.text.ParseException;

public class Clock {
    
    public static void main(String[] args) throws ParseException {
        Model model = new Model();
        View view = new View(model);
        model.addObserver(view);
        Controller controller = new Controller(model, view);

    }
}