package zup.com.br.zplay.services.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public class MovieSearchDTO implements Serializable {

    private String title;
    private String year;
    private String imdbID;
    private String type;
    private String poster;
}
