package org.itzhum.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComponentPanel extends JPanel implements ActionListener {
    private final JTextPane componentArea;
    private final JTextPane componentTypeArea;
    private final JScrollPane componentScrollPane;
    public ComponentPanel(String[] componentList){
        super();
        this.setLayout(new BorderLayout());


        JToolBar navigationBar = new JToolBar();
        JButton next = new JButton("Siguiente");
        next.setActionCommand("next");
        next.addActionListener(this);
        JButton previous = new JButton("Anterior");
        previous.setActionCommand("previous");
        previous.addActionListener(this);
        navigationBar.add(previous);
        navigationBar.add(next);
        add(navigationBar, BorderLayout.SOUTH);

        componentArea = new JTextPane();
        componentArea.setEditable(false);
        componentArea.setContentType("text/plain");
        componentArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        componentArea.setText(componentList[0]);
        //componentArea.setPreferredSize(new Dimension(this.getWidth()/2, this.getHeight()-navigationBar.getHeight()));
        componentArea.setMaximumSize(new Dimension(this.getWidth()/2, this.getHeight()-navigationBar.getHeight()));

        componentTypeArea = new JTextPane();
        componentTypeArea.setEditable(false);
        componentTypeArea.setContentType("text/plain");
        componentTypeArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        componentTypeArea.setText(componentList[1]);
        componentTypeArea.setPreferredSize(new Dimension(this.getWidth()/2, this.getHeight()-navigationBar.getHeight()));



        componentScrollPane = new JScrollPane(componentTypeArea);
        componentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        componentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        componentScrollPane.setMaximumSize(new Dimension(this.getWidth(), this.getHeight()-navigationBar.getHeight()));
        componentScrollPane.setRowHeaderView(componentArea);

        add(componentScrollPane, BorderLayout.CENTER);
        componentTypeArea.setCaretPosition(0);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("next")){

            System.out.println("Esta en: "+componentScrollPane.getVerticalScrollBar().getValue());
            componentScrollPane.getVerticalScrollBar().setValue(componentScrollPane.getVerticalScrollBar().getValue()+300);
            System.out.println("Se movio a: "+componentScrollPane.getVerticalScrollBar().getValue());

        } else if (e.getActionCommand().equals("previous")){
            componentScrollPane.getVerticalScrollBar().setValue(componentScrollPane.getVerticalScrollBar().getValue()-300);

        }

    }
}
