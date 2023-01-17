package love.simbot.example.tools;

import cn.hutool.core.codec.Base64Decoder;

public class ImageUtil {
    public static byte[] toImage(String base64) {
        try {
            byte[] data = Base64Decoder.decode(base64);
            for (int i = 0; i < data.length; i++) {
                if (data[i] < 0) {
                    data[i] += 256;
                }
            }
            return data;
        }
        catch (Exception e) {
            return null;
        }
    }
}
