package com.tooneCode.editor.model;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CodeEditorInlayList {
    List<CodeEditorInlayItem> items;
    int selectIndex;
    String prefix;
    Lock lock = new ReentrantLock();

    public void add(CodeEditorInlayItem item) {
        this.lock.lock();

        try {
            if (this.items == null) {
                this.items = new ArrayList();
            }

            this.selectIndex = this.items.size();
            item.setRankIndex(this.selectIndex);
            this.items.add(item);
            String batchId = ((CodeEditorInlayItem) this.items.get(0)).getBatchId();
            if (StringUtils.isBlank(batchId)) {
                batchId = ((CodeEditorInlayItem) this.items.get(0)).getRequestId();
            }

            item.setBatchId(batchId);
        } finally {
            this.lock.unlock();
        }

    }

    public void replace(int index, CodeEditorInlayItem item) {
        this.lock.lock();

        try {
            if (this.items != null && index >= 0 && index < this.items.size()) {
                CodeEditorInlayItem oldItem = (CodeEditorInlayItem) this.items.get(index);
                if (oldItem != null) {
                    item.setBatchId(oldItem.getBatchId());
                    item.setRendered(oldItem.isRendered());
                    item.setFirstDisplayTimeMs(oldItem.getFirstDisplayTimeMs());
                    item.setDisplayTimeMs(oldItem.getDisplayTimeMs());
                }

                item.setRankIndex(index);
                this.items.set(index, item);
            }
        } finally {
            this.lock.unlock();
        }

    }

    public CodeEditorInlayItem select(int index) {
        this.lock.lock();

        Object var2;
        try {
            if (this.items != null && !this.items.isEmpty()) {
                if (index < 0) {
                    index += this.items.size();
                } else if (index >= this.items.size()) {
                    index -= this.items.size();
                }

                this.selectIndex = index;
                return this.getCurrentInlayItem();
            }

            this.selectIndex = -1;
            var2 = null;
        } finally {
            this.lock.unlock();
        }

        return (CodeEditorInlayItem) var2;
    }

    public CodeEditorInlayItem getCurrentInlayItem() {
        this.lock.lock();

        CodeEditorInlayItem var1;
        try {
            if (this.items != null && this.selectIndex >= 0 && this.selectIndex < this.items.size()) {
                var1 = (CodeEditorInlayItem) this.items.get(this.selectIndex);
                return var1;
            }

            var1 = null;
        } finally {
            this.lock.unlock();
        }

        return var1;
    }

    public boolean isEmpty() {
        this.lock.lock();

        boolean var1;
        try {
            var1 = this.items == null || this.items.isEmpty();
        } finally {
            this.lock.unlock();
        }

        return var1;
    }

    public int size() {
        this.lock.lock();

        int var1;
        try {
            var1 = this.items == null ? 0 : this.items.size();
        } finally {
            this.lock.unlock();
        }

        return var1;
    }

    public List<CodeEditorInlayItem> getItems() {
        return this.items;
    }

    public int getSelectIndex() {
        return this.selectIndex;
    }

    
    public String getPrefix() {
        return this.prefix;
    }

    
    public Lock getLock() {
        return this.lock;
    }

    
    public void setItems(List<CodeEditorInlayItem> items) {
        this.items = items;
    }

    
    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    
    public void setLock(Lock lock) {
        this.lock = lock;
    }

    
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CodeEditorInlayList)) {
            return false;
        } else {
            CodeEditorInlayList other = (CodeEditorInlayList) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$items = this.getItems();
                Object other$items = other.getItems();
                if (this$items == null) {
                    if (other$items != null) {
                        return false;
                    }
                } else if (!this$items.equals(other$items)) {
                    return false;
                }

                if (this.getSelectIndex() != other.getSelectIndex()) {
                    return false;
                } else {
                    Object this$prefix = this.getPrefix();
                    Object other$prefix = other.getPrefix();
                    if (this$prefix == null) {
                        if (other$prefix != null) {
                            return false;
                        }
                    } else if (!this$prefix.equals(other$prefix)) {
                        return false;
                    }

                    Object this$lock = this.getLock();
                    Object other$lock = other.getLock();
                    if (this$lock == null) {
                        if (other$lock != null) {
                            return false;
                        }
                    } else if (!this$lock.equals(other$lock)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    
    protected boolean canEqual(Object other) {
        return other instanceof CodeEditorInlayList;
    }

    
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $items = this.getItems();
        result = result * 59 + ($items == null ? 43 : $items.hashCode());
        result = result * 59 + this.getSelectIndex();
        Object $prefix = this.getPrefix();
        result = result * 59 + ($prefix == null ? 43 : $prefix.hashCode());
        Object $lock = this.getLock();
        result = result * 59 + ($lock == null ? 43 : $lock.hashCode());
        return result;
    }

    
    public String toString() {
        List var10000 = this.getItems();
        return "CodeEditorInlayList(items=" + var10000 + ", selectIndex=" + this.getSelectIndex() + ", prefix=" + this.getPrefix() + ", lock=" + this.getLock() + ")";
    }

    
    public CodeEditorInlayList() {
    }

    
    public CodeEditorInlayList(List<CodeEditorInlayItem> items, int selectIndex, String prefix, Lock lock) {
        this.items = items;
        this.selectIndex = selectIndex;
        this.prefix = prefix;
        this.lock = lock;
    }
}
