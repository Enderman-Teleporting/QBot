return{
    feature="FreeTalk",
    parent="Reply",
    guide={
        "随意聊天功能",
        "在模型合适的情况下可以模拟真人在群内的聊天,role设置的是你对机器人的预设角色,需要详细描述,例如\"一名xx中学(具体化)学生\"",
        "不建议FreeTalk功能设置过多群,一两个就够了,API容易504"
    },
    help="自由聊天功能，机器人会模拟真人在群内聊天",
    config={
        {name="role",type= { "java.lang.String" },nullable=false},
        {name="freeTalk",type= { "java.lang.Boolean" },nullable=false}
    }
} 