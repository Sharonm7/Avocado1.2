package com.example.avocado1;

class Movie {

    private int id;
    private boolean video;
    private double vote_average;
    private String title;
    private double popularity;
    private String poster_path;
    private String original_language;
    private String overview;
    private String release_date;
//  private int vote_count;
//  private String original_title;
//  private List<Integer> genre_ids;
//  private String backdrop_path;
//  private boolean adult;


    Movie(String video, String vote_average, String title, String poster_path, String overview, String release_date) {

        this.video = Boolean.parseBoolean(video);
        this.vote_average = Double.parseDouble(vote_average);
        this.title = title;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.overview = overview;
        this.release_date = release_date;
    }

    int getId() {
        return id;
    }

    boolean isVideo() {
        return video;
    }

    double getVote_average() {
        return vote_average;
    }

    String getTitle() {
        return title;
    }

    double getPopularity() {
        return popularity;
    }

    String getPoster_path() {
        return poster_path;
    }

    String getOriginal_language() {
        return original_language;
    }

    String getOverview() {
        return overview;
    }

    String getRelease_date() {
        return release_date;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", video=" + video +
                ", vote_average=" + vote_average +
                ", title='" + title + '\'' +
                ", popularity=" + popularity +
                ", poster_path='" + poster_path + '\'' +
                ", original_language='" + original_language + '\'' +
                ", overview='" + overview + '\'' +
                ", release_date='" + release_date + '\'' +
                '}';
    }
}


