package com.laughingFace.microWash.ui.view;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by mathcoder on 2/04/15.
 */
public class DeviceSpinner extends LinearLayout {
    public DeviceSpinner(Context context) {
        super(context);
    }


//    private TextView tvName;//默认显示下拉框名字。
//    View item;//选择界面。
//    ListView lvItem;
//    ImageView ivAdd;//添加设备
//    ItemAdapter mItemAdapter;//列表适配器
//    List<DeviceTab> itemData;//列表数据
//    DeviceTab currentDevice;
//    PopupWindow popupWindow;//弹出框。
//    Context mContext;
//    private OnListener mClickListener;
//    int maxHeight = 400;//注意这里的单位是px,应该是dp.
//
//    public DeviceSpinner(Context context) {
//        super(context);
//        mContext = context;
//        init();
//    }
//
//    public DeviceSpinner(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mContext = context;
//        init();
//
//    }
//
//    public void init()
//    {
//        DisplayMetrics dm=getResources().getDisplayMetrics();
//        BaseInfo.screenWidth=dm.widthPixels;//屏幕像素宽度
//        BaseInfo.screenHeight=dm.heightPixels;//屏幕像素高度
//        Log.i("xixi","屏幕像素高度:"+BaseInfo.screenHeight);
//        //初始化选项控件
//        LayoutInflater iInflater = LayoutInflater.from(mContext);
//        tvName = (TextView) iInflater.inflate(R.layout.select_name, null);//选择的文本
//
//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showItem();
//            }
//        });
//        this.setGravity(Gravity.CENTER);
//        this.addView(tvName);
//
////        int sdk = android.os.Build.VERSION.SDK_INT;
////        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
////            this.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.select_bg));
////
////        } else {
////            this.setBackground(mContext.getResources().getDrawable(R.drawable.select_bg));
////        }
//
//        //初始化选择项控件。
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        item = inflater.inflate(R.layout.select_itemlist,null);
//
//        lvItem = (ListView) item.findViewById(R.id.select_item);
//        ivAdd = (ImageView) item.findViewById(R.id.item_add);
//        itemData = new ArrayList<DeviceTab>();
////        itemData.add("洗衣机");
////        itemData.add("洗衣机");
//        mItemAdapter = new ItemAdapter(mContext);
//        mItemAdapter.setData(itemData);
//        lvItem.setDividerHeight(0);
//        lvItem.setAdapter(mItemAdapter);
//        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                if (null != mClickListener)
//                {
//                    setCurrentDevice(itemData.get(position));
//                    mClickListener.changeSelect(itemData.get(position));
//                }
//                if(null != popupWindow)
//                    popupWindow.dismiss();
//
//            }
//        });
//
//    }
//    /**
//     * 设置添加设备监听
//     * @param listener
//     */
//    public void setAddOnClickListener(OnClickListener listener)
//    {
//        if (null != ivAdd)
//        ivAdd.setOnClickListener(listener);
//    }
//    public void addItemData(DeviceTab item)
//    {
//        if (null != itemData) {
//            for (int i = itemData.size() - 1; i >= 0;i--)
//            {
//                if (itemData.get(i).compareTo(item) == 0)
//                {
//                    itemData.remove(i);
//                    break;
//                }
//            }
//        }
//        itemData.add(item);
//        if (null != mItemAdapter)
//        mItemAdapter.setData(itemData);
//
//    }
//    public void clearItemData()
//    {
//        if (null != itemData)
//        itemData.clear();
//    }
//    public void notifyChangeItemData(OnListener listener)
//    {
//        this.mClickListener = listener;
//    }
//    /**
//     * 项列表数据
//     */
//    public void setItemData(List<DeviceTab> itemData)
//    {
//        this.itemData = itemData;
//    }
//    /**
//     * 显示选择项。
//     */
//    private void showItem()
//    {
//        mClickListener.onClick();
////初始化弹出框
//
//        popupWindow = new PopupWindow(item,
//                this.getWidth(), LayoutParams.WRAP_CONTENT, true);
////        popupWindow.setTouchable(true);
////        popupWindow.setFocusable(true);
////        popupWindow.setTouchInterceptor(new OnTouchListener() {
////
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////
////                Log.i("mengdd", "onTouch : ");
////
////                return false;
////                // 这里如果返回true的话，touch事件将被拦截
////                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
////            }
////        });
//        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//        // 我觉得这里是API的一个bug
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.tran));
////        popupWindow.getContentView().measure(0, 0);
////        Log.i("haha", "x:" + this.getWidth() + "," + this.getX() + ",y:" + this.getHeight() + "," + this.getY());
////        Log.i("haha","width:"+BaseInfo.screenWidth+",height:"+ BaseInfo.screenHeight);
////        // 设置好参数之后再show
////        Log.i("haha", "size:" + popupWindow.getContentView().getMeasuredHeight());
////        if (itemData.size() > 3)
////        {
////            lvItem.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,maxHeight));
////        }
////        else
////        {
////            lvItem.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,0,1));
////
////        }
//
////        if (BaseInfo.screenHeight - this.getY() - this.getHeight() < maxHeight)
////        {
////        int[] location = new int[2];
////        this.getLocationOnScreen(location);
////        popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getContentView().getMeasuredHeight());
////        }
////        else {
//            popupWindow.showAsDropDown(this);
////            popupWindow.showAtLocation(this,Gravity.BOTTOM,0,0);
////        }
////        popupWindow.showAtLocation(tvName,Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,0,0);
//    }
//    public void dismiss()
//    {
//        if (null != popupWindow)
//            popupWindow.dismiss();
//    }
//    /**
//     * 绑定数据，刷新数据源。
//     */
//    public void setCurrentDevice(DeviceTab device)
//    {
//        if (null == device)
//            return;
//        currentDevice = device;
//        tvName.setText(StringUtil.isEmpty(currentDevice.getName())?currentDevice.getDefault_Name():currentDevice.getName());
//    }
//    public DeviceTab getCurrentDevice()
//    {
//        return currentDevice;
//    }
//    private class ItemAdapter extends BaseAdapter
//    {
//        Context mContext ;
//        List<DeviceTab> mData;
//        LayoutInflater mInflater ;
//        public ItemAdapter(Context context)
//        {
//            this.mContext = context;
//            mInflater = LayoutInflater.from(mContext);
//        }
//        public void setData(List<DeviceTab> data)
//        {
//            this.mData = data;
//            notifyDataSetChanged();
//        }
//        @Override
//        public int getCount() {
//            return mData.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mData.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup parent) {
////            Log.i("haha",position+","+mData.get(position));
//            Holder holder = null;
//            if (view == null)
//            {
//                holder = new Holder();
//                view = mInflater.inflate(R.layout.select_item,null);
//                holder.btnDeviceName = (TextView) view.findViewById(R.id.btn_item_name);
//                view.setTag(holder);
//            }
//            else {
//                holder = (Holder) view.getTag();
//            }
//
//            holder.btnDeviceName.setText(StringUtil.isEmpty(mData.get(position).getName())?
//                    mData.get(position).getDefault_Name():mData.get(position).getName());
//
//            return view;
//        }
//        private class Holder {
//            TextView btnDeviceName;
//        }
//    }
//
//    public TextView getTvName() {
//        return tvName;
//    }
//
//    public void setTvName(TextView tvName) {
//        this.tvName = tvName;
//    }
//
//    public interface OnListener{
//        public void onClick();
//        public void changeSelect(DeviceTab text);
//    }
}
