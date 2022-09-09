package com.bw.movie.action;

import com.bw.movie.rpc.api.CinemaRpcService;
import com.bw.movie.rpc.pojo.CinemaComment;
import com.bw.movie.rpc.pojo.CinemaCommentGreat;
import com.bw.movie.rpc.pojo.Cinemas;
import com.bw.movie.rpc.vo.CinemaCommentVo;
import com.bw.movie.rpc.vo.CinemaScheduleVo;
import com.bw.movie.rpc.vo.CinemasVo;
import com.bw.movie.util.BwJsonHelper;
import com.bw.movie.vo.CinemaCommentInfo;
import com.bw.movie.vo.CommentGreatInfo;
import org.jboss.resteasy.annotations.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyj on 2018/7/19.
 */
@Path(value = "/cinema")
public class CinemaAction {

    private Logger logger = LoggerFactory.getLogger(CinemaAction.class);

    @Resource
    private CinemaRpcService cinemaRpcService;


    /**
     * 查询推荐的影院信息
     *
     * @param userId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v1/findRecommendCinemas")
    @Produces("text/html;charset=UTF-8")
    public String findRecommendCinemas(
            @HeaderParam("userId") int userId,
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        logger.info("findRecommendCinemas：userId={},page={},count={}", userId, page, count);
        try {
            List<CinemasVo> recommendCinemas = cinemaRpcService.findRecommendCinemas(userId, page, count);
            if (recommendCinemas == null) {
                return BwJsonHelper.returnJSON("0000", "查询成功", new ArrayList<CinemasVo>());
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", recommendCinemas);
        } catch (Exception e) {
            logger.error("findRecommendCinemas：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 查询附近的影院
     *
     * @param userId
     * @param longitude
     * @param latitude
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v1/findNearbyCinemas")
    @Produces("text/html;charset=UTF-8")
    public String findNearbyCinemas(
            @HeaderParam("userId") int userId,
            @QueryParam("longitude") String longitude,
            @QueryParam("latitude") String latitude,
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        logger.info("findNearbyCinemas：userId={},longitude={},latitude={},page={},count={}", userId, longitude, latitude, page, count);
        try {
            List<CinemasVo> nearbyCinemas = cinemaRpcService.findNearbyCinemas(userId, longitude, latitude, page, count);
            if (nearbyCinemas == null) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", nearbyCinemas);
        } catch (Exception e) {
            logger.error("findNearbyCinemas：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 查询影院信息明细
     *
     * @param cinemaId
     * @return
     */
    @GET
    @Path(value = "/v1/findCinemaInfo")
    @Produces("text/html;charset=UTF-8")
    public String findCinemaInfo(
            @HeaderParam("userId") int userId,
            @QueryParam("cinemaId") int cinemaId) {
        logger.info("findCinemaInfo：userId={},cinemaId={}", userId, cinemaId);
        if (cinemaId <= 0) {
            return BwJsonHelper.returnJSON("1001", "参数错误");
        }
        try {
            CinemasVo cinemaInfo = cinemaRpcService.findCinemaInfo(userId, cinemaId);
            if (cinemaInfo == null) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", cinemaInfo);
        } catch (Exception e) {
            logger.error("findCinemaInfo：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 查询所有电影院
     *
     * @param page
     * @param count
     * @param cinemaName
     * @return
     */
    @GET
    @Path(value = "/v1/findAllCinemas")
    @Produces("text/html;charset=UTF-8")
    public String findAllCinemas(
            @QueryParam("page") int page,
            @QueryParam("count") int count,
            @QueryParam("cinemaName") String cinemaName) {
        logger.info("findAllCinemas：cinemaName={}", cinemaName);
        try {
            List<CinemasVo> allCinemas = cinemaRpcService.findAllCinemas(page, count, cinemaName);
            if (allCinemas == null) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", allCinemas);
        } catch (Exception e) {
            logger.error("findAllCinemas：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 查询用户关注的影院信息
     *
     * @param userId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v1/verify/findCinemaPageList")
    @Produces("text/html;charset=UTF-8")
    public String findCinemaPageList(
            @HeaderParam("userId") int userId,
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        logger.info("findCinemaPageList：userId={}", userId);
        try {
            List<CinemasVo> cinemaPageList = cinemaRpcService.findCinemaPageList(userId, page, count);
            if (cinemaPageList == null) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", cinemaPageList);
        } catch (Exception e) {
            logger.error("findCinemaPageList：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 取消关注影院
     *
     * @param userId
     * @param cinemaId
     * @return
     */
    @GET
    @Path(value = "/v1/verify/cancelFollowCinema")
    @Produces("text/html;charset=UTF-8")
    public String cancelFollowCinema(
            @HeaderParam("userId") int userId,
            @QueryParam("cinemaId") int cinemaId) {
        logger.info("cancelFollowCinema：userId={},cinemaId={}", userId, cinemaId);
        try {
            int num = cinemaRpcService.cancelFollowCinema(userId, cinemaId);
            if (num == 0) {
                return BwJsonHelper.returnJSON("1001", "取消关注失败");
            }
            return BwJsonHelper.returnJSON("0000", "取消关注成功");
        } catch (Exception e) {
            logger.error("cancelFollowCinema：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 关注影院
     *
     * @param userId
     * @param cinemaId
     * @return
     */
    @GET
    @Path(value = "/v1/verify/followCinema")
    @Produces("text/html;charset=UTF-8")
    public String followCinema(
            @HeaderParam("userId") int userId,
            @QueryParam("cinemaId") int cinemaId) {
        logger.info("followCinema：userId={},cinemaId={}", userId, cinemaId);
        try {
            int num = cinemaRpcService.followCinema(userId, cinemaId);
            if (num == 0) {
                return BwJsonHelper.returnJSON("1001", "关注失败");
            }

            if (num == 2) {
                return BwJsonHelper.returnJSON("1001", "不能重复关注");
            }

            return BwJsonHelper.returnJSON("0000", "关注成功");
        } catch (Exception e) {
            logger.error("followCinema：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 查询影院评论
     *
     * @param cinemaId
     */
    @GET
    @Path(value = "/v1/findAllCinemaComment")
    @Produces("text/html;charset=UTF-8")
    public String findAllCinemaComment(@HeaderParam("userId") int userId, @QueryParam("cinemaId") int cinemaId, @QueryParam("page") int page,
                                       @QueryParam("count") int count) {
        logger.info("followCinema：userId={},cinemaId={}", userId, cinemaId, page, count);
        try {
            if (cinemaId <= 0 || page <= 0 || count <= 0) {
                return BwJsonHelper.returnJSON("1001", "cinemaId或page或count不能为0或不合法");
            }
            List<CinemaCommentVo> cinemaCommentVoList = cinemaRpcService.findAllCinemaComment(userId, cinemaId, page, count);
            if (cinemaCommentVoList.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", cinemaCommentVoList);

        } catch (Exception e) {
            logger.error("findAllCinemaComment：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 新增评论
     *
     * @param userId
     * @param cinemaCommentInfo
     * @return
     */
    @POST
    @Path(value = "/v1/verify/cinemaComment")
    @Produces("text/html;charset=UTF-8")
    public String cinemaComment(@HeaderParam("userId") int userId, @Form CinemaCommentInfo cinemaCommentInfo) {
        try {
            if (cinemaCommentInfo.getCinemaId() <= 0) {
                return BwJsonHelper.returnJSON("1001", "cinemaId不能为0或不合法");
            }
            String content = cinemaCommentInfo.getCommentContent();
            if (content.length() > 500 || content.equals("")) {
                return BwJsonHelper.returnJSON("1001", "评论过长或评论为空");
            }
            CinemaComment cinemaComment1 = new CinemaComment();
            cinemaComment1.setCommentUserId(userId);
            cinemaComment1.setCommentContent(cinemaCommentInfo.getCommentContent());
            cinemaComment1.setCinemaId(cinemaCommentInfo.getCinemaId());
            int i = cinemaRpcService.saveCinemaComment(cinemaComment1);
            if (i == 1) {
                return BwJsonHelper.returnJSON("0000", "评论成功");
            }
            if (i == 2) {
                return BwJsonHelper.returnJSON("1001", "已评论，不能重复评论");
            }
            return BwJsonHelper.returnJSON("1001", "评论失败");
        } catch (Exception e) {
            logger.error("cinemaComment：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 点赞
     *
     * @param userId
     * @param commentGreatInfo
     * @return
     */
    @POST
    @Path(value = "/v1/verify/cinemaCommentGreat")
    @Produces("text/html;charset=UTF-8")
    public String cinemaCommentGreat(@HeaderParam("userId") int userId, @Form CommentGreatInfo commentGreatInfo) {
        try {
            if (commentGreatInfo.getCommentId() <= 0) {
                return BwJsonHelper.returnJSON("1001", "commentId不能为0或不合法");
            }
            int isGreat = cinemaRpcService.whetherCinemaGreat(commentGreatInfo.getCommentId(), userId);
            if (isGreat == 1) {
                return BwJsonHelper.returnJSON("0000", "不能重复点赞");
            } else {
                CinemaCommentGreat cinemaCommentGreat = new CinemaCommentGreat();
                cinemaCommentGreat.setUserId(userId);
                cinemaCommentGreat.setCommentId(commentGreatInfo.getCommentId());
                Boolean boo = cinemaRpcService.saveCinemaCommentGreat(cinemaCommentGreat);
                if (boo) {
                    return BwJsonHelper.returnJSON("0000", "点赞成功");
                }
                return BwJsonHelper.returnJSON("1001", "点赞失败");
            }
        } catch (Exception e) {
            logger.error("cinemaCommentGreat：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 根据区域查询影院
     *
     * @param regionId
     * @return
     */
    @GET
    @Path(value = "/v2/findCinemaByRegion")
    @Produces("text/html;charset=UTF-8")
    public String findCinemaByRegion(@QueryParam("regionId") int regionId) {
        logger.info("findCinemaByRegion：regionId={}", regionId);
        try {
            List<Cinemas> cinemaInfo = cinemaRpcService.findCinemaByRegion(regionId);
            if (cinemaInfo.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "查询数据为空");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", cinemaInfo);
        } catch (Exception e) {
            logger.error("findCinemaByRegion：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 查询影院下的电影排期
     *
     * @return
     */
    @GET
    @Path(value = "/v2/findCinemaScheduleList")
    @Produces("text/html;charset=UTF-8")
    public String findCinemaScheduleList(@QueryParam("cinemaId") int cinemaId, @QueryParam("page") int page, @QueryParam("count") int count) {
        logger.info("findCinemaScheduleList: cinemaId={},page={},count={}", cinemaId, page, count);
        try {
            List<CinemaScheduleVo> cinemaScheduleList = cinemaRpcService.findCinemaScheduleList(cinemaId, page, count);
            if (cinemaScheduleList.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "查询数据为空");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", cinemaScheduleList);
        } catch (Exception e) {
            logger.error("findCinemaByRegion：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }

    }

}
