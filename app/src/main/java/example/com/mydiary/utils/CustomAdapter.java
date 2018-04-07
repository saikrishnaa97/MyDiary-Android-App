package example.com.mydiary.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by saikr on 01-04-2018.
 */

public abstract class CustomAdapter<T extends CustomViewHolder> extends RecyclerView.Adapter<T> {

    protected boolean isHeaderSet;
    protected boolean isFooterSet;

    private View headerView;
    private View footerView;

    protected  CustomAdapter() {
        isHeaderSet = false;
        isFooterSet = false;
    }

    protected CustomAdapter(View headerView, View footerView) {
        if (null != headerView) {
            this.headerView = headerView;
            isHeaderSet = true;
        }
        if (null != footerView) {
            this.footerView = footerView;
            isFooterSet = true;
        }
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == Constants.Companion.getTYPE_HEADER() && isHeaderSet) {
            return (T) new CustomViewHolder(headerView);
        } else if (viewType == Constants.Companion.getTYPE_FOOTER() && isFooterSet) {
            return (T) new CustomViewHolder(footerView);
        }
        return onCreateViewHolderImplementation(inflater, parent, viewType);
    }

    // region onCreateViewHolder implementation of Recycler View
    protected abstract T onCreateViewHolderImplementation(LayoutInflater inflater, ViewGroup parent,
                                                          int viewType);

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (isHeaderSet) {
            onBindViewHolderImplement(holder, position - 1);
        } else {
            onBindViewHolderImplement(holder, position);
        }
    }

    // Regular onBindViewHolder implementation for Recycler view
    protected abstract void onBindViewHolderImplement(T holder, int position);

    @Override
    public int getItemCount() {
        int size = getItemCountImplementation();
        if (isHeaderSet) {
            size++;
        }
        if (isFooterSet) {
            size++;
        }
        return size;
    }

    // getItemCount implementation for recycler view
    protected abstract int getItemCountImplementation();

    @Override
    public int getItemViewType(int position) {
        if (isHeaderSet && isPositionHeader(position))
            return Constants.Companion.getTYPE_HEADER();
        if (isFooterSet && isPositionFooter(position))
            return Constants.Companion.getTYPE_FOOTER();
        return getItemViewTypeImplementation(isHeaderSet ? position - 1 : position);
    }

    // getItemViewType implementation for Recycler view
    protected abstract int getItemViewTypeImplementation(int position);

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return position == getItemCount() - 1;
    }


    /**
     * Gets item position in list. Use this method instead of getAdapterPosition() in onClicks
     *
     * @param holder items holder
     * @return position of the holder in list
     */
    protected int getItemPosition(T holder) {
        if (isHeaderSet) {
            return holder.getAdapterPosition() - 1;
        } else {
            return holder.getAdapterPosition();
        }
    }
}
