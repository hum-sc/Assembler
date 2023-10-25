package org.itzhum;
import org.itzhum.exceptions.FileIsEndedException;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

public class Model {
    public File file;
    public Scanner scanner;
    public HashMap<String, String> instructions;

    public HashMap<String, String> pseudoInstructions;
    public HashMap<String, String> registers;
    public HashMap<String, String> labels;

    public  Model() {
        pseudoInstructions = new HashMap<>();
        String pathBase = new File("").getAbsolutePath();

        File pseudoInstructionsFile = new File(pathBase+"\\src\\main\\settings\\pseudoInstructions.cfg");

        try {
            Scanner scanner = new Scanner(pseudoInstructionsFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                pseudoInstructions.put(parts[0].toUpperCase(), null);
                System.out.println(pseudoInstructions.toString());
            }
            System.out.println("Termino el constructor");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }

    public void setFile(File file){
        this.file = file;
        try {
            this.scanner = new Scanner(file);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public String getNextLine() throws FileIsEndedException{
        if (scanner.hasNextLine()){
            return scanner.nextLine().toUpperCase();
        } else throw new FileIsEndedException("No more lines");
    }

    public boolean isIntruction(String line){
        return instructions.containsKey(line);
    }
    public boolean isPseudoInstruction(String line){
        return pseudoInstructions.containsKey(line);
    }

}
