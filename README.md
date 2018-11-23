A very simple example for recyclerview actions (on swipe - show buttons at right side)<br/>
You can customize the size of buttons, their icon and the amount of the buttons.<br/>

Code example: 
init:

```
  private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager((Context) this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(new MyAdapter(mItems));

        SwipeController SwipeController = new SwipeController((Context) this, SwipeController.BUTTON_WIDTH_NORMAL, (SwipeController.IActionsCallBack) this);
        
        
// Add Buttons
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

```
listener:

```
    @Override
    public void onActionsClicked(int actionsId, int adapterPosition) {
        if (actionsId == BT_1_ID) {
            Toast.makeText(MainActivity.this, "share: element #" + (int) mItems.get(adapterPosition), Toast.LENGTH_SHORT).show();
        }
        if (actionsId == BT_2_ID) {
            Toast.makeText(MainActivity.this, "delete: element #" + (int) mItems.get(adapterPosition), Toast.LENGTH_SHORT).show();
        }
        if (actionsId == BT_3_ID) {
            Toast.makeText(MainActivity.this, "comment: element # " + (int) mItems.get(adapterPosition), Toast.LENGTH_SHORT).show();
        }
    }
```





<img style="border: 1px solid black;float:left;" src="https://github.com/davidHarush/recyclerview_swipe_actions/blob/master/Screenshot_1.png" width="250" height="auto" hspace="30" />

<br/>
<br/>
Base on https://github.com/FanFataL/swipe-controller-demo
