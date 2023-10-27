package org.itzhum;

import org.itzhum.exceptions.ProcessCanceledException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.security.MessageDigest;
import java.util.Scanner;

public class View extends JFrame {

    static int height = 480;
    static int width = 920;
    public View (){
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setName("Ensamblador");
        setSize(width, height);
        setLocationRelativeTo(null);
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

        select.setSize(50, 50);

        selectLabel = new JLabel("Selecciona el archivo a ensamblar (.asm)");
        selectLabel.setHorizontalAlignment(JLabel.CENTER);
        selectLabel.setVerticalAlignment(JLabel.CENTER);
        selectLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        fileNameArea = new JTextArea();
        fileNameArea.setText(fileName);
        fileNameArea.setFont(new Font("Arial", Font.PLAIN, 14));

        fileNameArea.setRows(1);


        fileNameArea.setBounds(0, 0, 300, 50);

        fileNameArea.setEditable(false);
        fileNameArea.setLineWrap(true);
        fileNameArea.setWrapStyleWord(true);

        content.add(fileNameArea);

        content.add(selectLabel);
        content.add(select);

        springLayout.putConstraint(SpringLayout.SOUTH, selectLabel, 6, SpringLayout.VERTICAL_CENTER, content);
        springLayout.putConstraint(SpringLayout.WEST, selectLabel, 6, SpringLayout.WEST, fileNameArea);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, fileNameArea, -select.getWidth(), SpringLayout.HORIZONTAL_CENTER, content);
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

    public void showAssembledPage(ActionListener listener, String leftContent, String rightContent){
        JPanel header;
        JButton first;
        JLabel title;

        SpringLayout headerLayout;

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

        headerLayout = new SpringLayout();

        header = new JPanel( );
        header.setSize(720, 48);
        header.setMinimumSize(new Dimension(720, 48));
        header.setMaximumSize(new Dimension(720, 48));
        header.setPreferredSize(new Dimension(720, 48));

        header.setLayout(headerLayout);

        headerLayout.minimumLayoutSize(header);
        headerLayout.maximumLayoutSize(header);
        headerLayout.preferredLayoutSize(header);

        header.add(first);
        header.add(title);

        headerLayout.putConstraint(SpringLayout.WEST, first, 8, SpringLayout.WEST, header);
        headerLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, header);
        headerLayout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 0, SpringLayout.VERTICAL_CENTER, header);
        headerLayout.putConstraint(SpringLayout.VERTICAL_CENTER, first, 0, SpringLayout.VERTICAL_CENTER, header);


        container.add(header, BorderLayout.NORTH);

        //Parte central de la pantalla

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        DataPanel dataPanel = new DataPanel(leftContent, splitPane);
        splitPane.setLeftComponent(dataPanel);


        DataPanel dataPanell = new DataPanel(rightContent, splitPane);
        splitPane.setRightComponent(dataPanell);



        splitPane.setDividerLocation(460);
        splitPane.setEnabled(true);

        container.add(splitPane, BorderLayout.CENTER);


        updateUI(container);




    }

    public File getFile() throws FileNotFoundException, ProcessCanceledException, Exception{
        JFileChooser chooser = new JFileChooser();

        int retVal = chooser.showOpenDialog(this);
        if ( retVal == JFileChooser.APPROVE_OPTION ) {
            File file = chooser.getSelectedFile();
            if(file.getName().endsWith(".asm")){
                return file;
            } else {
                JOptionPane.showMessageDialog(this, "El archivo seleccionado no es un archivo .asm", "Error", JOptionPane.ERROR_MESSAGE);
                return getFile();

            }
        } else if( retVal == JFileChooser.CANCEL_OPTION ){
            throw new ProcessCanceledException( "El usuario canceló el proceso" );
        } else if (retVal == JFileChooser.ERROR_OPTION) {
            throw  new Exception("Error al seleccionar el archivo");
        }
        return  null;
    }
    public void updateUI(Container container) {
        container.validate();
        container.repaint();
    }
}
