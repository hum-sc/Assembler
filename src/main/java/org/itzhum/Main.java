package org.itzhum;
import org.itzhum.view.View;


public class Main {
    public static void main(String[] args) {
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model);
        controller.start();
    }
}