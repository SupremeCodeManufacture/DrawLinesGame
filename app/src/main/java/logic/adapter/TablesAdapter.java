package logic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.SupremeManufacture.DrawLines.R;

import data.TableObj;
import logic.helpers.Utils;

public class TablesAdapter extends RecyclerView.Adapter<TablesAdapter.ViewHolder> {

    private TableObj[] mData;
    private OnTableSelectedListener mOnTableSelectedListener;


    public TablesAdapter(TableObj[] tableObjs, OnTableSelectedListener listener) {
        this.mData = tableObjs;
        this.mOnTableSelectedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_type_itm, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        TableObj tableObj = mData[position];

        viewHolder.tvTitle.setText(Utils.getTableName(position));
        viewHolder.tvDescr.setText(tableObj.getTabDescr());
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout llWholeItm;
        TextView tvTitle, tvDescr;

        ViewHolder(final View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_name);
            tvDescr = (TextView) itemView.findViewById(R.id.tv_descr);

            llWholeItm = (RelativeLayout) itemView.findViewById(R.id.rl_zone);
            llWholeItm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION && mOnTableSelectedListener != null) {
                switch (view.getId()) {
                    case R.id.rl_zone:
                        mOnTableSelectedListener.oTableSelected(mData[clickedPosition].getTabId());
                        break;
                }
            }
        }
    }
}