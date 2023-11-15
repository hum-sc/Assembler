package org.itzhum;
import org.itzhum.exceptions.ProcessCanceledException;
import org.itzhum.types.ComponentType;
import org.itzhum.types.SizeType;
import org.itzhum.types.SymbolType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Controller implements ActionListener {
    View view;
    Model model;

    public Controller(View view, Model model){
        super();
        this.view = view;
        this.model = model;
        model.setFile(new File("C:\\Users\\hum-s\\Documents\\ene.asm"));
    }

    public boolean isInstruction(String line){
        return model.instructions.containsKey(line.toUpperCase());
    }
    public boolean isPseudoInstruction(String line){
        boolean isPseudo = model.pseudoInstructions.containsKey(line.toUpperCase());
        if(!isPseudo) isPseudo = line.startsWith("DUP(")&& line.endsWith(")");
        return isPseudo;
    }
    public boolean isRegistersComplete (String line){return model.registersComplete.containsKey(line.toUpperCase());}
    public boolean isRegisterHalf (String line){ return model.registersHalf.containsKey(line.toUpperCase());}
    public boolean isCaracterConstant(String line){
        boolean simpleQuoteStart = line.startsWith("'");
        boolean simpleQuoteEnd = line.endsWith("'");
        boolean doubleQuoteStart = line.startsWith("\"");
        boolean doubleQuoteEnd = line.endsWith("\"");
        return (simpleQuoteEnd && simpleQuoteStart)||( doubleQuoteEnd && doubleQuoteStart);
    }

    public boolean isByteDecimalConstant(String line){
        if (line.matches("(\\d+[Dd]?)")) {
            int number = Integer.parseInt(line.replaceAll("[Dd]", ""));
            return number >= 0 && number <= 255;
        }
        return false;
    }

    public boolean isWordDecimalConstant(String line){
        if (line.matches("(\\d+[Dd]?)")) {
            int number = Integer.parseInt(line.replaceAll("[Dd]", ""));
            return number >= 256 && number <= 65535;
        }
        return false;
    }

    public boolean isByteHexadecimalConstant(String line){
        if (line.matches("(0(\\d|[A-Fa-f])*[Hh])")) {
            int number = Integer.parseInt(line.replaceAll("[Hh]", ""), 16);
            return number >= 0 && number <= 255;
        }
        return false;
    }

    public boolean isWordHexadecimalConstant(String line){
        if (line.matches("(0(\\d|[A-Fa-f])*[Hh])")) {
            int number = Integer.parseInt(line.replaceAll("[Hh]", ""), 16);
            return number >= 256 && number <= 65535;
        }
        return false;
    }

    public boolean isByteBinaryConstant(String line){
        return line.matches("([01]{8}[Bb])");
    }

    public boolean isWordBinaryConstant(String line){
        return line.matches("([01]{16}[Bb])");
    }

    public boolean isValidSymbol(String line){
        return line.matches("([A-Za-z_][A-Za-z0-9_]{0,9})");
    }
    public boolean isSeparator(char c){
        return c == ' ' || c == ',' || c == ':';
    }

    public boolean isComment(String line){
        return  line.startsWith(";");
    }

    public boolean isSymbol(String component){
        return (!isComment(component) &&
                !isInstruction(component) &&
                !isPseudoInstruction(component) &&
                !isRegistersComplete(component) &&
                !isRegisterHalf(component) &&
                !isCaracterConstant(component) &&
                !isByteDecimalConstant(component) &&
                !isByteHexadecimalConstant(component) &&
                !isByteBinaryConstant(component) &&
                !isWordDecimalConstant(component) &&
                !isWordHexadecimalConstant(component) &&
                !isWordBinaryConstant(component) &&
                isValidSymbol(component));
    }

    public int componentToNumber(String component) throws  Exception{
        if (isByteDecimalConstant(component)|| isWordDecimalConstant(component)){
            return Integer.parseInt(component.replaceAll("[Dd]", ""));
        }
        if (isByteHexadecimalConstant(component)|| isWordHexadecimalConstant(component)){
            return Integer.parseInt(component.replaceAll("[Hh]", ""), 16);
        }
        if (isByteBinaryConstant(component)|| isWordBinaryConstant(component)){
            return Integer.parseInt(component.replaceAll("[Bb]", ""), 2);
        }
        throw new Exception("No es una constante numerica");
    }
    public String[] separateNotClosedCompounded(String component){
        String[] parts = component.split("[ ,:]");

        component = component.replaceFirst(parts[0], "");
        return new String[]{parts[0], component};
    }

    /**
     * @param component, length
     * @return Tamaño del arreglo
     * @throws Exception no es un arreglo o parametros erroneos
     */
    public ArrayList arrayByte(String component, int length) throws Exception{
        int size;
        if(component.startsWith("DUP(")){
            if(!component.endsWith(")")) throw new Exception("Falta el simbolo de cerrado");
            model.addComponent(component, ComponentType.PseudoInstruccion);
            component = component.substring(4, component.length()-1);
            if(isByteNumberConstant(component)){
                int number = componentToNumber(component);
                ArrayList <Integer> array = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    array.add(number);
                }
                return array;
            } else if (isCaracterConstant(component)){
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
            model.addComponent(component, ComponentType.PseudoInstruccion);
            component = component.substring(4, component.length()-1);

            if(!isWordNumberConstant(component)){
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
/*
    public void separateLines(){
        String line;
        StringBuilder component;

        boolean possibleCommentDoubleQuote;
        boolean possibleCommentSingleQuote;
        boolean isCompounded;

        while (model.scanner.hasNext()){
            possibleCommentDoubleQuote = false;
            possibleCommentSingleQuote = false;
            isCompounded = false;
            line = model.getNextLine();
            component = new StringBuilder();
            if (isComment(line)) continue;
            if (line.isEmpty() || line.isBlank()) continue;

            if(isPseudoInstruction(line)){
                model.addComponent(line, null);
                continue;
            }
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
                        if (!possibleCommentDoubleQuote){
                            if (!component.isEmpty()) model.addComponent(component.toString(), null);
                            component = new StringBuilder();
                        }
                        possibleCommentDoubleQuote = !possibleCommentDoubleQuote;

                        if (!possibleCommentDoubleQuote) {
                            component.append(c);
                            model.addComponent(component.toString(), null);
                            component = new StringBuilder();
                            continue;
                        }
                    }

                    if (c == '\'' && !possibleCommentDoubleQuote) {
                        if (!possibleCommentSingleQuote) {
                            if (!component.isEmpty()) {
                                model.addComponent(component.toString(), null);

                            }
                            component = new StringBuilder();
                        }
                        possibleCommentSingleQuote = !possibleCommentSingleQuote;
                        if (!possibleCommentSingleQuote) {
                            component.append(c);
                            model.addComponent(component.toString(), null);
                            component = new StringBuilder();
                            continue;
                        }
                    }
                    if (isSeparator(c) && !possibleCommentDoubleQuote && !possibleCommentSingleQuote) {
                        if (!component.isEmpty()) {
                            model.addComponent(component.toString(), c==':'? ComponentType.Etiqueta:null);

                        }
                        component = new StringBuilder();
                        continue;
                    }
                }
                component.append(c);
            }
            if((!component.isEmpty()) && !possibleCommentDoubleQuote && !possibleCommentSingleQuote && !isCompounded){
                model.addComponent(component.toString(), null);
            } else if (possibleCommentDoubleQuote || possibleCommentSingleQuote || isCompounded){
                separateNotClosedCompounded(component.toString());
            }


        }



    }

        public void identifyComponents(){
            for (AssemblerComponent component : model.components){
                if (component.type == null){
                    if(isPseudoInstruction(component.name)){
                        component.type =ComponentType.PseudoInstruccion;
                    } else if (isInstruction(component.name)){
                        component.type = ComponentType.Instruccion;
                    } else if(isRegisterHalf(component.name)){
                        component.type = ComponentType.RegistroMedio;
                    } else if(isRegistersComplete(component.name)){
                        component.type = ComponentType.RegistroCompleto
                    } else if(isCaracterConstant(component.name)){
                        component.type =ComponentType.CaracterConstante;
                    } else if(isByteDecimalConstant(component.name)) {
                        component.type = ComponentType.ConstanteDecimal;
                    } else if(isByteHexadecimalConstant(component.name)){
                        component.type = ComponentType.ConstanteHexadecimal;
                    } else if(isByteBinaryConstant(component.name)){
                        component.type = ComponentType.ConstanteBinaria;
                    } else if(isWordDecimalConstant(component.name)){
                        component.type = ComponentType.Desconocido;
                    } else if(isWordHexadecimalConstant(component.name)){
                        component.type = Model.wordHexadecimalConstant;
                    } else if(isWordBinaryConstant(component.name)){
                        component.type = Model.wordBinaryConstant;
                    } else if(isValidSymbol(component.name)){
                        component.type = Model.symbol;
                    } else {
                        component.type = Model.unknown;
                    }
                }
            }
        }
    */

    public ComponentType identifyComponent(String component){
        if(isPseudoInstruction(component)) return ComponentType.PseudoInstruccion;
        if(isInstruction(component)) return ComponentType.Instruccion;
        if(isRegisterHalf(component)) return ComponentType.RegistroBajo;
        if(isRegistersComplete(component)) return ComponentType.RegistroCompleto;
        if(isCaracterConstant(component)) return ComponentType.CaracterConstante;
        if(isByteDecimalConstant(component)) return ComponentType.ConstanteDecimalByte;
        if(isByteHexadecimalConstant(component)) return ComponentType.ConstanteHexadecimalByte;
        if(isByteBinaryConstant(component)) return ComponentType.ConstanteBinariaByte;
        if(isWordDecimalConstant(component)) return ComponentType.ConstanteDecimalWord;
        if(isWordHexadecimalConstant(component)) return ComponentType.ConstanteHexadecimalWord;
        if(isWordBinaryConstant(component)) return ComponentType.ConstanteBinariaWord;
        if(isValidSymbol(component)) return ComponentType.Simbolo;
        return ComponentType.Desconocido;
    }
    public void analysisSyntacticSemantic(){
        model.resetScanner();

        String line = model.getNextLine(), component = "";

        int lineCoutner = 0;
        int counterProgram = 0;

        boolean inStackSegment = false;
        boolean inDataSegment = false;
        boolean inCodeSegment = false;

        while (line != null ) {

            if (!line.isEmpty() && !line.isBlank() && !isComment(line)) try{
                if(isPseudoInstruction(line)){
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
                            }
                            if (line.equals(".DATA SEGMENT")){
                                inDataSegment = true;
                            }
                            if(line.equals(".CODE SEGMENT")){
                                inCodeSegment = true;

                            }
                            line = model.getNextLine();
                            lineCoutner++;
                            counterProgram = 0;
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

                    if (!isWordNumberConstant(component))throw new Exception("Se necesita una constante númerica");

                    ComponentType type;

                    if(isWordHexadecimalConstant(component)) type = ComponentType.ConstanteHexadecimalWord;
                    else if(isWordBinaryConstant(component)) type = ComponentType.ConstanteBinariaWord;
                    else type = ComponentType.ConstanteDecimalWord;

                    model.addComponent(component, type);

                    int length = componentToNumber(component);

                    result = getNextComponent(line);
                    line = result[1];
                    component = result[0];

                    //DUP
                    ArrayList<Integer> list = arrayWord(component, length);

                    model.addComponent(component, ComponentType.PseudoInstruccion);


                    if(!line.isEmpty() && !isComment(line)){
                        throw new Exception("Se esperaba el fin de la linea");
                    }
                    counterProgram += list.size()*16;
                }
                if (inDataSegment){
                    String[] result;
                    result = getNextComponent(line);
                    line = result[1];
                    component = result[0];

                    if(!isSymbol(component)) throw new Exception("Se esperaba un simbolo adecuado");
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
                            if (isByteNumberConstant(component)) {
                                model.addComponent(component, ComponentType.ConstanteDecimalByte);
                                int number = componentToNumber(component);
                                if (line.isEmpty() || isComment(line)) {
                                    model.addSymbol(symbol, SymbolType.Variable, number, SizeType.Byte, counterProgram);
                                    counterProgram += 8;
                                } else {
                                    result = getNextComponent(line);
                                    line = result[1];
                                    component = result[0];

                                    ArrayList list = arrayByte(component, number);

                                    if (!line.isEmpty() && !isComment(line)) {
                                        throw new Exception("Se esperaba el fin de la linea");
                                    }

                                    model.addSymbol(symbol, SymbolType.Variable, list, SizeType.Byte, counterProgram);

                                    counterProgram += isArrayString(list) ? 8*(list.get(0).toString().length()-2)*list.size() : 8*list.size();

                                }
                            } else if (isWordNumberConstant(component)) {
                                if (line.isEmpty()) throw new Exception("Valor palabra en variable byte");

                                model.addComponent(component, ComponentType.ConstanteDecimalWord);

                                result = getNextComponent(line);
                                line = result[1];
                                component = result[0];

                                ArrayList list = arrayByte(component, componentToNumber(component));

                                model.addComponent(component, ComponentType.PseudoInstruccion);

                                if (!line.isEmpty() || !isComment(line)) {
                                    throw new Exception("Se esperaba el fin de la linea");
                                }
                                counterProgram += isArrayString(list) ? 8*(list.get(0).toString().length()-2)*list.size() : 8*list.size();
                            } else if (isCaracterConstant(component)) {
                                if (line.isEmpty() || isComment(line)) {
                                    model.addComponent(component, ComponentType.CaracterConstante);
                                    model.addSymbol(symbol, SymbolType.Variable, component.substring(1,component.length()-1), SizeType.Byte, counterProgram);
                                    counterProgram += 8 * (component.length() - 2);
                                } else throw new Exception("Se esperaba el fin de la linea");
                            } else throw new Exception("Se esperaba una constante númerica, un arreglo o caracter");
                        }
                        case "DW" -> {
                            model.addComponent(component, ComponentType.PseudoInstruccion);
                            result = getNextComponent(line);
                            line = result[1];
                            component = result[0];
                            if (isWordNumberConstant(component)) {
                                if(isWordBinaryConstant(component) ||
                                        isByteBinaryConstant(component))
                                    model.addComponent(component, ComponentType.ConstanteBinariaWord);
                                else if(isWordHexadecimalConstant(component) ||
                                        isByteHexadecimalConstant(component))
                                    model.addComponent(component, ComponentType.ConstanteHexadecimalWord);
                                else model.addComponent(component, ComponentType.ConstanteDecimalWord);

                                int number = componentToNumber(component);

                                if (line.isEmpty() || isComment(line)) {
                                    model.addSymbol(symbol, SymbolType.Variable, number, SizeType.Word, counterProgram);
                                } else {
                                    result = getNextComponent(line);
                                    line = result[1];
                                    component = result[0];

                                    ArrayList<Integer> list = arrayWord(component, number);
                                    model.addComponent(component, ComponentType.PseudoInstruccion);

                                    if (!line.isEmpty() && !isComment(line)) {
                                        throw new Exception("Se esperaba el fin de la linea");
                                    }
                                    model.addSymbol(symbol, SymbolType.Variable, list, SizeType.Word, counterProgram);
                                    counterProgram += 16*list.size();
                                }
                            }
                            else throw new Exception("Se esperaba una constante numerica");
                        }
                        case "EQU" -> {
                            model.addComponent(component, ComponentType.PseudoInstruccion);
                            result = getNextComponent(line);
                            line = result[1];
                            component = result[0];

                            if(!line.isEmpty() && !isComment(line))throw new Exception("Se esperaba el fin de la linea");
                            if (isWordNumberConstant(component)) {
                                model.addComponent(component, identifyComponent(component));
                                model.addSymbol(symbol, SymbolType.Constante, componentToNumber(component), SizeType.Word, counterProgram);
                                counterProgram += 16;
                            } else throw new Exception("Se esperaba una constante numerica");

                        }
                        default -> throw new Exception("Tipo de dato erroneo, se esperaba DB, DW o EQU");
                    }
                }
                if (inCodeSegment){
                    //TODO: CODE SEGMENT ANALYSIS}


                }
            } catch (Exception e){
                model.setError(lineCoutner, e.getMessage());
                System.out.println(component+" , "+ e.getMessage()+" , "+ line);

                boolean isCausedByCaracterConstant = e.getCause() != null && e.getCause().getMessage().equals(ComponentType.CaracterConstante.toString());
                if(!component.isEmpty() && !isCausedByCaracterConstant){
                    model.addComponent(component, identifyComponent(component));
                }
                if(!line.isEmpty()){
                    addRestOFLine(line);
                }
            }
            line = model.getNextLine();
            lineCoutner++;
        }

    }

    private static boolean isArrayString(ArrayList lista) {
        return lista.get(0).getClass().equals(String.class);
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
            if(!result[0].isEmpty()) model.addComponent(result[0], identifyComponent(result[0]));
        }
    }

    private boolean isWordNumberConstant(String component) {
        return (isWordDecimalConstant(component) ||
                isWordHexadecimalConstant(component) ||
                isWordBinaryConstant(component) ||
                isByteNumberConstant(component) );
    }

    public boolean isByteNumberConstant(String component) {
        return (isByteDecimalConstant(component) ||
                isByteHexadecimalConstant(component) ||
                isByteBinaryConstant(component));
    }

    private void goToAssembledPage() {
        model.resetScanner();
        //separateLines();
        //identifyComponents();
        analysisSyntacticSemantic();
        view.showAssembledPage(this,model.getFile(),model.getComponentList());

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
                if (isSeparator(c) && !possibleCommentDoubleQuote && !possibleCommentSingleQuote) {
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
