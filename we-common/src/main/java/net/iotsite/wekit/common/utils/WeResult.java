package net.iotsite.wekit.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class WeResult implements Serializable {

    public WeResult(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @JSONField(name = "errcode")
    private Integer errCode;

    @JSONField(name = "errmsg")
    private String errMsg;

    protected boolean success() {
        return errCode == 0;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
