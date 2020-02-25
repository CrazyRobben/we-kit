package net.iotsite.wekit.api.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

@Getter
public class Param {
    private Integer sceneId;

    private String sceneStr;

    public Param(int sceneId) {
        this.sceneId = sceneId;
    }

    public Param(String sceneStr) {
        this.sceneStr = sceneStr;
    }

    public boolean isStr() {
        return null != sceneStr;
    }

    public JSONObject getScene() {
        JSONObject actionInfo = new JSONObject();
        JSONObject scene = new JSONObject();
        if (isStr()) {
            scene.put("scene_str", sceneStr);
        } else {
            scene.put("scene_id", sceneId);
        }
        actionInfo.put("scene", scene);
        return actionInfo;
    }
}
