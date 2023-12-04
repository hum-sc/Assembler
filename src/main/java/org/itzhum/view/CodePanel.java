package org.itzhum.view;

import org.itzhum.view.ColoredText.ANSIColorConstants;
import org.itzhum.view.ColoredText.ColoredTextPane;
import org.itzhum.view.ColoredText.GUIConstants;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;

public class CodePanel extends JPanel implements ActionListener {
    public JScrollPane scrollPane;

    JTextPane codeArea;

    JTextPane numberLineArea;


    JScrollPane codeScrollPane;
    LineNumbersView lineNumbers;
    LineNumbersView machineCodeLineNumbers;


    private String remaining = "";
    protected Color currentColor = ANSIColorConstants.COLOR_RESET;;
    protected boolean isBackground = false;
    protected StringBuffer typed = new StringBuffer("");
    private boolean colorMode;
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
        codeArea.setPreferredSize(new Dimension(this.getWidth()-25, this.getHeight()-navigationBar.getHeight()));

        this.colorMode = true;
        codeArea.setFont(GUIConstants.DEFAULT_FONT);
        codeArea.setBackground(ANSIColorConstants.BACKGROUND_RESET);
        codeArea.setForeground(ANSIColorConstants.COLOR_RESET);
        //this.setPreferredSize();
        this.append(content);

        codeArea.setBorder(BorderFactory.createLineBorder(Color.orange));





        codeScrollPane = new JScrollPane(codeArea);
        codeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        codeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        codeScrollPane.setMaximumSize(new Dimension(this.getWidth()-25, this.getHeight()-navigationBar.getHeight()));

        lineNumbers = new LineNumbersView(codeArea);
        codeScrollPane.setRowHeaderView(lineNumbers);

        this.add(codeScrollPane, BorderLayout.CENTER);
        codeArea.setCaretPosition(0);
    }
    public CodePanel(String content, String[] counterProgram){
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
        codeArea.setPreferredSize(new Dimension(this.getWidth()-25, this.getHeight()-navigationBar.getHeight()));

        this.colorMode = true;
        codeArea.setFont(GUIConstants.DEFAULT_FONT);
        codeArea.setBackground(ANSIColorConstants.BACKGROUND_RESET);
        codeArea.setForeground(ANSIColorConstants.COLOR_RESET);
        //this.setPreferredSize();
        this.append(content);

        codeArea.setBorder(BorderFactory.createLineBorder(Color.orange));





        codeScrollPane = new JScrollPane(codeArea);
        codeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        codeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        codeScrollPane.setMaximumSize(new Dimension(this.getWidth()-25, this.getHeight()-navigationBar.getHeight()));
        codeScrollPane.setRowHeaderView(numberLineArea);

        lineNumbers = new LineNumbersView(codeArea, counterProgram);
        codeScrollPane.setRowHeaderView(lineNumbers);

        this.add(codeScrollPane, BorderLayout.CENTER);
        codeArea.setCaretPosition(0);
    }

    public CodePanel(String content, String[] counterProgram, String[] machineCode){
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
        codeArea.setPreferredSize(new Dimension(this.getWidth()-25, this.getHeight()-navigationBar.getHeight()));

        this.colorMode = true;
        codeArea.setFont(GUIConstants.DEFAULT_FONT);
        codeArea.setBackground(ANSIColorConstants.BACKGROUND_RESET);
        codeArea.setForeground(ANSIColorConstants.COLOR_RESET);
        //this.setPreferredSize();
        this.append(content);

        codeArea.setBorder(BorderFactory.createLineBorder(Color.orange));





        codeScrollPane = new JScrollPane(codeArea);
        codeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        codeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        codeScrollPane.setMaximumSize(new Dimension(this.getWidth()-25, this.getHeight()-navigationBar.getHeight()));
        codeScrollPane.setRowHeaderView(numberLineArea);

        lineNumbers = new LineNumbersView(codeArea, counterProgram, machineCode);

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
            codeScrollPane.getVerticalScrollBar().setValue(codeScrollPane.getVerticalScrollBar().getValue()+300);

        } else if (e.getActionCommand().equals("previous")){
            codeScrollPane.getVerticalScrollBar().setValue(codeScrollPane.getVerticalScrollBar().getValue()-300);

        }



    }

    protected void append(String s, Color c, boolean background)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                background ? StyleConstants.Background : StyleConstants.Foreground, c);

        int len = codeArea.getDocument().getLength();//same value as getText().length();
        codeArea.setCaretPosition(len); //place caret at the end (with no selection)
        codeArea.setCharacterAttributes(aset, false);
        codeArea.replaceSelection(s); //there is no selection, so inserts at caret
    }

    public void append(String s)
    {
        codeArea.setEditable(true);
        //Add what is going to be appeneded to the history of what was typed.
        this.typed.append(s);
        //currentColor char position in addString
        int aPos = 0;
        //index of next Escape sequence
        int aIndex = 0;
        //index of "m" terminating Escape sequence
        int mIndex = 0;
        String tmpString = "";
        //true until no more Escape sequences
        boolean stillSearching = true;
        String addString = this.remaining + s;
        this.remaining = "";

        if(addString.length() > 0)
        {
            //find first escape
            aIndex = addString.indexOf(ANSIColorConstants.ESCAPE_TEXT);
            if(aIndex == -1)
            {
                //no escape code in this string, so just send it with currentColor color
                this.append(addString, this.currentColor, isBackground);
                return;
            }

            //otherwise there is an escape character in the string, so we process it
            if(aIndex > 0)
            {
                //Escape is not first char, so send text up to first escape
                tmpString = addString.substring(0, aIndex);
                this.append(tmpString, this.currentColor, isBackground);
                aPos = aIndex;
            }

            //aPos is now at the beginning of the first escape sequence
            stillSearching = true;
            while(stillSearching)
            {
                //find the end of the escape sequence
                mIndex = addString.indexOf(ANSIColorConstants.ESCAPE_TEXT_END, aPos);
                if(mIndex < 0)
                {
                    //the buffer ends halfway through the ansi string!
                    this.remaining = addString.substring(aPos, addString.length());
                    stillSearching = false;
                    continue;
                }
                else
                {
                    tmpString = addString.substring(aPos, mIndex + 1);
                    if(ANSIColorConstants.isEscape(tmpString))
                    {
                        this.currentColor =
                                ANSIColorConstants.isReset(tmpString) || !this.colorMode ?
                                        ANSIColorConstants.COLOR_RESET :
                                        ANSIColorConstants.getANSIColor(tmpString);
                        isBackground = ANSIColorConstants.isBackgroundEscape(tmpString);

                        if(ANSIColorConstants.isReset(tmpString) || !this.colorMode)
                        {
                            this.isBackground = false;
                            this.append("", ANSIColorConstants.COLOR_RESET, false);
                            this.append("", ANSIColorConstants.BACKGROUND_RESET, true);
                        }
                    }
                    else
                    {
                        //The escape sequence was received, but did not have a valid code
                        mIndex = aPos;
                        //skip the escape text, but still process all the gobbldy gook
                    }
                }

                aPos = mIndex + 1;
                //now we have the color, send text that is in that color
                aIndex = addString.indexOf(ANSIColorConstants.ESCAPE_TEXT, aPos);

                if(aIndex == -1)
                {
                    //if that was the last sequence of the input, send remaining text
                    tmpString = addString.substring(aPos, addString.length());
                    this.append(tmpString, this.currentColor, this.isBackground);
                    stillSearching = false;
                    //jump out of loop early, as the whole string has been sent
                    continue;
                }

                //there is another escape sequence, so send part of the string
                tmpString = addString.substring(aPos, aIndex);
                aPos = aIndex;
                this.append(tmpString, this.currentColor, isBackground);
                //while there's text in the input buffer
            }
        }
        codeArea.setEditable(false);
    }
}
