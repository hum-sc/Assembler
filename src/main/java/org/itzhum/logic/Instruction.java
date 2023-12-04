package org.itzhum.logic;

import org.itzhum.Code;
import org.itzhum.types.OperandType;

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
                    OperandPairAccepted.add(firstOperand.toString() + "," + lSecondOperand.toString());
                    Codes.put(firstOperand.toString() + "," + lSecondOperand.toString(),code);
                }
            }
            if(secondOperand == OperandType.MEMORY) {
                lSecondOperand = OperandType.MEMORYBYTE;
                if (firstOperand != OperandType.MEMORY) {
                    OperandPairAccepted.add(lFirstOperand.toString() + "," + secondOperand.toString());
                    Codes.put(lFirstOperand.toString() + "," + secondOperand.toString(),code);
                }
            }
            OperandPairAccepted.add(lFirstOperand.toString()+","+lSecondOperand.toString());
            Codes.put(lFirstOperand.toString()+","+lSecondOperand.toString(),code);
        }
        if (acceptWordByte){
            if(firstOperand == OperandType.REGISTER) lFirstOperand = OperandType.REGISTERWORD;
            if(secondOperand == OperandType.REGISTER) lSecondOperand = OperandType.REGISTERBYTE;

            if(firstOperand == OperandType.MEMORY) {
                lFirstOperand = OperandType.MEMORYWORD;
                if (secondOperand != OperandType.MEMORY) {
                    OperandPairAccepted.add(firstOperand.toString() + "," + lSecondOperand.toString());
                    Codes.put(firstOperand.toString() + "," + lSecondOperand.toString(),code);
                }
            }
            if(secondOperand == OperandType.MEMORY) {
                lSecondOperand = OperandType.MEMORYBYTE;
                if (firstOperand != OperandType.MEMORY) {
                    OperandPairAccepted.add(lFirstOperand.toString() + "," + secondOperand.toString());
                    Codes.put(lFirstOperand.toString() + "," + secondOperand.toString(),code);
                }
            }

            OperandPairAccepted.add(lFirstOperand.toString()+","+lSecondOperand.toString());
            Codes.put(lFirstOperand.toString()+","+lSecondOperand.toString(),code);
        }
        if(firstOperand == OperandType.MEMORY && secondOperand == OperandType.MEMORY){
            OperandPairAccepted.add(firstOperand.toString()+","+secondOperand.toString());
            Codes.put(firstOperand.toString()+","+secondOperand.toString(),code);
        }
        if (acceptWordWord){
            if (firstOperand== OperandType.REGISTER) lFirstOperand = OperandType.REGISTERWORD;
            if (secondOperand== OperandType.REGISTER) lSecondOperand = OperandType.REGISTERWORD;
            if (firstOperand== OperandType.MEMORY) {
                lFirstOperand = OperandType.MEMORYWORD;
                if (secondOperand!= OperandType.MEMORY){
                    OperandPairAccepted.add(firstOperand.toString()+","+lSecondOperand.toString());
                    Codes.put(firstOperand.toString()+","+lSecondOperand.toString(),code);
                }
            }
            if (secondOperand== OperandType.MEMORY){
                lSecondOperand = OperandType.MEMORYWORD;
                if (firstOperand!= OperandType.MEMORY) {
                    OperandPairAccepted.add(lFirstOperand.toString()+","+secondOperand.toString());
                    Codes.put(lFirstOperand.toString()+","+secondOperand.toString(),code);
                }
            }
            OperandPairAccepted.add(lFirstOperand.toString()+","+lSecondOperand.toString());
            Codes.put(lFirstOperand.toString()+","+lSecondOperand.toString(),code);
        }
        if(!acceptWordByte && !acceptByteByte && !acceptWordWord) {
            OperandPairAccepted.add(firstOperand.toString()+","+secondOperand.toString());
            Codes.put(firstOperand.toString()+","+secondOperand.toString(),code);
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

    public boolean checkSintax(OperandType firstOperand, OperandType secondOperand) throws Exception {
        if (!hasTwoOperands) {
            String errorMsg = "Instruccion " + name + " necesita ";
            if (hasNoOperand) {
                errorMsg += "ningun operando";
                if (hasOneOperand) errorMsg += " o ";
            }
            if (hasOneOperand) errorMsg += "un operando";
            throw new Exception(errorMsg);
        } else if (!OperandPairAccepted.contains(firstOperand.toString() + "," + secondOperand.toString())) {
            StringBuilder errorMsg = new StringBuilder("Instruccion " + name + " necesita una combinacion de operandos ");
            for (String op : OperandPairAccepted) {
                errorMsg.append(op).append(" o ");
            }
            errorMsg.append(" pero se recibio ").append(firstOperand.toString()).append(",").append(secondOperand.toString());
            throw new Exception(errorMsg.toString());
        }
        return hasTwoOperands && OperandPairAccepted.contains(firstOperand.toString() + "," + secondOperand.toString());
    }


    public String encode(){
        //String dir = DirectionSpecifier.generateDirection();
        Code code = Codes.get("");
        return code.generateCode();
    }

    public String encode(String operand) throws Exception {
        //String dir = DirectionSpecifier.generateDirection();
        String operandType = Identifier.identifyOperand(operand).toString();
        Code code = Codes.get(operandType);
        return code.generateCode(operand);
    }

    public String encode(String firstOperand, String secondOperand) throws Exception {
        //String dir = DirectionSpecifier.generateDirection();
        String firstType = Identifier.identifyOperand(firstOperand).toString();
        String secondType = Identifier.identifyOperand(secondOperand).toString();

        Code code = Codes.get(firstType+","+secondType);
        return code.generateCode(firstOperand, secondOperand);
    }

    public void printCodes(){
        for (String key: Codes.keySet()
             ) {
            System.out.println(key);
        }
    }
}
