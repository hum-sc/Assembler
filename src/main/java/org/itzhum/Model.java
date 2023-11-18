package org.itzhum;

import org.itzhum.types.ComponentType;
import org.itzhum.types.OperandType;
import org.itzhum.types.SizeType;
import org.itzhum.types.SymbolType;

import java.io.File;
import java.util.*;

public class Model {
    public File file;
    private int numberOfLines;
    public Scanner scanner;
    public HashMap<String, Instruction> instructions;

    public HashMap<String, String> pseudoInstructions;
    public HashMap<String, String> registersComplete;
    public  HashMap<String, String> registersHalf;
    public HashMap<String, String> labels;

    public LinkedHashMap<String,Symbol> symbols;

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

        symbols = new LinkedHashMap<>();

        numberOfLines = 0;

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
                boolean isNoOperands = parts[1].equals("1");
                boolean isOneOperand = parts[2].equals("1");
                boolean isTwoOperands = parts[3].equals("1");
                boolean isByte = parts[14].equals("1");
                boolean isWord = parts[15].equals("1");
                boolean isByteByte = parts[16].equals("1");
                boolean isWordByte = parts[17].equals("1");
                boolean isWordWord = parts[18].equals("1");
                Instruction instruction = new Instruction(parts[0].toUpperCase(),isNoOperands,isOneOperand,isTwoOperands, isByte, isWord, isByteByte, isWordByte, isWordWord);
                if(isOneOperand){
                    if(parts[4].equals("1")){
                        instruction.addOperandAccepted(OperandType.INMEDIATE);
                    }
                    if(parts[5].equals("1")){
                        instruction.addOperandAccepted(OperandType.MEMORY);
                    }
                    if(parts[6].equals("1")){
                        instruction.addOperandAccepted(OperandType.TAG);
                    }
                    if(parts[7].equals("1")){
                        instruction.addOperandAccepted(OperandType.REGISTER);
                    }
                }
                if(isTwoOperands){
                    if (parts[8].equals("1")){
                        instruction.addPairOperandAccepted(OperandType.REGISTER, OperandType.MEMORY);
                    }
                    if (parts[9].equals("1")){
                        instruction.addPairOperandAccepted(OperandType.REGISTER, OperandType.REGISTER);
                    }
                    if (parts[10].equals("1")){
                        instruction.addPairOperandAccepted(OperandType.REGISTER, OperandType.INMEDIATE);
                    }
                    if (parts[11].equals("1")){
                        instruction.addPairOperandAccepted(OperandType.MEMORY, OperandType.REGISTER);
                    }
                    if (parts[12].equals("1")){
                        instruction.addPairOperandAccepted(OperandType.MEMORY, OperandType.INMEDIATE);
                    }
                    if (parts[13].equals("1")){
                        instruction.addPairOperandAccepted(OperandType.MEMORY, OperandType.MEMORY);
                    }
                }
                instructions.put(parts[0].toUpperCase(), instruction);
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
            e.printStackTrace();
            System.out.println(e.toString());
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

    public int getNumberOfLines(){
        return numberOfLines;
    }

    public String getFile(){
        String fileString = "";
        int lineNumber = 0;
        try {
            resetScanner();
            while (scanner.hasNextLine()){
               fileString = fileString.concat(getNextLine());
               if(errors.containsKey(lineNumber)){
                   fileString = fileString.concat(" Error: "+errors.get(lineNumber));
               }
                fileString = fileString.concat("\n");
               lineNumber++;
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        numberOfLines = lineNumber;

        return fileString;
    }

    public boolean findSymbol(String name){
        return symbols.containsKey(name);
    }
    public void addSymbol(String name, SymbolType type, String value, SizeType size, int direction) throws Exception{
        if(symbols.containsKey(name)){
            throw new Exception("El simbolo "+name+" ya existe");
        }
        Symbol<String> symbol = new Symbol<>(name, type, value, size, direction);

        symbols.put(name, symbol);
    }

    public void addSymbol(String name, SymbolType type, int value, SizeType size, int direction) throws Exception{
        if(findSymbol(name)){
            throw new Exception("El simbolo "+name+" ya existe");
        }
        Symbol<Integer> symbol = new Symbol<>(name, type, value, size, direction);

        symbols.put(name,symbol);
    }
    public void addSymbol(String name, SymbolType type, ArrayList value, SizeType size, int direction) throws Exception{
        if(findSymbol(name)){
            throw new Exception("El simbolo "+name+" ya existe");
        }
        Symbol<ArrayList> symbol = new Symbol<>(name, type, value, size, direction);

        symbols.put(name,symbol);
    }
    public String[] getComponentList(){
        //TODO: Que todo quede alineado
        String[] list = {"",""};
        String spaces = "";
        int componentLength = 0;
        for (AssemblerComponent component: components) {
            componentLength = component.name.length();
            spaces = " ".repeat((maxLineLength - componentLength));

            list[0] = list[0].concat(component.name+"\n");
            list[1] = list[1].concat(component.type.toString()+"\n");

        }
        return list;
    }

    public Instruction getInstruction(String component) {
        return instructions.get(component);
    }

    public Symbol getSymbol(String component) {
        return symbols.get(component);
    }

    public String getErrors() {
        String errorsString = "";
        for (Integer lineNumber: errors.keySet().stream().sorted().toArray(Integer[]::new)) {
            errorsString = errorsString.concat("Error en la linea "+(lineNumber+1)+": "+errors.get(lineNumber)+"\n\n");
        }
        return errorsString;
    }

    public int[] getErrorLines() {
        int[] errorLines = new int[errors.size()];
        int i = 0;
        for (Integer lineNumber: errors.keySet().stream().sorted().toArray(Integer[]::new)) {
            errorLines[i] = lineNumber;
            i++;
        }
        return errorLines;
    }

    public Object[][] getSymbolData() {
        Object[][] data = new Object[symbols.size()][5];
        int i = 0;
        for (Symbol symbol: symbols.values()) {

            data[i][0] = symbol.getName();
            data[i][1] = symbol.getType();
            data[i][2] = symbol.getValue();
            data[i][3] = symbol.getSize();
            data[i][4] = "";//"0"+Integer.toHexString(symbol.getDirection())+"H";
            i++;
        }
        return data;
    }
}
