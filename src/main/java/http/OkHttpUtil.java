package http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;

/**
 * 网络请求Util
 */

/**
 * 这是枚举，用于标识请求的方法
 */
enum METHOD {
    POST, GET, DELETE, PUT
}

public class OkHttpUtil {


    /**
     * 通用的调用方法
     *
     * @param method 什么方法来执行请求
     * @param url 请求的地址
     * @param paramMap 参数map
     * @param resp 执行之后的回调接口
     */
    private void baseRequest(Map<String, String> headMap, METHOD method, String url, Map<String, Object> paramMap, IResponce resp) {

        String paramJsonStr = JSONObject.toJSONString(paramMap);
        baseRequest(headMap, method, url, paramJsonStr, resp);
    }

    /**
     * 通用的请求方法
     * @param headParam 要放到请求头的参数
     * @param method 这个请求用什么方法执行
     * @param url 要请求的地址
     * @param paramJsonStr json格式的字符串参数
     * @param resp 响应的回调
     */
    public void baseRequest(Map<String, String> headParam, METHOD method, String url, String paramJsonStr, IResponce resp) {

        System.out.println("请求地址:" + url);
        System.out.println("请求方法:" + method);

        OkHttpClient client = new OkHttpClient.Builder().build();


        String respStr;

            System.out.println("这是请求json 参数   " + paramJsonStr);
            Request.Builder header = new Request.Builder()
                    .url(url)
                    .removeHeader("User-Agent")
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Accept", "application/json")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");

            if (headParam != null) {
                for (Map.Entry<String, String> entry : headParam.entrySet()) {
                    header.header(entry.getKey(), entry.getValue());
                }
            }

            Request request = header.build();

            Response execute;
            try {

                execute = client.newCall(request).execute();
                assert execute.body() != null;
                respStr = execute.body().string();
                //System.out.println("这是响应信息" + respStr);


                System.out.println(respStr.contains("请求成功"));

                resp.handleResult(respStr);

            } catch (Exception e) {
                e.printStackTrace();
                resp.fail(e);

            }


        }





}
