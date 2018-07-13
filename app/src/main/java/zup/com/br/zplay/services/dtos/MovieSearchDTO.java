package zup.com.br.zplay.services.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public class MovieSearchDTO implements Serializable {

    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;
}
