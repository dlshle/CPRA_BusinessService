package org.wisc.business.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AjaxResponse
 * AjaxResponse defines the standard Ajax response for the front end.
 */
@Data
@NoArgsConstructor
public class AjaxResponse {
    private boolean isSuccess;
    private Integer status;
    private String message;
    private Object data;

    public static AjaxResponse success() {
        AjaxResponse result = new AjaxResponse();
        result.setSuccess(true);
        result.setStatus(200);
        result.setMessage("success");
        return result;
    }

    /**
     * success
     * success creates the standard success ajax response message to the
     * front end.
     * @param data data object
     * @return the AjaxResponse with data object wrapped
     */
    public static AjaxResponse success(Object data) {
        AjaxResponse result = new AjaxResponse();
        result.setSuccess(true);
        result.setStatus(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * error
     * error creates the standard error ajax response message to the front end.
     * @param code error status to indicate the error status
     * @param message error message to identify the error source
     * @return the AjaxResponse with error status wrapped
     */
    public static AjaxResponse error(int code, String message) {
        AjaxResponse result = new AjaxResponse();
        result.setSuccess(false);
        result.setStatus(code);
        result.setMessage(message);
        return result;
    }

    public static AjaxResponse notLoggedIn() {
        return AjaxResponse.error(400, "Login required for this action");
    }
}
