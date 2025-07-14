package io.github.et.subprocessLoader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MCServer {
    public String name;
    public String workingDir;
    public String command;
    public long group;
    public int rcon_port;
    public String rcon_password;
    public MCServer(String name,String workingDir,String command, long group,int rcon_port,String rcon_password){
        this.name = name;
        this.workingDir = workingDir;
        this.command = command;
        this.group = group;
        this.rcon_port=rcon_port;
        this.rcon_password=rcon_password;
    }
}
