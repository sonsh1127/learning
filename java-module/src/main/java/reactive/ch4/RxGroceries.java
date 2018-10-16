package reactive.ch4;

import io.reactivex.Observable;
import java.math.BigDecimal;
import reactive.common.LogUtil;

class RxGroceries {

    Observable<BigDecimal> purchase(String productName, int quantity) {
        return Observable.fromCallable(() -> doPurchase(productName, quantity));
    }

    BigDecimal doPurchase(String productName, int quantity) {
        LogUtil.log("Purchasing " + quantity + " " + productName);
        sleep(quantity);
        LogUtil.log("Done " + quantity + " " + productName);
        return new BigDecimal(quantity * productName.length());
    }

    private void sleep(int quantity) {
        try {
            Thread.sleep(1000 + quantity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
