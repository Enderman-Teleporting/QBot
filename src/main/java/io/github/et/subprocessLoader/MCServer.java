package io.github.et.subprocessLoader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MCServer {
    public String name;
    public String workingDir;
    public String command;
    public Long group;
    public MCServer(String name,String workingDir,String command, Long group){
        this.name = name;
        this.workingDir = workingDir;
        this.command = command;
        this.group = group;
    }
}
