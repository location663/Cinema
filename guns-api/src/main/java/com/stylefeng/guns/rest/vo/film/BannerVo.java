/**
 * Created by Intellij IDEA
 * User:cookie
 * Date:2019/11/27
 * Time:20:30
 **/
package com.stylefeng.guns.rest.vo.film;

import lombok.Data;

import java.io.Serializable;

@Data
public class BannerVo implements Serializable {
    private Integer bannerId;
    private String bannerAddress;
    private String bannerUrl;
}
