package org.itzhum;

import org.itzhum.types.ComponentType;
import org.itzhum.types.SizeType;
import org.itzhum.types.SymbolType;

import java.awt.event.WindowStateListener;
import java.io.File;
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

    public List<Symbol> symbols;

    public List<AssemblerComponent> components;

    public HashMap<Integer, String> errors;
    private int maxLineLength = 0;

    public Model() {
        pseudoInstructions = new HashMap<>();
        instructions = new HashMap<>();
        registersComplete = new HashMap<>();
        registersHalf = new HashMap<>();
        components = new ArrayList<>();
        errors = new HashMap<>();

        symbols = new ArrayList<>();

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
            System.out.println(scanner.hasNext());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void addComponent(String component, ComponentType type){
        if (component.length() > maxLineLength) maxLineLength = component.length();
        components.add(new AssemblerComponent(component.replace("\t", ""), type));
    }
    public void setError(Integer lineNumber, String error){
        errors.put(lineNumber, error);
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
        } else {
            return null;
        }
    }

    public void resetScanner(){
        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getFile(){
        String fileString = "";
        int lineNumber = 0;
        try {
            resetScanner();
            while (scanner.hasNextLine()){
               fileString = fileString.concat(lineNumber+":\t"+getNextLine());
               if(errors.containsKey(lineNumber)){
                   fileString = fileString.concat("Error: "+errors.get(lineNumber)+"\n");
               }
                fileString = fileString.concat("\n");
               lineNumber++;
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return fileString;
    }

    public boolean findSymbol(String goal){
        for (Symbol symbol: symbols) {
            if (symbol.getName().equals(goal)){
                return true;
            }
        }
        return false;
    }
    public void addSymbol(String name, SymbolType type, String value, SizeType size, int direction) throws Exception{
        if(findSymbol(name)){
            throw new Exception("El simbolo "+name+" ya existe");
        }
        Symbol<String> symbol = new Symbol<>(name, type, value, size, direction);

        symbols.add(symbol);
    }

    public void addSymbol(String name, SymbolType type, int value, SizeType size, int direction) throws Exception{
        if(findSymbol(name)){
            throw new Exception("El simbolo "+name+" ya existe");
        }
        Symbol<Integer> symbol = new Symbol<>(name, type, value, size, direction);

        symbols.add(symbol);
    }
    public void addSymbol(String name, SymbolType type, ArrayList value, SizeType size, int direction) throws Exception{
        if(findSymbol(name)){
            throw new Exception("El simbolo "+name+" ya existe");
        }
        Symbol<ArrayList> symbol = new Symbol<>(name, type, value, size, direction);

        symbols.add(symbol);
    }
    public String getComponentList(){
        //TODO: Que todo quede alineado
        String list = "";
        String spaces = "";
        int componentLength = 0;
        for (AssemblerComponent component: components) {
            componentLength = component.name.length();
            spaces = " ".repeat((maxLineLength - componentLength));
            list = list.concat(component.name+spaces+"\t"+component.type+ "\n");
        }
        return list;
    }
}
