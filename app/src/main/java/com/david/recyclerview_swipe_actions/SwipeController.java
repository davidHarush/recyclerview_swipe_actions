package com.david.recyclerview_swipe_actions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class SwipeController extends Callback {

    private static final int BUTTONS_STATE_GONE = 0;
    private static final int BUTTONS_STATE_RIGHT_VISIBLE = 1;
    //    private static final int BUTTONS_STATE_LEFT_VISIBLE = 2;
    public static final int BUTTON_WIDTH_BIG = 320;
    public static final int BUTTON_WIDTH_NORMAL = 230;
    public static final int BUTTON_WIDTH_SMALL = 150;

    private int buttonMargin = 20;
    private boolean swipeBack = false;
    private int buttonShowedState = BUTTONS_STATE_GONE;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private SwipeControllerActions buttonsActions = null;
    private ArrayList<Button> mRightButtons;
    private float mButtonWidth;
    private float edgesOfButtons;
    private Context mContext;

    public SwipeController(Context context, int mButtonWidth, SwipeControllerActions buttonsActions) {
        this.mButtonWidth = mButtonWidth;
        this.buttonsActions = buttonsActions;
        this.mContext = context;
        buttonMargin = toDp(buttonMargin);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != BUTTONS_STATE_GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != BUTTONS_STATE_GONE) {
                if (buttonShowedState == BUTTONS_STATE_RIGHT_VISIBLE) {
                    dX = Math.min(dX, -edgesOfButtons);
                }
//                else if (buttonShowedState == BUTTONS_STATE_LEFT_VISIBLE){
//                    dX = Math.min(dX, edgesOfButtons);
//                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == BUTTONS_STATE_GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -edgesOfButtons) {
                        buttonShowedState = BUTTONS_STATE_RIGHT_VISIBLE;
                    }
//                    else if (dX > edgesOfButtons){
//                        buttonShowedState = BUTTONS_STATE_LEFT_VISIBLE;
//                    }


                    if (buttonShowedState != BUTTONS_STATE_GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;

                    for (Button bt : mRightButtons) {
                        if (buttonsActions != null && buttonInstance != null && bt.buttonPosition.contains(event.getX(), event.getY())) {

                            if (buttonShowedState == BUTTONS_STATE_RIGHT_VISIBLE) {
                                buttonsActions.onActionsClicked(bt.actionsId, viewHolder.getAdapterPosition());
                            }

                        }
                    }
                    buttonShowedState = BUTTONS_STATE_GONE;
                    currentItemViewHolder = null;
                }
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    private void drawRightButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        mButtonWidth = toDp((int) mButtonWidth);
        buttonMargin = toDp((int) buttonMargin);

        float buttonWidthWithoutMargin = mButtonWidth - buttonMargin;
        float corners = 25;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        RectF rectButton = null;
        int lastRight = itemView.getRight() - buttonMargin;
        int lastLeft = (int) (itemView.getRight() - mButtonWidth);

        for (int i = 0; i < mRightButtons.size(); i++) {

            rectButton = new RectF(lastLeft, itemView.getTop(), lastRight, itemView.getBottom());

            lastRight = lastLeft - buttonMargin;
            lastLeft = (int) (lastLeft - mButtonWidth);

            p.setColor(mRightButtons.get(i).backgroundColor);
            c.drawRoundRect(rectButton, corners, corners, p);
            mRightButtons.get(i).buttonPosition = rectButton;

            int rectLength = (int) Math.min(rectButton.height(), rectButton.width());
            int halfSize = toDp((int) (rectLength / mRightButtons.get(i).size));
            int centreX = (int) rectButton.centerX();
            int centreY = (int) rectButton.centerY();

            mRightButtons.get(i).icon.setBounds(toDp(centreX - halfSize), toDp(centreY - halfSize), toDp(centreX + halfSize), toDp(centreY + halfSize));
            mRightButtons.get(i).icon.draw(c);
        }

        buttonInstance = null;
        lastLeft = (int) (lastLeft - buttonWidthWithoutMargin);

        if (buttonShowedState == BUTTONS_STATE_RIGHT_VISIBLE) {
            buttonInstance = new RectF(lastLeft, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }
    }

    private void drawLeftButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {

        mButtonWidth = toDp((int) mButtonWidth);
        buttonMargin = toDp((int) buttonMargin);

        float buttonWidthWithoutMargin = mButtonWidth - buttonMargin;
        float corners = 25;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        RectF rectButton = null;

        int lastLeft = (int) (itemView.getLeft());
        int lastRight = (int) (lastLeft + mButtonWidth);


        for (int i = 0; i < mRightButtons.size(); i++) {

            rectButton = new RectF(lastLeft, itemView.getTop(), lastRight, itemView.getBottom());

            lastLeft = (int) (lastLeft + mButtonWidth);
            lastRight = lastLeft + buttonMargin;

//            lastRight = lastLeft - buttonMargin;
//            lastLeft = (int) (lastLeft - mButtonWidth);

            p.setColor(mRightButtons.get(i).backgroundColor);
            c.drawRoundRect(rectButton, corners, corners, p);
            mRightButtons.get(i).buttonPosition = rectButton;

            int rectLength = (int) Math.min(rectButton.height(), rectButton.width());
            int halfSize = toDp((int) (rectLength / mRightButtons.get(i).size));
            int centreX = (int) rectButton.centerX();
            int centreY = (int) rectButton.centerY();

            mRightButtons.get(i).icon.setBounds(toDp(centreX - halfSize), toDp(centreY - halfSize), toDp(centreX + halfSize), toDp(centreY + halfSize));
            mRightButtons.get(i).icon.draw(c);
        }

        buttonInstance = null;
        lastLeft = (int) (lastLeft - buttonWidthWithoutMargin);

        if (buttonShowedState == BUTTONS_STATE_RIGHT_VISIBLE) {
            buttonInstance = new RectF(lastLeft, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }
    }


    private int toDp(int px) {
        return px;
//        Resources resources = mContext.getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//        return (int) dp;
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), p);
    }

    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
//            if(buttonShowedState == BUTTONS_STATE_RIGHT_VISIBLE) {
            drawRightButtons(c, currentItemViewHolder);
//            }else{
//                drawLeftButtons(c, currentItemViewHolder);
//            }
        }
    }

    public void setButton(ArrayList<Button> rightButtons) {
        mRightButtons = rightButtons;
        edgesOfButtons = (mRightButtons.size() * mButtonWidth) + 20;

    }

    static class Button {

        public static final float ICON_SIZE_BIG = (float) 2.5;
        public static final float ICON_SIZE_NORMAL = (float) 4.5;
        public static final float ICON_SIZE_SMALL = 7;
        RectF buttonPosition;
        int actionsId;
        Drawable icon;
        int backgroundColor;
        float size;

        public Button(int actionsId, Drawable icon, float size, int backgroundColor) {
            this.actionsId = actionsId;
            this.icon = icon;
            this.size = size;
            this.backgroundColor = backgroundColor;

        }
    }

    public interface SwipeControllerActions {
        void onActionsClicked(int actionsId, int adapterPosition);
    }

}

