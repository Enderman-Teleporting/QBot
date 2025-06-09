return{
    feature="Image",
    parent="Reply",
    guide={
        "绘图功能",
        "必须设置URL以及APIKEY，否则无法实现该功能"
    },
    help="绘图功能格式\"绘图 [提示词]\"，注意中间空格",
    config={
        {name="image",type= { "java.lang.Boolean" },nullable=false}
    }
} 