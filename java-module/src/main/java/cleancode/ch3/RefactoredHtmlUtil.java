package cleancode.ch3;

import cleancode.ch3.helpers.PageCrawlerImpl;
import cleancode.ch3.helpers.PageData;
import cleancode.ch3.helpers.PathParser;
import cleancode.ch3.helpers.SuiteResponder;
import cleancode.ch3.helpers.WikiPage;
import cleancode.ch3.helpers.WikiPagePath;

public class RefactoredHtmlUtil {

    private PageData pageData;
    private boolean isSuite;
    private WikiPage wikiPage = pageData.getWikiPage();

    private RefactoredHtmlUtil(PageData pageData) {
        this.pageData = pageData;
    }

    public static String testableHtml(PageData pageData, boolean includeSuiteSetup) {
        return new RefactoredHtmlUtil(pageData).render(includeSuiteSetup);
    }

    private String render(boolean isSuite) {
        this.isSuite = isSuite;
        if (isTest(pageData)) {
            String setUp = includeSetup();
            String body = includeBody();
            String tearDown = includeTearDown();
            pageData.setContent(setUp + body + tearDown);
        }

        return pageData.getHtml();
    }

    private String includeSetup() {
        String setup = "";
        if (isSuite) {
            setup += includeSuiteSetup();
        }
        setup += includeSetup2();
        return setup;
    }

    private String includeSetup2() {
        WikiPage setup = PageCrawlerImpl.getInheritedPage("Setup", wikiPage);
        if (setup != null) {
            String setupPathName = getPagePath(setup);
            return String.format("!include -setup %s \n", setupPathName);
        }
        return "";
    }

    private String includeSuiteSetup() {
        WikiPage suiteSetup = PageCrawlerImpl
                .getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
        if (suiteSetup != null) {
            String pagePathName = getPagePath(suiteSetup);
            return String.format("!include -setup %s \n", pagePathName);
        }

        return "";
    }

    private String getPagePath(WikiPage suiteSetup) {
        WikiPagePath pagePath = suiteSetup.getPageCrawler().getFullPath(suiteSetup);
        return PathParser.render(pagePath);
    }

    private String includeBody() {
        return pageData.getContent();
    }

    private String includeTearDown() {
        String content = "";
        WikiPage tearDown = PageCrawlerImpl.getInheritedPage("TearDown", wikiPage);
        if (tearDown != null) {
            content += includeTearDown2(tearDown);
            if (isSuite) {
                content += includeSuiteTeardown();
            }
        }
        return content;
    }

    private String includeTearDown2(WikiPage tearDown) {
        String tearDownPathName = getPagePath(tearDown);
        return String.format("\n !include -teardown . %s\n", tearDownPathName);
    }

    private String includeSuiteTeardown() {
        WikiPage suiteTearDown = PageCrawlerImpl
                .getInheritedPage(SuiteResponder.SUITE_TEARDOWN_NAME, wikiPage);
        if (suiteTearDown != null) {
            String pagePathName = getPagePath(suiteTearDown);
            return String.format("!include -teardown. %s \n", pagePathName);
        }
        return "";
    }

    private static boolean isTest(PageData pageData) {
        return pageData.hasAttribute("Test");
    }
}
