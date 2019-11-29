package com.stylefeng.guns.rest.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class ActorsVO implements Serializable {
    private DirectorVO director;
    private List<ActorVO> actors;

}
