package concurrent.ch1.wordcount;

import static org.junit.Assert.*;

import java.util.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocsTest {

    private Docs docs;

    @Before
    public void init() {
        this.docs = new Docs(2, "testdocs.xml");
    }

    @Test
    public void testReadDocs_Iterator() {
        Iterator<Doc> iterator = docs.iterator();
        workWithDocs(iterator);
    }

    private void workWithDocs(Iterator<Doc> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            Doc next = iterator.next();
            System.out.println(next);
            count++;
        }
        Assert.assertEquals(2, count);
    }

    @Test
    public void testReadDoc_LessThanMaxDocs() {
        Docs docs = new Docs(3, "testdocs.xml");
        workWithDocs(docs.iterator());
    }

    @Test
    public void testReadDocs_foreach() {
        docs.forEach(
                System.out::println
        );
    }

}