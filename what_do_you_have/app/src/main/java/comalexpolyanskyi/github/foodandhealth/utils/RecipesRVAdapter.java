package comalexpolyanskyi.github.foodandhealth.utils;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.RecipesModel;
import comalexpolyanskyi.github.foodandhealth.presenter.ImageLoader;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.RecipesListFragment;


public class RecipesRVAdapter extends RecyclerView.Adapter<RecipesRVAdapter.ViewHolder> implements ItemTouchHelperAdapter  {

    private final List<RecipesModel.DummyItem> mValues;
    private final RecipesListFragment.OnListFragmentInteractionListener mListener;
    private ImageLoader imageLoader;

    public RecipesRVAdapter(List<RecipesModel.DummyItem> items, RecipesListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        imageLoader = new ImageLoader(ContextCompat.getDrawable(ContextHolder.getContext(), R.mipmap.images));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        imageLoader.loadImageFromUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTKEzvQjaPFW6s02YqZkmyb7qZMiNEhFkRCkhpoOyYnU-z8OzYL8g", holder.mImageView);
        holder.mItem = mValues.get(position);
        holder.mIdView.setTag(mValues.get(position).content);
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
