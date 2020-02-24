package net.iotsite.wekit.api.api;


import net.iotsite.wekit.api.api.impl.WeQRCodeRestImpl;
import net.iotsite.wekit.common.utils.Result;

public interface WeQRCodeRest {

    /**
     * 生成带参数二维码
     *
     * @param limit
     * @param param
     * @return
     */
    Result qrCode(WeQRCodeRestImpl.Limit limit, WeQRCodeRestImpl.Param param);
}
