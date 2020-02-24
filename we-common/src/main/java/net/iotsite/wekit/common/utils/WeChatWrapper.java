package net.iotsite.wekit.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class WeChatWrapper implements HttpUtils.ResponseWrapper<Result> {

    @Override
    public Result wrapper(HttpUtils.HttpResponse response) {
        if (!response.isSuccess()) {
            return Result.failed(response.getCode(), "inner:" + response.getMessage());
        }
        String result = response.getResult();
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(result);
        } catch (Exception e) {
            log.error("wrapper:JSON转换失败:{}" + result);
            Result.failed("wrapper:返回结果数据格式异常");
        }
        if (null == jsonObject) {
            return Result.failed("wrapper:返回结果为空");
        }
        Integer errCode = jsonObject.getInteger("errcode");
        String errMsg = jsonObject.getString("errmsg");
        if (null == errCode) {
            return Result.success(jsonObject);
        }
        if (errCode.equals(0)) {
            return Result.success("OK");
        }
        return Result.failed(errCode, "wrapper:" + errMsg);
    }
}
