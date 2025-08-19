package io.github.et.tools;

import lombok.Getter;
import lombok.Setter;
import net.mamoe.mirai.contact.Group;

import java.util.HashMap;
import java.util.TreeMap;

@Getter
@Setter
public class Ranking {
    private HashMap<Long, Integer> rank = new HashMap<>();
    private String name;

    public Ranking(String name) {
        this.name = name;
    }
    public void add(long id,int by){
        if(rank.containsKey(id)){
            rank.put(id, rank.get(id)+by);
        }else {
            rank.put(id, by);
        }
    }
    public TreeMap<Long, Integer> sort() {
        TreeMap<Long, Integer> sortedMap = new TreeMap<>(
                (key1, key2) -> {
                    int valueCompare = rank.get(key2).compareTo(rank.get(key1));
                    if (valueCompare != 0) {
                        return valueCompare;
                    }
                    return key1.compareTo(key2);
                }
        );
        sortedMap.putAll(rank);

        return sortedMap;
    }

    public String toRankingString(Group subject) {
        StringBuilder sb = new StringBuilder(this.name + "\n");
        TreeMap<Long, Integer> sorted = sort();
        for (long i : sorted.keySet()) {
            sb.append(subject.get(i).getNick()).append("\t").append(sorted.get(i)).append("\n");
        }
        sb.append("\n");
        int total = 0;
        for (int i : sorted.values()) {
            total += i;
        }
        sb.append("总计:").append(total);
        return sb.toString();
    }
}