package com.david.recyclerview_swipe_actions;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int BT_1_ID = 1;
    public static final int BT_2_ID = 2;
    public static final int BT_3_ID = 3;

    private SwipeController mSwipeController = null;
    private List<Integer> mItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataSet();
        setupRecyclerView();
    }


    private void initDataSet() {
        mItems = new ArrayList<>();
        for (int i = 0; i < 56; i++) {
            mItems.add(i);
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(new MyAdapter(mItems));

        mSwipeController = new SwipeController(this, SwipeController.BUTTON_WIDTH_NORMAL, new SwipeController.SwipeControllerActions() {
            @Override
            public void onActionsClicked(int actionsId, int adapterPosition) {
                if (actionsId == BT_1_ID) {
                    Toast.makeText(MainActivity.this, "share: element #" + (int) mItems.get(adapterPosition), Toast.LENGTH_SHORT).show();
                }
                if (actionsId == BT_2_ID) {
                    Toast.makeText(MainActivity.this, "delete: element #" + (int) mItems.get(adapterPosition), Toast.LENGTH_SHORT).show();
                }
                if (actionsId == BT_3_ID) {
                    Toast.makeText(MainActivity.this, "comment: element #+ " + (int) mItems.get(adapterPosition), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ArrayList<SwipeController.Button> buttons = new ArrayList<>();

        buttons.add(new SwipeController.Button(BT_1_ID, getResources().getDrawable(R.drawable.vector_share), SwipeController.Button.ICON_SIZE_NORMAL, 0x88B0BBBB));
        buttons.add(new SwipeController.Button(BT_2_ID, getResources().getDrawable(R.drawable.vector_delete), SwipeController.Button.ICON_SIZE_NORMAL, 0x88BBBBB0));
        buttons.add(new SwipeController.Button(BT_3_ID, getResources().getDrawable(R.drawable.vector_comment), SwipeController.Button.ICON_SIZE_NORMAL, 0x88BBB0BB));


        mSwipeController.setButton(buttons);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(mSwipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                mSwipeController.onDraw(c);
            }
        });
    }
}
