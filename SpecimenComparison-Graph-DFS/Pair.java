import java.util.Objects;

public class Pair {

    private int x, y;
    
    public Pair(int x, int y) {
        this.x = Math.min(x, y);
        this.y = Math.max(x, y);
    }
    
    public int x() { return x; }
    public int y() { return y; }
    
    @Override
    public boolean equals(Object other) {
        
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        
        Pair p = (Pair) other;
        
        return x == p.x && y == p.y;
    }

    @Override    
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return String.format("(%d, %d)", x(), y());
    }
}

