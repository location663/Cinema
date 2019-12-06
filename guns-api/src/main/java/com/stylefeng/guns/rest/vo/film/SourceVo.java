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
public class SourceVo implements Serializable {
    private String sourceId;
    private String sourceName;
    private boolean active;
}
