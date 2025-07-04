return{
    feature="Reply",
    cn="AI消息回复",
    guide={
        "回复@消息,（群聊）及任何消息（私聊）功能",
        "URL是你使用的请求地址(API的URL),在model里面自定义自己所需要的模型(支持DeepSeek),请查看是否有相关权限",
        "Max_Message_Count是最大消息数,，GPT含有上下文，这个是记录上下文的最大条数"
    },
    help="支持群聊和私聊回复 想在群聊中触发请先at机器人",
    config={
        {name="URL",type= { "java.lang.String" },nullable=false},
        {name="APIKEY",type= { "java.lang.String" },nullable=false},
        {name="Max_Message_Count",type= { "java.lang.Integer" },nullable=false},
        {name="model",type= { "java.lang.String" },nullable=false},
        {name="reply",type= { "java.lang.Boolean" },nullable=false}
    }
}