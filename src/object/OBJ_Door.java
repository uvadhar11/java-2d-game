package object;

import javax.imageio.ImageIO;
import java.io.IOException;

// door object class extending the super object class
public class OBJ_Door extends SuperObject {
    public OBJ_Door() {
        name = "Door";
        try {
            // get the image and store it in image (a buffered image var)
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
        }
        catch (IOException e) {
            // print the error if there is one
            e.printStackTrace();
        }
    }
}
