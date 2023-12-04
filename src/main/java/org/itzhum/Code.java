package org.itzhum;

import org.itzhum.logic.Identifier;
import org.itzhum.types.ComponentType;
import org.itzhum.types.OperandType;
import org.itzhum.types.Reg;
import org.itzhum.types.Regs3;

import java.io.Console;

public class Code {
    private final String code;
    private String reg = "";
    private int shiftMin = 0;
    private int shiftMax = 0;
    private int inmMin = 0;
    private int inmMax = 0;
    private String dirMode = "";

    public Code(String code, String address, String shiftSize, String inmediateSize) {
        this.code = code;

        if (address.matches("mod[01]{3}rm")){
            this.reg = address.substring(3,6);
        }
        dirMode = address;
        if(shiftSize.length()==2){
            this.shiftMin = Integer.parseInt(shiftSize.substring(0,1));
            this.shiftMax = Integer.parseInt(shiftSize.substring(1,2));
        }else{
            this.shiftMin = Integer.parseInt(shiftSize);
            this.shiftMax = Integer.parseInt(shiftSize);
        }
        if(inmediateSize.length()==2){
            this.inmMin = Integer.parseInt(inmediateSize.substring(0,1));
            this.inmMax = Integer.parseInt(inmediateSize.substring(1,2));
        }
        else{
            this.inmMin = Integer.parseInt(inmediateSize);
            this.inmMax = Integer.parseInt(inmediateSize);
        }

    }

    public Code(String code) {
        this.code = code;
    }

    public String generateCode(){
        return code;
    }
    public String generateCode(String operand) throws Exception {
        String lcode = code;
        OperandType operandType = Identifier.identifyOperand(operand);
        String inm = "";
        String address = "";
        String shift = "";
        String lreg = reg;

        switch (operandType) {
            case INMEDIATE -> {
                if(Identifier.isByteNumberConstant(operand)){
                    lcode = lcode.replace('w','0');
                    lcode = lcode.replace('W','0');
                } else if(Identifier.isWordNumberConstant(operand)){
                    lcode = lcode.replace('w','1');
                    lcode = lcode.replace('W','1');
                }
                int number = Controller.componentToNumber(operand);
                inm = getInm(number);
            }
            case REGISTERBYTE -> {
                lcode = lcode.replace('w','0');
                lcode = lcode.replace('W','0');
                lcode = lcode.replace('s','0');
                lcode = lcode.replace('S','0');
                if (lreg.isEmpty()) lreg = Reg.getReg(operand).toString();
                address = getAddress(operand, lreg);
            } case REGISTERWORD -> {
                lcode = lcode.replace('w','1');
                lcode = lcode.replace('W','1');
                lcode = lcode.replace('s','0');
                lcode = lcode.replace('S','0');
                if (lreg.isEmpty()) lreg = Reg.getReg(operand).toString();
                address = getAddress(operand, lreg);
            } case MEMORYWORD -> {
                lcode = lcode.replace('w','1');
                lcode = lcode.replace('W','1');
                lcode = lcode.replace('s','0');
                lcode = lcode.replace('S','0');
                address = getAddress(operand, lreg);
            } case MEMORY, MEMORYBYTE -> {
                lcode = lcode.replace('w','0');
                lcode = lcode.replace('W','0');
                lcode = lcode.replace('s','0');
                lcode = lcode.replace('S','0');
                if (lreg.isEmpty()) lreg = Reg.getReg(operand).toString();
                address = getAddress(operand, lreg);
            }
            case SEGMENTREGISTER -> {
                lcode = lcode.replace('w','0');
                lcode = lcode.replace('W','0');
                lcode = lcode.replace('s','0');
                lcode = lcode.replace('S','0');
                lreg = Regs3.getRegs3(operand).toString();
                address = getAddress(operand, lreg);
            } case TAG -> {
                int counterProgram = Controller.getCounterProgram();
                int tagDirection = Controller.model.getSymbol(operand).getDirection();

                int jump =  tagDirection - counterProgram;

                shift = getTagShift(jump);
            }
        }


        return lcode+address+shift+inm;
    }

    private String getInm(int number) {
        if(number < 256){
            String inm = Integer.toBinaryString(number);
            if(inm.length() < 8){
                inm = "0".repeat(8-inm.length())+inm;
            }
            return inm;
        }
        else{
            String inm = Integer.toBinaryString(number);
            if(inm.length() < 16){
                inm = "0".repeat(16-inm.length())+inm;
            }
            return inm;
        }
    }

    public String generateCode(String des, String fuente) throws Exception {
        OperandType destType = Identifier.identifyOperand(des);
        OperandType fuenteType = Identifier.identifyOperand(fuente);
        String lcode = code;
        String inm = "";

        boolean isDestWord = destType.toString().contains("WORD");
        boolean isFuenteWord = fuenteType.toString().contains("WORD");

        System.out.println("isDestWord "+des+": " + isDestWord+ " isFuenteWord "+fuente+": " + isFuenteWord);


        if(fuenteType == OperandType.INMEDIATE){
            //TODO:Checar cuando afecta s
            if(Identifier.isWordNumberConstant(fuente)){
                isFuenteWord = true;
            }
            if(Identifier.isByteNumberConstant(fuente)){
                isFuenteWord = false;
            }
            if(isDestWord && !isFuenteWord){
                lcode = lcode.replace('s','1');
                lcode = lcode.replace('S','1');
            }else{
                lcode = lcode.replace('s', '0');
                lcode = lcode.replace('S', '0');
            }

            int number = Controller.componentToNumber(fuente);
            inm = getInm(number);
        }
        String lreg;
        String addres ="";
        if(reg.isEmpty()){
            if(destType == OperandType.SEGMENTREGISTER){
                if(dirMode.matches(".+regs3.+")){
                    lreg = Regs3.getRegs3(des).toString();
                    addres = getAddress(fuente, lreg);
                }
            }else if(fuenteType == OperandType.SEGMENTREGISTER){
                if(dirMode.matches(".+regs3.+")){
                    lreg = Regs3.getRegs3(fuente).toString();
                    addres = getAddress(des,lreg);
                }
            }else if(destType == OperandType.REGISTERBYTE || destType == OperandType.REGISTERWORD ) {
                lreg = Reg.getReg(des).toString();
                addres = getAddress(fuente, lreg);

            }  else{
                lreg = Reg.getReg(fuente).toString();
                addres = getAddress(des, lreg);
            }
        }else{
            lreg = reg;
            addres = getAddress(des, lreg);
        }

        if(isDestWord || isFuenteWord){
            lcode = lcode.replace('w', '1');
            lcode = lcode.replace('W', '1');
        } else {
            lcode = lcode.replace('w', '0');
            lcode = lcode.replace('W','0');
        }
        return lcode+addres+inm;
    }

    private String getAddress(String operand, String lreg) throws Exception {
        String mod="", rm="", desp="";
        OperandType operandType = Identifier.identifyOperand(operand);
        if(operandType==OperandType.REGISTERBYTE || operandType==OperandType.REGISTERWORD){
            mod = "11";
            rm = Reg.getReg(operand).toString();
        } else if(operand.matches("\\[BX]")){
            mod = "00";
            rm = "111";
        } else if (operand.matches("\\[BX\\+SI]")){
            mod = "00";
            rm = "000";
        } else if (operand.matches("\\[BX\\+DI]")){
            mod = "00";
            rm = "001";
        } else if (operand.matches("\\[BP\\+SI]")){
            rm = "010";
            mod = "00";
        } else if (operand.matches("\\[BP\\+DI]")){
            rm = "011";
            mod = "00";
        } else if (operand.matches("\\[SI]")){
            rm = "100";
            mod = "00";
        } else if (operand.matches("\\[DI]")){
            rm = "101";
            mod = "00";
        }
        else if(operand.matches("\\[BX\\s*\\+\\s*\\d+]")){
            desp = operand.split("\\+")[1];
            desp = desp.trim();
            desp = desp.substring(0,desp.length()-1);
            desp = getShift(Integer.parseInt(desp));
            if(desp.length() == 8) mod = "01";
            else mod = "10";
            rm = "111";
        }
        else if(operand.matches("\\[BX\\s*\\+\\s*SI\\s*\\+\\s*\\d+]")){
            desp = operand.split("\\+")[2];
            desp = desp.trim();
            desp = desp.substring(0,desp.length()-1);
            desp = getShift(Integer.parseInt(desp));
            if(desp.length() == 8) mod = "01";
            else mod = "10";
            rm = "000";
        }
        else if(operand.matches("\\[BX\\s*\\+\\s*DI\\s*\\+\\s*\\d+]")){
            desp = operand.split("\\+")[2];
            desp = desp.trim();
            desp = desp.substring(0,desp.length()-1);
            desp = getShift(Integer.parseInt(desp));
            if(desp.length() == 8) mod = "01";
            else mod = "10";
            rm = "001";
        }
        else if(operand.matches("\\[BP\\s*\\+\\s*SI\\s*\\+\\s*\\d+]")){
            desp = operand.split("\\+")[2];
            desp = desp.trim();
            desp = desp.substring(0,desp.length()-1);
            desp = getShift(Integer.parseInt(desp));
            if(desp.length() == 8) mod = "01";
            else mod = "10";
            rm = "010";
        }
        else if(operand.matches("\\[BP\\s*\\+\\s*DI\\s*\\+\\s*\\d+]")){
            desp = operand.split("\\+")[2];
            desp = desp.trim();
            desp = desp.substring(0,desp.length()-1);
            desp = getShift(Integer.parseInt(desp));
            if(desp.length() == 8) mod = "01";
            else mod = "10";
            rm = "011";
        }
        else if(operand.matches("\\[SI\\s*\\+\\s*\\d+]")){
            desp = operand.split("\\+")[1];
            desp = desp.trim();
            desp = desp.substring(0,desp.length()-1);
            desp = getShift(Integer.parseInt(desp));
            if(desp.length() == 8) mod = "01";
            else mod = "10";
            rm = "100";
        }
        else if(operand.matches("\\[DI\\s*\\+\\s*\\d+]")){
            desp = operand.split("\\+")[1];
            desp = desp.trim();
            desp = desp.substring(0,desp.length()-1);
            desp = getShift(Integer.parseInt(desp));
            if(desp.length() == 8) mod = "01";
            else mod = "10";
            rm = "101";
        }
        else if(operand.matches("\\[BP\\s*\\+\\s*\\d+]")){
            desp = operand.split("\\+")[1];
            desp = desp.trim();
            desp = desp.substring(0,desp.length()-1);
            desp = getShift(Integer.parseInt(desp));
            if(desp.length() == 8) mod = "01";
            else mod = "10";
            rm = "110";
        }
        else if (operand.matches("\\[[a-zA-Z]\\w*]")){
            String symbol = operand.substring(1,operand.length()-1);
            if(Controller.model.findSymbol(symbol)) {
                int direction = Controller.model.getSymbol(symbol).getDirection();
                desp = getShift(direction);
                mod = "00";
                rm = "110";
            }
        } else if(Identifier.isValidSymbol(operand)){
            if(Controller.model.findSymbol(operand)){
                int direction = Controller.model.getSymbol(operand).getDirection();
                desp = getShift(direction);
                rm = "110";
                mod = "00";
            }
        }


        return mod+lreg+rm+desp;
    }

    private String getTagShift(int shift) throws Exception {
        String despString = Integer.toBinaryString(shift);
        int shiftAbs = Math.abs(shift);

        if((shiftMax == 0 && shift!= 0))throw new Exception("No se esperaba desplazamiento");
        if((shiftMax == 1 && shiftAbs > 128)) throw new Exception("Desplazamiento fuera de rango");

        if(shiftMin <= 1){
            if(shift < 0 && shiftAbs <= 128) despString = despString.substring(despString.length()-8);
            else despString = "0".repeat(8-despString.length())+despString;
        }

        return despString;
    }
    private String getShift(int shift) throws Exception {

        String despString = Integer.toBinaryString(shift);
        int shiftAbs = Math.abs(shift);

        if((shiftMax == 0 && shift!= 0))throw new Exception("No se esperaba desplazamiento");
        if((shiftMax == 1 && shiftAbs > 255)) throw new Exception("Desplazamiento fuera de rango");
        if((shiftMax == 2 && shiftAbs > 65535)) throw new Exception("Desplazamiento fuera de rango");


        if(shiftMin <= 1){
            if(shift < 0 && shiftAbs <= 128) despString = despString.substring(despString.length()-8);
            if(despString.length() < 8) despString = "0".repeat(8-despString.length())+despString;
        }
        if(shiftMin == 2){
            if(shift < 0 && shiftAbs <= 32768) despString = despString.substring(despString.length()-16);
            else if(despString.length()<16) despString = "0".repeat(16-despString.length())+despString;
        }
        if(shiftMax == 1){
            if(shift < 0){
                if(shiftAbs > 128) throw new Exception("Desplazamiento fuera de rango");
            }
            if(shift > 0){
                if(despString.length() < 8) despString = "0".repeat(8-despString.length())+despString;
            }
        }
        if (shiftMax == 2){
            if(shift < 0){
                if(shiftAbs > 32768) throw new Exception("Desplazamiento fuera de rango");
                if(shiftAbs <= 128){
                    if(shiftMin <= 1){
                        despString = despString.substring(despString.length()-8);
                    }else if (shiftMin == 2){
                        despString = despString.substring(despString.length()-16);
                    }
                }
                if (shiftAbs > 128){
                    despString = despString.substring(despString.length()-16);
                }
            }else if (shift > 0){
                if(shiftMin <= 1) {
                    if (despString.length() <= 8) despString = "0".repeat(8 - despString.length()) + despString;
                    else despString = "0".repeat(16 - despString.length()) + despString;
                }else if (shiftMin == 2){
                    if (despString.length() < 16) despString = "0".repeat(16 - despString.length()) + despString;
                }
            }

        }
        return despString;
    }

}
