package com.example.BDMap;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.ontheway.R;
/**
 * Created by DLX on 2015/4/28.
 * 自定义adapter，需要实现Filterable接口
 */
public class MyAutoCompleteTextViewAdapter extends BaseAdapter implements Filterable {
    //listView需要的数据
    private List<MyCompleteTextViewInfo> data;
    //未经分类的数据的list
    private ArrayList<MyCompleteTextViewInfo> unFilteredData;
    //上下文
    private Context context;
    //自定义的Filter
    private MyFileter myFileter;
    public MyAutoCompleteTextViewAdapter(Context context, List<MyCompleteTextViewInfo> list) {
        this.context = context;
        data = list;
    }
    
    public List<MyCompleteTextViewInfo> getList(){
    	return data;
    }
    
    public void AddAll(List<MyCompleteTextViewInfo> listx){
    	data.addAll(listx);
    	notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public MyCompleteTextViewInfo getItem(int i) {
        return data.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(context).inflate(R.layout.auto_item, null);
            view.setTag(new ListCell((TextView) view.findViewById(R.id.tvTitle), (TextView) view.findViewById(R.id.tvDes)));
        }
        MyCompleteTextViewInfo info = getItem(i);
        ListCell lc = (ListCell) view.getTag();
        lc.getTv_title().setText(info.getTitle());
        lc.getTv_des().setText(info.getDes());
        return view;
    }
    /**
     * 获得Filter，是接口中的抽象函数
     * @return
     */
    @Override
    public Filter getFilter() {
        if (myFileter == null) {
            myFileter = new MyFileter();
        }
        return myFileter;
    }
    private class ListCell {
        private TextView tv_title, tv_des;
        public ListCell(TextView tv_title, TextView tv_des) {
            this.tv_title = tv_title;
            this.tv_des = tv_des;
        }
        public TextView getTv_title() {
            return tv_title;
        }
        public TextView getTv_des() {
            return tv_des;
        }
    }
    /**
     * 自己实现Filter，不实现的话会导致不能setAdapter
     */
    private class MyFileter extends Filter {
        /**
         * prefix是指筛选规则
         * @param prefix
         * @return  FilterResult对象，包含object和count属性，其中object用于传递ArrayList对象
         */
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (unFilteredData == null) {
                unFilteredData = new ArrayList<MyCompleteTextViewInfo>(data);
            }
            //如果查询规则为空，则直接将没有过滤的list穿回去，这里使用一个新的本地变量
            if (prefix == null) {
                ArrayList<MyCompleteTextViewInfo> list = unFilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                //筛选规则不为空，这里面的逻辑需要自己实现，这里实现的是如果筛选出由prefixString开头的
                String prefixString = prefix.toString().toLowerCase();
                int count = unFilteredData.size();
                ArrayList<MyCompleteTextViewInfo> lists = new ArrayList<MyCompleteTextViewInfo>();
                MyCompleteTextViewInfo infox;
                for (int i = 0; i < count; i++) {
                    infox = unFilteredData.get(i);
                    if (infox != null) {
                        if (infox.getTitle() != null && infox.getTitle().startsWith(prefixString)) {
                            lists.add(infox);
                        }
                    }
                }
                results.values = lists;
                results.count = lists.size();
            }
            return results;
        }
        /**
         * protected FilterResults performFiltering()方法的结果会传递到这个函数中
         * @param charSequence  筛选规则
         * @param filterResults 筛选结果
         */
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data = (List<MyCompleteTextViewInfo>) filterResults.values;
            if (filterResults.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}