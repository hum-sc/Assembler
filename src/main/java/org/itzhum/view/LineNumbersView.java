package org.itzhum.view;

import jdk.dynalink.beans.StaticClass;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashSet;
import java.util.Set;

class LineNumbersView extends JComponent implements DocumentListener, CaretListener, ComponentListener {

    private static final long serialVersionUID = 1L;
    private static int MARGIN_WIDTH_PX = 54;
private static int WORD_FONT_WIDTH_PX = 10;
    private int maxLength = 4;
    private String[] numbersAdded;

    private final JTextComponent editor;

    private final Set<Integer> errors;

    private Font font;

    private String[] numbers;

    public LineNumbersView(JTextComponent editor) {
        this.editor = editor;
        this.errors = new HashSet<Integer>();

        editor.getDocument().addDocumentListener(this);
        editor.addComponentListener(this);
        editor.addCaretListener(this);
    }
    public LineNumbersView(JTextComponent editor, String[] numbers) {
        this.editor = editor;
        this.errors = new HashSet<Integer>();

        editor.getDocument().addDocumentListener(this);
        editor.addComponentListener(this);
        editor.addCaretListener(this);



        this.numbers = numbers;

    }

    public LineNumbersView(JTextPane editor, String[] numbers, String[] numbersAdded, int maxNumbersLength, int maxNumbersAddedLength) {
        this.editor = editor;
        this.errors = new HashSet<Integer>();

        editor.getDocument().addDocumentListener(this);
        editor.addComponentListener(this);
        editor.addCaretListener(this);
        maxLength = maxNumbersLength+maxNumbersAddedLength+2;
        MARGIN_WIDTH_PX = WORD_FONT_WIDTH_PX* maxLength+WORD_FONT_WIDTH_PX;
        System.out.println(MARGIN_WIDTH_PX);
        this.numbers = numbers;
        this.numbersAdded = numbersAdded;
    }

    public void setErrors(int[] lineErrors){
        for(int i = 0; i < lineErrors.length; i++){
            errors.add(lineErrors[i]+1);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Rectangle clip = g.getClipBounds();
        int startOffset = editor.viewToModel(new Point(0, clip.y));
        int endOffset = editor.viewToModel(new Point(0, clip.y + clip.height));

        while (startOffset <= endOffset) {
            try {
                String lineNumber = getLineNumber(startOffset);
                if (lineNumber != null) {
                    int x = getInsets().left + 2;
                    int y = getOffsetY(startOffset);

                    font = font != null ? font : new Font(Font.MONOSPACED, Font.BOLD, editor.getFont().getSize());
                    g.setFont(font);

                    g.setColor( Color.GRAY);


                    g.drawString(lineNumber, x, y);
                }

                startOffset = Utilities.getRowEnd(editor, startOffset) + 1;
            } catch (BadLocationException e) {
                e.printStackTrace();
                // ignore and continue
            }
        }
    }

    /**
     * Returns the line number of the element based on the given (start) offset
     * in the editor model. Returns null if no line number should or could be
     * provided (e.g. for wrapped lines).
     */
    private String getLineNumber(int offset) {
        Element root = editor.getDocument().getDefaultRootElement();
        int index = root.getElementIndex(offset);
        Element line = root.getElement(index);

        if(numbers != null){
            if(numbersAdded != null){
                if(numbersAdded[index] == null) return line.getStartOffset() == offset ? numbers[index]:null;
                return line.getStartOffset() == offset ? numbers[index] +" ".repeat(maxLength-(numbers[index].length()+numbersAdded[index].length()))+ numbersAdded[index]:null;
            }
            return line.getStartOffset() == offset ? numbers[index]:null;
        }


        return line.getStartOffset() == offset ? String.format("%3d", index + 1) : null;
    }

    /**
     * Returns the y axis position for the line number belonging to the element
     * at the given (start) offset in the model.
     */
    private int getOffsetY(int offset) throws BadLocationException {
        FontMetrics fontMetrics = editor.getFontMetrics(editor.getFont());
        int descent = fontMetrics.getDescent();

        Rectangle r = editor.modelToView(offset);
        int y = r.y + r.height - descent;

        return y;
    }

    /**
     * Returns true if the given start offset in the model is the selected (by
     * cursor position) element.
     */
    private boolean hasError(int offset) {
        int caretPosition = editor.getCaretPosition();
        Element root = editor.getDocument().getDefaultRootElement();
        return errors.contains(root.getElementIndex(caretPosition));
    }

    /**
     * Schedules a refresh of the line number margin on a separate thread.
     */
    private void documentChanged() {
        SwingUtilities.invokeLater(() -> {
            repaint();
        });
    }

    /**
     * Updates the size of the line number margin based on the editor height.
     */
    private void updateSize() {
        Dimension size = new Dimension(MARGIN_WIDTH_PX, editor.getHeight());
        setPreferredSize(size);
        setSize(size);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        documentChanged();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateSize();
        documentChanged();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
        updateSize();
        documentChanged();
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}