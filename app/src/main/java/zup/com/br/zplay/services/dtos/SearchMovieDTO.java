package zup.com.br.zplay.services.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SearchMovieDTO implements Serializable {

    private static final long serialVersionUID = 5535515462418260226L;

    private List<MovieSearchDTO> Search = new ArrayList<>();
    private String totalResults;
    private String Response;

}
