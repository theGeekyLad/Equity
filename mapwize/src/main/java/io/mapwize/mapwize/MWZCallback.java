package io.mapwize.mapwize;

public interface MWZCallback<T> {

    void onSuccess(T object);
    void onFailure(Throwable t);

}
