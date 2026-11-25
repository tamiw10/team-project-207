package interface_adapter.create_trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import use_case.create_trip.CreateTripInputBoundary;
import use_case.create_trip.CreateTripInputData;

class CreateTripControllerTest {

  @Test
  void splitsFriendsOnCommasAndDropsEmptyEntries() {
    assertEquals(
        Arrays.asList("amelia", "jadyn"), CreateTripController.splitUsernames("amelia, jadyn"));
    assertEquals(Arrays.asList("a", "b"), CreateTripController.splitUsernames(",a,, b ,"));
    assertEquals(Collections.emptyList(), CreateTripController.splitUsernames("  "));
    assertEquals(Collections.emptyList(), CreateTripController.splitUsernames(null));
  }

  @Test
  void passesFormFieldsAndNavigationToTheUseCase() {
    final boolean[] calls = new boolean[3];
    final CreateTripInputBoundary interactor =
        new CreateTripInputBoundary() {
          @Override
          public void execute(CreateTripInputData inputData) {
            calls[0] = true;
          }

          @Override
          public void openForm() {
            calls[1] = true;
          }

          @Override
          public void switchToHomeView() {
            calls[2] = true;
          }
        };
    final CreateTripController controller = new CreateTripController(interactor);

    controller.execute("Trip", "Tokyo", "2027-02-13", "2027-02-19", "CAD", "250", "amelia");
    controller.openForm();
    controller.switchToHomeView();

    assertTrue(calls[0] && calls[1] && calls[2]);
  }
}
