package concurrent.ch1.wordcount;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class Docs implements Iterable<Doc> {

    private int maxDocs;
    private String fileName;

    public Docs(int maxDocs, String fileName) {
        this.maxDocs = maxDocs;
        this.fileName = fileName;
    }

    @Override
    public Iterator<Doc> iterator() {
        return new WikiDocIterator();
    }

    class WikiDocIterator implements Iterator<Doc> {

        private XMLEventReader reader;
        private int remainingPages;
        private XMLEvent eventFromHasNext;

        public WikiDocIterator() {
            remainingPages = maxDocs;
            try {
                reader = XMLInputFactory.newInstance()
                        .createXMLEventReader(new FileInputStream(fileName));
            } catch (XMLStreamException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean hasNext() {

            while (true) {
                try {
                    this.eventFromHasNext = reader.nextEvent();
                    if (eventFromHasNext.isEndDocument()) {
                        break;
                    } else if (eventFromHasNext.isStartElement() && eventFromHasNext
                            .asStartElement()
                            .getName()
                            .getLocalPart().equals("doc")) {
                        break;
                    }
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }

            return remainingPages > 0 && !eventFromHasNext.isEndDocument();
        }

        @Override
        public Doc next() {
            return handleDoc();
        }

        private Doc handleDoc() {
            String title = "";
            String content = "";
            while (true) {
                try {
                    XMLEvent xmlEvent = reader.nextEvent();
                    if (xmlEvent.isEndElement() && xmlEvent.asEndElement().getName().getLocalPart()
                            .equals("doc")) {
                        this.remainingPages--;
                        return new WikiDoc(title, content);
                    } else if (xmlEvent.isStartElement()) {
                        String name = xmlEvent.asStartElement().getName().getLocalPart();
                        if ("title".equals(name)) {
                            title = reader.getElementText();
                        } else if ("abstract".equals(name)) {
                            content = reader.getElementText();
                        }
                    }
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void remove() {

        }

        @Override
        public void forEachRemaining(Consumer<? super Doc> action) {

        }
    }

    @Override
    public void forEach(Consumer<? super Doc> action) {
        Iterator<Doc> iterator = iterator();
        while (iterator.hasNext()) {
            action.accept(iterator.next());
        }
    }

    @Override
    public Spliterator<Doc> spliterator() {
        return null;
    }
}
