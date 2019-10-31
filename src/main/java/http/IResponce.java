package http;

/**
 * 回调接口
 */
public interface IResponce {
    void handleResult(String responseStr);
    void fail(Exception e);
}
