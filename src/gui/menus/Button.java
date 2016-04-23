package gui.menus;

import gui.fontMeshCreator.GUIText;
import input.MouseButtonInput;
import input.MousePosInput;
import org.joml.Vector2f;
import utils.MathUtils;
import utils.ResourceHandler;

// Handles GUI buttons, when instantiating, override the "callback" method
// Then call update every frame to ensure no callback is missed
// NOTE: ONLY INSTANTIATE VIA ANONUMOUS INNER CLASS INSTANTIATION, e.g.
// Button button = new Button("foo", pos) {
//              @Override
//              public void callback() {
//                  // Do stuff
//              }
//             };

public abstract class Button {
    private String text;                // Displayed text on the button
    private Vector2f position;          // Position given in GUI coordinates
    private float width = 0.1f;         // Width in GUI coords
    private float height = 0.05f;       // Height in GUI coords
    private boolean pressing = false;   // If we have pressed down on the button but not let go yet
    protected GUIText guiText;               // Handle to the text

    public Button(String text, Vector2f position) {
        this.text = text;
        this.position = position;
        setupText();
    }

    public Button(String text, Vector2f position, float width, float height) {
        this.text = text;
        this.position = position;
        this.width = width;
        this.height = height;
        setupText();
    }

    private void setupText() {
        guiText = new GUIText(this.text, 1f, ResourceHandler.font, this.position, this.width, true);
        guiText.setColour(1f, 1f, 1f);
    }

    public abstract void callback();    // This needs to be implemented when instantiating

    public void update() {
        // This method does some juggling to avoid the mouse down spam

        Vector2f mouseGUI = MathUtils.screenSpaceToGUI(MousePosInput.getPosition());
        // Check if mouse 1 is down
        if (MouseButtonInput.isMouseLeftDown()) {
            // Now we need to know if it is within this buttons bounding box
            if (MathUtils.isWithinGuiBox(mouseGUI, position.x, position.y, width, height) && !pressing) {
                pressing = true;
            }
        }
        // Else, if left is up
        else if (pressing && MathUtils.isWithinGuiBox(mouseGUI, position.x, position.y, width, height)) {
            pressing = false;
            callback();
        }
        // Are we hovering over the button?
        else if (MathUtils.isWithinGuiBox(mouseGUI, position.x, position.y, width, height)) {
            guiText.setRenderParams(0.3f, 0.3f, 0.6f, 0.4f);
        }
        // Not hovering, clicking or anything else
        else {
            pressing = false;
            guiText.setRenderParams(0.4f, 0.3f, 0.6f, 0.4f);
        }
    }

    public void remove() {
        guiText.remove();
    }
}
