package org.itzhum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;

public class DataPanel extends JPanel implements ActionListener {
    public JScrollPane scrollPane;
    JTextArea textArea;
    public DataPanel(String content, JSplitPane parent){
        super();
        JToolBar navigationBar = new JToolBar();
        JButton next = new JButton("Siguiente");
        next.setActionCommand("next");
        next.addActionListener(this);
        JButton previous = new JButton("Anterior");
        previous.setActionCommand("previous");
        previous.addActionListener(this);
        navigationBar.add(previous);
        navigationBar.add(next);

        textArea = new JTextArea(20,460/14);
        textArea.setEditable(false);
        textArea.setLineWrap(false);
        textArea.setText(content);
        scrollPane = new JScrollPane(textArea);
        add(navigationBar, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        parent.add(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("next")){
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()+300);

        } else if (e.getActionCommand().equals("previous")){
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()-300);

        }

    }
}
