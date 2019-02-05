package selenium.javafx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import selenium.ChromeDriverSrtTicketing;

public class ReservationController implements Initializable {

    @FXML
    private TextField id2;

    @FXML
    private PasswordField password;

    @FXML
    private TextField departure;

    @FXML
    private TextField arrival;

    @FXML
    private TextField departDate;

    @FXML
    private TextField departTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleReservation(ActionEvent e) {
        new Thread(() -> {
            ChromeDriverSrtTicketing ticketing = new ChromeDriverSrtTicketing();
            String id = id2.getText();
            String password1 = password.getText();

            ticketing.login(id, password1);
            ticketing.tryReservation(departure.getText(), arrival.getText(), departDate.getText(),
                    departTime.getText());
        }).start();
    }
}
