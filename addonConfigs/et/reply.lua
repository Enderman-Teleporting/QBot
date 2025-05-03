return{
    feature="Reply",
    guide={
        "回复@消息,（群聊）及任何消息（私聊）功能，绘图功能，必须设置URL以及APIKEY，否则无法实现该功能",
        "URL是你使用的请求地址(API的URL),在model里面自定义自己所需要的模型(支持DeepSeek),请查看是否有相关权限",
        "Max_Message_Count是最大消息数,，GPT含有上下文，这个是记录上下文的最大条数",
        "FreeTalk是随意聊天功能,在模型合适的情况下可以模拟真人在群内的聊天,role设置的是你对机器人的预设角色,需要详细描述,例如“一名xx中学(具体化)学生”",
        "不建议FreeTalk功能设置过多群,一两个就够了,API容易504",
        "PS:绘图和FreeTalk由于API原因未测试过"
    },
    help="支持群聊和私聊回复 想在群聊中触发请先at机器人 绘图功能格式“绘图 [提示词]”，注意中间空格",
    config={
        {name="URL",type= { "java.lang.String" },nullable=false},
        {name="APIKEY",type= { "java.lang.String" },nullable=false},
        {name="Max_Message_Count",type= { "java.lang.Integer" },nullable=false},
        {name="FreeTalk",type= { "java.lang.Boolean", "com.alibaba.fastjson.JSONArray" },nullable=false},
        {name="role",type= { "java.lang.String" },nullable=false},
        {name="model",type= { "java.lang.String" },nullable=false},
        {name="reply",type= { "java.lang.Boolean", "com.alibaba.fastjson.JSONArray" },nullable=false},
        {name="image",type= { "java.lang.Boolean", "com.alibaba.fastjson.JSONArray" },nullable=false},
    }
}