package com.tooneCode.services.model;

import lombok.Generated;

public class TypingStat {
    double avgTypingSpeed;
    double lastTypingSpeed;
    long typingLength;
    String lastTypedChars;
    String currentTypedChars;
    Integer typedCharRowDiff;

    @Generated
    public double getAvgTypingSpeed() {
        return this.avgTypingSpeed;
    }

    @Generated
    public double getLastTypingSpeed() {
        return this.lastTypingSpeed;
    }

    @Generated
    public long getTypingLength() {
        return this.typingLength;
    }

    @Generated
    public String getLastTypedChars() {
        return this.lastTypedChars;
    }

    @Generated
    public String getCurrentTypedChars() {
        return this.currentTypedChars;
    }

    @Generated
    public Integer getTypedCharRowDiff() {
        return this.typedCharRowDiff;
    }

    @Generated
    public void setAvgTypingSpeed(double avgTypingSpeed) {
        this.avgTypingSpeed = avgTypingSpeed;
    }

    @Generated
    public void setLastTypingSpeed(double lastTypingSpeed) {
        this.lastTypingSpeed = lastTypingSpeed;
    }

    @Generated
    public void setTypingLength(long typingLength) {
        this.typingLength = typingLength;
    }

    @Generated
    public void setLastTypedChars(String lastTypedChars) {
        this.lastTypedChars = lastTypedChars;
    }

    @Generated
    public void setCurrentTypedChars(String currentTypedChars) {
        this.currentTypedChars = currentTypedChars;
    }

    @Generated
    public void setTypedCharRowDiff(Integer typedCharRowDiff) {
        this.typedCharRowDiff = typedCharRowDiff;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TypingStat)) {
            return false;
        } else {
            TypingStat other = (TypingStat) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (Double.compare(this.getAvgTypingSpeed(), other.getAvgTypingSpeed()) != 0) {
                return false;
            } else if (Double.compare(this.getLastTypingSpeed(), other.getLastTypingSpeed()) != 0) {
                return false;
            } else if (this.getTypingLength() != other.getTypingLength()) {
                return false;
            } else {
                label54:
                {
                    Object this$lastTypedChars = this.getLastTypedChars();
                    Object other$lastTypedChars = other.getLastTypedChars();
                    if (this$lastTypedChars == null) {
                        if (other$lastTypedChars == null) {
                            break label54;
                        }
                    } else if (this$lastTypedChars.equals(other$lastTypedChars)) {
                        break label54;
                    }

                    return false;
                }

                Object this$currentTypedChars = this.getCurrentTypedChars();
                Object other$currentTypedChars = other.getCurrentTypedChars();
                if (this$currentTypedChars == null) {
                    if (other$currentTypedChars != null) {
                        return false;
                    }
                } else if (!this$currentTypedChars.equals(other$currentTypedChars)) {
                    return false;
                }

                Object this$typedCharRowDiff = this.getTypedCharRowDiff();
                Object other$typedCharRowDiff = other.getTypedCharRowDiff();
                if (this$typedCharRowDiff == null) {
                    if (other$typedCharRowDiff != null) {
                        return false;
                    }
                } else if (!this$typedCharRowDiff.equals(other$typedCharRowDiff)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TypingStat;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        long $avgTypingSpeed = Double.doubleToLongBits(this.getAvgTypingSpeed());
        result = result * 59 + (int) ($avgTypingSpeed >>> 32 ^ $avgTypingSpeed);
        long $lastTypingSpeed = Double.doubleToLongBits(this.getLastTypingSpeed());
        result = result * 59 + (int) ($lastTypingSpeed >>> 32 ^ $lastTypingSpeed);
        long $typingLength = this.getTypingLength();
        result = result * 59 + (int) ($typingLength >>> 32 ^ $typingLength);
        Object $lastTypedChars = this.getLastTypedChars();
        result = result * 59 + ($lastTypedChars == null ? 43 : $lastTypedChars.hashCode());
        Object $currentTypedChars = this.getCurrentTypedChars();
        result = result * 59 + ($currentTypedChars == null ? 43 : $currentTypedChars.hashCode());
        Object $typedCharRowDiff = this.getTypedCharRowDiff();
        result = result * 59 + ($typedCharRowDiff == null ? 43 : $typedCharRowDiff.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        double var10000 = this.getAvgTypingSpeed();
        return "TypingStat(avgTypingSpeed=" + var10000 + ", lastTypingSpeed=" + this.getLastTypingSpeed() + ", typingLength=" + this.getTypingLength() + ", lastTypedChars=" + this.getLastTypedChars() + ", currentTypedChars=" + this.getCurrentTypedChars() + ", typedCharRowDiff=" + this.getTypedCharRowDiff() + ")";
    }

    @Generated
    public TypingStat() {
    }

    @Generated
    public TypingStat(double avgTypingSpeed, double lastTypingSpeed, long typingLength, String lastTypedChars, String currentTypedChars, Integer typedCharRowDiff) {
        this.avgTypingSpeed = avgTypingSpeed;
        this.lastTypingSpeed = lastTypingSpeed;
        this.typingLength = typingLength;
        this.lastTypedChars = lastTypedChars;
        this.currentTypedChars = currentTypedChars;
        this.typedCharRowDiff = typedCharRowDiff;
    }
}

