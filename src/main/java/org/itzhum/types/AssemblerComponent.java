package org.itzhum.types;

import org.itzhum.types.ComponentType;

public class AssemblerComponent {
    public String name;
    public ComponentType type;

    public AssemblerComponent(String name, ComponentType type){
        this.name = name;
        this.type = type;
    }
}
