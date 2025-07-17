return{
    game="Wordle",
    cn="Wordle",
    guide={
        "Wordle游戏功能"
    },
    rule="输入wordle [字母数量(4-11)]开始游戏，注意空格。\n玩家需要猜测一个单词，如果这个单词中有字母和原来单词相同，则标位绿色。如果要猜的单词和猜测的单词中有同一个字母而位置不符，则标为黄色。你共有6次机会",
    config={
        {name="wordle",type= { "java.lang.Boolean" },nullable=false},
    }
}