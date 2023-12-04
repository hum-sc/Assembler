package org.itzhum.logic;

import org.itzhum.Code;
import org.itzhum.Controller;
import org.itzhum.types.OperandType;
import org.itzhum.types.Symbol;
import org.itzhum.types.SymbolType;

import javax.naming.ldap.Control;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Instruction {
    private final boolean hasNoOperand;
    private final boolean hasOneOperand;
    private final boolean hasTwoOperands;
    private final boolean acceptByte;
    private final boolean acceptWord;
    private final boolean acceptByteByte;
    private final boolean acceptWordByte;
    private final boolean acceptWordWord;
    private final String name;

    private final Set<OperandType> OperandAccepted;
    private final Set<String> OperandPairAccepted;

    private final HashMap<String,Code> Codes;


    public Instruction(String name, boolean hasNoOperand, boolean hasOneOperand, boolean hasTwoOperands, boolean isByte, boolean isWord, boolean isByteByte, boolean isWordByte, boolean isWordWord) {
        this.name = name;
        this.hasNoOperand = hasNoOperand;
        this.hasOneOperand = hasOneOperand;
        this.hasTwoOperands = hasTwoOperands;
        this.acceptByte = isByte;
        this.acceptWord = isWord;
        this.acceptByteByte = isByteByte;
        this.acceptWordByte = isWordByte;
        this.acceptWordWord = isWordWord;
        OperandAccepted =new HashSet<OperandType>();
        OperandPairAccepted = new HashSet<String>();
        Codes = new HashMap<>();

    }

    public void addCode(String code) {
        Codes.put("",new Code(code));

    }

    public void addCode(OperandType operandType, String code) {
        //Codes.put(operandType.toString(),);
    }

    public void addCode(OperandType firstOperandType, OperandType secondOperandType, Code code) {
        Codes.put(firstOperandType.toString()+","+secondOperandType.toString(),code);
    }
    public void addOperandAccepted(OperandType operand, Code code){
        OperandType lOperand = operand;
        if(acceptByte){
            if(operand == OperandType.MEMORY) lOperand = OperandType.MEMORYBYTE;
            if(operand == OperandType.REGISTER) lOperand = OperandType.REGISTERBYTE;
            OperandAccepted.add(lOperand);
            Codes.put(lOperand.toString(),code);
        }
        if(acceptWord){
            if(operand == OperandType.MEMORY) lOperand = OperandType.MEMORYWORD;
            if(operand == OperandType.REGISTER) lOperand = OperandType.REGISTERWORD;
            OperandAccepted.add(lOperand);
            Codes.put(lOperand.toString(),code);
        }
        if(operand == OperandType.MEMORY){
            OperandAccepted.add(operand);
            Codes.put(operand.toString(), code);
        }

        if(!acceptWord && !acceptByte){
            OperandAccepted.add(operand);
            Codes.put(operand.toString(),code);
        }
    }


    public void addPairOperandAccepted(OperandType firstOperand, OperandType secondOperand, Code code){
        OperandType lFirstOperand = firstOperand;
        OperandType lSecondOperand = secondOperand;
        if(acceptByteByte){
            if(firstOperand == OperandType.REGISTER) lFirstOperand = OperandType.REGISTERBYTE;
            if(secondOperand == OperandType.REGISTER) lSecondOperand = OperandType.REGISTERBYTE;

            if(firstOperand == OperandType.MEMORY) {
                lFirstOperand = OperandType.MEMORYBYTE;
                if (secondOperand != OperandType.MEMORY) {
                    OperandPairAccepted.add(firstOperand + "," + lSecondOperand.toString());
                    Codes.put(firstOperand + "," + lSecondOperand,code);
                }
            }
            if(secondOperand == OperandType.MEMORY) {
                lSecondOperand = OperandType.MEMORYBYTE;
                if (firstOperand != OperandType.MEMORY) {
                    OperandPairAccepted.add(lFirstOperand.toString() + "," + secondOperand);
                    Codes.put(lFirstOperand + "," + secondOperand,code);
                }
            }
            OperandPairAccepted.add(lFirstOperand.toString()+","+lSecondOperand.toString());
            Codes.put(lFirstOperand +","+ lSecondOperand,code);
        }
        if (acceptWordByte){
            if(firstOperand == OperandType.REGISTER) lFirstOperand = OperandType.REGISTERWORD;
            if(secondOperand == OperandType.REGISTER) lSecondOperand = OperandType.REGISTERBYTE;

            if(firstOperand == OperandType.MEMORY) {
                lFirstOperand = OperandType.MEMORYWORD;
                if (secondOperand != OperandType.MEMORY) {
                    OperandPairAccepted.add(firstOperand + "," + lSecondOperand.toString());
                    Codes.put(firstOperand + "," + lSecondOperand,code);
                }
            }
            if(secondOperand == OperandType.MEMORY) {
                lSecondOperand = OperandType.MEMORYBYTE;
                if (firstOperand != OperandType.MEMORY) {
                    OperandPairAccepted.add(lFirstOperand.toString() + "," + secondOperand);
                    Codes.put(lFirstOperand + "," + secondOperand,code);
                }
            }

            OperandPairAccepted.add(lFirstOperand.toString()+","+lSecondOperand.toString());
            Codes.put(lFirstOperand +","+ lSecondOperand,code);
        }
        if(firstOperand == OperandType.MEMORY && secondOperand == OperandType.MEMORY){
            OperandPairAccepted.add(firstOperand +","+ secondOperand);
            Codes.put(firstOperand +","+ secondOperand,code);
        }
        if (acceptWordWord){
            if (firstOperand== OperandType.REGISTER) lFirstOperand = OperandType.REGISTERWORD;
            if (secondOperand== OperandType.REGISTER) lSecondOperand = OperandType.REGISTERWORD;
            if (firstOperand== OperandType.MEMORY) {
                lFirstOperand = OperandType.MEMORYWORD;
                if (secondOperand!= OperandType.MEMORY){
                    OperandPairAccepted.add(firstOperand +","+lSecondOperand.toString());
                    Codes.put(firstOperand +","+ lSecondOperand,code);
                }
            }
            if (secondOperand== OperandType.MEMORY){
                lSecondOperand = OperandType.MEMORYWORD;
                if (firstOperand!= OperandType.MEMORY) {
                    OperandPairAccepted.add(lFirstOperand.toString()+","+ secondOperand);
                    Codes.put(lFirstOperand +","+ secondOperand,code);
                }
            }
            OperandPairAccepted.add(lFirstOperand.toString()+","+lSecondOperand.toString());
            Codes.put(lFirstOperand +","+ lSecondOperand,code);
        }
        if(!acceptWordByte && !acceptByteByte && !acceptWordWord) {
            OperandPairAccepted.add(firstOperand.toString()+","+secondOperand.toString());
            Codes.put(firstOperand +","+ secondOperand,code);
        }
    }


    public boolean checkSintax() throws Exception{
        if(!hasNoOperand) {
            String errorMsg = "Instruccion "+name+" necesita ";
            if(hasOneOperand) {
                errorMsg += "un operando";
                if(hasTwoOperands) errorMsg += " o ";
            }
            if(hasTwoOperands) errorMsg += "dos operandos";
            throw new Exception(errorMsg);
        }
        return true;
    }

    public boolean checkSintax(OperandType operand) throws Exception{
        if(!hasOneOperand){
            String errorMsg = "Instruccion "+name+" necesita ";
            if(hasNoOperand) {
                errorMsg += "ningun operando";
                if(hasTwoOperands) errorMsg += " o ";
            }
            if(hasTwoOperands) errorMsg += "dos operandos";
            throw new Exception(errorMsg);
        }else if( !OperandAccepted.contains(operand)){
            StringBuilder errorMsg = new StringBuilder("Instruccion " + name + " necesita un operando de tipo ");
            for(OperandType op : OperandAccepted){
                errorMsg.append(op.toString()).append(" o ");
            }
            errorMsg.append(" pero se recibio ").append(operand.toString());
            throw new Exception(errorMsg.toString());
        }
        return true;
    }

    public boolean checkSintax(String firstOperand, String secondOperand) throws Exception {
        OperandType firstOperandType = Identifier.identifyOperand(firstOperand);
        OperandType secondOperandType = Identifier.identifyOperand(secondOperand);
        String operandCombination = firstOperandType.toString() + "," + secondOperandType.toString();
        if (!hasTwoOperands) {
            String errorMsg = "Instruccion " + name + " necesita ";
            if (hasNoOperand) {
                errorMsg += "ningun operando";
                if (hasOneOperand) errorMsg += " o ";
            }
            if (hasOneOperand) errorMsg += "un operando";
            throw new Exception(errorMsg);
        } else if (!OperandPairAccepted.contains(operandCombination)) {
            if(Controller.model.findSymbol(secondOperand)) {
                Symbol symbol = Controller.model.getSymbol(secondOperand);
                if(symbol.getType() == SymbolType.Constante) {
                    return OperandPairAccepted.contains(firstOperandType+","+OperandType.INMEDIATE);
                }
            }
            StringBuilder errorMsg = new StringBuilder("Instruccion " + name + " necesita una combinacion de operandos ");
            for (String op : OperandPairAccepted) {
                errorMsg.append(op).append(" o ");
            }
            errorMsg.append(" pero se recibio ").append(firstOperandType).append(",").append(secondOperandType);
            throw new Exception(errorMsg.toString());
        }
        return OperandPairAccepted.contains(operandCombination);
    }


    public String encode(){
        //String dir = DirectionSpecifier.generateDirection();
        Code code = Codes.get("");
        return code.generateCode();
    }

    public String encode(String operand) throws Exception {
        //String dir = DirectionSpecifier.generateDirection();
        OperandType operandType = Identifier.identifyOperand(operand);
        if(operandType == OperandType.CONSTANT){
            operand = Controller.model.getSymbol(operand).getValue().toString();
            if(Identifier.isWordNumberConstant(operand)){
                operandType = OperandType.INMEDIATE;
            }
        }
        Code code = Codes.get(operandType.toString());
        return code.generateCode(operand);
    }

    public String encode(String firstOperand, String secondOperand) throws Exception {
        //String dir = DirectionSpecifier.generateDirection();
        OperandType firstType = Identifier.identifyOperand(firstOperand);
        OperandType secondType = Identifier.identifyOperand(secondOperand);
        if(secondType == OperandType.CONSTANT){
            secondOperand = Controller.model.getSymbol(secondOperand).getValue().toString();
            if (Identifier.isWordNumberConstant(secondOperand)){
                secondType = OperandType.INMEDIATE;
            }
            System.out.println(secondType+","+secondOperand);
        }
        Code code = Codes.get(firstType+","+secondType);
        return code.generateCode(firstOperand, secondOperand);
    }
}
