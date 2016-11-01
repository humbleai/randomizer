package com.humbleai.randomizer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;


public class SetItemAdapter extends RecyclerView.Adapter<SetItemAdapter.ViewHolder> {

    private final List<SetItem> mDataset;
    private final Context mContext;
    private final int screenHeight;
    private int currentColor;
    private final SharedPreferences settings;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView mCard;
        public final TextView mTextViewTitle;
        public final ImageView mImageViewItemIcon;
        public ImageButton mImageButton;
        public RelativeLayout animatedLayout;

        public ViewHolder(View v, int viewType) {
            super(v);


            switch (viewType) {
                case 1: // sonuc
                    animatedLayout = (RelativeLayout) v.findViewById(R.id.animatedLayout);
                    mTextViewTitle = (TextView) v.findViewById(R.id.textViewTitle_item);
                    mImageViewItemIcon = (ImageView) v.findViewById(R.id.imageViewItemIcon);
                    break;
                default: //liste

                    mCard = (CardView) v.findViewById(R.id.card_view_view_set);
                    mTextViewTitle = (TextView) v.findViewById(R.id.textViewTitle_item);
                    mImageViewItemIcon = (ImageView) v.findViewById(R.id.imageViewItemIcon);
                    mImageButton = (ImageButton) v.findViewById(R.id.imageButtonDeleteItem);

            }


        }


    }


    public SetItemAdapter(List<SetItem> mDataset, Context mContext ) {
        this.mDataset = mDataset;
        this.mContext = mContext;

        final TypedArray styledAttributes = mContext.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        final int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        final float density = mContext.getResources().getDisplayMetrics().density;
        styledAttributes.recycle();
        screenHeight = Math.round(Resources.getSystem().getDisplayMetrics().heightPixels - density * mActionBarSize + mContext.getResources().getDimension(R.dimen.fab_margin));

        settings = mContext.getSharedPreferences(ScrollingActivity.PREFS_NAME, 0);
    }


    @Override
    public SetItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v ;

        switch (viewType) {
            case 1: // sonuc
                v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.stack_item, parent, false);
                if (settings.getBoolean("action_settings_animations_list", false)) {
                    v.setAlpha(0);
                    v.animate().setDuration(1000);
                    v.animate().alpha(1);
                }

                break;
            default: //liste
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycle_set_item, parent, false);
        }

        return new ViewHolder(v, viewType);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
       final SetItem setItem = mDataset.get(position);

        switch (getItemViewType(position)) {
            case 1: // sonuc

                holder.animatedLayout.getLayoutParams().height = screenHeight;
                holder.mImageViewItemIcon.setMaxHeight(screenHeight);

                holder.mImageViewItemIcon.setImageResource(setItem.getIcon());


                final float textSize = (200f / setItem.getTitle().length()) + 20;
                holder.mTextViewTitle.setTextSize(textSize);
                if ( mContext.getResources().getResourceTypeName(setItem.getIcon()).equals("color")) {
                    holder.mTextViewTitle.setTextColor(colorInvert(setItem.getIcon()));
                    holder.mImageViewItemIcon.setImageResource(setItem.getIcon());
                }
                holder.mTextViewTitle.setText(setItem.getTitle());
                if (settings.getBoolean("action_settings_animations_bg", false)) {
                    holder.mImageViewItemIcon.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.result_anim));
                }


                if (settings.getBoolean("action_settings_animations_text", false)) {
                    holder.mTextViewTitle.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.text_anim));
                }


                break;
            default: //liste

                float itemAlfa = 1f;
                if (setItem.getExcluded() == 1) itemAlfa = 0.2f;

                if (settings.getBoolean("action_settings_animations_list", false)) {
                    holder.mCard.setAlpha(0);
                    holder.mCard.animate().setDuration(800);
                    holder.mCard.animate().alpha(itemAlfa);
                } else {
                    holder.mCard.setAlpha(itemAlfa);
                }

                holder.mTextViewTitle.setText(setItem.getTitle());


                if ( mContext.getResources().getResourceTypeName(setItem.getIcon()).equals("color")) {
                    holder.mImageViewItemIcon.setImageResource(setItem.getIcon());
                } else {
                    holder.mImageViewItemIcon.setImageBitmap(
                            decodeSampledBitmapFromResource(mContext.getResources(), setItem.getIcon(), 48, 48));
                }

                holder.mCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                        final View promptView = layoutInflater.inflate(R.layout.input_dialog_item_preview, null, false);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setView(promptView);

                        TextView aTextViewTitle = (TextView) promptView.findViewById(R.id.textViewTitle_item);
                        ImageView aImageViewItemIcon = (ImageView) promptView.findViewById(R.id.imageViewItemIcon);

                        if ( mContext.getResources().getResourceTypeName(setItem.getIcon()).equals("drawable")) {
                            aImageViewItemIcon.setPadding(20, 20, 20, 0);
                        }
                        if ( mContext.getResources().getResourceTypeName(setItem.getIcon()).equals("color")) {
                             aTextViewTitle.setTextColor(colorInvert(setItem.getIcon()));
                        }


                        float textSize = (200f / setItem.getTitle().length()) + 20;

                        aTextViewTitle.setTextSize(textSize);

                        aImageViewItemIcon.setImageResource(setItem.getIcon());
                        aTextViewTitle.setText(setItem.getTitle());

                        alertDialogBuilder.setCancelable(true).setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alertDialogBuilder.create();
                        alert.show();


                    }
                });

                holder.mImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popup = new PopupMenu(mContext, v);
                        if (setItem.getSetType().equals("user"))
                            popup.getMenu().add(R.string.delete);
                        if (setItem.getSetType().equals("user")) popup.getMenu().add(R.string.edit);
                        if (setItem.getExcluded() == 1) popup.getMenu().add(R.string.include);
                        if (setItem.getExcluded() == 0) popup.getMenu().add(R.string.exclude);
                        final SetItemSQLiteHelper db = new SetItemSQLiteHelper(mContext);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if (item.getTitle().toString().contentEquals(mContext.getString(R.string.delete))) {

                                    db.deleteSetItem(setItem);
                                    // db.close();
                                    mDataset.remove(setItem);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());

                                }

                                if (item.getTitle().toString().contentEquals(mContext.getString(R.string.exclude)) || item.getTitle().toString().contentEquals(mContext.getString(R.string.include))) {
                                    if (setItem.getExcluded() == 1) {
                                        setItem.setExcluded(0);
                                    } else {
                                        setItem.setExcluded(1);
                                    }

                                    if (db.updateSetItem(setItem) > 0) {

                                        mDataset.set(position, setItem);
                                        notifyItemChanged(position);
                                    } else {
                                        Toast toast = Toast.makeText(mContext, R.string.newListFail, Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    // db.close();

                                }

                                if (item.getTitle().toString().contentEquals(mContext.getString(R.string.edit))) {

                                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                                    final View promptView = layoutInflater.inflate(R.layout.input_dialog, null, false);
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                                    alertDialogBuilder.setView(promptView);

                                    final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
                                    editText.setText(setItem.getTitle());

                                    final TextView textViewHead = (TextView) promptView.findViewById(R.id.textView);
                                    textViewHead.setText(R.string.edit);

                                    final ImageView mImageViewNewIcon = (ImageView) promptView.findViewById(R.id.imageViewNewItemIcon);

                                    currentColor = setItem.getIcon();
                                    mImageViewNewIcon.setImageResource(currentColor);

                                    mImageViewNewIcon.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TypedArray colors_icons = mContext.getResources().obtainTypedArray(R.array.colors_icons);
                                            final Random random = new Random();
                                            final int randColor = random.nextInt(colors_icons.length());
                                            currentColor = colors_icons.getResourceId(randColor, R.color.colorMainBg);
                                            colors_icons.recycle();
                                            mImageViewNewIcon.setImageResource(currentColor);
                                        }
                                    });

                                    alertDialogBuilder.setCancelable(true)
                                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int id) {

                                                    if (TextUtils.isEmpty(editText.getText()))
                                                        editText.setText(R.string.untitled);

                                                    setItem.setIcon(currentColor);
                                                    setItem.setTitle(editText.getText().toString());

                                                    int inserted = db.updateSetItem(setItem);
                                                    // db.close();

                                                    if (inserted == -1) {
                                                        Toast toast = Toast.makeText(mContext, R.string.newListFail, Toast.LENGTH_SHORT);
                                                        toast.show();
                                                    } else {
                                                        notifyItemChanged(position, setItem);

                                                        Toast toast = Toast.makeText(mContext, R.string.editListSuccess, Toast.LENGTH_SHORT);
                                                        toast.show();
                                                    }


                                                }
                                            })
                                            .setNegativeButton(R.string.cancel,
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                    // create an alert dialog
                                    AlertDialog alert = alertDialogBuilder.create();
                                    alert.show();

                                }

                                return false;
                            }
                        });
                        popup.show();
                    }
                });


        }





    }

    @Override
    public int getItemViewType(int position) {

        SetItem setItem = mDataset.get(position);
        return setItem.getItemViewType();
    }

    private int colorInvert(int renk) {
        final int a = ContextCompat.getColor(mContext, renk);
        final String b = String.format("%06X", (0xFFFFFF & a));
        final int c = 0xFFFFFF - Integer.parseInt(b, 16);

        return Color.parseColor(String.format("#%06X", 0xFFFFFF & c));
    }


    @Override
    public int getItemCount() {

        return (null != mDataset ? mDataset.size() : 0);
    }


    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}