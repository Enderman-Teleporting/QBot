import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.utils.json.JsonBuilder;

public class TestMain {
    public static void main(String[] args) throws BotInfoNotFoundException, ClassNotFoundException {
        JsonBuilder.initAll();
        System.out.println(JSON.toJSONString(JsonBuilder.buildFullJson(), SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue));
    }
}
