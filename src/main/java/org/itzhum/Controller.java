package org.itzhum;
import org.itzhum.exceptions.ProcessCanceledException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

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

        model.setFile(new File("C:\\Users\\hum-s\\Downloads\\plantillaNueva.asm"));
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
            System.out.println(number);
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

    public void start(){
        view.showMainPage(this, "No se ha seleccionado ningun archivo");
    }

    public void separateNotClosedCompounded(String component){
        String[] parts = component.split("[ ,:]");
        for(String part: parts){
            model.setComponent(part, null);
        }
    }

    public void separateLines(){
        String line = "", component;

        boolean possibleCommentDoubleQuote;
        boolean possibleCommentSingleQuote;
        boolean isCompounded;

        while (model.scanner.hasNext()){
            possibleCommentDoubleQuote = false;
            possibleCommentSingleQuote = false;
            isCompounded = false;
            line = model.getNextLine();
            component = "";
            if (isComment(line)) continue;
            if (line.isEmpty() || line.isBlank()) continue;

            if(isPseudoInstruction(line)){
                model.setComponent(line, null);
                continue;
            }
            for(char c: line.toCharArray()) {
                if (c == ';') break;
                if ((component.equals("DUP(") || component.equals("[")) && !isCompounded) {
                    isCompounded = true;
                }
                if ((c == ')' || c == ']') && isCompounded) {
                    isCompounded = false;
                }
                if (!isCompounded) {
                    if (c == '"' && !possibleCommentSingleQuote) {
                        if (!possibleCommentDoubleQuote){
                            if (!component.isEmpty()) model.setComponent(component, null);
                            component = "";
                        }
                        possibleCommentDoubleQuote = !possibleCommentDoubleQuote;

                        if (!possibleCommentDoubleQuote) {
                            component += c;
                            model.setComponent(component, null);
                            component = "";
                            continue;
                        }
                    }

                    if (c == '\'' && !possibleCommentDoubleQuote) {
                        if (!possibleCommentSingleQuote) {
                            if (!component.isEmpty()) {
                                model.setComponent(component, null);

                            }
                            component = "";
                        }
                        possibleCommentSingleQuote = !possibleCommentSingleQuote;
                        if (!possibleCommentSingleQuote) {
                            component += c;
                            model.setComponent(component, null);
                            component = "";
                            continue;
                        }
                    }
                    if (isSeparator(c) && !possibleCommentDoubleQuote && !possibleCommentSingleQuote) {
                        if (!component.isEmpty()) {
                            model.setComponent(component, c==':'?Model.tag:null);

                        }
                        component = "";
                        continue;
                    }
                }
                component += c;
            }
            if(!component.isEmpty() && !possibleCommentDoubleQuote && !possibleCommentSingleQuote && !isCompounded){
                model.setComponent(component, null);
            } else if (possibleCommentDoubleQuote || possibleCommentSingleQuote || isCompounded){
                separateNotClosedCompounded(component);
            }


        }



    }

    public void identifyComponents(){
        for (AssemblerComponent component : model.components){
            if (component.type == null){
                if(isPseudoInstruction(component.name)){
                    component.type = Model.pseudoInstruction;
                } else if (isInstruction(component.name)){
                    component.type = Model.instruction;
                } else if(isRegisterHalf(component.name)){
                    component.type = Model.registerHalf;
                } else if(isRegistersComplete(component.name)){
                    component.type = Model.registerComplete;
                } else if(isCaracterConstant(component.name)){
                    component.type = Model.caracterConstant;
                } else if(isByteDecimalConstant(component.name)) {
                    component.type = Model.byteDecimalConstant;
                } else if(isByteHexadecimalConstant(component.name)){
                    component.type = Model.byteHexadecimalConstant;
                } else if(isByteBinaryConstant(component.name)){
                    component.type = Model.byteBinaryConstant;
                } else if(isWordDecimalConstant(component.name)){
                    component.type = Model.wordDecimalConstant;
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

    private void goToAssembledPage() {

        separateLines();
        identifyComponents();
        view.showAssembledPage(this,model.getFile(),model.getComponentList());

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
