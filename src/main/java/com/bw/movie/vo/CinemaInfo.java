package com.bw.movie.vo;

public class CinemaInfo {
    //自增id
    private int id;
    //电影院名称
    private String name;
    //电影院地址
    private String address;
    //电影院电话
    private String phone;
    //营业时间文案
    private String businessHoursContent;
    //行车路线
    private String vehicleRoute;
    //影院商标
    private String logo;
    private int followCinema;
    //评论总数
    private int commentTotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessHoursContent() {
        return businessHoursContent;
    }

    public void setBusinessHoursContent(String businessHoursContent) {
        this.businessHoursContent = businessHoursContent;
    }

    public String getVehicleRoute() {
        return vehicleRoute;
    }

    public void setVehicleRoute(String vehicleRoute) {
        this.vehicleRoute = vehicleRoute;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getFollowCinema() {
        return followCinema;
    }

    public void setFollowCinema(int followCinema) {
        this.followCinema = followCinema;
    }

    public int getCommentTotal() {
        return commentTotal;
    }

    public void setCommentTotal(int commentTotal) {
        this.commentTotal = commentTotal;
    }

    @Override
    public String toString() {
        return "CinemaInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", businessHoursContent='" + businessHoursContent + '\'' +
                ", vehicleRoute='" + vehicleRoute + '\'' +
                ", logo='" + logo + '\'' +
                ", followCinema=" + followCinema +
                ", commentTotal=" + commentTotal +
                '}';
    }
}
