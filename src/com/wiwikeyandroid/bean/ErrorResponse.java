package com.wiwikeyandroid.bean;

/**
 * 服务器错误响应的封装对象
 * <br/>
 *   {<br/>
 *        "response": "error",<br/>
 *        "error": {<br/>
 *        "text": "用户名不存在"<br/>
 *           }<br/>
 *    }<br/>
 *
 */
public class ErrorResponse extends RBResponse {

    /**
     * text : 用户名不存在
     */

    private ErrorEntity error;

    public void setError(ErrorEntity error) {
        this.error = error;
    }

    public ErrorEntity getError() {
        return error;
    }

    public static class ErrorEntity {
        private String text;

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
