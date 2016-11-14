package comalexpolyanskyi.github.foodandhealth.utils.adapters;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import comalexpolyanskyi.github.foodandhealth.App;
import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments.BaseRVFragment;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;
import comalexpolyanskyi.github.foodandhealth.utils.imageloader.MySimpleImageLoader;

public class ArticleListFragmentAdapter extends AbstractAdapter<ArticleListItemDO>  {

    private Cursor cursor;
    private final BaseRVFragment.OnListFragmentInteractionListener listener;
    private MySimpleImageLoader imageLoader;

    public ArticleListFragmentAdapter(@NonNull Cursor items, @NonNull BaseRVFragment.OnListFragmentInteractionListener listener){
        cursor = items;
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

    public void swapCursor(Cursor cursor){
        if(cursor != null){
            this.cursor = cursor;
            notifyDataSetChanged();
        }
    }

    public ArticleListItemDO getItem(final int position) {
        cursor.moveToPosition(position);
        return new ArticleListItemDO(cursor);
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AbstractViewHolder(LayoutInflater.from(ContextHolder.getContext()).inflate(R.layout.fragment_article_list_item, parent, false), R.id.article_name, R.id.article_image, R.id.article_card);
    }

    public int getItemCount() {
        if(cursor != null){
            return cursor.getCount();
        }else{
            return 0;
        }
    }
}