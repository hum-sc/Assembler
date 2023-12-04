package org.itzhum.types;

public enum Regs3 {
    ES,CS,SS,DS;

    public static Regs3 getRegs3(String des) {
        switch (des) {
            case "ES" -> {
                return ES;
            }
            case "CS" -> {
                return CS;
            }
            case "SS" -> {
                return SS;
            }
            case "DS" -> {
                return DS;
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public String toString() {
        switch (this){
            case ES -> {
                return "000";
            }
            case CS -> {
                return "001";
            }
            case SS -> {
                return "010";
            }
            case DS -> {
                return "011";
            }
            default -> {
                return null;
            }
        }
    }
}
