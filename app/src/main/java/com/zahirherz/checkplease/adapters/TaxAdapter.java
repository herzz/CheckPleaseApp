package com.zahirherz.checkplease.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zahirherz.checkplease.R;
import com.zahirherz.checkplease.taxinfo.Tax;

/**
 * Created by zahirh on 11/2/15.
 */
public class TaxAdapter extends RecyclerView.Adapter<TaxAdapter.TaxViewHolder> {

    private Tax mTaxes;
    private Context mContext;

    public TaxAdapter(Context context, Tax taxes) {
        mContext = context;
        mTaxes = taxes;
    }

    @Override
    public void onBindViewHolder(TaxAdapter.TaxViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // Called whenever a new viewHolder is needed
    @Override
    public TaxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create root view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tax_list_item, parent, false);

        TaxViewHolder viewHolder = new TaxViewHolder(view);
        return viewHolder;
    }

    public class TaxViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

        public TextView mTaxLabel;

        public TaxViewHolder(View itemView) {
            super(itemView);
            mTaxLabel = (TextView) itemView.findViewById(R.id.taxLabel);
        }

        public void bindTax(Tax tax) {
            //mTaxLabel.setText(tax.getRate() + "");
        }
        @Override
        public void onClick(View v) {

        }
    }
}
