package com.scut.core;

import com.scut.constant.HttpStatus;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 729878935989080L;
    public AjaxResult(int code, String msg){
        super.put("code", code);
        super.put("msg", msg);
    }
    public AjaxResult(int code, String msg, Object data){
        super.put("code", code);
        super.put("msg", msg);
        if(data != null){
            super.put("data", data);
        }
    }
    public static AjaxResult success()
    {
        return AjaxResult.success("操作成功");
    }

    public static AjaxResult success(Object data)
    {
        return AjaxResult.success("操作成功", data);
    }
    public static AjaxResult success(String msg)
    {
        return AjaxResult.success(msg, null);
    }
    public static AjaxResult success(String msg, Object data)
    {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }
    public static AjaxResult error()
    {
        return AjaxResult.error("操作失败");
    }
    public static AjaxResult error(String msg)
    {
        return AjaxResult.error(msg, null);
    }
    public static AjaxResult error(String msg, Object data)
    {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }
    public static AjaxResult error(int code, String msg)
    {
        return new AjaxResult(code, msg, null);
    }
    @Override
    public AjaxResult put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }
}
