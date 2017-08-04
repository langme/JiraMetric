package com.langme.newmetrics;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.langme.newmetrics.DetailSheetFragment.OnListFragmentInteractionListener;
import com.langme.newmetrics.dummy.DetailContent.DetailItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DetailItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDetailSheetRecyclerViewAdapter extends RecyclerView.Adapter<MyDetailSheetRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private final List<DetailItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyDetailSheetRecyclerViewAdapter(List<DetailItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_detailsheet, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getNumber()));
        holder.mNameView.setText(mValues.get(position).nameTask);
        //holder.mCreateDateView.setText(mValues.get(position).createDate);
        //holder.mDeliverDateView.setText(mValues.get(position).deliverDate);
        holder.mEcartDateView.setText(mValues.get(position).ecartDate);

        // set backgroundcolor
        if (Constantes.State.NONCONFORME == mValues.get(position).finalState) {
            // NON CONFORME
            holder.mLinearLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
        } else {
            // CONFORME
            holder.mLinearLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout mLinearLayout;
        public final TextView mIdView;
        public final TextView mNameView;
        public final TextView mCreateDateView;
        public final TextView mDeliverDateView;
        public final TextView mEcartDateView;
        public DetailItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLinearLayout = (LinearLayout)view.findViewById(R.id.linearRow);
            mIdView = (TextView) view.findViewById(R.id.id);
            mNameView = (TextView) view.findViewById(R.id.name);
            mCreateDateView = (TextView) view.findViewById(R.id.create);
            mDeliverDateView = (TextView) view.findViewById(R.id.deliver);
            mEcartDateView = (TextView) view.findViewById(R.id.ecart);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
