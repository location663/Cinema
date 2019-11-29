/**
 * Created by IntelliJ IDEA.
 * User: Jql
 * Date  2019/11/29
 * Time  下午 8:01
 */

package com.stylefeng.guns.rest.common.exception;

public class CinemaQueryFailException extends Exception {

    private Integer status;
    private String msg;

    public CinemaQueryFailException() {
    }

    public CinemaQueryFailException(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
