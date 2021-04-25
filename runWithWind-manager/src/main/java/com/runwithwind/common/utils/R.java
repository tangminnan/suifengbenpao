package com.runwithwind.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;

@ApiModel
public class R<T> extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "错误码", name = "错误码")
	private Integer code;
	@ApiModelProperty(value = "错误码描述", name = "错误码描述")
	private String msg;
	@ApiModelProperty(value = "数据对象", name = "数据对象")
	private Object data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public R() {
		put("code", 200);
		put("msg", "操作成功");
	}

	public static R error() {
		return error(500, "操作失败");
	}

	public static R error(String msg) {
		return error(500, msg);
	}

	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		r.put("data",null);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		r.put("data",null);
		return r;
	}

	public static R data(Object map) {
		R r = new R();
		r.put("data",map);
		return r;
	}

	public static R ok() {
		R r = new R();
		r.put("data",null);
		return r;
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
