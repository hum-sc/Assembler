package org.itzhum;
import org.itzhum.exceptions.FileIsEndedException;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Model {
    public File file;
    public Scanner scanner;
    public HashMap<String, String> instructions;

    public HashMap<String, String> pseudoInstructions;
    public HashMap<String, String> registersComplete;
    public  HashMap<String, String> registersHalf;
    public HashMap<String, String> labels;

    public List<AssemblerComponent> components;

    static String pseudoInstruction = "PesudoInstruccion";
    static String instruction = "Instruccion";
    static String registerComplete = "Registro";
    static String registerHalf = "Registro";
    static String tag = "Etiqueta";
    static String caracterConstant = "Caracter constante";
    static String byteDecimalConstant = "Constante decimal";
    static String byteHexadecimalConstant = "Constante hexadecimal";
    static String byteBinaryConstant = "Constante binaria";
    static String wordDecimalConstant = "Constante decimal";
    static String wordHexadecimalConstant = "Constante hexadecimal";
    static String wordBinaryConstant = "Constante binaria";
    static String unknown = "Desconocido";

    static String symbol = "Simbolo";

    public Model() {
        pseudoInstructions = new HashMap<>();
        instructions = new HashMap<>();
        registersComplete = new HashMap<>();
        registersHalf = new HashMap<>();
        components = new ArrayList<>();

        String pathBase = new File("").getAbsolutePath();

        File configFile = new File(pathBase+"\\src\\main\\settings\\pseudoInstructions.cfg");

        try {
            Scanner scanner = new Scanner(configFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                pseudoInstructions.put(parts[0].toUpperCase(), null);
            }

            configFile = new File(pathBase+"\\src\\main\\settings\\instructions.cfg");
            scanner = new Scanner(configFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                instructions.put(parts[0].toUpperCase(), null);
            }

            configFile = new File(pathBase+"\\src\\main\\settings\\registersComplete.cfg");
            scanner = new Scanner(configFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                registersComplete.put(parts[0].toUpperCase(), null);
            }

            configFile = new File(pathBase+"\\src\\main\\settings\\registersHalf.cfg");
            scanner = new Scanner(configFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                registersHalf.put(parts[0].toUpperCase(), null);
            }
            
            
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

    public void setComponent(String component, String type){

      components.add(new AssemblerComponent(component.replace("\t", ""), type));
    }
    public String getNextLine(){
        String line;
        if (scanner.hasNextLine()){
            line =  scanner.nextLine().toUpperCase();

            while (line.startsWith(" ")||line.startsWith("\t")){
                line = line.substring(1);
            }
            while (line.endsWith(" ")||line.endsWith("\t")){
                line = line.substring(0, line.length()-1);
            }
            return line;
        } else return null;
    }

    public String getFile(){
        String fileString = "";
        int lineNumber = 0;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
               fileString = fileString.concat(lineNumber+":\t"+getNextLine()+"\n");
               lineNumber++;
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return fileString;
    }

    public String getComponentList(){
        //TODO: Que todo quede alineado
        String list = "";
        for (AssemblerComponent component: components) {
            list = list.concat(component.name + "\t" + component.type+ "\n");
        }
        return list;
    }
}
