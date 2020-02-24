package net.iotsite.wekit.message.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias(value = "xml")
public class Message {

    @XStreamAlias("ToUserName")
    protected String toUserName;

    @XStreamAlias("FromUserName")
    protected String fromUserName;

    @XStreamAlias("CreateTime")
    protected String createTime;

    @XStreamAlias("MsgType")
    protected String msgType;

    @XStreamAlias("MsgId")
    protected String msgId;


    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
