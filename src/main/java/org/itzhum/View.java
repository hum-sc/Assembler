package org.itzhum;

import org.itzhum.exceptions.ProcessCanceledException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.util.Scanner;

public class View extends JFrame {
    private BorderLayout borderLayout;
    public View (){
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setName("Ensamblador");
        setSize(720, 480);
        setLocationRelativeTo(null);
        borderLayout = new BorderLayout();
        this.setVisible(true);
    }

    public void showMainPage(ActionListener listener, String fileName){
        JPanel content;
        JButton select, next;
        JLabel title, selectLabel;

        JTextArea fileNameArea;

        Container container = this.getContentPane();
        container.removeAll();

        // Aquí ponemos el titulo
        title = new JLabel("Ensamblador");

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 40));

        container.add(title, BorderLayout.NORTH);

        //Parte central de la pantalla

        content = new JPanel();

        SpringLayout springLayout = new SpringLayout();


        content.setLayout(springLayout);

        select = new JButton("Seleccionar");
        select.setActionCommand("selectFile");
        select.addActionListener(listener);

        selectLabel = new JLabel("Selecciona el archivo a ensamblar (.asm)");
        selectLabel.setHorizontalAlignment(JLabel.CENTER);
        selectLabel.setVerticalAlignment(JLabel.CENTER);
        selectLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        fileNameArea = new JTextArea();
        fileNameArea.setText(fileName);
        fileNameArea.setFont(new Font("Arial", Font.PLAIN, 14));

        fileNameArea.setBounds(0, 0, 300, 50);

        fileNameArea.setEditable(false);
        fileNameArea.setLineWrap(true);
        fileNameArea.setWrapStyleWord(true);

        content.add(fileNameArea);

        content.add(selectLabel);
        content.add(select);

        springLayout.putConstraint(SpringLayout.SOUTH, selectLabel, 6, SpringLayout.VERTICAL_CENTER, content);
        springLayout.putConstraint(SpringLayout.WEST, selectLabel, 6, SpringLayout.WEST, fileNameArea);

        springLayout.putConstraint(SpringLayout.EAST, fileNameArea, 6, SpringLayout.HORIZONTAL_CENTER, content);
        springLayout.putConstraint(SpringLayout.NORTH, fileNameArea, 6, SpringLayout.SOUTH, selectLabel);

        springLayout.putConstraint(SpringLayout.WEST, select, 6, SpringLayout.EAST, fileNameArea);
        springLayout.putConstraint(SpringLayout.NORTH, select, 0, SpringLayout.NORTH, fileNameArea);

        container.add(content, BorderLayout.CENTER);


        // Aquí ponemos el botón de select
        next = new JButton("Continuar");
        next.addActionListener(listener);
        next.setActionCommand("assembledPage");

        container.add(next, BorderLayout.SOUTH);


        updateUI(container);


    }

    public void updateUI(Container container) {
        container.validate();
        container.repaint();
    }

    public void showAssembledPage(ActionListener listener){
        JPanel header;
        JButton first;
        JLabel title;

        JSplitPane splitPane;



        Container container = this.getContentPane();
        container.removeAll();
        // Aquí ponemos el titulo
        title = new JLabel("Ensamblador");

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 40));

        first = new JButton("Regresar");

        first.addActionListener(listener);
        first.setActionCommand("firstPage");

        first.setSize(48,48);

        header = new JPanel( new FlowLayout(FlowLayout.LEADING) );

        header.add(first);
        header.add(title);

        container.add(header, BorderLayout.NORTH);

        //Parte central de la pantalla

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        splitPane.setLeftComponent(scrollPane);

        JTextArea textArea2 = new JTextArea();
        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        splitPane.setRightComponent(scrollPane2);

        splitPane.setDividerLocation(360);
        splitPane.setEnabled(true);

        container.add(splitPane, BorderLayout.CENTER);


        updateUI(container);


    }
    public File getFile() throws FileNotFoundException, ProcessCanceledException, Exception{
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo ensamblador (*.asm)","asm");
        chooser.setFileFilter(filter);

        int retVal = chooser.showOpenDialog(this);
        if ( retVal == JFileChooser.APPROVE_OPTION ) {
            return chooser.getSelectedFile();
        } else if( retVal == JFileChooser.CANCEL_OPTION ){
            throw new ProcessCanceledException( "El usuario canceló el proceso" );
        } else if (retVal == JFileChooser.ERROR_OPTION) {
            throw  new Exception("Error al seleccionar el archivo");
        }
        return  null;
    }
}
