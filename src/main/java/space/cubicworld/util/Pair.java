package space.cubicworld.util;

import lombok.Data;

@Data
public class Pair<T, K> {

    private T key;
    private K value;

    public Pair(T key, K value){
        setKey(key);
        setValue(value);
    }

}
