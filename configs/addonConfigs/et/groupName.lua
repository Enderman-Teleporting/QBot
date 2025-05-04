return{
    feature = "GroupName",
    guide={
        "允许群成员通过指令更改群名"
    },
    help="可以更改群名称，格式为“群名称 [你想要的群名称]”，请注意中间的空格",
    config={
        {name="groupName",type= { "java.lang.Boolean", "com.alibaba.fastjson.JSONArray" },nullable=false},
    }
}