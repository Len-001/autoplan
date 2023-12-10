package com.github.task.bili.util.task;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.github.task.bili.model.task.BiliTaskInfo;
import com.github.task.bili.util.BiliWebUtil;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * B站直播送出即将过期的礼物
 *
 * @author srcrs
 */

@Log4j2
public class GiveGift {

    private final BiliWebUtil biliWebUtil;
    private final BiliTaskInfo taskInfo;

    public GiveGift(BiliWebUtil biliWebUtil, BiliTaskInfo taskInfo) {
        this.biliWebUtil = biliWebUtil;
        this.taskInfo = taskInfo;
    }

    /**
     * 获取一个直播间的room_id.
     *
     * @return String
     * @author srcrs
     * @since 2020-10-13
     */
    public String xliveGetRecommend() throws Exception {
//        JSONObject jsonObject = biliWebUtil.doGet("https://api.live.bilibili.com/relation/v1/AppWeb/getRecommendList");
        // 获取热榜直播间，原先的接口不稳定，换一个
        JSONObject jsonObject = biliWebUtil.doGet("https://api.live.bilibili.com/xlive/web-interface/v1/index/getHotRankList");
        if (jsonObject.getInt("code") == 0) {
            int index = RandomUtil.randomInt(0, jsonObject.getJSONObject("data").getInt("count") - 1);
            return jsonObject
                    .getJSONObject("data")
                    .getJSONArray("list")
                    .getJSONObject(index)
                    .getStr("roomid");
        } else {
            throw new Exception("获取热榜直播间信息失败：" + jsonObject);
        }
    }

    /**
     * B站获取直播间的uid.
     *
     * @param roomId up 主的 uid.
     * @return String.
     * @author srcrs.
     * @since 2020-10-13.
     */
    public String xliveGetRoomUid(String roomId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("room_id", roomId);
        return biliWebUtil.doGet("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom", params)
                .getJSONObject("data")
                .getJSONObject("room_info")
                .getStr("uid");
    }

    /**
     * 根据 uid 获取其 roomid
     *
     * @param mid 即 uid.
     * @return String 返回一个直播间id.
     * @author srcrs.
     * @since 2020-11-20.
     */
    public String getRoomInfoOld(String mid) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", mid);
        return biliWebUtil.doGet("https://api.bilibili.com/x/space/acc/info", params)
                .getJSONObject("data")
                .getJSONObject("live_room")
                .getStr("roomid");
    }

    /**
     * B站直播获取背包礼物.
     *
     * @return JsonArray
     * @author srcrs
     * @since 2020-10-13
     */
    public JSONArray xliveGiftBagList() throws Exception {
        JSONObject obj = biliWebUtil.doGet("https://api.live.bilibili.com/xlive/web-room/v1/gift/bag_list")
                .getJSONObject("data");
        return obj.getJSONArray("list");
    }

    /**
     * B站直播送出背包的礼物.
     *
     * @return JsonObject
     * @author srcrs
     * @since 2020-10-13
     */
    public JSONObject xliveBagSend(Map<String, Object> params) throws Exception {
        params.put("uid", taskInfo.getDedeuserid());
        params.put("csrf", taskInfo.getBiliJct());
        params.put("send_ruid", "0");
        params.put("storm_beat_id", "0");
        params.put("price", "0");
        params.put("platform", "pc");
        params.put("biz_code", "live");
        return biliWebUtil.doPost("https://api.live.bilibili.com/gift/v2/live/bag_send", params);
    }

    /**
     * 获取一个包含 uid , RooId , msg 的 map 对象.
     *
     * @param giveGiftRoomID 送礼的直播间id，0为随机一个直播间
     * @return JsonObject 返回一个包含 uid 和 RooId 的 json 对象.
     */
    public Map<String, String> getuidAndRid(String giveGiftRoomID) throws Exception {
        Map<String, String> result = new HashMap<>();
        /* 直播间 id */
        String roomId;
        /* 直播间 uid 即 up 的 id*/
        String uid;
        if (!"0".equals(giveGiftRoomID)) {
            /* 获取指定up的id */
            uid = giveGiftRoomID;
            roomId = getRoomInfoOld(uid);
            String status = "0";
            if (status.equals(roomId)) {
                result.put("msg", String.format("自定义up %s 无直播间，获取随机直播间", uid));
                /* 随机获取一个直播间 */
                roomId = xliveGetRecommend();
                uid = xliveGetRoomUid(roomId);
            } else {
                result.put("msg", String.format("自定义up %s 的直播间", uid));
            }
        } else {
            /* 随机获取一个直播间 */
            roomId = xliveGetRecommend();
            uid = xliveGetRoomUid(roomId);
            result.put("msg", "随机直播间");
        }
        result.put("uid", uid);
        result.put("roomid", roomId);
        return result;
    }

}
