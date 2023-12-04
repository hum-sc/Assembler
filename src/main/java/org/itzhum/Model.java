package org.itzhum;

import org.itzhum.logic.Identifier;
import org.itzhum.logic.Instruction;
import org.itzhum.types.*;

import java.io.File;
import java.math.BigInteger;
import java.util.*;

public class Model {
    public File file;
    private int numberOfLines;
    public Scanner scanner;
    public final HashMap<String, Instruction> instructions;

    public final HashMap<String, String> pseudoInstructions;
    public final HashMap<String, String> registersComplete;
    public final HashMap<String, String> registersHalf;
    public final HashMap<String, String> segmentRegister;

    public final LinkedHashMap<String, Symbol> symbols;

    public final List<AssemblerComponent> components;

    public final HashMap<Integer, String> errors;

    private String[] counterProgram;
    private String[] hexMachineCode;
    private String[] binMachineCode;
    private int maxLineLength = 0;
    public int maxCPLength = 0;
    public int maxCODELength = 0;

    public Model() {
        pseudoInstructions = new HashMap<>();
        instructions = new HashMap<>();
        registersComplete = new HashMap<>();
        registersHalf = new HashMap<>();
        segmentRegister = new HashMap<>();

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
                boolean isByte = parts[17].equals("1");
                boolean isWord = parts[18].equals("1");
                boolean isByteByte = parts[19].equals("1");
                boolean isWordByte = parts[20].equals("1");
                boolean isWordWord = parts[21].equals("1");

                Instruction instruction = new Instruction(parts[0].toUpperCase(),isNoOperands,isOneOperand,isTwoOperands, isByte, isWord, isByteByte, isWordByte, isWordWord);
                Code lcode;
                if(isNoOperands){
                    instruction.addCode(parts[22]);
                }
                if(isOneOperand){
                    if(parts[4].equals("1")){
                        lcode =  new Code(parts[23],parts[28],parts[33], parts[38]);
                        instruction.addOperandAccepted(OperandType.INMEDIATE, lcode);
                    }
                    if(parts[5].equals("1")){
                        lcode = new Code(parts[24],parts[29],parts[34], parts[39]);
                        instruction.addOperandAccepted(OperandType.MEMORY, lcode);

                    }
                    if(parts[6].equals("1")){

                        lcode = new Code(parts[25],parts[30],parts[35], parts[40]);
                        instruction.addOperandAccepted(OperandType.TAG, lcode);
                    }
                    if(parts[7].equals("1")){
                        lcode = new Code(parts[26],parts[31],parts[36], parts[41]);
                        instruction.addOperandAccepted(OperandType.REGISTER, lcode);
                    }
                    if(parts[8].equals("1")){
                        lcode = new Code(parts[27],parts[32],parts[37], parts[42]);
                        instruction.addOperandAccepted(OperandType.SEGMENTREGISTER, lcode);
                    }
                }
                //System.out.println(parts[0]+" "+isTwoOperands);
                try {
                    if (isTwoOperands) {
                        if (parts[9].equals("1")) {
                            lcode = new Code(parts[43], parts[51], parts[59], parts[67]);
                            instruction.addPairOperandAccepted(OperandType.REGISTER, OperandType.MEMORY, lcode);
                        }
                        if (parts[10].equals("1")) {
                            lcode = new Code(parts[44], parts[52], parts[60], parts[68]);
                            instruction.addPairOperandAccepted(OperandType.REGISTER, OperandType.REGISTER, lcode);
                        }
                        if (parts[11].equals("1")) {
                            lcode = new Code(parts[45], parts[53], parts[61], parts[69]);
                            instruction.addPairOperandAccepted(OperandType.REGISTER, OperandType.INMEDIATE, lcode);
                        }
                        if (parts[12].equals("1")) {
                            lcode = new Code(parts[46], parts[54], parts[62], parts[70]);
                            instruction.addPairOperandAccepted(OperandType.MEMORY, OperandType.REGISTER, lcode);
                        }
                        if (parts[13].equals("1")) {
                            lcode = new Code(parts[47], parts[55], parts[63], parts[71]);
                            instruction.addPairOperandAccepted(OperandType.MEMORY, OperandType.INMEDIATE, lcode);
                        }
                        if (parts[14].equals("1")) {
                            lcode = new Code(parts[48], parts[56], parts[64], parts[72]);
                            instruction.addPairOperandAccepted(OperandType.MEMORY, OperandType.MEMORY, lcode);
                        }
                        if (parts[15].equals("1")) {
                            lcode = new Code(parts[49], parts[57], parts[65], parts[73]);
                            instruction.addPairOperandAccepted(OperandType.SEGMENTREGISTER, OperandType.REGISTER, lcode);
                        }
                        if (parts[16].equals("1")) {
                            lcode = new Code(parts[50], parts[58], parts[66], parts[74]);
                            instruction.addPairOperandAccepted(OperandType.REGISTER, OperandType.SEGMENTREGISTER, lcode);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println(parts[0]);
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
            configFile = new File(pathBase+"\\src\\main\\settings\\regs.cfg");
            scanner = new Scanner(configFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                segmentRegister.put(parts[0].toUpperCase(), null);
            }
            
            
        } catch (Exception e) {
            //TODO: handle exception
        }


    }

    public void setFile(File file){
        this.file = file;
        try {
            this.scanner = new Scanner(file);
            int counter = 0;
            while (scanner.hasNext()){
                counter++;
               scanner.nextLine();
            }
            counterProgram = new String[counter+10];
            hexMachineCode = new String[counter+10];
            binMachineCode = new String[counter+10];
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void addComponent(String component, ComponentType type){
        if (component.length() > maxLineLength) maxLineLength = component.length();
        components.add(new AssemblerComponent(component.replace("\t", ""), type));
    }
    public void setCounterProgram(int lineNumber, int counter){
        counterProgram[lineNumber] ="0"+Integer.toHexString(counter).toUpperCase()+"H";
        if (counterProgram[lineNumber].length() > maxCPLength) maxCPLength = counterProgram[lineNumber].length();
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
                   fileString = fileString.concat("\u001B[1;31m Error: "+errors.get(lineNumber)+"\u001b[0m");
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

        for (AssemblerComponent component: components) {

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
            data[i][4] =symbol.getDirection() != null ? "0"+Integer.toHexString(symbol.getDirection()).toUpperCase()+"H":"";
            i++;
        }
        return data;
    }

    public String[] getCounterProgram() {
        String[] lcounterProgram = new String[this.counterProgram.length];
        for (int i = 0; i < this.counterProgram.length; i++) {
            if(counterProgram[i] == null) lcounterProgram[i] = "";
            else if(counterProgram[i].length() < maxCPLength) lcounterProgram[i] = "0".repeat(maxCPLength-counterProgram[i].length())+counterProgram[i];
            else lcounterProgram[i] = counterProgram[i];
        }
        return lcounterProgram;
    }

    public void addSymbol(String name, SymbolType type, int value, SizeType size) throws Exception {
        if(findSymbol(name)){
            throw new Exception("El simbolo "+name+" ya existe");
        }
        Symbol<Integer> symbol = new Symbol<>(name, type, value, size);

        symbols.put(name,symbol);
    }

    String[] getNextOperand(String line) throws Exception {
        boolean possibleCommentDoubleQuote;
        boolean possibleCommentSingleQuote;
        boolean isCompounded;
        int characterCounter = 0;
        possibleCommentDoubleQuote = false;
        possibleCommentSingleQuote = false;
        isCompounded = false;
        StringBuilder component = new StringBuilder();

        if(line.isEmpty() || line.isBlank()) return new String[]{"", ""};
        for(char c: line.toCharArray()) {
            if (c == ';') break;

            if ((component.toString().equals("DUP(") || component.toString().equals("[")) && !isCompounded) {
                isCompounded = true;
            }
            if ((c == ')' || c == ']') && isCompounded) {
                isCompounded = false;
            }

            if (!isCompounded) {
                if (c == '"' && !possibleCommentSingleQuote) {
                    //if (!possibleCommentDoubleQuote && (!component.isEmpty())) break;

                    possibleCommentDoubleQuote = !possibleCommentDoubleQuote;

                    if (!possibleCommentDoubleQuote) {
                        component.append(c);
                        characterCounter++;
                        break;
                    }
                }

                if (c == '\'' && !possibleCommentDoubleQuote) {
                    //if (!possibleCommentSingleQuote && (!component.isEmpty()))break;


                    possibleCommentSingleQuote = !possibleCommentSingleQuote;
                    if (!possibleCommentSingleQuote) {
                        component.append(c);
                        characterCounter++;
                        break;
                    }
                }
                if (Identifier.isSeparator(c) && !possibleCommentDoubleQuote && !possibleCommentSingleQuote) {
                    //characterCounter++;
                    if(component.isEmpty() && c != ','){
                        characterCounter++;
                        continue;
                    }
                    if (c == ','){
                        characterCounter++;
                        break;
                    }
                }
            }
            component.append(c);
            characterCounter ++;
        }

        for(char c: component.toString().toCharArray()){
            if(c == ' ') component.deleteCharAt(0);
            else break;
        }

        for(int i = component.length()-1; i >= 0; i--){
            if(component.charAt(i) == ' ') component.deleteCharAt(i);
            else break;
        }

        if((!component.isEmpty()) && !possibleCommentDoubleQuote && !possibleCommentSingleQuote && !isCompounded){
            return new String[]{component.toString(),line.substring(characterCounter)};
        } else if (possibleCommentDoubleQuote || possibleCommentSingleQuote || isCompounded){
            throw new Exception("Falta el simbolo de cerrado", new Throwable(ComponentType.CaracterConstante.toString()));
        }
        return new String[]{"",""};
    }

    public String[] getHexMachineCode() {
        String[] lHexMachineCode = new String[hexMachineCode.length];
        for (int i = 0; i < hexMachineCode.length; i++) {
            if(hexMachineCode[i] == null) lHexMachineCode[i] = "";
            else if(hexMachineCode[i].length() < maxCODELength) lHexMachineCode[i] = "0".repeat(maxCODELength-hexMachineCode[i].length())+hexMachineCode[i];
            else lHexMachineCode[i] = hexMachineCode[i];
        }
        return hexMachineCode;
    }

    /**
     *
     * @param lineCoutner Linea en la que se encuentra el codigo
     * @param encode Codigo en binario
     */
    public void addMachineCode(int lineCoutner, String encode) {

        hexMachineCode[lineCoutner] = Integer.toHexString(Integer.parseUnsignedInt(encode, 2)).toUpperCase();
        if(hexMachineCode[lineCoutner].length() %2 != 0) hexMachineCode[lineCoutner] = "0"+hexMachineCode[lineCoutner];
        hexMachineCode[lineCoutner] = "0"+hexMachineCode[lineCoutner]+"H";
        if (hexMachineCode[lineCoutner].length() > maxCODELength) maxCODELength = hexMachineCode[lineCoutner].length();
        binMachineCode[lineCoutner] = encode;
    }

    public String[] getBinMachineCode() {
        return binMachineCode;
    }

    public void clearData() {
        components.clear();
        errors.clear();
        symbols.clear();
        counterProgram = new String[numberOfLines+10];
        hexMachineCode = new String[numberOfLines+10];
        binMachineCode = new String[numberOfLines+10];
        maxLineLength = 0;
    }
}
