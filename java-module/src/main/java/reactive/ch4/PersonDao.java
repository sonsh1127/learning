package reactive.ch4;

import static io.reactivex.Observable.defer;

import io.reactivex.Observable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PersonDao {

    private static final int PAGE_SIZE = 1;

    Observable<Person> listPeople() {
        return defer(() -> Observable.fromIterable(query("Select * from people")));
    }

    List<Person> listPeople(int page) {
        return query("SELECT * FROM PEOPLE ORDER BY ID LIMIT ? OFFSET ?", PAGE_SIZE,
                page * PAGE_SIZE);
    }

    private List<Person> query(String sql, int pageSize, int offset) {
        System.out.println(
                "Doc query executed " + sql + " pageSize " + pageSize + " offset " + offset);
        if (offset >= 5) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(new Person("nins" + offset));
        }
    }

    private List<Person> query(String sql) {
        System.out.println("query executed " + sql);
        return Collections.emptyList();
    }

    Observable<Person> allPerople(int initialPage) {
        return defer(() -> Observable.fromIterable(listPeople(initialPage)))
                .concatWith(defer(() -> allPerople(initialPage + 1)));
    }


}
