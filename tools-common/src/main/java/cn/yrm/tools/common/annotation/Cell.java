package cn.yrm.tools.common.annotation;

public enum Cell {
    STRING(1),
    IMAGE(2);
    Cell(int value) {
        this.value = value;
    }
    private int value;

    public int getValue() {
        return value;
    }

    public boolean equals(Integer value) {
        return value != null && this.getValue() == value;
    }
}
