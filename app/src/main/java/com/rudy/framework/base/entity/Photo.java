package com.rudy.framework.base.entity;

import java.io.Serializable;

/**
 * Created by RudyJun on 2016/12/5.
 */
public class Photo implements Serializable{

    private int photoId;
    private String photo;
    private String uploadDate;

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}
