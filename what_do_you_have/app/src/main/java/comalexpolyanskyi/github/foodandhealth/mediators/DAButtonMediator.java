package comalexpolyanskyi.github.foodandhealth.mediators;


import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.DAButtonDAO;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class DAButtonMediator implements InteractionContract.Mediator<String>, InteractionContract.RequiredPresenter<Void> {

    private InteractionContract.RequiredView<Void> view;
    private InteractionContract.DAO<String> dao;

    public DAButtonMediator(final InteractionContract.RequiredView<Void> view) {
        this.view = view;
        this.dao = new DAButtonDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        final String url = ApiConstants.API_BASE_URL +
                ApiConstants.API_ARTICLES_DESC + parameters[0] +
                ApiConstants.API_BY_AUTH + parameters[1] +
                ApiConstants.API_ACT + parameters[2];
        dao.get(url, true);
    }

    @Override
    public void onError() {
        view.returnError(ContextHolder.getContext().getString(R.string.error_loading));
    }

    @Override
    public void onSuccess(Void response) {
        view.returnData(null);
    }

    @Override
    public void search(String... searchParameter) {

    }

    @Override
    public void onDestroy() {

    }
}
