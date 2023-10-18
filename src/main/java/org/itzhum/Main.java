package org.itzhum;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        JFrame f = new JFrame();
        JButton b = new JButton("Hola");
        b.setBounds(130,100,100,40);
        f.add(b);

        f.setSize(400,500);

        f.setLayout(null);

        f.setVisible(true);


    }
}