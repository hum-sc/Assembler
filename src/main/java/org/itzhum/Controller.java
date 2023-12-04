package org.itzhum;
import org.itzhum.exceptions.ProcessCanceledException;
import org.itzhum.logic.Identifier;
import org.itzhum.logic.Instruction;
import org.itzhum.types.*;
import org.itzhum.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Controller implements ActionListener {
    final Identifier identifier;
    View view;
    static Model model;
    static int counterProgram;
    static int conunterProgramInitial = 0;

    public Controller(View view, Model model){
        super();
        this.view = view;
        this.model = model;

        identifier = new Identifier(model);
        model.setFile(new File("C:\\Users\\hum-s\\Documents\\aprendiendoCodificar.asm"));
    }

    public static int componentToNumber(String component) throws  Exception{
        if (Identifier.isByteDecimalConstant(component) || Identifier.isWordDecimalConstant(component)){
            return Integer.parseInt(component.replaceAll("[Dd]", ""));
        }
        if (Identifier.isByteHexadecimalConstant(component) || Identifier.isWordHexadecimalConstant(component)){
            return Integer.parseInt(component.replaceAll("[Hh]", ""), 16);
        }
        if (Identifier.isByteBinaryConstant(component) || Identifier.isWordBinaryConstant(component)){
            return Integer.parseInt(component.replaceAll("[Bb]", ""), 2);
        }
        throw new Exception("No es una constante numerica");
    }

    public static int getCounterProgram() {
        return counterProgram;
    }

    public String[] separateNotClosedCompounded(String component){
        String[] parts = component.split("[ ,:]");

        component = component.replaceFirst(parts[0], "");
        return new String[]{parts[0], component};
    }

    public ArrayList arrayByte(String component, int length) throws Exception{
        int size;
        if(component.startsWith("DUP(")){
            if(!component.endsWith(")")) throw new Exception("Falta el simbolo de cerrado");
            component = component.substring(4, component.length()-1);
            if(identifier.isByteNumberConstant(component)){
                int number = componentToNumber(component);
                ArrayList <Integer> array = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    array.add(number);
                }
                return array;
            } else if (identifier.isCaracterConstant(component)){
                ArrayList<String> array = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    array.add(component.substring(1,component.length()-1));
                }
                return array;
            } else throw new Exception("Se esperaba una constante númerica de tipo byte o caracter cómo parametro de DUP");
        } else throw new Exception("Se esperaba la directiva DUP");
    }

    public ArrayList<Integer> arrayWord(String component, int elementNumber) throws Exception{
        int size;
        if(component.startsWith("DUP(")){
            if(!component.endsWith(")")) throw new Exception("Falta el simbolo de cerrado");
            component = component.substring(4, component.length()-1);

            if(!identifier.isWordNumberConstant(component)){
                throw new Exception("Se necesita una constante númerica de tipo palabra cómo parametro de DUP");
            }
            //TODO: Llenar la tabla

            int number = componentToNumber(component);
            ArrayList <Integer> array = new ArrayList<>();
            for (int i = 0; i < elementNumber; i++) {
                array.add(number);
            }
            return array;
        } else throw new Exception("Se necesita la directiva DUP");
    }

    public void analysisSyntacticSemantic(){
        model.resetScanner();

        String line = model.getNextLine(), component = "";

        int lineCoutner = 0;
        counterProgram = conunterProgramInitial;

        boolean inStackSegment = false;
        boolean inDataSegment = false;
        boolean inCodeSegment = false;

        while (line != null ) {
            component = "";
            model.setCounterProgram(lineCoutner, counterProgram);
            if (!line.isEmpty() && !line.isBlank() && !identifier.isComment(line)) try{
                if(identifier.isPseudoInstruction(line)){
                    model.addComponent(line, ComponentType.PseudoInstruccion);
                    if(line.equals("ENDS")){
                        if(inStackSegment) inStackSegment = false;
                        else if(inDataSegment) inDataSegment = false;
                        else if(inCodeSegment) inCodeSegment = false;
                        else throw new Exception("Se trata de cerrar un segmento sin haberlo abierto");
                        line = model.getNextLine();
                        lineCoutner++;
                        continue;

                    }

                    else if(!inStackSegment && !inDataSegment && !inCodeSegment){
                            if (line.equals(".STACK SEGMENT")){
                                inStackSegment = true;
                                counterProgram = conunterProgramInitial;
                            }
                            if (line.equals(".DATA SEGMENT")){
                                inDataSegment = true;
                                counterProgram = conunterProgramInitial;
                            }
                            if(line.equals(".CODE SEGMENT")){
                                inCodeSegment = true;
                                counterProgram = conunterProgramInitial;

                            }
                            line = model.getNextLine();
                            lineCoutner++;
                            continue;
                        }

                    else if (line.equals(".STACK SEGMENT") ||
                                line.equals(".DATA SEGMENT") ||
                                line.equals(".CODE SEGMENT")){
                            throw new Exception("Se trata de declarar el segmento CODE sin haber cerrado el segmento " +
                                    "anterior");
                        }
                }

                if (inStackSegment) {
                    String[] result;

                    result = getNextComponent(line);
                    line = result[1];
                    component = result[0];

                    if(!component.equals("DW")) throw new Exception("No se indica el tipo de dato o tipo de dato incorrecto");

                    model.addComponent(component, ComponentType.PseudoInstruccion);

                    result = getNextComponent(line);
                    line = result[1];
                    component = result[0];

                    if (!identifier.isWordNumberConstant(component))throw new Exception("Se necesita una constante númerica");

                    ComponentType type;

                    if(identifier.isWordHexadecimalConstant(component)) type = ComponentType.ConstanteHexadecimalWord;
                    else if(identifier.isWordBinaryConstant(component)) type = ComponentType.ConstanteBinariaWord;
                    else type = ComponentType.ConstanteDecimalWord;

                    model.addComponent(component, type);

                    int length = componentToNumber(component);

                    result = getNextComponent(line);
                    line = result[1];
                    component = result[0];

                    //DUP
                    ArrayList<Integer> list = arrayWord(component, length);

                    model.addComponent(component, ComponentType.PseudoInstruccion);


                    if(!line.isEmpty() && !identifier.isComment(line)){
                        throw new Exception("Se esperaba el fin de la linea");
                    }
                    counterProgram += list.size()*2;
                }
                if (inDataSegment){
                    String[] result;
                    result = getNextComponent(line);
                    line = result[1];
                    component = result[0];

                    if(!identifier.isSymbol(component)) throw new Exception("Se esperaba un simbolo adecuado");
                    String symbol = component;
                    model.addComponent(component, ComponentType.Simbolo);

                    result = getNextComponent(line);
                    line = result[1];
                    component = result[0];

                    switch (component) {
                        case "DB" -> {
                            model.addComponent(component, ComponentType.PseudoInstruccion);
                            result = getNextComponent(line);
                            line = result[1];
                            component = result[0];
                            if (identifier.isByteNumberConstant(component)) {
                                model.addComponent(component, ComponentType.ConstanteDecimalByte);
                                int number = componentToNumber(component);
                                if (line.isEmpty() || identifier.isComment(line)) {
                                    model.addSymbol(symbol, SymbolType.Variable, number, SizeType.Byte, counterProgram);
                                    counterProgram += 1;
                                } else {
                                    result = getNextComponent(line);
                                    line = result[1];
                                    component = result[0];

                                    ArrayList list = arrayByte(component, number);
                                    model.addComponent(component, ComponentType.PseudoInstruccion);

                                    if (!line.isEmpty() && !identifier.isComment(line)) {
                                        throw new Exception("Se esperaba el fin de la linea");
                                    }

                                    model.addSymbol(symbol, SymbolType.Variable, list, SizeType.Byte, counterProgram);

                                    counterProgram += Identifier.isArrayString(list) ? (list.get(0).toString().length())*list.size() : list.size();

                                }
                            } else if (identifier.isWordNumberConstant(component)) {
                                if (line.isEmpty()) throw new Exception("Valor palabra en variable byte");

                                model.addComponent(component, ComponentType.ConstanteDecimalWord);

                                result = getNextComponent(line);
                                line = result[1];
                                component = result[0];

                                ArrayList list = arrayByte(component, componentToNumber(component));

                                model.addComponent(component, ComponentType.PseudoInstruccion);

                                if (!line.isEmpty() || !identifier.isComment(line)) {
                                    throw new Exception("Se esperaba el fin de la linea");
                                }

                                if(Identifier.isArrayString(list)){
                                    System.out.println("Es un arreglo de string");

                                }
                                counterProgram += Identifier.isArrayString(list) ? (list.get(0).toString().length())*list.size() : list.size();
                            } else if (identifier.isCaracterConstant(component)) {
                                if (line.isEmpty() || identifier.isComment(line)) {
                                    model.addComponent(component, ComponentType.CaracterConstante);
                                    model.addSymbol(symbol, SymbolType.Variable, component.substring(1,component.length()-1), SizeType.Byte, counterProgram);
                                    counterProgram += (component.length() - 2);
                                } else throw new Exception("Se esperaba el fin de la linea");
                            } else throw new Exception("Se esperaba una constante númerica, un arreglo o caracter");
                        }
                        case "DW" -> {
                            model.addComponent(component, ComponentType.PseudoInstruccion);
                            result = getNextComponent(line);
                            line = result[1];
                            component = result[0];
                            if (identifier.isWordNumberConstant(component)) {
                                if(identifier.isWordBinaryConstant(component) ||
                                        identifier.isByteBinaryConstant(component))
                                    model.addComponent(component, ComponentType.ConstanteBinariaWord);
                                else if(identifier.isWordHexadecimalConstant(component) ||
                                        identifier.isByteHexadecimalConstant(component))
                                    model.addComponent(component, ComponentType.ConstanteHexadecimalWord);
                                else model.addComponent(component, ComponentType.ConstanteDecimalWord);

                                int number = componentToNumber(component);

                                if (line.isEmpty() || identifier.isComment(line)) {
                                    model.addSymbol(symbol, SymbolType.Variable, number, SizeType.Word, counterProgram);
                                    counterProgram+=2;
                                } else {
                                    result = getNextComponent(line);
                                    line = result[1];
                                    component = result[0];

                                    ArrayList<Integer> list = arrayWord(component, number);
                                    model.addComponent(component, ComponentType.PseudoInstruccion);

                                    if (!line.isEmpty() && !identifier.isComment(line)) {
                                        throw new Exception("Se esperaba el fin de la linea");
                                    }
                                    model.addSymbol(symbol, SymbolType.Variable, list, SizeType.Word, counterProgram);
                                    counterProgram += 2*list.size();
                                }
                            }
                            else throw new Exception("Se esperaba una constante numerica");
                        }
                        case "EQU" -> {
                            model.addComponent(component, ComponentType.PseudoInstruccion);
                            result = getNextComponent(line);
                            line = result[1];
                            component = result[0];

                            if(!line.isEmpty() && !identifier.isComment(line))throw new Exception("Se esperaba el fin de la linea");
                            if (identifier.isWordNumberConstant(component)) {
                                model.addComponent(component, identifier.identifyComponent(component));
                                model.addSymbol(symbol, SymbolType.Constante, componentToNumber(component), SizeType.Word);

                            } else throw new Exception("Se esperaba una constante numerica");

                        }
                        default -> throw new Exception("Tipo de dato erroneo, se esperaba DB, DW o EQU");
                    }
                }
                if (inCodeSegment){

                    String[] result;
                    result = getNextComponent(line);
                    line = result[1];
                    component = result[0];

                    if(identifier.isInstruction(component)){
                        Instruction instruction = model.getInstruction(component);
                        if(line.isEmpty() || identifier.isComment(line)){
                            boolean isSintaxCorrect = instruction.checkSintax();
                            if(!isSintaxCorrect) throw new Exception("No está definida la instruccion "+component+"sin operandos");
                            model.addComponent(component, ComponentType.Instruccion);
                            String machineCode = instruction.encode();
                            model.addMachineCode(lineCoutner, machineCode);
                            //TODO: Aumentar el CP

                            counterProgram+=machineCode.length()/8;

                        } else{
                            //Primer operando
                            model.addComponent(component, ComponentType.Instruccion);
                            result = model.getNextOperand(line);
                            line = result[1];
                            component = result[0];

                            OperandType type = Identifier.identifyOperand(component);
                            if(type == OperandType.INVALID){
                                //addRestOFLine(component);
                                throw new Exception("Se esperaba un operando valido");
                            }

                            if(line.isEmpty()|| Identifier.isComment(line)){
                                boolean isSintaxCorrect = instruction.checkSintax(type);
                                if(!isSintaxCorrect) throw new Exception("No está definida la instruccion "+component+"con un operando de tipo "+type.toString());
                                model.addComponent(component, Identifier.identifyComponent(component));
                                String machineCode = instruction.encode(component);
                                model.addMachineCode(lineCoutner, machineCode);
                                //TODO: Aumentar el CP
                                counterProgram+=machineCode.length()/8;
                            } else{
                                model.addComponent(component, identifier.identifyComponent(component));
                                String firstOperand = component;
                                result = model.getNextOperand(line);
                                line = result[1];
                                component = result[0];




                                OperandType type2 = identifier.identifyOperand(component);
                                if(type2 == OperandType.INVALID) {
                                    throw new Exception("Se esperaba un operando valido");
                                }
                                boolean isSintaxCorrect = instruction.checkSintax(type, type2);
                                if(!isSintaxCorrect) throw new Exception("No está definida la instruccion "+component+"con un operando de tipo "+type.toString()+" y "+type2.toString());
                                model.addComponent(component, identifier.identifyComponent(component));
                                String machineCode = instruction.encode(firstOperand, component);
                                model.addMachineCode(lineCoutner, machineCode);

                                //TODO: Aumentar el CP
                                counterProgram+=machineCode.length()/8;
                            }
                        }
                    } else if(identifier.isTag(component)){
                        component = component.substring(0,component.length()-1);
                        model.addSymbol(component, SymbolType.Etiqueta, counterProgram, null, counterProgram);
                        model.addComponent(component, ComponentType.Etiqueta);

                    } else throw new Exception("Se esperaba una instruccion o simbolo");


                }

                if(!inCodeSegment && !inDataSegment && !inStackSegment){
                    throw new Exception("Se trata de escribir fuera de un segmento");
                }

            } catch (Exception e){
                model.setError(lineCoutner, e.getMessage());
                boolean isCausedByCaracterConstant = e.getCause() != null && e.getCause().getMessage().equals(ComponentType.CaracterConstante.toString());
                if(!component.isEmpty() && !isCausedByCaracterConstant){
                    addRestOFLine(component);
                    //model.addComponent(component, identifyComponent(component));
                }
                if(!line.isEmpty()){
                    addRestOFLine(line);
                }
            }
            line = model.getNextLine();
            lineCoutner++;
        }

    }

    private void addRestOFLine(String line) {
        String[] result = new String[2];
        while (!line.isEmpty()){
            try {
                result = getNextComponent(line);
            } catch (Exception e){
                if(e.getCause().getMessage().equals(ComponentType.CaracterConstante.toString())){
                    result = separateNotClosedCompounded(line);
                }
            }
            line = result[1];
            if(!result[0].isEmpty()) model.addComponent(result[0], identifier.identifyComponent(result[0]));
        }
    }

    private void goToAssembledPage() {
        model.resetScanner();
        //separateLines();
        //identifyComponents();
        analysisSyntacticSemantic();
        view.showAssembledPage(this,model.getFile(),model.getComponentList(),model.getErrors(), model.getErrorLines(), model.getSymbolData(), model.getCounterProgram(), model.getHexMachineCode());

    }

    public void start(){
        view.showMainPage(this, "No se ha seleccionado ningun archivo");
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
                model.errors.clear();
                model.components.clear();
                model.symbols.clear();
                model.clearData();


                view.showMainPage(this, model.file != null ? model.file.getName():"No se ha seleccionado ningun archivo");
                break;
            default:
                System.out.println("default");
                break;
        }
    }


    /**
     *
     * @param line, es la linea de la que se quiere obtener el siguiente componente
     * @return String[component, line]
     */
    public String[] getNextComponent(String line) throws Exception {
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
                    if (!possibleCommentDoubleQuote && (!component.isEmpty())) break;

                    possibleCommentDoubleQuote = !possibleCommentDoubleQuote;

                    if (!possibleCommentDoubleQuote) {
                        component.append(c);
                        characterCounter++;
                        break;
                    }
                }

                if (c == '\'' && !possibleCommentDoubleQuote) {
                    if (!possibleCommentSingleQuote && (!component.isEmpty()))break;


                    possibleCommentSingleQuote = !possibleCommentSingleQuote;
                    if (!possibleCommentSingleQuote) {
                        component.append(c);
                        characterCounter++;
                        break;
                    }
                }
                if (identifier.isSeparator(c) && !possibleCommentDoubleQuote && !possibleCommentSingleQuote) {
                    if(c ==':') component.append(c);
                    characterCounter++;
                    if(component.isEmpty()) continue;
                    break;
                }
            }
            component.append(c);
            characterCounter ++;
        }

        if((!component.isEmpty()) && !possibleCommentDoubleQuote && !possibleCommentSingleQuote && !isCompounded){
            return new String[]{component.toString(),line.substring(characterCounter)};
        } else if (possibleCommentDoubleQuote || possibleCommentSingleQuote || isCompounded){
            throw new Exception("Falta el simbolo de cerrado", new Throwable(ComponentType.CaracterConstante.toString()));
        }
        return new String[]{"",""};
    }

}
