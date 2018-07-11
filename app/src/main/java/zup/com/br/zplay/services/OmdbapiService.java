package zup.com.br.zplay.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import zup.com.br.zplay.services.dtos.MovieSearchDTO;

public interface OmdbapiService {

    @GET()
    Call<List<MovieSearchDTO>> obterLista(@Query("s") String search);

    @GET()
    Call<MovieSearchDTO> obterFilme(@Query("t") String imdbID);

}
