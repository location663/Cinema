/**
 * Created by Intellij IDEA
 * User:cookie
 * Date:2019/11/28
 * Time:21:03
 **/
package com.stylefeng.guns.rest.vo.film;

import lombok.Data;

import java.io.Serializable;
@Data
public class YearVo implements Serializable {
    private String yearId;
    private String yearName;
    private boolean active;
}
