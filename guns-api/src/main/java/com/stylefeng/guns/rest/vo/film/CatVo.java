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
public class CatVo implements Serializable {
    private Integer catId;
    private String catName;
    private boolean isAcboolean;
}
