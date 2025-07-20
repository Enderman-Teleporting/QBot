package io.github.et.messager;

import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.et.tools.Ranking;
import io.github.et.utils.json.FeatureInUse;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

@SuppressWarnings("unused")
public class Interactions extends SimpleListenerHost {
    private static HashMap<Long, Ranking> bonk = new HashMap<>();
    private static HashMap<Long, Ranking> patCat = new HashMap<>();
    private static HashMap<Long, Ranking> patPaimon = new HashMap<>();
    private static HashMap<Long, Ranking> patCreeper = new HashMap<>();
    private static HashMap<Long, Ranking> patDog = new HashMap<>();
    private static HashMap<Long, Ranking> patFrog = new HashMap<>();

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Exception occurred when having an interaction, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent", exception);
    }

    @EventHandler
    public void interact(GroupMessageEvent event) throws IOException {
        if (FeatureInUse.isInUse("Interact", event.getSubject().getId())) {
            Random rand = new Random();
            MessageChain msg = event.getMessage();
            if (msg.size() >= 2) {
                if (msg.get(0) instanceof PlainText text && msg.get(1) instanceof At at) {
                    switch (text.getContent().trim()) {
                        case "敲" -> {
                            if (!bonk.containsKey(event.getSubject().getId())) {
                                bonk.put(event.getSubject().getId(), new Ranking("被敲榜"));
                            }
                            int times = rand.nextInt(1, 1001);
                            bonk.get(event.getSubject().getId()).add(at.getTarget(), times);
                            ExternalResource ex = ExternalResource.create(Interactions.class.getResource("io/github/et/memes/bonk.gif").openStream());
                            Image img = ExternalResource.uploadAsImage(ex, event.getSubject());
                            MessageChain reply = new MessageChainBuilder().append(new PlainText("你狠狠地敲了"))
                                    .append(new At(at.getTarget()))
                                    .append(new PlainText(" " + times + "下"))
                                    .append(img)
                                    .build();

                            event.getSubject().sendMessage(reply);
                        }
                        case "摸摸猫" -> {
                            if (!patCat.containsKey(event.getSubject().getId())) {
                                patCat.put(event.getSubject().getId(), new Ranking("摸猫榜"));
                            }
                            int times = rand.nextInt(1, 1001);
                            patCat.get(event.getSubject().getId()).add(at.getTarget(), times);
                            ExternalResource ex = ExternalResource.create(Interactions.class.getResource("io/github/et/memes/patCat.gif").openStream());
                            Image img = ExternalResource.uploadAsImage(ex, event.getSubject());
                            MessageChain reply = new MessageChainBuilder().append(new PlainText("你摸了"))
                                    .append(new At(at.getTarget()))
                                    .append(new PlainText(" 的猫猫伙伴" + times + "次"))
                                    .append(img)
                                    .build();
                            event.getSubject().sendMessage(reply);
                        }
                        case "摸摸狗" -> {
                            if (!patDog.containsKey(event.getSubject().getId())) {
                                patDog.put(event.getSubject().getId(), new Ranking("摸汪榜"));
                            }
                            int times = rand.nextInt(1, 1001);
                            patDog.get(event.getSubject().getId()).add(at.getTarget(), times);
                            ExternalResource ex = ExternalResource.create(Interactions.class.getResource("io/github/et/memes/patDog.gif").openStream());
                            Image img = ExternalResource.uploadAsImage(ex, event.getSubject());
                            MessageChain reply = new MessageChainBuilder().append(new PlainText("你摸了"))
                                    .append(new At(at.getTarget()))
                                    .append(new PlainText(" 家的旺柴" + times + "下"))
                                    .append(img)
                                    .build();
                            event.getSubject().sendMessage(reply);
                            if (rand.nextInt(1, 101) == 1) {
                                event.getSubject().sendMessage("狗勾跳起来咬了你一口");
                            }
                        }
                        case "摸摸派蒙" -> {
                            if (!patPaimon.containsKey(event.getSubject().getId())) {
                                patPaimon.put(event.getSubject().getId(), new Ranking("摸派蒙榜"));
                            }
                            int times = rand.nextInt(1, 1001);
                            patPaimon.get(event.getSubject().getId()).add(at.getTarget(), times);
                            ExternalResource ex = ExternalResource.create(Interactions.class.getResource("io/github/et/memes/patPaimon.gif").openStream());
                            Image img = ExternalResource.uploadAsImage(ex, event.getSubject());
                            MessageChain reply = new MessageChainBuilder().append(new PlainText("你把"))
                                    .append(new At(at.getTarget()))
                                    .append(new PlainText(" 旅行者的派蒙抓住狠狠rua了" + times + "下"))
                                    .append(img)
                                    .build();
                            event.getSubject().sendMessage(reply);
                        }
                        case "摸摸苦力怕" -> {
                            if (!patCreeper.containsKey(event.getSubject().getId())) {
                                patCreeper.put(event.getSubject().getId(), new Ranking("摸苦力怕榜"));
                            }
                            int times = rand.nextInt(1, 1001);
                            ExternalResource ex = ExternalResource.create(Interactions.class.getResource("io/github/et/memes/patCreeper.gif").openStream());
                            Image img = ExternalResource.uploadAsImage(ex, event.getSubject());
                            patCreeper.get(event.getSubject().getId()).add(at.getTarget(), times);
                            MessageChain reply = new MessageChainBuilder().append(new PlainText("你摸了"))
                                    .append(new At(at.getTarget()))
                                    .append(new PlainText(" 家的苦力怕" + times + "下并说：好可爱！"))
                                    .append(img)
                                    .build();
                            event.getSubject().sendMessage(reply);
                            if (rand.nextBoolean()) {
                                event.getSubject().sendMessage("苦力怕爆炸了,把你带走了");
                            }
                        }
                        case "摸摸青蛙" -> {
                            if (!patFrog.containsKey(event.getSubject().getId())) {
                                patFrog.put(event.getSubject().getId(), new Ranking("摸青蛙榜"));
                            }
                            int times = rand.nextInt(1, 1001);
                            patFrog.get(event.getSubject().getId()).add(at.getTarget(), times);
                            ExternalResource ex = ExternalResource.create(Interactions.class.getResource("io/github/et/memes/patFrog.gif").openStream());
                            Image img = ExternalResource.uploadAsImage(ex, event.getSubject());
                            MessageChain reply = new MessageChainBuilder().append(new PlainText("你摸了"))
                                    .append(new At(at.getTarget()))
                                    .append(new PlainText(" 的青蛙" + times + "次"))
                                    .append(img)
                                    .build();
                            event.getSubject().sendMessage(reply);
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onMessage(GroupMessageEvent event) {
        if (FeatureInUse.isInUse("Interact", event.getSubject().getId())) {
            if (!bonk.containsKey(event.getSubject().getId())) {
                bonk.put(event.getSubject().getId(), new Ranking("被敲榜"));
            }
            if (!patCat.containsKey(event.getSubject().getId())) {
                patCat.put(event.getSubject().getId(), new Ranking("摸猫榜"));
            }
            if (!patDog.containsKey(event.getSubject().getId())) {
                patDog.put(event.getSubject().getId(), new Ranking("摸汪榜"));
            }
            if (!patPaimon.containsKey(event.getSubject().getId())) {
                patPaimon.put(event.getSubject().getId(), new Ranking("摸派蒙榜"));
            }
            if (!patCreeper.containsKey(event.getSubject().getId())) {
                patCreeper.put(event.getSubject().getId(), new Ranking("摸苦力怕榜"));
            }
            if (!patFrog.containsKey(event.getSubject().getId())) {
                patFrog.put(event.getSubject().getId(), new Ranking("摸青蛙榜"));
            }
            switch (event.getMessage().contentToString()){
                case "被敲榜" -> event.getSubject().sendMessage(bonk.get(event.getSubject().getId()).toRankingString(event.getSubject()));
                case "摸猫榜" -> event.getSubject().sendMessage(patCat.get(event.getSubject().getId()).toRankingString(event.getSubject()));
                case "摸青蛙榜" -> event.getSubject().sendMessage(patFrog.get(event.getSubject().getId()).toRankingString(event.getSubject()));
                case "摸汪榜" -> event.getSubject().sendMessage(patDog.get(event.getSubject().getId()).toRankingString(event.getSubject()));
                case "摸派蒙榜" -> event.getSubject().sendMessage(patPaimon.get(event.getSubject().getId()).toRankingString(event.getSubject()));
                case "摸苦力怕榜" -> event.getSubject().sendMessage(patCreeper.get(event.getSubject().getId()).toRankingString(event.getSubject()));
            }
        }
    }
}