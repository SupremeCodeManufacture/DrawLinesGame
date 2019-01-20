package logic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.SupremeManufacture.DrawLines.R;

import java.util.List;

import data.LevelObj;
import logic.helpers.Utils;

public class TableLevelsAdapter extends RecyclerView.Adapter<TableLevelsAdapter.ViewHolder> {

    private LevelObj[] mData;
    private OnTableLevelSelectedListener mOnTableLevelSelectedListener;
    private List<String> finishedLevels = Utils.getFinishedLevelsList();
    private String mTableTypeId;


    public TableLevelsAdapter(LevelObj[] array, String tableTypeId, OnTableLevelSelectedListener listener) {
        this.mData = array;
        this.mTableTypeId = tableTypeId;
        this.mOnTableLevelSelectedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_level_itm, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        LevelObj tableObj = mData[position];

        viewHolder.tvTitle.setText(tableObj.getLevelName());

        if (finishedLevels.contains(mTableTypeId + "," + tableObj.getLevelId())) {
            viewHolder.ivStatus.setVisibility(View.VISIBLE);

        } else {
            viewHolder.ivStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout llWholeItm;
        TextView tvTitle;
        ImageView ivStatus;

        ViewHolder(final View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_txt);
            ivStatus = (ImageView) itemView.findViewById(R.id.ic_status);

            llWholeItm = (RelativeLayout) itemView.findViewById(R.id.rl_itm);
            llWholeItm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION && mOnTableLevelSelectedListener != null) {
                switch (view.getId()) {
                    case R.id.rl_itm:
                        mOnTableLevelSelectedListener.oTableLevelSelected(mData[clickedPosition].getLevelId());
                        break;
                }
            }
        }
    }
}