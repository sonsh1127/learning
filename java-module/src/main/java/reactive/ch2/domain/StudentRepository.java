package reactive.ch2.domain;

public interface StudentRepository {
    Student findById(Long id);
}
