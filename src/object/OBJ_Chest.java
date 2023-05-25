package object;

import javax.imageio.ImageIO;
import java.io.IOException;

// chest object class extending the super object class
public class OBJ_Chest extends SuperObject {
    public OBJ_Chest() {
        name = "Chest";
        try {
            // get the image and store it in image (a buffered image var)
            image = ImageIO.read(getClass().getResourceAsStream("/objects/chest.png"));
        }
        catch (IOException e) {
            // print the error if there is one
            e.printStackTrace();
        }
    }
}
