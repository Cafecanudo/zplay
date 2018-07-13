package zup.com.br.zplay.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;

@Data
@Entity(nameInDb = "movie")
public class MovieEntity implements Serializable {

    private static final long serialVersionUID = 3081108693541396667L;
    @Id(autoincrement = true)
    private Long   id;
    @NotNull
    @Unique
    private String imdbId;
    @NotNull
    private String type;
    private String production;
    @NotNull
    private String title;
    @NotNull
    private String year;
    @NotNull
    private String time;
    private String director;
    @NotNull
    private String writer;
    private String actors;
    private String url_post;
    private String plot;
    @NotNull
    private String genre;
    @NotNull
    private String language;
    @NotNull
    private String score;
    @NotNull
    private String rank;
    private String votes;
    private Date   dataCadastro;
    @Generated(hash = 1307505904)
    public MovieEntity(Long id, @NotNull String imdbId, @NotNull String type,
            String production, @NotNull String title, @NotNull String year,
            @NotNull String time, String director, @NotNull String writer,
            String actors, String url_post, String plot, @NotNull String genre,
            @NotNull String language, @NotNull String score, @NotNull String rank,
            String votes, Date dataCadastro) {
        this.id = id;
        this.imdbId = imdbId;
        this.type = type;
        this.production = production;
        this.title = title;
        this.year = year;
        this.time = time;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.url_post = url_post;
        this.plot = plot;
        this.genre = genre;
        this.language = language;
        this.score = score;
        this.rank = rank;
        this.votes = votes;
        this.dataCadastro = dataCadastro;
    }
    @Generated(hash = 1226161063)
    public MovieEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getImdbId() {
        return this.imdbId;
    }
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getProduction() {
        return this.production;
    }
    public void setProduction(String production) {
        this.production = production;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getYear() {
        return this.year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getDirector() {
        return this.director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getWriter() {
        return this.writer;
    }
    public void setWriter(String writer) {
        this.writer = writer;
    }
    public String getActors() {
        return this.actors;
    }
    public void setActors(String actors) {
        this.actors = actors;
    }
    public String getUrl_post() {
        return this.url_post;
    }
    public void setUrl_post(String url_post) {
        this.url_post = url_post;
    }
    public String getPlot() {
        return this.plot;
    }
    public void setPlot(String plot) {
        this.plot = plot;
    }
    public String getGenre() {
        return this.genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getLanguage() {
        return this.language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getScore() {
        return this.score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public String getRank() {
        return this.rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public String getVotes() {
        return this.votes;
    }
    public void setVotes(String votes) {
        this.votes = votes;
    }
    public Date getDataCadastro() {
        return this.dataCadastro;
    }
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
