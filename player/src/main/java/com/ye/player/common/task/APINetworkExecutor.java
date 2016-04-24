package com.ye.player.common.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class APINetworkExecutor {

/*    private static final String TAG = "APINetworkExecutor";

    private static UserInfoService userInfoService = new UserInfoService(HuaqianApplication.app.getApplicationContext());

    public static <T> NetworkAsync<T> post(String url, Map<String, Object> paramData, Class<T> resultType,
                                           CallBack<T> callback) {
        if (paramData == null) {
            paramData = new HashMap<String, Object>();
        }
        paramData = setUserIdLoginId(paramData);
        NetworkAsync<T> task = new NetworkAsync<T>(url, paramData, resultType, callback);
        task.execute();
        return task;
    }

    public static <T> void syncPost(String url, Map<String, Object> paramData, Class<T> resultType, CallBack<T> callback) {
        if (paramData == null) {
            paramData = new HashMap<String, Object>();
        }
        paramData = setUserIdLoginId(paramData);
        CustomHttpResponse result = APINetworkClient.post(url, paramData);
        processResult(result, callback, resultType);
    }

    private static Map<String, Object> setUserIdLoginId(Map<String, Object> paramData) {
        String userId = userInfoService.getUserId();
        String loginId = userInfoService.getLoginId();
        paramData.put("appVersion", Utils.getVersionCode());
        if (!StringUtil.isEmpty(loginId)) {
            paramData.put("loginId", loginId);
            paramData.put("userId", userId);
        }
        return paramData;
    }

    public static class NetworkAsync<T> extends AsyncTask<Void, Integer, CustomHttpResponse> implements CancelListener {

        private String url;

        private CallBack<T> callback;

        private Class<T> resultType;

        private Map<String, Object> paramData;

        public NetworkAsync(String url, Map<String, Object> paramData, Class<T> resultType, CallBack<T> callback) {
            this.url = url;
            this.callback = callback;
            this.resultType = resultType;
            this.paramData = paramData;
        }

        @Override
        protected CustomHttpResponse doInBackground(Void... params) {
            return APINetworkClient.post(url, paramData);
        }

        @Override
        protected void onPostExecute(CustomHttpResponse result) {
            super.onPostExecute(result);
            processResult(result, callback, resultType);
        }

        @Override
        public void cancel() {
            cancel(true);
        }
    }

    private static <T> void processResult(CustomHttpResponse result, CallBack<T> callback, Class<T> resultType) {
        if (result != null) {
            int statusCode = result.getStatusCode();
            if (statusCode == NetworkConstants.OK) {
                Log.d(TAG, result.getData());
                T data = null;
                try {
                    if (!StringUtil.isEmpty(result.getData())) {
                        data = JSONUtil.parseObject(result.getData(), resultType);
                    }
                } catch (Exception e) {
                    callback.onError(NetworkConstants.RESPONSE_DECODE_EXCEPTION, "网络返回数据解析异常，请稍后再试");
                    return;
                }
                if (data != null) {
                    Response response = (Response) data;
                    if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                        callback.onSuccess(data);
                        return;
                    }
                    callback.onError(response.getStatusCode(), response.getMsg());
                    if (response.getStatusCode() == Response.NOT_LOGIN) {
                        userInfoService.updateCurrentUserInfo(null);
                        LogoutUtil.noticeNotLogin();
                    }
                } else {
                    callback.onError(NetworkConstants.NETWORK_EXCEPTION, "未知错误，请稍后再试");
                }
            } else {
                switch (statusCode) {
                    case NetworkConstants.NETWORK_TIMEOUT_EXCEPTION: {
                        callback.onError(NetworkConstants.NETWORK_TIMEOUT_EXCEPTION, "网络请求超时，请检查网络后再试");
                        break;
                    }
                    case NetworkConstants.PARAM_ENCRPT_EXCEPTION: {
                        callback.onError(NetworkConstants.PARAM_ENCRPT_EXCEPTION, "参数加密错误，请稍后再试");
                        break;
                    }
                    case NetworkConstants.NETWORK_EXCEPTION: {
                        callback.onError(NetworkConstants.NETWORK_EXCEPTION, "网络异常，请检查网络后再试");
                        break;
                    }
                    default:
                        callback.onError(NetworkConstants.NETWORK_EXCEPTION, "未知错误，请稍后再试");
                        break;
                }
            }
        }
    }

    public static class CallBack<T> extends AsyncCallBacks.OneTwo<T, Integer, String> {
    }*/
}
