/**
 * Created by Intellij IDEA
 * User:cookie
 * Date:2019/11/28
 * Time:20:44
 **/
package com.stylefeng.guns.rest.util;

import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.stylefeng.guns.rest.vo.film.FilmInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mtime2VoTrans {
    public static List<FilmInfo> filmTrans(List<MtimeFilmT> mTime){
        ArrayList<FilmInfo> filmInfos = new ArrayList<>();
        for (MtimeFilmT mtimeFilmT : mTime) {
            Date filmTime = mtimeFilmT.getFilmTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //上映时间
            String format = sdf.format(filmTime);
            FilmInfo filmInfo = new FilmInfo(mtimeFilmT.getUuid(),mtimeFilmT.getFilmType(),mtimeFilmT.getImgAddress(),
                    mtimeFilmT.getFilmName(),mtimeFilmT.getFilmScore(),mtimeFilmT.getFilmPresalenum(),format,mtimeFilmT.getFilmBoxOffice()
            );
            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }
}
