package org.itzhum;

import org.itzhum.types.OperandType;

import java.util.HashSet;
import java.util.Set;

public class Instruction {
    private boolean hasNoOperand, hasOneOperand, hasTwoOperands;
    private boolean acceptByte, acceptWord, acceptByteByte, acceptWordByte, acceptWordWord;
    private String name;

    private Set<OperandType> OperandAccepted;
    private Set<String> OperandPairAccepted;

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

    }

    public void addOperandAccepted(OperandType operand){
        if(acceptByte){
            if(operand == OperandType.MEMORY) operand = OperandType.MEMORYBYTE;
            if(operand == OperandType.REGISTER) operand = OperandType.REGISTERBYTE;
            OperandAccepted.add(operand);
        }
        if(acceptWord){
            if(operand == OperandType.MEMORY) operand = OperandType.MEMORYWORD;
            if(operand == OperandType.REGISTER) operand = OperandType.REGISTERWORD;
            OperandAccepted.add(operand);
        }

        if(!acceptWord && !acceptByte)OperandAccepted.add(operand);
    }


    public void addPairOperandAccepted(OperandType firstOperand, OperandType secondOperand){
        OperandType lFirstOperand = firstOperand;
        OperandType lSecondOperand = secondOperand;
        if(acceptByteByte){
            if(firstOperand == OperandType.MEMORY) lFirstOperand = OperandType.MEMORYBYTE;
            if(secondOperand == OperandType.MEMORY) lSecondOperand = OperandType.MEMORYBYTE;
            if(firstOperand == OperandType.REGISTER) lFirstOperand = OperandType.REGISTERBYTE;
            if(secondOperand == OperandType.REGISTER) lSecondOperand = OperandType.REGISTERBYTE;
            OperandPairAccepted.add(lFirstOperand.toString()+","+lSecondOperand.toString());
        }
        if (acceptWordByte){
            if(firstOperand == OperandType.MEMORY) lFirstOperand = OperandType.MEMORYWORD;
            if(secondOperand == OperandType.MEMORY) lSecondOperand = OperandType.MEMORYBYTE;
            if(firstOperand == OperandType.REGISTER) lFirstOperand = OperandType.REGISTERWORD;
            if(secondOperand == OperandType.REGISTER) lSecondOperand = OperandType.REGISTERBYTE;
            OperandPairAccepted.add(lFirstOperand.toString()+","+lSecondOperand.toString());
        }

        if (acceptWordWord){
            if (firstOperand== OperandType.MEMORY) lFirstOperand = OperandType.MEMORYWORD;
            if (secondOperand== OperandType.MEMORY) lSecondOperand = OperandType.MEMORYWORD;
            if (firstOperand== OperandType.REGISTER) lFirstOperand = OperandType.REGISTERWORD;
            if (secondOperand== OperandType.REGISTER) lSecondOperand = OperandType.REGISTERWORD;
            OperandPairAccepted.add(lFirstOperand.toString()+","+lSecondOperand.toString());
        }
        if(!acceptWordByte && !acceptByteByte && !acceptWordWord) OperandPairAccepted.add(firstOperand.toString()+","+secondOperand.toString());
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

    public boolean checkSintax(OperandType firstOperand, OperandType secondOperand) throws Exception{
        if(!hasTwoOperands) {
            String errorMsg = "Instruccion " + name + " necesita ";
            if (hasNoOperand) {
                errorMsg += "ningun operando";
                if (hasOneOperand) errorMsg += " o ";
            }
            if (hasOneOperand) errorMsg += "un operando";
            throw new Exception(errorMsg);
        } else if (!OperandPairAccepted.contains(firstOperand.toString()+","+secondOperand.toString())){
            StringBuilder errorMsg = new StringBuilder("Instruccion " + name + " necesita una combinacion de operandos ");
            for(String op : OperandPairAccepted){
                errorMsg.append(op).append(" o ");
            }
            errorMsg.append(" pero se recibio ").append(firstOperand.toString()).append(",").append(secondOperand.toString());
            throw new Exception(errorMsg.toString());
        }
        return hasTwoOperands && OperandPairAccepted.contains(firstOperand.toString()+","+secondOperand.toString());
    }
}
