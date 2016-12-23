package rakshit.socketapiconnect;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SocketApiConnect {
    private static final MediaType FORM = MediaType.parse("multipart/form-data");
    private final OkHttpClient client;
    private final String flowId;
    private final String authkey;

    public SocketApiConnect(String flowId, String authKey) {
        this.client = new OkHttpClient();
        this.flowId = flowId;
        this.authkey = authKey;
    }

    /**
     * Returns the base URL for block calls
     *
     * @return string Base URL for block calls
     */
    private static final String getBaseUrl() {
        return "http://sokt.io/";
    }

    /**
     * Build a URL for a block call
     *
     * @param flowId Block to be called
     * @return string Generated URL
     */
    public static String flowUrlBuild(String flowId) {
        return SocketApiConnect.getBaseUrl() + "/" +flowId  ;
    }

    /**
     * Call a block
     *
     * @param body  Arguments to send to the block (Map)
     * @return Map
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Map call(Map body) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        Set<Map.Entry<String, Argument>> entrySet = body.entrySet();

        for (Map.Entry<String, Argument> entry : entrySet) {
            Argument argument = entry.getValue();
            if ("data".equals(argument.getType())) {
                builder.addFormDataPart(entry.getKey(), argument.getValue());
            } else {
                File file = new File(argument.getValue());
                if (file.exists() && file.isFile()) {
                    builder.addFormDataPart(entry.getKey(), file.getName(), RequestBody.create(MultipartBody.FORM, file));
                } else {
                    result.put("error", "File not exist or can't be read.");

                    return result;
                }
            }
        }

        MultipartBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(SocketApiConnect.flowUrlBuild(flowId))
                .addHeader("User-Agent", "SocketAPIConnect_Java")
                .addHeader("authkey", authkey)
                .post(requestBody)
                .build();

        try (Response response = this.client.newCall(request).execute()) {
            Gson gson = new Gson();

            Map<String, Object> map = gson.fromJson(response.body().string(), new TypeToken<Map<String, Object>>() {
            }.getType());

            if (response.code() != 200 || "false".equals(map.get("success"))) {
                result.put("error", map.get("error"));

                return result;
            } else {
                result.put("success", map.get("invocation_id"));
                return result;
            }
        }
    }
}