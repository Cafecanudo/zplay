package zup.com.br.zplay.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zup.com.br.zplay.R;
import zup.com.br.zplay.activities.SupportActivity;
import zup.com.br.zplay.adapters.SearchMovieAdapter;
import zup.com.br.zplay.services.OmdbapiService;
import zup.com.br.zplay.services.dtos.MovieSearchDTO;
import zup.com.br.zplay.services.dtos.SearchMovieDTO;
import zup.com.br.zplay.utils.AppClient;
import zup.com.br.zplay.utils.RecyclerItemClickListener;

public class DialogSearch extends Dialog {

    @BindView(R.id.editSearch)
    public TextView editSearch;

    @BindView(R.id.textNoFound)
    public TextView textNoFound;

    @BindView(R.id.progressBar)
    public LinearLayout progressBar;

    @BindView(R.id.groupSearch)
    public LinearLayout groupSearch;

    @BindView(R.id.groupBtnNavigate)
    public LinearLayout groupBtnNavigate;

    @BindView(R.id.groupListNavigate)
    public LinearLayout groupListNavigate;

    @BindView(R.id.textQuandade)
    public TextView textQuandade;

    @BindView(R.id.textPage)
    public TextView textPage;

    @BindView(R.id.btnBack)
    public View btnBack;

    @BindView(R.id.btnAnterior)
    public View btnAnterior;

    @BindView(R.id.btnProximo)
    public View btnProximo;

    @BindView(R.id.listMovie)
    public RecyclerView listMovie;

    private int maxPages    = 1;
    private int currentPage = 1;
    private String textSearch;

    Call<SearchMovieDTO> consultarFilme;
    private SearchMovieAdapter   searchMovieAdapter;
    private List<MovieSearchDTO> movieSearchDTOList;
    private SupportActivity      activity;

    private DialogSearch(@NonNull SupportActivity activity) {
        super(activity);
        this.activity = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_seach, null);
        setContentView(view);

        ButterKnife.bind(this, view);

        this.movieSearchDTOList = new ArrayList<>();
        this.searchMovieAdapter = new SearchMovieAdapter(activity, movieSearchDTOList);

        resets();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 1);
        listMovie.setLayoutManager(mLayoutManager);
        listMovie.setItemAnimator(new DefaultItemAnimator());
        listMovie.setAdapter(searchMovieAdapter);

        listMovie.addOnItemTouchListener(new RecyclerItemClickListener(activity, (_view, position) -> DialogMovieAdd.show(movieSearchDTOList.get(position).getImdbID(), activity)));
    }

    public static DialogSearch show(@NonNull SupportActivity activity) {
        DialogSearch dialog = new DialogSearch(activity);
        dialog.show();
        return dialog;
    }

    @OnClick(R.id.progressBar)
    public void progressBarOnClick() {
        consultarFilme.cancel();
        groupSearch.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnSearch)
    public void btnSearchOnClick() {
        resets();

        this.textSearch = editSearch.getText().toString();
        if (this.textSearch.length() >= 4) {
            pesquisarFilmeAPI(this.textSearch, this.currentPage);
        } else {
            Toast.makeText(this.getContext(), R.string.char_min_4, Toast.LENGTH_LONG).show();
        }
    }

    private void resets() {
        maxPages = 1;
        currentPage = 1;

        progressBar.setVisibility(View.GONE);
        textNoFound.setVisibility(View.GONE);

        groupListNavigate.setVisibility(View.GONE);
        groupBtnNavigate.setVisibility(View.GONE);

        groupSearch.setVisibility(View.VISIBLE);

        movieSearchDTOList.clear();
        searchMovieAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btnBack, R.id.btnAnterior, R.id.btnProximo})
    public void btnSearchOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack: {
                resets();
                break;
            }
            case R.id.btnAnterior: {
                pesquisarFilmeAPI(this.textSearch, --this.currentPage);
                break;
            }
            case R.id.btnProximo: {
                pesquisarFilmeAPI(this.textSearch, ++this.currentPage);
                break;
            }

        }
    }

    private void populateListMovie(SearchMovieDTO searchMovieDTO) {
        progressBar.setVisibility(View.GONE);
        if (!searchMovieDTO.getSearch().isEmpty()) {
            movieSearchDTOList.clear();
            movieSearchDTOList.addAll(searchMovieDTO.getSearch());
            searchMovieAdapter.notifyDataSetChanged();

            groupBtnNavigate.setVisibility(View.GONE);
            groupListNavigate.setVisibility(View.VISIBLE);
            textQuandade.setText(searchMovieDTO.getTotalResults() + " registros.");
            textQuandade.setVisibility(View.VISIBLE);

            int quantidade = Integer.valueOf(searchMovieDTO.getTotalResults());
            this.maxPages = (int) Math.ceil(quantidade / searchMovieDTO.getSearch().size());

            if (quantidade > searchMovieDTO.getSearch().size()) {
                groupBtnNavigate.setVisibility(View.VISIBLE);
                textPage.setText(this.currentPage + "/" + this.maxPages);

                btnAnterior.setEnabled(true);
                btnAnterior.setAlpha(1F);
                if (this.currentPage == 1) {
                    btnAnterior.setAlpha(0.4F);
                    btnAnterior.setEnabled(false);
                }

                btnProximo.setEnabled(true);
                btnProximo.setAlpha(1F);
                if (this.currentPage >= this.maxPages) {
                    btnProximo.setAlpha(0.4F);
                    btnProximo.setEnabled(false);
                }
            }
        } else {
            editSearch.setText(null);
            groupSearch.setVisibility(View.VISIBLE);
            textNoFound.setVisibility(View.VISIBLE);
            groupListNavigate.setVisibility(View.GONE);
        }
    }

    private void pesquisarFilmeAPI(String filme, int page) {
        hideKeyboard();


        page = page <= 0 ? 1 : page > this.maxPages ? this.maxPages : page;
        groupSearch.setVisibility(View.GONE);
        textNoFound.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        OmdbapiService omdbapiService = AppClient.getClient().create(OmdbapiService.class);
        consultarFilme = omdbapiService.obterListaPagina(filme, page);
        consultarFilme.enqueue(new Callback<SearchMovieDTO>() {
            @Override
            public void onResponse(Call<SearchMovieDTO> call, Response<SearchMovieDTO> response) {
                if (response.isSuccessful() && response.body().getResponse().equalsIgnoreCase("true")) {
                    populateListMovie(response.body());
                } else {
                    groupSearch.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    groupListNavigate.setVisibility(View.GONE);

                    Toast.makeText(DialogSearch.this.getContext(), R.string.algo_errado, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchMovieDTO> call, Throwable t) {
                groupSearch.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                groupListNavigate.setVisibility(View.GONE);

                if (call.isExecuted()) {
                    return;
                }
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(DialogSearch.this.getContext(), R.string.conexao_lenta, Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(DialogSearch.this.getContext(), R.string.algo_errado, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hideKeyboard() {
        ((InputMethodManager) this.activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
    }
}
