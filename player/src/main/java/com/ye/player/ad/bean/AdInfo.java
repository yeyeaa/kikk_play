package com.ye.player.ad.bean;
public class AdInfo {
    private String adId;

    private String link;

    private String imageSoturce;

    private Long startTime;

    private Long duration;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageSoturce() {
        return imageSoturce;
    }

    public void setImageSoturce(String imageSoturce) {
        this.imageSoturce = imageSoturce;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

}
