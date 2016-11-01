package comalexpolyanskyi.github.foodandhealth.utils.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import comalexpolyanskyi.github.foodandhealth.App;
import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.models.dataObjects.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.ArticleListFragment;
import comalexpolyanskyi.github.foodandhealth.utils.MySimpleImageLoader;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class ArticleListFragmentAdapter extends AbstractAdapter<ArticleListItemDO>  {

    private final List<ArticleListItemDO> values;
    private final ArticleListFragment.OnListFragmentInteractionListener listener;
    private MySimpleImageLoader imageLoader;

    public ArticleListFragmentAdapter(@NonNull List<ArticleListItemDO> items, @NonNull ArticleListFragment.OnListFragmentInteractionListener listener){
        values = items;
        this.listener = listener;
        imageLoader = App.getImageLoader();
    }

    @Override
    public void onBind(final AbstractViewHolder holder, final ArticleListItemDO data, int position, int viewType) {
        holder.<TextView>get(R.id.article_name).setText(data.getName());
        imageLoader.loadImageFromUrl(data.getPhotoUrl(), holder.<ImageView>get(R.id.article_image));
        holder.<View>get(R.id.article_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(data.getId());
                listener.onRecipesFragmentInteraction(v);
            }
        });
    }

    public ArticleListItemDO getItem(final int position) {
        return values.get(position);
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AbstractViewHolder(LayoutInflater.from(ContextHolder.getContext()).inflate(R.layout.fragment_article_list_item, parent, false), R.id.article_name, R.id.article_image, R.id.article_card);
    }

    public int getItemCount() {
        return values.size();
    }
}