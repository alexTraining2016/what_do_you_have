package comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapterUtil;

import android.content.Context;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;


public abstract class SectionCursorAdapter<T, S extends ViewHolder, H extends ViewHolder> extends CursorAdapter implements SectionIndexer {

    private static final String ERROR_ILLEGAL_STATE = "IllegalStateException during build sections. "
            + "More then likely your cursor has been disconnected from the database, so your cursor will be set to null. "
            + "In most cases your content observer has already been notified of a database change and SectionCursorAdapter should get a new cursor shortly.";

    private static final int NO_CURSOR_POSITION = -99;
    private static final int VIEW_TYPE_SECTION = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private int sectionLayoutResId;
    private int itemLayoutResId;
    private SortedMap<Integer, T> sectionMap = new TreeMap<>();
    private ArrayList<Integer> sectionList = new ArrayList<>();
    private Object[] fastScrollObjects;
    private LayoutInflater layoutInflater;

    public SectionCursorAdapter(Context context, Cursor cursor, int flags, int sectionLayoutResId, int itemLayoutResId) {
        super(context, cursor, flags);

        init(context, null, sectionLayoutResId, itemLayoutResId);
    }

    private void init(Context context, SortedMap<Integer, T> sections, int sectionLayoutResId, int itemLayoutResId) {
        this.sectionLayoutResId = sectionLayoutResId;
        this.itemLayoutResId = itemLayoutResId;
        layoutInflater = LayoutInflater.from(context);

        if (sections != null) {
            sectionMap = sections;
        } else {
            buildSections();
        }
    }

    private LayoutInflater getInflater() {
        return layoutInflater;
    }

    private void buildSections() {
        if (hasOpenCursor()) {
            final Cursor cursor = getCursor();
            cursor.moveToPosition(-1);

            try {
                sectionMap = buildSections(cursor);
            } catch (IllegalStateException e) {
                swapCursor(null);
                sectionMap = new TreeMap<>();
                return;
            }

            if (sectionMap == null) {
                sectionMap = new TreeMap<>();
            }
        }
    }

    private SortedMap<Integer, T> buildSections(Cursor cursor) throws IllegalStateException {
        final TreeMap<Integer, T> sections = new TreeMap<>();

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
        return super.getCount() + sectionMap.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int listPosition) {
        if (isSection(listPosition))
            return sectionMap.get(listPosition);
        else
            return super.getItem(getCursorPositionWithoutSections(listPosition));
    }

    @Override
    public long getItemId(int listPosition) {
        if (isSection(listPosition))
            return listPosition;
        else {
            final int cursorPosition = getCursorPositionWithoutSections(listPosition);
            final Cursor cursor = getCursor();

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
            fastScrollObjects = null;
            sectionList.clear();
        }
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        if (hasOpenCursor()) {
            buildSections();
            fastScrollObjects = null;
            sectionList.clear();
        }

        super.notifyDataSetInvalidated();
    }

    private boolean hasOpenCursor() {
        final Cursor cursor = getCursor();

        if (cursor != null && cursor.isClosed()) {
            swapCursor(null);
            return false;

        }
        return cursor != null;
    }

    private boolean isSection(int listPosition) {
        return sectionMap.containsKey(listPosition);
    }

    private int getCursorPositionWithoutSections(int listPosition) {

        if (sectionMap.size() == 0) {
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

    private int getSectionPosition(int listPosition) {
        boolean isSection = false;
        int numPrecedingSections = 0;
        for (Integer sectionPosition : sectionMap.keySet()) {
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
        boolean hasSections = sectionMap != null && sectionMap.size() > 0;

        return sectionIndex == 0 && hasSections && listPosition < sectionMap.firstKey();
    }

    @Override
    public int getItemViewType(int listPosition) {
        return isSection(listPosition) ? VIEW_TYPE_SECTION : VIEW_TYPE_ITEM;
    }

    @SuppressWarnings("unchecked")
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
            T section = sectionMap.get(position);
            bindSectionViewHolder(position, (S) view.getTag(), parent, section);
        } else {
            bindItemViewHolder((H) view.getTag(), cursor, parent);
        }

        return view;
    }

    private View newSectionView(ViewGroup parent, T section) {
        View view = getInflater().inflate(sectionLayoutResId, parent, false);
        view.setTag(createSectionViewHolder(view, section));

        return view;
    }

    protected abstract S createSectionViewHolder(View sectionView, T section);

    protected abstract void bindSectionViewHolder(int position, S sectionViewHolder, ViewGroup parent, T section);

    private View newItemView(Cursor cursor, ViewGroup parent) {
        View view = getInflater().inflate(itemLayoutResId, parent, false);
        view.setTag(createItemViewHolder(cursor, view));

        return view;
    }

    protected abstract H createItemViewHolder(Cursor cursor, View itemView);

    protected abstract void bindItemViewHolder(H itemViewHolder, Cursor cursor, ViewGroup parent);

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionList.size() == 0) {
            for (Integer key : sectionMap.keySet()) {
                sectionList.add(key);
            }
        }

        return sectionIndex < sectionList.size() ? sectionList.get(sectionIndex) : getCount();
    }

    @Override
    public int getSectionForPosition(int position) {
        final Object[] objects = getSections(); // the fast scroll section objects
        final int sectionIndex = getSectionPosition(position);

        return sectionIndex < objects.length ? sectionIndex : 0;
    }

    @Override
    public Object[] getSections() {
        if (fastScrollObjects == null) {
            fastScrollObjects = getFastScrollDialogLabels();
        }

        return fastScrollObjects;
    }

    protected int getMaxIndexerLength() {
        return 3;
    }

    private Object[] getFastScrollDialogLabels() {
        if (sectionMap == null) return new Object[]{};

        final int sectionCount = sectionMap.size();
        final String[] titles = new String[sectionCount];

        final int max = VERSION.SDK_INT < VERSION_CODES.KITKAT ? getMaxIndexerLength() : Integer.MAX_VALUE;

        int i = 0;
        for (Object object : sectionMap.values()) {
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