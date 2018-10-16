package concurrent;

import org.junit.Assert;
import org.junit.Test;
import selenium.CredentialProvider;
import selenium.PropertiesCredentialProvider;

public class PropertiesCredentialProviderTest {

    private CredentialProvider credentialProvider = new PropertiesCredentialProvider();

    @Test
    public void get() {
        Assert.assertEquals("myid", credentialProvider.getId());
    }

}
