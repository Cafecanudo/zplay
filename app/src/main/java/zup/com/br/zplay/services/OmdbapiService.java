package zup.com.br.zplay.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import zup.com.br.zplay.services.dtos.DetailMovieDTO;
import zup.com.br.zplay.services.dtos.SearchMovieDTO;

import static zup.com.br.zplay.utils.AppClient.API_KEY;

public interface OmdbapiService {

    @GET("?apikey=" + API_KEY)
    Call<SearchMovieDTO> obterListaPagina(@Query("s") String search, @Query("page") int page);

    @GET("?apikey=" + API_KEY)
    Call<DetailMovieDTO> obterFilmePorID(@Query("i") String imdbID);

}
