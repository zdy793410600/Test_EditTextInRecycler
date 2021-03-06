package com.example.zhaodanyang.testedittextinrecyclerview;

import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiSelectRecyclerAdapter<T> extends BaseArrayRecyclerAdapter<T> {

    private final SparseBooleanArray selectedArray = new SparseBooleanArray();

    /**
     * 获取选中状态的列表
     *
     * @return
     */
    public SparseBooleanArray getSelectedArray() {
        return selectedArray;
    }

    private boolean selectable;//是否可选

    public MultiSelectRecyclerAdapter(boolean selectable) {
        this.selectable = selectable;
    }

    public MultiSelectRecyclerAdapter() {
    }

    public boolean isSelectable() {
        return selectable;
    }

    /**
     * 设置可选择性
     *
     * @param selectable
     */
    public void setSelectable(boolean selectable) {
        if (this.selectable != selectable) {
            this.selectable = selectable;
            if (!isSelectable()) {//不可选择的 默认清除上次选中的 子类可以复写
                clearSelected();
            } else {
                this.notifyDataSetChanged();
            }
        }
    }

    /**
     * 清除选中的
     */
    public void clearSelected() {
        selectedArray.clear();
        this.notifyDataSetChanged();
    }

    /**
     * 选择全部
     */
    public void selectAll() {
        selectedArray.clear();
        for (int i = 0; i < getItemCount(); i++) {
            selectedArray.put(i, true);
        }
        this.notifyDataSetChanged();
    }

    /**
     * 获取选中的items 位置
     *
     * @return
     */
    public List<Integer> getSelectedPositions() {
        List<Integer> selected = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            boolean itemSelected = selectedArray.get(i, false);
            if (itemSelected) {
                selected.add(i);
            }
        }
        return selected;
    }

    @Override
    public boolean removeItem(int position) {
        selectedArray.delete(position);
        return super.removeItem(position);
    }

    @Override
    public boolean removeItem(T t) {
        int indexOf = getData().indexOf(t);
        if (indexOf >= 0) {
            selectedArray.delete(indexOf);
        }
        return super.removeItem(t);
    }

    /**
     * 获取选中的数据
     *
     * @return
     */
    @NonNull
    public ArrayList<T> getSelectedData() {
        ArrayList<T> selectedArray = new ArrayList<>();
        List<Integer> selectedData = getSelectedPositions();
        for (int i = 0; i < selectedData.size(); i++) {
            T item = getItem(selectedData.get(i));
            if (item != null) {
                selectedArray.add(item);
            }
        }
        return selectedArray;
    }


    /**
     * 选中和取消
     *
     * @param pos
     * @param selected
     * @return
     */
    public boolean setSelected(int pos, boolean selected) {
        if (pos >= 0 && pos < getItemCount()) {
            boolean itemSelected = selectedArray.get(pos, false);
            if (selected != itemSelected) {
                selectedArray.put(pos, selected);
                this.notifyItemChanged(pos);
                return true;
            }
        }
        return false;
    }

    /**
     * 反选
     *
     * @param pos
     * @return
     */
    public boolean toggleSelected(int pos) {
        int headerCount = getParentHeaderFooterAdapter() != null ? getParentHeaderFooterAdapter().getHeaderCount() : 0;
        if (pos >= 0 && pos < getItemCount() + headerCount) {
            boolean itemSelected = selectedArray.get(pos - headerCount, false);
            selectedArray.put(pos - headerCount, !itemSelected);
            this.notifyItemChanged(pos);
            return true;
        }
        return false;
    }


    /**
     * 反选  不更新UI
     *
     * @param pos
     * @return
     */
    public boolean toggleSelectedWithoutUpdateUI(int pos) {
        if (pos >= 0 && pos < getItemCount()) {
            boolean itemSelected = selectedArray.get(pos, false);
            selectedArray.put(pos, !itemSelected);
            return true;
        }
        return false;
    }


    /**
     * 是否选中
     *
     * @param pos
     * @return
     */
    public boolean isSelected(int pos) {
        if (pos >= 0 && pos < getItemCount()) {
            return selectedArray.get(pos, false);
        }
        return false;
    }


    /**
     * 是否全部选中
     *
     * @return
     */
    public boolean isAllSelected() {
        if (selectedArray.size() < getItemCount()) {
            return false;
        } else {
            for (int i = 0; i < getItemCount(); i++) {
                boolean itemSelected = selectedArray.get(i, false);
                if (!itemSelected) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public final void onBindHoder(ViewHolder holder, T t, int position) {
        onBindSelectableHolder(holder, t, isSelected(position), position);
    }

    public abstract void onBindSelectableHolder(ViewHolder holder, T t, boolean selected, int position);
}
