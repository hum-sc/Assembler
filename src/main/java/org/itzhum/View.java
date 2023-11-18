package org.itzhum;

import org.itzhum.exceptions.ProcessCanceledException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.io.File;
import java.io.FileNotFoundException;


public class View extends JFrame {

    private JSplitPane verticalSplit;
    private JSplitPane northSplit;
    private JSplitPane southSplit;
    public View (){
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setName("Ensamblador");
        setMinimumSize(new Dimension(760,480));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        this.setVisible(true);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                onResize(e);
                super.componentResized(e);
            }
        });
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

    public void showAssembledPage(ActionListener listener, String code, String[] componentList, String errors, int[] errorLines, Object[][] symbolDataTable){
        JPanel header;
        JButton first;
        JLabel title;

        SpringLayout headerLayout;

        JSplitPane splitPane;



        Container container = this.getContentPane();
        container.removeAll();

        System.out.println(this.getHeight()+","+this.getWidth());
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

        verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        northSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        CodePanel codePanel = new CodePanel(code);
        codePanel.setErrorLines(errorLines);
        ComponentPanel componentPanel = new ComponentPanel(componentList);
        DataPanel errorPanel = new DataPanel(errors, northSplit);

        TablePanel tablePanel = new TablePanel(symbolDataTable, new Object[]{"Simbolo","Tipo", "Valor", "Tama\u00f1o", "Direccion"});

        northSplit.setLeftComponent(codePanel);
        northSplit.setRightComponent(componentPanel);

        southSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
/*
        southSplit.setLeftComponent(errorPanel);
        southSplit.setRightComponent(tablePanel);
        southSplit.setDividerLocation((getWidth()/3)*2);
        verticalSplit.add(southSplit);
*/
        northSplit.setDividerLocation((getWidth()/3)*2);

        verticalSplit.add(northSplit);
        verticalSplit.add(tablePanel);

        verticalSplit.setDividerLocation((getHeight()/5)*3);

        container.add(verticalSplit, BorderLayout.CENTER);


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
                showErrorMessage("El archivo seleccionado no es un archivo .asm");
                return getFile();

            }
        } else if( retVal == JFileChooser.CANCEL_OPTION ){
            throw new ProcessCanceledException( "El usuario canceló el proceso" );
        } else if (retVal == JFileChooser.ERROR_OPTION) {
            throw  new Exception("Error al seleccionar el archivo");
        }
        return  null;
    }
    public void showErrorMessage(String message){
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void updateUI(Container container) {
        container.validate();
        container.repaint();
    }

    public void onResize(ComponentEvent e){
        Dimension size = e.getComponent().getSize();
        System.out.println(size.width+","+size.height);
        verticalSplit.setDividerLocation((getHeight()/5)*3);
        northSplit.setDividerLocation((getWidth()/3)*2);
        southSplit.setDividerLocation((getWidth()/3)*2);
    }
}
