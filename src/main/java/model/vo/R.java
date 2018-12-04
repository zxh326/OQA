package model.vo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private static final Integer SUCCESS_STATUS = 200;
    private static final Integer ERROR_STATUS = -1;
    private static final String SUCCESS_MSG = "ok";
    
    public R() {
        super();
    }

    public R(int code) {
        super();
        setStatus(code);
    }

    public R(HttpStatus status) {
        super();
        setStatus(status.value());
        setMsg(status.getReasonPhrase());
    }
    
    public R success() {
        put("msg", SUCCESS_MSG);
        put("status", SUCCESS_STATUS);
        return this;
    }
    
    public R success(String msg) {
        put("msg", msg);
        put("status", SUCCESS_STATUS);
        return this;
    }
    
    public R error(String msg) {
        put("msg", msg);
        put("status", ERROR_STATUS);
        return this;
    }

    public R setData(String key, Object obj) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> data = (HashMap<String, Object>) get("data");
        if (data == null) {
            data = new HashMap<String, Object>();
            put("data", data);
        }
        data.put(key, obj);
        return this;
    }
    
    private void setStatus(int status) {
        put("status", status);
    }


    private void setMsg(String msg) {
        put("msg", msg);
    }

    public R setValue(String key, Object val) {
        put(key, val);
        return this;
    }
    
    /**
     * 返回JSON字符串
     */
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
