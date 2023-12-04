package org.itzhum.logic;

import org.itzhum.Model;
import org.itzhum.types.*;

import java.util.ArrayList;

public class Identifier {
    public static Model model;

    public Identifier(Model model) {
        Identifier.model = model;
    }

    public static boolean isArrayString(ArrayList lista) {
        return lista.get(0).getClass().equals(String.class);
    }

    public static boolean isInstruction(String line) {
        return model.instructions.containsKey(line.toUpperCase());
    }

    public static boolean isPseudoInstruction(String line) {
        boolean isPseudo = model.pseudoInstructions.containsKey(line.toUpperCase());
        if (!isPseudo) isPseudo = line.startsWith("DUP(") && line.endsWith(")");
        if (!isPseudo) isPseudo = line.startsWith("BYTE PTR ");
        if (!isPseudo) isPseudo = line.startsWith("WORD PTR ");
        return isPseudo;
    }

    public static boolean isRegistersComplete(String line) {
        return model.registersComplete.containsKey(line.toUpperCase());
    }

    public static boolean isRegisterHalf(String line) {
        return model.registersHalf.containsKey(line.toUpperCase());
    }

    public static boolean isCaracterConstant(String line) {
        boolean simpleQuoteStart = line.startsWith("'");
        boolean simpleQuoteEnd = line.endsWith("'");
        boolean doubleQuoteStart = line.startsWith("\"");
        boolean doubleQuoteEnd = line.endsWith("\"");
        return (simpleQuoteEnd && simpleQuoteStart) || (doubleQuoteEnd && doubleQuoteStart);
    }

    public static boolean isByteDecimalConstant(String line) {
        if (line.matches("(\\d+[Dd]?)")) {
            int number = Integer.parseInt(line.replaceAll("[Dd]", ""));
            return number >= 0 && number <= 255;
        }
        return false;
    }

    public static boolean isWordDecimalConstant(String line) {
        if (line.matches("(\\d+[Dd]?)")) {
            int number = Integer.parseInt(line.replaceAll("[Dd]", ""));
            return number >= 256 && number <= 65535;
        }
        return false;
    }

    public static boolean isByteHexadecimalConstant(String line) {
        if (line.matches("(0(\\d|[A-Fa-f])*[Hh])")) {
            int number = Integer.parseInt(line.replaceAll("[Hh]", ""), 16);
            return number >= 0 && number <= 255;
        }
        return false;
    }

    public static boolean isWordHexadecimalConstant(String line) {
        if (line.matches("(0(\\d|[A-Fa-f])*[Hh])")) {
            int number = Integer.parseInt(line.replaceAll("[Hh]", ""), 16);
            return number >= 256 && number <= 65535;
        }
        return false;
    }

    public static boolean isByteBinaryConstant(String line) {
        return line.matches("([01]{8}[Bb])");
    }

    public static boolean isWordBinaryConstant(String line) {
        return line.matches("([01]{16}[Bb])");
    }

    public static boolean isValidSymbol(String line) {
        return line.matches("([A-Za-z_][A-Za-z0-9_]{0,9})");
    }

    public static boolean isSeparator(char c) {
        return c == ' ' || c == ',' || c == ':';
    }

    public static boolean isComment(String line) {
        line = line.trim();
        return line.startsWith(";");
    }

    public static boolean isSymbol(String component) {
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

    public static boolean isTag(String component){
        if(component.endsWith(":")){
            component = component.substring(0, component.length()-1);
            return isValidSymbol(component);
        }
        if(model.findSymbol(component)){
            Symbol symbol = model.getSymbol(component);
            return symbol.getType() == SymbolType.Etiqueta;
        }
        return false;
    }

    public static ComponentType identifyComponent(String component){
        if(component.matches("\\[.*]")) return ComponentType.Memoria;

        if(isPseudoInstruction(component)) return ComponentType.PseudoInstruccion;
        if(isInstruction(component)) return ComponentType.Instruccion;
        if(isRegisterHalf(component)) return ComponentType.RegistroBajo;
        if(isRegistersComplete(component)) return ComponentType.RegistroCompleto;
        if(isSegmentRegister(component)) return ComponentType.RegistroSegmento;
        if(isCaracterConstant(component)) return ComponentType.CaracterConstante;
        if(isByteDecimalConstant(component)) return ComponentType.ConstanteDecimalByte;
        if(isByteHexadecimalConstant(component)) return ComponentType.ConstanteHexadecimalByte;
        if(isByteBinaryConstant(component)) return ComponentType.ConstanteBinariaByte;
        if(isWordDecimalConstant(component)) return ComponentType.ConstanteDecimalWord;
        if(isWordHexadecimalConstant(component)) return ComponentType.ConstanteHexadecimalWord;
        if(isWordBinaryConstant(component)) return ComponentType.ConstanteBinariaWord;
        if(isTag(component)) return ComponentType.Etiqueta;
        if(isValidSymbol(component)) return ComponentType.Simbolo;
        return ComponentType.Desconocido;
    }

    private static boolean isSegmentRegister(String component) {
        return model.segmentRegister.containsKey(component.toUpperCase());
    }

    public static OperandType identifyOperand(String component){
        ComponentType componentType = identifyComponent(component);


        switch (componentType) {
            case RegistroBajo -> {
                return OperandType.REGISTERBYTE;
            }
            case RegistroCompleto -> {
                return OperandType.REGISTERWORD;
            }
            case RegistroSegmento -> {
                return OperandType.SEGMENTREGISTER;
            }
            case Etiqueta -> {
                if(model.findSymbol(component)){
                    return OperandType.TAG;
                }
                else return OperandType.INVALID;
            }
            case ConstanteDecimalByte, ConstanteHexadecimalByte, ConstanteBinariaByte, ConstanteBinariaWord, ConstanteDecimalWord, ConstanteHexadecimalWord -> {
                return OperandType.INMEDIATE;
            }
            case Simbolo -> {
                if(model.findSymbol(component)){
                    Symbol symbol = model.getSymbol(component);

                    if(symbol.getSize() == SizeType.Byte)
                        return OperandType.MEMORYBYTE;
                    else return OperandType.MEMORYWORD;
                }
                else return OperandType.INVALID;
            }
            case Memoria -> {
                return OperandType.MEMORY;
            }
            case PseudoInstruccion -> {
                if(component.startsWith("BYTE PTR ")){
                    component = component.substring(9);
                    componentType = identifyComponent(component);
                    if (componentType == ComponentType.Memoria) {
                        return OperandType.MEMORYBYTE;
                    }
                    if(componentType == ComponentType.Simbolo){
                        if (!model.findSymbol(component)) return OperandType.INVALID;
                        if(model.getSymbol(component).getType() == SymbolType.Variable)
                            return OperandType.MEMORYBYTE;
                    }
                    //TODO: IDENTIFICAR QUE ES.
                    return OperandType.INVALID;
                }
                else if(component.startsWith("WORD PTR ")){
                    component = component.substring(9);
                    componentType = identifyComponent(component);
                    if (componentType == ComponentType.Memoria) {
                        return OperandType.MEMORYWORD;
                    }
                    if(componentType == ComponentType.Simbolo){
                        if (!model.findSymbol(component)) return OperandType.INVALID;
                        if(model.getSymbol(component).getType() == SymbolType.Variable)
                            return OperandType.MEMORYWORD;
                    }
                    return OperandType.INVALID;
                }
                else return OperandType.INVALID;
            }
            default -> {
                return OperandType.INVALID;
            }
        }
    }

    public static boolean isWordNumberConstant(String component) {
        return (isWordDecimalConstant(component) ||
                isWordHexadecimalConstant(component) ||
                isWordBinaryConstant(component) ||
                isByteNumberConstant(component) );
    }

    public static boolean isByteNumberConstant(String component) {
        return (isByteDecimalConstant(component) ||
                isByteHexadecimalConstant(component) ||
                isByteBinaryConstant(component));
    }
}