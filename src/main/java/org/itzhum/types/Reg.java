package org.itzhum.types;

public enum Reg {
    AX, CX, DX, BX, SP, BP, SI, DI, AL, CL, DL, BL, AH, CH, DH, BH;

    @Override
    public String toString() {
        switch (this){
            case AX, AL -> {
                return "000";
            }
            case CX, CL -> {
                return "001";
            }
            case DX, DL -> {
                return "010";
            }
            case BX, BL -> {
                return "011";
            }
            case SP, AH -> {
                return "100";
            }
            case BP, CH -> {
                return "101";
            }
            case SI, DH -> {
                return "110";
            }
            case DI, BH -> {
                return "111";
            }
            default -> {
                return null;
            }
        }
    }
    public static Reg getReg(String reg){
        switch (reg){
            case "AX" -> {
                return AX;
            }
            case "CX" -> {
                return CX;
            }
            case "DX" -> {
                return DX;
            }
            case "BX" -> {
                return BX;
            }
            case "SP" -> {
                return SP;
            }
            case "BP" -> {
                return BP;
            }
            case "SI" -> {
                return SI;
            }
            case "DI" -> {
                return DI;
            }
            case "AL" -> {
                return AL;
            }
            case "CL" -> {
                return CL;
            }
            case "DL" -> {
                return DL;
            }
            case "BL" -> {
                return BL;
            }
            case "AH" -> {
                return AH;
            }
            case "CH" -> {
                return CH;
            }
            case "DH" -> {
                return DH;
            }
            case "BH" -> {
                return BH;
            } default -> {
                return null;
            }
        }
    }
}
