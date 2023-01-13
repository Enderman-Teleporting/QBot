package love.simbot.example.tools;

import java.io.*;
import java.util.UUID;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;

public class ImageUtil {
    public static String toString(String imgSource) {
        InputStream in;
        byte[] data;
        try {
            in = new FileInputStream(imgSource);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            return "诶,出了点小错诶...";
        }
        Base64Encoder encoder = new Base64Encoder();
        return encoder.encode(data);
    }

    public static void toImage(String base64, UUID uuid) throws IOException {
        byte[] data = Base64Decoder.decode(base64);
        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                data[i] += 256;
            }
        }
        var out = new FileOutputStream("./cache/temp/images/" + uuid.toString() + ".jpg");
        out.write(data);
        out.flush();
        out.close();

    }
}
