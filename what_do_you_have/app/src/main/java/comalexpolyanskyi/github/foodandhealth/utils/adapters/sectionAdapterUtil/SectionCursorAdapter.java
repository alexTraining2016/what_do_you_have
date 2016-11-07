package comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapterUtil;

import android.content.Context;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import java.util.ArrayList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


public abstract class SectionCursorAdapter<T, S extends ViewHolder, H extends ViewHolder>  extends CursorAdapter implements SectionIndexer {

    private static final String ERROR_ILLEGAL_STATE = "IllegalStateException during build sections. "
            + "More then likely your cursor has been disconnected from the database, so your cursor will be set to null. "
            + "In most cases your content observer has already been notified of a database change and SectionCursorAdapter should get a new cursor shortly.";

    public static final int NO_CURSOR_POSITION = -99; // used when mapping section list position to cursor position

    protected static final int VIEW_TYPE_SECTION = 0;
    protected static final int VIEW_TYPE_ITEM = 1;

    private int mSectionLayoutResId;
    private int mItemLayoutResId;

    protected SortedMap<Integer, T> mSectionMap = new TreeMap<Integer, T>(); // should not be null
    ArrayList<Integer> mSectionList = new ArrayList<Integer>();
    private Object[] mFastScrollObjects;

    private LayoutInflater mLayoutInflater;

    public SectionCursorAdapter(Context context, Cursor cursor, int flags, int sectionLayoutResId, int itemLayoutResId) {
        super(context, cursor, flags);
        init(context, null, sectionLayoutResId, itemLayoutResId);
    }

    private void init(Context context, SortedMap<Integer, T> sections, int sectionLayoutResId, int itemLayoutResId) {
        this.mSectionLayoutResId = sectionLayoutResId;
        this.mItemLayoutResId = itemLayoutResId;
        mLayoutInflater = LayoutInflater.from(context);
        if (sections != null) {
            mSectionMap = sections;
        } else {
            buildSections();
        }
    }

    protected LayoutInflater getInflater() {
        return mLayoutInflater;
    }

    private void buildSections() {
        if (hasOpenCursor()) {
            Cursor cursor = getCursor();
            cursor.moveToPosition(-1);
            try {
                mSectionMap = buildSections(cursor);
            } catch (IllegalStateException e) {
                Log.w(SectionCursorAdapter.class.getName(), ERROR_ILLEGAL_STATE, e);
                swapCursor(null);
                mSectionMap = new TreeMap<Integer, T>();
                return;
            }
            if (mSectionMap == null) {
                mSectionMap = new TreeMap<Integer, T>();
            }
        }
    }

    protected SortedMap<Integer, T> buildSections(Cursor cursor) throws IllegalStateException {
        TreeMap<Integer, T> sections = new TreeMap<Integer, T>();
        int cursorPosition = 0;
        while (hasOpenCursor() && cursor.moveToNext()) {
            T section = getSectionFromCursor(cursor);
            if (cursor.getPosition() != cursorPosition)
                throw new IllegalCursorMovementException("Do no move the cursor's position in getSectionFromCursor.");
            if (!sections.containsValue(section))
                sections.put(cursorPosition + sections.size(), section);
            cursorPosition++;
        }
        return sections;
    }

    protected abstract T getSectionFromCursor(Cursor cursor) throws IllegalStateException;

    @Override
    public int getCount() {
        return super.getCount() + mSectionMap.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int listPosition) {
        if (isSection(listPosition))
            return mSectionMap.get(listPosition);
        else
            return super.getItem(getCursorPositionWithoutSections(listPosition));
    }

    @Override
    public long getItemId(int listPosition) {
        if (isSection(listPosition))
            return listPosition;
        else {
            int cursorPosition = getCursorPositionWithoutSections(listPosition);
            Cursor cursor = getCursor();
            if (hasOpenCursor() && cursor.moveToPosition(cursorPosition)) {
                return cursor.getLong(cursor.getColumnIndex("_id"));
            }
            return NO_CURSOR_POSITION;
        }
    }

    @Deprecated
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Deprecated
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    @Override
    public void notifyDataSetChanged() {
        if (hasOpenCursor()) {
            buildSections();
            mFastScrollObjects = null;
            mSectionList.clear();
        }
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        if (hasOpenCursor()) {
            buildSections();
            mFastScrollObjects = null;
            mSectionList.clear();
        }
        super.notifyDataSetInvalidated();
    }

    protected boolean hasOpenCursor() {
        Cursor cursor = getCursor();
        if (cursor != null && cursor.isClosed()) {
            swapCursor(null);
            return false;
        }
        return cursor != null;
    }

    public Set<Integer> getSectionListPositions() {
        return mSectionMap.keySet();
    }

    public T getSection(int sectionPosition) {
        if (mSectionList.contains(sectionPosition)) {
            return mSectionMap.get(mSectionList.get(sectionPosition));
        }
        return null;
    }

    public boolean isSection(int listPosition) {
        return mSectionMap.containsKey(listPosition);
    }

    public int getCursorPositionWithoutSections(int listPosition) {
        if (mSectionMap.size() == 0) {
            return listPosition;
        } else if (!isSection(listPosition)) {
            int sectionIndex = getSectionPosition(listPosition);
            if (isListPositionBeforeFirstSection(listPosition, sectionIndex)) {
                return listPosition;
            } else {
                return listPosition - (sectionIndex + 1);
            }
        } else {
            return NO_CURSOR_POSITION;
        }
    }

    public int getSectionPosition(int listPosition) {
        boolean isSection = false;
        int numPrecedingSections = 0;
        for (Integer sectionPosition : mSectionMap.keySet()) {
            if (listPosition > sectionPosition)
                numPrecedingSections++;
            else if (listPosition == sectionPosition)
                isSection = true;
            else
                break;
        }
        return isSection ? numPrecedingSections : Math.max(numPrecedingSections - 1, 0);
    }

    private boolean isListPositionBeforeFirstSection(int listPosition, int sectionIndex) {
        boolean hasSections = mSectionMap != null && mSectionMap.size() > 0;
        return sectionIndex == 0 && hasSections && listPosition < mSectionMap.firstKey();
    }

    @Override
    public int getItemViewType(int listPosition) {
        return isSection(listPosition) ? VIEW_TYPE_SECTION : VIEW_TYPE_ITEM;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean isSection = isSection(position);
        Cursor cursor = getCursor();
        View view;

        if (!isSection) {
            int newPosition = getCursorPositionWithoutSections(position);
            if (!hasOpenCursor()) {
                // This only happens when the scroll is super fast and someone backs out.
                return new View(parent.getContext());
            } else if (!cursor.moveToPosition(newPosition)) {
                throw new IllegalStateException("couldn't move cursor to position " + newPosition);
            }
        }

        if (convertView == null) {
            view = isSection ? newSectionView(parent, (T) getItem(position))
                    : newItemView(cursor, parent);
        } else {
            view = convertView;
        }

        if (isSection) {
            T section = mSectionMap.get(position);
            bindSectionViewHolder(position, (S) view.getTag(), parent, section);
        } else {
            bindItemViewHolder((H) view.getTag(), cursor, parent);
        }

        return view;
    }

    protected View newSectionView(ViewGroup parent, T section) {
        View view = getInflater().inflate(mSectionLayoutResId, parent, false);
        view.setTag(createSectionViewHolder(view, section));

        return view;
    }

    protected abstract S createSectionViewHolder(View sectionView, T section);

    protected abstract void bindSectionViewHolder(int position, S sectionViewHolder, ViewGroup parent, T section);

    protected View newItemView(Cursor cursor, ViewGroup parent) {
        View view = getInflater().inflate(mItemLayoutResId, parent, false);
        view.setTag(createItemViewHolder(cursor, view));

        return view;
    }

    protected abstract H createItemViewHolder(Cursor cursor, View itemView);

    protected abstract void bindItemViewHolder(H itemViewHolder, Cursor cursor, ViewGroup parent);

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (mSectionList.size() == 0) {
            for (Integer key : mSectionMap.keySet()) {
                mSectionList.add(key);
            }
        }
        return sectionIndex < mSectionList.size() ? mSectionList.get(sectionIndex) : getCount();
    }

    @Override
    public int getSectionForPosition(int position) {
        Object[] objects = getSections(); // the fast scroll section objects
        int sectionIndex = getSectionPosition(position);

        return sectionIndex < objects.length ? sectionIndex : 0;
    }

    @Override
    public Object[] getSections() {
        if (mFastScrollObjects == null) {
            mFastScrollObjects = getFastScrollDialogLabels();
        }
        return mFastScrollObjects;
    }

    protected int getMaxIndexerLength() {
        return 3;
    }

    private Object[] getFastScrollDialogLabels() {
        if (mSectionMap == null) return new Object[] { };

        int sectionCount = mSectionMap.size();
        String[] titles = new String[sectionCount];

        int max = VERSION.SDK_INT < VERSION_CODES.KITKAT ? getMaxIndexerLength() : Integer.MAX_VALUE;
        int i = 0;
        for (Object object : mSectionMap.values()) {
            if (object == null) {
                titles[i] = "";
            } else if (object.toString().length() >= max) {
                titles[i] = object.toString().substring(0, max);
            } else {
                titles[i] = object.toString();
            }
            i++;
        }
        return titles;
    }
}