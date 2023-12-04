package org.itzhum.types;

import org.itzhum.types.SizeType;
import org.itzhum.types.SymbolType;

import java.util.HexFormat;

public class Symbol<T> {
    private String name;
    private SymbolType type;
    private T value;

    private SizeType size;
    private Integer direction;

    public Symbol(String name, SymbolType type, T value, SizeType size, int direction){
        this.name = name;
        this.type = type;
        this.value = value;
        this.size = size;
        this.direction = direction;
    }

    public Symbol(String name, SymbolType type, T value, SizeType size){
        this.name = name;
        this.type = type;
        this.value = value;
        this.size = size;
        direction = null;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SymbolType getType() {
        return type;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public SizeType getSize() {
        return size;
    }

    public void setSize(SizeType size) {
        this.size = size;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
