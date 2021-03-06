package reactive.ch2.domain;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MemoryStudentRepository implements StudentRepository {

    private ConcurrentMap<Long, Student> students = new ConcurrentHashMap<>();

    @Override
    public Student findById(Long id) {
        return students.get(id);
    }
}
