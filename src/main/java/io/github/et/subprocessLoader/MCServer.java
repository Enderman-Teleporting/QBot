package io.github.et.subprocessLoader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MCServer {
    private String name;
    private String command;
    private Long group;
    public MCServer(String name,String command, Long group){
        this.name = name;
        this.command = command;
        this.group = group;
    }
}
