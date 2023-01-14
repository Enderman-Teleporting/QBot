package love.simbot.example.tools;

import java.io.*;

import cn.hutool.core.codec.Base64Decoder;

public class ImageUtil {
    public static void toImage(String base64, String uuid) throws IOException {
        byte[] data = Base64Decoder.decode(base64);
        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                data[i] += 256;
            }
        }
        File file = new File("./cache/temp/images/");
        if (!file.exists()) {
            file.mkdirs();
        }
        var out = new FileOutputStream("./cache/temp/images/" + uuid+ ".jpg");
        out.write(data);
        out.close();

    }
}
