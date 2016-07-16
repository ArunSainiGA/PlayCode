package com.cycus.playcodeapp.SetterGetter;

/**
 * Created by Arun_Saini on 18-06-2016.
 */
public class GamesBean {
    float gamePrice;
    int gameId;
    int gameRating;
    String gameTitle="";
    String gameThumbnail;
    String gameDesc;
    String gameShortDesc;
    int catId;
    String catName;
    int serviceId;
    String screen1;
    String screen2;
    String screen3;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public float getGamePrice() {
        return gamePrice;
    }

    public void setGamePrice(float gamePrice) {
        this.gamePrice = gamePrice;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameRating() {
        return gameRating;
    }

    public void setGameRating(int gameRating) {
        this.gameRating = gameRating;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGameThumbnail() {
        return gameThumbnail;
    }

    public void setGameThumbnail(String gameThumbnail) {
        this.gameThumbnail = gameThumbnail;
    }

    public String getGameDesc() {
        return gameDesc;
    }

    public void setGameDesc(String gameDesc) {
        this.gameDesc = gameDesc;
    }

    public String getGameShortDesc() {
        return gameShortDesc;
    }

    public void setGameShortDesc(String gameShortDesc) {
        this.gameShortDesc = gameShortDesc;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getScreen1() {
        return screen1;
    }

    public void setScreen1(String screen1) {
        this.screen1 = screen1;
    }

    public String getScreen2() {
        return screen2;
    }

    public void setScreen2(String screen2) {
        this.screen2 = screen2;
    }

    public String getScreen3() {
        return screen3;
    }

    public void setScreen3(String screen3) {
        this.screen3 = screen3;
    }
}
