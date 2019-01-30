package concurrent.ch1.wordcount;

public class WikiDoc extends Doc {

    private String title;
    private String content;

    public WikiDoc(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getText() {
        return content;
    }

    @Override
    public String toString() {
        return "WikiDoc{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
