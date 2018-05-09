package com.wiwikeyandroid.net;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.wiwikeyandroid.App;
import com.wiwikeyandroid.bean.ErrorResponse;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.utils.DesUtil;

import org.seny.android.utils.ALog;
import org.seny.android.utils.MD5Utils;
import org.seny.android.utils.MyToast;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpLoader {
    /**
     * 过滤重复请求。保存当前正在消息队列中执行的Request.key为对应的requestCode.
     */
    private static final HashMap<Integer, Request> mInFlightRequests =
            new HashMap<Integer, Request>();
    /**
     * 消息队列，全局使用一个
     */
    private static RequestQueue mRequestQueue = Volley.newRequestQueue(App.application);
    /**
     * 图片加载工具，自定义缓存机制
     */
    private static ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new LevelTwoLruBitmapCache(App.application));

    /**
     * 添加一个请求到请求队列
     *
     * @param requestCode 请求的唯一标识码
     * @return 返回该Request，方便链式编程
     */
    public static Request addRequest(Request<?> request, int requestCode) {
        if (mRequestQueue != null && request != null) {
            request.setTag(requestCode);
            mRequestQueue.add(request);
        }
        return mInFlightRequests.put(requestCode, request);//添加到正在处理请求中
    }

    /**
     * 取消请求
     *
     * @param requestCode 请求的唯一标识码
     */

    public static Request cancelRequest(int requestCode) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(requestCode);//从请求队列中取消对应的任务
        }
        return mInFlightRequests.remove(requestCode);//从集合中删除对应的任务
    }

    /**
     * 获取一个ImageLoader对象用于加载图片
     *
     * @return
     */
    public static ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * 从Map集合中构建一个get请求参数字符串
     *
     * @param param get请求map集合
     * @return get请求的字符串结构
     */
    private static String buildGetParam(Map<String, String> param) {

        StringBuilder buffer = new StringBuilder();
        if (param != null) {
            buffer.append("?");
            for (Map.Entry<String, String> entry : param.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    continue;
                }
                try {
                    buffer.append(URLEncoder.encode(key, "UTF-8"));
                    buffer.append("=");
                    buffer.append(URLEncoder.encode(value, "UTF-8"));
                    buffer.append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
        String str = buffer.toString();
        //去掉最后的&
        if (str.length() > 1 && str.endsWith("&")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 生成公共Header头信息
     *
     * @return
     */
    private static Map<String, String> generateHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        //        appkey		身份标识，服务器端进行识别
        //        udid		    客户端硬件标识
        //        os			ios& android& WM7
        //        osversion	    5.0
        //        appversion	app发布版本
        //        sourceid
        //        ver
        //        userid		登录完之后传客户端
        //        usersession	登录标识
        //        unique		app自动激活后服务器返回标识


        return headers;
    }

    /**
     * 发送GsonRequest请求
     *
     * @param method      请求方式
     * @param url         请求地址
     * @param params      请求参数,可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     * @param isCache     是否需要缓存本次响应的结果
     */

    private static void request(int method, String url, Map<String, String> params, Class<? extends RBResponse> clazz, final int requestCode, final ResponseListener listener, boolean isCache) {
        if (mInFlightRequests.get(requestCode) == null) {
            GsonRequest request = makeRBRequest(method, url + buildGetParam(params), null, clazz, requestCode, listener, isCache);
            addRequest(request, requestCode);
        } else {
            ALog.i("Hi guy,the request (RequestCode is " + requestCode + ")  is already in-flight , So Ignore!");
        }
    }

    /**
     * 发送get方式的GsonRequest请求,默认缓存请求结果
     *
     * @param url         请求地址
     * @param params      GET请求参数，拼接在URL后面。可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     */
    public static void get(String url, Map<String, String> params, Class<? extends RBResponse> clazz, final int requestCode, final ResponseListener listener) {
        request(Request.Method.GET, url, params, clazz, requestCode, listener, true);
    }

    /**
     * 发送get方式的GsonRequest请求
     *
     * @param url         请求地址
     * @param params      GET请求参数，拼接在URL后面。可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     * @param isCache     是否需要缓存本次响应的结果,没有网络时会使用本地缓存
     */
    public static void get(String url, Map<String, String> params, Class<? extends RBResponse> clazz, final int requestCode, final ResponseListener listener, boolean isCache) {
        request(Request.Method.GET, url, params, clazz, requestCode, listener, isCache);
    }

    /**
     * 发送post方式的GsonRequest请求，默认缓存请求结果
     *
     * @param url         请求地址
     * @param params      请求参数，可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     */
    public static void post(String url, Map<String, String> params, Class<? extends RBResponse> clazz, final int requestCode, final ResponseListener listener) {
        request(Request.Method.POST, url, params, clazz, requestCode, listener, true);
    }

    /**
     * 发送post方式的GsonRequest请求
     *
     * @param url         请求地址
     * @param params      请求参数，可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     * @param isCache     是否需要缓存本次响应的结果,没有网络时会使用本地缓存
     */
    public static void post(String url, Map<String, String> params, Class<? extends RBResponse> clazz, final int requestCode, final ResponseListener listener, boolean isCache) {
        request(Request.Method.POST, url, params, clazz, requestCode, listener, isCache);
    }


    /**
     * 初始化一个RBRequest
     *
     * @param method      请求方法
     * @param url         请求地址
     * @param params      请求参数，可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    监听器用来响应结果
     * @return 返回一个RBRequest对象
     */
    private static GsonRequest makeRBRequest(int method, String url, Map<String, String> params, Class<? extends RBResponse> clazz, int requestCode, ResponseListener listener, boolean isCache) {
        RequestListener requestListener = new RequestListener(requestCode, listener);
        GsonRequest request = new GsonRequest<RBResponse>(method, url, params, clazz, requestListener, requestListener, isCache) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //TODO 默认处理，如需自定义header，可重写
                return generateHeaders();
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy());//设置超时时间，重试次数，重试因子（1,1*2,2*2,4*2）等
        request.setTag(requestCode);//以requestCode为标记，方便移除
        return request;
    }

    /**
     * 成功获取到服务器响应结果的监听，供UI层注册
     */
    public interface ResponseListener {
        /**
         * 当成功获取到服务器响应结果的时候调用
         *
         * @param requestCode response对应的requestCode
         * @param response    返回的response
         */
        void onGetResponseSuccess(int requestCode, RBResponse response);

        /**
         * 网络请求失败，做一些释放性的操作，比如关闭对话框
         *
         * @param requestCode 请求码
         * @param error       异常详情
         */
        void onGetResponseError(int requestCode, VolleyError error);
    }


    /**
     * RequestListener，封装了Volley错误和成功的回调监，并执行一些默认处理，同时会将事件通过OnGetResponseListener抛到UI层
     */
    private static class RequestListener implements Response.ErrorListener, Response.Listener<RBResponse> {

        private ResponseListener listener;
        private int requestCode;

        public RequestListener(int requestCode, ResponseListener listener) {
            this.requestCode = requestCode;
            this.listener = listener;
        }

        //服务器返回数据成功，解析失败也会走此方法
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Request request = mInFlightRequests.remove(requestCode);//请求错误，从正在飞的集合中删除该请求
            if (listener != null) {
                listener.onGetResponseError(requestCode, error);
            }
            MyToast.show(App.application, "服务器请求失败！");
            //请求失败，解析本地缓存
            ALog.w("服务器请求失败，使用本地缓存数据！");
            getCacheResponse(request);
        }


        @Override
        public void onResponse(RBResponse response) {
            mInFlightRequests.remove(requestCode);//请求成功，从正在飞的集合中删除该请求
            if (response != null) {
                //执行通用处理，如果是服务器返回的ErrorResponse，直接提示错误信息并返回
                if ("error".equals(response.getResponse()) && response instanceof ErrorResponse) {
                    ErrorResponse errorResponse = (ErrorResponse) response;
                    MyToast.show(App.application, errorResponse.getError().getText());
                    return;
                }

                if (listener != null) {
                    listener.onGetResponseSuccess(requestCode, response);
                }
            }
        }


        /**
         * 从缓存中读取json数据
         *
         * @param request 要寻找缓存的request
         */
        private void getCacheResponse(Request request) {
            try {
                //获取缓存文件
                File cacheFile = new File(App.application.getCacheDir(), "" + MD5Utils.encode(request.getUrl()));
                StringWriter sw = new StringWriter();
                //读取缓存文件
                FileCopyUtils.copy(new FileReader(cacheFile), sw);
                if (request instanceof GsonRequest) {
                    //如果是GsonRequest，那么解析出本地缓存的json数据为RBResponse
                    GsonRequest gr = (GsonRequest) request;
                    //获取缓存文件解密。
                    String decrypt = DesUtil.decrypt(sw.toString(), DesUtil.DESKEY);
                    RBResponse response = (RBResponse) gr.getGson().fromJson(decrypt, gr.getClazz());
                    //传给onResponse，让前面的人用缓存数据
                    onResponse(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
