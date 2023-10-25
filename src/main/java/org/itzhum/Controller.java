package org.itzhum;

import org.itzhum.exceptions.FileIsEndedException;
import org.itzhum.exceptions.ProcessCanceledException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Controller implements ActionListener {
    View view;
    Model model;

    public Controller(){
        super();
    }

    public Controller(View view, Model model){
        super();
        this.view = view;
        this.model = model;
    }

    public void start(){
        view.showMainPage(this, "No se ha seleccionado ningun archivo");
    }

    public void separateLines(){
        boolean isFinished = false;
        while(!isFinished) {
            try {
                String line = model.getNextLine();
                if(model.isPseudoInstruction(line)){
                }
                System.out.println(line);
            } catch (FileIsEndedException e) {
                isFinished = true;
            }
        }
        System.out.println("end separareLines()");

    }

    private void goToAssembledPage() {

        view.showAssembledPage(this,"","");
        separateLines();

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "selectFile":
                try {
                    model.setFile(view.getFile());
                    view.showMainPage(this, model.file.getName());

                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (ProcessCanceledException ex) {
                    System.out.println("Process canceled");
                } catch ( Exception ex ){
                    System.out.println(ex.getMessage());
                }

                break;
            case "assembledPage":
                goToAssembledPage();
                break;
            case "firstPage":
                view.showMainPage(this, model.file != null ? model.file.getName():"No se ha seleccionado ningun archivo");
                break;
            default:
                System.out.println("default");
                break;
        }
    }


}
