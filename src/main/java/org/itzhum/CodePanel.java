package org.itzhum;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.*;
import java.io.Console;

public class CodePanel extends JPanel implements ActionListener {
    public JScrollPane scrollPane;

    JTextPane codeArea;

    JTextPane numberLineArea;

    JScrollPane codeScrollPane;
    LineNumbersView lineNumbers;
    public CodePanel(String content){
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



        codeArea = new JTextPane();
        codeArea.setEditable(false);
        codeArea.setContentType("text/plain");
        codeArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        codeArea.setText(content);
        codeArea.setPreferredSize(new Dimension(this.getWidth()-25, this.getHeight()-navigationBar.getHeight()));




        codeScrollPane = new JScrollPane(codeArea);
        codeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        codeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        codeScrollPane.setMaximumSize(new Dimension(this.getWidth()-25, this.getHeight()-navigationBar.getHeight()));
        codeScrollPane.setRowHeaderView(numberLineArea);

        lineNumbers = new LineNumbersView(codeArea);
        codeScrollPane.setRowHeaderView(lineNumbers);

        this.add(codeScrollPane, BorderLayout.CENTER);
        codeArea.setCaretPosition(0);
    }

    public void setErrorLines(int[] errorLines) {
        this.lineNumbers.setErrors(errorLines);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("next")){

            System.out.println("Esta en: "+codeScrollPane.getVerticalScrollBar().getValue());
            codeScrollPane.getVerticalScrollBar().setValue(codeScrollPane.getVerticalScrollBar().getValue()+300);
            System.out.println("Se movio a: "+codeScrollPane.getVerticalScrollBar().getValue());

        } else if (e.getActionCommand().equals("previous")){
            codeScrollPane.getVerticalScrollBar().setValue(codeScrollPane.getVerticalScrollBar().getValue()-300);

        }

    }
}
