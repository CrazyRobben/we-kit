package net.iotsite.wekit.api.dto;

import lombok.Getter;

@Getter
public class Limit {

    private boolean limit;

    private Integer limitTime;

    public Limit(Integer limitTime) {
        limit = true;
        this.limitTime = limitTime;
    }

    public Limit(boolean limit) {
        this.limit = limit;
    }
}
