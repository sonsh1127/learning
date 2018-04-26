package pattern.asyncmethod;

import java.util.Optional;

public interface AsyncCallback<T> {
    void onComplete(T result, Optional<Exception> exception);
}
