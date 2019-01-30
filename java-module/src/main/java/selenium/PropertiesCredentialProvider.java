package selenium;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesCredentialProvider implements CredentialProvider {

    Properties prop = new Properties();

    public PropertiesCredentialProvider() {
        InputStream stream = this.getClass().getResourceAsStream("test.properties");
        try {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getId() {
        return prop.getProperty("id");
    }

    @Override
    public String getPassword() {
        return prop.getProperty("password");
    }
}
