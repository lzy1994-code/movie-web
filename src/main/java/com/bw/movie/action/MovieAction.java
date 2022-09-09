package com.bw.movie.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bw.movie.rpc.api.MovieRpcService;
import com.bw.movie.rpc.pojo.MovieComment;
import com.bw.movie.rpc.pojo.MovieCommentGreat;
import com.bw.movie.rpc.pojo.MovieCommentReply;
import com.bw.movie.rpc.pojo.Movies;
import com.bw.movie.rpc.vo.*;
import com.bw.movie.util.BwJsonHelper;
import com.bw.movie.util.QRcodeUtil;
import com.bw.movie.util.WebUtil;
import com.bw.movie.vo.CommentGreatInfo;
import com.bw.movie.vo.MovieCommentInfo;
import com.bw.movie.vo.MovieCommentReplyInfo;
import org.jboss.resteasy.annotations.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.List;

/**
 * Created by xyj on 2018/7/19.
 */
@Path(value = "/movie")
public class MovieAction {

    private Logger logger = LoggerFactory.getLogger(MovieAction.class);

    @Resource
    private MovieRpcService movieRpcService;


    /**
     * 查询热门电影列表
     *
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v1/findHotMovieList")
    @Produces("text/html;charset=UTF-8")
    public String findHotMovieList(
            @HeaderParam("userId") int userId,
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        List<MovieVo> hotMovieList = movieRpcService.findHotMovieList(userId, page, count);
        if (hotMovieList == null) {
            return BwJsonHelper.returnJSON("0000", "无数据");
        }
        return BwJsonHelper.returnJSON("0000", "查询成功", hotMovieList);
    }

    /**
     * 查询正在上映电影列表
     *
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v1/findReleaseMovieList")
    @Produces("text/html;charset=UTF-8")
    public String findReleaseMovieList(
            @HeaderParam("userId") int userId,
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        List<MovieVo> releaseMovieList = movieRpcService.findReleaseMovieList(userId, page, count);
        if (releaseMovieList == null) {
            return BwJsonHelper.returnJSON("0000", "无数据");
        }
        return BwJsonHelper.returnJSON("0000", "查询成功", releaseMovieList);
    }

    /**
     * 查询即将上映电影列表
     *
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v1/findComingSoonMovieList")
    @Produces("text/html;charset=UTF-8")
    public String findComingSoonMovieList(
            @HeaderParam("userId") int userId,
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        List<MovieVo> comingSoonMovieList = movieRpcService.findComingSoonMovieList(userId, page, count);
        if (comingSoonMovieList == null) {
            return BwJsonHelper.returnJSON("0000", "无数据");
        }
        return BwJsonHelper.returnJSON("0000", "查询成功", comingSoonMovieList);
    }

    /**
     * 根据电影ID查询电影信息
     *
     * @param movieId
     * @return
     */
    @GET
    @Path(value = "/v1/findMoviesById")
    @Produces("text/html;charset=UTF-8")
    public String findMoviesById(
            @QueryParam("movieId") int movieId) {
        Movies movies = movieRpcService.findMoviesById(movieId);
        if (movies == null) {
            return BwJsonHelper.returnJSON("0000", "无数据");
        }

        return BwJsonHelper.returnJSON("0000", "查询成功", movies);
    }

    /**
     * 查看电影详情
     *
     * @param movieId
     * @return
     */
    @GET
    @Path(value = "/v1/findMoviesDetail")
    @Produces("text/html;charset=UTF-8")
    public String findMoviesDetail(
            @HeaderParam("userId") int userId,
            @QueryParam("movieId") int movieId) {
        MovieVo moviesDetail = movieRpcService.findMoviesDetail(userId, movieId);
        if (moviesDetail == null) {
            return BwJsonHelper.returnJSON("0000", "无数据");
        }

        return BwJsonHelper.returnJSON("0000", "查询成功", moviesDetail);
    }

    /**
     * 查询用户关注的影片列表
     *
     * @param userId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v1/verify/findMoviePageList")
    @Produces("text/html;charset=UTF-8")
    public String findMoviePageList(
            @HeaderParam("userId") int userId,
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        List<MovieListVo> moviePageList = movieRpcService.findMoviePageList(userId, page, count);
        if (moviePageList == null) {
            return BwJsonHelper.returnJSON("0000", "无数据");
        }

        return BwJsonHelper.returnJSON("0000", "查询成功", moviePageList);
    }

    /**
     * 取消关注电影
     *
     * @param userId
     * @param movieId
     * @return
     */
    @GET
    @Path(value = "/v1/verify/cancelFollowMovie")
    @Produces("text/html;charset=UTF-8")
    public String cancelFollowMovie(
            @HeaderParam("userId") int userId,
            @QueryParam("movieId") int movieId) {
        logger.info("cancelFollowMovie：userId={},movieId={}", userId, movieId);
        try {
            int num = movieRpcService.cancelFollowMovie(userId, movieId);
            if (num == 0) {
                return BwJsonHelper.returnJSON("1001", "取消关注失败");
            }
            return BwJsonHelper.returnJSON("0000", "取消关注成功");
        } catch (Exception e) {
            logger.error("cancelFollowMovie={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 关注电影
     *
     * @param userId
     * @param movieId
     * @return
     */
    @GET
    @Path(value = "/v1/verify/followMovie")
    @Produces("text/html;charset=UTF-8")
    public String followMovie(
            @HeaderParam("userId") int userId,
            @QueryParam("movieId") int movieId) {
        logger.info("followMovie：userId={},movieId={}", userId, movieId);
        try {
            int num = movieRpcService.followMovie(userId, movieId);
            if (num == 0) {
                return BwJsonHelper.returnJSON("1001", "关注失败");
            }
            if (num == 2) {
                return BwJsonHelper.returnJSON("1001", "不能重复关注");
            }
            return BwJsonHelper.returnJSON("0000", "关注成功");
        } catch (Exception e) {
            logger.error("followMovie={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 根据影院ID查询该影院当前排期的电影列表
     *
     * @param cinemaId
     * @return
     */
    @GET
    @Path(value = "/v1/findMovieListByCinemaId")
    @Produces("text/html;charset=UTF-8")
    public String findMovieListByCinemaId(
            @QueryParam("cinemaId") int cinemaId) {
        try {
            List<MovieListVo> movieList = movieRpcService.findMovieListByCinemaId(cinemaId);
            if (movieList == null) {
                return BwJsonHelper.returnJSON("1001", "无数据");
            }

            return BwJsonHelper.returnJSON("0000", "查询成功", movieList);
        } catch (Exception e) {
            logger.error("findMovieListByCinemaId={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 根据电影ID查询当前排片该电影的影院列表
     *
     * @param movieId
     * @return
     */
    @GET
    @Path(value = "/v1/findCinemasListByMovieId")
    @Produces("text/html;charset=UTF-8")
    public String findCinemasListByMovieId(
            @QueryParam("movieId") int movieId) {
        try {
            List<CinemasVo> cinemasList = movieRpcService.findCinemasListByMovieId(movieId);
            if (cinemasList == null) {
                return BwJsonHelper.returnJSON("1001", "无数据");
            }

            return BwJsonHelper.returnJSON("0000", "查询成功", cinemasList);
        } catch (Exception e) {
            logger.error("findCinemasListByMovieId={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 根据电影ID和影院ID查询电影排期列表
     *
     * @param movieId
     * @param cinemasId
     * @return
     */
    @GET
    @Path(value = "/v1/findMovieScheduleList")
    @Produces("text/html;charset=UTF-8")
    public String findMovieScheduleList(
            @QueryParam("movieId") int movieId,
            @QueryParam("cinemasId") int cinemasId) {
        try {
            List<MovieScheduleVo> movieScheduleList = movieRpcService.findMovieScheduleList(movieId, cinemasId);
            if (movieScheduleList == null) {
                return BwJsonHelper.returnJSON("1001", "无数据");
            }

            return BwJsonHelper.returnJSON("0000", "查询成功", movieScheduleList);
        } catch (Exception e) {
            logger.error("findMovieScheduleList={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 购票下单
     *
     * @param userId
     * @param scheduleId
     * @param amount
     * @param sign
     * @return
     */
    @POST
    @Path(value = "/v1/verify/buyMovieTicket")
    @Produces("text/html;charset=UTF-8")
    public String buyMovieTicket(
            @HeaderParam("userId") int userId,
            @FormParam("scheduleId") int scheduleId,
            @FormParam("amount") int amount,
            @FormParam("sign") String sign) {
        try {
            if (amount == 0) {
                return BwJsonHelper.returnJSON("1001", "请选择您的购票数量");
            }
            //校验签名
            StringBuffer sb = new StringBuffer();
            sb.append(userId);
            sb.append(scheduleId);
            sb.append(amount);
            sb.append("movie");
            String mySign = WebUtil.MD5(sb.toString());

            if (!mySign.equals(sign)) {
                return BwJsonHelper.returnJSON("1001", "签名不对");
            }

            String orderId = movieRpcService.buyMovieTicket(userId, scheduleId, amount, sign);

            return BwJsonHelper.returnJSON("0000", "下单成功", "orderId", orderId);
        } catch (Exception e) {
            logger.error("buyMovieTicket={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 支付
     *
     * @param userId
     * @param orderId
     * @param payType
     * @param request
     * @return
     */
    @POST
    @Path(value = "/v1/verify/pay")
    @Produces("text/html;charset=UTF-8")
    public String pay(
            @HeaderParam("userId") int userId,
            @FormParam("orderId") String orderId,
            @FormParam("payType") int payType,
            @Context HttpServletRequest request) {
        try {
            //获取用户真实IP
            String ip = WebUtil.getIpAddress(request);
            logger.info("获取用户真实IP为={}", ip);

            String result = movieRpcService.pay(orderId, payType, ip);
            logger.info("返回客户端支付数据={}", result);

            return result;
        } catch (Exception e) {
            logger.error("pay={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 查询电影评论
     *
     * @param movieId
     * @return
     */
    @GET
    @Path(value = "/v1/findAllMovieComment")
    @Produces("text/html;charset=UTF-8")
    public String findAllMovieComment(@HeaderParam("userId") int userId, @QueryParam("movieId") int movieId, @QueryParam("page") int page,
                                      @QueryParam("count") int count) {
        logger.info("findAllMovieComment：userId={},movieId={},page={},count={}", userId, movieId, page, count);
        try {
            if (movieId <= 0 || page <= 0 || count <= 0) {
                return BwJsonHelper.returnJSON("1001", "movieId或page或count不能为0或不合法");
            }
            List<MovieCommentVo> movieCommentVoList = movieRpcService.findAllMovieComment(userId, movieId, page, count);
            if (movieCommentVoList.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", movieCommentVoList);
        } catch (Exception e) {
            logger.error("findAllMovieComment：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }


    /**
     * 新增评论
     *
     * @param movieComment
     * @return
     */
    @POST
    @Path(value = "/v1/verify/movieComment")
    @Produces("text/html;charset=UTF-8")
    public String movieComment(@HeaderParam("userId") int userId, @Form MovieCommentInfo movieComment) {

        try {
            if (movieComment.getMovieId() <= 0) {
                return BwJsonHelper.returnJSON("1001", "movieId不能为0或不合法");
            }
            MovieComment movieComment1 = new MovieComment();
            String content = movieComment.getCommentContent();
            if (content.length() > 500 || content == null) {
                return BwJsonHelper.returnJSON("1001", "评论不能过长并且不能为空！");
            } else {
                movieComment1.setCommentContent(movieComment.getCommentContent());
                movieComment1.setMovieId(movieComment.getMovieId());
                movieComment1.setCommentUserId(userId);
                movieComment1.setScore(movieComment.getScore());
                int i = movieRpcService.saveMovieComment(movieComment1);
                if (i == 1) {
                    return BwJsonHelper.returnJSON("0000", "评论成功");
                }
                if (i == 2) {
                    return BwJsonHelper.returnJSON("1001", "已评论，无法重复评论");
                }
                return BwJsonHelper.returnJSON("1001", "评论失败");
            }
        } catch (Exception e) {
            logger.error("movieComment：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }

    }

    /**
     * 查询影片评论下的回复
     *
     * @param commentId
     * @return
     */
    @GET
    @Path(value = "/v1/findCommentReply")
    @Produces("text/html;charset=UTF-8")
    public String findCommentReply(@HeaderParam("userId") int userId, @QueryParam("commentId") int commentId, @QueryParam("page") int page,
                                   @QueryParam("count") int count) {
        logger.info("findCommentReply：userId={},commentId={},page={},count={}", userId, commentId, page, count);
        try {
            if (commentId <= 0 || page <= 0 || count <= 0) {
                return BwJsonHelper.returnJSON("1001", "commentId或page或count不能为0或不合法");
            }
            List<MovieCommentReplyVo> movieCommentReplyVoList = movieRpcService.findAllCommentReply(commentId, page, count);
            if (movieCommentReplyVoList.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", movieCommentReplyVoList);
        } catch (Exception e) {
            logger.error("findCommentReply：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 新增回复
     *
     * @param movieCommentReply
     * @return
     */
    @POST
    @Path(value = "/v1/verify/commentReply")
    @Produces("text/html;charset=UTF-8")
    public String commentReply(@HeaderParam("userId") int userId, @Form MovieCommentReplyInfo movieCommentReply) {
        try {
            if (movieCommentReply.getReplyContent() == null) {
                return BwJsonHelper.returnJSON("1001", "回复内容不能为空！");
            }
            MovieCommentReply movieCommentReply1 = new MovieCommentReply();
            movieCommentReply1.setCommentId(movieCommentReply.getCommentId());
            movieCommentReply1.setReplyContent(movieCommentReply.getReplyContent());
            movieCommentReply1.setReplyUserId(userId);
            boolean boo = movieRpcService.addCommentReply(movieCommentReply1);
            if (boo) {
                return BwJsonHelper.returnJSON("0000", "回复成功");
            }
            return BwJsonHelper.returnJSON("0000", "回复失败");
        } catch (Exception e) {
            logger.error("commentReply：{}", e);
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
    @Path(value = "/v1/verify/movieCommentGreat")
    @Produces("text/html;charset=UTF-8")
    public String movieCommentGreat(@HeaderParam("userId") int userId, @Form CommentGreatInfo commentGreatInfo) {
        try {
            if (commentGreatInfo.getCommentId() <= 0) {
                return BwJsonHelper.returnJSON("1001", "commentId不能为0或不合法");
            }
            int isGreat = movieRpcService.whetherMovieGreat(commentGreatInfo.getCommentId(), userId);
            if (isGreat == 1) {
                return BwJsonHelper.returnJSON("0000", "不能重复点赞");
            } else {
                MovieCommentGreat commentGreat = new MovieCommentGreat();
                commentGreat.setUserId(userId);
                commentGreat.setCommentId(commentGreatInfo.getCommentId());
                Boolean boo = movieRpcService.saveMovieCommentGreat(commentGreat);
                if (boo) {
                    return BwJsonHelper.returnJSON("0000", "点赞成功");
                }
                return BwJsonHelper.returnJSON("1001", "点赞失败");
            }
        } catch (Exception e) {
            logger.error("movieCommentGreat：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 根据影院ID查询该影院下即将上映的电影列表
     *
     * @param cinemaId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v1/findSoonMovieByCinemaId")
    @Produces("text/html;charset=UTF-8")
    public String findSoonMovieByCinemaId(
            @QueryParam("cinemaId") int cinemaId,
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        try {
            List<MovieListVo> movieList = movieRpcService.findSoonMovieByCinemaId(cinemaId);
            if (movieList == null || movieList.size() == 0) {
                return BwJsonHelper.returnJSON("1001", "无数据");
            }

            return BwJsonHelper.returnJSON("0000", "查询成功", "movieList", movieList);
        } catch (Exception e) {
            logger.error("findMovieByCinemaId={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * TODO 临时使用
     *
     * @return
     */
    @GET
    @Path(value = "/v1/findMovieList")
    @Produces("text/html;charset=UTF-8")
    public String findMovieList() {
        JSONArray ja = new JSONArray();

        JSONObject obj = new JSONObject();
        obj.put("name", "碟中谍6-全面瓦解");
        obj.put("duration", "148分钟");
        obj.put("imageUrl", "http://172.17.8.100/images/movie/temp/dzd6.jpg");
        obj.put("videoResource", "http://172.17.8.100/video/temp/dzd6.mp4");
        ja.add(obj);

        obj = new JSONObject();
        obj.put("name", "黄金兄弟");
        obj.put("duration", "100分钟");
        obj.put("imageUrl", "http://172.17.8.100/images/movie/temp/hjxd.jpg");
        obj.put("videoResource", "http://172.17.8.100/video/temp/xdtm.mp4");
        ja.add(obj);

        obj = new JSONObject();
        obj.put("name", "反贪风暴3");
        obj.put("duration", "100分钟");
        obj.put("imageUrl", "http://172.17.8.100/images/movie/temp/ftfb3.jpg");
        obj.put("videoResource", "http://172.17.8.100/video/temp/dzd6.mp4");
        ja.add(obj);


        obj = new JSONObject();
        obj.put("name", "鎌仓物语");
        obj.put("duration", "120分钟");
        obj.put("imageUrl", "http://172.17.8.100/images/movie/temp/lcwy.jpg");
        obj.put("videoResource", "http://172.17.8.100/video/temp/dzd6.mp4");
        ja.add(obj);

        obj = new JSONObject();
        obj.put("name", "江湖儿女");
        obj.put("duration", "136分钟");
        obj.put("imageUrl", "http://172.17.8.100/images/movie/temp/jhen.jpg");
        obj.put("videoResource", "http://172.17.8.100/video/temp/dzd6.mp4");
        ja.add(obj);

        obj = new JSONObject();
        obj.put("name", "李茶的姑妈");
        obj.put("duration", "113分钟");
        obj.put("imageUrl", "http://172.17.8.100/images/movie/temp/lcdgm.jpg");
        obj.put("videoResource", "http://172.17.8.100/video/temp/dzd6.mp4");
        ja.add(obj);

        obj = new JSONObject();
        obj.put("name", "悲伤逆流成河");
        obj.put("duration", "105分钟");
        obj.put("imageUrl", "http://172.17.8.100/images/movie/temp/bsnlch.jpg");
        obj.put("videoResource", "http://172.17.8.100/video/temp/dzd6.mp4");
        ja.add(obj);

        obj = new JSONObject();
        obj.put("name", "影");
        obj.put("duration", "115分钟");
        obj.put("imageUrl", "http://172.17.8.100/images/movie/temp/ying.jpg");
        obj.put("videoResource", "http://172.17.8.100/video/temp/dzd6.mp4");
        ja.add(obj);

        obj = new JSONObject();
        obj.put("name", "无双 ");
        obj.put("duration", "130分钟");
        obj.put("imageUrl", "http://172.17.8.100/images/movie/temp/ws.jpg");
        obj.put("videoResource", "http://172.17.8.100/video/temp/dzd6.mp4");
        ja.add(obj);

        return BwJsonHelper.returnJSON("0000", "查询成功", ja);

    }


    /**
     * 2.0查询热门电影列表
     *
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/findHotMovieList")
    @Produces("text/html;charset=UTF-8")
    public String findHotMovieInfoList(
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        List<MovieInfoVo> hotMovieInfoList = movieRpcService.findHotMovieInfoList(page, count);
        if (hotMovieInfoList.isEmpty()) {
            return BwJsonHelper.returnJSON("0000", "无数据");
        }
        return BwJsonHelper.returnJSON("0000", "查询成功", hotMovieInfoList);
    }

    /**
     * 2.0查询正在上映电影列表
     *
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/findReleaseMovieList")
    @Produces("text/html;charset=UTF-8")
    public String findReleaseMovieInfoList(
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        List<MovieInfoVo> releaseMovieInfoList = movieRpcService.findReleaseMovieInfoList(page, count);
        if (releaseMovieInfoList.isEmpty()) {
            return BwJsonHelper.returnJSON("0000", "无数据");
        }
        return BwJsonHelper.returnJSON("0000", "查询成功", releaseMovieInfoList);
    }

    /**
     * 2.0查询即将上映电影列表
     *
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/findComingSoonMovieList")
    @Produces("text/html;charset=UTF-8")
    public String findComingSoonMovieInfoList(
            @HeaderParam("userId") int userId,
            @QueryParam("page") int page,
            @QueryParam("count") int count) {
        List<ComingSoonMovieVo> comingSoonMovieInfoList = movieRpcService.findComingSoonMovieInfoList(userId, page, count);
        if (comingSoonMovieInfoList.isEmpty()) {
            return BwJsonHelper.returnJSON("0000", "无数据");
        }
        return BwJsonHelper.returnJSON("0000", "查询成功", comingSoonMovieInfoList);
    }

    /**
     * 2.0 预约
     *
     * @param userId
     * @param movieId
     * @return
     */

    @POST
    @Path(value = "/v2/verify/reserve")
    @Produces("text/html;charset=UTF-8")
    public String reserve(@HeaderParam("userId") int userId, @FormParam("movieId") int movieId) {
        logger.info("reserve：userId={},movieId={}", userId, movieId);
        try {
            if (movieId <= 0) {
                return BwJsonHelper.returnJSON("1001", "movieId参数不合法");
            }
            int i = movieRpcService.reserve(userId, movieId);
            if (i == 1) {
                return BwJsonHelper.returnJSON("0000", "预约成功");
            }
            if (i == 2) {
                return BwJsonHelper.returnJSON("1001", "已预约");
            }
            return BwJsonHelper.returnJSON("1001", "预约失败");
        } catch (Exception e) {
            logger.error("reserve={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 查询电影详情
     *
     * @param userId
     * @param movieId
     * @return
     */
    @GET
    @Path(value = "/v2/findMoviesDetail")
    @Produces("text/html;charset=UTF-8")
    public String findMoviesDetailById(@HeaderParam("userId") int userId, @QueryParam("movieId") int movieId) {
        try {
            MovieDetailVo moviesDetail = movieRpcService.findMoviesDetailById(userId, movieId);
            if (moviesDetail == null) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", moviesDetail);
        } catch (Exception e) {
            logger.error("findMoviesDetailById={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 根据电影的id查询电影评论
     *
     * @param movieId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/findAllMovieComment")
    @Produces("text/html;charset=UTF-8")
    public String findAllMovieCommentById(@HeaderParam("userId") int userId, @QueryParam("movieId") int movieId,
                                          @QueryParam("page") int page, @QueryParam("count") int count) {
        logger.info("findAllMovieComment：userId={},movieId={},page={},count={}", userId, movieId, page, count);
        try {
            if (movieId <= 0 || page <= 0 || count <= 0) {
                return BwJsonHelper.returnJSON("1001", "movieId或page或count不能为0或不合法");
            }
            List<MovieCommentVos> movieComment = movieRpcService.findAllMovieCommentById(userId, movieId, page, count);
            if (movieComment.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", movieComment);
        } catch (Exception e) {
            logger.error("findAllMovieCommentById：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 根据电影id,区域id 查询播放影院信息
     *
     * @param movieId
     * @param regionId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/findCinemasInfoByRegion")
    @Produces("text/html;charset=UTF-8")
    public String findCinemasInfoByRegion(@QueryParam("movieId") int movieId, @QueryParam("regionId") int regionId,
                                          @QueryParam("page") int page, @QueryParam("count") int count) {

        logger.info("findCinemasInfoByRegion: movieId={},regionId={},page={},count={}", movieId, regionId, page, count);
        try {
            if (movieId <= 0 || page <= 0 || count <= 0) {
                return BwJsonHelper.returnJSON("1001", "movieId或page或count不能为0或不合法");
            }
            List<CinemaVo> cinemasInfo = movieRpcService.findCinemasInfo(movieId, regionId, page, count);
            if (cinemasInfo.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", cinemasInfo);
        } catch (Exception e) {
            logger.error("findCinemasInfoByRegion：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }

    }

    /**
     * 2.0 根据电影id，时间 查询播放影院信息
     *
     * @param movieId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/findCinemasInfoByDate")
    @Produces("text/html;charset=UTF-8")
    public String findCinemasInfoByDate(@QueryParam("movieId") int movieId, @QueryParam("date") String date,
                                        @QueryParam("page") int page, @QueryParam("count") int count) {
        logger.info("findCinemasInfoByDate: movieId={},date={},page={},count={}", movieId, date, page, count);
        try {
            if (movieId <= 0 || page <= 0 || count <= 0) {
                return BwJsonHelper.returnJSON("1001", "movieId或page或count不能为0或不合法");
            }
            List<CinemaVo> cinemasInfo = movieRpcService.findCinemasInfoByDate(movieId, page, count);
            if (cinemasInfo.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", cinemasInfo);

        } catch (Exception e) {
            logger.error("findCinemasInfoByDate：{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 根据价格电影id 查询播放影院信息
     *
     * @param movieId
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/findCinemasInfoByPrice")
    @Produces("text/html;charset=UTF-8")
    public String findCinemasInfoByPrice(@QueryParam("movieId") int movieId,
                                         @QueryParam("page") int page, @QueryParam("count") int count) {
        logger.info("findCinemasInfoByPrice: movieId={},page={},count={}", movieId, page, count);
        try {
            if (movieId <= 0 || page <= 0 || count <= 0) {
                return BwJsonHelper.returnJSON("1001", "movieId或page或count不能为0或不合法");
            }
            List<CinemaVo> cinemasInfo = movieRpcService.findCinemasInfoByPrice(movieId, page, count);
            if (cinemasInfo.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "无数据");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", cinemasInfo);
        } catch (Exception e) {
            logger.error("findCinemasInfoByPrice:{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 根据影厅id 查询座位信息
     *
     * @param hallId
     * @return
     */
    @GET
    @Path(value = "/v2/findSeatInfo")
    @Produces("text/html;charset=UTF-8")
    public String findSeatInfo(@QueryParam("hallId") int hallId) {
        logger.info("findSeatInfo: hallId={}", hallId);
        try {
            List<CinemaHallSeatVo> cinemaHallSeat = movieRpcService.findSeatInfo(hallId);
            return BwJsonHelper.returnJSON("0000", "查询成功", cinemaHallSeat);
        } catch (Exception e) {
            logger.error("findSeatInfo:{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 购票下单
     */
    @POST
    @Path(value = "/v2/verify/buyMovieTickets")
    @Produces("text/html;charset=UTF-8")
    public String buyMovieTickets(@HeaderParam("userId") int userId, @FormParam("scheduleId") int scheduleId,
                                  @FormParam("seat") String seat,
                                  @FormParam("sign") String sign) {
        logger.info("buyMovieTickets: userId={},scheduleId ={},seat={},sign={}",
                userId, scheduleId, seat, sign);
        try {

            if (scheduleId <= 0 || seat == null || "".equals(seat) || sign == null || "".equals(sign)) {
                return BwJsonHelper.returnJSON("1001", "入参不能为空");
            }

            String[] split = seat.split(",");
            int amount = split.length;

            //校验签名
            StringBuffer sb = new StringBuffer();
            sb.append(userId);
            sb.append(scheduleId);
            sb.append("movie");
            String mySign = WebUtil.MD5(sb.toString());

            if (!mySign.equals(sign)) {
                return BwJsonHelper.returnJSON("1001", "签名不对");
            }

            String orderId = movieRpcService.buyMovieTickets(userId, scheduleId, seat, amount);

            if ("1".equals(orderId)) {
                return BwJsonHelper.returnJSON("1001", "下单失败", "未找到该电影相关排期");
            }
            if ("2".equals(orderId)) {
                BwJsonHelper.returnJSON("1001", "订单创建失败");
            }
            return BwJsonHelper.returnJSON("0000", "下单成功", "orderId", orderId);
        } catch (Exception e) {
            logger.error("findSeatInfo:{}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }


    /**
     * 2.0 支付
     *
     * @param userId
     * @param orderId
     * @param payType
     * @param request
     * @return
     */
    @POST
    @Path(value = "/v2/verify/pay")
    @Produces("text/html;charset=UTF-8")
    public String payV2(
            @HeaderParam("userId") int userId,
            @FormParam("orderId") String orderId,
            @FormParam("payType") int payType,
            @Context HttpServletRequest request) {
        try {
            logger.info("payV2: userId={},orderId ={},payType={}", userId, orderId, payType);
            //获取用户真实IP
            String ip = WebUtil.getIpAddress(request);
            logger.info("获取用户真实IP为={}", ip);
            String result = movieRpcService.pay(orderId, payType, ip);
            if ("1001".equals(result)) {
                return BwJsonHelper.returnJSON("1001", "支付失败", "未找到该订单");
            }
            logger.info("返回客户端支付数据={}", result);
            return result;
        } catch (Exception e) {
            logger.error("pay={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 修改座位状态
     *
     * @param hallId
     * @param row
     * @param seat
     * @param status
     * @return
     */
    @POST
    @Path(value = "/v2/updateSeatStatus")
    @Produces("text/html;charset=UTF-8")
    public String updateSeatStatus(@QueryParam("hallId") int hallId, @QueryParam("row") int row,
                                   @QueryParam("seat") int seat, @QueryParam("status") int status) {

        logger.info("updateSeatStatus: hallId={},row={},seat={},status={}", hallId, row, seat, status);

        try {
            int i = movieRpcService.updateSeatStatus(hallId, row, seat, status);
            if (i == 1) {
                BwJsonHelper.returnJSON("0000", "修改成功");
            }
            return BwJsonHelper.returnJSON("1001", "修改失败");
        } catch (Exception e) {
            logger.error("updateSeatStatus={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }
    }

    /**
     * 2.0 根据关键字查询电影信息
     *
     * @param keyword
     * @param page
     * @param count
     * @return
     */
    @GET
    @Path(value = "/v2/findMovieByKeyword")
    @Produces("text/html;charset=UTF-8")
    public String findMovieByKeyword(@QueryParam("keyword") String keyword, @QueryParam("page") int page, @QueryParam("count") int count) {
        logger.info("findMovieByKeyword: keyword={},page={},count={}", keyword, page, count);
        try {
            List<MovieInfoVo> movieByKeyword = movieRpcService.findMovieByKeyword(keyword, page, count);
            if (movieByKeyword.isEmpty()) {
                return BwJsonHelper.returnJSON("0000", "未查到相关电影");
            }
            return BwJsonHelper.returnJSON("0000", "查询成功", movieByKeyword);
        } catch (Exception e) {
            logger.error("findMovieByKeyword={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }

    }

    /**
     * 2.0 根据电影ID和影院ID查询电影排期列表
     *
     * @param movieId
     * @param cinemaId
     * @return
     */
    @GET
    @Path(value = "/v2/findMovieSchedule")
    @Produces("text/html;charset=UTF-8")
    public String findMovieSchedule(@QueryParam("movieId") int movieId, @QueryParam("cinemaId") int cinemaId) {
        logger.info("findMovieSchedule:movieId={},cinemaId={}", movieId, cinemaId);
        try {
            List<MovieCinemaScheduleVo> movieScheduleList = movieRpcService.findMovieScheduleListV2(movieId, cinemaId);
            return BwJsonHelper.returnJSON("0000", "查询成功", movieScheduleList);
        } catch (Exception e) {
            logger.error("findMovieSchedule={}", e);
            return BwJsonHelper.returnJSON("1001", "网络异常");
        }


    }


}
