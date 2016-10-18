package comalexpolyanskyi.github.foodandhealth.utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.RecipesModel;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.ListFragment;


public class RecipesRVAdapter extends RecyclerView.Adapter<RecipesRVAdapter.ViewHolder> {

    private final List<RecipesModel.DummyItem> mValues;
    private final ListFragment.OnListFragmentInteractionListener mListener;
    private Activity activity;

    public RecipesRVAdapter(List<RecipesModel.DummyItem> items, ListFragment.OnListFragmentInteractionListener listener, Activity activity) {
        this.activity = activity;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.imageView.setImageFromUrl("", activity);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final SuperImageView imageView;
        final TextView mIdView;
        final TextView mContentView;
        RecipesModel.DummyItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (SuperImageView) view.findViewById(R.id.test);
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
