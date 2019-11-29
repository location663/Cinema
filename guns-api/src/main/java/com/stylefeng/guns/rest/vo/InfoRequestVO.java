package com.stylefeng.guns.rest.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InfoRequestVO implements Serializable {
    private String biography;
    private ActorsVO actors;
    private ImgVO imgVO;
}
