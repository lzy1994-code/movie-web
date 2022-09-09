package com.bw.movie.action;

import com.bw.movie.rpc.api.ToolRpcService;
import com.bw.movie.rpc.pojo.AK;
import com.bw.movie.rpc.pojo.AppVersion;
import com.bw.movie.rpc.pojo.SystemMessages;
import com.bw.movie.rpc.vo.BannerVo;
import com.bw.movie.rpc.vo.RegionVo;
import com.bw.movie.util.BwJsonHelper;
import com.bw.movie.util.EncryptUtil;
import com.bw.movie.util.WebUtil;
import com.bw.pay.client.api.WeChatRpcService;
import com.bw.pay.client.pojo.ProductConstants;
import com.bw.pay.client.pojo.WeChatVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ws.rs.*;
import java.util.List;

/**
 * Created by xyj on 2018/7/19.
 */
@Path(value = "/tool")
public class ToolAction {

    private Logger logger = LoggerFactory.getLogger(ToolAction.class);

    @Resource
    private ToolRpcService toolRpcService;

    @Resource
    private WeChatRpcService weChatRpcService;

    /**
     * 意见反馈
     *
     * @param userId
     * @param content
     * @return
     */
    @POST
    @Path(value = "/v1/verify/recordFeedBack")
    @Produces("text/html;charset=UTF-8")
    public String recordFeedBack(
            @HeaderParam("userId") int userId,
            @FormParam("content") String content
    ) {
        try {
            int num = toolRpcService.recordFeedBack(userId, content);
            if (num == 0) {
                return BwJsonHelper.returnJSON("1001", "反馈失败");
            }
            return BwJsonHelper.returnJSON("0000", "反馈成功");
        } catch (Exception e) {
            logger.error("recordFeedBack：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }


    /**
     * 查询新版本
     *
     * @param ak
     * @return
     */
    @GET
    @Path(value = "/v1/findNewVersion")
    @Produces("text/html;charset=UTF-8")
    public String findNewVersion(
            @HeaderParam("ak") String ak) {
        try {
            if (ak == null || ak.equals("")) {
                return BwJsonHelper.returnJSON("1001", "ak不能为空");
            }

            AppVersion appVersion = toolRpcService.findNewAk();
            String ak2 = appVersion.getAk();

            AK newAk = new AK(ak2);
            AK oldAk = new AK(ak);

            if (oldAk.compare(newAk) == -1) {
                return BwJsonHelper.returnJSON("0000", "查询成功", "flag", 1, "downloadUrl", appVersion.getDownloadUrl());
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", "flag", 2);
        } catch (Exception e) {
            logger.error("findNewVersion：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 查询系统消息列表
     *
     * @param userId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v1/verify/findAllSysMsgList")
    @Produces("text/html;charset=UTF-8")
    public String findAllSysMsgList(@HeaderParam("userId") int userId, @QueryParam("page") int page, @QueryParam("count") int count) {
        logger.info("findAllSysMsgList：userId={},page={},count={}", userId, page, count);
        try {
            if (userId <= 0 || page <= 0 || count <= 0) {
                return BwJsonHelper.returnJSON("1001", "userId或page或count不能为0或不合法");
            }
            List<SystemMessages> systemMessagesList = toolRpcService.findAllSysMsgs(userId, page, count);
            if (systemMessagesList.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", systemMessagesList);
        } catch (Exception e) {
            logger.error("findAllMessages：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 系统消息读取状态修改
     *
     * @param id
     * @return
     */
    @GET
    @Path(value = "/v1/verify/changeSysMsgStatus")
    @Produces("text/html;charset=UTF-8")
    public String changeSysMsgStatus(@HeaderParam("userId") int userId, @QueryParam("id") int id) {
        logger.info("changeSysMsgStatus：userId={},id={}", userId, id);
        try {
            if (id <= 0) {
                return BwJsonHelper.returnJSON("1001", "id不能为0或不合法");
            }
            boolean boo = toolRpcService.sysMsgsStatusChange(userId, id);
            if (boo == true) {
                return BwJsonHelper.returnJSON("0000", "状态改变成功");
            } else {
                return BwJsonHelper.returnJSON("1001", "状态改变失败");
            }
        } catch (Exception e) {
            logger.error("statusChange：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }

    }

    /**
     * 查询用户当前未读消息数量
     *
     * @param userId
     * @return
     */
    @GET
    @Path(value = "/v1/verify/findUnreadMessageCount")
    @Produces("text/html;charset=UTF-8")
    public String findUnreadMessageCount(@HeaderParam("userId") int userId) {
        logger.info("findUnreadMessageCount：userId={}", userId);
        try {
            int num = toolRpcService.findSysMsgsStatus(userId);
            if (num >= 0) {
                return BwJsonHelper.returnJSON("0000", "查询成功", "count", num);
            } else {
                return BwJsonHelper.returnJSON("1001", "查询成功");
            }
        } catch (Exception e) {
            logger.error("findStatus：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 上传设备的token
     *
     * @param userId
     * @param token
     * @param os
     * @return
     */
    @POST
    @Path(value = "/v1/verify/uploadPushToken")
    @Produces("text/html;charset=UTF-8")
    public String uploadPushToken(
            @HeaderParam("userId") int userId,
            @FormParam("token") String token,
            @FormParam("os") int os) {
        logger.info("uploadPushToken：userId={},token={},os={}", userId, token, os);
        if (token == null || token.equals("")) {
            return BwJsonHelper.returnJSON("1001", "token不能为空");
        }
        try {
            int num = toolRpcService.uploadPushToken(userId, token, os);

            if (num > 0) {
                return BwJsonHelper.returnJSON("0000", "上传成功");
            }

            return BwJsonHelper.returnJSON("1001", "上传失败");
        } catch (Exception e) {
            logger.error("uploadPushToken", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }


    /**
     * 微信分享前置接口，获取分享所需参数
     *
     * @param time
     * @param sign
     * @return
     */
    @GET
    @Path(value = "/v1/wxShare")
    @Produces("text/html;charset=UTF-8")
    public String wxShare(
            @QueryParam("time") String time,
            @QueryParam("sign") String sign) {
        logger.info("wxShare：time={},sign={}", time, sign);
        try {
            if (time == null || time.equals("") || sign == null || sign.equals("")) {
                return BwJsonHelper.returnJSON("1001", "非法请求");
            }

            String str = time + "wxShare" + "movie";
            String newSign = WebUtil.MD5(str);
            if (!newSign.equals(sign)) {
                return BwJsonHelper.returnJSON("1001", "签名验证失败");
            }

            WeChatVo weChatInfo = weChatRpcService.getWeChatInfo(ProductConstants.PRODUCT_MOVIE);

            String appSecret = weChatInfo.getAppSecret();
            appSecret = EncryptUtil.encrypt(appSecret);
            return BwJsonHelper.returnJSON("0000", "分享成功", "appId", weChatInfo.getAppId(), "appSecret", appSecret);
        } catch (Exception e) {
            logger.error("wxShare", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }

    }

    /**
     * 加密（供测试同学使用）
     *
     * @param password
     * @return
     */
    @GET
    @Path(value = "/v1/encrypt")
    @Produces("text/html;charset=UTF-8")
    public String encrypt(
            @QueryParam("password") String password) {
        String pwd = EncryptUtil.encrypt(password);
        return BwJsonHelper.returnJSON("0000", "加密成功", "cipherText", pwd);
    }

    /**
     * 2.0 banner展示
     */
    @GET
    @Path(value = "/v2/banner")
    @Produces("text/html;charset=UTF-8")
    public String bannerShow() {
        try {
            List<BannerVo> bannerVos = toolRpcService.bannerShow();
            return BwJsonHelper.returnJSON("0000", "查询成功", bannerVos);
        } catch (Exception e) {
            logger.error("bannerShow", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 查询区域列表
     *
     * @return
     */
    @GET
    @Path(value = "/v2/findRegionList")
    @Produces("text/html;charset=UTF-8")
    public String findRegionList() {
        try {
            List<RegionVo> regionList = toolRpcService.findRegionList();
            return BwJsonHelper.returnJSON("0000", "查询成功", regionList);
        } catch (Exception e) {
            logger.error("findRegionList", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 排期时间列表
     *
     * @return
     */
    @GET
    @Path(value = "/v2/findDateList")
    @Produces("text/html;charset=UTF-8")
    public String findDateList() {
        try {
            List<String> dateList = toolRpcService.dateList();
            return BwJsonHelper.returnJSON("0000", "查询成功", dateList);
        } catch (Exception e) {
            logger.error("findDateList", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }

    }


}
