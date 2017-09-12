/*
 *  This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

   Also(warning!):
 
  1)You are not allowed to sell this product to third party.
  2)You can't change license and made it like you are the owner,author etc.
  3)All redistributions of source code files must contain all copyright
     notices that are currently in this file, and this list of conditions without
     modification.
 */

package marquee;

import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * When the screen element is not big enough to show the text then an animation will start automatically
 * 
 * @author GOXR3PLUS
 *
 */
public class Marquee extends Pane {

    @FXML
    private Text text;

    // minimum distance to Pane bounds
    private static final double OFFSET = 5;

    private Timeline timeline = new Timeline();

    /**
     * Constructor
     */
    public Marquee() {

	// FXMLLOADER
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Marquee.fxml"));
	    loader.setController(this);
	    loader.setRoot(this);
	    loader.load();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}

    }

    /**
     * Called as soon as .fxml is initialized [[SuppressWarningsSpartan]]
     */
    @FXML
    private void initialize() {

	// Pane

	// Text
	text.setManaged(false);

	startAnimation();
    }

    /**
     * This method changes the text of the Marquee
     * 
     * @param value
     * @return this
     */
    public Marquee setText(String value) {

	// text
	// text.setStyle("-fx-font-size:12px !important;")
	//text.setTextAlignment(TextAlignment.RIGHT)
	text.setText(value);

	return this;
    }

    /**
     * Defines text string that is to be displayed.
     * 
     * @return The TextProperty
     */
    public StringProperty textProperty() {
	return text.textProperty();
    }

    /**
     * This method starts the Animation of the marquee
     */
    private final void startAnimation() {

	// KeyFrame
	KeyFrame updateFrame = new KeyFrame(Duration.millis(35), new EventHandler<ActionEvent>() {

	    private boolean rightMovement;

	    @Override
	    public void handle(ActionEvent event) {
		double textWidth = text.getLayoutBounds().getWidth();
		double paneWidth = getWidth();
		double layoutX = text.getLayoutX();

		if (2 * OFFSET + textWidth <= paneWidth && layoutX >= OFFSET) {
		    // stop, if the pane is large enough and the position is
		    // correct
		    text.setLayoutX(OFFSET);
		    timeline.stop();
		} else {
		    if ((rightMovement && layoutX >= OFFSET)
			    || (!rightMovement && layoutX + textWidth + OFFSET <= paneWidth)) {
			// invert movement, if bounds are reached
			rightMovement = !rightMovement;
		    }

		    // update position
		    if (rightMovement) {
			layoutX += 1;
		    } else {
			layoutX -= 1;
		    }
		    text.setLayoutX(layoutX);
		}
	    }
	});
	timeline.getKeyFrames().add(updateFrame);
	timeline.setCycleCount(Animation.INDEFINITE);

	// listen to bound changes of the elements to start/stop the
	// animation
	InvalidationListener listener = o -> {
	    double textWidth = text.getLayoutBounds().getWidth();
	    double paneWidth = getWidth();
	    if (textWidth + 2 * OFFSET > paneWidth && timeline.getStatus() != Animation.Status.RUNNING)
		timeline.play();
	};

	text.layoutBoundsProperty().addListener(listener);
	widthProperty().addListener(listener);

    }

}
