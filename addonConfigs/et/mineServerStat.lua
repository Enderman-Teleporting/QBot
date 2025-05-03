return{
    feature = "MineServerStat",
    guide={
        "MC查服功能"
    },
    help="可以用来查询MC服务器信息，格式“查服 [ip]:[端口]”或者“查服 [ip]”，注意当中的空格，冒号为英文冒号",
    config={
        {name="mineServerStat",type= { "java.lang.Boolean","com.alibaba.fastjson.JSONArray" },nullable=false}
    }
}