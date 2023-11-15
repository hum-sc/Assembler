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
}
