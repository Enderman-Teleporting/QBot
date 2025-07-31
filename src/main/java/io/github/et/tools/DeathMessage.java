package io.github.et.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DeathMessage {
    public static final String REGEX = "\\w+(( was pricked to death)|( walked into a cactus while trying to escape .+)|( drowned)|( drowned while trying to escape .+)|( died from dehydration)|( died from dehydration while trying to escape .+)|( experienced kinetic energy)|( experienced kinetic energy while trying to escape .+)|( blew up)|( was blown up by .+)|( was blown up by .+ using .+)|( was killed by \\[Intentional Game Design])|( hit the ground too hard)|( hit the ground too hard while trying to escape .+)|( fell from a high place)|( fell off a ladder)|( fell off some vines)|( fell off some (sweeping|twisting) vines)|( fell off scaffolding)|( fell while climbing)|( was doomed to fall( by .+)?)|( was impaled on a stalagmite)|( was impaled on a stalagmite while fighting .+)|( was squashed by a falling (anvil|block))|( was skewered by a falling stalactite)|( went up in flames)|( walked into fire while fighting .+)|( burned to death)|( was burned to a crisp while fighting .+)|( went off with a bang)|( went off with a bang due to a firework fired from .+ by .+)|( tried to swim in lava)|( tried to swim in lava to escape .+)|( was struck by lightning)|( was struck by lightning while fighting .+)|( discovered the floor was lava)|( walked into the danger zone due to .+)|( was killed by magic)|( was killed by magic while trying to escape .+)|( was killed by .+ using .+)|( froze to death)|( was frozen to death by .+)|( was slain by .+)|( was stung to death( by .+ using .+)?)|( was obliterated by a sonically-charged shriek.*)|( was smashed by .+)|( was shot by .+)|( was pummeled by .+)|( was fireballed by .+)|( was shot by a skull from .+)|( starved to death)|( starved to death while fighting .+)|( suffocated in a wall( while fighting .+)?)|( was squished too much)|( was squashed by .+)|( left the confines of this world(.+)?)|( was poked to death by a sweet berry bush)|( was poked to death by a sweet berry bush while trying to escape .+)|( was killed (by .+ )?while trying to hurt .+)|( was impaled by .+)|( fell out of the world)|( didn't want to live in the same world as .+)|( withered away)|( withered away while fighting .+)|( died)|( died because of .+)|( was killed)|( was killed while fighting .+)|( was killed by even more magic))$";
    public static String getDeathMessage(String msg) {
        try {
            Pattern pattern = Pattern.compile(REGEX);
            Matcher matcher = pattern.matcher(msg.trim());

            if (matcher.find()) {
                return matcher.group();
            }
        } catch (PatternSyntaxException ignored) {}
        return null;
    }
}
