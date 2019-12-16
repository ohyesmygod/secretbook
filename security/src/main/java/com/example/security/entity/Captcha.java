package com.example.security.entity;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class Captcha {

    private String code;

    private LocalDateTime expireTime;

    private BufferedImage image;

    public Captcha(String code, int expireIn){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public Captcha(BufferedImage image, String code, int expireIn){
        this.image = image;
        this.code = code;
        this.image = image;
    }

    public Captcha(String code, LocalDateTime expireTime){
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
