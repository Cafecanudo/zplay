package zup.com.br.zplay.services.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DetailMovieDTO implements Serializable {

    public String Error;
    public String Title;
    public String Year;
    public String Rated;
    public String Released;
    public String Runtime;
    public String Genre;
    public String Director;
    public String Writer;
    public String Actors;
    public String Plot;
    public String Language;
    public String Country;
    public String Awards;
    public String Poster;
    public String Metascore;
    public String Type;
    public String DVD;
    public String BoxOffice;
    public String Production;
    public String Website;
    public String Response;
    public String imdbRating;
    public String imdbVotes;
    public String imdbID;
    public List<RatingDTO> ratings = new ArrayList<>();
}
