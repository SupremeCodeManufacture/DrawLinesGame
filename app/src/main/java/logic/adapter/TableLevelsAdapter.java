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

import java.util.ArrayList;
import java.util.List;

import data.App;
import data.DataWrapperObj;
import data.LevelObj;

public class TableLevelsAdapter extends RecyclerView.Adapter<TableLevelsAdapter.ViewHolder> {

    private LevelObj[] mData;
    private OnTableLevelSelectedListener mOnTableLevelSelectedListener;
    private List<String> passedLevels = new ArrayList<>();
    private List<String> lockedLevels = new ArrayList<>();
    private String mTableTypeId;
    private boolean mHasLockRestrictions;


    public TableLevelsAdapter(DataWrapperObj dataWrapperObj, String tableTypeId, OnTableLevelSelectedListener listener) {
        this.mData = dataWrapperObj.getLevelObjs();
        this.mTableTypeId = tableTypeId;
        this.mOnTableLevelSelectedListener = listener;
        this.mHasLockRestrictions = !App.isPaidUnlockLvls() && !App.isPaidFull();

        if (dataWrapperObj.getPassedLevels() != null)
            this.passedLevels.addAll(dataWrapperObj.getPassedLevels());

        if (dataWrapperObj.getLockedLevels() != null)
            this.lockedLevels.addAll(dataWrapperObj.getLockedLevels());
    }

    public void updateViews(){
        this.notifyDataSetChanged();
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

        if (mHasLockRestrictions && lockedLevels.contains(mTableTypeId + "," + tableObj.getLevelId())) {
            viewHolder.ivStatusLocked.setVisibility(View.VISIBLE);

        } else {
            viewHolder.ivStatusLocked.setVisibility(View.GONE);

            if (passedLevels.contains(mTableTypeId + "," + tableObj.getLevelId())) {
                viewHolder.ivStatusPassed.setVisibility(View.VISIBLE);

            } else {
                viewHolder.ivStatusPassed.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout llWholeItm;
        TextView tvTitle;
        ImageView ivStatusPassed, ivStatusLocked;

        ViewHolder(final View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_txt);
            ivStatusPassed = (ImageView) itemView.findViewById(R.id.ic_status_passed);
            ivStatusLocked = (ImageView) itemView.findViewById(R.id.ic_status_locked);

            llWholeItm = (RelativeLayout) itemView.findViewById(R.id.rl_itm);
            llWholeItm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION && mOnTableLevelSelectedListener != null) {
                switch (view.getId()) {
                    case R.id.rl_itm:
                        LevelObj tableObj = mData[clickedPosition];
                        String levelId = mData[clickedPosition].getLevelId();

                        if (mHasLockRestrictions && lockedLevels.contains(mTableTypeId + "," + tableObj.getLevelId())) {
                            mOnTableLevelSelectedListener.onLockedLevelSelected();

                        } else {
                            mOnTableLevelSelectedListener.oTableLevelSelected(levelId);
                        }

                        break;
                }
            }
        }
    }
}