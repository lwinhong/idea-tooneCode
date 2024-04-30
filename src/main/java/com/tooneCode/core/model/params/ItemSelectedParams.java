package com.tooneCode.core.model.params;

import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

public class ItemSelectedParams {
    String id;
    int index;
    String uri;

    public ItemSelectedParams() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Pure
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this);
        b.add("id", this.id);
        b.add("index", this.index);
        b.add("uri", this.uri);
        return b.toString();
    }

    @Pure
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else if (!super.equals(obj)) {
            return false;
        } else {
            ItemSelectedParams other = (ItemSelectedParams)obj;
            return this.id == other.id;
        }
    }

    @Pure
    public int hashCode() {
        return 31 * super.hashCode();
    }
}

