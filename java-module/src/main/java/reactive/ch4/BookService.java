package reactive.ch4;

import io.reactivex.Observable;

class BookService {

    public void bestBookFor(Person person) {
        Book book;
        try {
            book = recommend(person);
        } catch (Exception e) {
            book = bestSeller();
        }
        display(book.getTitle());
    }

    public void bestBook2(Person person) {
        rxRecommend(person)
                .onErrorResumeNext(rxBestSeller())
                .map(Book::getTitle)
                .subscribe(this::display);
    }

    private Observable<Book> rxBestSeller() {
        return Observable.just(bestSeller());
    }

    private Book bestSeller() {
        return new Book("Nins");
    }

    private void display(String title) {
        System.out.println(title);
    }

    private Observable<Book> rxRecommend(Person person) {
        return Observable.just(recommend(person));
    }

    private Book recommend(Person person) {
        return new Book(person.name);
    }
}
