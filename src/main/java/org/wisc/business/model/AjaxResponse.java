package org.wisc.business.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AjaxResponse {
    private boolean isSuccess;
    private int code;
    private String message;
    private Object data;

    public static AjaxResponse success() {
        AjaxResponse result = new AjaxResponse();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    public static AjaxResponse success(Object data) {
        AjaxResponse result = new AjaxResponse();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static AjaxResponse error(int code, String message) {
        AjaxResponse result = new AjaxResponse();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
