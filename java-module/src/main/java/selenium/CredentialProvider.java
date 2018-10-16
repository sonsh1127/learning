package selenium;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public interface CredentialProvider {

    String getId();

    String getPassword();
}


