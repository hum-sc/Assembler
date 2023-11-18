package org.itzhum.types;

import java.lang.reflect.Type;

public enum ComponentType {
    PseudoInstruccion,
    Instruccion,
    RegistroBajo,
    RegistroCompleto,
    Etiqueta,
    CaracterConstante,
    ConstanteDecimalByte,
    ConstanteHexadecimalByte,
    ConstanteBinariaByte,
    ConstanteDecimalWord,
    ConstanteHexadecimalWord,
    ConstanteBinariaWord,
    Simbolo,
    Desconocido;

    public Type getJavaType(){
        return switch (this) {
            case ConstanteDecimalByte, ConstanteDecimalWord, ConstanteHexadecimalByte, ConstanteHexadecimalWord, ConstanteBinariaByte, ConstanteBinariaWord ->
                    Integer.class;
            case CaracterConstante, RegistroBajo, RegistroCompleto, Simbolo, Etiqueta, Desconocido, Instruccion, PseudoInstruccion ->
                    String.class;
            default -> null;
        };

    }


    @Override
    public String toString() {
        switch(this) {
            case Simbolo -> {
                return "Simbolo";
            }
            case ConstanteDecimalByte -> {
                return "Constante Decimal Byte";
            }
            case ConstanteHexadecimalByte -> {
                return "Constante Hexadecimal Byte";
            }
            case ConstanteBinariaByte -> {
                return "Constante Binaria Byte";
            }
            case ConstanteDecimalWord -> {
                return "Constante Decimal Word";
            }
            case ConstanteHexadecimalWord -> {
                return "Constante Hexadecimal Word";
            }
            case ConstanteBinariaWord -> {
                return "Constante Binaria Word";
            }
            case CaracterConstante -> {
                return "Caracter Constante";
            }
            case RegistroBajo -> {
                return "Registro Bajo";
            }
            case RegistroCompleto -> {
                return "Registro Completo";
            }
            case Etiqueta -> {
                return "Etiqueta";
            }
case Desconocido -> {
                return "Desconocido";
            }
            case Instruccion -> {
                return "Instruccion";
            }
            case PseudoInstruccion -> {
                return "Pseudo-Instruccion";
            }
            default -> {
                return "Desconocido";
            }
        }
    }
}
