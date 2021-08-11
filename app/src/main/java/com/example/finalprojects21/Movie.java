package com.example.finalprojects21;
/**
 *@Preeti Kumari
 *@version1.0
 */

import java.io.Serializable;

public class Movie implements Serializable {

    private String tile;
    private String year;
    private String rated;
    private String releaseDate;
    private String posterUrl;

    public String getTile() {
        return tile;
    }

    public String getYear() {
        return year;
    }

    public String getRated() {
        return rated;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

}
