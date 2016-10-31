package comalexpolyanskyi.github.foodandhealth.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.App;
import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.RecipesModel;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.RecipesListFragment;


public class RecipesRVAdapter extends RecyclerView.Adapter<RecipesRVAdapter.ViewHolder> implements ItemTouchHelperAdapter  {


    private static String[] IMAGE_URLS =
                       {
                               "http://shushi168.com/data/out/114/36276270-image.png",
                               "http://makeitlast.se/wp-content/uploads/2015/10/loppis_12.jpg",
                               "https://images-na.ssl-images-amazon.com/images/G/01/img15/pet-products/small-tiles/30423_pets-products_january-site-flip_3-cathealth_short-tile_592x304._CB286975940_.jpg",
                               "https://IngredientListAdapter-media-cache-ak0.pinimg.com/236x/8a/1b/7c/8a1b7c35091025bf2417ce2d9a6b058d.jpg",
                               "https://cnet4.cbsistatic.com/hub/i/2011/10/27/a66dfbb7-fdc7-11e2-8c7c-d4ae52e62bcc/android-wallpaper5_2560x1600_1.jpg",
                               "https://www.android.com/static/img/home/more-from-2.png",
                               "http://www.howtablet.ru/wp-content/uploads/2016/04/%D0%9E%D0%B1%D0%BD%D0%BE%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5-Android-6.0.1-Marshmallow.jpg",
                               "http://keddr.com/wp-content/uploads/2015/12/iOS-vs-Android.jpg",
                               "http://shushi168.com/data/out/8/37224223-android-wallpaper.jpg",
                               "https://www.android.com/static/img/history/features/feature_icecream_3.png",
                               "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRfZ5OiAt7GIz57jyvjK8ca82pIvgd7pvD-3JyPG73ppN8FbqpbUA",
                               "http://androidwallpape.rs/content/02-wallpapers/131-night-sky/wallpaper-2707591.jpg",
                               "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTKEzvQjaPFW6s02YqZkmyb7qZMiNEhFkRCkhpoOyYnU-z8OzYL8g",
                               "http://shushi168.com/data/out/179/35958912-picture.png",
                               "http://bjstlh.com/data/wallpapers/58/WDF_1038969.jpg",
                               "https://c2.staticflickr.com/2/1520/24330829813_944c817720_b.jpg",
                               "http://shushi168.com/data/out/179/37944388-picture.jpg"
                       };

    private final List<RecipesModel.DummyItem> mValues;
    private final RecipesListFragment.OnListFragmentInteractionListener mListener;
    private AntiMalevich imageLoader;

    public RecipesRVAdapter(List<RecipesModel.DummyItem> items, RecipesListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        imageLoader = App.getMalevich();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int i;
        if(position < IMAGE_URLS.length){
            i = position;
        }else{
            i = position / IMAGE_URLS.length;
        }
        holder.mIdView.setTag(mValues.get(position).content);
        imageLoader.loadImageFromUrl(IMAGE_URLS[i], holder.mImageView);
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    v.setTag(holder.mItem);
                    mListener.onRecipesFragmentInteraction(v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mValues, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mValues, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mIdView;
        TextView mContentView;
        ImageView mImageView;
        RecipesModel.DummyItem mItem;

        ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.imageView);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
