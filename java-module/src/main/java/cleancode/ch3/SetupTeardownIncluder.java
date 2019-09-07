package cleancode.ch3;

import cleancode.ch3.helpers.PageData;

public class SetupTeardownIncluder {

    private PageData pageData;
    private boolean isSuite;

    public static String render(PageData pageData) {
        return render(pageData, false);
    }

    public static String render(PageData pageData, boolean isSuite) {
        return new SetupTeardownIncluder(pageData).render(isSuite);
    }

    private SetupTeardownIncluder (PageData pageData) {
        this.pageData = pageData;
    }

    private String render(boolean isSuite) {
        this.isSuite = isSuite;
        return "";
    }



}
