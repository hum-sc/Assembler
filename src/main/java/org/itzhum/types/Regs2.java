package org.itzhum.types;

public enum Regs2 {
    ES, CS, SS, DS;

    @Override
    public String toString() {
        switch (this){
            case ES -> {
                return "00";
            }
            case CS -> {
                return "01";
            }
            case SS -> {
                return "10";
            }
            case DS -> {
                return "11";
            }
            default -> {
                return null;
            }
        }
    }
}
