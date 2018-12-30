package concurrent.ch1.wordcount;


abstract class Page {
    public String getTitle() { throw new UnsupportedOperationException(); }
    public String getText() { throw new UnsupportedOperationException(); }
    public boolean isPoisonPill() { return false; }
}
