package org.itzhum;

import javax.swing.*;

public class TablePanel extends JPanel {

    JTable table;
    public TablePanel(Object[][] data, Object[] columnNames){
        super();
        table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);

        // Table ocupies all the space
        //table.setPreferredScrollableViewportSize(table.getPreferredSize());
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setPreferredSize(getSize());

        // Table shouldn't be editable
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);


        add(scrollPane);
    }
}
