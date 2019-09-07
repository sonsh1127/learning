package cleancode.ch3.helpers;

public class WikiPage {

    private PageCrawler pageCrawler;

    public WikiPage(PageCrawler pageCrawler) {
        this.pageCrawler = pageCrawler;
    }

    public PageCrawler getPageCrawler() {
        return pageCrawler;
    }
}
