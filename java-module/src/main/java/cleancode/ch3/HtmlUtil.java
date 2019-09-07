package cleancode.ch3;

import cleancode.ch3.helpers.PageCrawlerImpl;
import cleancode.ch3.helpers.PageData;
import cleancode.ch3.helpers.PathParser;
import cleancode.ch3.helpers.SuiteResponder;
import cleancode.ch3.helpers.WikiPage;
import cleancode.ch3.helpers.WikiPagePath;

public class HtmlUtil {

    public static String testableHtml(PageData pageData, boolean includeSuiteSetup)
            throws Exception {

        WikiPage wikiPage = pageData.getWikiPage();
        StringBuffer buffer = new StringBuffer();

        if (pageData.hasAttribute("Test")) {
            if (includeSuiteSetup) {
                WikiPage suiteSetup = PageCrawlerImpl
                        .getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);

                if (suiteSetup != null) {
                    WikiPagePath pagePath = suiteSetup.getPageCrawler().getFullPath(suiteSetup);
                    String pagePathName = PathParser.render(pagePath);
                    buffer.append("!include -setup .")
                            .append(pagePathName)
                            .append("\n");
                }
            }
            WikiPage setup = PageCrawlerImpl.getInheritedPage("Setup", wikiPage);
            if (setup != null) {
                WikiPagePath setupPath = wikiPage.getPageCrawler().getFullPath(setup);
                String setupPathName = PathParser.render(setupPath);
                buffer.append("!include -setup .")
                        .append(setupPathName)
                        .append("\n");
            }
        }

        buffer.append(pageData.getContent());

        if (pageData.hasAttribute("Test")) {
            WikiPage tearDown = PageCrawlerImpl.getInheritedPage("TearDown", wikiPage);
            if (tearDown != null) {
                WikiPagePath tearDownPath = wikiPage.getPageCrawler().getFullPath(tearDown);
                String tearDownPathName = PathParser.render(tearDownPath);
                buffer.append("\n")
                        .append("!include -teardown .")
                        .append(tearDownPathName)
                        .append("\n");

                if (includeSuiteSetup) {
                    WikiPage suiteTearDown = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
                    if (suiteTearDown != null) {
                        WikiPagePath pagePath = suiteTearDown.getPageCrawler().getFullPath(suiteTearDown);
                        String pagePathName = PathParser.render(pagePath);
                        buffer.append("!include -teardown .")
                                .append(pagePathName)
                                .append("\n");
                    }
                }
            }
        }

        pageData.setContent(buffer.toString());
        return pageData.getHtml();
    }
}

