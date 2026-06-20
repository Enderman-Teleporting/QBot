package io.github.et;

import lombok.SneakyThrows;

public class HeartBeat implements Runnable {
    @SneakyThrows
    @Override
    public void run(){
        while(true){
            if(Main.bot==null){
                continue;
            }
            Thread.sleep(30000);
            Main.bot.getAsFriend().getAvatarUrl();
        }
    }
}
