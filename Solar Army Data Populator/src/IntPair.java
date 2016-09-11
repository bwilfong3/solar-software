class IntPair {
    int p1, p2;
    IntPair(int p1, int p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntPair)) return false;
        IntPair key = (IntPair) o;
        return p1 == key.p1 && p2 == key.p2;
    }
    
    @Override
    public int hashCode() {
        int result = p1;
        result = 31 * result + p2;
        return result;
    }
}