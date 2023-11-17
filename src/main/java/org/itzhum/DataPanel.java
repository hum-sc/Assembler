package org.itzhum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;

public class DataPanel extends JPanel implements ActionListener {
    public JScrollPane scrollPane;
    JTextPane textArea;
    public DataPanel(String content, JSplitPane parent){
        super();

        this.setBorder(BorderFactory.createLineBorder(Color.orange));
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

        textArea = new JTextPane();
        textArea.setEditable(false);
        textArea.setContentType("text/plain");
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        textArea.setText(content);
        textArea.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()-navigationBar.getHeight()));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMaximumSize(new Dimension(this.getWidth(), this.getHeight()-navigationBar.getHeight()));

        add(scrollPane, BorderLayout.CENTER);

        textArea.setCaretPosition(0);

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
