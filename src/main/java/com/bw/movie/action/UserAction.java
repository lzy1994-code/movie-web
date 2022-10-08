package com.bw.movie.action;

import com.alibaba.fastjson.JSONArray;
import com.bw.movie.rpc.api.UserRpcService;
import com.bw.movie.rpc.pojo.User;
import com.bw.movie.rpc.pojo.UserBuyTicketDetail;
import com.bw.movie.rpc.pojo.UserBuyTicketRecord;
import com.bw.movie.rpc.vo.*;
import com.bw.movie.util.*;
import com.bw.movie.vo.UserInfoForm;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.Form;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by xyj on 2018/7/19
 */
@Path(value = "/user")
public class UserAction {

    private Logger logger = LoggerFactory.getLogger(UserAction.class);

    @Resource
    private UserRpcService userRpcService;

    /**
     * 注册
     *
     * @param userInfoForm
     * @return
     */
    @POST
    @Path(value = "/v1/registerUser")
    @Produces("text/html;charset=UTF-8")
    public String registerUser(@Form UserInfoForm userInfoForm) {
        try {
            logger.info("registerUser：{}", userInfoForm.toString());
            if (userInfoForm.getPhone() == null || userInfoForm.getPhone().equals("") || userInfoForm.getPhone().length() != 11) {
                return BwJsonHelper.returnJSON("1001", "请正确填写手机号");
            }

            if (userInfoForm.getPwd() == null || userInfoForm.getPwd().equals("")) {
                return BwJsonHelper.returnJSON("1002", "密码不能为空");
            }

            if (!userInfoForm.getPwd().equals(userInfoForm.getPwd2())) {
                return BwJsonHelper.returnJSON("1002", "两次输入的密码不相同");
            }

            if (userInfoForm.getNickName() == null || userInfoForm.getNickName().equals("")) {
                return BwJsonHelper.returnJSON("1003", "昵称不能为空");
            }

            if (userInfoForm.getSex() <= 0) {
                return BwJsonHelper.returnJSON("1004", "请选择性别");
            }

            if (userInfoForm.getEmail() == null || userInfoForm.getEmail().equals("")) {
                return BwJsonHelper.returnJSON("1005", "邮箱不能为空");
            }

            if (!WebUtil.checkEmail(userInfoForm.getEmail())) {
                return BwJsonHelper.returnJSON("1005", "请输入正确的邮箱");
            }


            UserRegisterVo user = (UserRegisterVo) CopyBean.getBean(new UserRegisterVo(), userInfoForm);
            int num = userRpcService.register(user);

            if (num == 0) {
                return BwJsonHelper.returnJSON("1005", "注册失败,请联系管理员");
            }

            if (num == 2) {
                return BwJsonHelper.returnJSON("1005", "注册失败,手机号已存在");
            }

            if (num == 3) {
                return BwJsonHelper.returnJSON("1005", "注册失败,昵称已存在");
            }

            return BwJsonHelper.returnJSON("0000", "注册成功");
        } catch (Exception e) {
            logger.error("registerUser", e);
            return BwJsonHelper.returnJSON("1005", "注册失败,请联系管理员");
        }
    }

    /**
     * 登陆
     *
     * @param phone
     * @param pwd
     * @return
     */
    @POST
    @Path(value = "/v1/login")
    @Produces("text/html;charset=UTF-8")
    public String login(
            @HeaderParam("ak") String ak,
            @FormParam("phone") String phone,
            @FormParam("pwd") String pwd) {
        logger.info("login：phone={},pwd={}", phone, pwd);
        try {
            if (phone == null || phone.equals("")) {
                return BwJsonHelper.returnJSON("1001", "请输入手机号");
            }

            if (pwd == null || pwd.equals("")) {
                return BwJsonHelper.returnJSON("1001", "请输入密码");
            }

            UserLoginVo user = userRpcService.login(phone, pwd);

            if (user == null) {
                return BwJsonHelper.returnJSON("1001", "登陆失败,账号或密码错误");
            }

            return BwJsonHelper.returnJSON("0000", "登陆成功", user);
        } catch (Exception e) {
            logger.error("login：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 查询用户首页信息
     *
     * @param userId
     * @return
     */
    @GET
    @Path(value = "/v1/verify/findUserHomeInfo")
    @Produces("text/html;charset=UTF-8")
    public String findUserHomeInfo(
            @HeaderParam("userId") int userId) {
        try {
            UserInfoVo userHome = userRpcService.findUserHome(userId);
            if (userHome == null) {
                return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", userHome);
        } catch (Exception e) {
            logger.error("findUserHomeInfo：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 修改用户信息
     *
     * @param userInfoForm
     * @return
     */
    @POST
    @Path(value = "/v1/verify/modifyUserInfo")
    @Produces("text/html;charset=UTF-8")
    public String modifyUserInfo(@Form UserInfoForm userInfoForm) {
        try {
            logger.info("modifyUserInfo={}", userInfoForm);
            if (userInfoForm.getNickName() == null || "".equals(userInfoForm.getNickName())) {
                return BwJsonHelper.returnJSON("1001", "用户昵称不能为空");
            }
            if (userInfoForm.getEmail() == null || userInfoForm.getEmail().equals("")) {
                return BwJsonHelper.returnJSON("1001", "邮箱不能为空");
            }

            if (!WebUtil.checkEmail(userInfoForm.getEmail())) {
                return BwJsonHelper.returnJSON("1001", "请输入正确的邮箱");
            }

            User user = (User) CopyBean.getBean(new User(), userInfoForm);
            user.setId(userInfoForm.getUserId());
            int num = userRpcService.modifyUserInfo(user);

            if (num == 0) {
                return BwJsonHelper.returnJSON("1001", "修改失败");
            }

            return BwJsonHelper.returnJSON("0000", "修改成功", user);
        } catch (Exception e) {
            logger.error("modifyUserInfo：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 上传头像
     *
     * @param userId
     * @param formDataInput
     * @return
     */
    @POST
    @Path(value = "/v1/verify/uploadHeadPic")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/html;charset=UTF-8")
    public String uploadHeadPic(@HeaderParam("userId") int userId,
                                MultipartFormDataInput formDataInput) {
        logger.info("userId：" + userId);
        try {
            Map<String, List<InputPart>> uploadForm = formDataInput.getFormDataMap();

            List<InputPart> inputParts = uploadForm.get("image");
            if (null != inputParts) {
                String savePath = "";
                String headPath = "";
                ConfigInfo configInfo = ConfigInfo.getConfigInfo();
                for (InputPart inputPart : inputParts) {
                    MultivaluedMap<String, String> header = inputPart.getHeaders();
                    String suffix = WebUtil.getFileSuffix(header);

                    InputStream inputStream = inputPart.getBody(InputStream.class, null);
                    byte[] bytes = IOUtils.toByteArray(inputStream);

                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newFileName = df.format(new Date()) + "." + suffix;
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    String nowTime = sdf2.format(new Date());
                    savePath = String.format(configInfo.getHeadSavePath(), nowTime);
                    headPath = String.format(configInfo.getHeadPath(), nowTime);
                    savePath = savePath + newFileName;
                    headPath = headPath + newFileName;

                    WebUtil.writeFile(bytes, savePath);

                    userRpcService.updateUserHeadPic(userId, headPath);
                }
                return BwJsonHelper.returnJSON("0000", "上传成功", "headPath", headPath);
            } else {
                logger.error("uploadHeadPic：{}", "没有找到图片");
                return BwJsonHelper.returnJSON("1001", "没有找到图片");
            }
        } catch (Exception e) {
            logger.error("uploadHeadPic：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param newPwd2
     * @return
     */
    @POST
    @Path(value = "/v1/verify/modifyUserPwd")
    @Produces("text/html;charset=UTF-8")
    public String modifyUserPwd(@HeaderParam("userId") int userId,
                                @FormParam("oldPwd") String oldPwd,
                                @FormParam("newPwd") String newPwd,
                                @FormParam("newPwd2") String newPwd2) {
        logger.info("modifyUserPwd：oldPwd={},newPwd={},newPwd2={}", oldPwd, newPwd, newPwd2);
        try {
            if (oldPwd == null || oldPwd.equals("")) {
                return BwJsonHelper.returnJSON("1001", "旧密码不能为空");
            }

            if (newPwd == null || newPwd.equals("")) {
                return BwJsonHelper.returnJSON("1001", "新密码不能为空");
            }

            if (!newPwd.equals(newPwd2)) {
                return BwJsonHelper.returnJSON("1001", "两次密码输入不一致");
            }

            int num = userRpcService.updateUserPwd(userId, newPwd, oldPwd);

            if (num == 0) {
                return BwJsonHelper.returnJSON("1001", "密码修改失败");
            }

            return BwJsonHelper.returnJSON("0000", "密码修改成功");

        } catch (Exception e) {
            logger.error("modifyUserPwd：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId
     * @return
     */
    @GET
    @Path(value = "/v1/verify/getUserInfoByUserId")
    @Produces("text/html;charset=UTF-8")
    public String getUserInfoByUserId(
            @HeaderParam("userId") int userId) {
        try {
            return BwJsonHelper.returnJSON("0000", "查询成功", userRpcService.getUserInfoByUserId(userId));
        } catch (Exception e) {
            logger.error("getUserInfoByUserId：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 签到
     *
     * @param userId
     * @return
     */
    @GET
    @Path(value = "/v1/verify/userSignIn")
    @Produces("text/html;charset=UTF-8")
    public String userSignIn(
            @HeaderParam("userId") int userId) {
        try {
            int num = userRpcService.userSign(userId);
            if (num == 2) {
                return BwJsonHelper.returnJSON("1001", "今天已签到");
            }

            if (num == 0) {
                return BwJsonHelper.returnJSON("1001", "签到失败");
            }
            return BwJsonHelper.returnJSON("0000", "签到成功");
        } catch (Exception e) {
            logger.error("userSignIn：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 用户购票记录查询列表
     *
     * @param userId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v1/verify/findUserBuyTicketRecordList")
    @Produces("text/html;charset=UTF-8")
    public String findUserBuyTicketRecordList(@HeaderParam("userId") int userId,
                                              @QueryParam("page") int page,
                                              @QueryParam("count") int count,
                                              @QueryParam("status") int status) {
        logger.info("findUserBuyTicketRecordList：userId={},page={},count={},status={}", userId, page, count, status);

        try {
            List<UserBuyTicketRecord> userBuyTicketRecordList = userRpcService.findUserBuyTicketRecordList(userId, page, count, status);
            if (userBuyTicketRecordList.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无数据", new JSONArray());
            }
            return BwJsonHelper.returnJSON("0000", "请求成功", userBuyTicketRecordList);
        } catch (Exception e) {
            logger.error("findUserBuyTicketRecordList：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 微信绑定登陆
     *
     * @param ak
     * @param code
     * @return
     */
/*    @POST
    @Path(value = "/v1/weChatBindingLogin")
    @Produces("text/html;charset=UTF-8")
    public String weChatBindingLogin(
            @HeaderParam("ak") String ak,
            @FormParam("code") String code) {
        try {
            logger.info("weChatBindingLogin：code={}", code);
            UserLoginVo userLogin = userRpcService.wxBindingLogin(code);
            if (userLogin == null) {
                logger.info("weChatBindingLogin登陆失败");
                return BwJsonHelper.returnJSON("1001", "微信登陆失败");
            }
            return BwJsonHelper.returnJSON("0000", "登陆成功", userLogin);
        } catch (Exception e) {
            logger.error("weChatBindingLogin={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }*/

    /**
     * 绑定微信账号
     *
     * @param userId
     * @param code
     * @return
     */
  /*  @POST
    @Path(value = "/v1/verify/bindWeChat")
    @Produces("text/html;charset=UTF-8")
    public String bindWeChat(
            @HeaderParam("userId") int userId,
            @FormParam("code") String code) {
        try {
            logger.info("bindWeChat：user={},code={}", userId, code);
            int num = userRpcService.bindWeChat(userId, code);
            if (num == 2) {
                logger.info("bindWeChat：已绑定微信账号");
                return BwJsonHelper.returnJSON("1001", "已绑定微信账号");
            } else if (num == 1) {
                return BwJsonHelper.returnJSON("0000", "绑定成功");
            }
            return BwJsonHelper.returnJSON("1001", "绑定失败");
        } catch (Exception e) {
            logger.error("bindWeChat={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }*/

    /**
     * 是否绑定微信账号
     *
     * @param userId
     * @return 1=绑定 2=未绑定
     */
/*    @GET
    @Path(value = "/v1/verify/whetherToBindWeChat")
    @Produces("text/html;charset=UTF-8")
    public String whetherToBindWeChat(
            @HeaderParam("userId") int userId) {
        try {
            logger.info("whetherToBindWeChat：userId={}", userId);
            int num = userRpcService.whetherToBindWeChat(userId);
            return BwJsonHelper.returnJSON("0000", "登陆成功", "bindStatus", num);
        } catch (Exception e) {
            logger.error("whetherToBindWeChat={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }*/

    /**
     * 2.0 发送邮箱验证码
     *
     * @param email
     * @return
     */
    @POST
    @Path(value = "/v2/sendOutEmailCode")
    @Produces("text/html;charset=UTF-8")
    public String sendOutEmailCode(@FormParam("email") String email) {
        logger.info("sendOutEmailCode , email={}", email);
        if (email == null || "".equals(email)) {
            return BwJsonHelper.returnJSON("1001", "邮箱不能为空");
        }
        if (!Pattern.matches(CheckUtil.REGEX_EMAIL, email)) {
            return BwJsonHelper.returnJSON("1001", "邮箱格式错误");
        }
        try {
            int i = userRpcService.sendOutEmailCode(email);
            if (i == 1) {
                return BwJsonHelper.returnJSON("0000", "发送成功");
            }
            return BwJsonHelper.returnJSON("1001", "发送失败");
        } catch (Exception e) {
            logger.error("sendOutEmailCode={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 校验验证码
     *
     * @param email
     * @param code
     * @return
     */
    @POST
    @Path(value = "/v2/checkCode")
    @Produces("text/html;charset=UTF-8")
    public String checkCode(@FormParam("email") String email, @FormParam("code") String code) {
        logger.info("checkCode, email={},code={}", email, code);
        if (!Pattern.matches(CheckUtil.REGEX_EMAIL, email)) {
            return BwJsonHelper.returnJSON("1001", "邮箱格式错误");
        }
        if (email == null || "".equals(email)) {
            return BwJsonHelper.returnJSON("1001", "邮箱不能为空");
        }
        if (code == null || "".equals(code)) {
            return BwJsonHelper.returnJSON("1001", "验证码不能为空");
        }
        try {
            int i = userRpcService.checkCode(email, code);
            if (i == 3) {
                return BwJsonHelper.returnJSON("1001", "验证码已过期");
            }
            if (i == 4) {
                return BwJsonHelper.returnJSON("1001", "验证码错误");
            }
            return BwJsonHelper.returnJSON("0000", "验证通过");
        } catch (Exception e) {
            logger.error("checkCode={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 注册
     *
     * @param nickName
     * @param email
     * @param pwd
     * @param code
     * @return
     */
    @POST
    @Path(value = "/v2/register")
    @Produces("text/html;charset=UTF-8")
    public String register(@FormParam("nickName") String nickName, @FormParam("email") String email, @FormParam("pwd") String pwd, @FormParam("code") String code) {
        try {
            logger.info("register: nickName={},email ={},pwd ={},code={}", nickName, email, pwd, code);

            if (pwd == null || pwd.equals("")) {
                return BwJsonHelper.returnJSON("1002", "密码不能为空");
            }
            if (code == null || code.equals("")) {
                return BwJsonHelper.returnJSON("1002", "验证码不能为空");
            }
            if (nickName == null || nickName.equals("")) {
                return BwJsonHelper.returnJSON("1002", "昵称不能为空");
            }
            if (!WebUtil.checkEmail(email)) {
                return BwJsonHelper.returnJSON("1004", "请输入正确的邮箱");
            }
            int num = userRpcService.register(nickName, email, pwd, code);
            switch (num) {
                case 1:
                    return BwJsonHelper.returnJSON("0000", "注册成功");
                case 2:
                    return BwJsonHelper.returnJSON("1005", "注册失败,邮箱已存在");
                case 3:
                    return BwJsonHelper.returnJSON("1001", "验证码已过期");
                case 4:
                    return BwJsonHelper.returnJSON("1001", "验证码错误");
                case 5:
                    return BwJsonHelper.returnJSON("1005", "注册失败,昵称已存在");
                default:
                    return BwJsonHelper.returnJSON("1001", "请联系管理员");
            }
        } catch (Exception e) {
            logger.error("register", e);
            return BwJsonHelper.returnJSON("1001", "注册失败,请联系管理员");
        }
    }

    /**
     * 2.0 登陆
     *
     * @param email
     * @param pwd
     * @return
     */
    @POST
    @Path(value = "/v2/login")
    @Produces("text/html;charset=UTF-8")
    public String login(
            @FormParam("email") String email,
            @FormParam("pwd") String pwd) {
        logger.info("login：email={},pwd={}", email, pwd);
        try {
            if (email == null || email.equals("")) {
                return BwJsonHelper.returnJSON("1001", "请输入邮箱");
            }
            if (!WebUtil.checkEmail(email)) {
                return BwJsonHelper.returnJSON("1001", "请输入正确的邮箱");
            }
            if (pwd == null || pwd.equals("")) {
                return BwJsonHelper.returnJSON("1001", "请输入密码");
            }

            UserLoginVo user = userRpcService.loginV2(email, pwd);

            if (user == null) {
                return BwJsonHelper.returnJSON("1001", "登陆失败,账号或密码错误");
            }

            return BwJsonHelper.returnJSON("0000", "登陆成功", user);
        } catch (Exception e) {
            logger.error("login：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 2.0 查询用户预约电影信息
     *
     * @param userId
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findUserReserve")
    @Produces("text/html;charset=UTF-8")
    public String findUserReserve(@HeaderParam("userId") int userId) {
        logger.info("findUserReserve: userId={}", userId);
        try {
            List<UserReserveVo> userReserve = userRpcService.findUserReserve(userId);
            if (userReserve.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无预约电影");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", userReserve);
        } catch (Exception e) {
            logger.error("findUserReserve：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 2.0 购票记录
     *
     * @param userId
     * @param page
     * @param count
     * @param status
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findUserBuyTicketRecord")
    @Produces("text/html;charset=UTF-8")
    public String findUserBuyTicketRecord(@HeaderParam("userId") int userId, @QueryParam("page") int page,
                                          @QueryParam("count") int count, @QueryParam("status") int status) {
        logger.info("findUserBuyTicketRecord: userId={},page={},count={},status={}", userId, page, count, status);

        try {
            List<UserBuyTicketRecordVo> userBuyTicketRecord = userRpcService.findUserBuyTicketRecord(userId, page, count, status);
            if (userBuyTicketRecord.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无购票记录");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", userBuyTicketRecord);
        } catch (Exception e) {
            logger.error("findUserBuyTicketRecord: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 2.0 查看订单详情
     *
     * @param userId
     * @param orderId
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findBuyTicketRecordByOrderId")
    @Produces("text/html;charset=UTF-8")
    public String findBuyTicketRecordByOrderId(@HeaderParam("userId") int userId, @QueryParam("orderId") String orderId) {

        logger.info("findBuyTicketRecordByOrderId: userId={},orderId={}", userId, orderId);

        try {
            UserBuyTicketRecord buyTicketRecord = userRpcService.findBuyTicketRecordByOrderId(userId, orderId);
            return BwJsonHelper.returnJSON("0000", "查询成功", buyTicketRecord);
        } catch (Exception e) {
            logger.error("findBuyTicketRecordByOrderId: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }

    }

    /**
     * 2.0 查询看过的电影
     *
     * @param userId
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findSeenMovie")
    @Produces("text/html;charset=UTF-8")
    public String findSeenMovie(@HeaderParam("userId") int userId) {
        logger.info("findSeenMovie: userId={}", userId);
        try {
            List<UserSeenMovieRecordVo> seenMovie = userRpcService.findSeenMovie(userId);
            if (seenMovie.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无看过的电影");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", seenMovie);
        } catch (Exception e) {
            logger.error("findSeenMovie: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }

    }

    /**
     * 2.0 我的电影票
     *
     * @param userId
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findMyMovieTicket")
    @Produces("text/html;charset=UTF-8")
    public String findMyMovieTicket(@HeaderParam("userId") int userId) {
        logger.info("findMyMovieTicket: userId={}", userId);
        try {
            List<UserBuyTicketRecord> myMovieTicket = userRpcService.findMyMovieTicket(userId);
            if (myMovieTicket.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无电影票");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", myMovieTicket);
        } catch (Exception e) {
            logger.error("findMyMovieTicket: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }

    }

    /**
     * 2.0  查询取票码
     *
     * @param userId
     * @param recordId 购票记录id
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findExchangeCode")
    @Produces("text/html;charset=UTF-8")
    public String findExchangeCode(@HeaderParam("userId") int userId, @QueryParam("recordId") int recordId) {
        logger.info("findExchangeCode: userId={},recordId={}", userId, recordId);
        try {
            UserBuyTicketDetail exchangeCode = userRpcService.findExchangeCode(userId, recordId);
            if (exchangeCode.getExchangeCode() == null) {
                return BwJsonHelper.returnJSON("0000", "取票码为空");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", exchangeCode);
        } catch (Exception e) {
            logger.error("findExchangeCode: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }

    }

    /**
     * 取票
     *
     * @param orderId
     * @return
     */
    @GET
    @Path(value = "/v2/getTicket")
    @Produces("text/html;charset=UTF-8")
    public String getTicket(@QueryParam("orderId") String orderId) {
        logger.info("getTicket: orderId={}", orderId);
        try {
            int ticket = userRpcService.getTicket(orderId);
            if (ticket == 1) {
                return BwJsonHelper.returnJSON("0000", "取票成功");
            }
            return BwJsonHelper.returnJSON("1001", "取票失败");
        } catch (Exception e) {
            logger.error("getTicket: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 查询用户关注电影列表
     *
     * @param userId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findUserFollowMovieList")
    @Produces("text/html;charset=UTF-8")
    public String findUserFollowMovieList(@HeaderParam("userId") int userId, @QueryParam("page") int page, @QueryParam("count") int count) {
        logger.info("getTicket: userId={},page={},count={}", userId, page, count);
        try {
            List<UserFollowMovieVo> userFollowMovieList = userRpcService.findUserFollowMovieList(userId, page, count);
            if (userFollowMovieList.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无关注电影");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", userFollowMovieList);
        } catch (Exception e) {
            logger.error("findUserFollowMovieList: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 查询用户关注影院列表
     *
     * @param userId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findUserFollowCinemaList")
    @Produces("text/html;charset=UTF-8")
    public String findUserFollowCinemaList(@HeaderParam("userId") int userId, @QueryParam("page") int page, @QueryParam("count") int count) {
        logger.info("findUserFollowCinemaList: userId={},page={},count={}", userId, page, count);
        try {
            List<UserFollowCinemaVo> userFollowCinemaList = userRpcService.findUserFollowCinemaList(userId, page, count);
            if (userFollowCinemaList.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无关注影院");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", userFollowCinemaList);
        } catch (Exception e) {
            logger.error("findUserFollowCinemaList: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }


    /**
     * 修改用户手机号
     *
     * @param userId
     * @param phone
     * @return
     */
    @POST
    @Path(value = "/v2/verify/updateUserPhone")
    @Produces("text/html;charset=UTF-8")
    public String updateUserPhone(@HeaderParam("userId") int userId, @FormParam("phone") String phone) {
        logger.info("updateUserPhone: userId={},phone={}", userId, phone);
        try {
            int i = userRpcService.updateUserPhone(userId, phone);
            if (i == 1) {
                return BwJsonHelper.returnJSON("0000", "修改成功");
            }
            return BwJsonHelper.returnJSON("1001", "修改失败");
        } catch (Exception e) {
            logger.error("updateUserPhone: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 修改用户生日
     *
     * @param userId
     * @param birthday
     * @return
     */
    @POST
    @Path(value = "/v2/verify/updateUserBirthday")
    @Produces("text/html;charset=UTF-8")
    public String updateUserBirthday(@HeaderParam("userId") int userId, @FormParam("birthday") String birthday) {
        logger.info("updateUserBirthday: userId={},birthday={}", userId, birthday);
        try {
            int i = userRpcService.updateUserBirthday(userId, birthday);
            if (i == 1) {
                return BwJsonHelper.returnJSON("0000", "修改成功");
            }
            return BwJsonHelper.returnJSON("1001", "修改失败");
        } catch (Exception e) {
            logger.error("updateUserBirthday: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 2.0 查询我的电影评论
     *
     * @param userId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findMyMovieCommentList")
    @Produces("text/html;charset=UTF-8")
    public String findMyMovieCommentList(@HeaderParam("userId") int userId, @QueryParam("page") int page, @QueryParam("count") int count) {
        logger.info("findMyMovieCommentList: userId={},page={},count={}", userId, page, count);
        try {
            List<MyMovieCommentVo> myMovieCommentList = userRpcService.findMyMovieCommentList(userId, page, count);
            if (myMovieCommentList.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无电影评论");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", myMovieCommentList);
        } catch (Exception e) {
            logger.error("findMyMovieCommentList: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 2.0 查询我的影院评论
     *
     * @param userId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findMyCinemaCommentList")
    @Produces("text/html;charset=UTF-8")
    public String findMyCinemaCommentList(@HeaderParam("userId") int userId, @QueryParam("longitude") String longitude, @QueryParam("latitude") String latitude, @QueryParam("page") int page, @QueryParam("count") int count) {
        logger.info("findMyCinemaCommentList: userId={},longitude={},latitude={},page={},count={}", userId, longitude, latitude, page, count);
        try {
            List<MyCinemaCommentVo> myCinemaCommentList = userRpcService.findMyCinemaCommentList(userId, longitude, latitude, page, count);
            if (myCinemaCommentList.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无影院评论");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", myCinemaCommentList);
        } catch (Exception e) {
            logger.error("findMyCinemaCommentList: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }
    }

    /**
     * 2.0 查询用户看过的电影评论
     *
     * @param userId
     * @param recordId
     * @return
     */
    @GET
    @Path(value = "/v2/verify/findSeenMovieComment")
    @Produces("text/html;charset=UTF-8")
    public String findSeenMovieComment(@HeaderParam("userId") int userId, @QueryParam("recordId") int recordId) {
        logger.info("findSeenMovieComment: userId={},recordId={}", userId, recordId);

        try {
            MySeenMovieCommentVo seenMovieComment = userRpcService.findSeenMovieComment(userId, recordId);
            if (seenMovieComment == null) {
                return BwJsonHelper.returnJSON("1001", "无评论");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", seenMovieComment);
        } catch (Exception e) {
            logger.error("findSeenMovieComment: {}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常,请联系管理员");
        }

    }


}
